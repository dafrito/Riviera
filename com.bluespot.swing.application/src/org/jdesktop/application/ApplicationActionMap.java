/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package org.jdesktop.application;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ActionMap;

/**
 * An {@link javax.swing.ActionMap ActionMap} class where each entry corresponds
 * to an <tt>&#064;Action</tt> method from a single <tt>actionsClass</tt> (i.e.
 * a class that contains one or more <tt>&#064;Actions</tt>). Each entry's key
 * is the <tt>&#064;Action's</tt> name (the method name by default), and the value is
 * an {@link ApplicationAction} that calls the <tt>&#064;Actions</tt> method.
 * For example, the code below prints <tt>"Hello World"</tt>:
 * 
 * <pre>
 * public class HelloWorldActions {
 *     public @Action
 *     void Hello() {
 *         System.out.print(&quot;Hello &quot;);
 *     }
 * 
 *     public @Action
 *     void World() {
 *         System.out.println(&quot;World&quot;);
 *     }
 * }
 * // ...
 * ApplicationActionMap appAM = new ApplicationActionMap(SimpleActions.class);
 * ActionEvent e = new ActionEvent(&quot;no src&quot;, ActionEvent.ACTION_PERFORMED, &quot;no cmd&quot;);
 * appAM.get(&quot;Hello&quot;).actionPerformed(e);
 * appAM.get(&quot;World&quot;).actionPerformed(e);
 * </pre>
 * 
 * <p>
 * If a <tt>ResourceMap</tt> is provided then each <tt>ApplicationAction's</tt> (
 * {@link javax.swing.Action#putValue putValue},
 * {@link javax.swing.Action#getValue getValue}) properties are initialized from
 * the ResourceMap.
 * 
 * <p>
 * TBD: explain use of resourcemap including action types, actionsObject,
 * actionsClass, ProxyActions,
 * 
 * @see ApplicationAction
 * @see ResourceMap
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */

public class ApplicationActionMap extends ActionMap {
    /**
     * 
     */
    private static final long serialVersionUID = 4785768758780757249L;
    private final ApplicationContext context;
    private final ResourceMap resourceMap;
    private final Class actionsClass;
    private final Object actionsObject;
    private final List<ApplicationAction> proxyActions;

    public ApplicationActionMap(final ApplicationContext context, final Class actionsClass, final Object actionsObject,
            final ResourceMap resourceMap) {
        if (context == null) {
            throw new IllegalArgumentException("null context");
        }
        if (actionsClass == null) {
            throw new IllegalArgumentException("null actionsClass");
        }
        if (actionsObject == null) {
            throw new IllegalArgumentException("null actionsObject");
        }
        if (!(actionsClass.isInstance(actionsObject))) {
            throw new IllegalArgumentException("actionsObject not an instanceof actionsClass");
        }
        this.context = context;
        this.actionsClass = actionsClass;
        this.actionsObject = actionsObject;
        this.resourceMap = resourceMap;
        this.proxyActions = new ArrayList<ApplicationAction>();
        this.addAnnotationActions(resourceMap);
        this.maybeAddActionsPCL();
    }

    public void connectActions() {

    }

    public final ApplicationContext getContext() {
        return this.context;
    }

    public final Class getActionsClass() {
        return this.actionsClass;
    }

    public final Object getActionsObject() {
        return this.actionsObject;
    }

    /**
     * All of the {@code @ProxyActions} recursively defined by this {@code
     * ApplicationActionMap} and its parent ancestors.
     * <p>
     * Returns a read-only list of the {@code @ProxyActions} defined by this
     * {@code ApplicationActionMap's} {@code actionClass} and, recursively, by
     * this {@code ApplicationActionMap's} parent. If there are no proxyActions,
     * an empty list is returned.
     * 
     * @return a list of all the proxyActions for this {@code
     *         ApplicationActionMap}
     */
    public List<ApplicationAction> getProxyActions() {
        // TBD: proxyActions that shadow should be merged
        final ArrayList<ApplicationAction> allProxyActions = new ArrayList<ApplicationAction>(this.proxyActions);
        ActionMap parent = this.getParent();
        while (parent != null) {
            if (parent instanceof ApplicationActionMap) {
                allProxyActions.addAll(((ApplicationActionMap) parent).proxyActions);
            }
            parent = parent.getParent();
        }
        return Collections.unmodifiableList(allProxyActions);
    }

    private String aString(final String s, final String emptyValue) {
        return (s.length() == 0) ? emptyValue : s;
    }

    private void putAction(final String key, final ApplicationAction action) {
        if (this.get(key) != null) {
            // TBD log a warning - two actions with the same key
        }
        this.put(key, action);
    }

    /*
     * Add Actions for each actionsClass method with an @Action annotation and
     * for the class's @ProxyActions annotation
     */
    private void addAnnotationActions(final ResourceMap resourceMap) {
        final Class<?> actionsClass = this.getActionsClass();
        // @Action
        for (final Method m : actionsClass.getDeclaredMethods()) {
            final Action action = m.getAnnotation(Action.class);
            if (action != null) {
                final String methodName = m.getName();
                final String enabledProperty = this.aString(action.enabledProperty(), null);
                final String selectedProperty = this.aString(action.selectedProperty(), null);
                final String actionName = this.aString(action.name(), methodName);
                final Task.BlockingScope block = action.block();
                final ApplicationAction appAction = new ApplicationAction(this, resourceMap, actionName, m,
                        enabledProperty, selectedProperty, block);
                this.putAction(actionName, appAction);
            }
        }
        // @ProxyActions
        final ProxyActions proxyActionsAnnotation = actionsClass.getAnnotation(ProxyActions.class);
        if (proxyActionsAnnotation != null) {
            for (final String actionName : proxyActionsAnnotation.value()) {
                final ApplicationAction appAction = new ApplicationAction(this, resourceMap, actionName);
                appAction.setEnabled(false); // will track the enabled property
                // of the Action it's bound to
                this.putAction(actionName, appAction);
                this.proxyActions.add(appAction);
            }
        }
    }

    /*
     * If any of the ApplicationActions need to track an enabled or selected
     * property defined in actionsClass, then add our PropertyChangeListener. If
     * none of the @Actions in actionClass provide an enabledProperty or
     * selectedProperty argument, then we don't need to do this.
     */
    private void maybeAddActionsPCL() {
        boolean needsPCL = false;
        final Object[] keys = this.keys();
        if (keys != null) {
            for (final Object key : keys) {
                final javax.swing.Action value = this.get(key);
                if (value instanceof ApplicationAction) {
                    final ApplicationAction actionAdapter = (ApplicationAction) value;
                    if ((actionAdapter.getEnabledProperty() != null) || (actionAdapter.getSelectedProperty() != null)) {
                        needsPCL = true;
                        break;
                    }
                }
            }
            if (needsPCL) {
                try {
                    final Class actionsClass = this.getActionsClass();
                    final Method m = actionsClass.getMethod("addPropertyChangeListener", PropertyChangeListener.class);
                    m.invoke(this.getActionsObject(), new ActionsPCL());
                } catch (final Exception e) {
                    final String s = "addPropertyChangeListener undefined " + this.actionsClass;
                    throw new Error(s, e);
                }
            }
        }
    }

    /*
     * When the value of an actionsClass @Action enabledProperty or
     * selectedProperty changes, forward the PropertyChangeEvent to the
     * ApplicationAction object itself.
     */
    private class ActionsPCL implements PropertyChangeListener {
        public void propertyChange(final PropertyChangeEvent event) {
            final String propertyName = event.getPropertyName();
            final Object[] keys = ApplicationActionMap.this.keys();
            if (keys != null) {
                for (final Object key : keys) {
                    final javax.swing.Action value = ApplicationActionMap.this.get(key);
                    if (value instanceof ApplicationAction) {
                        final ApplicationAction appAction = (ApplicationAction) value;
                        if (propertyName.equals(appAction.getEnabledProperty())) {
                            appAction.forwardPropertyChangeEvent(event, "enabled");
                        } else if (propertyName.equals(appAction.getSelectedProperty())) {
                            appAction.forwardPropertyChangeEvent(event, "selected");
                        }
                    }
                }
            }
        }
    }
}
