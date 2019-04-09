package thermo.data.structure.structure.CML;

import java.util.List;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLScalar;

import thermo.CML.CMLAbstractThermo;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.substructure.SubStructure;

public class CMLSubStructure extends CMLAbstractThermo  {

	@Override
	public void toCML() {
		SubStructure info = (SubStructure) getStructure();
	    this.setId("SubStructure");

	        CMLScalar sourceOfStructureS = new CMLScalar();
	        sourceOfStructureS.setId("sourceOfStructure");
	        sourceOfStructureS.setValue(info.getSourceOfStructure());
	        this.appendChild(sourceOfStructureS);
	        
	        
	        IAtomContainer substructure = info.getSubstructure();
	        StructureAsCML cmlstructure;
			try {
				cmlstructure = new StructureAsCML(substructure);
		        CMLScalar substructureS = new CMLScalar();
		        substructureS.setId("CMLStructure");
		        substructureS.setValue(cmlstructure.getCmlStructureString());
		        this.appendChild(substructureS);
			} catch (CDKException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void fromCML() {
		List<CMLElement> proplist = this.getChildCMLElements();
		if(proplist.size() == 2) {
			CMLScalar source = (CMLScalar) proplist.get(0);
			String sourceS = source.getString();
        
			CMLScalar mol = (CMLScalar) proplist.get(1);
			String molS = mol.getString();
			
			StructureAsCML cmlstructure = new StructureAsCML("", molS);
			IAtomContainer atomcontainer;
			try {
				atomcontainer = cmlstructure.getMolecule();
				SubStructure substructure = new SubStructure(atomcontainer, sourceS);
				this.setStructure(substructure);
			} catch (CDKException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}

}
