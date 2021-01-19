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
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.Bond;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.ISingleElectron;

import thermo.data.structure.utilities.MoleculeUtilities;

/**
 *
 * @author blurock
 */
public class QueryMoleculeWithMetaAtoms extends AtomContainer {

	QueryMoleculeWithMetaAtoms(IAtomContainer mol) {
		List<IAtom> radicals = MoleculeUtilities.findSingleElectrons(mol);

        List<ISingleElectron> atoms = new ArrayList<ISingleElectron>();
        for(int i=0;i<mol.getSingleElectronCount();i++) {
           ISingleElectron sing = mol.getSingleElectron(i);
           atoms.add(sing);
       }

        //System.out.println("QueryMoleculeWithMetaAtoms: molecule: " + mol.getClass().getCanonicalName());
		for (int i = 0; i < mol.getAtomCount(); i++) {
			IAtom atm = mol.getAtom(i);
			//System.out.println("QueryMoleculeWithMetaAtoms: " + atm.getClass().getCanonicalName()); 
			//System.out.println("QueryMoleculeWithMetaAtoms: " + atm.getSymbol()); 
			QueryAtomWithMetaAtoms qatm = new QueryAtomWithMetaAtoms(atm);
			qatm.setSymbol(atm.getSymbol());
			
			//Iterator<IAtom> iter = radicals.iterator();
			Iterator<ISingleElectron> iter  = atoms.iterator();
			
			while (iter.hasNext()) {
				ISingleElectron sing = iter.next();
				if(sing.contains(atm)) {
					qatm.setToRadical();
				}
			}
			this.addAtom(qatm);
		}
		for (int i = 0; i < mol.getBondCount(); i++) {
			IBond bnd = mol.getBond(i);

			int i1 = mol.getAtomNumber(bnd.getAtom(0));
			int i2 = mol.getAtomNumber(bnd.getAtom(1));
			QueryAtomWithMetaAtoms qatm1 = (QueryAtomWithMetaAtoms) this.getAtom(i1);
			QueryAtomWithMetaAtoms qatm2 = (QueryAtomWithMetaAtoms) this.getAtom(i2);
			QueryAtomWithMetaAtoms[] vec = new QueryAtomWithMetaAtoms[2];
			vec[0] = qatm1;
			vec[1] = qatm2;
			IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
			QueryBondWithMetaAtoms qbnd = new QueryBondWithMetaAtoms(builder);
			qbnd.setOrder(bnd.getOrder());
			if (bnd.getFlag(CDKConstants.ISAROMATIC)) {
				qbnd.setFlag(CDKConstants.ISAROMATIC, true);
				qatm1.setFlag(CDKConstants.ISAROMATIC, true);
				qatm1.setFlag(CDKConstants.ISAROMATIC, true);
			}
			qbnd.setAtoms(vec);
			this.addBond((IBond) qbnd);
		}
	}

}
