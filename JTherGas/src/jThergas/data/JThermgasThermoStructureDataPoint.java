
package jThergas.data;

import jThergas.data.read.JThergasTokenizer;
import jThergas.data.structure.JThergasStructureData;
import jThergas.data.structure.JThergasAtomicStructure;
import jThergas.data.thermo.JThergasThermoData;
import jThergas.exceptions.JThergasReadException;

/** The information in a single Thergas block of thermo information
 *
 * The information of each line is parsed using three data structures:
 * <ul>
 * <li> {@link  jthergas.data.structure.JThergasStructureData JThergasStructureData}
 * <li> {@link JThergasAtomicStructure}
 * <li> {@link JThergasThermoData}
 * </ul>
 * All the information of the block is held in these three classes.
 * 
 * The parse procedure parses
 * @version 2008.10
 * @author blurock
 */
public class JThermgasThermoStructureDataPoint {
	boolean debug = false;
    /**
     * This gives the number of atoms
     */
    private JThergasThermoData thermodynamics;
    protected JThergasStructureData structure;
    private JThergasAtomicStructure atomicStructure;
    
    
    /** Parse through a single block of Thermodynamic information
     * {@link JThergasTokenizer} is used to retrieve the 3 or 4 lines of the block
     * {@link jthergas.data.thermo.JThergasThermoData},
     * {@link jthergas.data.structure.JThergasStructureData} and
     * {@link jthergas.data.structure.JThergasAtomicStructure}
     * are used to parse the information out of the lines
     * 
     * @param fileTokenized The line information
     * @throws jthergas.exceptions.JThergasReadException
     */
    public  void parse(JThergasTokenizer fileTokenized)  throws JThergasReadException {
    	String line1 = fileTokenized.line1;
    	String line1a = fileTokenized.line1a;
    	String line2 = fileTokenized.line2;
    	String line3 = fileTokenized.line3;
    	boolean group = fileTokenized.group;
    	int linenumber = fileTokenized.getLineNumber();
    	int groupnumber = fileTokenized.getLineNumber();
    	parse(line1,line1a,line2,line3,group,linenumber,groupnumber);
    }
    public  void parse(String line1, String line1a, String line2, String line3, 
    		boolean group, int linenumber, int groupnumber)  throws JThergasReadException {
        thermodynamics = new JThergasThermoData();
        structure = new JThergasStructureData();
        atomicStructure = new JThergasAtomicStructure();
        
        try {
        	if(debug) {
        		System.out.println("Get Structure        : " + line1);
        		System.out.println("Get Structure (extra): " + line1a);
        	}
            getStructure().parse(line1, line1a,group);
            if(debug) {
            	System.out.println("Structure: " + getStructure().writeToString());
            }
            if(debug) {
        		System.out.println("Thermodynamics        : " + line2);
        	}
            getThermodynamics().parse(line2);
            if(debug) {
            	System.out.println("Thermodynamics: " + getThermodynamics().writeToString());
            }
        	if(debug) {
        		System.out.println("AtomicStructure        : " + line3);
        	}
            getAtomicStructure().parse(line3);
            if(debug) {
            	System.out.println("AtomicStructure: " + getAtomicStructure().writeToString());
            }
        } catch(JThergasReadException ex) {
            throw new JThergasReadException("ERROR at line: " + linenumber + ", block: " + groupnumber + "\n" + ex.writeToString());
        }
    }
    /** This writes the data in (close to) the standard form of Thergas
     * 
     * @return The multi-lined string with the information.
     */
    public String writeToString() {
        StringBuffer buf = new StringBuffer();
        String structureS = getStructure().writeToString();
        buf.append(structureS + "\n");
        String thermoS = getThermodynamics().writeToString();
        buf.append(thermoS + "\n");
        String atomicS = getAtomicStructure().writeToString();
        buf.append(atomicS + "\n");
        return buf.toString();
    }

    /** Get thermodynamic information
     *
     * @return thermodynamic information
     */
    public JThergasThermoData getThermodynamics() {
        return thermodynamics;
    }

    /** Get the structural information
     *
     * @return the structural information
     */
    public JThergasStructureData getStructure() {
        return structure;
    }

    /** Get the atomic structure information
     *
     * @return the atomic structure information
     */
    public JThergasAtomicStructure getAtomicStructure() {
        return atomicStructure;
    }

    public void energyConversion(double conversion) {
		JThergasThermoData thermo = getThermodynamics();
		double entropy    = thermo.getStandardEntropy();
		double enthalpy   = thermo.getStandardEnthalpy();
		double[] cpvalues = thermo.getCpValues();
		
		thermo.setStandardEntropy(entropy*conversion);
		thermo.setStandardEnthalpy(enthalpy*conversion);
		for(int i=0;i<cpvalues.length;i++) {
			cpvalues[i] = cpvalues[i]*conversion;
		}
  	
    }
}
