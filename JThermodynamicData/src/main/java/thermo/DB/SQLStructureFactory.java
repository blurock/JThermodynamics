package thermo.DB;

import thermo.CML.CMLAbstractThermo;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.CML.CMLBensonThermodynamicBase;
import thermo.data.benson.CML.CMLHeatCapacityTemperaturePair;
import thermo.data.benson.DB.SQLBensonThermodynamicBase;
import thermo.data.benson.DB.SQLHeatCapacityTemperaturePair;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.bonds.CML.CMLBondLength;
import thermo.data.structure.bonds.DB.SQLBondLength;
import thermo.data.structure.disassociation.CML.CMLDisassociationEnergy;
import thermo.data.structure.disassociation.DB.SQLDisassociationEnergy;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.CML.CMLDatabaseMolecule;
import thermo.data.structure.structure.CML.CMLMetaAtomInfo;
import thermo.data.structure.structure.CML.CMLSubStructure;
import thermo.data.structure.structure.DB.SQLDatabaseMolecule;
import thermo.data.structure.structure.DB.SQLMetaAtomInfo;
import thermo.data.structure.structure.DB.SQLStructureAsCML;
import thermo.data.structure.structure.symmetry.CML.CMLSymmetryDefinition;
import thermo.data.structure.structure.symmetry.DB.SQLSymmetryDefinition;
import thermo.data.structure.structure.vibrational.CML.CMLVibrationalStructureInfo;
import thermo.data.structure.structure.vibrational.DB.SQLVibrationalStructureInfo;
import thermo.data.structure.substructure.DB.SQLSubStructure;
import thermo.data.tables.DB.SQLCalculationMatrix;

public enum SQLStructureFactory {
	
	benson {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLBensonThermodynamicBase(connect);
		}

		@Override
		public String getClassName() {
			return BensonThermodynamicBase.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			return new CMLBensonThermodynamicBase();
		}
		
	}, structure {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLStructureAsCML(connect);
		}

		@Override
		public String getClassName() {
			return StructureAsCML.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			return new CMLSubStructure();
		}
		
	}, substructure {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLSubStructure(connect);
		}

		@Override
		public String getClassName() {
			return SQLSubStructure.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			return null;
		}
		
	}, symmetry {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLSymmetryDefinition(connect);
		}

		@Override
		public String getClassName() {
			return SQLSymmetryDefinition.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			return new CMLSymmetryDefinition();
		}
		
	}, molecule {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLDatabaseMolecule(connect);
		}

		@Override
		public String getClassName() {
			return SQLDatabaseMolecule.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			return new CMLDatabaseMolecule();
		}
		
	}, metaatom {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLMetaAtomInfo(connect);
		}

		@Override
		public String getClassName() {
			return SQLMetaAtomInfo.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			return new CMLMetaAtomInfo();
		}
		
	}, disassociation {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLDisassociationEnergy(connect);
		}

		@Override
		public String getClassName() {
			return SQLDisassociationEnergy.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			return new CMLDisassociationEnergy();
		}
		
	}, bondlength {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLBondLength(connect);
		}

		@Override
		public String getClassName() {
			return SQLBondLength.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			return new CMLBondLength();
		}
		
	}, heatcapacity {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLHeatCapacityTemperaturePair(connect);
		}

		@Override
		public String getClassName() {
			return SQLHeatCapacityTemperaturePair.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			return new CMLHeatCapacityTemperaturePair();
		}
		
	}, vibrational {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLVibrationalStructureInfo(connect);
		}

		@Override
		public String getClassName() {
			return SQLVibrationalStructureInfo.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			return new CMLVibrationalStructureInfo();
		}
		
	}, matrix {

		@Override
		public SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect) {
			return new SQLCalculationMatrix(connect);
		}

		@Override
		public String getClassName() {
			return SQLCalculationMatrix.class.getCanonicalName();
		}

		@Override
		public CMLAbstractThermo getCMLAbstract() {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
	public abstract SQLStructureThermoAbstractInterface getInterface(ThermoSQLConnection connect);
	public abstract String getClassName();
	public abstract CMLAbstractThermo getCMLAbstract();
}
