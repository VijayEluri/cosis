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
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * A profile containing things such as the name etc and the accounts.
 * @author Kavon Farvardin
 */
public class Profile {

    private File file;
    
    private Secure auth, newAuth = null;

    private String name, salt, verify, hint, backupPath, encryptedVerification;

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
    	ObjectInputStream in = null;
        try {
            file = data;
            in = new ObjectInputStream(new FileInputStream(file));
            
            name = in.readUTF();
            salt = in.readUTF();
            verify = in.readUTF();
            encryptedVerification = in.readUTF();

            recovery = in.readBoolean();
            if (recovery)
                hint = in.readUTF();

            backup = in.readBoolean();
            if (backup)
                backupPath = in.readUTF();

            timeout = in.readInt();
            
            accounts = new ArrayList<Account>();
            
            int numAccounts = in.readInt();            
            for(int i = 0; i < numAccounts; i++) {
            	accounts.add((Account)in.readObject());
            }
            
        } catch(EOFException ex) {
            int answer = JOptionPane.showConfirmDialog(null,
                    "A file with the .cosis extension: " + file.getName()
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
     * @return true if the provided Secure object was used to decrypt the acconuts.
     */
    public boolean authenticate(Secure authKey) {
        try {
            String testVerify = authKey.decrypt(encryptedVerification);
            if (testVerify.equals(verify)) {
                auth = authKey;
            } else {
                throw new SecurityException("Decryption resulted in invalid verification.");
            }

            int numAccounts = accounts.size();
            for(int i = 0; i < numAccounts; i++) {
            	auth.decrypt(accounts.get(i));
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

            String encryptedVerif = new Secure(password, salt).encrypt(verification);


            //now let's start writing to the user's file!

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userFile));
            out.writeUTF(name);
            out.writeUTF(salt);
            out.writeUTF(verification);
            out.writeUTF(encryptedVerif);

            //here we begin with preferences, these are default values
            out.writeBoolean(false);    //recovery
            out.writeBoolean(false);    //backup
            out.writeInt(15);           //timeout length in minutes
            out.writeInt(0);			//number of accounts

            out.flush();
            out.close();

            //if we've made it here, we're successful
            return true;
        } catch (Exception ex) {
            Errors.log(ex);
            Errors.displayWarning("Problem creating profile!");
            return false;
        }
    }

    public void save(boolean alsoBackup) {
    	save(file);
    	if(alsoBackup && backup)
    		save(new File(backupPath));
    }
    
    private void save(File location) {
    	try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(location));

            out.writeUTF(name);
            out.writeUTF(salt);
            out.writeUTF(verify);

            if(newAuth != null)
                encryptedVerification = newAuth.encrypt(verify);

            out.writeUTF(encryptedVerification);

            out.writeBoolean(recovery);
            if(recovery)
                out.writeUTF(hint);

            out.writeBoolean(backup);
            if(backup)
                out.writeUTF(backupPath);

            out.writeInt(timeout);
            
            int numAccounts = accounts.size();
            
            out.writeInt(numAccounts);

            for(int i = 0; i < numAccounts; i++) {
            	out.writeObject(accounts.get(i));
            }

            out.flush();
            out.close();



        } catch (Exception ex) {
            Errors.log(ex);
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
        save(true);
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
            return backupPath;
        } else {
            return "";
        }
    }

    /**
     * @param newpath sets a new file path for data file backups
     */
    public void setBackupPath(String newpath) {
        backupPath = newpath;
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
    
    public ArrayList<Account> getAccounts() {
    	return accounts;
    }

    @Override
    public String toString() {
        return name;
    }

}