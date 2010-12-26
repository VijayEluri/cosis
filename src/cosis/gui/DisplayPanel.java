package cosis.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DisplayPanel extends JPanel {
	
	JLabel id, pw, image, title, date;
	JCheckBox systemTray;
	JTextField idBox, pwBox;
	JButton showHide;
	Account currentAccount = null;
	
	
	public DisplayPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		id = new JLabel("User ID:");
		pw = new JLabel("Password:");
		
		image = new JLabel();
		title = new JLabel();
		date = new JLabel();
		
		
		
		
		
		
	}
	
	
	public void displayAccount(Account a) {
		if(currentAccount == null) {
			
		}
		
	}
	
	public void setEditMode(boolean flag) {
		
	}
	
}
