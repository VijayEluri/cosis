//    This file is part of Cosis.
//
//    Cosis is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Cosis is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Cosis.  If not, see <http://www.gnu.org/licenses/>.

package cosis.gui.window;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import cosis.Main;
import cosis.gui.Account;
import cosis.gui.DisplayPanel;
import cosis.gui.MajorWindowController;
import cosis.gui.ManagedWindow;
import cosis.gui.Profile;
import cosis.gui.SelectionPanel;
import cosis.gui.SelectionPanel.FilterMode;
import cosis.media.Picture;
import cosis.util.Errors;

public class MainGUI implements ManagedWindow {
	
	private JFrame frame;
	
	private JMenu file, help;
	
	private JPopupMenu searchMenu, menuMenu;
	
	private JMenuItem quit, about, preferences, logout, backupNow;
	
	private JRadioButtonMenuItem searchAll, searchName, searchID, searchPW, searchDisc;
	
	private JTextField searchBox;
	
	private JButton newAccount, removeAccount, editAccount, copyPassword, copyUsername,	
						eraseClipboard, eraseSearch, search;
	
	private final ClipboardManager clipboard = new ClipboardManager();
	
	private SelectionPanel selectPanel;
	
	private DisplayPanel displayPanel;
	
	private Profile profile;
	
	public MainGUI(Profile p) {
		profile = p;
		
		frame = new JFrame();
		frame.setTitle(profile.getName() + (profile.getName().endsWith("s") ? "'" : "'s") + " Credentials");
        frame.setResizable(Main.DEBUG);
        frame.addWindowListener(new MajorWindowController(this));
        frame.setJMenuBar(makeMenuBar());
        frame.setIconImage(Picture.getImageIcon("icons/size32/cosis.png").getImage());
        
		p.getAccounts().add(new Account("Gmail", Picture.getImageIcon("accounts/email.png"), "", "", "", "", false));
		p.getAccounts().add(new Account("Treamspeak", Picture.getImageIcon("accounts/audio_headset.png"), "", "", "", "", false));
		p.getAccounts().add(new Account("kavon.org root", Picture.getImageIcon("accounts/ssh.png"), "", "", "", "", false));
        
        
        selectPanel = new SelectionPanel(p.getAccounts());
        displayPanel = new DisplayPanel();        
		
		frame.setContentPane(createContentPane());
		
		frame.addWindowFocusListener(new WindowAdapter() { //TODO this might become a big problem when switching and pasting between windows and shit.
            public void windowGainedFocus(WindowEvent e) {
                searchBox.requestFocusInWindow();
            }
        });
		
		
		
		frame.pack();
        frame.setLocationRelativeTo(Main.wm.getMajorWindow().getComponentForLocation());
        frame.setVisible(true);
        refresh();  
	}
	
	private JPanel createContentPane() {
		JPanel maxPayne = new JPanel();
		maxPayne.setLayout(new BoxLayout(maxPayne, BoxLayout.Y_AXIS));
		
		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BoxLayout(bottomPane, BoxLayout.X_AXIS));
		
			bottomPane.add(selectPanel);
			bottomPane.add(Box.createHorizontalStrut(3));
			bottomPane.add(displayPanel);
		
		maxPayne.add(makeToolBar());
		maxPayne.add(bottomPane);
		
		
		return maxPayne;
	}
	
	private JToolBar makeToolBar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		
		ToolBarListener tl = new ToolBarListener();
		ButtonGroup bg = new ButtonGroup();

		newAccount = makeButton("Add a new account", Picture.getImageIcon("icons/size22/account-new.png"), tl);
		removeAccount = makeButton("Remove account", Picture.getImageIcon("icons/size22/account-remove.png"), tl);
		editAccount = makeButton("Edit account", Picture.getImageIcon("icons/size22/account-edit.png"), tl);
		copyPassword = makeButton("Copy password", Picture.getImageIcon("icons/size22/copy-pw.png"), tl);
		copyUsername = makeButton("Copy user ID", Picture.getImageIcon("icons/size22/copy-id.png"), tl);
		eraseClipboard = makeButton("Clear system clipboard", Picture.getImageIcon("icons/size22/clear-clipboard.png"), tl);
		eraseSearch = makeButton("Clear search box", Picture.getImageIcon("icons/size22/searchbox-clear.png"), tl);
		search = makeButton("Select a search filter", Picture.getImageIcon("icons/size22/searchbox-find.png"), tl);
		
		searchBox = new JTextField(12); //TODO add keyboard shortcuts to automagically copy pw or username of top hit
		searchBox.addKeyListener(new SearchBoxListener());
		
		searchMenu = new JPopupMenu();
		searchAll = makeRadioMenu("All", tl, bg, searchMenu);
		searchName = makeRadioMenu("Name", tl, bg, searchMenu);
		searchID = makeRadioMenu("User ID", tl, bg, searchMenu);
		searchPW = makeRadioMenu("Password", tl, bg, searchMenu);
		searchDisc = makeRadioMenu("Description", tl, bg, searchMenu);
		
		searchAll.setSelected(true);
				
		toolbar.add(search);
		toolbar.add(searchBox);
		toolbar.add(eraseSearch);
		toolbar.addSeparator();
		toolbar.add(newAccount);
		toolbar.add(removeAccount);
		toolbar.add(editAccount);		
		toolbar.addSeparator();
		toolbar.add(copyUsername);
		toolbar.add(copyPassword);
		toolbar.add(eraseClipboard);
		
		
		
		
		return toolbar;
	}
	
	private JRadioButtonMenuItem makeRadioMenu(String text, ActionListener tl, ButtonGroup bg, JPopupMenu menu) {
		JRadioButtonMenuItem radio = new JRadioButtonMenuItem(text);
		radio.addActionListener(tl);
		bg.add(radio);
		menu.add(radio);
		return radio;
	}
	
	private JButton makeButton(String tooltip, Icon icon, ActionListener ac) {
		JButton button = new JButton(icon);
		button.setToolTipText(tooltip);
		button.addActionListener(ac);
		button.setFocusable(false);
		return button;
	}
	
	private JMenuBar makeMenuBar() {
		JMenuBar menubar = new JMenuBar();
        file = new JMenu("File");
        file.setMnemonic('F');
        
        	backupNow = new JMenuItem("Backup Now"); //TODO add picture for this.
        	backupNow.setMnemonic('B');
        
        	logout = new JMenuItem("Log out");
        	logout.setMnemonic('L');
        	logout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        	quit = new JMenuItem("Exit", Picture.getImageIcon("icons/size16/exit.png"));
        	quit.setMnemonic('E');
        	quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        	preferences = new JMenuItem("Preferences"); //TODO add picture for this.
        	preferences.setMnemonic('P');

        file.add(backupNow);
        file.add(preferences);
        file.addSeparator();
        file.add(logout);
        file.add(quit);

        help = new JMenu("Help");
        help.setMnemonic('H');
        
        	about = new JMenuItem("About", Picture.getImageIcon("icons/size16/help-about.png"));
        	about.setMnemonic('A');
        	
        help.add(about);

        MenuListener ml = new MenuListener();
        	backupNow.addActionListener(ml);
        	preferences.addActionListener(ml);
        	logout.addActionListener(ml);
        	quit.addActionListener(ml);
        	about.addActionListener(ml);

        menubar.add(file);
        menubar.add(help);
        return menubar;
	}
	
	/**
     * JMenuBar's listener
     */
    private class MenuListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
        	Object selectedObject = e.getSource();
            if (selectedObject == quit) {
            	Main.wm.destroyAll();
                System.exit(0);
            } else if (selectedObject == about) {
            	Main.wm.addMinor(new About());
            } else if (selectedObject == preferences) {
            	
                //TODO preferences window
            	
            } else if (selectedObject == logout) {
            	
            	//TODO implement logging out
            	
            } else if (selectedObject == backupNow) { //TODO make a negative value for backup-timer mean manual backups
            	if(profile.isBackupEnabled()) {
            		profile.save(true);
            	} else {
            		backupNow.setEnabled(false);
            	}            	
            } else {
            	Errors.log(new UnsupportedOperationException("MenuListener was not prepared for this object."));
            }
        }
    }
    
    private class ToolBarListener implements ActionListener {
    	
    	public void actionPerformed(ActionEvent e) {
    		Object selectedObject = e.getSource();
    		if(selectedObject == newAccount) {
    			
    			//TODO add account window
    			
    		} else if(selectedObject == removeAccount) {
    			
    			//TODO remove account confirmation
    			
    		} else if(selectedObject == editAccount) {
    			
    			//TODO toggle editing mode
    			
    		} else if(selectedObject == copyPassword) {
    			
    			//TODO copy password
    			
    		} else if(selectedObject == copyUsername) {

    			//TODO copy username
    			
    		} else if(selectedObject == eraseClipboard) {
    			
    			for(int i = 0; i < 15; i++) {
    				clipboard.setContents("cosis");
    				clipboard.setContents("was");
    				clipboard.setContents("here");
    			}
    			
    			for(int i = 0; i < 15; i++)
    				clipboard.setContents("");
    			
    		} else if(selectedObject == eraseSearch) {
    			
    			searchBox.setText("");
    			eraseSearch.setEnabled(false);
    			
    		} else if(selectedObject == search) {
    			searchMenu.show(search, 0, search.getHeight());
    			
    		} else if(selectedObject == searchAll) {    			
    			selectPanel.setFilterMode(FilterMode.ALL);
    			
    		} else if(selectedObject == searchName) {    			
    			selectPanel.setFilterMode(FilterMode.NAME);
    			
    		} else if(selectedObject == searchID) {    			
    			selectPanel.setFilterMode(FilterMode.ID);
    			
    		} else if(selectedObject == searchPW) {    			
    			selectPanel.setFilterMode(FilterMode.PASSWORD);
    			
    		} else if(selectedObject == searchDisc) {    			
    			selectPanel.setFilterMode(FilterMode.DESCRIPTION);
    			
    		} else {
            	Errors.log(new UnsupportedOperationException("ToolbarListener was not prepared for this object."));
            }
    	}
    }
    
    private class SearchBoxListener extends KeyAdapter {
    	@Override
    	public void keyReleased(KeyEvent e) {
    		String currentContents = searchBox.getText();
    		
    		selectPanel.filter(currentContents);
    		
    		if(currentContents.equals("")) {
    			eraseSearch.setEnabled(false);
    		} else {
    			eraseSearch.setEnabled(true);
    		}
    	}
    }
    
    private class ClipboardManager implements ClipboardOwner {
    	
    	private final Clipboard clipboard;
    	
    	public ClipboardManager() {
    		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	}
    	
		@Override
		public void lostOwnership(Clipboard arg0, Transferable arg1) {}
		
		public void setContents(String text) {
			clipboard.setContents(new StringSelection(text), this);
		}
    	
    }

	@Override
	public void minimize() {
		frame.setVisible(false);
	}

	@Override
	public void destroy() {
		frame.dispose();
	}

	@Override
	public void display() {
		frame.setVisible(true);
        if(Main.WIN) frame.setExtendedState(Frame.NORMAL);
	}

	@Override
	public void refresh() {
		backupNow.setEnabled(profile.isBackupEnabled());
		eraseSearch.setEnabled(searchBox.getText().equals("") ? false : true);
		
		
		frame.validate();
	}

	@Override
	public Component getComponentForLocation() {
		return (Component)frame;
	}

}
