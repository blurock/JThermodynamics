package info.esblurock.thermodynamics.base.benson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class BensonThermodynamicBaseElements implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ID;
	private String reference;
	private String thermodynamicType;
    private ArrayList<HeatCapacityTemperaturePair> setOfHeatCapacities;
    private Double standardEnthalpy;
    private Double standardEntropy;
    private double dHtoCalories = 1000.0;

    public BensonThermodynamicBaseElements() {
    	init();
    }
    void init() {
    	reference = "";
    	thermodynamicType = "";
    	setOfHeatCapacities = new ArrayList<HeatCapacityTemperaturePair>();
    	standardEnthalpy = 0.0;
    	standardEntropy = 0.0;
    	dHtoCalories = 1000.0;   	
    }
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getThermodynamicType() {
		return thermodynamicType;
	}
	public void setThermodynamicType(String thermodynamicType) {
		this.thermodynamicType = thermodynamicType;
	}
	public ArrayList<HeatCapacityTemperaturePair> getSetOfHeatCapacities() {
		return setOfHeatCapacities;
	}
	public void setSetOfHeatCapacities(ArrayList<HeatCapacityTemperaturePair> setOfHeatCapacities) {
		this.setOfHeatCapacities = setOfHeatCapacities;
	}
	public Double getStandardEnthalpy() {
		return standardEnthalpy;
	}
	public void setStandardEnthalpy(Double standardEnthalpy) {
		this.standardEnthalpy = standardEnthalpy;
	}
	public Double getStandardEntropy() {
		return standardEntropy;
	}
	public void setStandardEntropy(Double standardEntropy) {
		this.standardEntropy = standardEntropy;
	}
	public double getdHtoCalories() {
		return dHtoCalories;
	}
	public void setdHtoCalories(double dHtoCalories) {
		this.dHtoCalories = dHtoCalories;
	}
	public void add(info.esblurock.thermodynamics.base.benson.HeatCapacityTemperaturePair newpair) {
		setOfHeatCapacities.add(newpair);
	}
	
    public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("BensonThermodynamicBase");
        if (this.getID() != null) {
            buf.append("(" + this.getID() + ")");
        }
        buf.append(":\tH298:");
        buf.append(this.getStandardEnthalpy().toString());
        buf.append("\tS298:");
        buf.append(this.standardEntropy.toString());
        buf.append("\tReference:'");
        buf.append(this.getReference());
        buf.append("'");
        //Iterator<HeatCapacityTemperaturePair> iter = (Iterator<HeatCapacityTemperaturePair>) 
        
        if (setOfHeatCapacities != null) {
            Iterator<HeatCapacityTemperaturePair> iter = setOfHeatCapacities.iterator();
            while (iter.hasNext()) {
                HeatCapacityTemperaturePair pair = iter.next();
                buf.append("\t[");
                buf.append(pair.toString());
                buf.append("]");
            }
        }
        return buf.toString();
    }

	public String printThermodynamicsAsHTMLTable() {
        StringBuilder buf = new StringBuilder();
        buf.append("<tr>");
        buf.append("<td>");
        if (this.getID() == null) {
            buf.append("Correction: ");
        } else {
            buf.append(this.getID());
        }
        buf.append("</td>");
        buf.append("<td>");
        buf.append(this.standardEnthalpy);
        buf.append("</td>");
        buf.append("<td>");
        buf.append(this.standardEntropy);
        buf.append("</td>");
        ArrayList<HeatCapacityTemperaturePair> pairs = this.getSetOfHeatCapacities();
        if (pairs != null) {
            Iterator<HeatCapacityTemperaturePair> p = pairs.iterator();
            buf.append("<td>");
            while (p.hasNext()) {
                HeatCapacityTemperaturePair pair = p.next();
                buf.append(pair.getHeatCapacityValue());
                buf.append(" at ");
                buf.append(pair.getTemperatureValue());
                buf.append(" K ");

                buf.append("<br>");
            }
            buf.append("</td>");
        } else {
            buf.append("<td> ---  </td>");
        }
        buf.append("<td>");
        buf.append(this.getReference());
        buf.append("</td>");
        buf.append("</tr>");
        return buf.toString();

    }

}
