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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This is for minor designated windows, majors should implement their own.
 * I know I could'ave just said defaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)
 * but this gives me flexibility in the future should something come up.
 * 
 * @author Kavon Farvardin
 */
public class WindowController extends WindowAdapter {

    ManagedWindow mw;

    public WindowController(ManagedWindow mw) {
        this.mw = mw;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        mw.destroy();
    }
}
