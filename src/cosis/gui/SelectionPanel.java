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

import javax.swing.JPanel;

public class SelectionPanel extends JPanel {

	public enum FilterMode {ALL, NAME, ID, PASSWORD, DESCRIPTION};
	
	private FilterMode filter = FilterMode.ALL;
	
	public void setFilterMode(FilterMode newMode) {
		filter = newMode;
	}
	
	public void filter(String query) {
		System.out.println(query);
	}
}
