/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compare;

import jThergas.data.read.ReadFileToString;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import thermo.compute.CompareThermodynamicInformationSets;
import thermo.compute.ComputeThermodynamicsFromMolecule;
import thermo.compute.SetOfThermodynamicDifferences;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.NASAPolynomial;
import thermo.data.benson.SetOfThermodynamicInformation;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;

/**
 *
 * @author edwardblurock
 */
public class CompareJThergasWithExGasThermo {

    String inputString;
    private double defaultLowerT = 300.0;
    private double defaultUpperT = 3000.0;
    private String defaultPhase = "G";

    private ThermoSQLConnection connect;
    private ComputeThermodynamicsFromMolecule computeThermo;
    private CompareThermodynamicInformationSets compareThermo;

    SetOfThermodynamicInformation exgasThermValues;
    SetOfThermodynamicInformation jthergasValues;

    private void initialize() throws ThermodynamicComputeException {
        connect = new ThermoSQLConnection();
        connect.connect();
        computeThermo = new ComputeThermodynamicsFromMolecule(connect);
        compareThermo = new CompareThermodynamicInformationSets();
    }
    public CompareJThergasWithExGasThermo(String filename, boolean frombensonradical) throws ThermodynamicComputeException {
        initialize();
        File fileF = new File(filename);
        read(fileF,frombensonradical);
    }
    public CompareJThergasWithExGasThermo(File fileF, boolean frombensonradical) throws ThermodynamicComputeException {
         initialize();
         read(fileF, frombensonradical);
    }
    private void read(File fileF, boolean frombensonradical) {
        ReadFileToString read = new ReadFileToString();
        read.read(fileF);
        inputString = read.outputString;
    }
    public void compare(boolean frombensonradical) throws ThermodynamicComputeException {
        jthergasValues = new SetOfThermodynamicInformation("JThergas");
        exgasThermValues = new SetOfThermodynamicInformation("ExGas");
        StringBuilder errorbuf = new StringBuilder();
        StringTokenizer tok = new StringTokenizer(inputString,"\n");
        while(tok.hasMoreTokens()) {
            String line = tok.nextToken();
            try {
                build(line,frombensonradical);
            } catch (IOException ex) {
                errorbuf.append(ex.toString());
                errorbuf.append("\n");
            }
        }
        SetOfThermodynamicDifferences diff = compareThermo.computeDifference(jthergasValues, exgasThermValues);
        List difflist = diff;
        Collections.sort( difflist);
        System.out.println(difflist.toString());
        //diff.toStringInTable();
    }
    @SuppressWarnings("unused")
	private void printDifferenceAsTable(SetOfThermodynamicDifferences diff) {
    }
    
    public void build(String line,boolean frombensonradical) throws IOException, ThermodynamicComputeException {
        StringTokenizer tok = new StringTokenizer(line);
        if(tok.countTokens() >= 16) {
            String nancystring = tok.nextToken();
            String name = tok.nextToken();
            NASAPolynomial exgasNASA = setUpNASAPolynomial(name, tok);
            System.out.println(exgasNASA.toString());
            ThermodynamicInformation jthergasNASA = setUpJThergasNASAPolynomial(nancystring,name,frombensonradical);

            double jthergasEnthalpy = jthergasNASA.getStandardEnthalpy298();
            double exgasEnthalpy = exgasNASA.getStandardEnthalpy298();
            System.out.println("CompareJThergasWithExGasThermo: "
                    + jthergasEnthalpy
                    + "\t"
                    + exgasEnthalpy);
            jthergasNASA.setName(exgasNASA.getName());
            jthergasValues.add(jthergasNASA);
            exgasThermValues.add(exgasNASA);
        } else {
            throw new IOException("ERROR in parsing line (not enough tokens" + line);
        }
    }

    private double[] readSevenCoefficients(StringTokenizer tok) {
        double[] coeffs = new double[7];
        for(int i=0;i<7;i++) {
            String cS = tok.nextToken();
            Double cD = Double.valueOf(cS);
            coeffs[i] = cD.doubleValue();
        }
        return coeffs;
    }

    private NASAPolynomial setUpNASAPolynomial(String name, StringTokenizer tok) {
            double lower[]  = readSevenCoefficients(tok);
            double upper[] = readSevenCoefficients(tok);
            NASAPolynomial nasa = new NASAPolynomial();
            nasa.setThermodynamicType("EXGAS");
            nasa.lower = lower;
            nasa.upper = upper;
            nasa.lowerT = defaultLowerT;
            nasa.upperT = defaultUpperT;
            nasa.name = name;
            nasa.phase = defaultPhase;
            return nasa;
    }

    private ThermodynamicInformation setUpJThergasNASAPolynomial(String nancystring, 
    		String name,
    		boolean frombensonradical) throws ThermodynamicComputeException {
        ThermodynamicInformation thermo = computeThermo.computeThermodynamics(nancystring,frombensonradical);
        return thermo;
    }
}
