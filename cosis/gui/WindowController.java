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
 * @author Kavon Farvardin
 */
class WindowController extends WindowAdapter {

    ManagedWindow mw;

    WindowController(ManagedWindow mw) {
        this.mw = mw;
    }

    @Override
    public void windowIconified(WindowEvent e) {
        mw.minimize();
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        mw.maximize();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        mw.destroy();
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        mw.refresh();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        mw.display();
    }
}
