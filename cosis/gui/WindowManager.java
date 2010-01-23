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
 * @since
 */
public class WindowManager {

    private ManagedWindow mainWindow;
    private ArrayList<ManagedWindow> minors = new ArrayList<ManagedWindow>();
    private Tray tray;

    WindowManager(ManagedWindow mjw) {
        setMajorWindow(mjw);
    }
    
    public void setMajorWindow(ManagedWindow mjw) {
        mainWindow = mjw;
    }

    /**
     * Adds the given ManagedWindow to the manager and calls
     * its display method.
     * @param mnw
     */
    public void addMinor(ManagedWindow mnw) {
        minors.add(mnw);
        mnw.display();
    }    
    /**
     * Finds the given ManagedWindow and calls destroy, then removes
     * it from the manager.
     */
    public void removeMinor(ManagedWindow mnw) {
        for(int i = 0; i < minors.size(); i++) {
            if(minors.get(i) == mnw) {
                mnw.destroy();
                minors.remove(i);
            }
        }
    }

    /**
     * Calls minimize() on all ManagedWindows
     */
    public void minimizeAll() {
        for(ManagedWindow item : minors)
            item.minimize();
        mainWindow.minimize();
    }

    /**
     * Calls maximise on all ManagedWindows
     */
    public void maximizeAll() {
        mainWindow.maximize();
        for(ManagedWindow item : minors)
            item.maximize();
    }

    public int getNumberOfMinors() {
        return minors.size();
    }
    public void destroyMinors() {

    }
    public void destroyMajor() {
        
    }
    //public void prioritize(ManagedWindow aMinor) ????
}
