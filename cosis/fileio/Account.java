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

package cosis.fileio;

import cosis.util.Errors;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * Keeps track of the many informations that go along with each account
 * @author Kavon
 */
public class Account {
    private String name, path, userID, notes, dateCreated, dateModified;
    private boolean passwordHidden, favorite;
    private byte[] password;
    private Profile profile;
    public Account(String name, String path, String userID, String password,
            String notes, String dateCreated, String dateModified, 
            boolean passwordHidden, boolean favorite, Profile associatedProfile)
            throws IllegalBlockSizeException, BadPaddingException {

        this.profile = associatedProfile;

        this.name = name;
        this.path = path;
        this.userID = userID;
        this.password = profile.getSecure().encrypt(password);
        this.notes = notes;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.passwordHidden = passwordHidden;
        this.favorite = favorite;
        
    }
    public String getName() {
        return name;
    }
    public void setName(String newName) {
        name = newName;
    }
    public String getImagePath() {
        return path;
    }
    public void setImagePath(String newPath) {
        path = newPath;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String newID) {
        userID = newID;
    }
    public String getDecryptedPassword() throws IllegalBlockSizeException, BadPaddingException {
        return profile.getSecure().decrypt(password)[0];
    }
    public void setPassword(String newPassword) throws IllegalBlockSizeException, BadPaddingException {
        password = profile.getSecure().encrypt(newPassword);
    }
    public boolean isPasswordHidden() {
        return passwordHidden;
    }
    public void setPasswordHidden(Boolean passwordHidden) {
        this.passwordHidden = passwordHidden;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String newNotes) {
        notes = newNotes;
    }
    public String getDateCreated() {
        return dateCreated;
    }
    public String getLastEditDate() {
        return dateModified;
    }
    public void setLastEditDate(String date) {
        dateModified = date;
    }
    public void setFavorite(boolean toggle) {
        favorite = toggle;
    }
    public boolean isFavorite() {
        return favorite;
    }
    public String[] getAllData() throws IllegalBlockSizeException, BadPaddingException {
        String[] allData = {
            name, path, userID, profile.getSecure().decrypt(password)[0], notes, dateCreated, dateModified, Boolean.toString(passwordHidden), Boolean.toString(favorite)
        };
        return allData;
    }
    public boolean equals(Account other) {
        try {
            String[] acct1 = getAllData();
            String[] acct2 = other.getAllData();
            for (int i = 0; i < acct1.length; i++) {
                if (!acct1[i].equals(acct2[i])) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            Errors.log(ex);
            return false;
        }
    }
}
