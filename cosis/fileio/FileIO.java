/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cosis.fileio;

import cosis.util.Errors;
import cosis.util.Utils;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Kavon
 */
public abstract class FileIO {
    /**
     * Finds valid user data files.
     * @return File[] of .db8 files within the cosis folder
     */
    public static File[] getUserFiles() {
        File[] allFiles = (new File(System.getProperty("user.dir") + "/cosis_data")).listFiles();
        if (allFiles.length == 0)
            return allFiles;

        ArrayList<File> validFiles = new ArrayList<File>();
        //find a valid file
        for (int i = 0; i < allFiles.length; i++) {
            String name = allFiles[i].getName();
            //name must be 5 characters (including extension) or more, a file (as opposed to a directory), and end in .db8
            if (name.length() >= 5 && allFiles[i].isFile() && name.substring(name.length() - 4).equalsIgnoreCase(".db8")) {
                validFiles.add(allFiles[i]);
            }
        }
        if (validFiles.size() == 0) {
            return new File[0];
        } else {
            File[] fileArray = new File[validFiles.size()];
            for (int i = 0; i < validFiles.size(); i++) {
                fileArray[i] = validFiles.get(i);
            }
            return fileArray;
        }
    }

    /**
     * @param name name of profile
     * @return If the desired profile name has a conflict, null will be returned.
     */
    public static String getFileNameForName(String name) {
        File[] files = FileIO.getUserFiles();
            Profile[] profiles = new Profile[files.length];
            for (int i = 0; i < files.length; i++) {
                profiles[i] = new Profile(files[i]);
            }
            String filename = Utils.removeSpaces(name);
            int count = 0;
            boolean keepgoing = true;

            if (profiles.length > 0) {
                //check for duplicate names
                for (int i = 0; i < profiles.length; i++) {
                    if (profiles[i].getName().equalsIgnoreCase(name)) {
                        //stop making the profile, another profile with the same name exists
                        Errors.displayInformation("A profile with that name already exists!");
                        return null;
                    }
                }

                //check for duplicate filenames
                do {
                    String profileName = profiles[count].getFile().getName();
                    if (profileName.substring(0, profileName.length() - 4).equalsIgnoreCase(filename)) {
                        filename += "_";
                        count = 0;
                    } else {
                        count++;
                    }
                    if (count >= profiles.length) {
                        filename += ".db8";
                        keepgoing = false;
                    }
                } while (keepgoing);
            } else {
                filename += ".db8";
            }
        return filename;
    }
    
}
