/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jThergas.data.group;

import jThergas.data.JThermgasThermoStructureDataPoint;
import jThergas.data.read.JThergasTokenizer;
import jThergas.data.structure.JThergasStructureData;
import jThergas.exceptions.JThergasNotAGroupElement;
import jThergas.exceptions.JThergasReadException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 *
 * @author blurock
 */
public class JThergasThermoStructureGroupPoint extends JThermgasThermoStructureDataPoint {
    private String centerAtomTypeS;
    private JThergasCenterAtom centerAtomType;
    private HashSet<JThergasGroupElement> groupElements;

    /** The empty constructor
     *
     */
    public JThergasThermoStructureGroupPoint() {
    }
/** The top level routine to parse the group name within the block
 *
 * The {@link jThergas.data.JThermgasThermoStructureDataPoint#parse(jThergas.data.read.JThergasTokenizer) parse }
 * procedure is called from {@link jThergas.data.JThermgasThermoStructureDataPoint  JThermgasThermoStructureDataPoint}
 * to read in the block data. By calling {@link JThergasThermoStructureGroupPoint#parseGroupDescription() parseGroupDescription()}
 * the group in (from {@link JThergasStructureData#getNancyLinearForm()  Nancy Linear form} is parsed.
 *
 * @param fileTokenized
 * @throws jthergas.exceptions.JThergasReadException
 */
    public void parse(JThergasTokenizer fileTokenized) throws JThergasReadException, NumberFormatException {
        super.parse(fileTokenized);

        parseGroupDescription();
    }
    
    public  void parse(String line1, String line1a, String line2, String line3, 
    		boolean group, int linenumber, int groupnumber)  throws JThergasReadException {
    	super.parse(line1, line1a, line2, line3, group, linenumber, groupnumber);
    	parseGroupDescription();
    }
/** The group description in
 *  (from {@link JThergasStructureData#getNancyLinearForm()  Nancy Linear form} is parsed
 *  using the {@link JThergasThermoStructureGroupPoint#separateOutGroupElements() separateOutGroupElements()} method.
 *  the {@link JThergasThermoStructureGroupPoint#groupElements groupElements} and
 * {@link JThergasThermoStructureGroupPoint#centerAtomType centerAtomType} are
 * filled.
 *
 * @throws jthergas.exceptions.JThergasReadException
 */
    private void parseGroupDescription() throws JThergasReadException, NumberFormatException {
        if (this.getStructure().getNancyLinearForm().length() > 0) {
        	parseGroupDescription(this.getStructure().getNancyLinearForm());
        	/*
            String[] groupElementsS = separateOutGroupElements();
            groupElements = new HashSet<JThergasGroupElement>();
            centerAtomTypeS = groupElementsS[0];
            centerAtomType = new JThergasCenterAtom(centerAtomTypeS);
            for (int i = 1; i < groupElementsS.length; i++) {
                JThergasGroupElement element = new JThergasGroupElement();
                element.parse(groupElementsS[i]);
                groupElements.add(element);
            }
            */
        } else {
            centerAtomType = null;
            groupElements = null;
        }
    }
    public void parseGroupDescription(String benson) throws JThergasNotAGroupElement, NumberFormatException {
    	String[] groupElementsS = separateOutGroupElements(benson);
        
        groupElements = new HashSet<JThergasGroupElement>();
        centerAtomTypeS = groupElementsS[0];
        centerAtomType = new JThergasCenterAtom(centerAtomTypeS);
        for (int i = 1; i < groupElementsS.length; i++) {
            JThergasGroupElement element = new JThergasGroupElement();
            element.parse(groupElementsS[i]);
            groupElements.add(element);
        }
    	
    }
/** This parses the group data into a String array.
 *
 * The zeroth element is the center atom string
 * The rest are the group elements.
 *
 * The group elements are identified by surrounding parens
 *
 * @return the array of center atom and group elements
 */
    private String[] separateOutGroupElements(String benson) {
        StringTokenizer tok = new StringTokenizer(benson, "-");

        ArrayList<String> vec = new ArrayList<String>();

        while (tok.hasMoreTokens()) {
            String elementS = tok.nextToken().trim();
            
            int index = elementS.substring(1).indexOf("(");
            while (index > 0) {
                String sub = elementS.substring(0, index + 1);
                vec.add(sub);
                elementS = elementS.substring(index + 1);
                index = elementS.substring(1).indexOf("(");
            }
            vec.add(elementS);
        }
        String[] arr = new String[vec.size()];
        Iterator<String> iter = vec.iterator();
        int j=0;
        while(iter.hasNext()) {
            arr[j++] = iter.next();
        }
       return arr;
    }

    /** Get the center atom type string
     *
     * @return the center atom type string
     */
    public String getCenterAtomTypeS() {
        return centerAtomTypeS;
    }

    /** get the vector of group elements
     *
     * @return the vector of group elements
     */
    public HashSet<JThergasGroupElement> getGroupElements() {
        return groupElements;
    }

    /** The center atom type information
     *
     * @return The center atom type information
     */
    public JThergasCenterAtom getCenterAtomType() {
        return centerAtomType;
    }
    public void setNancyLinearForm(String benson) {
    	structure = new JThergasStructureData();
    	structure.setNancyLinearForm(benson);
    }
}
