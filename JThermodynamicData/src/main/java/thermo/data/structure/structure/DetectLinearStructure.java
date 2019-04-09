/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import java.io.IOException;
import java.sql.SQLException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.matching.SubstituteLinearStructures;

/**
 *
 * @author edwardblurock
 */
public class DetectLinearStructure extends SubstituteLinearStructures {

    public DetectLinearStructure(ThermoSQLConnection c) throws SQLException, CDKException, ClassNotFoundException, IOException {
        super(c);
    }
    public boolean isLinear(IAtomContainer mol) throws CDKException, IOException {
        IAtomContainer sub = substitute(mol);
        boolean ans = false;
        if(sub.getAtomCount() == 1)
            ans = true;
        return ans;
    }

}
