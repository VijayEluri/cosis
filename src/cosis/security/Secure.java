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

package cosis.security;

import cosis.gui.Account;
import cosis.util.Errors;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encrypter/Decrypter using AES.
 *
 * I spent a lot of time on this and I'm confident in its security.
 *
 * @author Kavon Farvardin
 */
public class Secure {

    private Cipher encryptCipher,  decryptCipher;

    /**
     * Constructs a new Secure object, which can decrypt a user's profile.
     * @param password Password user enters to log-in
     * @param salt Random salt associated with the profile.
     */
    public Secure(String password, String salt) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(makeAESKey(password, salt), "AES");

            encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);

            decryptCipher = Cipher.getInstance("AES");
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (Exception ex) {
            Errors.log(ex);
            Errors.displayWarning("Problem with cipher creation!");
            System.exit(1);
        }
    }
    
    /**
     * Calls doFinal from the given cipher on the given String and returns a String
     * of that result. All versions between bytes and characters use the UTF-8 character set
     */
    private String applyCiphertoString(String s, Cipher c) throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
    	return new String(c.doFinal(s.getBytes("UTF-8")), "UTF-8");
    }

    /**
     * Encrypts an Account, it modifies the parameter.
     * 
     * Only the name, userid, password, notes, and dateModified fields are
     * acted upon.
     * 
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public void encrypt(Account a) throws IllegalBlockSizeException, BadPaddingException {
        try {            
            a.setName(applyCiphertoString(a.getName(), encryptCipher));
            a.setUserID(applyCiphertoString(a.getUserID(), encryptCipher));
            a.setPassword(applyCiphertoString(a.getPassword(), encryptCipher));
            a.setNotes(applyCiphertoString(a.getNotes(), encryptCipher));
            a.setLastEditDate(applyCiphertoString(a.getLastEditDate(), encryptCipher));
        } catch (UnsupportedEncodingException ex) {
            Errors.log(ex);
            System.exit(1);
        }        
    }    

    /**
     * Decrypts an Account, it modifies the parameter.
     * 
     * Only the name, userid, password, notes, and dateModified fields are
     * acted upon.
     * 
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public void decrypt(Account a) throws IllegalBlockSizeException, BadPaddingException {
    	try {            
            a.setName(applyCiphertoString(a.getName(), decryptCipher));
            a.setUserID(applyCiphertoString(a.getUserID(), decryptCipher));
            a.setPassword(applyCiphertoString(a.getPassword(), decryptCipher));
            a.setNotes(applyCiphertoString(a.getNotes(), decryptCipher));
            a.setLastEditDate(applyCiphertoString(a.getLastEditDate(), decryptCipher));
        } catch (UnsupportedEncodingException ex) {
            Errors.log(ex);
            System.exit(1);
        } 
    }

    /**
     * Evaporates sea water and refines the resulting NaCl compound for password hashing purposes.
     * @return A sodium chloride atom.
     */
    public static String createSalt() {
        return Crypt.generateSalt();
    }

    /**
     * Creates a 16 byte key for AES-128 using 32 bytes of an OpenBSD-style Blowfish password hash.
     */
    private byte[] makeAESKey(String password, String salt) {
        try {
            byte[] hash = Crypt.hashPassword(password, salt).getBytes("UTF-8");
            byte[] key = new byte[16];

            if(hash.length < 32) {
                Errors.displayWarning("Password hash length too small!");
                Errors.log(new GeneralSecurityException("Password hash length too small!"));
                System.exit(1);
            }

            int leftPos = 0, rightPos = 31;
            while(leftPos < 16) {
                key[leftPos] = (byte) (hash[leftPos] ^ hash[rightPos]);
                leftPos++;
                rightPos--;
            }

            return key;
        } catch (UnsupportedEncodingException ex) {
            Errors.log(ex);
            System.exit(1);
            return null;
        }
    }
}
