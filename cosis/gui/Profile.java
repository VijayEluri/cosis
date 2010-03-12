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
import cosis.security.Secure;
import cosis.util.Errors;
import cosis.util.FileIO;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * A profile
 * @author Kavon
 */
public class Profile {

    private File file;
    private DataInputStream in;
    private Secure auth, newAuth = null;

    private String name, salt, verify, hint, path;
    private byte[] encryptedVerification, encryptedAccounts;

    private boolean recovery, backup;

    private int timeout;

    private ArrayList<Account> accounts;

    /**
     * Used by the sign in window to display available profiles
     * @param data file to load for this profile
     */
    public Profile(File data) {        
        load(data);
    }

    private void load(File data) {
        try {
            file = data;
            in = new DataInputStream(new FileInputStream(file));
            name = in.readUTF();
            salt = in.readUTF();
            verify = in.readUTF();
            int verificationLength = in.readInt();
            encryptedVerification = readByteArray(in, verificationLength);

            recovery = in.readBoolean();
            if (recovery)
                hint = in.readUTF();

            backup = in.readBoolean();
            if (backup)
                path = in.readUTF();

            timeout = in.readInt();

            encryptedAccounts = readByteArray(in);
            
        } catch(EOFException ex) {
            int answer = JOptionPane.showConfirmDialog(null,
                    "A file with the .db8 extension: " + file.getName()
                    + "\nwas found in the Cosis data folder, but is"
                    + "\ncorrupt and otherwise unreadable, delete this file?",
                    "Unrecognized Profile Found - " + Main.NAME, JOptionPane.OK_CANCEL_OPTION);
            if (answer == JOptionPane.OK_OPTION) {
                try {
                    in.close();
                    boolean sucessful = file.delete();
                    if (!sucessful) {
                        Errors.log(new IOException("Failed to delete "
                                + file.getAbsolutePath() + " Writeable: "
                                + file.canWrite()));
                    }
                } catch (IOException innerEx) {
                    Errors.log(innerEx);
                }
            }
            System.exit(0);
        } catch (Exception ex) {
            Errors.log(ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Errors.log(ex);
            }
        }
    }

    /**
     *
     * @param authKey instance of Secure which will be tested
     * @return true if and only if the provided Secure instance can decrypt
     * all user data correctly
     */
    public boolean authenticate(Secure authKey) {
        try {
            String testVerify = authKey.decrypt(encryptedVerification)[0];
            if (testVerify.equals(verify)) {
                auth = authKey;
            } else {
                throw new SecurityException("Decryption resulted in invalid verification.");
            }

            String[] rawData = auth.decrypt(encryptedAccounts);
            String act_name, act_path, userID, password, notes, dateCreated, dateModified;
            boolean passwordHidden, favorite;

            accounts = new ArrayList<Account>();
            for (int i = 0; i < rawData.length; i++) {
                act_name = rawData[i++];
                act_path = rawData[i++];
                userID = rawData[i++];
                password = rawData[i++];
                notes = rawData[i++];
                dateCreated = rawData[i++];
                dateModified = rawData[i++];
                passwordHidden = Boolean.parseBoolean(rawData[i++]);
                favorite = Boolean.parseBoolean(rawData[i]);
                accounts.add(new Account(act_name, act_path, userID, password,
                        notes, dateCreated, dateModified, passwordHidden, favorite, this));
            }

            return true;
        } catch (Exception ex) {
            //No point in logging this exception, it means the password was wrong
            return false;
        }
    }

    /**
     * Creates a profile and saves it to the cosis data folder.
     * @param name name of Profile
     * @param salt salt for Profile
     * @param password password for Profile
     * @return true only if the profile was created sucessfully
     */
    public static boolean generateProfile(String name, String password, String salt) {
        try {
            String filename = FileIO.getFileNameForName(name);
            if(filename == null)
                return false;   //name is a duplicate

            //make the file
            File userFile = new File(System.getProperty("user.dir") + "/cosis_data", filename);
            userFile.createNewFile();

            //yea that's right, this is my awesome char array
            char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', '.', ',', '/', '<', '>',
            ':', ':', '[', ']', '{', '}', '-', '_', '+', '=',
            '|', '(', ')', '&', '*', '!', '@', '#', '$', '%',
            '^', '`', '~'};
            
            //make a random authentication string
            String verification = "";
            SecureRandom rand = new SecureRandom();
            for (int i = 0; i < 128; i++) {
                verification += letters[rand.nextInt(letters.length)];
            }

            byte[] encryptedVerif = new Secure(password, salt).encrypt(verification);


            //now let's start writing to the user's file!

            DataOutputStream out = new DataOutputStream(new FileOutputStream(userFile));
            out.writeUTF(name);
            out.writeUTF(salt);
            out.writeUTF(verification);

            out.writeInt(encryptedVerif.length);
            out.write(encryptedVerif);

            //here we begin with preferences, these are default values
            out.writeBoolean(false);    //recovery
            out.writeBoolean(false);    //backup
            out.writeInt(15);           //timeout length in minutes

            out.flush();
            out.close();

            //if we've made it here, we're sucessful
            return true;
        } catch (Exception ex) {
            Errors.log(ex);
            Errors.displayWarning("Problem creating profile!");
            return false;
        }
    }

    /**
     * Saves this profile in its current state.
     */
    public void save() {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));

            out.writeUTF(name);
            out.writeUTF(salt);
            out.writeUTF(verify);

            if(newAuth != null)
                encryptedVerification = newAuth.encrypt(verify);

            out.writeInt(encryptedVerification.length);
            out.write(encryptedVerification);

            out.writeBoolean(recovery);
            if(recovery)
                out.writeUTF(hint);

            out.writeBoolean(backup);
            if(backup)
                out.writeUTF(path);

            out.writeInt(timeout);

            if (newAuth != null) {
                for (int i = 0; i < accounts.size(); i++) {
                    out.write(newAuth.encrypt(accounts.get(i).getAllData()));
                }
            } else {

                for (int i = 0; i < accounts.size(); i++) {
                    out.write(auth.encrypt(accounts.get(i).getAllData()));
                }

            }

            out.flush();
            out.close();



        } catch (Exception ex) {
            Errors.log(ex);
        }
    }

    /**
     * Reads the DataInputStream byte by byte until the given length
     * @param in DataInputStream
     * @param length Number of bytes to read
     * @return byte[] with a length == the parameter length
     */
    public static byte[] readByteArray(DataInputStream in, int length) {
        try {
            byte[] chunk = new byte[length];

            for (int i = 0; i < chunk.length; i++) {
                chunk[i] = in.readByte();
            }

            return chunk;
        } catch (IOException ex) {
            Errors.log(ex);
            return new byte[0];
        }
    }

    /**
     * Reads the rest of the DataInputStream's bytes
     * @param in the stream
     * @return teh rest of the stream
     */
    public static byte[] readByteArray(DataInputStream in) {
        try {
            return readByteArray(in, in.available());
        } catch (IOException ex) {
            Errors.log(ex);
            return new byte[0];
        }
    }

    /**
     * @return name of this profile
     */
    public String getName() {
        return name;
    }

    /**
     * @param name name to assign to this profile
     * @return true if name was successfully changed, false
     * for any other reason including duplicate profile name
     */
    public boolean setName(String name) {
        String newFileName = FileIO.getFileNameForName(name);
        if(newFileName == null)
            return false;

        this.name = name;
        return file.renameTo(new File(System.getProperty("user.dir") + "/cosis_data", newFileName));
    }

    /**
     * @return the salt of this profile
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @return the instance of Secure which can encrypt/decrypt its data
     */
    public Secure getSecure() {
        return auth;
    }

    /**
     * @param newSecure assigns this profile to use this instance of Secure
     */
    public void setSecure(Secure newSecure) {
        newAuth = newSecure;
        save();
        auth = newAuth;
        newAuth = null;
        load(file);
        authenticate(auth);
    }

    /**
     * @return the file which this profile saves to etc
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the idle logout time setting
     */
    public int getTimeout() {
        return timeout;
    }

      /**
       * @param time sets profile to this timeout setting
       */
      public void setTimeout(int time) {
        timeout = time;
    }

    /**
     * @return whether or not backup is enabled
     */
    public boolean isBackupEnabled() {
        return backup;
    }

    /**
     * @param pool sets the backup setting of this profile
     */
    public void setBackupEnabled(boolean pool) {
        backup = pool;
    }

    /**
     * @return the data backup file path
     */
    public String getBackupPath() {
        if (isBackupEnabled()) {
            return path;
        } else {
            return "";
        }
    }

    /**
     * @param newpath sets a new file path for data file backups
     */
    public void setBackupPath(String newpath) {
        path = newpath;
    }

    /**
     * @return whether or not recovery is enabled
     */
    public boolean isRecoveryEnabled() {
        return recovery;
    }

    /**
     * @param pool sets recovery to this setting
     */
    public void setRecoveryEnabled(boolean pool) {
        recovery = pool;
    }

    /**
     * @return gets this profile's hint message, returns an empty string if none
     */
    public String getHint() {
        if (isRecoveryEnabled()) {
            return hint;
        } else {
            return "";
        }
    }

    /**
     * @param hint assigns hint
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public String toString() {
        return name;
    }

}