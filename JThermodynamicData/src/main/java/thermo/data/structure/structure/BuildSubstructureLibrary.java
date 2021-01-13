/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import jThergas.data.read.ReadFileToString;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLAtomCounts;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.DB.SQLStructureType;
import thermo.data.structure.linearform.NancyLinearFormToGeneralStructure;

/**
 *
 * @author edwardblurock
 */
public class BuildSubstructureLibrary {
    ThermoSQLConnection connect;
    NancyLinearFormToGeneralStructure linearStructure;
    SQLStructureAsCML sqlStructureAsCML;
    SQLStructureType sqlStructureType;
    SQLAtomCounts sqlAtomCounts;

    public BuildSubstructureLibrary(ThermoSQLConnection connect) throws SQLException {
        this.connect = connect;
        linearStructure = new NancyLinearFormToGeneralStructure(connect);
        sqlStructureAsCML = new SQLStructureAsCML(connect);
        sqlStructureType = new SQLStructureType(connect);
        sqlAtomCounts = new SQLAtomCounts(connect);
    }
    public void build(File fileF, boolean storedata) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException, CDKException {
        ReadFileToString read = new ReadFileToString();
        FileReader r = new FileReader(fileF);
        BufferedReader breader = new BufferedReader(r);

        read.read(breader);

        build(read.outputString,storedata);
    }

     void build(String data, boolean storedata) throws SQLException, CDKException, ClassNotFoundException, IOException {
        StringTokenizer tok = new StringTokenizer(data,"\n");
        while(tok.hasMoreTokens()){
            String line = tok.nextToken();
            parseLine(line,storedata);
        }
    }

    public void parseLine(String line,boolean storedata) throws SQLException, CDKException, ClassNotFoundException, IOException {
        StringTokenizer tok = new StringTokenizer(line);
        if(tok.countTokens() > 2) {
            String nancy           = tok.nextToken();
            String nameOfStructure = tok.nextToken();
            ArrayList<String> types = new ArrayList<String>();

            IAtomContainer molecule = linearStructure.convert(nancy);
            molecule.setID(nameOfStructure);
            StructureAsCML cmlstruct = new StructureAsCML(molecule);

            while(tok.hasMoreTokens()) {
                String typeS = tok.nextToken();
                StructureType type = new StructureType(nameOfStructure,typeS);
                if(storedata) {
                	sqlStructureType.deleteElement(type);
                	sqlStructureType.addToDatabase(type);
                } else {
                	System.out.println(type.writeAsString());
                }
            }
            AtomCounts counts = new AtomCounts(molecule);
            if(storedata) {
            	sqlStructureAsCML.deleteElement(cmlstruct);
            	sqlStructureAsCML.addToDatabase(cmlstruct);
            	sqlAtomCounts.deleteElement(counts.getMoleculeID());
            	sqlAtomCounts.addToDatabase(counts);
            } else {
            	System.out.println(cmlstruct.toString());
            	System.out.println(counts.toString());
            }
        }

    }


}
