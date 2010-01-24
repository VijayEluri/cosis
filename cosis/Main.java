//    Cosis - a cross-platform account manager
//    Copyright © 2010  Kavon Farvardin
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

package cosis;

import cosis.gui.Welcome;
import cosis.gui.WindowManager;
import cosis.util.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Kavon Farvardin
 */
public class Main {

    public static final boolean DEBUG = true;
    public static final String VERSION = "1.0-DEV";
    public static final String BUILD_DATE = "TESTING BUILD";
    public static final String NAME = "Cosis";
    public static final String CONTACT = "kavon.org/cosis.htm";
    public static final String[] AUTHORS = {"Kavon Farvardin"};

    public static boolean WIN = false, MAC = false, UNIX = false, TRAY = true;

    public static final WindowManager wm = new WindowManager();

    /**
     * @param args ignored
     */
    public static void main(String[] args) {
        
        /**
         * Determine the OS and Java Version, mostly for disabling the system tray as needed.
         */
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            WIN = true;
        } else if (osName.contains("Apple") || osName.contains("Mac") || osName.contains("OS X")) {
            MAC = true;
        } else {
            UNIX = true;
        }

        double javaVersion = Double.valueOf(System.getProperty("java.specification.version"));

        if (javaVersion < 1.6 && !MAC) {
            JOptionPane.showMessageDialog(null,
                    "Your system has an outdated version of the Java" +
                    " Runtime Environment (" + javaVersion + ").\n" +
                    "Please update your version to 1.6 or greater: www.java.com/getjava",
                    "Outdated Java - " + Main.NAME, JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else if(MAC) {
            TRAY = false;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Errors.log(ex);
        }

        wm.setMajorWindow(new Welcome());

    }

}
