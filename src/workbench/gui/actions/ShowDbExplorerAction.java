/*
 * ShowDbExplorerAction.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2005, Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: info@sql-workbench.net
 *
 */
package workbench.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import workbench.gui.MainWindow;
import workbench.resource.ResourceMgr;

/**
 *	Action to paste the contents of the clipboard into the entry field
 *	@author  info@sql-workbench.net
 */
public class ShowDbExplorerAction
	extends WbAction
{
	private MainWindow mainWin;
	public ShowDbExplorerAction(MainWindow aWindow)
	{
		super();
		mainWin = aWindow;
		this.initMenuDefinition("MnuTxtShowDbExplorer",KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
		this.setIcon(ResourceMgr.getImage("Database"));
	}

	public void executeAction(ActionEvent e)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				mainWin.showDbExplorer();
			}
		});
	}
}
