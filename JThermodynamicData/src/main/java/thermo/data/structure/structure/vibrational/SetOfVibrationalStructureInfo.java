/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/** the set of vibrational structures and information
 *
 * @author blurock
 */
public class SetOfVibrationalStructureInfo extends ArrayList<VibrationalStructureInfo> {
    
    /** The empty constructor
     * 
     */
    public SetOfVibrationalStructureInfo() {
    }
    
    /** The constructor from a vector of vibrational set.
     * 
     * @param set
     */
    public SetOfVibrationalStructureInfo(HashSet<VibrationalStructureInfo> set) {
        Iterator<VibrationalStructureInfo> obj = set.iterator();
        while(obj.hasNext()) {
            VibrationalStructureInfo info = (VibrationalStructureInfo) obj.next();
            this.add(info);
        }
    }
    
    @Override
   public String toString() {
       StringBuffer buf = new StringBuffer();
       buf.append("Set of Vibrational Structure Info\n");
       Iterator<VibrationalStructureInfo> obj = this.iterator();
       while(obj.hasNext()) {
            VibrationalStructureInfo info = obj.next();
           buf.append(info.toString());
           buf.append("\n");
       }
       
       
       return buf.toString();
   } 

}
