/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author blurock
 */
public class SymmetryMatch {
    Hashtable<String,String> matchedSymmetries;
    private SetOfSymmetryAssignments fromMolecule;
    /**
     * 
     * @param symmatch
     * @param frommol 
     */
    public SymmetryMatch(SetOfSymmetryAssignments frommol) {
        fromMolecule = frommol;
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Symmetry Match;\n");
        buf.append(getFromMolecule().toString());
        return buf.toString();
    }

    public SetOfSymmetryAssignments getFromMolecule() {
        return fromMolecule;
    }
	public boolean sameAtoms(SymmetryMatch matchToCompare) {
		SetOfSymmetryAssignments compareFromMolecule = matchToCompare.getFromMolecule();
		boolean matched = true;
		if(fromMolecule.size() == compareFromMolecule.size()) {
			Set<String> keylist = fromMolecule.keySet();
			Iterator<String> iter = keylist.iterator();
			while(iter.hasNext() && matched) {
				SymmetryAssignment match = fromMolecule.get(iter.next());
				//System.out.println("sameAtoms: SymmetryAssignment: " + match.toString());
				//System.out.println("sameAtoms: Set: " + compareFromMolecule.toString());
				matched = atomSetMatch(match,compareFromMolecule);
				//System.out.println("sameAtoms: matched?: " + matched);
			}
			
		} else {
			matched = false;
		}
		return matched;
	}
	
	private boolean atomSetMatch(SymmetryAssignment match, 
			SetOfSymmetryAssignments compareFromMolecule) {
		Set<String> compkeylist = compareFromMolecule.keySet();
		Iterator<String> compiter = compkeylist.iterator();
		boolean notmatched = true;
		while(compiter.hasNext() && notmatched) {
			SymmetryAssignment compmatch = compareFromMolecule.get(compiter.next());
			//System.out.println("atomSetMatch compare: " + match.toString());
			//System.out.println("atomSetMatch compare: " + compmatch.toString());
			notmatched = !match.matches(compmatch);
			//System.out.println("atomSetMatch compare: " + !notmatched);
		}
		return !notmatched;
	}
}
