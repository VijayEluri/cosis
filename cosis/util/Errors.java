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

package cosis.util;

import cosis.media.Picture;
import cosis.Main;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Logs errors and/or informs the user,
 * if necessary, of any problems or information.
 * @author Kavon Farvardin
 */
public abstract class Errors {

    /**
     * Displays a pop-up menu with a warning icon and a message.
     */
    public static void displayWarning(String warning) {
        JOptionPane.showMessageDialog(null, warning, "Warning! - "
                + Main.NAME, JOptionPane.WARNING_MESSAGE, Picture.getImageIcon("error.png"));
    }
    /**
     * Displays a pop-up menu with a warning icon and a message.
     * Overrides a JFrame's AlwaysOnTop setting for a second.
     */
    public static void displayWarning(String warning, JFrame frame) {
        frame.setAlwaysOnTop(false);
        JOptionPane.showMessageDialog(null, warning, "Warning! - "
                + Main.NAME, JOptionPane.WARNING_MESSAGE, Picture.getImageIcon("error.png"));
        frame.setAlwaysOnTop(true);
    }

    /**
     * Displays a pop-up menu with an informative icon and a message.
     */
    public static void displayInformation(String message) {
        JOptionPane.showMessageDialog(null, message, "Information - "
                + Main.NAME, JOptionPane.INFORMATION_MESSAGE, Picture.getImageIcon("info.png"));
    }
    /**
     * Displays a pop-up menu with an informative icon and a message.
     * Overrides a JFrame's AlwaysOnTop setting for a second.
     */
    public static void displayInformation(String message, JFrame frame) {
        frame.setAlwaysOnTop(false);
        JOptionPane.showMessageDialog(null, message, "Information - "
                + Main.NAME, JOptionPane.INFORMATION_MESSAGE, Picture.getImageIcon("info.png"));
        frame.setAlwaysOnTop(true);
    }

    public static void log(Exception ex) {

        try {
            PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/cosis_data/error_log.txt", true)));
            log.println(" | " + Utils.getTimestamp() + " | ");
            ex.printStackTrace(log);
            log.println();
            log.flush();
            log.close();
        } catch (IOException e) {
            displayWarning("Could not write to log file!");
        }

        if (Main.DEBUG)
            ex.printStackTrace();
    }

    public static void logSystemInformation() {
        throw new UnsupportedOperationException("Logging system information has not yet been implemented.");
    }
}