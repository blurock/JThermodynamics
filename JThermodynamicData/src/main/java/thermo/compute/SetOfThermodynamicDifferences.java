/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compute;

import java.util.Iterator;
import thermo.compare.ThermodynamicDifference;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;

/**
 *
 * @author edwardblurock
 */
public class SetOfThermodynamicDifferences extends SetOfBensonThermodynamicBase {
	private static final long serialVersionUID = 1393621733486845590L;

	public SetOfThermodynamicDifferences(String name) {
    }

    public String stringTableHeader(){
        StringBuilder buf = new StringBuilder();
        buf.append("Molecule");
        buf.append("\t");
        buf.append("Enthalpy");
        buf.append("\t");
        buf.append("Entropy");
        //Iterator<ThermodynamicDifference> iterator = this.iterator();
        //ThermodynamicDifference diff = (ThermodynamicDifference) iterator.next();

        return buf.toString();
    }
    
 public String toStringInTable() {
     StringBuilder buf = new StringBuilder();
     String header = stringTableHeader();
     buf.append(header);
        Iterator<BensonThermodynamicBase> iterator = this.iterator();
        while(iterator.hasNext()) {
        	ThermodynamicDifference diff = (ThermodynamicDifference) iterator.next();
        buf.append(diff.toStringInTable());
        buf.append("\n");
        }
     return buf.toString();
 }
 
}
