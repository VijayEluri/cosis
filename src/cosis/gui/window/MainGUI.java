package cosis.gui.window;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import cosis.Main;
import cosis.gui.DisplayPanel;
import cosis.gui.MajorWindowController;
import cosis.gui.ManagedWindow;
import cosis.gui.Profile;
import cosis.gui.SelectionPanel;
import cosis.media.Picture;
import cosis.util.Errors;

public class MainGUI implements ManagedWindow {
	
	private JFrame frame;
	private JMenu file, help;
	private JMenuItem quit, about, preferences, logout, backupNow;
	private JButton newAccount, removeAccount, editAccount, copyPassword, copyUsername, eraseClipboard, eraseSearch;
	private SelectionPanel selectPanel;
	private DisplayPanel displayPanel;
	private Profile profile;
	
	public MainGUI() {
		frame = new JFrame();
        frame.setResizable(Main.DEBUG);
        frame.addWindowListener(new MajorWindowController(this));
        frame.setJMenuBar(makeMenuBar());
        frame.setIconImage(Picture.getImageIcon("icons/size32/cosis.png").getImage());        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	private JToolBar makeToolBar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);

		newAccount = new JButton("new acct");
		removeAccount = new JButton("remove acct");
		editAccount = new JButton("edit acct");
		copyPassword = new JButton("copy pw");
		copyUsername = new JButton("copy username");
		eraseClipboard = new JButton("erase clipboard");
		eraseSearch = new JButton("erase search");
		
		toolbar.add(eraseSearch);
		toolbar.add(new JTextField(15));
		toolbar.addSeparator();
		toolbar.add(newAccount);
		toolbar.add(removeAccount);
		toolbar.addSeparator();
		toolbar.add(editAccount);
		toolbar.add(copyUsername);
		toolbar.add(copyPassword);
		toolbar.add(eraseClipboard);
		
		return toolbar;
	}
	
	private JButton makeButton(Icon icon, ActionListener ac) {
		JButton button = new JButton(icon);
		button.addActionListener(ac);
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
            if (e.getSource() == quit) {
            	Main.wm.destroyAll();
                System.exit(0);
            } else if (e.getSource() == about) {
            	Main.wm.addMinor(new About());
            } else if (e.getSource() == preferences) {
            	
                //TODO preferences window
            	
            } else if (e.getSource() == logout) {
            	
            	//TODO implement logging out
            	
            } else if (e.getSource() == backupNow) {
            	
            	//TODO implement manual backup
            	
            } else {
            	Errors.log(new UnsupportedOperationException("MenuListener was not prepared for this object."));
            }
        }
    }
    
    private class ToolbarListener implements ActionListener {
    	
    	public void actionPerformed(ActionEvent e) {
    		if(e.getSource() == newAccount) {
    			
    			//TODO add account window
    			
    		} else if(e.getSource() == removeAccount) {
    			
    			//TODO remove account confirmation
    			
    		} else if(e.getSource() == editAccount) {
    			
    			//TODO toggle editing mode
    			
    		} else if(e.getSource() == copyPassword) {
    			
    			//TODO copy password
    			
    		} else if(e.getSource() == copyUsername) {
    			
    			//TODO copy username
    			
    		} else if(e.getSource() == eraseClipboard) {
    			
    			//TODO spam clipboard and set it to null
    			
    		} else if(e.getSource() == eraseSearch) {
    			
    			//TODO clear search box, then disable self etc
    			
    		} else {
            	Errors.log(new UnsupportedOperationException("ToolbarListener was not prepared for this object."));
            }
    	}
    }

	public void load(Profile p) {
		profile = p;
		frame.setTitle(profile.getName() + (profile.getName().endsWith("s") ? "'" : "'s") + " Credentials");
		//frame.setContentPane(panel); //TODO add this
		frame.add(makeToolBar());
		
		refresh();	
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
		// TODO Auto-generated method stub
		frame.validate();
	}

	@Override
	public Component getComponentForLocation() {
		return (Component)frame;
	}

}
