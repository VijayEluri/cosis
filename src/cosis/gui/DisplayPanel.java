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

package cosis.gui;

import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cosis.media.Picture;

public class DisplayPanel extends JPanel {
	
	private JLabel id, pw, descLabel, image, title, date;
	private JCheckBox systemTray;
	private JTextField idBox, pwBox;
	private JTextArea desc;
	private JButton showHide;
	private Account currentAccount = null;
	
	
	public DisplayPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(CENTER_ALIGNMENT);
		
		
		id = new JLabel("User ID:     ");
		pw = new JLabel("Password:");
		descLabel = new JLabel("Description:");
		
		image = new JLabel(Picture.getImageIcon("accounts/account.png"));
		
		title = new JLabel("Title");
		title.setFont(new Font(title.getFont().getName(), Font.BOLD, 16));
		
		date = new JLabel("Last Modified: 12:12pm  on  12/12/12");
		date.setFont(new Font(date.getFont().getName(), Font.ITALIC, 10));
		
		idBox = new JTextField();
		pwBox = new JTextField();
		
		systemTray = new JCheckBox("Show in System Tray");
		systemTray.setFocusable(false);
		
		showHide = new JButton(Picture.getImageIcon("icons/size32/show.png"));
		
		desc = new JTextArea(6, 45);
		desc.setLineWrap(true);
		desc.setWrapStyleWord(true);
		
		JScrollPane descPane = new JScrollPane(desc);
		descPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);		
		
		JPanel titleRow = new JPanel();
		titleRow.setLayout(new BoxLayout(titleRow, BoxLayout.X_AXIS));
		titleRow.setAlignmentX(CENTER_ALIGNMENT);
		
			titleRow.add(image);
			titleRow.add(Box.createHorizontalStrut(5));
			titleRow.add(title);
		
		JPanel idRow = new JPanel();
		idRow.setLayout(new BoxLayout(idRow, BoxLayout.X_AXIS));
		idRow.setAlignmentX(CENTER_ALIGNMENT);
		
			idRow.add(id);
			idRow.add(Box.createHorizontalStrut(5));
			idRow.add(idBox);
			
		JPanel pwRow = new JPanel();
		pwRow.setLayout(new BoxLayout(pwRow, BoxLayout.X_AXIS));
		pwRow.setAlignmentX(CENTER_ALIGNMENT);
		
			pwRow.add(pw);
			pwRow.add(Box.createHorizontalStrut(5));
			pwRow.add(pwBox);
			pwRow.add(Box.createHorizontalStrut(5));
			pwRow.add(showHide);
			
			
		JPanel descLabelPanel = new JPanel();
		descLabelPanel.setLayout(new BoxLayout(descLabelPanel, BoxLayout.X_AXIS));
			
			descLabelPanel.add(descLabel);
			descLabelPanel.add(Box.createHorizontalGlue());
			
		JPanel dateRow = new JPanel();
		dateRow.setLayout(new BoxLayout(dateRow, BoxLayout.X_AXIS));
			
				dateRow.add(date);
				dateRow.add(Box.createHorizontalGlue());
				dateRow.add(systemTray);
				
			
		
		add(Box.createVerticalStrut(5));
		add(titleRow);
		add(Box.createVerticalStrut(5));
		add(idRow);
		add(Box.createVerticalStrut(5));
		add(pwRow);
		add(Box.createVerticalStrut(7));
		add(descLabelPanel);
		add(Box.createVerticalStrut(2));
		add(descPane);
		add(Box.createVerticalStrut(5));
		add(dateRow);
	}
	
//	public void showComponents(boolean b) {
//		id.setVisible(b);
//		pw.setVisible(b);
//		descLabel.setVisible(b);
//		image.setVisible(b);
//		title.setVisible(b);
//		date.setVisible(b);
//		systemTray.setVisible(b);
//		idBox.setVisible(b);
//		pwBox.setVisible(b);
//		desc.setVisible(b);
//		showHide.setVisible(b);
//		validate();
//	}
	
	public void refresh() {
		
	}
	
	
	public void setAccount(Account a) {
		if(currentAccount == null) {

		}
	}
	
	public void setEditMode(boolean flag) {
		
	}
	
}
