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

package cosis.gui.window;

import cosis.Main;
import cosis.gui.MajorWindowController;
import cosis.util.FileIO;
import cosis.gui.Profile;
import cosis.gui.ManagedWindow;
import cosis.media.Picture;
import cosis.security.Secure;
import cosis.util.Errors;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
    private JMenuItem newProfile, quit,  removeProfile,  about;
    private JLabel error;
    private SignInPanel panel = new SignInPanel();
    private int attempts = 0;

    public SignIn() {
        frame = new JFrame("Login - " + Main.NAME);
        frame.setResizable(Main.DEBUG);
        frame.addWindowListener(new MajorWindowController(this));
        frame.setContentPane(panel);
        frame.setJMenuBar(makeMenuBar());
        frame.setIconImage(Picture.getImageIcon("icons/size32/cosis.png").getImage());
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);

        panel.pwField.requestFocus();
    }

    public void minimize() {
        frame.setVisible(false);
    }

    public void destroy() {
        frame.dispose();
    }

    public void display() {
        frame.setVisible(true);
        frame.setExtendedState(Frame.NORMAL);
    }

    public void refresh() {
        panel.combomodel.removeAllElements();
        Profile[] list = FileIO.getProfiles();

        boolean listNotEmpty = !(list.length == 0);
        panel.signin.setEnabled(listNotEmpty);
        panel.remove.setEnabled(listNotEmpty);
        panel.publicCheck.setEnabled(listNotEmpty);
        panel.profileBox.setEnabled(listNotEmpty);
        panel.pwField.setEnabled(listNotEmpty);
        if(listNotEmpty) {
            panel.title.setText("Please login to continue");
        } else {
            panel.title.setText("Add a profile");
            panel.add.requestFocus();
        }

        for(Profile p : list)
            panel.combomodel.addElement(p);

        frame.validate();
    }
    
    public Component getComponentForLocation() {
        return (Component)frame;
    }

    private void removeProfile(Profile profile) {
        if(profile != null) {
            int answer = JOptionPane.showConfirmDialog(
                    frame, "Really remove \"" + profile.getName() + "\" by deleting:\n"
                    + profile.getFile().getAbsolutePath() + "\nfrom the filesystem?",
                    "Confirm Profile Deletion - " + Main.NAME, JOptionPane.OK_CANCEL_OPTION);
            if(answer == JOptionPane.OK_OPTION) {
                boolean sucessful = profile.getFile().delete();
                if(!sucessful) {
                    Errors.log(new IOException("Failed to delete "
                            + profile.getFile().getAbsolutePath() + " Writeable: "
                            + profile.getFile().canWrite()));
                } else {
                    refresh();
                }
            }
        }
    }

    private void lookBusy(boolean busy) {
        if(busy) {
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            panel.title.setText("Processing...");
        } else {
            frame.setCursor(null);
            panel.title.setText("Please login to continue");
        }

        error.setVisible(!busy);
        panel.pwField.setEnabled(!busy);
        panel.profileBox.setEnabled(!busy);
        panel.add.setEnabled(!busy);
        panel.remove.setEnabled(!busy);
        panel.signin.setEnabled(!busy);
        panel.publicCheck.setEnabled(!busy);
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

        newProfile = new JMenuItem("New Profile", Picture.getImageIcon("icons/size16/new.png"));
        newProfile.setMnemonic('N');
        newProfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        removeProfile = new JMenuItem("Remove Profile", Picture.getImageIcon("icons/size16/delete.png"));
        removeProfile.setMnemonic('R');
        removeProfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        quit = new JMenuItem("Exit", Picture.getImageIcon("icons/size16/exit.png"));
        quit.setMnemonic('E');
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        file.add(newProfile);
        file.add(removeProfile);
        file.addSeparator();
        file.add(quit);

        help = new JMenu("Help");
        help.setMnemonic('H');
        about = new JMenuItem("About", Picture.getImageIcon("icons/size16/help-about.png"));
        about.setMnemonic('A');
        help.add(about);

        MenuListener ml = new MenuListener();
        newProfile.addActionListener(ml);
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
        JCheckBox publicCheck;
        JPasswordField pwField;
        DefaultComboBoxModel combomodel;

        SignInPanel() {
            setLayout(new BorderLayout(2, 2));
            JPanel innerPanel = new JPanel();
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

            //title
            titleRow = new JPanel();
            titleRow.setLayout(new BoxLayout(titleRow, BoxLayout.X_AXIS));
            title = new JLabel("Please login to continue");
            title.setFont(new Font(title.getFont().toString(), Font.PLAIN, 14));
            title.setAlignmentX(CENTER_ALIGNMENT);
            titleRow.add(title);


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
            signin = new JButton("Login");

            pwRow.add(pwField);
            pwRow.add(Box.createHorizontalStrut(10));
            pwRow.add(signin);

            //public question
            JPanel pubRow = new JPanel();
            pubRow.setLayout(new BoxLayout(pubRow, BoxLayout.X_AXIS));
            publicCheck = new JCheckBox("Hide any visible passwords");
            pubRow.add(publicCheck);


            //buttonRow
            buttonRow = new JPanel();
            buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
            add = new JButton("New Profile", Picture.getImageIcon("icons/size16/list-add.png"));
            remove = new JButton("Remove Profile", Picture.getImageIcon("icons/size16/list-remove.png"));

            buttonRow.add(Box.createHorizontalStrut(25));
            buttonRow.add(add);
            buttonRow.add(Box.createHorizontalStrut(10));
            buttonRow.add(remove);
            buttonRow.add(Box.createHorizontalStrut(25));

            ButtonListener bl = new ButtonListener();
            signin.addActionListener(bl);
            pwField.addKeyListener(new QuickSignin());
            add.addActionListener(bl);
            remove.addActionListener(bl);


            ProfileChangeListener cl = new ProfileChangeListener();
            profileBox.addActionListener(cl);

            error = new JLabel("Incorrect password, please try again");
            error.setForeground(Color.RED);
            error.setAlignmentX(CENTER_ALIGNMENT);
            error.setVisible(false);


            innerPanel.add(Box.createVerticalStrut(5));
            innerPanel.add(titleRow);
            innerPanel.add(Box.createVerticalStrut(10));
            innerPanel.add(profileBox);
            innerPanel.add(Box.createVerticalStrut(10));
            innerPanel.add(pwRow);
            innerPanel.add(Box.createVerticalStrut(5));
            innerPanel.add(pubRow);
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
                if((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK)
                    panel.publicCheck.setSelected(true);
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
            panel.pwField.requestFocus();
            error.setVisible(false);
            attempts = 0;
        }
    }

    /**
     * JMenuBar's listener
     */
    private class MenuListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == about) {
                Main.wm.addMinor(new About());
            } else if (e.getSource() == newProfile) {
                Main.wm.addMinor(new CreateProfile());
            } else if (e.getSource() == quit) {
                Main.wm.destroyAll();
                System.exit(0);
            } else if (e.getSource() == removeProfile) {
                removeProfile((Profile)panel.profileBox.getSelectedItem());
            }
        }
    }

    /**
     * Handles events, signs the user in
     */
    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == panel.signin && panel.combomodel.getSize() > 0) {
                lookBusy(true);
                Authenticate auth = new Authenticate((Profile) panel.profileBox.getSelectedItem());
                auth.execute();
            }
            if (e.getSource() == panel.add) {
                Main.wm.addMinor(new CreateProfile());
            }
            if (e.getSource() == panel.remove) {
                removeProfile((Profile)panel.profileBox.getSelectedItem());
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

                    if ((attempts % 2) != 0 && attempts >= 3) {
                        Errors.log(new SecurityException(attempts + " bad password attempts on "
                                + user.getName() + ", " + user.getFile().getName()));

                        if(user.isRecoveryEnabled()) Errors.displayInformation("Hint: " + user.getHint());
                    }
                    panel.pwField.requestFocusInWindow();
                } else {
                    user.setUserInPublicLocation(true);
                    frame.setCursor(null);
                    frame.dispose();
                    Errors.displayInformation("I would load your profile now, but that's not implemented yet! :D");
                    System.exit(0);
                }
            } catch (Exception ignore) {
                Errors.log(ignore);
            }
        }
    }
}
