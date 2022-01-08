/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry;

import java.util.ArrayList;
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
	
	boolean debug = true;
	
    private int highestSymmetry = 12;
    String dontCheck = "X";
    String noCheckS = "N";
    String linearCheckS = "L";
    SetOfBensonThermodynamicBase setOfCorrections;
    double gasConstant;
    String referenceS = "External Symmetry Correction";
    private final DetermineExternalSymmetry determineSecondary;
    SymmetryDefinition defintion;
    
    SymmetryMatch symmetryMatch;

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
        symmetryMatch = null;
    }

    @Override
    public void initializeSymmetry() {
        symmetryValue = 1;
    }

    
	public SymmetryMatch getSymmetryMatch() {
		return symmetryMatch;
	}

	public void setSymmetryMatch(SymmetryMatch symmetryMatch) {
		this.symmetryMatch = symmetryMatch;
	}
	
	public SymmetryDefinition getMatchedSymmetry() {
		return defintion;
	}

	@Override
    public int determineSymmetry(IAtomContainer structure, SetOfBensonThermodynamicBase corrections) throws CDKException {
    	setOfCorrections = new SetOfBensonThermodynamicBase();
        initializeSymmetry();
        if(debug) {
        System.out.println("DetermineExternalSymmetry.determineSymmetry");
        System.out.println(MoleculeUtilities.toString(structure));
        }
        //double topsymmetry = 1.0;
        boolean notdone = true;
        int currentSymmetry = highestSymmetry;
        while (currentSymmetry > 0 && notdone) {
            Iterator<SymmetryDefinition> idef = symmetryDefinitions.iterator();
            while (idef.hasNext() && notdone) {
                defintion = idef.next();
                int defsymmetry = defintion.getInternalSymmetryFactor().intValue();
                if (defsymmetry == currentSymmetry) {
                    	if(debug) {
                    		System.out.println("Determine Symmetry: def=" + defintion.getMetaAtomName());
                    	}
                        int symmetry = determineSymmetry.determineSymmetry(defintion, structure);
                        if(debug) {
                        	System.out.println("Determine Symmetry: ans= " + symmetry);
                        }
                        combineInSymmetryNumber(symmetry);
                        notdone = symmetryMatch == null;
                        if(debug) {
                        if(notdone) {
                        	System.out.println("determineSymmetry Symmetry match NOT found");                        	
                        } else {
                        	System.out.println("determineSymmetry Symmetry match found");
                        }
                        }
                }
            }
            currentSymmetry--;
        }
        
        if(symmetryMatch != null) {
            double symmD = this.determineSymmetry.symmetryDefinition.getInternalSymmetryFactor();
            double correction = -gasConstant * Math.log(symmD);
            String symname = this.determineSymmetry.symmetryDefinition.getMetaAtomName();
            BensonThermodynamicBase benson = new BensonThermodynamicBase(referenceS, null, 0.0, correction);
            symname = symname + " (" + symmD + ")";
            benson.setID(symname);
            benson.setReference("Symmetry");
            corrections.add(benson);
            symmetryValue = (int) symmD;
        }
        if(debug) {
        	System.out.println("setOfCorrections: \n" + setOfCorrections.toString());
        	System.out.println("corrections: \n" + corrections.toString());
        }
        
        return getSymmetryValue();
    }
 /*  combineInSymmetryNumber
  * 
  * The purpose of combineInSymmetryNumber is to see whether the secondary conditions hold.
  * The SymmetryMatch element that satisfy secondary conditions are put in a list.
  * 
  * For external symmetry, only one of the matches, including secondary conditions, should hold
  * If so, then finalmatch element is set to the SymmetryMatch that holds.
  * 
  * A non-null finalmatch means that a SymmetryMatch has been found.
  *
 *
 */
    public void combineInSymmetryNumber(int symmetry) {
    	ArrayList<SymmetryMatch>  matchlist = new ArrayList<SymmetryMatch>();
    	if(debug) {
    		System.out.println("DetermineExternalSymmetry: combineInSymmetryNumber: " + symmetry);
    		System.out.println("symmetry matches: " + determineSymmetry.getSymmetryMatches().size());
    	}
        Iterator<SymmetryMatch> iter = determineSymmetry.getSymmetryMatches().iterator();
        while (iter.hasNext()) {
            SymmetryMatch match = iter.next();
            
            if(debug) {
            	System.out.println("Find Symmetry of " + match.toString());
            }
            try {
                if(findSymmetryContribution(match))  {
                	matchlist.add(match);
                }
                
           } catch (CDKException ex) {
                Logger.getLogger(DetermineExternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        if(matchlist.size() == 1) {
        	symmetryMatch = matchlist.get(0);
        	if(debug) {
        		System.out.println("Symmetry Match found: \n" + symmetryMatch.toString());
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
    private boolean findSymmetryContribution(SymmetryMatch match) throws CDKException {
    	if(debug) {
    		System.out.println("findSymmetryContribution");
    	}
        SetOfSymmetryAssignments assignments = match.getFromMolecule();
        Iterator<String> iter = assignments.keySet().iterator();
        boolean notdone = true;
        while (iter.hasNext() && notdone) {
            SymmetryAssignment assignment = assignments.get(iter.next());
            String connectionSymmetry = assignment.getSymmetryConnection();
            IAtomContainer mol = assignment.getStructure();
            if (connectionSymmetry.contains(linearCheckS)) {
                boolean isLinear = isMoleculeLinear(mol);
                if (isLinear) {
                	if(debug) {
                		System.out.println("\t\tSymmetry of Connection: " + assignment.getGroupName() + " is linear");
                	}
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

        return notdone;
    }

    private boolean isMoleculeLinear(IAtomContainer mol) {
        boolean ans = false;
        if (mol.getAtomCount() == 1) {
            ans = true;
        }
        return ans;
    }

    private double findSymmetryOfConnection(IAtomContainer mol, String connection) throws CDKException {
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
        if(debug) {
        	System.out.println("findSymmetryOfConnection: determineSecondary.determineSymmetry");
        }
        double symm = determineSecondary.determineSymmetry(cpymol,setOfCorrections);
        if(debug) {
        	System.out.println("Secondary Symmetry=" + symm);
        }
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
