/*
 * WbFilePicker.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: support@sql-workbench.net
 *
 */
package workbench.gui.components;

import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import workbench.gui.WbSwingUtilities;
import workbench.log.LogMgr;
import workbench.resource.Settings;
import workbench.util.ExceptionUtil;
import workbench.util.StringUtil;

/**
 *
 * @author  Thomas Kellerer
 */
public class WbFilePicker
	extends javax.swing.JPanel
{
	private String lastDir;
	private FileFilter fileFilter;
	private boolean allowMultiple;
	private File[] selectedFiles;
	private String lastDirProperty;
	private boolean selectDirectory;

	public WbFilePicker()
	{
		super();
		initComponents();
	}

	public void setSelectDirectoryOnly(boolean flag)
	{
		selectDirectory = flag;
	}

	public boolean getSelectDirectoryOnly(boolean flag)
	{
		return selectDirectory;
	}

	public void setTextFieldPropertyName(String name)
	{
		this.tfFilename.setName(name);
	}

	public void setTextfieldTooltip(String text)
	{
		tfFilename.setToolTipText(text);
	}

	public void setButtonTooltip(String text)
	{
		selectFileButton.setToolTipText(text);
	}

	public void setLastDirProperty(String prop)
	{
		this.lastDirProperty = prop;
		this.lastDir = Settings.getInstance().getProperty(prop, null);
	}

	public void setEnabled(boolean flag)
	{
		super.setEnabled(flag);
		this.tfFilename.setEnabled(flag);
		this.selectFileButton.setEnabled(flag);
	}

	@Override
	public void setToolTipText(String text)
	{
		super.setToolTipText(text);
		tfFilename.setToolTipText(text);
		selectFileButton.setToolTipText(text);
	}

	/**
	 * Adds an ActionListener for the text field.
	 * @param l
	 */
	public void addActionListener(ActionListener l)
	{
		this.tfFilename.addActionListener(l);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    tfFilename = new StringPropertyEditor();
    selectFileButton = new FlatButton();

    setLayout(new java.awt.GridBagLayout());

    tfFilename.setColumns(10);
    tfFilename.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    tfFilename.setName("library"); // NOI18N
    tfFilename.addMouseListener(new TextComponentMouseListener());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(tfFilename, gridBagConstraints);

    selectFileButton.setText("...");
    selectFileButton.setMaximumSize(new java.awt.Dimension(22, 22));
    selectFileButton.setMinimumSize(new java.awt.Dimension(22, 22));
    selectFileButton.setPreferredSize(new java.awt.Dimension(22, 22));
    selectFileButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectFileButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
    add(selectFileButton, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents
	private void selectFileButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectFileButtonActionPerformed
	{//GEN-HEADEREND:event_selectFileButtonActionPerformed
		try
		{
			JFileChooser jf = new WbFileChooser();
			if (selectDirectory)
			{
				jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			}
			else
			{
				jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jf.setMultiSelectionEnabled(allowMultiple);
				if (this.lastDir != null)
				{
					jf.setCurrentDirectory(new File(this.lastDir));
				}
				if (this.fileFilter != null)
				{
					jf.setFileFilter(this.fileFilter);
				}
			}

			int answer = jf.showOpenDialog(SwingUtilities.getWindowAncestor(this));
			if (answer == JFileChooser.APPROVE_OPTION)
			{
				if (this.allowMultiple)
				{
					this.selectedFiles = jf.getSelectedFiles();
				}
				else
				{
					this.selectedFiles = new File[1];
					this.selectedFiles[0] = jf.getSelectedFile();
				}

				StringBuilder path = new StringBuilder(this.selectedFiles.length * 100);
				for (int i = 0; i < this.selectedFiles.length; i++)
				{
					if (this.selectedFiles.length > 1 && i > 0)
					{
						path.append(StringUtil.getPathSeparator());
					}
					path.append(this.selectedFiles[i].getAbsolutePath().trim());
				}
				String newValue = path.toString();
				String oldValue = tfFilename.getText();
				this.tfFilename.setText(newValue);
				if (this.lastDirProperty != null)
				{
					Settings.getInstance().setProperty(lastDirProperty, selectedFiles[0].getParent());
				}
				this.firePropertyChange("filename", oldValue, newValue);
			}
		}
		catch (Throwable e)
		{
			LogMgr.logError("WbFilePicker.selectFileButtonActionPerformed()", "Error selecting file", e);
			WbSwingUtilities.showErrorMessage(ExceptionUtil.getDisplay(e));
		}
	}//GEN-LAST:event_selectFileButtonActionPerformed

	public String getFilename()
	{
		return tfFilename.getText();
	}

	public void setFilename(String name)
	{
		this.tfFilename.setText(name != null ? name : "");
		this.tfFilename.setCaretPosition(0);
	}

	public File getSelectedFile()
	{
		if (this.selectedFiles == null)
		{
			return null;
		}
		return this.selectedFiles[0];
	}

	public File[] getSelectedFiles()
	{
		if (!this.allowMultiple)
		{
			return null;
		}
		return this.selectedFiles;
	}

	public void setAllowMultiple(boolean flag)
	{
		this.allowMultiple = flag;
	}

	public void setFileFilter(FileFilter f)
	{
		this.fileFilter = f;
	}
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton selectFileButton;
  private javax.swing.JTextField tfFilename;
  // End of variables declaration//GEN-END:variables

}
