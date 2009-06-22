package org.jdesktop.application;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

public class TaskService extends AbstractBean {
    private final String name;
    private final ExecutorService executorService;
    private final List<Task> tasks;
    private final PropertyChangeListener taskPCL;

    public TaskService(final String name, final ExecutorService executorService) {
        if (name == null) {
            throw new IllegalArgumentException("null name");
        }
        if (executorService == null) {
            throw new IllegalArgumentException("null executorService");
        }
        this.name = name;
        this.executorService = executorService;
        this.tasks = new ArrayList<Task>();
        this.taskPCL = new TaskPCL();
    }

    public TaskService(final String name) {
        this(name, new ThreadPoolExecutor(3, // corePool size
                10, // maximumPool size
                1L, TimeUnit.SECONDS, // non-core threads time to live
                new LinkedBlockingQueue<Runnable>()));
    }

    public final String getName() {
        return this.name;
    }

    private List<Task> copyTasksList() {
        synchronized (this.tasks) {
            if (this.tasks.isEmpty()) {
                return Collections.emptyList();
            }
            return new ArrayList<Task>(this.tasks);
        }
    }

    private class TaskPCL implements PropertyChangeListener {
        public void propertyChange(final PropertyChangeEvent e) {
            final String propertyName = e.getPropertyName();
            if ("done".equals(propertyName)) {
                final Task task = (Task) (e.getSource());
                if (task.isDone()) {
                    List<Task> oldTaskList, newTaskList;
                    synchronized (TaskService.this.tasks) {
                        oldTaskList = TaskService.this.copyTasksList();
                        TaskService.this.tasks.remove(task);
                        task.removePropertyChangeListener(TaskService.this.taskPCL);
                        newTaskList = TaskService.this.copyTasksList();
                    }
                    TaskService.this.firePropertyChange("tasks", oldTaskList, newTaskList);
                    final Task.InputBlocker inputBlocker = task.getInputBlocker();
                    if (inputBlocker != null) {
                        inputBlocker.unblock();
                    }
                }
            }
        }
    }

    private void maybeBlockTask(final Task task) {
        final Task.InputBlocker inputBlocker = task.getInputBlocker();
        if (inputBlocker == null) {
            return;
        }
        if (inputBlocker.getScope() != Task.BlockingScope.NONE) {
            if (SwingUtilities.isEventDispatchThread()) {
                inputBlocker.block();
            } else {
                final Runnable doBlockTask = new Runnable() {
                    public void run() {
                        inputBlocker.block();
                    }
                };
                SwingUtilities.invokeLater(doBlockTask);
            }
        }
    }

    public void execute(final Task task) {
        if (task == null) {
            throw new IllegalArgumentException("null task");
        }
        if (!task.isPending() || (task.getTaskService() != null)) {
            throw new IllegalArgumentException("task has already been executed");
        }
        task.setTaskService(this);
        // TBD: what if task has already been submitted?
        List<Task> oldTaskList, newTaskList;
        synchronized (this.tasks) {
            oldTaskList = this.copyTasksList();
            this.tasks.add(task);
            newTaskList = this.copyTasksList();
            task.addPropertyChangeListener(this.taskPCL);
        }
        this.firePropertyChange("tasks", oldTaskList, newTaskList);
        this.maybeBlockTask(task);
        this.executorService.execute(task);
    }

    public List<Task> getTasks() {
        return this.copyTasksList();
    }

    public final void shutdown() {
        this.executorService.shutdown();
    }

    public final List<Runnable> shutdownNow() {
        return this.executorService.shutdownNow();
    }

    public final boolean isShutdown() {
        return this.executorService.isShutdown();
    }

    public final boolean isTerminated() {
        return this.executorService.isTerminated();
    }

    public final boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.executorService.awaitTermination(timeout, unit);
    }
}
