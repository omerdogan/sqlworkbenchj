package workbench.gui.actions;

import workbench.interfaces.ClipboardSupport;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.KeyStroke;
import workbench.gui.MainWindow;
import workbench.gui.WbSwingUtilities;
import workbench.gui.profiles.DriverEditorDialog;
import workbench.resource.ResourceMgr;

/**
 *	@author  workbench@kellerer.org
 */
public class ManageDriversAction extends WbAction
{
	private MainWindow client;

	public ManageDriversAction(MainWindow aClient)
	{
		super();
		this.client = aClient;
		this.putValue(Action.NAME, ResourceMgr.getString("MnuTxtEditDrivers"));
		this.putValue(Action.SHORT_DESCRIPTION, ResourceMgr.getDescription("EditDrivers"));
		this.putValue(WbAction.MAIN_MENU_ITEM, ResourceMgr.MNU_TXT_FILE);
	}

	public void actionPerformed(ActionEvent e)
	{
		DriverEditorDialog d = new DriverEditorDialog(client, true);
		WbSwingUtilities.center(d, client);
		d.show();
	}
}
