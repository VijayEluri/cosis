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
import java.awt.Component;
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
     * @param warning The message to be displayed.
     */
    public static void displayWarning(String warning) {
        JOptionPane.showMessageDialog(null, warning, "Warning! - "
                + Main.NAME, JOptionPane.WARNING_MESSAGE, Picture.getImageIcon("error.png"));
    }
    /**
     * @param warning message to be displayed
     * @param frame the frame that will have it's AlwaysOnTop setting temporarily turned off to display the message.
     */
    public static void displayWarning(String warning, Component loc) {
        JOptionPane.showMessageDialog(loc, warning, "Warning! - "
                + Main.NAME, JOptionPane.WARNING_MESSAGE, Picture.getImageIcon("error.png"));
    }

    /**
     * @param message The message to be displayed.
     */
    public static void displayInformation(String message) {
        JOptionPane.showMessageDialog(null, message, "Information - "
                + Main.NAME, JOptionPane.INFORMATION_MESSAGE, Picture.getImageIcon("info.png"));
    }
    /**
     * @param message message to be displayed
     * @param frame the frame that will have it's AlwaysOnTop setting temporarily turned off to display the message.
     */
    public static void displayInformation(String message, Component loc) {
        JOptionPane.showMessageDialog(loc, message, "Information - "
                + Main.NAME, JOptionPane.INFORMATION_MESSAGE, Picture.getImageIcon("info.png"));
    }

    /**
     * @param ex the Exception to output to log file
     */
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

    /**
     * Prints information about the operating system to the log file for error reporting purposes
     */
    public static void logSystemInformation() {
        throw new UnsupportedOperationException("Logging system information has not yet been implemented.");
    }
}