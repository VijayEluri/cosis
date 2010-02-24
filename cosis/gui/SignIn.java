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
import cosis.fileio.FileIO;
import cosis.fileio.Profile;
import cosis.media.Picture;
import cosis.security.Secure;
import cosis.util.Errors;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

/**
 * @author Kavon Farvardin
 */
public class SignIn implements ManagedWindow {

    private JFrame frame;
    private JMenu file,  help;
    private JMenuItem newProfile,  faq,  quit,  removeProfile,  about;
    private JLabel error;
    private SignInPanel panel = new SignInPanel();
    private int attempts = 0;

    public SignIn() {
        frame = new JFrame("Unlock Profile - " + Main.NAME + " " + Main.VERSION);
        frame.setResizable(Main.DEBUG);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowController(this));
        frame.setContentPane(panel);
        frame.setJMenuBar(makeMenuBar());
        frame.setIconImage(Picture.getImageIcon("cosis.png").getImage());
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
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
        panel.combomodel.removeAllElements();
        Profile[] list = FileIO.getProfiles();
        for(Profile p : list)
            panel.combomodel.addElement(p);
        frame.validate();
    }
    
    public Component getComponentForLocation() {
        return (Component)frame;
    }

    private void lookBusy(boolean busy) {
        if(busy)
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        else
            frame.setCursor(null);

        error.setVisible(!busy);
        panel.pwField.setEnabled(!busy);
        panel.profileBox.setEnabled(!busy);
        panel.add.setEnabled(!busy);
        panel.remove.setEnabled(!busy);
        panel.signin.setEnabled(!busy);
        file.setEnabled(!busy);
        help.setEnabled(!busy);
    }

    /**
     * Makes the JMenuBar for the JFrame
     */
    private JMenuBar makeMenuBar() {
        JMenuBar menubar = new JMenuBar();
        file = new JMenu("File");
        file.setMnemonic('F');

        newProfile = new JMenuItem("New Profile", Picture.getImageIcon("list_add_user.png"));
        newProfile.setMnemonic('N');
        newProfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        removeProfile = new JMenuItem("Remove Profile", Picture.getImageIcon("list_remove_user.png"));
        removeProfile.setMnemonic('R');
        removeProfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        quit = new JMenuItem("Exit", Picture.getImageIcon("exit.png"));
        quit.setMnemonic('E');
        file.add(newProfile);
        file.add(removeProfile);
        file.addSeparator();
        file.add(quit);

        help = new JMenu("Help");
        help.setMnemonic('H');
        faq = new JMenuItem("F.A.Q.", Picture.getImageIcon("help_hint.png"));
        faq.setMnemonic('Q');
        about = new JMenuItem("About", Picture.getImageIcon("help_about.png"));
        about.setMnemonic('A');
        help.add(faq);
        help.add(about);

        MenuListener ml = new MenuListener();
        newProfile.addActionListener(ml);
        faq.addActionListener(ml);
        quit.addActionListener(ml);
        removeProfile.addActionListener(ml);
        about.addActionListener(ml);

        menubar.add(file);
        menubar.add(help);
        return menubar;
    }

    private class SignInPanel extends JPanel {

        JLabel title;
        JPanel titleRow, buttonRow;
        JButton add, remove, signin;
        JComboBox profileBox;
        JPasswordField pwField;
        DefaultComboBoxModel combomodel;

        SignInPanel() {
            setLayout(new BorderLayout(2, 2));
            JPanel innerPanel = new JPanel();
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

            //title
            titleRow = new JPanel();
            titleRow.setLayout(new BoxLayout(titleRow, BoxLayout.X_AXIS));
            title = new JLabel("Select a Profile");
            title.setFont(new Font(title.getFont().toString(), Font.PLAIN, 14));
            title.setAlignmentX(CENTER_ALIGNMENT);
            titleRow.add(title);

            //buttonRow
            buttonRow = new JPanel();
            buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
            add = new JButton("New Profile", Picture.getImageIcon("list_add16.png"));
            remove = new JButton("Remove Profile", Picture.getImageIcon("list_remove16.png"));

            buttonRow.add(Box.createHorizontalStrut(25));
            buttonRow.add(add);
            buttonRow.add(Box.createHorizontalStrut(10));
            buttonRow.add(remove);
            buttonRow.add(Box.createHorizontalStrut(25));

            //comboRow
            JPanel comboRow = new JPanel();
            comboRow.setLayout(new BoxLayout(comboRow, BoxLayout.X_AXIS));
            combomodel = new DefaultComboBoxModel(FileIO.getProfiles());
            profileBox = new JComboBox();
            profileBox.setModel(combomodel);
            profileBox.setAlignmentX(CENTER_ALIGNMENT);
            comboRow.add(profileBox);

            //pwRow
            JPanel pwRow = new JPanel();
            pwRow.setLayout(new BoxLayout(pwRow, BoxLayout.X_AXIS));
            pwField = new JPasswordField();
            signin = new JButton("Unlock");

            pwRow.add(pwField);
            pwRow.add(Box.createHorizontalStrut(10));
            pwRow.add(signin);

            ButtonListener bl = new ButtonListener();
            signin.addActionListener(bl);
            pwField.addKeyListener(new QuickSignin());
            add.addActionListener(bl);
            remove.addActionListener(bl);


            ProfileChangeListener cl = new ProfileChangeListener();
            profileBox.addActionListener(cl);

            error = new JLabel("Incorrect password, please try again.");
            error.setForeground(Color.RED);
            error.setAlignmentX(CENTER_ALIGNMENT);
            error.setVisible(false);


            innerPanel.add(Box.createVerticalStrut(5));
            innerPanel.add(titleRow);
            innerPanel.add(Box.createVerticalStrut(10));
            innerPanel.add(profileBox);
            innerPanel.add(Box.createVerticalStrut(10));
            innerPanel.add(pwRow);
            innerPanel.add(Box.createVerticalStrut(3));
            innerPanel.add(error);
            innerPanel.add(new Box.Filler(new Dimension(1, 4), //min
                    new Dimension(1, 22), //pref
                    new Dimension(1, 32))); //max
            innerPanel.add(buttonRow);
            innerPanel.add(Box.createVerticalStrut(5));

            add(innerPanel, BorderLayout.CENTER);   //these are to provide
            add(new JPanel(), BorderLayout.EAST);   // space around the frame
            add(new JPanel(), BorderLayout.WEST);   // from borderlayout
            add(new JPanel(), BorderLayout.SOUTH);
            add(new JPanel(), BorderLayout.NORTH);
        }
    }

    private class QuickSignin extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                panel.signin.doClick();
            }
        }
    }

    /**
     * Clears the password field if the user chooses another profile from the combobox
     */
    private class ProfileChangeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            panel.pwField.setText("");
            error.setVisible(false);
            attempts = 0;
        }
    }

    /**
     * JMenuBar's listener
     */
    private class MenuListener implements ActionListener {

        public void actionPerformed(ActionEvent e) { //JMenuItem newProfile, faq, quit, removeProfile, about;
            if (e.getSource() == about) {
//                new About();
            } else if (e.getSource() == newProfile) {
                Main.wm.addMinor(new CreateProfile());
            } else if (e.getSource() == faq) {
//                new FAQ();
            } else if (e.getSource() == quit) {
                Main.wm.destroyAll();
                System.exit(0);
            } else if (e.getSource() == removeProfile) {
//                new RemoveProfile(frame, getMatchingProfile((String) panel.profileBox.getSelectedItem()));
            }
        }
    }

    /**
     * Handles events, signs the user in
     */
    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == panel.signin) {
                lookBusy(true);

                Authenticate auth = new Authenticate((Profile)panel.profileBox.getSelectedItem());
                auth.execute();
            }
            if (e.getSource() == panel.add) {
                Main.wm.addMinor(new CreateProfile());

            }
            if (e.getSource() == panel.remove) {
//                new RemoveProfile(frame, getMatchingProfile((String) panel.profileBox.getSelectedItem()));
            }
        }
    }

    private class Authenticate extends SwingWorker<Boolean, Object> {

        private Profile user;

        Authenticate(Profile user) {
            this.user = user;
        }

        @Override
        public Boolean doInBackground() {
            return user.authenticate(new Secure(
                    new String(panel.pwField.getPassword()),
                    user.getSalt()));
        }

        @Override
        protected void done() {
            try {
                if (!get()) {
                    lookBusy(false);
                    panel.pwField.setText("");
                    attempts++;

                    if (user.isRecoveryEnabled() && (attempts % 2) != 0 && attempts >= 3) {
                        Errors.displayInformation("Hint: " + user.getHint());
                    }
                    panel.pwField.requestFocusInWindow();
                } else {
                    frame.setCursor(null);
                    frame.dispose();
                    System.out.println("I would open the main window but that's not implemented yet!");
//                    new ShowProfile(user, auth, signTray);
                }
            } catch (Exception ignore) {
                Errors.log(ignore);
            }
        }
    }



}
