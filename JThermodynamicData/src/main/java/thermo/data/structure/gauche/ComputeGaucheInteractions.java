/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.gauche;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.mcss.RMap;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.Cycles;

import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.structure.substructure.FindSubstructure;
import thermo.data.structure.substructure.ListOfStructureMatches;
import thermo.data.structure.substructure.StructureMatch;
import thermo.data.structure.substructure.ThermodyanmicsForSubstructures;
import thermo.data.structure.substructure.SubStructure;

/**
 *
 * @author edwardblurock
 */
public class ComputeGaucheInteractions extends ThermodyanmicsForSubstructures {

    public ComputeGaucheInteractions(ThermoSQLConnection connection) throws SQLException, CDKException {
        super("StericCorrections", connection);
    }

    public void compute(IAtomContainer molecule, SetOfBensonThermodynamicBase thermo) throws SQLException, CDKException {
        FindSubstructure findSubstructure = new FindSubstructure(molecule, connection);
        System.out.println("ComputeGaucheInteractions" + namesOfType);
        ListOfStructureMatches matches = findSubstructure.findNonoverlappingSubstructures(namesOfType);
        Iterator<StructureMatch> iter = matches.iterator();
        //Cycles cycles = Cycles.all(molecule);
        while (iter.hasNext()) {
            StructureMatch match = iter.next();
            String name = match.getNameOfStructure();
            SubStructure sub = findSubstructure.getSubStructure(name);
            Iterator<RMap> miter = match.iterator();
            boolean includeGauche = true;
            Cycles.markRingAtomsAndBonds(molecule);
            while(miter.hasNext() && includeGauche) {
            	RMap rmap = miter.next();
            	IAtom atm = molecule.getAtom(rmap.getId1());
            	//System.out.println("Gauche: " + atm.toString());
            	if(atm.isInRing()) {
            		//System.out.println("Is it ring");
            		IAtom subatm = sub.getSubstructure().getAtom(rmap.getId2());
           		int count = sub.getSubstructure().getConnectedBondsCount(subatm);
           		//System.out.println("Count: " + count);
            		if(count> 1) {
            			includeGauche = false;
            		}
            	}
            }
            if(includeGauche) {
            	HashSet<BensonThermodynamicBase> vec = thermodynamics.retrieveStructuresFromDatabase(name);
            	Iterator<BensonThermodynamicBase> biter = vec.iterator();
            	BensonThermodynamicBase data = biter.next();
            	thermo.add(data);
            }
        }
    }
}
