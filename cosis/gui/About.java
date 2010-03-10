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
        frame = new JFrame("About");
        frame.setResizable(Main.DEBUG);
        frame.setIconImage(Picture.getImageIcon("cosis.png").getImage());
        frame.addWindowListener(new WindowController(this));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(Main.NAME);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("a cross-platform account manager");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("Version:  " + Main.VERSION);
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

        panel.add(Box.createVerticalStrut(10));

        name.setFont(new Font(fontName, Font.BOLD, 18));
        panel.add(name);

        panel.add(Box.createVerticalStrut(2));

        subtitle.setFont(new Font(fontName, Font.ITALIC, 13));
        panel.add(subtitle);

        panel.add(Box.createVerticalStrut(10));

        for (JLabel person : authors) {
            person.setFont(new Font(fontName, Font.PLAIN, 14));
            panel.add(person);
            panel.add(Box.createVerticalStrut(5));
        }

        panel.add(Box.createVerticalStrut(5));

        version.setFont(regular);
        panel.add(version);

        panel.add(Box.createVerticalStrut(5));

        homepage.setFont(regular);
        panel.add(homepage);

        panel.add(Box.createVerticalStrut(5));

        contact.setFont(regular);
        panel.add(contact);

        panel.add(Box.createVerticalStrut(5));

        build.setFont(regular);
        panel.add(build);

        panel.add(Box.createRigidArea(new Dimension(325, 10)));

        frame.setContentPane(panel);

        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(Main.wm.getMajorWindow().getComponentForLocation());
    }

    public void minimize() {
        //no tray support yet
    }

    public void maximize() {
        //no tray support yet
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
