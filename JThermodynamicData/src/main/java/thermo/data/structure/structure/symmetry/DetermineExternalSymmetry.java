/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.utilities.MoleculeUtilities;
import thermo.properties.SProperties;

/**
 *
 * @author edwardblurock
 */
public class DetermineExternalSymmetry extends DetermineTotalSymmetry {
	
	boolean debug = false;
	
    private int highestSymmetry = 12;
    String dontCheck = "X";
    String noCheckS = "N";
    String linearCheckS = "L";
    SetOfBensonThermodynamicBase setOfCorrections;
    double gasConstant;
    String referenceS = "External Symmetry Correction";
    private final DetermineExternalSymmetry determineSecondary;

    public SetOfBensonThermodynamicBase getSetOfCorrections() {
        return setOfCorrections;
    }

    public void setSetOfCorrections(SetOfBensonThermodynamicBase set) {
        this.setOfCorrections = set;
    }

    public DetermineExternalSymmetry(
            DetermineSymmetryFromSingleDefinition determine,
            SetOfSymmetryDefinitions definitions,
            SetOfSymmetryDefinitions secondary) {
        super(determine, definitions);
        determineSymmetry = determine;
        String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
        gasConstant = Double.valueOf(gasconstantS).doubleValue();
        if (secondary != null) {
            DetermineExternalSymmetryFromSingleDefinition single = new DetermineExternalSymmetryFromSingleDefinition();
            determineSecondary = new DetermineExternalSymmetry(single, secondary, null);
        } else {
            determineSecondary = this;
        }
    }

    @Override
    public void initializeSymmetry() {
        symmetryValue = 1;
    }

    @Override
    public int determineSymmetry(IAtomContainer structure, SetOfBensonThermodynamicBase corrections) throws CDKException {
    	setOfCorrections = new SetOfBensonThermodynamicBase();
        initializeSymmetry();
        
        double topsymmetry = 1.0;
        int currentSymmetry = highestSymmetry;
        while (currentSymmetry > 0 && topsymmetry == 1.0) {
            Iterator<SymmetryDefinition> idef = symmetryDefinitions.iterator();
            while (idef.hasNext()) {
                SymmetryDefinition defintion = idef.next();
                int defsymmetry = defintion.getInternalSymmetryFactor().intValue();
                if (defsymmetry == currentSymmetry) {
                    if (topsymmetry < defintion.getInternalSymmetryFactor().doubleValue()) {
                    	if(debug) {
                    		System.out.println("Determine Symmetry: def=" + defintion.getMetaAtomName());
                    	}
                        int symmetry = determineSymmetry.determineSymmetry(defintion, structure);
                        if(debug) {
                        	System.out.println("Determine Symmetry: ans= " + symmetry);
                        }
                         combineInSymmetryNumber(symmetry);
                        topsymmetry = symmetryValue;
                        if(debug) {
                        	System.out.println("Determine Symmetry: topsymmetry= " + symmetry);
                        	System.out.println(corrections.toString());
                        }
                    } else {
                    	if(debug) {
                    		System.out.println("Symmetry Skipped, cannot yield higher symmetry");
                    	}
                    }
                }
            }
            if(debug) {
            	System.out.println("Top Symmetry: " + topsymmetry);
            }
            currentSymmetry--;
        }
        if(debug) {
        	System.out.println("setOfCorrections: \n" + setOfCorrections.toString());
        	System.out.println("corrections: \n" + corrections.toString());
        }
        corrections.add(setOfCorrections);
        return getSymmetryValue();
    }
 /*  combineInSymmetryNumber
  * 
  *
 *
 */
    public void combineInSymmetryNumber(int symmetry) {
    	if(debug) {
    		System.out.println("combineInSymmetryNumber: " + symmetry);
    	}
        Iterator<SymmetryMatch> iter = determineSymmetry.getSymmetryMatches().iterator();
        while (iter.hasNext()) {
            SymmetryMatch match = iter.next();
            if(debug) {
            	System.out.println("Find Symmetry of " + match.toString());
            }
            try {
                double symmD = findSymmetryContribution(match);
                if (symmetryValue < symmD) {
                    symmetryValue = (int) symmD;
                } else {
                	if(debug) {
                		System.out.println("Found Symmetry: " + symmD + "\t but less than current top " + symmetryValue);
                	}
                }
                symmetryValue *= symmD;
            } catch (CDKException ex) {
                Logger.getLogger(DetermineExternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

    public int getSymmetryValue() {
        return symmetryValue;
    }
/* This routine determines if the side condition holds for the symmetry
 *     Loop through SetOfSymmetryAssignments and check each SymmetryAssignment
 *
 * pairs:  set of (group, match in orignal molecule): extracted from symmetry definition
 * For each SymmetryMatch (match of symmetry structure and original molecule):
 *
 *   mol: The structure associated with the SymmetryAssignment
 *      From SymmstryAssignment, the type of symmetry expected is extracted
 *
 *         If the side condition (type of symmetry) holds, then
 *             symmD is set to internal symmetry factor
 *                Linear:  {@link isMoleculeLinear}
 *                NoCheck: do nothing
 *                Otherwise:  {@link findSymmetryOfConnection}
 *         if notdone is set to false, that means one of the tests failed.
 *         all tests must be true not to fail (i.e. notdone remains true).
 */
    private double findSymmetryContribution(SymmetryMatch match) throws CDKException {
    	if(debug) {
    		System.out.println("findSymmetryContribution");
    	}
        double symmD = 1.0;
        SetOfSymmetryAssignments assignments = match.getFromMolecule();
        Iterator<String> iter = assignments.keySet().iterator();
        //System.out.println("\tFind symmetry of Connections for " + match.toString());
        //List<SymmetryPair> pairs = this.determineSymmetry.symmetryDefinition.extractListOfSymmetryPairs();
        boolean notdone = true;
        while (iter.hasNext() && notdone) {
            SymmetryAssignment assignment = assignments.get(iter.next());
            //String connectionSymmetry = findConnectionSymmetry(assignment, pairs);
            String connectionSymmetry = assignment.getSymmetryConnection();
            IAtomContainer mol = assignment.getStructure();
            if (connectionSymmetry.contains(linearCheckS)) {
                boolean isLinear = isMoleculeLinear(mol);
                if (isLinear) {
                    //System.out.println("\t\tSymmetry of Connection: " + assignment.getGroupName() + " is linear");
                    //System.out.println("Contributing Symmetry: " + symmD);
                } else {
                    notdone = false;
                    }
            } else if (connectionSymmetry.contains(dontCheck)) {
            } else if (!connectionSymmetry.contains(noCheckS)) {
                double symmetryOfConnection = findSymmetryOfConnection(mol, assignment.getGroupName());
                 double expected = Double.parseDouble(connectionSymmetry);
                if (symmetryOfConnection == expected) {
                } else {
                    notdone = false;
                    }
            }

        }
        if(debug) {
        	System.out.println("findSymmetryContribution notdone=" + notdone);
        }
        if(notdone) {
            symmD = this.determineSymmetry.symmetryDefinition.getInternalSymmetryFactor();
        }
        if(debug) {
        	System.out.println("findSymmetryContribution symmD=" + symmD);
        }
        if (symmD != 1.0) {
            String symname = this.determineSymmetry.symmetryDefinition.getMetaAtomName();
            if(debug) {
            	System.out.println("findSymmetryContribution symname=" + symname);
            }
            double correction = -gasConstant * Math.log(symmD);
            if(debug) {
            	System.out.println("findSymmetryContribution correction=" + correction);
            }
            if (setOfCorrections != null) {
                BensonThermodynamicBase benson = new BensonThermodynamicBase(referenceS, null, 0.0, correction);
                symname = symname + "(" + symmD + ")";
                benson.setReference(symname);
                if(debug) {
                	System.out.println(benson.toString());
                }
                setOfCorrections.add(benson);
            }
        } else {
        	if(debug) {
        		System.out.println("Did not satisfy secondary requirement");
        	}
        }

        return symmD;
    }

    private boolean isMoleculeLinear(IAtomContainer mol) {
        boolean ans = false;
        if (mol.getAtomCount() == 1) {
            ans = true;
        }
        return ans;
    }

    private double findSymmetryOfConnection(IAtomContainer mol, String connection) throws CDKException {
        //SetOfBensonThermodynamicBase set = setOfCorrections;
        setOfCorrections = new SetOfBensonThermodynamicBase();
        IAtomContainer cpymol = new AtomContainer(mol);
        IAtom connected = MoleculeUtilities.findAtomInMolecule(connection, cpymol);
        Atom x = new Atom("R");
        x.setSymbol("X");
        x.setID("X");
        Bond bndx = new Bond(connected, x);
        cpymol.addAtom(x);
        cpymol.addBond(bndx);
        determineSecondary.setSetOfCorrections(null);
        double symm = determineSecondary.determineSymmetry(cpymol,setOfCorrections);
        //setOfCorrections = set;
        return symm;
    }
/*
    private String findConnectionSymmetry(SymmetryAssignment assignment, List<SymmetryPair> pairs) {
        String connectedsymmetry = "N";
        String name = assignment.getGroupName();
        boolean notfound = true;
        Iterator<SymmetryPair> iter = pairs.iterator();
        while (notfound && iter.hasNext()) {
            SymmetryPair pair = iter.next();
            if (pair.getStructureName().equals(name)) {
                connectedsymmetry = pair.getConnectedSymmetry();
                notfound = false;
            }
        }
        return connectedsymmetry;
    }
    */
}
