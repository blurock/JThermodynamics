/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.matching;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomRef;
import org.openscience.cdk.Bond;
import org.openscience.cdk.graph.Cycles;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.utilities.MoleculeUtilities;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/** This class is used to find the connected substructures from a molecule
 * 
 * 
 *
 * @author blurock
 */
public class IsolateConnectedStructure {
    /**
     * 
     * @param mol The molecule to isolate connected structure from
     * @param connected  The atom with which the connected structure is bonded
     * @param firstinstructure The first atom in the connected structure 
     * @return A molecule which has the connected structure
     * 
     * 
     * 
     */
    public IAtomContainer IsolateConnectedStructure(IAtomContainer mol, IAtom connected, IAtom firstinstructure) {
        List<IAtom> directconnected = mol.getConnectedAtomsList(firstinstructure);
        IAtomContainer substructure = new AtomContainer();
        boolean notfound = true;
        Iterator<IAtom> i = directconnected.iterator();
        while(i.hasNext() && notfound) {
            IAtom atm = i.next();
            if(atm == connected) {
                notfound = false;
                directconnected.remove(atm);
            }
        }
        /*
        if(firstinstructure.isInRing() && connected.isInRing()) {
        	substructure.addAtom(firstinstructure);
        	substructure.addAtom(connected);

            List<IAtom> direct = new ArrayList<IAtom>(directconnected);
        	Iterator<IAtom> iter = direct.iterator();
        	while(iter.hasNext()) {
        		IAtom atm = iter.next();
        		substructure.addAtom(atm);
        		int count = 0;
        		if(atm.isInRing()) {
            		AtomRef catm = new AtomRef(connected);
            		MetaRingAtom ringatm = new MetaRingAtom(catm);  
            		ringatm.setSymbol(catm.getSymbol() + count++);
        			substructure.addAtom(ringatm);
        		} else {
                	findConnected(mol,substructure, firstinstructure, directconnected, connected);        			
        		}
        	}
        
        } else {
        */
        	substructure.addAtom(firstinstructure);
        	//boolean noloop = 
        	findConnected(mol,substructure, firstinstructure, directconnected, connected);
/*
        	if(!noloop) {
        		AtomRef catm = new AtomRef(connected);
        		MetaRingAtom ringatm = new MetaRingAtom(catm);
        		substructure.addAtom(ringatm);
        	}
*/
        //}
        return substructure;
    }

    private void connect(IAtomContainer substructure, IAtom atm, List<IAtom> directconnected) {
        Iterator<IAtom> i = directconnected.iterator();
        while(i.hasNext()) {
            IAtom connect = i.next();
            Bond bnd = new Bond(atm,connect);
            substructure.addAtom(connect);
            substructure.addBond(bnd);
        }
    }

    private boolean findConnected(IAtomContainer mol, IAtomContainer substructure, IAtom atm, List<IAtom> directconnected, IAtom original) {
        boolean noloop = true;
        if(!directconnected.contains(original)) {
            findNewAtoms(substructure, directconnected);
            connect(substructure,atm,directconnected);
            Iterator<IAtom> i = directconnected.iterator();
            while(i.hasNext() && noloop) {
                IAtom next = i.next();
                List<IAtom> connected = mol.getConnectedAtomsList(next);
                noloop = findConnected(mol,substructure,next,connected,original);
            }
        } else {
            noloop = false;
        }
        return noloop;
    }

    private void findNewAtoms(IAtomContainer substructure, List<IAtom> connected) {
      
        Iterator<IAtom> i = substructure.atoms().iterator();
        while(i.hasNext()) {
            IAtom atm = i.next();
            if(connected.contains(atm)) {
                connected.remove(atm);
            }
        }
    }
}
