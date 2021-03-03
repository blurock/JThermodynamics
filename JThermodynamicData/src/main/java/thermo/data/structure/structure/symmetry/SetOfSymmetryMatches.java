/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author blurock
 */
public class SetOfSymmetryMatches extends ArrayList<SymmetryMatch>  {
	private static final long serialVersionUID = 1L;
	boolean debug = false;
	
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator<SymmetryMatch> i = this.iterator();
        while(i.hasNext()) {
            SymmetryMatch match = i.next();
            buf.append(match.toString());
        }
        return buf.toString();
    }
    
    /* eliminateMatchingSymmetries
     * This eleminates from the ArrayList<SymmetryMatch>
     * 
     * The prerequisite is that the reference molecules are the same.
     * This was insured by the initial calls of internal and external symmetries
     */
    public void eliminateMatchingSymmetries(SymmetryMatch matchToDelete) {
    	if(debug) {
    	   System.out.println("eliminateMatchingSymmetries: to delete " 
    			   	+ matchToDelete.toString());
    	}
    		Iterator<SymmetryMatch> iter = this.iterator();
    		boolean notdone = iter.hasNext();
    		while(notdone) {
    			SymmetryMatch match = iter.next();
    			if(debug) {
    				System.out.println("Should be deleted?: " + match.toString());
    			}
    			if(match.sameAtoms(matchToDelete)) {
    				if(debug) {
    					System.out.println("Should be deleted");
    				}
    				this.remove(match);
    				// Only one is deleted... if more is possible, then uncomment this statement
    				//eliminateMatchingSymmetries(matchToDelete);
    				notdone = false;
    			} else {
    				notdone = iter.hasNext();
    			}
    		}
    }
}
