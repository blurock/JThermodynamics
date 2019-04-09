package thermo.data.structure.bonds.CML;

import java.util.List;

import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLScalar;

import thermo.CML.CMLAbstractThermo;
import thermo.data.structure.bonds.BondLength;

public class CMLBondLength extends CMLAbstractThermo {

	@Override
	public void toCML() {
		BondLength length = (BondLength) getStructure();
        this.setId("BondLength");
        
        CMLScalar atom1S = new CMLScalar();
        atom1S.setId("atom1");
        atom1S.setValue(length.getAtom1());
        this.appendChild(atom1S);
        
        CMLScalar atom2S = new CMLScalar();
        atom2S.setId("atom2");
        atom2S.setValue(length.getAtom2());
        this.appendChild(atom2S);
        
        CMLScalar bondOrderS = new CMLScalar();
        bondOrderS.setId("bondorder");
        bondOrderS.setValue(length.getBondOrder());
        this.appendChild(bondOrderS);
        
        CMLScalar bondLengthS = new CMLScalar();
        bondLengthS.setId("bondlength");
        bondLengthS.setValue(length.getBondLength());
        this.appendChild(bondLengthS);
        
        CMLScalar sourceS = new CMLScalar();
        sourceS.setId("source");
        sourceS.setValue(length.getSource());
        this.appendChild(sourceS);
        
        
	}

	@Override
	public void fromCML() {
        List<CMLElement> proplist = this.getChildCMLElements();
        if (proplist.size() == 5) {
            CMLScalar atom1cml = (CMLScalar) proplist.get(0);
            String atom1 = atom1cml.getString();
            
            CMLScalar atom2cml = (CMLScalar) proplist.get(1);
            String atom2 = atom2cml.getString();
            
            CMLScalar bondordercml = (CMLScalar) proplist.get(2);
            int bondorder = bondordercml.getInt();
            
            CMLScalar bondlengthcml = (CMLScalar) proplist.get(3);
            double bondlength = bondlengthcml.getDouble();
            
            CMLScalar sourcecml = (CMLScalar) proplist.get(4);
            String source = sourcecml.getString();
            
            BondLength bond = new BondLength(atom1,atom2,bondorder,bondlength,source);
            this.setStructure(bond);
        }
	}

}
