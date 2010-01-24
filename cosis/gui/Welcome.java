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
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Welcome screen
 * @author Kavon Farvardin
 * @since Version 0.1b
 */
class Welcome implements ManagedWindow {

    private JButton add, exit;

    public Welcome() {
        JFrame frame = new JFrame(Main.NAME + " " + Main.VERSION);
        frame.setResizable(Main.DEBUG);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(Picture.getImageIcon("icons/cosis.png").getImage());
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

    private class MainPanel extends JPanel {

        MainPanel() {
            JPanel innerPanel; //where all other panels go
            JPanel welcomeRow; //the welcome message in big bold font
            JPanel messageRow; //message to guide the user to make a new profile
            JPanel buttonRow;  //buttons at the bottom
            JLabel welcome, messageOne, messageTwo;

            //welcomeRow
            welcomeRow = new JPanel();
            welcome = new JLabel("Welcome to " + Main.NAME + "!");
            welcome.setFont(new Font(welcome.getFont().toString(), Font.BOLD, 16));

            welcomeRow.setLayout(new BoxLayout(welcomeRow, BoxLayout.X_AXIS));
            welcomeRow.add(welcome);

            //messageRow
            messageRow = new JPanel();
            messageOne = new JLabel("You do not have a Profile set up yet. To get started");
            messageTwo = new JLabel("with " + Main.NAME + ", create your own Profile with the Add button below.");

            messageRow.setLayout(new BoxLayout(messageRow, BoxLayout.Y_AXIS));
            messageRow.add(messageOne);
            messageRow.add(messageTwo);

            //messageHolder
            JPanel messageHolder = new JPanel();
            messageHolder.setLayout(new BoxLayout(messageHolder, BoxLayout.X_AXIS));
            messageHolder.add(messageRow);

            //buttonRow
            buttonRow = new JPanel();
            add = new JButton("Add", Picture.getImageIcon("icons/list_add16.png"));
            exit = new JButton("Exit", Picture.getImageIcon("icons/exit.png"));

            buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
            buttonRow.add(Box.createVerticalStrut(1)); //a vertical in an X_AXIS shoves it all to the other side
            buttonRow.add(add);
            buttonRow.add(Box.createHorizontalStrut(15));
            buttonRow.add(exit);

            //innerPanel
            innerPanel = new JPanel();
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
            innerPanel.add(welcomeRow);
            innerPanel.add(Box.createVerticalStrut(10));
            innerPanel.add(messageHolder);
            innerPanel.add(Box.createVerticalStrut(10));
            innerPanel.add(buttonRow);

            //MainPanel
            setLayout(new BorderLayout(0, 0));
            add(innerPanel, BorderLayout.CENTER);   //these are to provide
            add(new JPanel(), BorderLayout.EAST);   // space around the frame
            add(new JPanel(), BorderLayout.WEST);   // from borderlayout
            add(new JPanel(), BorderLayout.SOUTH);
            add(new JPanel(), BorderLayout.NORTH);

            clickListen cl = new clickListen();
            add.addActionListener(cl);
            exit.addActionListener(cl);
        }
    }

    private class clickListen implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == add) {
//                new CreateProfile(true);
            }
            if (e.getSource() == exit) {
                System.exit(0);
            }
        }
    }
}
