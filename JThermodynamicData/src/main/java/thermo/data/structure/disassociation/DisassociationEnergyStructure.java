/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.disassociation;

import java.sql.SQLException;
import org.openscience.cdk.interfaces.IAtomContainer;
import thermo.data.structure.substructure.SubStructure;
import thermo.data.structure.utilities.MoleculeUtilities;

/**
 *
 * @author edwardblurock
 */
public class DisassociationEnergyStructure extends SubStructure {

    public DisassociationEnergyStructure() throws SQLException {
    }
    public DisassociationEnergyStructure(IAtomContainer substructure, String source) {
        this.setSubstructure(substructure);
        this.setSourceOfStructure(source);
    }

    @Override
    public void setSubstructure(IAtomContainer substructure) {
        this.substructure = (IAtomContainer) MoleculeUtilities.eliminateHydrogens(substructure);
    }

}
