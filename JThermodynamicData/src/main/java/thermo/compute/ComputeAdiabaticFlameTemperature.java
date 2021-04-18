/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compute;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import org.openscience.cdk.interfaces.IAtomContainer;
import thermo.data.benson.DB.SQLBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.structure.structure.HydrocarbonCompleteCombustion;
import thermo.exception.ThermodynamicComputeException;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;

/**
 *
 * @author edwardblurock
 */
public class ComputeAdiabaticFlameTemperature {
    ThermodynamicInformation fuelThermo;
    ThermodynamicInformation o2Thermo;
    ThermodynamicInformation h2oThermo;
    ThermodynamicInformation co2Thermo;
    ThermodynamicInformation n2Thermo;


    ThermoSQLConnection connect;
    HydrocarbonCompleteCombustion fuelAtoms;
    SQLBensonThermodynamicBase sqlthermo;

    double initialTemperatureIncrement = 20.0;
    double smallestTemperatureIncrement = 0.1;
    String carbondioxideS = "carbon dioxide";
    String oxygenS = "oxygen";
    String waterS = "water";

    public ComputeAdiabaticFlameTemperature(ThermoSQLConnection c) throws SQLException {
        connect = c;
        sqlthermo = new SQLBensonThermodynamicBase(connect);
        initializeThermodynamics();
    }

    public void initializeThermodynamics() throws SQLException{
        HashSet<BensonThermodynamicBase> co2V = sqlthermo.retrieveStructuresFromDatabase(carbondioxideS);
        Iterator<BensonThermodynamicBase> iter = co2V.iterator();
        co2Thermo = iter.next();
        HashSet<BensonThermodynamicBase> o2V = sqlthermo.retrieveStructuresFromDatabase(oxygenS);
        iter = o2V.iterator();
        o2Thermo = iter.next();
        HashSet<BensonThermodynamicBase> h2oV = sqlthermo.retrieveStructuresFromDatabase(waterS);
        iter = h2oV.iterator();
        h2oThermo = iter.next();

    }
    public void setFuel(IAtomContainer atoms, String method) throws ThermodynamicComputeException{
        fuelAtoms = new HydrocarbonCompleteCombustion(atoms);
        fuelAtoms.findCompleteCombustionInOxygen();
        initializeThermodynamicsForFuel(atoms,method);
    }
    public double computeFlameTemperatureOxygen(IAtomContainer atoms,double beginT, String method) throws ThermodynamicComputeException {
        setFuel(atoms,method);
        return computeFlameTemperatureOxygen(beginT);
    }
    public void initializeThermodynamicsForFuel(IAtomContainer atoms, String method) throws ThermodynamicComputeException {
        ComputeThermodynamicsFromMolecule thermo = new ComputeThermodynamicsFromMolecule(connect);
        SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
        fuelThermo = thermo.computeThermodynamics(atoms, thermodynamics, method);
    }
    public double computeFlameTemperatureOxygen(double beginT) throws ThermodynamicComputeException {
        double temperature = beginT;
        System.out.println("Fuel + " +
                fuelAtoms.getOxidizer() + " O2 --> " +
                fuelAtoms.getNumberCO2() + " CO2 + " + fuelAtoms.getNumberH2O() + " H2O");
        double totalFuelEnthalpy = computeTotalEnthalpyFuelInOxygen(temperature);
        double totalProductEnthalpy = computeTotalEnthalpyProducts(temperature);
        while(totalFuelEnthalpy > totalProductEnthalpy) {
            temperature += initialTemperatureIncrement;
            totalProductEnthalpy = computeTotalEnthalpyProducts(temperature);
        }
        return temperature - initialTemperatureIncrement;
    }
    public double computeTotalEnthalpyFuelInOxygen(double t) throws ThermodynamicComputeException {
        double fuelEnthalpy = fuelThermo.computeEnthalpy(t);
        double o2Enthalpy = o2Thermo.computeEnthalpy(t);
        double total = fuelEnthalpy + fuelAtoms.getOxidizer()*o2Enthalpy;
        System.out.println("t,Enthalpy: " + t + "\t" + fuelEnthalpy + "\t (" + fuelAtoms.getOxidizer() + ") * " + o2Enthalpy);
        return total;
    }
    public double computeTotalEnthalpyProducts(double t) throws ThermodynamicComputeException {
        double co2Base     = co2Thermo.computeEnthalpy(300.0);
        double h2oBase     = h2oThermo.computeEnthalpy(300.0);
        double co2Enthalpy = co2Thermo.computeEnthalpy(t);
        double h2oEnthalpy = h2oThermo.computeEnthalpy(t);
        double total = co2Enthalpy * fuelAtoms.getNumberCO2() + h2oEnthalpy*fuelAtoms.getNumberH2O();
        System.out.println("T,Enthalpy: " + t + "\t" + total 
                + "\t h20,co2: "
                + (h2oEnthalpy - h2oBase) * 4.18 + "\t "
                + (co2Enthalpy - co2Base) * 4.18 + "\t"
                + h2oBase*4.18 + "\t" 
                + co2Base*4.18 + "\t"
                + (co2Enthalpy * fuelAtoms.getNumberCO2()) + "\t"
                + (h2oEnthalpy * fuelAtoms.getNumberH2O()) );
        return total;
    }


}
