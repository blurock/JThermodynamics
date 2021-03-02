package thermo.data.structure.structure.matching;

import org.openscience.cdk.AtomRef;

public class MetaRingAtom extends AtomRef {
	String ringsymbol;
	MetaRingAtom(AtomRef atm) {
		super(atm);
		this.ringsymbol = atm.getSymbol();
	}
	
	@Override
	public void setSymbol(String sym) {
		this.ringsymbol = sym;
	}
	@Override
	public String getSymbol() {
		return this.ringsymbol;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Ring Atom:   ");
		buf.append(super.toString());
		return buf.toString();
	}
}
