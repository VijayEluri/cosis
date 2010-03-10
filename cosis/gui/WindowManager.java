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

import java.util.ArrayList;

/**
 * Manages all GUI windows.
 * @author Kavon Farvardin
 */
public class WindowManager {

    private ManagedWindow major = null;
    public ArrayList<ManagedWindow> minors = new ArrayList<ManagedWindow>();

    /**
     * Sets another ManagedWindow as the MajorWindow, destroys all
     * @param mjw
     */
    public void setMajorWindow(ManagedWindow newMajor) {
        if(major != null)
            major.destroy();
        if(minors.size() > 0)
            destroyMinors();
        major = newMajor;
    }

    public ManagedWindow getMajorWindow() {
        return major;
    }

    /**
     * Adds the given ManagedWindow to the manager and calls
     * its display method.
     * @param mnw
     */
    public void addMinor(ManagedWindow minor) {
        minors.add(minor);
        minor.display();
    }    
    /**
     * Finds the given ManagedWindow and calls destroy, then removes
     * it from the manager.
     */
    public void removeMinor(ManagedWindow minor) {
        for(int i = 0; i < minors.size(); i++) {
            if(minors.get(i) == minor) {
                minors.remove(i);
            }
        }
    }

    /**
     * Calls minimize on all ManagedWindows
     */
    public void minimizeAll() {
        for(ManagedWindow item : minors)
            item.minimize();
        major.minimize();
    }

    /**
     * Calls maximize on all ManagedWindows
     */
    public void maximizeAll() {
        major.maximize();
        for(ManagedWindow item : minors)
            item.maximize();
    }

    /**
     * Destroys all minor ManagedWindows
     */
    public void destroyMinors() {
        //All minors should be removing themselves from the list.
        for(int i = 0; i < minors.size();)
            minors.get(i).destroy();
    }

    /**
     * Destroys all windows.
     */
    public void destroyAll() {
        destroyMinors();
        major.destroy();
    }
}
