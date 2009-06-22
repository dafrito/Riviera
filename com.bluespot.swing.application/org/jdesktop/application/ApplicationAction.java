/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package org.jdesktop.application;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * The {@link javax.swing.Action} class used to implement the
 * <tt>&#064;Action</tt> annotation. This class is typically not instantiated
 * directly, it's created as a side effect of constructing an
 * <tt>ApplicationActionMap</tt>:
 * 
 * <pre>
 * public class MyActions {
 *     &#064;Action
 *     public void anAction() {
 *     } // an @Action named &quot;anAction&quot;
 * }
 * ApplicationContext ac = ApplicationContext.getInstance();
 * ActionMap actionMap = ac.getActionMap(new MyActions());
 * myButton.setAction(actionMap.get(&quot;anAction&quot;));
 * </pre>
 * 
 * <p>
 * When an ApplicationAction is constructed, it initializes all of its
 * properties from the specified <tt>ResourceMap</tt>. Resource names must match
 * the {@code @Action's} name, which is the name of the corresponding method, or
 * the value of the optional {@code @Action} name parameter. To initialize the
 * text and shortDescription properties of the action named <tt>"anAction"</tt>
 * in the previous example, one would define two resources:
 * 
 * <pre>
 * anAction.Action.text = Button/Menu/etc label text for anAction
 * anAction.Action.shortDescription = Tooltip text for anAction
 * </pre>
 * 
 * <p>
 * A complete description of the mapping between resources and Action properties
 * can be found in the ApplicationAction {@link #ApplicationAction constructor}
 * documentation.
 * 
 * <p>
 * An ApplicationAction's <tt>enabled</tt> and <tt>selected</tt> properties can
 * be delegated to boolean properties of the Actions class, by specifying the
 * corresponding property names. This can be done with the {@code @Action}
 * annotation, e.g.:
 * 
 * <pre>
 * public class MyActions {
 *     &#064;Action(enabledProperty = &quot;anActionEnabled&quot;)
 *     public void anAction() {
 *     }
 * 
 *     public boolean isAnActionEnabled() {
 *         // will fire PropertyChange when anActionEnabled changes 
 *         return anActionEnabled;
 *     }
 * }
 * </pre>
 * 
 * If the MyActions class supports PropertyChange events, then then
 * ApplicationAction will track the state of the specified property
 * ("anActionEnabled" in this case) with a PropertyChangeListener.
 * 
 * <p>
 * ApplicationActions can automatically <tt>block</tt> the GUI while the
 * <tt>actionPerformed</tt> method is running, depending on the value of block
 * annotation parameter. For example, if the value of block is
 * <tt>Task.BlockingScope.ACTION</tt>, then the action will be disabled while
 * the actionPerformed method runs.
 * 
 * <p>
 * An ApplicationAction can have a <tt>proxy</tt> Action, i.e. another Action
 * that provides the <tt>actionPerformed</tt> method, the enabled/selected
 * properties, and values for the Action's long and short descriptions. If the
 * proxy property is set, this ApplicationAction tracks all of the
 * aforementioned properties, and the <tt>actionPerformed</tt> method just calls
 * the proxy's <tt>actionPerformed</tt> method. If a <tt>proxySource</tt> is
 * specified, then it becomes the source of the ActionEvent that's passed to the
 * proxy <tt>actionPerformed</tt> method. Proxy action dispatching is as simple
 * as this:
 * 
 * <pre>
 * public void actionPerformed(ActionEvent actionEvent) {
 *     javax.swing.Action proxy = getProxy();
 *     if (proxy != null) {
 *         actionEvent.setSource(getProxySource());
 *         proxy.actionPerformed(actionEvent);
 *     }
 *     // ....
 * }
 * </pre>
 * 
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 * @see ApplicationContext#getActionMap(Object)
 * @see ResourceMap
 */
public class ApplicationAction extends AbstractAction {
    private static final long serialVersionUID = -7275590199922800649L;

    private final ApplicationActionMap appAM;
    private final ResourceMap resourceMap;
    private final String actionName; // see getName()
    private final Method actionMethod; // The @Action method
    private final String enabledProperty; // names a bound
    // appAM.getActionsClass() property
    private final Method isEnabledMethod; // Method object for
    // is/getEnabledProperty
    private final Method setEnabledMethod; // Method object for
    // setEnabledProperty
    private final String selectedProperty; // names a bound
    // appAM.getActionsClass() property
    private final Method isSelectedMethod; // Method object for
    // is/getSelectedProperty
    private final Method setSelectedMethod; // Method object for
    // setSelectedProperty
    private final Task.BlockingScope block;
    private javax.swing.Action proxy = null;
    private Object proxySource = null;
    private PropertyChangeListener proxyPCL = null;

    /**
     * Construct an <tt>ApplicationAction</tt> that implements an
     * <tt>&#064;Action</tt>.
     * 
     * <p>
     * If a {@code ResourceMap} is provided, then all of the
     * {@link javax.swing.Action Action} properties are initialized with the
     * values of resources whose key begins with {@code baseName}. ResourceMap
     * keys are created by appending an &#064;Action resource name, like
     * "Action.shortDescription" to the &#064;Action's baseName For example,
     * Given an &#064;Action defined like this:
     * 
     * <pre>
     * &#064;Action
     * void actionBaseName() {
     * }
     * </pre>
     * <p>
     * Then the shortDescription resource key would be
     * <code>actionBaseName.Action.shortDescription</code>, as in:
     * 
     * <pre>
     * actionBaseName.Action.shortDescription = Do perform some action
     * </pre>
     * 
     * <p>
     * The complete set of &#064;Action resources is:
     * 
     * <pre>
     * Action.icon
     * Action.text
     * Action.shortDescription
     * Action.longDescription
     * Action.smallIcon
     * Action.largeIcon
     * Action.command
     * Action.accelerator
     * Action.mnemonic
     * Action.displayedMnemonicIndex
     * </pre>
     * 
     * <p>
     * A few the resources are handled specially:
     * <ul>
     * <li><tt>Action.text</tt><br>
     * Used to initialize the Action properties with keys <tt>Action.NAME</tt>,
     * <tt>Action.MNEMONIC_KEY</tt> and <tt>Action.DISPLAYED_MNEMONIC_INDEX</tt>
     * . If the resources's value contains an "&" or an "_" it's assumed to mark
     * the following character as the mnemonic. If
     * Action.mnemonic/Action.displayedMnemonic resources are also defined (an
     * odd case), they'll override the mnemonic specfied with the Action.text
     * marker character.
     * 
     * <li><tt>Action.icon</tt><br>
     * Used to initialize both ACTION.SMALL_ICON,LARGE_ICON. If Action.smallIcon
     * or Action.largeIcon resources are also defined they'll override the value
     * defined for Action.icon.
     * 
     * <li><tt>Action.displayedMnemonicIndexKey</tt><br>
     * The corresponding javax.swing.Action constant is only defined in Java SE
     * 6. We'll set the Action property in Java SE 5 too.
     * </ul>
     * 
     * @param appAM
     *            the ApplicationActionMap this action is being constructed for.
     * @param resourceMap
     *            initial Action properties are loaded from this ResourceMap.
     * @param baseName
     *            the name of the &#064;Action
     * @param actionMethod
     *            unless a proxy is specified, actionPerformed calls this
     *            method.
     * @param enabledProperty
     *            name of the enabled property.
     * @param selectedProperty
     *            name of the selected property.
     * @param block
     *            how much of the GUI to block while this action executes.
     * 
     * @see #getName
     * @see ApplicationActionMap#getActionsClass
     * @see ApplicationActionMap#getActionsObject
     */
    public ApplicationAction(final ApplicationActionMap appAM, final ResourceMap resourceMap, final String baseName,
            final Method actionMethod, final String enabledProperty, final String selectedProperty,
            final Task.BlockingScope block) {
        if (appAM == null) {
            throw new IllegalArgumentException("null appAM");
        }
        if (baseName == null) {
            throw new IllegalArgumentException("null baseName");
        }
        this.appAM = appAM;
        this.resourceMap = resourceMap;
        this.actionName = baseName;
        this.actionMethod = actionMethod;
        this.enabledProperty = enabledProperty;
        this.selectedProperty = selectedProperty;
        this.block = block;

        /*
         * If enabledProperty is specified, lookup up the is/set methods and
         * verify that the former exists.
         */
        if (enabledProperty != null) {
            this.setEnabledMethod = this.propertySetMethod(enabledProperty, boolean.class);
            this.isEnabledMethod = this.propertyGetMethod(enabledProperty);
            if (this.isEnabledMethod == null) {
                throw this.newNoSuchPropertyException(enabledProperty);
            }
        } else {
            this.isEnabledMethod = null;
            this.setEnabledMethod = null;
        }

        /*
         * If selectedProperty is specified, lookup up the is/set methods and
         * verify that the former exists.
         */
        if (selectedProperty != null) {
            this.setSelectedMethod = this.propertySetMethod(selectedProperty, boolean.class);
            this.isSelectedMethod = this.propertyGetMethod(selectedProperty);
            if (this.isSelectedMethod == null) {
                throw this.newNoSuchPropertyException(selectedProperty);
            }
            super.putValue(ApplicationAction.SELECTED_KEY, Boolean.FALSE);
        } else {
            this.isSelectedMethod = null;
            this.setSelectedMethod = null;
        }

        if (resourceMap != null) {
            this.initActionProperties(resourceMap, baseName);
        }
    }

    /*
     * Shorter convenience constructor used to create ProxyActions, see
     * ApplicationActionMap.addProxyAction().
     */
    ApplicationAction(final ApplicationActionMap appAM, final ResourceMap resourceMap, final String actionName) {
        this(appAM, resourceMap, actionName, null, null, null, Task.BlockingScope.NONE);
    }

    private IllegalArgumentException newNoSuchPropertyException(final String propertyName) {
        final String actionsClassName = this.appAM.getActionsClass().getName();
        final String msg = String.format("no property named %s in %s", propertyName, actionsClassName);
        return new IllegalArgumentException(msg);
    }

    /**
     * The name of the {@code @Action} enabledProperty whose value is returned
     * by {@link #isEnabled isEnabled}, or null.
     * 
     * @return the name of the enabledProperty or null.
     * @see #isEnabled
     */
    String getEnabledProperty() {
        return this.enabledProperty;
    }

    /**
     * The name of the {@code @Action} selectedProperty whose value is returned
     * by {@link #isSelected isSelected}, or null.
     * 
     * @return the name of the selectedProperty or null.
     * @see #isSelected
     */
    String getSelectedProperty() {
        return this.selectedProperty;
    }

    /**
     * Return the proxy for this action or null.
     * 
     * @return the value of the proxy property.
     * @see #setProxy
     * @see #setProxySource
     * @see #actionPerformed
     */
    public javax.swing.Action getProxy() {
        return this.proxy;
    }

    /**
     * Set the proxy for this action. If the proxy is non-null then we
     * delegate/track the following:
     * <ul>
     * <li><tt>actionPerformed</tt><br>
     * Our <tt>actionPerformed</tt> method calls the delegate's after the
     * ActionEvent source to be the value of <tt>getProxySource</tt>
     * 
     * <li><tt>shortDescription</tt><br>
     * If the proxy's shortDescription, i.e. the value for key
     * {@link javax.swing.Action#SHORT_DESCRIPTION SHORT_DESCRIPTION} is not
     * null, then set this action's shortDescription. Most Swing components use
     * the shortDescription to initialize their tooltip.
     * 
     * <li><tt>longDescription</tt><br>
     * If the proxy's longDescription, i.e. the value for key
     * {@link javax.swing.Action#LONG_DESCRIPTION LONG_DESCRIPTION} is not null,
     * then set this action's longDescription.
     * </ul>
     * 
     * @see #setProxy
     * @see #setProxySource
     * @see #actionPerformed
     */
    public void setProxy(final javax.swing.Action proxy) {
        final javax.swing.Action oldProxy = this.proxy;
        this.proxy = proxy;
        if (oldProxy != null) {
            oldProxy.removePropertyChangeListener(this.proxyPCL);
            this.proxyPCL = null;
        }
        if (this.proxy != null) {
            this.updateProxyProperties();
            this.proxyPCL = new ProxyPCL();
            proxy.addPropertyChangeListener(this.proxyPCL);
        } else if (oldProxy != null) {
            this.setEnabled(false);
            this.setSelected(false);
        }
        this.firePropertyChange("proxy", oldProxy, this.proxy);
    }

    /**
     * Return the value that becomes the <tt>ActionEvent</tt> source before the
     * ActionEvent is passed along to the proxy Action.
     * 
     * @return the value of the proxySource property.
     * @see #getProxy
     * @see #setProxySource
     * @see ActionEvent#getSource
     */
    public Object getProxySource() {
        return this.proxySource;
    }

    /**
     * Set the value that becomes the <tt>ActionEvent</tt> source before the
     * ActionEvent is passed along to the proxy Action.
     * 
     * @param source
     *            the <tt>ActionEvent</tt> source/
     * @see #getProxy
     * @see #getProxySource
     * @see ActionEvent#setSource
     */
    public void setProxySource(final Object source) {
        final Object oldValue = this.proxySource;
        this.proxySource = source;
        this.firePropertyChange("proxySource", oldValue, this.proxySource);
    }

    private void maybePutDescriptionValue(final String key, final javax.swing.Action proxy) {
        final Object s = proxy.getValue(key);
        if (s instanceof String) {
            this.putValue(key, s);
        }
    }

    private void updateProxyProperties() {
        final javax.swing.Action proxy = this.getProxy();
        if (proxy != null) {
            this.setEnabled(proxy.isEnabled());
            final Object s = proxy.getValue(ApplicationAction.SELECTED_KEY);
            this.setSelected((s instanceof Boolean) && ((Boolean) s).booleanValue());
            this.maybePutDescriptionValue(javax.swing.Action.SHORT_DESCRIPTION, proxy);
            this.maybePutDescriptionValue(javax.swing.Action.LONG_DESCRIPTION, proxy);
        }
    }

    /*
     * This PCL is added to the proxy action, i.e. getProxy(). We track the
     * following properties of the proxy action we're bound to: enabled,
     * selected, longDescription, shortDescription. We only mirror the
     * description properties if they're non-null.
     */
    private class ProxyPCL implements PropertyChangeListener {
        public void propertyChange(final PropertyChangeEvent e) {
            final String propertyName = e.getPropertyName();
            if ((propertyName == null) || "enabled".equals(propertyName) || "selected".equals(propertyName)
                    || javax.swing.Action.SHORT_DESCRIPTION.equals(propertyName)
                    || javax.swing.Action.LONG_DESCRIPTION.equals(propertyName)) {
                ApplicationAction.this.updateProxyProperties();
            }
        }
    }

    /*
     * The corresponding javax.swing.Action constants are only defined in
     * Mustang (1.6), see
     * http://download.java.net/jdk6/docs/api/javax/swing/Action.html
     */
    private static final String SELECTED_KEY = "SwingSelectedKey";
    private static final String DISPLAYED_MNEMONIC_INDEX_KEY = "SwingDisplayedMnemonicIndexKey";
    private static final String LARGE_ICON_KEY = "SwingLargeIconKey";

    /*
     * Init all of the javax.swing.Action properties for the @Action named
     * actionName.
     */
    private void initActionProperties(final ResourceMap resourceMap, final String baseName) {
        boolean iconOrNameSpecified = false; // true if Action's icon/name
        // properties set
        final String typedName = null;

        // Action.text => Action.NAME,MNEMONIC_KEY,DISPLAYED_MNEMONIC_INDEX_KEY
        final String text = resourceMap.getString(baseName + ".Action.text");
        if (text != null) {
            MnemonicText.configure(this, text);
            iconOrNameSpecified = true;
        }
        // Action.mnemonic => Action.MNEMONIC_KEY
        final Integer mnemonic = resourceMap.getKeyCode(baseName + ".Action.mnemonic");
        if (mnemonic != null) {
            this.putValue(javax.swing.Action.MNEMONIC_KEY, mnemonic);
        }
        // Action.mnemonic => Action.DISPLAYED_MNEMONIC_INDEX_KEY
        final Integer index = resourceMap.getInteger(baseName + ".Action.displayedMnemonicIndex");
        if (index != null) {
            this.putValue(ApplicationAction.DISPLAYED_MNEMONIC_INDEX_KEY, index);
        }
        // Action.accelerator => Action.ACCELERATOR_KEY
        final KeyStroke key = resourceMap.getKeyStroke(baseName + ".Action.accelerator");
        if (key != null) {
            this.putValue(javax.swing.Action.ACCELERATOR_KEY, key);
        }
        // Action.icon => Action.SMALL_ICON,LARGE_ICON_KEY
        final Icon icon = resourceMap.getIcon(baseName + ".Action.icon");
        if (icon != null) {
            this.putValue(javax.swing.Action.SMALL_ICON, icon);
            this.putValue(ApplicationAction.LARGE_ICON_KEY, icon);
            iconOrNameSpecified = true;
        }
        // Action.smallIcon => Action.SMALL_ICON
        final Icon smallIcon = resourceMap.getIcon(baseName + ".Action.smallIcon");
        if (smallIcon != null) {
            this.putValue(javax.swing.Action.SMALL_ICON, smallIcon);
            iconOrNameSpecified = true;
        }
        // Action.largeIcon => Action.LARGE_ICON
        final Icon largeIcon = resourceMap.getIcon(baseName + ".Action.largeIcon");
        if (largeIcon != null) {
            this.putValue(ApplicationAction.LARGE_ICON_KEY, largeIcon);
            iconOrNameSpecified = true;
        }
        // Action.shortDescription => Action.SHORT_DESCRIPTION
        this.putValue(javax.swing.Action.SHORT_DESCRIPTION,
                resourceMap.getString(baseName + ".Action.shortDescription"));
        // Action.longDescription => Action.LONG_DESCRIPTION
        this.putValue(javax.swing.Action.LONG_DESCRIPTION, resourceMap.getString(baseName + ".Action.longDescription"));
        // Action.command => Action.ACTION_COMMAND_KEY
        this.putValue(javax.swing.Action.ACTION_COMMAND_KEY, resourceMap.getString(baseName + ".Action.command"));
        // If no visual was defined for this Action, i.e. no text
        // and no icon, then we default Action.NAME
        if (!iconOrNameSpecified) {
            this.putValue(javax.swing.Action.NAME, this.actionName);
        }
    }

    private String propertyMethodName(final String prefix, final String propertyName) {
        return prefix + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    private Method propertyGetMethod(final String propertyName) {
        final String[] getMethodNames = { this.propertyMethodName("is", propertyName),
                this.propertyMethodName("get", propertyName) };
        final Class actionsClass = this.appAM.getActionsClass();
        for (final String name : getMethodNames) {
            try {
                return actionsClass.getMethod(name);
            } catch (final NoSuchMethodException ignore) {
            }
        }
        return null;
    }

    private Method propertySetMethod(final String propertyName, final Class type) {
        final Class actionsClass = this.appAM.getActionsClass();
        try {
            return actionsClass.getMethod(this.propertyMethodName("set", propertyName), type);
        } catch (final NoSuchMethodException ignore) {
            return null;
        }
    }

    /**
     * 
     * The name of this Action. This string begins with the name the
     * corresponding &#064;Action method (unless the <tt>name</tt> &#064;Action
     * parameter was specified).
     * 
     * <p>
     * This name is used as a prefix to look up action resources, and the
     * ApplicationContext Framework uses it as the key for this Action in
     * ApplicationActionMaps.
     * 
     * <p>
     * Note: this property should not confused with the
     * {@link javax.swing.Action#NAME Action.NAME} key. That key is actually
     * used to initialize the <tt>text</tt> properties of Swing components,
     * which is why we call the corresponding ApplicationAction resource
     * "Action.text", as in:
     * 
     * <pre>
     * myCloseButton.Action.text = Close
     * </pre>
     * 
     * 
     * @return the (read-only) value of the name property
     */
    public String getName() {
        return this.actionName;
    }

    /**
     * The resourceMap for this Action.
     * 
     * @return the (read-only) value of the resourceMap property
     */
    public ResourceMap getResourceMap() {
        return this.resourceMap;
    }

    /**
     * 
     * Provides parameter values to &#064;Action methods. By default, parameter
     * values are selected based exclusively on their type:
     * <table border=1>
     * <tr>
     * <th>Parameter Type</th>
     * <th>Parameter Value</th>
     * </tr>
     * <tr>
     * <td><tt>ActionEvent</tt></td>
     * <td><tt>actionEvent</tt></td>
     * </tr>
     * <tr>
     * <td><tt>javax.swing.Action</tt></td>
     * <td>this <tt>ApplicationAction</tt> object</td>
     * </tr>
     * <tr>
     * <td><tt>ActionMap</tt></td>
     * <td>the <tt>ActionMap</tt> that contains this <tt>Action</tt></td>
     * </tr>
     * <tr>
     * <td><tt>ResourceMap</tt></td>
     * <td>the <tt>ResourceMap</tt> of the the <tt>ActionMap</tt> that contains
     * this <tt>Action</tt></td>
     * </tr>
     * <tr>
     * <td><tt>ApplicationContext</tt></td>
     * <td>the value of <tt>ApplicationContext.getInstance()</tt></td>
     * </tr>
     * </table>
     * 
     * <p>
     * ApplicationAction subclasses may also select values based on the value of
     * the <tt>Action.Parameter</tt> annotation, which is passed along as the
     * <tt>pKey</tt> argument to this method:
     * 
     * <pre>
     * &#064;Action
     * public void doAction(@Action.Parameter(&quot;myKey&quot;) String myParameter) {
     *     // The value of myParameter is computed by:
     *     // getActionArgument(String.class, &quot;myKey&quot;, actionEvent)
     * }
     * </pre>
     * 
     * <p>
     * If <tt>pType</tt> and <tt>pKey</tt> aren't recognized, this method calls
     * {@link #actionFailed} with an IllegalArgumentException.
     * 
     * 
     * @param pType
     *            parameter type
     * @param pKey
     *            the value of the &#064;Action.Parameter annotation
     * @param actionEvent
     *            the ActionEvent that trigged this Action
     */
    protected Object getActionArgument(final Class pType, final String pKey, final ActionEvent actionEvent) {
        Object argument = null;
        if (pType == ActionEvent.class) {
            argument = actionEvent;
        } else if (pType == javax.swing.Action.class) {
            argument = this;
        } else if (pType == ActionMap.class) {
            argument = this.appAM;
        } else if (pType == ResourceMap.class) {
            argument = this.resourceMap;
        } else if (pType == ApplicationContext.class) {
            argument = this.appAM.getContext();
        } else if (pType == Application.class) {
            argument = this.appAM.getContext().getApplication();
        } else {
            final Exception e = new IllegalArgumentException("unrecognized @Action method parameter");
            this.actionFailed(actionEvent, e);
        }
        return argument;
    }

    private Task.InputBlocker createInputBlocker(final Task task, final ActionEvent event) {
        Object target = event.getSource();
        if (this.block == Task.BlockingScope.ACTION) {
            target = this;
        }
        return new DefaultInputBlocker(task, this.block, target, this);
    }

    private void noProxyActionPerformed(final ActionEvent actionEvent) {
        Object taskObject = null;

        /*
         * Create the arguments array for actionMethod by calling
         * getActionArgument() for each parameter.
         */
        final Annotation[][] allPAnnotations = this.actionMethod.getParameterAnnotations();
        final Class<?>[] pTypes = this.actionMethod.getParameterTypes();
        final Object[] arguments = new Object[pTypes.length];
        for (int i = 0; i < pTypes.length; i++) {
            String pKey = null;
            for (final Annotation pAnnotation : allPAnnotations[i]) {
                if (pAnnotation instanceof Action.Parameter) {
                    pKey = ((Action.Parameter) pAnnotation).value();
                    break;
                }
            }
            arguments[i] = this.getActionArgument(pTypes[i], pKey, actionEvent);
        }

        /*
         * Call target.actionMethod(arguments). If the return value is a Task,
         * then execute it.
         */
        try {
            final Object target = this.appAM.getActionsObject();
            taskObject = this.actionMethod.invoke(target, arguments);
        } catch (final Exception e) {
            this.actionFailed(actionEvent, e);
        }

        if (taskObject instanceof Task) {
            final Task task = (Task) taskObject;
            if (task.getInputBlocker() == null) {
                task.setInputBlocker(this.createInputBlocker(task, actionEvent));
            }
            final ApplicationContext ctx = this.appAM.getContext();
            ctx.getTaskService().execute(task);
        }
    }

    /**
     * This method implements this <tt>Action's</tt> behavior.
     * <p>
     * If there's a proxy Action then call its actionPerformed method.
     * Otherwise, call the &#064;Action method with parameter values provided by
     * {@code getActionArgument()}. If anything goes wrong call {@code
     * actionFailed()}.
     * 
     * @param actionEvent
     * @{inheritDoc
     * @see #setProxy
     * @see #getActionArgument
     * @see Task
     */
    public void actionPerformed(final ActionEvent actionEvent) {
        final javax.swing.Action proxy = this.getProxy();
        if (proxy != null) {
            actionEvent.setSource(this.getProxySource());
            proxy.actionPerformed(actionEvent);
        } else if (this.actionMethod != null) {
            this.noProxyActionPerformed(actionEvent);
        }
    }

    /**
     * If the proxy action is null and {@code enabledProperty} was specified,
     * then return the value of the enabled property's is/get method applied to
     * our ApplicationActionMap's {@code actionsObject}. Otherwise return the
     * value of this Action's enabled property.
     * 
     * @return {@inheritDoc}
     * @see #setProxy
     * @see #setEnabled
     * @see ApplicationActionMap#getActionsObject
     */
    @Override
    public boolean isEnabled() {
        if ((this.getProxy() != null) || (this.isEnabledMethod == null)) {
            return super.isEnabled();
        }
        try {
            final Object b = this.isEnabledMethod.invoke(this.appAM.getActionsObject());
            return (Boolean) b;
        } catch (final Exception e) {
            throw this.newInvokeError(this.isEnabledMethod, e);
        }
    }

    /**
     * If the proxy action is null and {@code enabledProperty} was specified,
     * then set the value of the enabled property by invoking the corresponding
     * {@code set} method on our ApplicationActionMap's {@code actionsObject}.
     * Otherwise set the value of this Action's enabled property.
     * 
     * @param enabled
     *            {@inheritDoc}
     * @see #setProxy
     * @see #isEnabled
     * @see ApplicationActionMap#getActionsObject
     */
    @Override
    public void setEnabled(final boolean enabled) {
        if ((this.getProxy() != null) || (this.setEnabledMethod == null)) {
            super.setEnabled(enabled);
        } else {
            try {
                this.setEnabledMethod.invoke(this.appAM.getActionsObject(), enabled);
            } catch (final Exception e) {
                throw this.newInvokeError(this.setEnabledMethod, e, enabled);
            }
        }
    }

    /**
     * If the proxy action is null and {@code selectedProperty} was specified,
     * then return the value of the selected property's is/get method applied to
     * our ApplicationActionMap's {@code actionsObject}. Otherwise return the
     * value of this Action's enabled property.
     * 
     * @return true if this Action's JToggleButton is selected
     * @see #setProxy
     * @see #setSelected
     * @see ApplicationActionMap#getActionsObject
     */
    public boolean isSelected() {
        if ((this.getProxy() != null) || (this.isSelectedMethod == null)) {
            final Object v = this.getValue(ApplicationAction.SELECTED_KEY);
            return (v instanceof Boolean) ? ((Boolean) v).booleanValue() : false;
        }
        try {
            final Object b = this.isSelectedMethod.invoke(this.appAM.getActionsObject());
            return (Boolean) b;
        } catch (final Exception e) {
            throw this.newInvokeError(this.isSelectedMethod, e);
        }
    }

    /**
     * If the proxy action is null and {@code selectedProperty} was specified,
     * then set the value of the selected property by invoking the corresponding
     * {@code set} method on our ApplicationActionMap's {@code actionsObject}.
     * Otherwise set the value of this Action's selected property.
     * 
     * @param selected
     *            this Action's JToggleButton's value
     * @see #setProxy
     * @see #isSelected
     * @see ApplicationActionMap#getActionsObject
     */
    public void setSelected(final boolean selected) {
        if ((this.getProxy() != null) || (this.setSelectedMethod == null)) {
            super.putValue(ApplicationAction.SELECTED_KEY, Boolean.valueOf(selected));
        } else {
            try {
                super.putValue(ApplicationAction.SELECTED_KEY, Boolean.valueOf(selected));
                if (selected != this.isSelected()) {
                    this.setSelectedMethod.invoke(this.appAM.getActionsObject(), selected);
                }
            } catch (final Exception e) {
                throw this.newInvokeError(this.setSelectedMethod, e, selected);
            }
        }
    }

    /**
     * Keeps the {@code @Action selectedProperty} in sync when the value of
     * {@code key} is {@code Action.SELECTED_KEY}.
     * 
     * @param key
     *            {@inheritDoc}
     * @param value
     *            {@inheritDoc}
     */
    @Override
    public void putValue(final String key, final Object value) {
        if (ApplicationAction.SELECTED_KEY.equals(key) && (value instanceof Boolean)) {
            this.setSelected((Boolean) value);
        } else {
            super.putValue(key, value);
        }
    }

    /*
     * Throw an Error because invoking Method m on the actionsObject, with the
     * specified arguments, failed.
     */
    private Error newInvokeError(final Method m, final Exception e, final Object... args) {
        String argsString = (args.length == 0) ? "" : args[0].toString();
        for (int i = 1; i < args.length; i++) {
            argsString += ", " + args[i];
        }
        final String actionsClassName = this.appAM.getActionsObject().getClass().getName();
        final String msg = String.format("%s.%s(%s) failed", actionsClassName, m, argsString);
        return new Error(msg, e);
    }

    /*
     * Forward the @Action class's PropertyChangeEvent e to this Action's
     * PropertyChangeListeners using actionPropertyName instead the original
     * 
     * @Action class's property name. This method is used by
     * ApplicationActionMap#ActionsPCL to forward @Action enabledProperty and
     * selectedProperty changes.
     */
    void forwardPropertyChangeEvent(final PropertyChangeEvent e, final String actionPropertyName) {
        if ("selected".equals(actionPropertyName) && (e.getNewValue() instanceof Boolean)) {
            this.putValue(ApplicationAction.SELECTED_KEY, e.getNewValue());
        }
        this.firePropertyChange(actionPropertyName, e.getOldValue(), e.getNewValue());
    }

    /*
     * Log enough output for a developer to figure out what went wrong.
     */
    private void actionFailed(final ActionEvent actionEvent, final Exception e) {
        // TBD Log an error
        // e.printStackTrace();
        throw new Error(e);
    }

    /**
     * Returns a string representation of this <tt>ApplicationAction</tt> that
     * should be useful for debugging. If the action is enabled it's name is
     * enclosed by parentheses; if it's selected then a "+" appears after the
     * name. If the action will appear with a text label, then that's included
     * too. If the action has a proxy, then we append the string for the proxy
     * action.
     * 
     * @return A string representation of this ApplicationAction
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" ");
        final boolean enabled = this.isEnabled();
        if (!enabled) {
            sb.append("(");
        }
        sb.append(this.getName());
        final Object selectedValue = this.getValue(ApplicationAction.SELECTED_KEY);
        if (selectedValue instanceof Boolean) {
            if (((Boolean) selectedValue).booleanValue()) {
                sb.append("+");
            }
        }
        if (!enabled) {
            sb.append(")");
        }
        final Object nameValue = this.getValue(javax.swing.Action.NAME); // [getName()].Action.text
        if (nameValue instanceof String) {
            sb.append(" \"");
            sb.append((String) nameValue);
            sb.append("\"");
        }
        this.proxy = this.getProxy();
        if (this.proxy != null) {
            sb.append(" Proxy for: ");
            sb.append(this.proxy.toString());
        }
        return sb.toString();
    }
}
