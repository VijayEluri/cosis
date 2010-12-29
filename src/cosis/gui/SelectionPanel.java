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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import cosis.media.Picture;

public class SelectionPanel extends JPanel {

	public enum FilterMode {ALL, NAME, ID, PASSWORD, DESCRIPTION};
	
	private FilterMode filter = FilterMode.ALL;
	
	private JList list;
	
	public SelectionPanel(ArrayList<Account> accounts) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		list = new JList();
		list.setCellRenderer(new AccountListRenderer());
		list.setPreferredSize(new Dimension(248,0));
		
		list.setListData(accounts.toArray());
		
		JScrollPane scrollyPolly = new JScrollPane(list);
		scrollyPolly.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		add(scrollyPolly);
	}
	
	
	
	
	private class AccountListRenderer implements ListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			JLabel label = new JLabel();
			Account a = (Account)value;
			
			label.setText(a.getName());
			label.setIcon(Picture.getImageIcon(a.getImageIcon()));
			
			if(isSelected) {
				label.setBackground(Color.LIGHT_GRAY);
			} else {
				label.setBackground(Color.WHITE);
			}
			
			label.validate();
			
			return label;
		}
		
	}
	
	public void setFilterMode(FilterMode newMode) {
		filter = newMode;
	}
	
	public void filter(String query) {
		System.out.println(query);
	}
}
