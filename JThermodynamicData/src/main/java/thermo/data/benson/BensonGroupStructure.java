/*
 */

package thermo.data.benson;

import java.util.Iterator;
import java.util.Vector;

/**The structural information associated with a Benson rule:
 * <ul>
 * <li> The name of the structure rule (usually as it appears in the file)
 * <li> The name of the center atom
 * <li> A vector of {@link BensonConnectAtomStructure} structures
 * </ul>
 *
 * @author blurock
 */
public class BensonGroupStructure implements Comparable<BensonGroupStructure> {
    private String structureName;
    private String centerAtomS;
    private Vector<BensonConnectAtomStructure> bondedAtoms = null;
    
    /** empty constructor
     * initializes the vector of {@link BensonConnectAtomStructure} structures
     */
    public BensonGroupStructure() {
        structureName = "";
        centerAtomS = "";
        bondedAtoms = new Vector<BensonConnectAtomStructure>();
        
    }
    /** Constructor with name of group
     * initializes the vector of {@link BensonConnectAtomStructure} structures
     * @param name Name of the structure
     */
    public BensonGroupStructure(String name) {
        structureName = name;
        centerAtomS = "";
        bondedAtoms = new Vector<BensonConnectAtomStructure>();
    }

    /** Add a connected structure
     *
     * Add a {@link BensonConnectAtomStructure} structure to the list of
     * connections to the center atom
     *
     * @param grpmult The connecting structure
     */
    public void addBondedAtom(BensonConnectAtomStructure grpmult) {
        getBondedAtoms().add(grpmult);
    }
    /** Get the center atom name
     *
     * @return center atom name
     */
    public String getCenterAtomS() {
        return centerAtomS.toLowerCase();
    }

    /** Set the center atom name
     *
     * @param centerAtomS center atom name
     */
    public void setCenterAtomS(String centerAtomS) {
        this.centerAtomS = centerAtomS;
    }

    /** Get the structure name (name of the group)
     *
     * @return structure name
     */
    public String getStructureName() {
        return structureName;
    }

    /** Set the structure name (name of the group)
     *
     * @param structureName the structure name (name of the group)
     */
    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    /** Get the vector of {@link BensonConnectAtomStructure} structure representing the bonded atoms
     *
     * @return The vector of bonded atoms
     */
    public Vector<BensonConnectAtomStructure> getBondedAtoms() {
        return bondedAtoms;
    }
    /** Set the names to null and 
     * initialize the vector of {@link BensonConnectAtomStructure} structures
     * 
     */
    public void reset() {
       bondedAtoms = new Vector<BensonConnectAtomStructure>();
       structureName = "";
       centerAtomS = "";
    }
    
    public boolean hasSameCenterAtom(String centerAtom) {
    	return centerAtom.equalsIgnoreCase(centerAtomS);
    }
    
    

    /** Write on one line the group information
     *
     * @return The string representation of the group information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        buf.append("Benson Group: ");
        buf.append(structureName);
        buf.append("\t Center: ");
        buf.append(centerAtomS);
        for(int i=0;i<bondedAtoms.size();i++) {
            buf.append(bondedAtoms.elementAt(i).toString());
        }
        return buf.toString();
    }
    @Override
    public boolean equals(Object o) {
    	BensonGroupStructure obj = (BensonGroupStructure) o;
    	return this.compareTo(obj) == 0;
    }
	@Override
	public int compareTo(BensonGroupStructure o) {
		int ans = 0;
		if(this.centerAtomS.equalsIgnoreCase(o.getCenterAtomS())) {
			if(this.getBondedAtoms().size() == o.getBondedAtoms().size()) {
				Iterator<BensonConnectAtomStructure> iter = this.getBondedAtoms().iterator();
				boolean matched = true;
				while(iter.hasNext() && matched) {
					BensonConnectAtomStructure n = iter.next();
					matched = o.getBondedAtoms().contains(iter.next());
				}
				if(!matched) {
					ans= -1;
				}
			} else {
				ans = this.getBondedAtoms().size() - o.getBondedAtoms().size();
			}
		} else {
			ans = this.centerAtomS.compareTo(o.getCenterAtomS());
		}
		return ans;
	}
}
