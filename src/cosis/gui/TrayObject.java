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

import cosis.Main;
import cosis.media.Picture;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class TrayObject extends TrayIcon {

    TrayObject(Dimension size) {
        super(Picture.getImageIcon("icons/size16/user-default.png").getImage(),
                Main.NAME + " " + Main.VERSION);

        //Should say "you are not logged in" etc?

        setPopupMenu(new TrayMenu());
        addMouseListener(new TrayListener());
    }

    class TrayMenu extends PopupMenu {
        MenuItem exit;

        ActionListener trayListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem selectedItem = (MenuItem)e.getSource();
                if(selectedItem == exit) {
                    Main.wm.destroyAll();
                    System.exit(0);
                }
            }
        };
        TrayMenu() {
            exit = new MenuItem("Exit");
            exit.addActionListener(trayListener);

            add(exit);
        }

    }

    private class TrayListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int clicks = e.getClickCount();
            if (clicks == 2) {
                if(Main.wm.isHidden())
                    Main.wm.maximizeAll();
                else
                    Main.wm.minimizeAll();
            } else if ((clicks % 10) == 0) {
                TrayObject.this.displayMessage("Ouch!", "That hurts :(", MessageType.WARNING);
            }
        }
    }
}
