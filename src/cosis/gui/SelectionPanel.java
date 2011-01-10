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
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import cosis.media.Picture;

public class SelectionPanel extends JPanel {

	public enum FilterMode {ALL, NAME, ID, PASSWORD, DESCRIPTION};
	
	private FilterMode filter = FilterMode.ALL;
	
	private JList list;
	
	public SelectionPanel(ArrayList<Account> accounts) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new AccountListRenderer());
		list.setPreferredSize(new Dimension(192,0));
		
		list.setListData(accounts.toArray());
		
		JScrollPane scrollyPolly = new JScrollPane(list);
		scrollyPolly.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		add(scrollyPolly);
	}
	
	
	
	
	private class AccountListRenderer extends JLabel implements ListCellRenderer {

		private final Color HIGHLIGHT_COLOR = new Color(173, 216, 230);
		
		public AccountListRenderer() {
			setOpaque(true);
		    setIconTextGap(10);
		}
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			Account a = (Account)value;
			
			setText(a.getName());
			setIcon(new ImageIcon(a.getImageIcon().getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));	

		    if (isSelected) {
		      setBackground(HIGHLIGHT_COLOR);
		    } else {
		      setBackground(Color.white);
		    }
			
			return this;
		}
		
	}
	
	public void setFilterMode(FilterMode newMode) {
		filter = newMode;
	}
	
	public void filter(String query) {
		System.out.println(query);
	}
}
