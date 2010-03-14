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
import java.awt.Image;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

class TrayObject extends TrayIcon {

    private final Timer timer = new Timer();
    private boolean doubleClick = false, betweenAClick = false;

    TrayObject(Dimension size) {
        super(Picture.getImageIcon("cosisGIF.gif").getImage().getScaledInstance(
                size.width, size.height, Image.SCALE_SMOOTH),
                Main.NAME + Main.VERSION);

        MouseListener mouseListener = new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                int clicks = e.getClickCount();
                if(clicks == 2) {
                    doubleClick = true;
                } else if(clicks == 1 && !betweenAClick) {
                    betweenAClick = true;
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            if(doubleClick) {
                                System.out.println("Double click!");
                                doubleClick = false;
                            } else {
                                System.out.println("Single click!");
                            }
                            betweenAClick = false;
                        }

                    }, 275);

                } else if(clicks >= 10) {
                    TrayObject.this.displayMessage("Ouch!", "That hurts :(", MessageType.WARNING);
                }
            }

            //I don't care about these actions.
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
        };

        this.addMouseListener(mouseListener);

    }
}
