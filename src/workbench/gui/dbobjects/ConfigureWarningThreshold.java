/*
 * ConfigureWarningThreshold.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2005, Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: info@sql-workbench.net
 *
 */
package workbench.gui.dbobjects;

import java.awt.event.ActionListener;

import workbench.resource.ResourceMgr;

/**
 *
 * @author  thomas
 */
public class ConfigureWarningThreshold 
	extends javax.swing.JPanel	
	implements ActionListener
{
	
	/** Creates new form ConfigureWarningThreshold */
	public ConfigureWarningThreshold()
	{
		initComponents();
		this.checkBoxEnableWarning.addActionListener(this);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()//GEN-BEGIN:initComponents
	{
		java.awt.GridBagConstraints gridBagConstraints;
		
		checkBoxEnableWarning = new javax.swing.JCheckBox();
		jLabel1 = new javax.swing.JLabel();
		textFieldThresholdValue = new javax.swing.JTextField();
		jPanel1 = new javax.swing.JPanel();
		
		setLayout(new java.awt.GridBagLayout());
		
		checkBoxEnableWarning.setText(ResourceMgr.getString("LabelEnableDataThresholdWarning"));
		checkBoxEnableWarning.setToolTipText(ResourceMgr.getDescription("LabelEnableDataThresholdWarning"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(checkBoxEnableWarning, gridBagConstraints);
		
		jLabel1.setText(ResourceMgr.getString("LabelThresholdLevel"));
		jLabel1.setToolTipText(ResourceMgr.getDescription("LabelThresholdLevel"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(jLabel1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 9);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		add(textFieldThresholdValue, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weighty = 1.0;
		add(jPanel1, gridBagConstraints);
		
	}//GEN-END:initComponents
	
	public long getThresholdValue()
	{
		if (this.checkBoxEnableWarning.isSelected())
		{
			try
			{
				String v = this.textFieldThresholdValue.getText();
				long rows = Long.parseLong(v);
				return rows;
			}
			catch (Exception e)
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
	}
	
	public void setThresholdValue(long aValue)
	{
		this.checkBoxEnableWarning.setSelected(aValue > 0);
		this.textFieldThresholdValue.setEnabled(aValue > 0);
		this.jLabel1.setEnabled(aValue > 0);
		if (aValue <= 0)
		{
			this.textFieldThresholdValue.setText("");
		}
		else
		{
			this.textFieldThresholdValue.setText(Long.toString(aValue));
		}
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		if (e.getSource() == this.checkBoxEnableWarning)
		{
			this.textFieldThresholdValue.setEnabled(this.checkBoxEnableWarning.isSelected());
			this.jLabel1.setEnabled(this.checkBoxEnableWarning.isSelected());
		}
	}
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox checkBoxEnableWarning;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JTextField textFieldThresholdValue;
	// End of variables declaration//GEN-END:variables
	
}
