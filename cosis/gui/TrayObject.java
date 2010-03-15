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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

class TrayObject extends TrayIcon {

    

    TrayObject(Dimension size) {
        super(Picture.getImageIcon("cosisGIF.gif").getImage().getScaledInstance(
                size.width, size.height, Image.SCALE_SMOOTH),
                Main.NAME + Main.VERSION);        
        this.addMouseListener(new TrayListener());
    }

    private class TrayListener extends MouseAdapter {

        private final Timer timer = new Timer();
        private boolean doubleClick = false, betweenAClick = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            int clicks = e.getClickCount();
            if (clicks == 2) {

                doubleClick = true;

                if(Main.wm.isHidden())
                    Main.wm.maximizeAll();
                else
                    Main.wm.minimizeAll();

                betweenAClick = false;

            } else if (!betweenAClick && clicks == 1) {
                betweenAClick = true;
                long clickDelay = 350;

                /**
                 * I could grab the Windows doubleclick timout property, but
                 * on my system it was a very long 500ms without me noticing,
                 * and I'm a fast double cilcker. I'll have to test this on my
                 * laptop to see what should happen here.
                 */
//                if (Main.WIN)
//                    clickDelay = ((Integer) Toolkit.getDefaultToolkit().getDesktopProperty(
//                            "awt.multiClickInterval")).longValue();

                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        if(!doubleClick) {

                        System.out.println("Single click!");

                        } else
                        doubleClick = false;
                        
                        betweenAClick = false;
                    }
                }, clickDelay);

            } else if (clicks >= 10) {
                TrayObject.this.displayMessage("Ouch!", "That hurts :(", MessageType.WARNING);
            }
        }
    }
}
