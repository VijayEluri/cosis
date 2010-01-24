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

package cosis.util;

import cosis.fileio.Account;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import javax.swing.JLabel;

/**
 * Utility methods, pretty much anything which does something general and
 * other classes may want to use.
 * @author Kavon Farvardin
 */
public abstract class Utils {

    /**
     * @param orig String to have spaces removed
     * @return Returns a String with no spaces, does not modify origional
     */
    public static String removeSpaces(String orig) {
        String modify = orig;
        modify.trim();
        modify = orig.replaceAll(" ", "_");
        return modify;
    }

    /**
     * Produces a timestamp, mostly used for logging.
     * @return the current time and date
     */
    public static String getTimestamp() {
        Calendar now = new GregorianCalendar();
        now.setTime(new Date());
        return (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DAY_OF_MONTH) + "/" +
                now.get(Calendar.YEAR) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" +
                now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);
    }

    /**
     * Shows a hard-coded error message with the given JLabel
     * Sets all JLabels to visible
     * @param i Error number
     * @param label JLabel to show the user
     * @note I really hate this legacy code, I don't know what I was smoking when I thought of this.
     */
    public static void showJLabelError(int i, JLabel label) {
        switch (i) {
            case 0: label.setText("Error: Unexpected Length"); break;
            case 1: label.setText("All fields must be complete"); break;
            case 2: label.setText("Name too short"); break;
            case 3: label.setText("Name too long"); break;
            case 4: label.setText("Name must begin with letter"); break;
            case 5: label.setText("Password too short."); break;
            case 6: label.setText("Password excessively long"); break;
            case 7: label.setText("This error doesn't exist"); break;
            case 8: label.setText("Passwords do not match"); break;
            case 9: label.setText("Profile name can't be password"); break;
            case 10: label.setText("Name has illegal characters"); break;
            case 11: label.setText("This profile already exists!"); break;
            default:
                label.setText("Error message doesn't exist!");
                Errors.log(new Exception("JLabel Error case #" + i + " doesn't exist!")); break;
        }
        label.setVisible(true);
    }
    /**
     * Checks for an illegal character in a String
     * @param s String to check
     * @return Returns true if the String contains an illegal character, false otherwise
     */
    public static boolean hasIllegalCharacters(String s) {
        if(s.contains("/") || s.contains("\\") || s.contains(":") || s.contains(";")
                || s.contains("*") || s.contains("?") || s.contains("\'")
                || s.contains("\"") || s.contains("<") || s.contains(">")
                || s.contains(".") || s.contains("|") || s.contains("#")
                || s.contains("(") || s.contains(")")) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Checks the first letter of a String for an upper or lowercase letter
     * @param s String to check
     * @return Returns true if the String begins with an upper or lowercase letter
     */
    public static boolean hasFirstLetter(String s) {
        return Pattern.compile("^[A-Za-z]").matcher(s).find();
    }

    /**
     * Alphabetizes a String[], ignoring case.
     * Uses a Merge Sort algorithm
     * @param array 
     * @return String[] in lexiographical order
     */
    public static String[] mergeSort(String[] array) {
        ArrayList<String> list = new ArrayList<String>();

        for(String item : array) list.add(item);

        list = mergeSortString(list);

        String[] result = new String[list.size()];
        for(int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }

        return result;
    }
    /**
     * Alphabetizes the Account[] by name, ignoring case.
     * Uses a Merge Sort algorithm.
     * @param array 
     * @return Account[] in lexiographical order according to its Name
     */
    public static Account[] mergeSort(Account[] array) {
        ArrayList<Account> list = new ArrayList<Account>();

        for(Account item : array) list.add(item);

        list = mergeSortAccount(list);

        Account[] result = new Account[list.size()];
        for(int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }

        return result;
    }

    /**
     * Alphabetizes an ArrayList of Strings recursivley with
     * a Merge Sort style algorithm, which should have n*log(n)
     * complexity.
     *
     * @param list list to sort
     * @return a sorted ArrayList of Strings
     */
    public static ArrayList<String> mergeSortString(ArrayList<String> list) {
        ArrayList<String> left = new ArrayList<String>();
        ArrayList<String> right = new ArrayList<String>();


        if(list.size() <= 1) return list; //end recursion

        //split the list in half
        for(int i = 0; i < (list.size() / 2); i++) {
            left.add(list.get(i));
        }
        for(int i = (list.size() / 2); i < list.size(); i++) {
            right.add(list.get(i));
        }

        left = mergeSortString(left);
        right = mergeSortString(right);

        return mergeString(left, right);
    }
    /**
     * Does the actual merging for mergeSortString(ArrayList<String>)
     */
    private static ArrayList<String> mergeString(ArrayList<String> left, ArrayList<String> right) {
        ArrayList<String> result = new ArrayList<String>();
        int leftIndex = 0;
        int rightIndex = 0;

        //go through both sides and merge them in order like putting together a deck of cards
        while(leftIndex < left.size() && rightIndex < right.size()) {
            if((left.get(leftIndex).compareToIgnoreCase(right.get(rightIndex))) > 0) {
                result.add(right.get(rightIndex++));
            } else {
                result.add(left.get(leftIndex++));
            }
        }

        //pick up the left-over values on whichever side which are already sorted
        while(leftIndex < left.size()) {
            result.add(left.get(leftIndex++));
        }
        while(rightIndex < right.size()) {
            result.add(right.get(rightIndex++));
        }
        return result;
    }

    /**
     * Alphabetizes an ArrayList of Accounts recursivley with
     * a Merge Sort style algorithm, which should have n*log(n)
     * complexity.
     *
     * @param list to be sorted
     * @return a sorted ArrayList of Account
     */
    public static ArrayList<Account> mergeSortAccount(ArrayList<Account> list) {
        ArrayList<Account> left = new ArrayList<Account>();
        ArrayList<Account> right = new ArrayList<Account>();


        if(list.size() <= 1) return list; //end recursion

        //split the list in half
        for(int i = 0; i < (list.size() / 2); i++) {
            left.add(list.get(i));
        }
        for(int i = (list.size() / 2); i < list.size(); i++) {
            right.add(list.get(i));
        }

        left = mergeSortAccount(left);
        right = mergeSortAccount(right);

        return mergeAccount(left, right);
    }
    /**
     * Does the actual merging for mergeSortAccount()
     */
    private static ArrayList<Account> mergeAccount(ArrayList<Account> left, ArrayList<Account> right) {
        ArrayList<Account> result = new ArrayList<Account>();
        int leftIndex = 0;
        int rightIndex = 0;

        //go through both sides and merge them in order like putting together a deck of cards
        while(leftIndex < left.size() && rightIndex < right.size()) {
            if((left.get(leftIndex).getName().compareToIgnoreCase(right.get(rightIndex).getName())) > 0) {
                result.add(right.get(rightIndex++));
            } else {
                result.add(left.get(leftIndex++));
            }
        }

        //pick up the left-over values on whichever side which are already sorted
        while(leftIndex < left.size()) {
            result.add(left.get(leftIndex++));
        }
        while(rightIndex < right.size()) {
            result.add(right.get(rightIndex++));
        }
        return result;
    }

}
