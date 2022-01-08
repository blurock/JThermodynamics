/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.Cycles;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;

/** Meta atom definition with molecule and connection information
 *
 *
 *
 * @author blurock
 */
public class MetaAtomDefinition extends MetaAtomInfo {
    
    private IAtomContainer molecule;
    String unspecifiedAtomPrefix = "R.";
    String unspecifiedAtom = "R";
    private List<Integer> specifiedAtoms;
    private List<Integer> unspecifiedAtoms;
    private List<IBond> connectingBonds;
    private List<Integer> connectingAtoms;
    
    
    /**
     *
     * @param definition
     */
    public MetaAtomDefinition(MetaAtomDefinition definition) {
    	super(definition);
        molecule = definition.molecule;
        specifiedAtoms = new ArrayList<Integer>(definition.specifiedAtoms);
         unspecifiedAtoms = new ArrayList<Integer>(definition.unspecifiedAtoms);
         connectingBonds = new ArrayList<IBond>(definition.connectingBonds);
         connectingAtoms = new ArrayList<Integer>(definition.connectingAtoms);
    }
    /**
     *
     * @param info
     * @param cmlstruct
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public MetaAtomDefinition(MetaAtomInfo info, StructureAsCML cmlstruct) throws CDKException, ClassNotFoundException, IOException {
        super(info);
        putMolecule(cmlstruct);
    }
    /**
     *
     * @param name
     * @param mol
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public MetaAtomDefinition(String name, IAtomContainer mol) throws CDKException, ClassNotFoundException, IOException {
         setMetaAtomName(name);
         putMolecule(mol);
         
     }
     /**
      *
      * @param name
      * @param cmlstruct
      * @throws org.openscience.cdk.exception.CDKException
      * @throws java.lang.ClassNotFoundException
      * @throws java.io.IOException
      */
     public MetaAtomDefinition(String name, StructureAsCML cmlstruct) throws CDKException, ClassNotFoundException, IOException {
         setMetaAtomName(name);
         putMolecule(cmlstruct);
     }

    private boolean isUnspecifiedAtom(IAtom atom) {
        return atom.getSymbol().equals(unspecifiedAtom) 
                || atom.getSymbol().startsWith(unspecifiedAtomPrefix);
    }

    private void isolateSpecifiedAtoms() {
        setSpecifiedAtoms(new ArrayList<Integer>());
        setUnspecifiedAtoms(new ArrayList<Integer>());
        
        for(int i=0;i<getMolecule().getAtomCount();i++) {
            IAtom atom = getMolecule().getAtom(i);
            if(isUnspecifiedAtom(atom)) {
                getUnspecifiedAtoms().add(Integer.valueOf(i));
            } else {
                getSpecifiedAtoms().add(Integer.valueOf(i));
            }
        }
    }
    private void putMolecule(IAtomContainer mol) throws CDKException, ClassNotFoundException, IOException {
        molecule = mol;
        Cycles.markRingAtomsAndBonds(mol);
         isolateSpecifiedAtoms();
         determineConnectingBondsAndAtoms();
         setElementName(molecule.getID());
    }

    private void putMolecule(StructureAsCML cmlstruct) throws CDKException, ClassNotFoundException, IOException {
             NormalizeMoleculeFromCMLStructure norm = new NormalizeMoleculeFromCMLStructure();
             putMolecule(norm.getNormalizedMolecule(cmlstruct));
    }

    /**
     *
     * @return
     */
    public IAtomContainer getMolecule() {
        return molecule;
    }

    /**
     *
     * @param specifiedAtoms
     */
    public void setSpecifiedAtoms(ArrayList<Integer> specifiedAtoms) {
        this.specifiedAtoms = specifiedAtoms;
    }

    /**
     *
     * @param unspecifiedAtoms
     */
    public void setUnspecifiedAtoms(ArrayList<Integer> unspecifiedAtoms) {
        this.unspecifiedAtoms = unspecifiedAtoms;
    }
    /**
     *
     */
    public void determineConnectingBondsAndAtoms() {
        connectingBonds = new ArrayList<IBond>();
        connectingAtoms = new ArrayList<Integer>();
        for(int i=0;i<molecule.getBondCount();i++) {
            IBond bnd = molecule.getBond(i);
            IAtom atm1 = bnd.getAtom(0);
            IAtom atm2 = bnd.getAtom(1);
            if(isUnspecifiedAtom(atm1) && !isUnspecifiedAtom(atm2)) {
                getConnectingBonds().add(bnd);
                Integer aI = Integer.valueOf(molecule.indexOf(atm2));
                if(!connectingAtoms.contains(aI)) 
                    getConnectingAtoms().add(aI);
            } if(!isUnspecifiedAtom(atm1) && isUnspecifiedAtom(atm2)) {
                getConnectingBonds().add(bnd);
                Integer aI = Integer.valueOf(molecule.indexOf(atm1));
                if(!connectingAtoms.contains(aI)) 
                    getConnectingAtoms().add(aI);
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        buf.append("MetaAtomDefinition: " + getMetaAtomName() + "\n");
        buf.append("MetaAtom Type     : " + getMetaAtomType() + "\n");
        String specS = arrayListAsString("specifiedAtoms",getSpecifiedAtoms());
        String unspecS = arrayListAsString("unspecifiedAtoms",getUnspecifiedAtoms());
        //String bondsS = arrayListAsString("connectingBonds",connectingBonds);
        String atomsS = arrayListAsString("connectingAtoms",getConnectingAtoms());
        buf.append(specS + "\n");
        buf.append(unspecS + "\n");
        //buf.append(bondsS + "\n");
        buf.append(atomsS + "\n");
        StructureAsCML cml;
        try {
            cml = new StructureAsCML(molecule);
            buf.append(cml.getCmlStructureString());
        } catch (CDKException ex) {
            Logger.getLogger(MetaAtomDefinition.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return buf.toString();
    }
    @SuppressWarnings("rawtypes")
	private String arrayListAsString(String name, List lst) {
        StringBuffer buf = new StringBuffer();
        buf.append(name + ":  ");
        Iterator i = lst.iterator();
        while(i.hasNext()) {
            Object iO = i.next();
            buf.append(iO.toString() + "\t");
        }
        
        return buf.toString();
    }

    /**
     *
     * @return
     */
    public List<Integer> getSpecifiedAtoms() {
        return specifiedAtoms;
    }

    /**
     *
     * @return
     */
    public List<Integer> getUnspecifiedAtoms() {
        return unspecifiedAtoms;
    }

    /**
     *
     * @return
     */
    public List<IBond> getConnectingBonds() {
        return connectingBonds;
    }

    /**
     *
     * @return
     */
    public List<Integer> getConnectingAtoms() {
        return connectingAtoms;
    }
    /**
     *
     * @return
     */
    public IAtom createMetaAtom() {
    	MetaAtomInfo atm = new MetaAtomInfo();
    	atm.setElementName(getElementName());
    	atm.setMetaAtomType(getMetaAtomType());
        atm.setSymbol(getMetaAtomName());
        atm.setAtomTypeName(getMetaAtomType());
        atm.setCharge(Double.valueOf(0.0));
        atm.setFormalNeighbourCount(unspecifiedAtoms.size());
        atm.setValency(unspecifiedAtoms.size());
        return atm;
    }
    /**
     *
     * @param atm
     * @return
     */
    public IBond getConnectingBond(IAtom atm) {
        Iterator<IBond> i = connectingBonds.iterator();
        IBond bnd = null;
        boolean notfound = true;
        while(i.hasNext() && notfound) {
            bnd = i.next();
            if(bnd.contains(atm)) {
                notfound = false;
            }
        }
        return bnd;
    }
    /**
     *
     * @param atm
     * @return
     */
    public IAtom getConnectingAtom(IAtom atm) {
        IBond bnd = getConnectingBond(atm);
        IAtom connect = bnd.getAtom(0);
        if(connect.equals(atm)) {
            connect = bnd.getAtom(1);
        }
        return connect;
    }
}
