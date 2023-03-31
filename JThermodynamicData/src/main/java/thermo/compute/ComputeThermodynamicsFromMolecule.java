/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compute;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import thermo.data.benson.BensonGroupStructuresFromMolecule;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.SQLSetOfBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonGroupStructures;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.StandardThergasBensonThermoType;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.structure.disassociation.CalculateDisassociationEnergy;
import thermo.data.structure.gauche.ComputeGaucheInteractions;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.AddHydrogenToSingleRadical;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;
import thermo.data.structure.structure.DB.SQLSubstituteBackMetaAtomIntoMolecule;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.CalculateSymmetryCorrection;
import thermo.data.structure.structure.vibrational.CalculateVibrationalCorrectionForRadical;
import thermo.data.structure.substructure.ThermodyanmicsForSubstructures;
import thermo.data.structure.translational.CalculateTranslationalCorrection;
import thermo.data.structure.utilities.MoleculeUtilities;
import thermo.exception.NotARadicalException;
import thermo.exception.ThermodynamicComputeException;
import thermo.exception.ThermodynamicException;
import thermo.properties.SProperties;

/**
 *
 * @author blurock
 */
public class ComputeThermodynamicsFromMolecule {
    String bensonAtomType = "BensonAtom";
    String nancyLinearFormType = "NancyLinearForm";
    ThermoSQLConnection connect;
    SQLMetaAtomDefinitionFromMetaAtomInfo sqlMetaAtom;
    SetOfMetaAtomsForSubstitution metaAtomSubstitutions;
    BensonGroupStructuresFromMolecule bensonGroups;
    SQLSetOfBensonThermodynamicBase sqlthermodynamics;
    SetOfBensonThermodynamicBase bensonBase;
    StandardThergasBensonThermoType standardbenson;
    NancyLinearFormToMolecule nancyFormToMolecule;
    SQLSubstituteBackMetaAtomIntoMolecule substitute;
    CalculateDisassociationEnergy disassociation;
    AddHydrogenToSingleRadical formRH;
    String HBITypesS = "HBI";
    ThermodyanmicsForSubstructures findHBIThermo;
    String cycleTypesS = "Cycles";
    ThermodyanmicsForSubstructures findCyclesThermo;
    String torsionTypesS = "StericCorrections";
    ThermodyanmicsForSubstructures findTorsionsThermo;
    ComputeGaucheInteractions gauche;
    InChIGeneratorFactory inchifactory;

    private double[] temperatures;
    double gasConstant;
    IAtomContainer molecule;

    /**
     * Constructor
     *
     * This is the basic constructor which sets up the computation of thermodynamic
     * quantities.
     *
     * Several exceptions are caught within the code (see code) and are 'translated'
     * to a general 'ThermodyanmicComputeException'.
     *
     * @param c The mySQL connector
     * @throws ThermodynamicComputeException thrown if an error occurs
     */
    public ComputeThermodynamicsFromMolecule(ThermoSQLConnection c) throws ThermodynamicComputeException {
        try {
            connect = c;
            sqlMetaAtom = new SQLMetaAtomDefinitionFromMetaAtomInfo(connect);
            metaAtomSubstitutions = sqlMetaAtom.createSubstitutionSets(bensonAtomType);
            bensonGroups = new BensonGroupStructuresFromMolecule();
            bensonBase = new SetOfBensonThermodynamicBase();
            sqlthermodynamics = new SQLSetOfBensonThermodynamicBase(bensonBase, connect);
            standardbenson = new StandardThergasBensonThermoType();
            nancyFormToMolecule = new NancyLinearFormToMolecule(c);
            inchifactory = null;
            substitute = new SQLSubstituteBackMetaAtomIntoMolecule(nancyLinearFormType, connect);
            disassociation = new CalculateDisassociationEnergy(connect);
            temperatures = standardbenson.getTemperaturesAsDoubleValues();
            String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
            gasConstant = Double.valueOf(gasconstantS).doubleValue();
            formRH = new AddHydrogenToSingleRadical();
            findHBIThermo = new ThermodyanmicsForSubstructures(HBITypesS, connect);
            findCyclesThermo = new ThermodyanmicsForSubstructures(cycleTypesS, connect);
            findTorsionsThermo = new ThermodyanmicsForSubstructures(torsionTypesS, connect);
            gauche = new ComputeGaucheInteractions(connect);

        } catch (SQLException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in setting up database: " + ex.toString());
        } catch (CDKException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in setting up database: " + ex.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in setting up database: " + ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in setting up database: " + ex.toString());
        }
    }

    /**
     * Compute the thermodynamics from the CDK molecule.
     *
     * The Benson rules and corrections are added to {@link thermodynamics) and the
     * combined value is returned.
     *
     * This routine checks if the molecule is a radical or a stable species. If the
     * molecule is a radical, then the
     * {@link computeThermodynamicsForRadicalContributions} is called to get the
     * radical corrections. If the molecule is a stable species, then
     * {@link computeThermodynamicsForMolecule} is called.
     *
     * The list of corrections from either routine (added to the thermodynamics
     * structure) is then combined into one value and this value is returned and
     * printed.
     * 
     * The only difference when using Benson radical rules is a check is not made
     * for a radical (which would add a hydrogen and compute with radical). The
     * computation for a molecule or radical is the same, only with a radical, the
     * rules for radicals have to be included (otherwise no contribution for radical
     * atoms).
     *
     * @param molecule        The molecule in CDK Molecule form
     * @param thermodynamics  The Benson rules and corrections are added
     *                        individually to this structure.
     * @param fromBensonRules if true, compute using Benson radical rules
     * @return The full thermodynamics (all Benson rules and corrections combined)
     * @throws ThermodynamicComputeException
     */
    public ThermodynamicInformation computeThermodynamics(IAtomContainer moleculetocompute,
            SetOfBensonThermodynamicBase thermodynamics, String method) throws ThermodynamicComputeException {
        BensonThermodynamicBase combinedThermodynamics = null;
        MoleculeUtilities.setImplicitHydrogensToZero(moleculetocompute);

        try {
            if (method.equals(SProperties.getProperty("thermo.parameter.bensonradicalkey"))) {
                computeThermodynamicsForMolecule(moleculetocompute, thermodynamics);
            } else if (method.equals(SProperties.getProperty("thermo.parameter.thergaskey"))) {
                if (formRH.isARadical(moleculetocompute)) {
                    computeThermodynamicsForRadicalContributions(moleculetocompute, thermodynamics);
                } else {
                    computeThermodynamicsForMolecule(moleculetocompute, thermodynamics);
                }
            } else if (method.equals(SProperties.getProperty("thermo.parameter.thermkey"))) {
                computeTHERMThermodynamicsForMolecule(moleculetocompute, thermodynamics);
            } else {
                System.out.println("Method not found: " + method);
            }
            combinedThermodynamics = thermodynamics.combineToOneBensonRule(temperatures);
            combinedThermodynamics.setID("Total");
            combinedThermodynamics.setReference("Sum Total");
            /*
             * System.out.println(combinedThermodynamics.toString());
             * System.out.println("========================================================"
             * );
             */
        } catch (ThermodynamicException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in calculating thermodyanmics: " + ex.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in calculating thermodyanmics: " + ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in calculating thermodyanmics: " + ex.toString());
        } catch (SQLException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return combinedThermodynamics;
    }

    /**
     * This computes the thermodynamics for a CDK stable molecule.
     *
     * @param mol    The molecule in CDK Molecule form
     * @param thermo The Benson rules and corrections are added individually to this
     *               structure.
     * @throws ThermodynamicException
     * @throws CDKException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws SQLException
     * @throws
     */
    public void computeThermodynamicsForMolecule(IAtomContainer mol, SetOfBensonThermodynamicBase thermo)
            throws ThermodynamicException, CDKException, ClassNotFoundException, IOException, SQLException {
        IAtomContainer molorig = null;
        try {
            molorig = mol.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        IAtomContainer substituted = metaAtomSubstitutions.substitute(mol);
        SetOfBensonGroupStructures bensonset = bensonGroups.deriveBensonGroupStructures(substituted);
        sqlthermodynamics.setUpFromSetOfBensonGroupStructures(bensonset, thermo);
        CalculateSymmetryCorrection symmetryCorrections = new CalculateSymmetryCorrection(connect);

        symmetryCorrections.calculate(molorig, thermo);
        BensonThermodynamicBase cycle = (BensonThermodynamicBase) findCyclesThermo
                .FindLargestStructureThermodynamics(molorig);
        if (cycle != null)
            thermo.add(cycle);

        gauche.compute(molorig, thermo);
    }

    public void computeThermodynamicsFromBensonRadicalRules(IAtomContainer moleculetocompute,
            SetOfBensonThermodynamicBase thermodynamics) {
        // TODO Auto-generated method stub

    }

    public void computeTHERMThermodynamicsForMolecule(IAtomContainer R, SetOfBensonThermodynamicBase thermo)
            throws NotARadicalException, ThermodynamicException, SQLException, CDKException, IOException,
            ClassNotFoundException {
        IAtomContainer RH = formRH.convert(R);
        MoleculeUtilities.normalizeMolecule(RH);
        computeThermodynamicsForMolecule(RH, thermo);

        BensonThermodynamicBase hbi = (BensonThermodynamicBase) findHBIThermo.FindLargestStructureThermodynamics(R);
        if (hbi != null) {
            System.out.println(hbi.toString());
            thermo.add(hbi);
        } else {
            BensonThermodynamicBase nohbi = new BensonThermodynamicBase("HBI Correction not found", null, 0.0, 0.0);
            nohbi.setID("HBI Correction not found");
            nohbi.setReference("No Radical Correction, just disassociation and spin");
            System.out.println(nohbi.toString());
            thermo.add(nohbi);
            disassociation.calculate(R, thermo);
        }
        BensonThermodynamicBase spinthermo = computeSpinContribution();
        thermo.add(spinthermo);
        System.out.println(spinthermo.toString());

        BensonThermodynamicBase hradical = computeHydrogenRadicalContribution();
        thermo.add(hradical);
        System.out.println(hradical.toString());
    }

    /**
     * Compute thermodynamic corrections for radical
     *
     * For both the radical and the stable species (radical with a hydrogen added:
     * {@link AddHydrogenToSingleRadical} compute:
     * <ul>
     * <li>Benson Groups
     * <li>Symmetry
     * <li>Disassociation energy
     * <li>Spin contribution
     * <li>Hydrogen radical contribution
     * <li>Vibrational structures
     * </ul>
     *
     * The radical species corrections are subtracted from the stable species
     * values.
     *
     * @param R      The radical in CDK Molecule form
     * @param thermo The Benson rules and corrections are added individually to this
     *               structure.
     * @throws NotARadicalException   routine called for a stable species
     * @throws ThermodynamicException
     * @throws SQLException           Usually means no SQL connection
     * @throws CDKException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void computeThermodynamicsForRadicalContributions(IAtomContainer R, SetOfBensonThermodynamicBase thermo)
            throws NotARadicalException, ThermodynamicException, SQLException, CDKException, IOException,
            ClassNotFoundException {

        // SetOfBensonThermodynamicBase thermominus = new
        // SetOfBensonThermodynamicBase();
        IAtomContainer RH = formRH.convert(R);
        MoleculeUtilities.normalizeMolecule(RH);

        StructureAsCML struct = new StructureAsCML(RH);
        IAtomContainer substituted = metaAtomSubstitutions.substitute(struct);
        SetOfBensonGroupStructures bensonset = bensonGroups.deriveBensonGroupStructures(substituted);

        BensonThermodynamicBase cycle = (BensonThermodynamicBase) findCyclesThermo
                .FindLargestStructureThermodynamics(RH);
        if (cycle != null)
            thermo.add(cycle);

        gauche.compute(RH, thermo);
        sqlthermodynamics.setUpFromSetOfBensonGroupStructures(bensonset, thermo);

        disassociation.calculate(R, thermo);

        BensonThermodynamicBase translation = CalculateTranslationalCorrection.translationalEnergy(RH);
        thermo.add(translation);

        BensonThermodynamicBase spinthermo = computeSpinContribution();
        thermo.add(spinthermo);

        BensonThermodynamicBase hradical = computeHydrogenRadicalContribution();
        thermo.add(hradical);

        CalculateVibrationalCorrectionForRadical vibrational = new CalculateVibrationalCorrectionForRadical(connect);
        vibrational.calculate(R, RH, thermo);

        // System.out.println("Radical Symmetry
        // Calculations============================================");
        CalculateSymmetryCorrection symmetryCorrectionsR = new CalculateSymmetryCorrection(connect);
        symmetryCorrectionsR.calculate(R, thermo);

        /*
         * The calculation reduces to -Rln(Sym(R.))... so this part not needed
         * System.out.
         * println("Molecule Symmetry Calculations============================================"
         * ); CalculateSymmetryCorrection symmetryCorrectionsRH = new
         * CalculateSymmetryCorrection(connect); symmetryCorrectionsRH.calculate(RH,
         * thermominus); System.out.
         * println("Done     Symmetry Calculations============================================"
         * ); thermominus.Minus(); thermo.add(thermominus);
         * 
         */
    }

    BensonThermodynamicBase computeSpinContribution() {
        double spin = gasConstant * Math.log1p(1.0);
        // Double ln2 = Math.log1p(1.0);
        Double spinD = Double.valueOf(spin);
        String spinS = "Spin Contribution: Rln(2) = (" + gasConstant + ")*(" + Math.log1p(1.0) + ")";
        BensonThermodynamicBase spinthermo = new BensonThermodynamicBase(spinS, null, 0.0, spinD);
        spinthermo.setID(spinS);
        spinthermo.setReference("Radical Correction");
        return spinthermo;
    }

    IAtomContainer getMolecule() {
        return molecule;
    }

    private BensonThermodynamicBase computeHydrogenRadicalContribution() {
        double hrad = -52.1;
        Double hradD = Double.valueOf(hrad);
        String hradS = "Hydrogen Radical";
        BensonThermodynamicBase hradical = new BensonThermodynamicBase(hradS, null, hradD, 0.0);
        hradical.setID(hradS);
        hradical.setReference("Radical Correction");
        return hradical;

    }

    public BensonThermodynamicBase combineThermodynamics(SetOfBensonThermodynamicBase thermodynamics)
            throws ThermodynamicComputeException {
        return thermodynamics.combineToOneBensonRule(temperatures);
    }
}
