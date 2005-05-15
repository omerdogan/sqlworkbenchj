/*
 * TableCompletionHandler.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2005, Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: support@sql-workbench.net
 *
 */
package workbench.gui.completion;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import workbench.gui.WbSwingUtilities;
import workbench.gui.editor.JEditTextArea;
import workbench.log.LogMgr;
import workbench.resource.Settings;
import workbench.util.TableAlias;


/**
 * @author  support@sql-workbench.net
 */
public class CompletionPopup
	implements FocusListener, MouseListener, KeyListener
{
	private JEditTextArea editor;
	private JScrollPane scroll;
	private JWindow window;
	private JPanel content;
	private JList elementList;
	private List listeners;
	private ListModel data;
	private JComponent headerComponent;
	private String pasteCase;
	
	public CompletionPopup(JEditTextArea ed, JComponent header, ListModel listData)
	{
		this.data = listData;
		this.editor = ed;
		this.headerComponent = header;
		this.pasteCase = Settings.getInstance().getAutoCompletionPasteCase();
		this.elementList = new JList();
		this.elementList.setModel(this.data);
		this.elementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Border b = new CompoundBorder(elementList.getBorder(), new EmptyBorder(0,2,0,2));
		this.elementList.setBorder(b);
		
		elementList.addFocusListener(this);
		elementList.addMouseListener(this);
		
		content = new JPanel()
		{
			public boolean isManagingFocus() { return false; }
			public boolean getFocusTraversalKeysEnabled() {	return false;	}
		};
		content.setLayout(new BorderLayout());
		scroll = new JScrollPane(this.elementList);
		scroll.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		elementList.setVisibleRowCount(10);
		content.add(scroll);
		elementList.addKeyListener(this);
	}

	
	public void showPopup()
	{
		if (window != null) closePopup(false);
		try
		{
			scroll.setColumnHeaderView(headerComponent);
			headerComponent.doLayout();
			Dimension d = headerComponent.getPreferredSize();
			d.height += 25;
			elementList.setMinimumSize(d);
			scroll.setMinimumSize(d);

			Point p = editor.getCursorLocation();
			SwingUtilities.convertPointToScreen(p, editor);

			int count = data.getSize();
			elementList.setVisibleRowCount(count < 12 ? count + 1 : 12);
			
			String s = editor.getSelectedText();
			int index = findEntry(s);
			if (index == -1) index = 0;
			
			elementList.setSelectedIndex(index);
			elementList.ensureIndexIsVisible(index);
			
			window = new JWindow((Frame)SwingUtilities.getWindowAncestor(editor));
			editor.setKeyEventInterceptor(this);
			
			elementList.doLayout();
			scroll.invalidate();
			scroll.doLayout();
			
			window.setLocation(p);
			window.setContentPane(content);
			window.addKeyListener(this);
			window.pack();
			if (window.getWidth() < d.width + 5)
			{
				window.setSize(d.width + 5, window.getHeight());
			}
			
			WbSwingUtilities.requestFocus(window, elementList);
			window.show();
		}
		catch (Exception e)
		{
			LogMgr.logWarning("CompletionPopup.showPopup()", "Error displaying popup window",e);
		}
	}
	
	private String getPasteValue(String value)
	{
		if (value == null) return value;
		if ("lower".equalsIgnoreCase(pasteCase))
		{
			return value.toLowerCase();
		}
		else if ("upper".equalsIgnoreCase(pasteCase))
		{
			return value.toUpperCase();
		}
		return value;
	}
	
	private void closePopup(boolean pasteEntry)
	{
		editor.removeKeyEventInterceptor();
		
		if (this.window != null)
		{
			editor.requestFocus();
			this.window.hide();
			this.window.dispose();
			if (pasteEntry)
			{
				Object o = this.elementList.getSelectedValue();
				if (o != null)
				{
					String value = null;
					if (o instanceof TableAlias)
					{
						value = ((TableAlias)o).getNameToUse();
					}
					else
					{
						value = o.toString();
					}
					editor.setSelectedText(getPasteValue(value));
				}
			}
		}
	}

	protected int findEntry(String s)
	{
		if (s == null) return -1;
		int count = this.data.getSize();
		String search = s.toLowerCase();
		for (int i=0; i < count; i++)
		{
			String entry = this.data.getElementAt(i).toString();
			if (entry.length() == 0) continue;
			entry = entry.toLowerCase();
			if (entry.startsWith(search)) return i;
		}
		return -1;
	}
	
	protected int findEntry(char c)
	{
		int count = this.data.getSize();
		char sc = Character.toLowerCase(c);
		for (int i=0; i < count; i++)
		{
			String entry = this.data.getElementAt(i).toString();
			if (entry.length() == 0) continue;
			char ec = Character.toLowerCase(entry.charAt(0));
			
			if (ec == sc) return i;
		}
		return -1;
	}
	
	/**
	 * Implementation of the FocusListener interface
	 */
	public void focusGained(FocusEvent focusEvent)
	{
	}
	
	/**
	 * Implementation of the FocusListener interface
	 */
	public void focusLost(FocusEvent focusEvent)
	{
		closePopup(false);
	}
	
	/**
	 * Implementation of the MouseListener interface
	 */
	public void mouseClicked(java.awt.event.MouseEvent mouseEvent)
	{
		if (mouseEvent.getClickCount() == 2)
		{
			closePopup(true);
		}
	}
	
	public void mouseEntered(MouseEvent mouseEvent) {}
	public void mouseExited(MouseEvent mouseEvent) {}
	public void mousePressed(MouseEvent mouseEvent)	{}
	public void mouseReleased(MouseEvent mouseEvent) {}
	
	public void keyPressed(KeyEvent evt)
	{
		int index = -1;
		switch(evt.getKeyCode())
		{
			case KeyEvent.VK_HOME:
				elementList.setSelectedIndex(0);
				elementList.ensureIndexIsVisible(0);
				evt.consume();
				break;
			case KeyEvent.VK_END:
				index = data.getSize() - 1;
				elementList.setSelectedIndex(index);
				elementList.ensureIndexIsVisible(index);
				evt.consume();
				break;
			case KeyEvent.VK_TAB:
				evt.consume();
				break;
			case KeyEvent.VK_ENTER:
				closePopup(true);
				evt.consume();
				break;
			case KeyEvent.VK_ESCAPE:
				closePopup(false);
				evt.consume();
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_LEFT:
				forwardKeyToList(evt);
				break;
		}
	}
	
	private void forwardKeyToList(KeyEvent evt)
	{
		KeyListener[] l = elementList.getKeyListeners();
		for (int i=0; i < l.length; i++)
		{
			if (l[i] != this) l[i].keyPressed(evt);
		}
	}
	public void keyTyped(KeyEvent evt)
	{
		char ch = evt.getKeyChar();
		int index = findEntry(ch);
		if (index > -1)
		{
			elementList.setSelectedIndex(index);
			elementList.ensureIndexIsVisible(index);
			evt.consume();
		}
	}

	public void keyReleased(KeyEvent keyEvent)
	{
	}

}
