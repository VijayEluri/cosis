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

import cosis.gui.MinorWindowController;
import cosis.Main;
import cosis.gui.ManagedWindow;
import cosis.media.Picture;
import cosis.util.Errors;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Shows information about this program
 */
public class About implements ManagedWindow {

    private JFrame frame;

    /**
     * Displays the AboutGUI window, containing information about this program
     * @author Kavon Farvardin
     * @since the beginning of time
     */
    public About() {
        //System.out.println(Errors.getReportingInformation());
        frame = new JFrame("About");
        frame.setResizable(Main.DEBUG);
        frame.setIconImage(Picture.getImageIcon("icons/size16/help-about.png").getImage());
        frame.addWindowListener(new MinorWindowController(this));

        //Right Side
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(Main.NAME);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("a cross-platform account manager");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("Version:  " + Main.VERSION 
                + (Main.DEBUG ? "-DEV" : ""));

        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel[] authors = new JLabel[Main.AUTHORS.length];
        for (int i = 0; i < Main.AUTHORS.length; i++) {
            authors[i] = new JLabel(Main.AUTHORS[i]);
            authors[i].setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        JLabel homepage = new JLabel("Homepage:  " + Main.HOMEPAGE);
        homepage.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contact = new JLabel("Contact:  " + Main.CONTACT);
        contact.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel build = new JLabel("Build Date:  " + Main.BUILD_DATE);
        build.setAlignmentX(Component.CENTER_ALIGNMENT);

        String fontName = version.getFont().toString();
        Font regular = new Font(fontName, Font.PLAIN, 12);

        textPanel.add(Box.createVerticalStrut(10));

        name.setFont(new Font(fontName, Font.BOLD, 18));
        textPanel.add(name);

        textPanel.add(Box.createVerticalStrut(2));

        subtitle.setFont(new Font(fontName, Font.ITALIC, 13));
        textPanel.add(subtitle);

        textPanel.add(Box.createVerticalStrut(10));

        for (JLabel person : authors) {
            person.setFont(new Font(fontName, Font.PLAIN, 14));
            textPanel.add(person);
            textPanel.add(Box.createVerticalStrut(5));
        }

        textPanel.add(Box.createVerticalStrut(5));

        version.setFont(regular);
        textPanel.add(version);

        textPanel.add(Box.createVerticalStrut(5));

        homepage.setFont(regular);
        textPanel.add(homepage);

        textPanel.add(Box.createVerticalStrut(5));

        contact.setFont(regular);
        textPanel.add(contact);

        textPanel.add(Box.createVerticalStrut(5));

        build.setFont(regular);
        textPanel.add(build);

        textPanel.add(Box.createRigidArea(new Dimension(325, 10)));
        
        //Left Side
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BoxLayout(iconPanel,  BoxLayout.Y_AXIS));
        
        JLabel icon = new JLabel(Picture.getImageIcon("icons/size256/cosis.png"));
        iconPanel.add(icon);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        
        mainPanel.add(iconPanel);
        mainPanel.add(Box.createHorizontalStrut(5));
        mainPanel.add(textPanel);

        frame.setContentPane(mainPanel);

        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(Main.wm.getMajorWindow().getComponentForLocation());
    }

    public void minimize() {
        frame.setVisible(false);
    }

    public void destroy() {
        Main.wm.removeMinor(this);
        frame.dispose();
    }

    public void display() {
        frame.setVisible(true);
    }

    public void refresh() {
        frame.validate();
    }

    public Component getComponentForLocation() {
        return (Component)frame;
    }
}
