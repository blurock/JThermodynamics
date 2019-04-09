/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.matching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.matchers.IQueryAtomContainer;
import org.openscience.cdk.isomorphism.mcss.RMap;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;

/**
 *
 * @author blurock
 */
public class GetSubstructureMatches {
    String matchAnyTypePrefix = "R";
    /**
     * 
     */
    public GetSubstructureMatches() {
    }

    /**
     * 
     * @param mol1
     * @param mol2
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     */
    public boolean equals(IAtomContainer m1, IAtomContainer m2) throws CDKException {
        QueryMoleculeWithMetaAtoms mol1 = new QueryMoleculeWithMetaAtoms((IAtomContainer) m1);
        QueryMoleculeWithMetaAtoms mol2 = new QueryMoleculeWithMetaAtoms((IAtomContainer) m2);
        boolean ans = mol1.getAtomCount() == mol2.getAtomCount();
        if (ans) {
            if (mol1.getAtomCount() == 1) {
                IAtom atm1 = mol1.getAtom(0);
                IAtom atm2 = mol2.getAtom(0);
                String type1 = atm1.getSymbol();
                String type2 = atm2.getSymbol();
                if(!type1.equals(type2)) {
                    if(type1.startsWith(matchAnyTypePrefix) || type2.startsWith(matchAnyTypePrefix))
                        ans = true;
                    else 
                        ans = false;
                } else {
                    ans = true;
                }
            } else {
                UniversalIsomorphismTester tester = new UniversalIsomorphismTester();
                ans = tester.isIsomorph(mol1, mol2);
            }
        }
        return ans;
    }

    /**
     * 
     * @param mol1
     * @param substructure
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     */
    public List<List<RMap>> getBondMatches(IAtomContainer mol1, QueryMoleculeWithMetaAtoms substructure) throws CDKException {
        QueryMoleculeWithMetaAtoms qmol1 = new QueryMoleculeWithMetaAtoms(mol1);
        UniversalIsomorphismTester tester = new UniversalIsomorphismTester();
        return tester.getSubgraphMaps(qmol1, (IAtomContainer) substructure);
    }

    /**
     * 
     * @param mol1
     * @param substructure
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     */
    public List<List<RMap>> getAtomMatches(IAtomContainer mol1, QueryMoleculeWithMetaAtoms substructure) throws CDKException {
        QueryMoleculeWithMetaAtoms qmol1 = new QueryMoleculeWithMetaAtoms(mol1);
        List<List<RMap>> map = null;
        if(substructure.getBondCount() == 1) {
            map = singleBondSubstructureCase(qmol1, substructure);
        } else {
            //map = getAtomMatches(qmol1, substructure);
            UniversalIsomorphismTester tester = new UniversalIsomorphismTester();
            map = tester.getSubgraphAtomsMaps(qmol1, (IAtomContainer) substructure);
        }
        return map;
    }

    /**
     * 
     * @param mol1
     * @param mol2
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     */
    public List<List<RMap>> getBondMatches(IAtomContainer mol1, IAtomContainer mol2) throws CDKException {
        //QueryMoleculeWithMetaAtoms qmol1 = new QueryMoleculeWithMetaAtoms(mol1);
        QueryMoleculeWithMetaAtoms qmol2 = new QueryMoleculeWithMetaAtoms(mol2);
        return getBondMatches(mol1,qmol2);
    }

    /**
     * 
     * @param mol1
     * @param mol2
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     */
    public List<List<RMap>> getAtomMatches(IAtomContainer mol1, IAtomContainer mol2) throws CDKException {
        QueryMoleculeWithMetaAtoms qmol1 = new QueryMoleculeWithMetaAtoms(mol1);
        QueryMoleculeWithMetaAtoms qmol2 = new QueryMoleculeWithMetaAtoms(mol2);
        UniversalIsomorphismTester tester = new UniversalIsomorphismTester();
        List<List<RMap>> map = null;
        if(qmol2.getBondCount() == 1) {
            map = singleBondSubstructureCase(qmol1, qmol2);
        } else if(qmol2.getAtomCount() == 1) {
            List<List<RMap>> single = tester.getSubgraphAtomsMaps(qmol1, qmol2);
            map = new ArrayList<List<RMap>>(single);
        } else {
            map = tester.getSubgraphAtomsMaps(qmol1, qmol2);
        }
        return map;
    }
    private void assignRadicals(List<IAtom> radicals1, QueryMoleculeWithMetaAtoms qmol) {
        Iterator<IAtom> iter = radicals1.iterator();
        while(iter.hasNext()) {
            QueryAtomWithMetaAtoms atm = (QueryAtomWithMetaAtoms) iter.next();
            atm.setToRadical();
        }
    }

    /**
     * 
     * @param mol1
     * @param mol2
     * @param atmnum
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     */
    public Integer[] getMatchesForAtom(IAtomContainer mol1, IAtomContainer mol2, int atmnum) throws CDKException {
        GetSubstructureMatches match = new GetSubstructureMatches();

        List<List<RMap>> matches = match.getAtomMatches(mol1, mol2);
        return getMatchesForAtomWithBondList(mol1, mol2, atmnum, matches);
    }

    /**
     * 
     * @param mol1
     * @param mol2
     * @param atmnum
     * @param matches
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     */
    public Integer[] getMatchesForAtomWithBondList(IAtomContainer mol1, IAtomContainer mol2, int atmnum, List<List<RMap>> matches) throws CDKException {

        HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < matches.size(); i++) {
            List<RMap> lst = matches.get(i);
            for (int j = 0; j < lst.size(); j++) {
                RMap m = lst.get(j);
                if (m.getId2() == atmnum) {
                    Integer n = new Integer(m.getId1());
                    if (!set.contains(n)) {
                        set.add(n);
                    }
                }
            }
        }
        Integer[] seta = (Integer[]) set.toArray(new Integer[set.size()]);
        return seta;
    }

    /**
     * 
     * @param mol1
     * @param mol2
     * @param matches
     * @return
     */
    public ArrayList<ArrayList<IBond>> isolateListOfBondsFromMatches(IAtomContainer mol1, IAtomContainer mol2, List<RMap> matches) {
        ArrayList<ArrayList<IBond>> bonds = new ArrayList<ArrayList<IBond>>();
        ArrayList<IBond> bnds1 = new ArrayList<IBond>();
        ArrayList<IBond> bnds2 = new ArrayList<IBond>();
        bonds.add(bnds1);
        bonds.add(bnds2);

        for (int i = 0; i < matches.size(); i++) {
            RMap map = matches.get(i);
            int id1 = map.getId1();
            int id2 = map.getId1();
            IBond bnd1 = mol1.getBond(id1);
            IBond bnd2 = mol1.getBond(id1);
            bnds1.add(bnd1);
            bnds2.add(bnd2);
        }
        return bonds;
    }
    List<List<RMap>> singleBondSubstructureCase(QueryMoleculeWithMetaAtoms qmol1,QueryMoleculeWithMetaAtoms qmol2) {
        IBond bond = qmol2.getBond(0);
        QueryAtomWithMetaAtoms atm1 = (QueryAtomWithMetaAtoms) bond.getAtom(0);
        QueryAtomWithMetaAtoms atm2 = (QueryAtomWithMetaAtoms) bond.getAtom(1);

        ArrayList<List<RMap>> map = new ArrayList<List<RMap>>();
        ArrayList<RMap> match = null;
        Iterator<IBond> iter = qmol1.bonds().iterator();
        while(iter.hasNext()) {
            IBond molbond = iter.next();
            QueryAtomWithMetaAtoms molatm1 = (QueryAtomWithMetaAtoms) molbond.getAtom(0);
            QueryAtomWithMetaAtoms molatm2 = (QueryAtomWithMetaAtoms) molbond.getAtom(1);

            if(molatm1.matches(atm1)) {
                if(molatm2.matches(atm2)) {
                    RMap match1 = new RMap(qmol1.getAtomNumber(molatm1),qmol2.getAtomNumber(atm1));
                    RMap match2 = new RMap(qmol1.getAtomNumber(molatm2),qmol2.getAtomNumber(atm2));
                    match = new ArrayList<RMap>();
                    match.add(match1);
                    match.add(match2);
                    map.add(match);
                }
            } else {
                if(molatm1.matches(atm2)) {
                    if(molatm2.matches(atm1)) {
                    RMap match1 = new RMap(qmol1.getAtomNumber(molatm1),qmol2.getAtomNumber(atm2));
                    RMap match2 = new RMap(qmol1.getAtomNumber(molatm2),qmol2.getAtomNumber(atm1));
                    match = new ArrayList<RMap>();
                    match.add(match1);
                    match.add(match2);
                    map.add(match);
                    }
                }
            }
        }
        return map;
    }

}
