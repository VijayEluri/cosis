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
import cosis.util.Base64;
import cosis.util.Errors;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Used to encrypt and decrypt profile data.
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
    public Secure(char[] password, byte[] salt, byte[] iv) {
    	try {
    		Security.addProvider(new BouncyCastleProvider());
               	
        	SecretKey secretKey = SecretKeyFactory.getInstance("PBKDF2", "BC").generateSecret(new PBEKeySpec(password, salt, 12354, 32));
            IvParameterSpec initVector = new IvParameterSpec(iv);
            
            encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, initVector);

            decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, initVector);
        } catch (Exception ex) {
            Errors.log(ex);
            Errors.displayWarning("Problem with cipher creation!");
            System.exit(1);
        }
    }
    
    /**
     * Encrypts a String. Added this for the verification string. Uses UTF-8 and base64 encoding
     * @param s an encrypted string
     * @return a new string which has been encrypted with this Secure's cipher
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public String encrypt(String s) throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException  {
    	return Base64.encodeToString(encryptCipher.doFinal(s.getBytes("UTF-8")), false);
    }
    
    /**
     * Decrypts a String. Added this for the verification string. Uses UTF-8 and base64 encoding
     * @param s an decrypted string
     * @return a new string which has been decrypted with this Secure's cipher
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public String decrypt(String s) throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
    	return new String(decryptCipher.doFinal(Base64.decodeFast(s)), "UTF-8");
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
            a.setName(encrypt(a.getName()));
            a.setUserID(encrypt(a.getUserID()));
            a.setPassword(encrypt(a.getPassword()));
            a.setNotes(encrypt(a.getNotes()));
            a.setLastEditDate(encrypt(a.getLastEditDate()));
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
    		a.setName(decrypt(a.getName()));
            a.setUserID(decrypt(a.getUserID()));
            a.setPassword(decrypt(a.getPassword()));
            a.setNotes(decrypt(a.getNotes()));
            a.setLastEditDate(decrypt(a.getLastEditDate()));
        } catch (UnsupportedEncodingException ex) {
            Errors.log(ex);
            System.exit(1);
        } 
    }

    /**
     * Creates a specified number of random bytes for salts or IVs.
     */
    public static byte[] createRandomBytes(int numBytes) {
    	SecureRandom rand = new SecureRandom();
    	byte[] nacl = new byte[numBytes];
    	rand.nextBytes(nacl);
        return nacl;
    }

//    /**
//     * Creates a 16 byte key for AES using jBCrypt, a Java implementation of OpenBSD's
//     * Blowfish password hashing code, as described in "A Future-Adaptable Password Scheme"
//     * by Niels Provos and David Mazières.
//     */
//    private byte[] makeAESKey(String password, String salt) {          
//        byte[] hash = Crypt.hashPassword(password, salt);
//        
//        System.out.print("Length: " + hash.length + "\t");
//        for(byte b : hash)
//        	System.out.print(b + "\t");
//        System.out.println();
//        
//        byte[] key = new byte[16];
//
//        if(hash.length < 24) {
//            Errors.displayWarning("Password hash length too small!");
//            Errors.log(new GeneralSecurityException("Password hash length too small!"));
//            System.exit(1);
//        }
//
//        int leftPos = 0, rightPos = 23;
//        while(rightPos > 15) {
//            key[leftPos] = (byte) (hash[leftPos] ^ hash[rightPos]);
//            leftPos++;
//            rightPos--;
//        }
//
//        return key;
//    }
}
