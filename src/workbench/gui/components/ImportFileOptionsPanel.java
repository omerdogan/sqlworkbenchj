/*
 * ImportFileOptionsPanel.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2005, Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: info@sql-workbench.net
 *
 */
package workbench.gui.components;

import workbench.resource.ResourceMgr;
import workbench.resource.Settings;

/**
 *
 * @author  info@sql-workbench.net
 */
public class ImportFileOptionsPanel extends javax.swing.JPanel
{
	
	/** Creates new form ImportFileOptionsPanel */
	public ImportFileOptionsPanel()
	{
		initComponents();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
  private void initComponents()//GEN-BEGIN:initComponents
  {
    java.awt.GridBagConstraints gridBagConstraints;

    checkBoxHeaders = new javax.swing.JCheckBox();
    labelDelimiter = new javax.swing.JLabel();
    textFieldDelimiter = new javax.swing.JTextField();
    labelDateFormat = new javax.swing.JLabel();
    textFieldDateFormat = new javax.swing.JTextField();
    labelDecimalFormat = new javax.swing.JLabel();
    textFieldDecimalFormat = new javax.swing.JTextField();
    labelQuoteChar = new javax.swing.JLabel();
    textFieldQuoteChar = new javax.swing.JTextField();
    dummy = new javax.swing.JPanel();
    decodeUnicode = new javax.swing.JCheckBox();

    setLayout(new java.awt.GridBagLayout());

    setMinimumSize(new java.awt.Dimension(150, 73));
    setPreferredSize(new java.awt.Dimension(150, 73));
    checkBoxHeaders.setText(ResourceMgr.getString("LabelImportFileHeaders"));
    checkBoxHeaders.setToolTipText(ResourceMgr.getDescription("LabelImportFileHeaders"));
    checkBoxHeaders.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 10;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    add(checkBoxHeaders, gridBagConstraints);

    labelDelimiter.setLabelFor(textFieldDelimiter);
    labelDelimiter.setText(ResourceMgr.getString("LabelColDelimiter"));
    labelDelimiter.setToolTipText(ResourceMgr.getDescription("LabelColDelimiter"));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
    add(labelDelimiter, gridBagConstraints);

    textFieldDelimiter.setColumns(4);
    textFieldDelimiter.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 7);
    add(textFieldDelimiter, gridBagConstraints);

    labelDateFormat.setLabelFor(textFieldDateFormat);
    labelDateFormat.setText(ResourceMgr.getString("LabelImportDateFormat"));
    labelDateFormat.setToolTipText(ResourceMgr.getDescription("LabelImportDateFormat"));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
    add(labelDateFormat, gridBagConstraints);

    textFieldDateFormat.setColumns(8);
    textFieldDateFormat.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 7);
    add(textFieldDateFormat, gridBagConstraints);

    labelDecimalFormat.setLabelFor(textFieldDateFormat);
    labelDecimalFormat.setText(ResourceMgr.getString("LabelImportNumberFormat"));
    labelDecimalFormat.setToolTipText(ResourceMgr.getDescription("LabelImportNumberFormat"));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
    add(labelDecimalFormat, gridBagConstraints);

    textFieldDecimalFormat.setColumns(8);
    textFieldDecimalFormat.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 7);
    add(textFieldDecimalFormat, gridBagConstraints);

    labelQuoteChar.setLabelFor(textFieldDateFormat);
    labelQuoteChar.setText(ResourceMgr.getString("LabelImportQuoteChar"));
    labelQuoteChar.setToolTipText(ResourceMgr.getDescription("LabelImportQuoteChar"));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
    add(labelQuoteChar, gridBagConstraints);

    textFieldQuoteChar.setColumns(8);
    textFieldQuoteChar.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 7);
    add(textFieldQuoteChar, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.weighty = 1.0;
    add(dummy, gridBagConstraints);

    decodeUnicode.setText(java.util.ResourceBundle.getBundle("language/wbstrings").getString("LabelImportDecode"));
    decodeUnicode.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    add(decodeUnicode, gridBagConstraints);

  }//GEN-END:initComponents
	
	public void saveSettings()
	{
		Settings.getInstance().setLastImportDelimiter(this.getColumnDelimiter());
		Settings.getInstance().setLastImportWithHeaders(this.getContainsHeader());
		Settings.getInstance().setLastImportDateFormat(this.getDateFormat());
		Settings.getInstance().setLastImportNumberFormat(this.getNumberFormat());
		Settings.getInstance().setLastImportQuoteChar(this.getQuoteChar());
		Settings.getInstance().setLastImportDecode(this.getDecodeUnicode());
	}
	
	public void restoreSettings()
	{
		this.setContainsHeader(Settings.getInstance().getLastImportWithHeaders());
		this.setColumnDelimiter(Settings.getInstance().getLastImportDelimiter(true));
		this.setDateFormat(Settings.getInstance().getLastImportDateFormat());
		this.setNumberFormat(Settings.getInstance().getLastImportNumberFormat());
		this.setQuoteChar(Settings.getInstance().getLastImportQuoteChar());
		this.setDecodeUnicode(Settings.getInstance().getLastImportDecode());
	}

	public void setNumberFormat(String aFormat)
	{
		this.textFieldDecimalFormat.setText(aFormat);
	}
	
	public String getNumberFormat()
	{
		return this.textFieldDecimalFormat.getText();
	}
	
	public String getDateFormat()
	{
		return this.textFieldDateFormat.getText();
	}
	
	public void setDateFormat(String aFormat)
	{
		this.textFieldDateFormat.setText(aFormat);
		this.textFieldDateFormat.setCaretPosition(0);
	}
	
	public boolean getContainsHeader()
	{
		return this.checkBoxHeaders.isSelected();
	}
	
	public void setContainsHeader(boolean aFlag)
	{
		this.checkBoxHeaders.setSelected(aFlag);
	}
	
	public String getColumnDelimiter()
	{
		return this.textFieldDelimiter.getText();
	}
	public void setColumnDelimiter(String aDelimiter)
	{
		this.textFieldDelimiter.setText(aDelimiter);
	}
	
	public void setQuoteChar(String aChar)
	{
		this.textFieldQuoteChar.setText(aChar);
	}
	
	public String getQuoteChar()
	{
		return this.textFieldQuoteChar.getText();
	}
	
	public boolean getDecodeUnicode()
	{
		return decodeUnicode.isSelected();
	}
	
	public void setDecodeUnicode(boolean flag)
	{
		this.decodeUnicode.setSelected(flag);
	}

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox checkBoxHeaders;
  private javax.swing.JCheckBox decodeUnicode;
  private javax.swing.JPanel dummy;
  private javax.swing.JLabel labelDateFormat;
  private javax.swing.JLabel labelDecimalFormat;
  private javax.swing.JLabel labelDelimiter;
  private javax.swing.JLabel labelQuoteChar;
  private javax.swing.JTextField textFieldDateFormat;
  private javax.swing.JTextField textFieldDecimalFormat;
  private javax.swing.JTextField textFieldDelimiter;
  private javax.swing.JTextField textFieldQuoteChar;
  // End of variables declaration//GEN-END:variables
	
}
