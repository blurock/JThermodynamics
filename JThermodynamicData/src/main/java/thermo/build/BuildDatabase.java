/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.build;

import jThergas.exceptions.JThergasReadException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.exception.CDKException;

import thermo.LineCommandsParameters;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.thergas.BuildBensonTable;
import thermo.data.benson.thergas.BuildThermoForMolecules;
import thermo.data.benson.thergas.BuildThermoForSubStructures;
import thermo.data.structure.structure.BuildStructureLibrary;
import thermo.data.structure.structure.BuildSubstructureLibrary;
import thermo.data.structure.structure.symmetry.BuildSymmetryDefinition;
import thermo.data.structure.structure.vibrational.ReadVibrationalModes;
import thermo.data.tables.BuildCalculationTable;
import thermo.exception.ThermodynamicException;

/**
 *
 * @author edwardblurock
 */
public class BuildDatabase {

    private static String initializeS = "Initialize";
    private static String bensonS = "Benson";
    private static String moleculesS = "Molecules";
    private static String structuresS = "Structures";
    private static String substructurethermoS = "SubstructureThermo";
    private static String metaatomdefS = "MetaAtoms";
    private static String symmetryDefinitionS = "SymmetryDefinition";
    private static String deleteS = "Delete";
    private static String resourceS = "JThermodynamicData/src/thermo/resources";
    private static String bensonGroupFileS = "Groups.input";
    private static String vibrationalmodeS = "Vibrational";
    private static String readBondLengthS = "BondLengths";
    private static String readCalculationTableS = "Table";
    private static String disassociationS = "DisassociationEnergy";

    /**
     * @param args the command line arguments
     */
    public static boolean executeCommand(String[] args) {
    	boolean foundCommand = true;
    	if(args.length < 1) {
    		foundCommand = false;
    	} else {
    		Map<String, String> parameters = LineCommandsParameters.parameterSetFromArguments(args);
    		System.out.println(parameters);
    		List<String> standardargs = LineCommandsParameters.requiredParameterInOrder(args);
    		String type = new String(args[0]);
    		System.out.println(type + "   " + LineCommandsParameters.thermoKeyword);
        try {
            if (type.equalsIgnoreCase(bensonS)) {
                buildBenson(standardargs, parameters);
            } else if (type.equalsIgnoreCase(moleculesS)) {
                buildMolecules(standardargs, parameters);
            } else if (type.equalsIgnoreCase(disassociationS)) {
                buildDisasociationEnergy(args);
            } else if (type.equalsIgnoreCase(substructurethermoS)) {
                buildSubStructureThermo(standardargs, parameters);
            } else if (type.equalsIgnoreCase(structuresS)) {
                buildSubstructures(args);
            } else if (type.equalsIgnoreCase(metaatomdefS)) {
                buildMetaAtoms(args);
            } else if (type.equalsIgnoreCase(vibrationalmodeS)) {
                buildVibrationalModes(args);
            } else if (type.equalsIgnoreCase(symmetryDefinitionS)) {
                buildSymmetryDefinition(args);
            } else if (type.equalsIgnoreCase(deleteS)) {
                deleteReferenceFromDatabase(args);
            } else if (type.equalsIgnoreCase(readBondLengthS)) {
                buildBondLengthFromDatabase(args);
            } else if (type.equalsIgnoreCase(readCalculationTableS)) {
                buildCalculationTable(args);
            } else {
            	foundCommand = false;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JThergasReadException ex) {
            Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    	}
        return foundCommand;
    }
    
    public static void commands() {
        System.out.println("Expecting the type of build:");
        System.out.println(bensonS + ":  For adding benson structure rules");
        System.out.println(moleculesS + ": For adding molecular thermodynamic information");
        System.out.println(metaatomdefS + ": For adding substructures with type");
        System.out.println(structuresS + ": For adding structures for meta-atoms");
        System.out.println(initializeS + ": Initialize Database with standard information");
        System.out.println(symmetryDefinitionS + ": For adding symmetry definitions");
        System.out.println("\nEach of these types have further line arguments.");
        System.out.println("Put in the type to see further information about what is needed.");
    }
    


    private static void buildBenson(List<String> standardargs, Map<String, String> parameters) throws FileNotFoundException, JThergasReadException, IOException {
        if (standardargs.size()< 4) {
            System.out.println(moleculesS + " Filename ReferenceName");
            System.out.println("Filename: The file with the benson thermodynamics");
            System.out.println("Referencename: The name of the source type of this data (for example \"Standard\") ");
            System.out.println("Test: if true, will not enter data in database ");
        } else {
            String fileS = standardargs.get(1);
            String referenceS = standardargs.get(2);
            String testS = standardargs.get(3);
            String energyunits = parameters.get(LineCommandsParameters.energyunits);
            
            System.out.println("File         : " + fileS);
            System.out.println("Reference    : " + referenceS);
            System.out.println("Energy       : " + energyunits);
            
            boolean test = true;
            if(testS.compareTo("false") == 0) {
            	test = false;
            	System.out.println("Read file and upload to database");
            } else {
            	System.out.println("Just read in file, no upload to database");
            }
            File fileF = new File(fileS);
            BuildBensonTable buildBenson = new BuildBensonTable();
            String output = buildBenson.build(fileF, referenceS,test,energyunits);
            System.out.println(output);
        }
    }

    private static void buildMolecules(List<String> standardargs, Map<String, String> parameters) {
        if (standardargs.size()< 4) {
            System.out.println(moleculesS + " Filename ReferenceName");
            System.out.println("Filename: The file with the benson thermodynamics");
            System.out.println("Referencename: The name of the source type of this data (for example \"Standard\") ");
            System.out.println("Test: if true, will not enter data in database ");
        } else {
            String fileS = standardargs.get(1);
            String referenceS = standardargs.get(2);
            String testS = standardargs.get(3);
            String energyunits = parameters.get(LineCommandsParameters.energyunits);
            boolean test = true;
            if(testS.compareTo("false") == 0) {
            	test = false;
            	System.out.println("Read file and upload to database");
            } else {
            	System.out.println("Just read in file, no upload to database");
            }

            BuildThermoForMolecules build = new BuildThermoForMolecules();
            ThermoSQLConnection connection = new ThermoSQLConnection();
            connection.connect();
            try {
                System.out.println("Initial Database with '" + referenceS + "' as source");
                build.initializeTable(referenceS,!test);
            } catch (SQLException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Read in File (" + referenceS + "): " + fileS);
            try {
                File bensonFile = new File(fileS);
                build.build(connection, bensonFile, referenceS, !test, energyunits);
            } catch (JThergasReadException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static void buildSubStructureThermo(List<String> standardargs, Map<String, String> parameters) {
        if (standardargs.size() < 5) {
            System.out.println(substructurethermoS + " Filename Type ReferenceName");
            System.out.println("Filename: The file with the benson thermodynamics");
            System.out.println("Type: The type of substructure for the thermo data");
            System.out.println("Referencename: The name of the source type of this data (for example \"Standard\") ");
            System.out.println("Test: if true, will not enter data in database ");
        } else {
            String fileS = standardargs.get(1);
            String typeS = standardargs.get(2);
            String referenceS = standardargs.get(3);
            String testS = standardargs.get(4);
            String energyunits = parameters.get(LineCommandsParameters.energyunits);
            boolean testB = true;
            if(testS.compareTo("false") == 0) {
            	testB = false;
            }
            BuildThermoForSubStructures build = new BuildThermoForSubStructures(typeS,referenceS);
            ThermoSQLConnection connection = new ThermoSQLConnection();
            connection.connect();
            try {
                System.out.println("Initial Database with '" + referenceS + "' as source");
                build.initializeTable(referenceS, !testB);
            } catch (SQLException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Read in File (" + referenceS + "): " + fileS);
            try {
                File bensonFile = new File(fileS);
                build.build(connection, bensonFile, referenceS, !testB, energyunits);
            } catch (JThergasReadException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void buildMetaAtoms(String[] args) throws SQLException {
        ThermoSQLConnection connection = new ThermoSQLConnection();
        connection.connect();
        if (args.length < 3) {
            System.out.println(metaatomdefS + " Filename Test");
            System.out.println("Filename: File with structure name and meta atom name and type");
            System.out.println("Test: if true, will not enter data in database ");
        } else {
            try {
                String fileS = args[1];
                String testS = args[2];
                boolean test = true;
                if(testS.compareTo("false") == 0) {
                	test = false;
                	System.out.println("Read file and upload to database");
                } else {
                	System.out.println("Just read in file, no upload to database");
                }
                BuildStructureLibrary buildstructure = new BuildStructureLibrary(connection);
                File fileF = new File(fileS);
                buildstructure.build(fileF,!test);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CDKException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static void buildSubstructures(String[] args) throws SQLException {
        ThermoSQLConnection connection = new ThermoSQLConnection();
        connection.connect();
        if (args.length < 3) {
            System.out.println(structuresS + " Filename ReferenceName");
            System.out.println("Filename: The file with nancy structures, name and type");
            System.out.println("Test: if true, will not enter data in database ");
        } else {
            try {
                String fileS = args[1];
                String testS = args[2];
                boolean test = true;
                if(testS.compareTo("false") == 0) {
                	test = false;
                	System.out.println("Read file and upload to database");
                } else {
                	System.out.println("Just read in file, no upload to database");
                }
                BuildSubstructureLibrary buildstructure = new BuildSubstructureLibrary(connection);
                File fileF = new File(fileS);
                buildstructure.build(fileF,!test);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CDKException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static void buildVibrationalModes(String[] args) throws SQLException {
        ThermoSQLConnection connection = new ThermoSQLConnection();
        connection.connect();
        if (args.length < 4) {
            System.out.println(vibrationalmodeS + " Filename ReferenceName");
            System.out.println("Filename: The file with the benson thermodynamics");
            System.out.println("Referencename: The name of the source type of this data (for example \"Standard\") ");
            System.out.println("Test: if true, will not enter data in database ");
        } else {
                String fileS = args[1];
                String referenceS = args[2];
                String testS = args[3];
                boolean test = true;
                if(testS.compareTo("false") == 0) {
                	test = false;
                	System.out.println("Read file and upload to database");
                } else {
                	System.out.println("Just read in file, no upload to database");
                }
                
                ReadVibrationalModes vibread = new ReadVibrationalModes(connection);
                File fileF = new File(fileS);
            try {
                vibread.build(fileF,referenceS, !test);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CDKException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void deleteReferenceFromDatabase(String[] args) {
        if (args.length < 2) {
            System.out.println(deleteS + " Database ReferenceName");
            System.out.println("Database: The database type: Molecule, Benson");
            System.out.println("Referencename: The name of the source type of this data (for example \"Standard\") ");
            System.out.println("Test: if true, will not enter data in database ");
        } else {
            System.out.println("Delete From Database");
            String databasetype = args[1];
            String referenceS = args[2];
            String testS = args[3];
            boolean test = true;
            if(testS.compareTo("false") == 0) {
            	test = false;
            	System.out.println("Read file and upload to database");
            } else {
            	System.out.println("Just read in file, no upload to database");
            }

            if (databasetype.equalsIgnoreCase(moleculesS)) {
                try {
                    System.out.println("Delete From Molecule Database: Reference ='" + referenceS + "'");
                    BuildThermoForMolecules build = new BuildThermoForMolecules();
                    build.initializeTable(referenceS, !test);
                } catch (SQLException ex) {
                    Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.err.println("Command not found: " + databasetype);
            }
        }

    }

    private static void buildSymmetryDefinition(String[] args) {
        ThermoSQLConnection connection = new ThermoSQLConnection();
        connection.connect();
        if (args.length < 3) {
            System.out.println(symmetryDefinitionS + " Filename ReferenceName");
            System.out.println("Filename: The file with the symmetry definition (structure and assignments)");
            System.out.println("Test: if true, will not enter data in database ");
        } else {
            try {
                String fileS = args[1];
                String testS = args[2];
                boolean test = true;
                if(testS.compareTo("false") == 0) {
                	test = false;
                	System.out.println("Read file and upload to database");
                } else {
                	System.out.println("Just read in file, no upload to database");
                }

                BuildSymmetryDefinition build = new BuildSymmetryDefinition(connection);
                File fileF = new File(fileS);
                build.build(fileF, !test);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CDKException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

    private static void buildBondLengthFromDatabase(String[] args) {
         ThermoSQLConnection connection = new ThermoSQLConnection();
        connection.connect();
        if (args.length < 3) {
            System.out.println(symmetryDefinitionS + " Filename ReferenceName");
            System.out.println("Filename: The file with the benson thermodynamics");
            System.out.println("Referencename: The name of the source type of this data (for example \"Standard\") ");
            System.out.println("Test: if true, will not enter data in database ");
        } else {
            try {
                String filename = args[1];
                String source = args[2];
                String testS = args[3];
                boolean test = true;
                if(testS.compareTo("false") == 0) {
                	test = false;
                	System.out.println("Read file and upload to database");
                } else {
                	System.out.println("Just read in file, no upload to database");
                }

                ReadBondLengthTable read = new ReadBondLengthTable(connection);
                read.read(filename, source, !test);
            } catch (SQLException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }

    private static void buildCalculationTable(String[] args) {
        ThermoSQLConnection connection = new ThermoSQLConnection();
        connection.connect();
        if (args.length < 2) {
            System.out.println(readCalculationTableS + " Filename");
            System.out.println("Filename: The file with the table information");
        } else {
            try {
                String filename = args[1];
                File fileF = new File(filename);
                @SuppressWarnings("unused")
				BuildCalculationTable table = new BuildCalculationTable(fileF,connection);
            } catch (SQLException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }

    private static void buildDisasociationEnergy(String[] args) {
          ThermoSQLConnection connection = new ThermoSQLConnection();
        connection.connect();
        if (args.length < 3) {
            System.out.println(disassociationS + " Filename ReferenceName");
            System.out.println("Filename: The file with the disassociation energies");
            System.out.println("Referencename: The name of the source type of this data (for example \"Standard\") ");
            System.out.println("Test: if true, will not enter data in database ");
        } else {
            try {
                String filename = args[1];
                String source = args[2];
                String testS = args[3];
                boolean test = true;
                if(testS.compareTo("false") == 0) {
                	test = false;
                	System.out.println("Read file and upload to database");
                } else {
                	System.out.println("Just read in file, no upload to database");
                }

                File fileF = new File(filename);
                ReadDisassociationData read = new ReadDisassociationData(connection);
                read.build(fileF, source,!test);
            } catch (CDKException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ThermodynamicException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(BuildDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
       }

    }
}
