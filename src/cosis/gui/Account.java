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

import java.io.Serializable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.ImageIcon;

/**
 * Keeps track of the many informations that go along with each account
 * @author Kavon
 */
public class Account implements Serializable {
    /**
	 * This ID, 1448695048347751567L, is compatible with Cosis 1.0
	 */
	private static final long serialVersionUID = 1448695048347751567L;
	private String name, userID, password, notes, dateModified;
    private boolean favorite;
    private ImageIcon image;
    /**
     * The only constructor for all accounts. Put blank strings or empty ImageIcons if you
     * don't have anything to put for some of the fields.
     * @param name Name
     * @param image profile's image
     * @param userID User ID
     * @param password Password
     * @param notes Misc notes
     * @param dateModified last modification
     * @param favorite if this account is in the system tray
     */
    public Account(String name, ImageIcon image, String userID, String password,
            String notes, String dateModified,  boolean favorite) {

        this.name = name;
        this.image = image;
        this.userID = userID;
        this.password = password;
        this.notes = notes;
        this.dateModified = dateModified;
        this.favorite = favorite;
        
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public ImageIcon getImageIcon() {
        return image;
    }

    public void setImageIcon(ImageIcon newImage) {
        image = newImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String newID) {
        userID = newID;
    }
    /**
     * Uses the Secure of the designated Profile this Account belongs to.
     * @return decrypted password
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String getPassword() {
        return password;
    }
    /**
     * Uses the Secure of the designated Profile this Account belongs to.
     * @param newPassword encrypted and stored
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public void setPassword(String newPassword) {
        password = newPassword;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String newNotes) {
        notes = newNotes;
    }

    public String getLastEditDate() {
        return dateModified;
    }

    public void setLastEditDate(String date) {
        dateModified = date;
    }
    /**
     * AKA "Show in System Tray"
     */
    public void setFavorite(boolean toggle) {
        favorite = toggle;
    }
    /**
     * true if set to show in system tray, false otherwise
     */
    public boolean isFavorite() {
        return favorite;
    }
    /**
     * Compares the values getters from one account to the other.
     */
    public boolean equals(Account other) {
        return (getName().equals(other.getName()) &&
        		getImageIcon().equals(other.getImageIcon()) &&
        		getUserID().equals(other.getUserID()) &&
        		getPassword().equals(other.getPassword()) &&
        		getNotes().equals(other.getNotes()) &&
        		getLastEditDate().equals(other.getLastEditDate()) &&
        		(isFavorite() == other.isFavorite()));
    }
}
















