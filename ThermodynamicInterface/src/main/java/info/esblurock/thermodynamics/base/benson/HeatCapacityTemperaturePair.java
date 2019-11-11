/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package info.esblurock.thermodynamics.base.benson;

import java.io.Serializable;

/** This corresponds exactly to the SQL database element
 *
 * @author blurock
 */
public class HeatCapacityTemperaturePair implements Comparable<HeatCapacityTemperaturePair>, Serializable {
	private static final long serialVersionUID = 1L;
	private String structureName;
    private double temperatureValue;
    private double heatCapacityValue;
    private String reference;
    
    /** Empty Constructor
     *
     */
    public HeatCapacityTemperaturePair() {
    	structureName = "";
        temperatureValue = 1000.0;
        heatCapacityValue = 0.0;
        reference = "";
    }
    /** Constructor with elements
     *
     * @param name The name of the pair
     * @param t    The temperature
     * @param cp   The assigned heat compacity
     */
    public HeatCapacityTemperaturePair(String name, double t, double cp) {
        structureName = name;
        temperatureValue = t;
        heatCapacityValue = cp;
        reference = "";
    }

    /**
     *
     * @return
     */
    public String getStructureName() {
        return structureName;
    }

    /**
     *
     * @param structureName
     */
    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    /**
     *
     * @return
     */
    public double getTemperatureValue() {
        return temperatureValue;
    }

    /**
     *
     * @param temperatureValue
     */
    public void setTemperatureValue(double temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    /**
     *
     * @return
     */
    public double getHeatCapacityValue() {
        return heatCapacityValue;
    }

    /**
     *
     * @param heatCapacityValue
     */
    public void setHeatCapacityValue(double heatCapacityValue) {
        this.heatCapacityValue = heatCapacityValue;
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return reference;
    }

    /**
     *
     * @param reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getTemperatureValue());
        buf.append(", ");
        buf.append(this.getHeatCapacityValue());
        
        return buf.toString();
    }
 
    /**
     *
     * @param structureName
     * @param temperatureValue
     * @param heatCapacityValue
     * @param reference
     */
    public HeatCapacityTemperaturePair(String structureName, double temperatureValue, double heatCapacityValue, String reference) {
        this.structureName = structureName;
        this.temperatureValue = temperatureValue;
        this.heatCapacityValue = heatCapacityValue;
        this.reference = reference;
    }

    /**
     *
     * @param pairnew
     */
    public void addInto(HeatCapacityTemperaturePair pairnew) {
        this.heatCapacityValue += pairnew.heatCapacityValue;
    }

    public int doCompare(HeatCapacityTemperaturePair pair1, HeatCapacityTemperaturePair pair2) {
        //HeatCapacityTemperaturePair pair1 = (HeatCapacityTemperaturePair) arg0;
        //HeatCapacityTemperaturePair pair2 = (HeatCapacityTemperaturePair) arg1;
        int ans = 0;
        if(pair1.temperatureValue > pair2.temperatureValue) {
            ans = 1;
        } if(pair1.temperatureValue < pair2.temperatureValue) {
            ans = -1;
        }
        return ans;
    }

    public int compareTo(HeatCapacityTemperaturePair o) {
       return doCompare(this,o);
    }
}
