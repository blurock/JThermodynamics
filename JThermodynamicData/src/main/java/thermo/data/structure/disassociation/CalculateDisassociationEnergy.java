/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.disassociation;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.structure.disassociation.DB.SQLDisassociationEnergy;
import thermo.data.structure.substructure.FindSubstructure;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author edwardblurock
 */
public class CalculateDisassociationEnergy {
    ThermoSQLConnection connect;
    String disassociationS = "DisassociationEnergy";

    public CalculateDisassociationEnergy(ThermoSQLConnection connect) {
        this.connect = connect;
    }

    public void calculate(IAtomContainer mol, SetOfBensonThermodynamicBase corrections) throws SQLException, CDKException {
        BensonThermodynamicBase thermo = (BensonThermodynamicBase) calculate(mol);
        corrections.add(thermo);
    }
    public ThermodynamicInformation calculate(IAtomContainer mol) throws SQLException, CDKException {
        Double entropyD = Double.valueOf(0.0);

        DisassociationEnergy energy = getDisassociationEnergyForMolecule(mol);
        Double energyD = energy.getDisassociationEnergy();
        BensonThermodynamicBase thermo = new BensonThermodynamicBase(disassociationS,null,energyD,entropyD);
        String referenceS = "Disassociation Energy From: " + energy.getSubstructure().getID();
        thermo.setID(referenceS);
        thermo.setReference("Disassociation Energy");
        return thermo;
    }

    public Double calculateDisassociationEnergy(IAtomContainer mol) throws SQLException, CDKException {
        DisassociationEnergy energy = getDisassociationEnergyForMolecule(mol);
        return energy.getDisassociationEnergy();
    }
    public DisassociationEnergy getDisassociationEnergyForMolecule(IAtomContainer mol) throws SQLException, CDKException {
       DisassociationEnergy energy = null;
       FindSubstructure find = new FindSubstructure(mol, connect);
       StructureAsCML cml = new StructureAsCML(mol);
       /*
       System.out.println(cml.toString());
       */
       SQLDisassociationEnergy sqldiss = new SQLDisassociationEnergy(connect);
       List<String> names = sqldiss.listOfDisassociationStructures();
       String name = find.findLargestSubstructure(names);
       HashSet<DisassociationEnergy> energyV = sqldiss.retrieveStructuresFromDatabase(name);
       if(energyV.size() == 1) {
           Iterator<DisassociationEnergy> iter = energyV.iterator();
           energy = iter.next();
       } else if(energyV.size() == 0) {
           System.out.println(names);
           Iterator<String> iter = names.iterator();
           while(iter.hasNext())
        	   System.out.print(" " + iter.next());
           System.out.println("");
           
           StructureAsCML cmlE = new StructureAsCML(mol);
           System.out.println(cmlE.toString());           
           throw new CDKException("Disassociation Energy for " + mol.getID() + " could not be found\n");
       } else {
           String error = "ERROR in Database: "
                   + energyV.size()
                   + " structures found for single disassociation structure "
                   + name;
           throw new CDKException(error);
       }
      return energy;
    }

}
