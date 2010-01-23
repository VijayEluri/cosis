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

import cosis.util.Errors;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
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
     * Encrypts a String[]
     * @param input 
     * @return an encrypted byte[].
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] encrypt(String[] input) throws IllegalBlockSizeException, BadPaddingException {
        String[] dataString = input;
        ArrayList<Byte> listByte = new ArrayList<Byte>();

        for (int i = 0; i < dataString.length; i++) {

            byte[] converted = dataString[i].getBytes(); //FIXME: Incorrectly converts special chars to bytes.

            for (int k = 0; k < converted.length; k++) {
                //fill listByte
                listByte.add(converted[k]);
            }
            //Add a endl to seperate strings
            listByte.add((byte) '\n');
        }
        //convert the list to an array
        byte[] arrayByte = new byte[listByte.size()];
        for (int i = 0; i < listByte.size(); i++) {
            arrayByte[i] = listByte.get(i);
        }
        return encryptCipher.doFinal(arrayByte);
    }

    /**
     * Encrypts a single String
     * @param input
     * @return an encrypted byte[]
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] encrypt(String input) throws IllegalBlockSizeException, BadPaddingException {
        return encrypt(new String[]{input});
    }

    /**
     * Decrypts a byte[]
     * @param input
     * @return a decrypted String[].
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String[] decrypt(byte[] input) throws IllegalBlockSizeException, BadPaddingException {
        byte[] dataByte = input;
        ArrayList<String> listString = new ArrayList<String>();
        String chunk = "";
        //decrypt the big dataByte
        dataByte = decryptCipher.doFinal(dataByte);

        //reformulate the strings
        for (int i = 0; i < dataByte.length; i++) {
            if (dataByte[i] == (byte) '\n') {
                listString.add(chunk);
                chunk = "";
            } else {
                chunk += (char) dataByte[i];
            }
        }
        //convert the list to an array, I like it this way. toArray() gives me problems
        String[] arrayString = new String[listString.size()];
        for (int i = 0; i < listString.size(); i++) {
            arrayString[i] = listString.get(i);
        }
        return arrayString;
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
            byte[] hash = Crypt.hashPassword(password, salt).getBytes("ISO-8859-1");
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
