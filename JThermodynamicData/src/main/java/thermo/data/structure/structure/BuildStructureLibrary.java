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
import java.util.HashSet;
import java.util.StringTokenizer;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.aromaticity.Aromaticity;
//import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.linearform.NancyLinearFormToGeneralStructure;
import thermo.data.structure.structure.DB.SQLMetaAtomInfo;

/**
 *
 * @author edwardblurock
 */
public class BuildStructureLibrary {

    ThermoSQLConnection connect;
    NancyLinearFormToGeneralStructure linearStructure;
    SQLMetaAtomInfo sqlMetaAtomInfo;
    SQLStructureAsCML sqlStructureAsCML;

    String linearMetaAtomAtomS = "L";

    public BuildStructureLibrary(ThermoSQLConnection c) throws SQLException {
        connect = c;
        linearStructure = new NancyLinearFormToGeneralStructure(connect);
        sqlMetaAtomInfo = new SQLMetaAtomInfo(connect);
        sqlStructureAsCML = new SQLStructureAsCML(connect);
    }
    
    
    public BuildStructureLibrary(HashSet<MetaAtomInfo> ans) {
        linearStructure = new NancyLinearFormToGeneralStructure(ans);    	
    }
    
    
    public void build(File fileF, boolean storedata) throws FileNotFoundException, IOException, SQLException, CDKException, ClassNotFoundException {
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

    public void parseLine(String line, boolean storedata) throws SQLException, CDKException, ClassNotFoundException, IOException {
    	MetaAtomLine atomline = parseToMetaAtom(line,storedata);
    	StructureAsCML cmlstruct = setUpStructureAsCML(atomline.getNancy(),atomline.getElementName());
    	storeMetaAtomDefinition(cmlstruct,atomline,storedata);
    }
	
	public MetaAtomLine parseToMetaAtom(String line, boolean storedata) {
	   	if(!storedata) {
    		System.out.println("\n=========================================");
    		System.out.println("Parsed Information=======================");
    	}
    	StringTokenizer tok = new StringTokenizer(line);
    	MetaAtomInfo info = new MetaAtomInfo();
    	MetaAtomLine atomline = new MetaAtomLine(info);
        if(tok.countTokens() > 3) {
            String nancy           = tok.nextToken();
            String nameOfStructure = tok.nextToken();
            String metaAtomName    = tok.nextToken();
            String typeOfStructure = tok.nextToken();

            
            atomline.setElementName(nameOfStructure);
            atomline.setMetaAtomName(metaAtomName);
            atomline.setMetaAtomType(typeOfStructure);
            atomline.setNancy(nancy);
        }
        return atomline;
	}
	public StructureAsCML setUpStructureAsCML(String nancy, String nameOfStructure) throws SQLException, CDKException {

            IAtomContainer molecule = linearStructure.convert(nancy);
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
            boolean detectAromaticity = Aromaticity.cdkLegacy().apply(molecule);
            
            //boolean detectAromaticity = CDKHueckelAromaticityDetector.detectAromaticity(molecule);
            molecule.setID(nameOfStructure);
            StructureAsCML cmlstruct = new StructureAsCML(molecule);
            if(detectAromaticity) {
                System.out.println("detect Aromaticity: \n" + cmlstruct.getCmlStructureString());
            }
            return cmlstruct;
	}
		

    public void storeMetaAtomDefinition(StructureAsCML cmlstruct, MetaAtomInfo info, boolean storedata) throws SQLException {
        if(storedata) {
        sqlMetaAtomInfo.deleteElement(info);
        sqlMetaAtomInfo.addToDatabase(info);
        sqlStructureAsCML.deleteElement(cmlstruct);
        sqlStructureAsCML.addToDatabase(cmlstruct);
        } else {
    		System.out.println("\n=========================================");
    		System.out.println(info.toString());
    		System.out.println(cmlstruct.toString());
        }
    	
    }

}
