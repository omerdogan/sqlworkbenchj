/*
 * WorkspaceOptions.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2013, Thomas Kellerer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * To contact the author please send an email to: support@sql-workbench.net
 *
 */
package workbench.gui.settings;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import workbench.gui.components.WbFilePicker;
import workbench.interfaces.Restoreable;
import workbench.resource.ResourceMgr;
import workbench.resource.Settings;
import workbench.util.StringUtil;

/**
 *
 * @author  Thomas Kellerer
 */
public class WorkspaceOptions
	extends JPanel
	implements Restoreable, ActionListener
{
	public WorkspaceOptions()
	{
		super();
		initComponents();
		String[] types = new String[] {
			ResourceMgr.getString("LblFileWksplink"),
			ResourceMgr.getString("LblFileWkspcontent"),
			ResourceMgr.getString("LblFileWkspnone")
		};
		fileHandling.setModel(new DefaultComboBoxModel(types));
	}

	@Override
	public void restoreSettings()
	{
		autoSaveWorkspace.setSelected(Settings.getInstance().getAutoSaveWorkspace());
		createBackup.setSelected(Settings.getInstance().getCreateWorkspaceBackup());
		backupCount.setEnabled(createBackup.isSelected());
		backupCount.setText(Integer.toString(Settings.getInstance().getMaxWorkspaceBackup()));
		backupDirPicker.setFilename(Settings.getInstance().getWorkspaceBackupDir());
		backupSettingsFile.setSelected(Settings.getInstance().makeVersionedBackups());
		ExternalFileHandling handling = Settings.getInstance().getFilesInWorkspaceHandling();
		switch (handling)
		{
			case link:
				fileHandling.setSelectedIndex(0);
				break;
			case content:
				fileHandling.setSelectedIndex(1);
				break;
			case none:
				fileHandling.setSelectedIndex(2);
				break;
			default:
				fileHandling.setSelectedIndex(0);
		}
	}

	@Override
	public void saveSettings()
	{
		Settings set = Settings.getInstance();

		// General settings
		set.setAutoSaveWorkspace(autoSaveWorkspace.isSelected());
		set.setCreateWorkspaceBackup(createBackup.isSelected());
		int index = fileHandling.getSelectedIndex();
		switch (index)
		{
			case 0:
				set.setFilesInWorkspaceHandling(ExternalFileHandling.link);
				break;
			case 1:
				set.setFilesInWorkspaceHandling(ExternalFileHandling.content);
				break;
			case 2:
				set.setFilesInWorkspaceHandling(ExternalFileHandling.none);
				break;
			default:
				set.setFilesInWorkspaceHandling(ExternalFileHandling.link);
		}
		int value = StringUtil.getIntValue(backupCount.getText(), -1);
		if (value > -1)
		{
			set.setMaxWorkspaceBackup(value);
		}
		set.setWorkspaceBackupDir(backupDirPicker.getFilename());
		set.setMakeBackups(backupSettingsFile.isSelected());
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {
		GridBagConstraints gridBagConstraints;

    autoSaveWorkspace = new JCheckBox();
    jPanel1 = new JPanel();
    jLabel3 = new JLabel();
    fileHandling = new JComboBox();
    jPanel2 = new JPanel();
    createBackup = new JCheckBox();
    jLabel1 = new JLabel();
    backupCount = new JTextField();
    backupSettingsFile = new JCheckBox();
    jLabel2 = new JLabel();
    backupDirPicker = new WbFilePicker();

    setLayout(new GridBagLayout());

    autoSaveWorkspace.setText(ResourceMgr.getString("LblAutoSaveWksp")); // NOI18N
    autoSaveWorkspace.setToolTipText(ResourceMgr.getString("d_LblAutoSaveWksp")); // NOI18N
    autoSaveWorkspace.setBorder(null);
    autoSaveWorkspace.setHorizontalAlignment(SwingConstants.LEFT);
    autoSaveWorkspace.setHorizontalTextPosition(SwingConstants.RIGHT);
    autoSaveWorkspace.setIconTextGap(5);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(10, 12, 3, 0);
    add(autoSaveWorkspace, gridBagConstraints);

    jPanel1.setLayout(new GridBagLayout());

    jLabel3.setText(ResourceMgr.getString("LblRememberFileWksp")); // NOI18N
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(4, 3, 0, 0);
    jPanel1.add(jLabel3, gridBagConstraints);

    fileHandling.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(0, 8, 0, 0);
    jPanel1.add(fileHandling, gridBagConstraints);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.insets = new Insets(3, 10, 0, 0);
    add(jPanel1, gridBagConstraints);

    jPanel2.setBorder(BorderFactory.createTitledBorder(ResourceMgr.getString("LblWkspVersioning"))); // NOI18N
    jPanel2.setLayout(new GridBagLayout());

    createBackup.setText(ResourceMgr.getString("LblBckWksp")); // NOI18N
    createBackup.setToolTipText(ResourceMgr.getString("d_LblBckWksp")); // NOI18N
    createBackup.setBorder(null);
    createBackup.addActionListener(this);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(6, 6, 0, 0);
    jPanel2.add(createBackup, gridBagConstraints);

    jLabel1.setLabelFor(backupCount);
    jLabel1.setText(ResourceMgr.getString("LblMaxWkspBck")); // NOI18N
    jLabel1.setToolTipText(ResourceMgr.getString("d_LblMaxWkspBck")); // NOI18N
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(1, 32, 1, 2);
    jPanel2.add(jLabel1, gridBagConstraints);

    backupCount.setColumns(3);
    backupCount.setHorizontalAlignment(JTextField.TRAILING);
    backupCount.setToolTipText(ResourceMgr.getString("d_LblMaxWkspBck")); // NOI18N
    backupCount.setMinimumSize(new Dimension(30, 20));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(0, 13, 1, 0);
    jPanel2.add(backupCount, gridBagConstraints);

    backupSettingsFile.setText(ResourceMgr.getString("LblAutoSaveSettings")); // NOI18N
    backupSettingsFile.setToolTipText(ResourceMgr.getString("d_LblAutoSaveSettings")); // NOI18N
    backupSettingsFile.setBorder(null);
    backupSettingsFile.setHorizontalAlignment(SwingConstants.LEFT);
    backupSettingsFile.setHorizontalTextPosition(SwingConstants.RIGHT);
    backupSettingsFile.setIconTextGap(5);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(7, 32, 3, 0);
    jPanel2.add(backupSettingsFile, gridBagConstraints);

    jLabel2.setText(ResourceMgr.getString("LblBckDir")); // NOI18N
    jLabel2.setToolTipText(ResourceMgr.getString("d_LblBckDir")); // NOI18N
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(0, 32, 4, 0);
    jPanel2.add(jLabel2, gridBagConstraints);

    backupDirPicker.setToolTipText(ResourceMgr.getString("d_LblBckDir")); // NOI18N
    backupDirPicker.setSelectDirectoryOnly(true);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(0, 13, 5, 11);
    jPanel2.add(backupDirPicker, gridBagConstraints);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(7, 9, 0, 9);
    add(jPanel2, gridBagConstraints);
  }

  // Code for dispatching events from components to event handlers.

  public void actionPerformed(ActionEvent evt)
  {
    if (evt.getSource() == createBackup)
    {
      WorkspaceOptions.this.createBackupActionPerformed(evt);
    }
  }// </editor-fold>//GEN-END:initComponents

	private void createBackupActionPerformed(ActionEvent evt)//GEN-FIRST:event_createBackupActionPerformed
	{//GEN-HEADEREND:event_createBackupActionPerformed
		backupCount.setEnabled(createBackup.isSelected());
		backupSettingsFile.setEnabled(createBackup.isSelected());
	}//GEN-LAST:event_createBackupActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private JCheckBox autoSaveWorkspace;
  private JTextField backupCount;
  private WbFilePicker backupDirPicker;
  private JCheckBox backupSettingsFile;
  private JCheckBox createBackup;
  private JComboBox fileHandling;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JPanel jPanel1;
  private JPanel jPanel2;
  // End of variables declaration//GEN-END:variables

}
