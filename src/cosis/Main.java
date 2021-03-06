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

import cosis.util.FileIO;
import cosis.gui.window.SignIn;
import cosis.gui.window.Welcome;
import cosis.gui.WindowManager;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.security.Security;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Kavon Farvardin
 */
public class Main {

    public static final boolean DEBUG = true;
    public static final String VERSION = "1.0";
    public static final String BUILD_DATE = "TESTING BUILD";
    public static final String NAME = "Cosis";
    public static final String CONTACT = "cosis.support@gmail.com";
    public static final String HOMEPAGE = "kavon.org/cosis.htm";
    public static final String[] AUTHORS = {"Kavon Farvardin"};

    public static boolean WIN = false, MAC = false, UNIX = false, TRAY, FIRST_RUN;

    public static WindowManager wm;

    public static void main(String[] args) {

        if(GraphicsEnvironment.isHeadless()) {
            System.out.println("\nSorry, Cosis is not command-line friendly," +
                    "\nalthough it wouldn't be hard to make a CLI interface...\n" +
                    "send me an email if you want one: " + CONTACT + "\n");
            System.exit(0);
        }

        //Check the OS and Java Version. Version >= 1.6 && OS != Macintosh
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            WIN = true;
        } else if (osName.startsWith("Mac")) {
            MAC = true;
        } else {
            UNIX = true;
        }

        try {
            double javaVersion = Double.parseDouble(System.getProperty("java.specification.version"));
            if (javaVersion < 1.5) {
                JOptionPane.showMessageDialog(null,
                        "Your system has an outdated version of the Java"
                        + " Runtime Environment (" + javaVersion + ").\n"
                        + "Please update your version.",
                        "Outdated Java - " + Main.NAME, JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        } catch (NumberFormatException x) {
            System.err.println("COSIS - VERSION CHECKING FAILED");
            x.printStackTrace();
            System.exit(1);
        }

        /**
         * From here on, we're on a normal OS with a proper version of Java.
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception x) {
            System.err.println("COSIS - NATIVE SYSTEM THEME SET FAILED");
            x.printStackTrace();
            System.exit(1);
        }

        TRAY = SystemTray.isSupported();
        FIRST_RUN = FileIO.isFirstRun();
        Security.addProvider(new BouncyCastleProvider());
        wm = new WindowManager();
        //Start the GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (FIRST_RUN) {
                    wm.setMajorWindow(new Welcome());
                } else {
                    wm.setMajorWindow(new SignIn());
                }
            }
        });        
    }
}
