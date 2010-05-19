package com.bluespot.crypto.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.bluespot.collections.observable.list.ObservableList;
import com.bluespot.demonstration.Demonstration;
import com.bluespot.solver.Solver;
import com.bluespot.solver.SolverListener;
import com.bluespot.solver.substitution.SubstitutionSolver;

public class GUI extends Demonstration {

    ObservableList<String> solutions = new ObservableList<String>();

    private final JList solutionsList = new JList(this.solutions);
    private final JTextField encryptedField = new JTextField();
    private final JButton solveButton = new JButton(new AbstractAction("Solve") {
        private static final long serialVersionUID = 4365943850935890354L;

        @Override
        public void actionPerformed(ActionEvent e) {
            GUI.this.solveButton.setEnabled(false);
            final String encrypted = GUI.this.encryptedField.getText();
            final Solver<String, String> solver = new SubstitutionSolver();
            solver.addSolverListener(GUI.this.listener);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    solver.solve(encrypted);
                }
            }).start();
        }
    });

    @Override
    protected Component newContentPane() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 500));
        panel.setLayout(new BorderLayout());

        JPanel entry = new JPanel(new BorderLayout());
        entry.add(this.encryptedField, BorderLayout.CENTER);
        entry.add(this.solveButton, BorderLayout.EAST);
        panel.add(entry, BorderLayout.NORTH);
        panel.add(new JScrollPane(this.solutionsList), BorderLayout.CENTER);
        return panel;
    }

    private SolverListener<String> listener = new SolverListener<String>() {

        @Override
        public void onSolution(String result) {
            GUI.this.solutions.add(result);
        }

        @Override
        public void finished() {
            GUI.this.solveButton.setText("Solve");
            GUI.this.solveButton.setEnabled(true);
        }

    };

    public static void main(String[] args) {
        Demonstration.launch(GUI.class);
    }

}
