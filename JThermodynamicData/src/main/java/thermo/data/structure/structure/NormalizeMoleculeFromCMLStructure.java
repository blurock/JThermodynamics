/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure;

import java.io.IOException;
import java.util.Iterator;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
/**
 *
 * @author blurock
 */
public class NormalizeMoleculeFromCMLStructure {

    String unspecifiedAtom1 = "R";
    String unspecifiedAtom2 = "*";

    public NormalizeMoleculeFromCMLStructure() {
    }
    public boolean unspecifiedAtom(String name) {
        boolean ans = name.equals(unspecifiedAtom1) 
                || name.equals(unspecifiedAtom2)
                || name.equals(name);
        return ans;
    }
    
    public IAtomContainer getNormalizedMolecule(StructureAsCML cmlstruct) throws CDKException, ClassNotFoundException, IOException {
        IAtomContainer mol = cmlstruct.getMolecule();
        


        initialNormalizationOfAtoms(mol);
        initialNormalizationOfBonds(mol);

       //ValencyChecker vcheck = new ValencyChecker();
        //HybridizationMatcher hybrid = new HybridizationMatcher();
        for (int i = 0; i < mol.getAtomCount(); i++) {
            IAtom atm = mol.getAtom(i);
            if (!unspecifiedAtom(atm.getSymbol())) {
                IsotopeFactory  obj = new IsotopeFactory() {
                };
                if(obj.isElement(atm.getSymbol())) {
                    obj.configure(atm);
                } else {
                    System.err.println("NormalizeMoleculeFromCMLStructure Not an element: " + atm.getSymbol());
                }
                
            /*
            //IAtomType tp = hybrid.findMatchingAtomType(mol, atm);
            //atm.setSymbol(tp.getAtomTypeName());
            atm.setHydrogenCount(vcheck.calculateNumberOfImplicitHydrogens(atm));
            atm.setFormalNeighbourCount(tp.getFormalNeighbourCount());
            atm.setValency(tp.getValency());
            atm.setBondOrderSum(tp.getBondOrderSum());
            atm.setAtomTypeName(tp.getAtomTypeName());
             */
            } else {
                //atm.setSymbol(tp.getAtomTypeName());
                atm.setImplicitHydrogenCount(0);
                atm.setFormalNeighbourCount(mol.getConnectedAtomsCount(atm));
                //atm.setValency();
                //atm.setBondOrderSum(mol.getBondOrderSum(atm));
            //atm.setAtomTypeName(tp.getAtomTypeName());

            }
        }
        setAromaticity(mol);
        /*
        for (int i = 0; i < mol.getAtomCount(); i++) {
            IAtom atm = mol.getAtom(i);
            //printAtomInfo(atm);
        }
        */
        return mol;
    }

    private void initialNormalizationOfAtoms(IAtomContainer mol) {
        for (int i = 0; i < mol.getAtomCount(); i++) {
            IAtom atm = mol.getAtom(i);
            atm.setImplicitHydrogenCount(0);
        }
    }

    private void initialNormalizationOfBonds(IAtomContainer mol) {
         for (int i = 0; i < mol.getBondCount(); i++) {
            IBond bnd = mol.getBond(i);
            boolean aromatic = bnd.getFlag(CDKConstants.ISAROMATIC);
            if(aromatic) {
                //bnd.setOrder(IBond.Order.SINGLE);
                //IAtom atm1 = bnd.getAtom(0);
                //IAtom atm2 = bnd.getAtom(1);
            }
        }
    }

    private void setAromaticity(IAtomContainer mol) throws CDKException {
        Iterator<IBond> bonds = mol.bonds().iterator();
        while(bonds.hasNext()) {
            IBond bond = bonds.next();
            if(bond.getFlag(CDKConstants.ISAROMATIC))
                bond.setFlag(CDKConstants.ISAROMATIC, false);
        }
        Iterator<IAtom> atoms = mol.atoms().iterator();
        while(atoms.hasNext()) {
            IAtom atm = atoms.next();
            if(atm.getFlag(CDKConstants.ISAROMATIC)) {
                atm.setFlag(CDKConstants.ISAROMATIC, false);
            }
        }
   
    }
/*
    private void printAtomInfo(Atom atm) {
        System.out.println("Atom ID                       " + atm.getID());
        System.out.println("Symbol                        " + atm.getSymbol());
        System.out.println("Hydrogen Count                " + atm.getImplicitHydrogenCount());
        System.out.println("Formal Neighbor Count         " + atm.getFormalNeighbourCount());
        System.out.println("Valuence                      " + atm.getValency());
        System.out.println("Atom Type Name                " + atm.getFormalNeighbourCount());
        System.out.println("Bond order sum                " + atm.getBondOrderSum());

    }
    */
}
