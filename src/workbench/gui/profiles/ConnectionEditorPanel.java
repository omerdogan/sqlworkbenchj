/*
 * ConnectionEditorPanel.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2005, Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: info@sql-workbench.net
 *
 */
package workbench.gui.profiles;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import workbench.db.ConnectionMgr;
import workbench.db.ConnectionProfile;
import workbench.db.DbDriver;
import workbench.gui.WbSwingUtilities;
import workbench.gui.components.BooleanPropertyEditor;
import workbench.gui.components.PasswordPropertyEditor;
import workbench.gui.components.StringPropertyEditor;
import workbench.gui.components.WbButton;
import workbench.gui.components.WbTraversalPolicy;
import workbench.gui.help.HtmlViewer;
import workbench.interfaces.SimplePropertyEditor;
import workbench.log.LogMgr;
import workbench.resource.ResourceMgr;
import workbench.util.FileDialogUtil;

/**
 *
 * @author  info@sql-workbench.net
 */
public class ConnectionEditorPanel
	extends JPanel
	implements PropertyChangeListener, ActionListener
{
	private ConnectionProfile currentProfile;
	private List drivers;
	private ProfileListModel sourceModel;
	private boolean init;
	private List editors;

	public ConnectionEditorPanel()
	{
		this.initComponents();

		WbTraversalPolicy policy = new WbTraversalPolicy();
		policy.addComponent(tfProfileName);
		policy.addComponent(cbDrivers);
		policy.addComponent(tfURL);
		policy.addComponent(tfUserName);
		policy.addComponent(tfPwd);
		policy.addComponent(cbAutocommit);
		policy.addComponent(cbStorePassword);
		policy.addComponent(cbSeperateConnections);
		policy.addComponent(cbIgnoreDropErrors);
		policy.addComponent(disableTableCheck);
		policy.addComponent(rollbackBeforeDisconnect);
		policy.addComponent(tfWorkspaceFile);
		policy.setDefaultComponent(tfProfileName);

		this.setFocusCycleRoot(true);
		this.setFocusTraversalPolicy(policy);

		this.initEditorList();

		this.selectWkspButton.addActionListener(this);
	}

	private void initEditorList()
	{
		this.editors = new ArrayList(10);
		for (int i=0; i < this.getComponentCount(); i++)
		{
			Component c = this.getComponent(i);
			if (c instanceof SimplePropertyEditor)
			{
				this.editors.add(c);
				c.addPropertyChangeListener(c.getName(), this);
        ((SimplePropertyEditor)c).setImmediateUpdate(true);
			}
		}
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
  private void initComponents()//GEN-BEGIN:initComponents
  {
    java.awt.GridBagConstraints gridBagConstraints;

    tfProfileName = new StringPropertyEditor();
    cbDrivers = new javax.swing.JComboBox();
    tfURL = new StringPropertyEditor();
    tfUserName = new StringPropertyEditor();
    tfPwd = new PasswordPropertyEditor();
    cbAutocommit = new BooleanPropertyEditor();
    lblUsername = new javax.swing.JLabel();
    lblPwd = new javax.swing.JLabel();
    lblDriver = new javax.swing.JLabel();
    lblUrl = new javax.swing.JLabel();
    jSeparator2 = new javax.swing.JSeparator();
    cbStorePassword = new BooleanPropertyEditor();
    cbSeperateConnections = new BooleanPropertyEditor();
    cbIgnoreDropErrors = new BooleanPropertyEditor();
    jSeparator1 = new javax.swing.JSeparator();
    workspaceFileLabel = new javax.swing.JLabel();
    tfWorkspaceFile = new StringPropertyEditor();
    selectWkspButton = new javax.swing.JButton();
    manageDriversButton = new WbButton();
    extendedProps = new javax.swing.JButton();
    helpButton = new WbButton();
    disableTableCheck = new BooleanPropertyEditor();
    rollbackBeforeDisconnect = new BooleanPropertyEditor();
    confirmUpdates = new BooleanPropertyEditor();

    setLayout(new java.awt.GridBagLayout());

    setMinimumSize(new java.awt.Dimension(200, 200));
    tfProfileName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    tfProfileName.setName("name");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
    add(tfProfileName, gridBagConstraints);

    cbDrivers.setFocusCycleRoot(true);
    cbDrivers.setMaximumSize(new java.awt.Dimension(32767, 20));
    cbDrivers.setMinimumSize(new java.awt.Dimension(40, 20));
    cbDrivers.setName("driverclass");
    cbDrivers.setPreferredSize(new java.awt.Dimension(120, 20));
    cbDrivers.setVerifyInputWhenFocusTarget(false);
    cbDrivers.addItemListener(new java.awt.event.ItemListener()
    {
      public void itemStateChanged(java.awt.event.ItemEvent evt)
      {
        cbDriversItemStateChanged(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 6);
    add(cbDrivers, gridBagConstraints);

    tfURL.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    tfURL.setMaximumSize(new java.awt.Dimension(2147483647, 20));
    tfURL.setMinimumSize(new java.awt.Dimension(40, 20));
    tfURL.setName("url");
    tfURL.setPreferredSize(new java.awt.Dimension(100, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 6);
    add(tfURL, gridBagConstraints);

    tfUserName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    tfUserName.setToolTipText("");
    tfUserName.setMaximumSize(new java.awt.Dimension(2147483647, 20));
    tfUserName.setMinimumSize(new java.awt.Dimension(40, 20));
    tfUserName.setName("username");
    tfUserName.setPreferredSize(new java.awt.Dimension(100, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 6);
    add(tfUserName, gridBagConstraints);

    tfPwd.setName("password");
    tfPwd.setNextFocusableComponent(cbAutocommit);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 6);
    add(tfPwd, gridBagConstraints);

    cbAutocommit.setText("Autocommit");
    cbAutocommit.setName("autocommit");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 6);
    add(cbAutocommit, gridBagConstraints);

    lblUsername.setLabelFor(tfUserName);
    lblUsername.setText(ResourceMgr.getString(ResourceMgr.TXT_DB_USERNAME));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 0);
    add(lblUsername, gridBagConstraints);

    lblPwd.setLabelFor(tfPwd);
    lblPwd.setText(ResourceMgr.getString(ResourceMgr.TXT_DB_PASSWORD));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
    add(lblPwd, gridBagConstraints);

    lblDriver.setLabelFor(cbDrivers);
    lblDriver.setText(ResourceMgr.getString(ResourceMgr.TXT_DB_DRIVER));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 0);
    add(lblDriver, gridBagConstraints);

    lblUrl.setLabelFor(tfURL);
    lblUrl.setText(ResourceMgr.getString(ResourceMgr.TXT_DB_URL));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 0);
    add(lblUrl, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(jSeparator2, gridBagConstraints);

    cbStorePassword.setSelected(true);
    cbStorePassword.setText(ResourceMgr.getString("LabelSavePassword"));
    cbStorePassword.setToolTipText(ResourceMgr.getDescription("LabelSavePassword"));
    cbStorePassword.setName("storePassword");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
    add(cbStorePassword, gridBagConstraints);

    cbSeperateConnections.setText(ResourceMgr.getString("LabelSeperateConnections"));
    cbSeperateConnections.setToolTipText(ResourceMgr.getDescription("LabelSeperateConnections"));
    cbSeperateConnections.setName("useSeperateConnectionPerTab");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
    add(cbSeperateConnections, gridBagConstraints);

    cbIgnoreDropErrors.setSelected(true);
    cbIgnoreDropErrors.setText(ResourceMgr.getString("LabelIgnoreDropErrors"));
    cbIgnoreDropErrors.setToolTipText(ResourceMgr.getDescription("LabelIgnoreDropErrors"));
    cbIgnoreDropErrors.setName("ignoreDropErrors");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
    add(cbIgnoreDropErrors, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 15;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
    gridBagConstraints.weighty = 1.0;
    add(jSeparator1, gridBagConstraints);

    workspaceFileLabel.setLabelFor(tfWorkspaceFile);
    workspaceFileLabel.setText(ResourceMgr.getString("LabelOpenWksp"));
    workspaceFileLabel.setToolTipText(ResourceMgr.getDescription("LabelOpenWksp"));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 13;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(10, 5, 2, 0);
    add(workspaceFileLabel, gridBagConstraints);

    tfWorkspaceFile.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    tfWorkspaceFile.setMaximumSize(new java.awt.Dimension(2147483647, 20));
    tfWorkspaceFile.setMinimumSize(new java.awt.Dimension(40, 20));
    tfWorkspaceFile.setName("workspaceFile");
    tfWorkspaceFile.setPreferredSize(new java.awt.Dimension(100, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 13;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(10, 5, 2, 5);
    add(tfWorkspaceFile, gridBagConstraints);

    selectWkspButton.setText("...");
    selectWkspButton.setMaximumSize(new java.awt.Dimension(26, 22));
    selectWkspButton.setMinimumSize(new java.awt.Dimension(26, 22));
    selectWkspButton.setPreferredSize(new java.awt.Dimension(26, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 13;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(10, 0, 2, 6);
    add(selectWkspButton, gridBagConstraints);

    manageDriversButton.setText(ResourceMgr.getString("LabelEditDrivers"));
    manageDriversButton.setToolTipText(ResourceMgr.getDescription("EditDrivers"));
    manageDriversButton.setMaximumSize(new java.awt.Dimension(100, 25));
    manageDriversButton.setMinimumSize(new java.awt.Dimension(70, 25));
    manageDriversButton.setPreferredSize(new java.awt.Dimension(100, 25));
    manageDriversButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        showDriverEditorDialog(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 16;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 6, 0);
    add(manageDriversButton, gridBagConstraints);

    extendedProps.setText(ResourceMgr.getString("LabelConnExtendedProps"));
    extendedProps.setToolTipText(ResourceMgr.getDescription("LabelConnExtendedProps"));
    extendedProps.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EtchedBorder(), new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 6, 1, 6))));
    extendedProps.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(java.awt.event.MouseEvent evt)
      {
        extendedPropsMouseClicked(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 6);
    add(extendedProps, gridBagConstraints);

    helpButton.setText(ResourceMgr.getString("LabelHelp"));
    helpButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        helpButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 16;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
    add(helpButton, gridBagConstraints);

    disableTableCheck.setText(ResourceMgr.getString("LabelDisableAutoTableCheck"));
    disableTableCheck.setToolTipText(ResourceMgr.getDescription("LabelDisableAutoTableCheck"));
    disableTableCheck.setName("disableUpdateTableCheck");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
    add(disableTableCheck, gridBagConstraints);

    rollbackBeforeDisconnect.setText(ResourceMgr.getString("LabelRollbackBeforeDisconnect"));
    rollbackBeforeDisconnect.setToolTipText(ResourceMgr.getDescription("LabelRollbackBeforeDisconnect"));
    rollbackBeforeDisconnect.setName("rollbackBeforeDisconnect");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
    add(rollbackBeforeDisconnect, gridBagConstraints);

    confirmUpdates.setText(ResourceMgr.getString("LabelConfirmDbUpdates"));
    confirmUpdates.setToolTipText(ResourceMgr.getDescription("LabelConfirmDbUpdates"));
    confirmUpdates.setName("confirmUpdates");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
    add(confirmUpdates, gridBagConstraints);

  }//GEN-END:initComponents

	private void helpButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_helpButtonActionPerformed
	{//GEN-HEADEREND:event_helpButtonActionPerformed
		HtmlViewer viewer = new HtmlViewer((JDialog)SwingUtilities.getWindowAncestor(this));
		viewer.showProfileHelp();
	}//GEN-LAST:event_helpButtonActionPerformed

	private void extendedPropsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_extendedPropsMouseClicked
	{//GEN-HEADEREND:event_extendedPropsMouseClicked
		this.editExtendedProperties();
	}//GEN-LAST:event_extendedPropsMouseClicked

	private void cbDriversItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cbDriversItemStateChanged
	{//GEN-HEADEREND:event_cbDriversItemStateChanged
		if (this.init) return;
		if (evt.getStateChange() == ItemEvent.SELECTED)
		{
			String oldDriver = null;
			DbDriver newDriver = null;
			try
			{
				oldDriver = this.currentProfile.getDriverclass();
				newDriver = (DbDriver)this.cbDrivers.getSelectedItem();
				if(this.currentProfile != null)
				{
					this.currentProfile.setDriverclass(newDriver.getDriverClass());
					this.currentProfile.setDriverName(newDriver.getName());
				}
				if (oldDriver == null || !oldDriver.equals(newDriver.getDriverClass()))
				{
					this.tfURL.setText(newDriver.getSampleUrl());
				}
			}
			catch (Exception e)
			{
				LogMgr.logError("ConnectionProfilePanel.cbDriversItemStateChanged()", "Error changing driver", e);
			}

			if (!newDriver.canReadLibrary())
			{
				SwingUtilities.invokeLater(
					new Runnable()
					{
						public void run()
						{
							if (WbSwingUtilities.getYesNo(ConnectionEditorPanel.this, ResourceMgr.getString("MsgDriverLibraryNotReadable")))
							{
								showDriverEditorDialog(null);
							}
						}
					});
			}
		}
	}//GEN-LAST:event_cbDriversItemStateChanged

	private void showDriverEditorDialog(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showDriverEditorDialog
	{//GEN-HEADEREND:event_showDriverEditorDialog
		// not really nice, but works until the driver editor can be
		// called from a different location...
		Frame parent = (Frame)(SwingUtilities.getWindowAncestor(this)).getParent();
		DriverEditorDialog d = new DriverEditorDialog(parent, true);
		WbSwingUtilities.center(d,parent);
		d.show();
    if (!d.isCancelled())
    {
  		List drivers = ConnectionMgr.getInstance().getDrivers();
    	this.setDrivers(drivers);
    }
    d.dispose();
	}//GEN-LAST:event_showDriverEditorDialog

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox cbAutocommit;
  private javax.swing.JComboBox cbDrivers;
  private javax.swing.JCheckBox cbIgnoreDropErrors;
  private javax.swing.JCheckBox cbSeperateConnections;
  private javax.swing.JCheckBox cbStorePassword;
  private javax.swing.JCheckBox confirmUpdates;
  private javax.swing.JCheckBox disableTableCheck;
  private javax.swing.JButton extendedProps;
  private javax.swing.JButton helpButton;
  private javax.swing.JSeparator jSeparator1;
  private javax.swing.JSeparator jSeparator2;
  private javax.swing.JLabel lblDriver;
  private javax.swing.JLabel lblPwd;
  private javax.swing.JLabel lblUrl;
  private javax.swing.JLabel lblUsername;
  private javax.swing.JButton manageDriversButton;
  private javax.swing.JCheckBox rollbackBeforeDisconnect;
  private javax.swing.JButton selectWkspButton;
  private javax.swing.JTextField tfProfileName;
  private javax.swing.JPasswordField tfPwd;
  private javax.swing.JTextField tfURL;
  private javax.swing.JTextField tfUserName;
  private javax.swing.JTextField tfWorkspaceFile;
  private javax.swing.JLabel workspaceFileLabel;
  // End of variables declaration//GEN-END:variables

	public void setDrivers(List aDriverList)
	{
		if (aDriverList != null)
		{
			this.init = true;
			Object currentDriver = this.cbDrivers.getSelectedItem();
			try
			{
				Collections.sort(aDriverList, DbDriver.getDriverClassComparator());
				this.cbDrivers.setModel(new DefaultComboBoxModel(aDriverList.toArray()));
				if (currentDriver != null)
				{
					this.cbDrivers.setSelectedItem(currentDriver);
				}
			}
			catch (Exception e)
			{
				LogMgr.logError("ConnectionEditorPanel.setDrivers()", "Error when setting new driver list", e);
			}
			finally
			{
				this.init = false;
			}

		}
	}

	public void editExtendedProperties()
	{
		if (this.currentProfile == null) return;
		Properties p = this.currentProfile.getConnectionProperties();
		ConnectionPropertiesEditor editor = new ConnectionPropertiesEditor(p);
		Dimension d = new Dimension(300,250);
		editor.setMinimumSize(d);
		editor.setPreferredSize(d);

		int choice = JOptionPane.showConfirmDialog(this, editor, ResourceMgr.getString("TxtEditConnPropsWindowTitle"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (choice == JOptionPane.OK_OPTION)
		{
			this.currentProfile.setConnectionProperties(editor.getProperties());
		}
	}
	public void selectWorkspace()
	{
		FileDialogUtil util = new FileDialogUtil();
		String filename = util.getWorkspaceFilename(SwingUtilities.getWindowAncestor(this), false, true);
		if (filename == null) return;
		this.tfWorkspaceFile.setText(filename);
	}

	public void setSourceList(ProfileListModel aSource)
	{
		this.sourceModel = aSource;
	}

	public void updateProfile()
	{
		if (this.init) return;
		if (this.currentProfile == null) return;
		if (this.editors == null) return;
		boolean changed = false;

		for (int i=0; i < this.editors.size(); i++)
		{
			SimplePropertyEditor editor = (SimplePropertyEditor)this.editors.get(i);
			changed = changed || editor.isChanged();
			editor.applyChanges();
		}

		if (changed)
		{
			this.sourceModel.profileChanged(this.currentProfile);
		}
	}

	public ConnectionProfile getProfile()
	{
		this.updateProfile();
		return this.currentProfile;
	}

	private void initPropertyEditors()
	{
		if (this.editors == null) return;
		if (this.currentProfile == null) return;

		for (int i=0; i < this.editors.size(); i++)
		{
			SimplePropertyEditor editor = (SimplePropertyEditor)this.editors.get(i);
			Component c = (Component)editor;
			String property = c.getName();
			if (property != null)
			{
				editor.setSourceObject(this.currentProfile, property);
			}
		}
	}

	public void setProfile(ConnectionProfile aProfile)
	{
		if (aProfile == null) return;

		this.currentProfile = aProfile;
		this.initPropertyEditors();

		String drvClass = aProfile.getDriverclass();
		DbDriver drv = null;
		if (drvClass != null)
		{
			String name = aProfile.getDriverName();
			long start = System.currentTimeMillis();
			drv = ConnectionMgr.getInstance().findDriverByName(drvClass, name);
			long end = System.currentTimeMillis();
		}

		try
		{
			this.init = true;
			cbDrivers.setSelectedItem(drv);
		}
		catch (Exception e)
		{
			LogMgr.logError("ConnectionEditorPanel.setProfile()", "Error setting profile", e);
		}
		finally
		{
			this.init = false;
		}
	}

	/** This method gets called when a bound property is changed.
	 * @param evt A PropertyChangeEvent object describing the event source
	 *   	and the property that has changed.
	 *
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		//this.updateProfile();
    if (!this.init)	this.sourceModel.profileChanged(this.currentProfile);
	}

	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		if (e.getSource() == this.selectWkspButton)
		{
			this.selectWorkspace();
		}
	}

}
