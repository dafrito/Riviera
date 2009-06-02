package com.bluespot.swing;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;


public class GroupLayoutBuilder {
    
    private GroupLayout layout;
    private JComponent component;
    private Group verticalGroup;
    private Group horizontalGroup;
    private Group fieldGroup;
    private Group labelGroup;
    
    public GroupLayoutBuilder(JComponent component) {
        this.component = component;
        this.layout = new GroupLayout(this.component);
        this.horizontalGroup = this.layout.createParallelGroup();
        this.verticalGroup = this.layout.createSequentialGroup();
        this.labelGroup = this.layout.createParallelGroup();
        this.fieldGroup = this.layout.createParallelGroup();
        
        this.horizontalGroup.addGroup(this.layout.createSequentialGroup()
            .addGroup(this.labelGroup)
            .addGroup(this.fieldGroup)
        );
    }

    public Group getVerticalGroup() {
        return this.verticalGroup;
    }

    public void setVerticalGroup(Group verticalGroup) {
        this.verticalGroup = verticalGroup;
    }

    public Group getHorizontalGroup() {
        return this.horizontalGroup;
    }

    public void setHorizontalGroup(Group horizontalGroup) {
        this.horizontalGroup = horizontalGroup;
    }
    
    public void add(JComponent comp) {
        this.horizontalGroup.addComponent(comp);
        this.verticalGroup.addComponent(comp);
    }
    
    public void addLabeledPair(String labelName, JComponent comp) {
        JLabel label = new JLabel(labelName);
        this.labelGroup.addComponent(label);
        this.fieldGroup.addComponent(comp);
        this.verticalGroup.addGroup(
            this.layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(label)
                .addComponent(comp)
        );
    }
    
    public void build() {
        this.layout.setAutoCreateContainerGaps(true);
        this.layout.setAutoCreateGaps(true);
        
        this.layout.setHorizontalGroup(this.horizontalGroup);
        this.layout.setVerticalGroup(this.verticalGroup);
        
        this.component.setLayout(this.layout);
    }

    public void add(String string) {
        this.add(new JLabel(string));
    }
    
}
