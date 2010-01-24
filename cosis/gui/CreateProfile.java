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
import cosis.fileio.Profile;
import cosis.media.Picture;
import cosis.security.Secure;
import cosis.util.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/**
 *
 * @author Kavon Farvardin
 * @since 0.3b
 */
class CreateProfile implements ManagedWindow {

    private JFrame frame;
    private JTextField nameField;
    private JPasswordField passwordField,  passwordField2;
    private JLabel error;
    private JButton create;

    /**
     * Makes the CreateProfileGUI GUI
     */
    public CreateProfile() {
        frame = new JFrame("Add a Profile");
        frame.setResizable(Main.DEBUG);
        frame.setIconImage(Picture.getImageIcon("cosis.png").getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //titleRow
        JPanel titleRow = new JPanel();
        titleRow.setLayout(new BoxLayout(titleRow, BoxLayout.X_AXIS));
        JLabel title = new JLabel("New Profile");
        JLabel person = new JLabel(Picture.getImageIcon("user.png"));
        title.setFont(new Font(title.getFont().toString(), Font.BOLD, 16));

        titleRow.add(Box.createHorizontalStrut(15));
        titleRow.add(person);
        titleRow.add(Box.createHorizontalStrut(10));
        titleRow.add(title);
        titleRow.add(Box.createHorizontalStrut(15));

        //leftBottom
        JPanel leftBottom = new JPanel();
        leftBottom.setLayout(new BoxLayout(leftBottom, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("Name:");
        JLabel password = new JLabel("Password:");
        JLabel password2 = new JLabel("Password Again:");

        leftBottom.add(name);
        leftBottom.add(Box.createVerticalStrut(15));
        leftBottom.add(password);
        leftBottom.add(Box.createVerticalStrut(15));
        leftBottom.add(password2);

        //rightBottom
        JPanel rightBottom = new JPanel();
        rightBottom.setLayout(new BoxLayout(rightBottom, BoxLayout.Y_AXIS));
        nameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        passwordField2 = new JPasswordField(15);

        QuickCreate qc = new QuickCreate();
        nameField.addKeyListener(qc);
        passwordField.addKeyListener(qc);
        passwordField2.addKeyListener(qc);

        rightBottom.add(nameField);
        rightBottom.add(Box.createVerticalStrut(5));
        rightBottom.add(passwordField);
        rightBottom.add(Box.createVerticalStrut(5));
        rightBottom.add(passwordField2);

        //Bottom
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.add(Box.createHorizontalStrut(5));
        bottom.add(leftBottom);
        bottom.add(Box.createHorizontalStrut(5));
        bottom.add(rightBottom);
        bottom.add(Box.createHorizontalStrut(5));

        //createRow
        JPanel createRow = new JPanel();
        createRow.setLayout(new BoxLayout(createRow, BoxLayout.X_AXIS));
        error = new JLabel("***********************"); //fixes bug in Windows XP
        error.setForeground(Color.RED);
        error.setVisible(false);
        create = new JButton("Create", Picture.getImageIcon("apply.png"));
        create.addActionListener(new ButtonListen());

        createRow.add(new Box.Filler(new Dimension(4, 1), //min
                new Dimension(250, 1), //pref
                new Dimension(1500, 1))); //max
        createRow.add(error);
        createRow.add(Box.createHorizontalStrut(10));
        createRow.add(create);
        createRow.add(Box.createHorizontalStrut(10));

        panel.add(Box.createVerticalStrut(15));
        panel.add(titleRow);
        panel.add(Box.createVerticalStrut(10));
        panel.add(bottom);
        panel.add(Box.createVerticalStrut(10));
        panel.add(createRow);
        panel.add(Box.createVerticalStrut(10));

        frame.setContentPane(panel);       
        
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(Main.wm.getMajorWindow().getComponentForLocation());
    }

    public void minimize() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void maximize() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void destroy() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void display() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void refresh() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Component getComponentForLocation() {
        return (Component)frame;
    }
    private class QuickCreate extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                create.doClick();
            }
        }
    }

    /**
     * Worker class to create the profile. Takes a long time so this
     * is needed in order to have a responsive UI
     */
    private class MakeProfile extends SwingWorker<Boolean, Object> {

        @Override
        public Boolean doInBackground() {
            return Profile.generateProfile(nameField.getText(), new String(passwordField.getPassword()), Secure.createSalt());
        }

        @Override
        protected void done() {
            try {
                if (get()) {
                    if (Main.firstRun) {
//                        currentframes[0].dispose();
                        frame.dispose();
//                        new SignIn(false);

                        Main.firstRun = false;
                    } else {
//                        currentframes[0].setEnabled(true);
                        frame.dispose();
                    }
                } else {
                    Utils.showJLabelError(11, error);
                    nameField.setEnabled(true);
                    passwordField.setEnabled(true);
                    passwordField2.setEnabled(true);
                    create.setEnabled(true);
                    nameField.setText("");
                    passwordField.setText("");
                    passwordField2.setText("");
                }
                frame.setCursor(null);
            } catch (Exception ignore) {
            }
        }
    }

    /**
     * Lisenter for the JButton
     */
    private class ButtonListen implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == create) {
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                if (thereAreErrors() == false) {
                    error.setVisible(false);
                    nameField.setEnabled(false);
                    passwordField.setEnabled(false);
                    passwordField2.setEnabled(false);
                    create.setEnabled(false);
                    (new MakeProfile()).execute();
                } else {
                    frame.setCursor(null);
                }
            }
        }
    }

    /**
     * Checks for Utils the user makes while entering data for a Profile
     * @return Returns true if there is an error, false if it is error free.
     * @note I know its big and ugly, but I'm not going to rewrite somthing which works
     */
    private boolean thereAreErrors() {
        if (nameField.getText().length() < 0) { //name's length negative?
            Utils.showJLabelError(0, error);
            return true;
        } else if (nameField.getText().length() == 0) { //name field empty?
            Utils.showJLabelError(1, error);
            return true;
        } else if (nameField.getText().length() > 0 && nameField.getText().length() < 3) { //name between 0 and 3 characters?
            Utils.showJLabelError(2, error);
            return true;
        } else if (nameField.getText().length() > 20) { //name larger than 20 characters?
            Utils.showJLabelError(3, error);
            return true;
        } else if (Utils.hasFirstLetter(nameField.getText()) == false) { //does name start with a letter A-Z or a-z?
            Utils.showJLabelError(4, error);
            return true;
        } else if (passwordField.getPassword().length < 0) { //password length negative?
            Utils.showJLabelError(0, error);
            return true;
        } else if (passwordField.getPassword().length == 0) { //password field empty?
            Utils.showJLabelError(1, error);
            return true;
        } else if (passwordField.getPassword().length > 0 && passwordField.getPassword().length < 6) { //password between 0 and 6 characters?
            Utils.showJLabelError(5, error);
            passwordField.setText("");
            passwordField2.setText("");
            return true;
        } else if (passwordField.getPassword().length > 50) { //password over 50 characters?
            Utils.showJLabelError(6, error);
            passwordField.setText("");
            passwordField2.setText("");
            return true;
        } else if (passwordField2.getPassword().length < 0) { //password field 2 a negative length?
            Utils.showJLabelError(0, error);
            return true;
        } else if (passwordField2.getPassword().length == 0) { //password field 2 empty?
            Utils.showJLabelError(1, error);
            return true;
        } else if (Arrays.equals(passwordField.getPassword(), passwordField2.getPassword()) == false) { //compare passwordfields
            Utils.showJLabelError(8, error);
            passwordField.setText("");
            passwordField2.setText("");
            return true;
        } else if (Arrays.equals(nameField.getText().toCharArray(), passwordField.getPassword())) {
            Utils.showJLabelError(9, error);
            passwordField.setText("");
            passwordField2.setText("");
            return true;
        } else if (Utils.hasIllegalCharacters(nameField.getText())) {
            Utils.showJLabelError(10, error);
            nameField.setText("");
            return true;
        } else {
            return false;
        }
    }
}