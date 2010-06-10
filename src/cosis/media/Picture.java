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

package cosis.media;

import cosis.util.*;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Stores a Picture
 * @author Kavon Farvardin
 */
public class Picture {
    private ImageIcon icon;
    private String path;
    private boolean custom;
    Picture(String path) {
        icon = getImageIcon(path);
        this.path = path;
        custom = path.startsWith("generic/") ? false : true;
    }
    /**
     * @return the ImageIcon
     */
    public ImageIcon getIcon() {
        return icon;
    }
    /**
     * @return the path of this picture
     */
    public String getPath() {
        return path;
    }
    /**
     * @return true if the image is not one included with Cosis, false otherwise.
     */
    public boolean isCustom() {
        return custom;
    }

    /**
     * Finds an image to be converted to an ImageIcon. Image must be within the jar file
     * @param path path of image
     * @return Returns an ImageIcon of the image in the path specified
     */
    public static ImageIcon getImageIcon(String path) {
        if(path.length() == 0) return new ImageIcon();
        if(path.startsWith("generic/") || !(path.contains("\\") || path.startsWith("/"))) {
            try {
                URL imgURL = Picture.class.getResource(path);
                return new ImageIcon(imgURL);
            } catch (Exception ex) {
                Errors.log(new FileNotFoundException(path));
                return new ImageIcon();
            }
        } else {
            try {
                ImageIcon icon = new ImageIcon(new File(path).getCanonicalPath());
                if(icon.getIconWidth() != 32 || icon.getIconHeight() != 32) {
                    Image image = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    return new ImageIcon(image);
                } else {
                    return icon;
                }
            } catch (IOException ex) {
                Errors.log(new FileNotFoundException(path));
                return getImageIcon("empty32.png");
            }
        }
    }

    /**
     * @return local path of images
     */
    public static Picture[] getGenericPictures() {
        /*Fucking most ridiculous thing ever...
         Apparently it is almost motherfucking impossible
         to list the files in a folder within the .jar file in Java!
         Now I've got to do this messy shit of hard coding these.
         I am not going to waste hours of my time to get this working
         "properly" when I doubt I'll be adding many more images at all.*/
        String[] imagesIncluded = {
            "generic/account.png",
            "generic/admin.png",
            "generic/alarmclock.png",
            "generic/art.png",
            "generic/audio_headset.png",
            "generic/blogging.png",
            "generic/book.png",
            "generic/cards.png",
            "generic/chess.png",
            "generic/computer.png",
            "generic/cookie.png",
            "generic/dice.png",
            "generic/email.png",
            "generic/guy.png",
            "generic/home.png",
            "generic/im.png",
            "generic/joystick.png",
            "generic/laptop.png",
            "generic/lightning.png",
            "generic/love.png",
            "generic/music.png",
            "generic/network.png",
            "generic/online_banking.png",
            "generic/password.png",
            "generic/rocket.png",
            "generic/secure.png",
            "generic/smiley.png",
            "generic/social_networking.png",
            "generic/ssh.png",
            "generic/star.png",
            "generic/star2.png",
            "generic/tux.png",
            "generic/video.png",
            "generic/website.png",
            "generic/writing.png"
        };
        Picture[] pictures = new Picture[imagesIncluded.length];
        for(int i = 0; i < imagesIncluded.length; i++) {
            pictures[i] = new Picture(imagesIncluded[i]);
        }
        return pictures;
    }
}
