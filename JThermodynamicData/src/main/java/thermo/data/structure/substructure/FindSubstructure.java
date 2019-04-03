/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.substructure;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.isomorphism.mcss.RMap;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLAtomCounts;
import thermo.data.structure.structure.AtomCounts;
import thermo.data.structure.structure.matching.GetSubstructureMatches;
import thermo.data.structure.substructure.DB.SQLSubStructure;

/**
 *
 * @author edwardblurock
 */
public class FindSubstructure {

    ThermoSQLConnection connection;
    SQLSubStructure sqlSubStructure;
    SQLAtomCounts sqlAtomicCounts;
    IAtomContainer moleculeToCompare;
    AtomCounts atomCounts;
    GetSubstructureMatches matches;

    /**
     *
     * @param molecule
     * @param connection
     */
    public FindSubstructure(IAtomContainer molecule,
            ThermoSQLConnection connection) {
        this.connection = connection;

        sqlSubStructure = new SQLSubStructure(connection);
        sqlAtomicCounts = new SQLAtomCounts(connection);

        moleculeToCompare = molecule;
        atomCounts = new AtomCounts(molecule);
        matches = new GetSubstructureMatches();
    }

    /**
     *
     * @param names
     * @return
     * @throws SQLException
     * @throws CDKException
     */
    public String findLargestSubstructure(List<String> names) throws SQLException, CDKException {
        List<AtomCounts> counts = findSubStructureCandidates(names);
        String name = findLargestSubStructure(counts);
        return name;
    }

    /**
     *
     * @param names
     * @return
     * @throws SQLException
     * @throws CDKException
     */
    public ListOfStructureMatches findNonoverlappingSubstructures(List<String> names) throws SQLException, CDKException {

        ListOfStructureMatches candidates = new ListOfStructureMatches();
        Iterator<String> iter = names.iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            List<List<RMap>> map = findSubstructureMatch(name);
            candidates.append(name, map);
        }
        return insertIfUnique(candidates);
    }

    /**
     *
     * @param candidates The list of candidate matches
     * @return collection The collection of 'unique' matches.
     *
     * The list of candidate matches are checked and put into the list if it
     * unique or it is not a subset of one in the collection.
     */
    public ListOfStructureMatches insertIfUnique(ListOfStructureMatches candidates) {
        ListOfStructureMatches collection = new ListOfStructureMatches();
        insertIfUnique(candidates, collection);
        return collection;
    }

    /**
     *
     * @param candidates The list of candidate matches
     * @param collection The collection to add to
     *
     * The list of candidate matches are checked and put into the list if it
     * unique or it is not a subset of one in the collection.
     */
    public void insertIfUnique(ListOfStructureMatches candidates, ListOfStructureMatches collection) {
        candidates.sort();
        Iterator<StructureMatch> iter = candidates.iterator();
        while (iter.hasNext()) {
            StructureMatch map = iter.next();
            updateCollection(map, collection);
        }
    }

    /**
     *
     * @param map
     * @param collection
     *
     * This routine inserts map into the collection if the first identifiers in the
     * map does not match with every element of the collection.
     *
     * It is assumed that the size of the map is less than or equal any map in
     * the collection
     *
     * The help routine is 'testForOverlap'
     */
    private void updateCollection(StructureMatch map, ListOfStructureMatches collection) {
            Iterator<StructureMatch> iter = collection.iterator();
            boolean nomatch = true;
            while (iter.hasNext()) {
                StructureMatch cmap = iter.next();
                if (cmap.size() == map.size()) {
                    // same size: add if not overlapping
                    if (testForOverlap(map, cmap)) {
                        nomatch = false;
                    }

                } else if (cmap.size() > map.size()) {
                    // cmap larger:
                    //   if map is a subset, then don't add
                    //   if map is not a subset, then add
                    if (testForOverlap(map, cmap)) {
                        nomatch = false;
                    }
                } else {
                    System.err.println("Mapping was not ordered before entering "
                            + "this routine: logical error");
                }
            }
            if(nomatch)
                collection.add(map);
    }

    /**
     *
     * @param map The map of the new mapping
     * @param cmap The map of one in the collection
     * @return  true if both maps match the same substructure.
     *
     * This is the case where the size of map is less than or equal to
     * the size of cmap.
     *
     *
     * If the set of first identifiers of map is found in cmap, then map
     * matches cmap (if the sizes are the same) or is a subset (if cmap is
     * larger) and the routine returns true.
     *
     * The logic of the routine is that the 'match' is true until
     * proven otherwise. 'match' start off true. Each Id in the
     * new mapping is checked to see if it is in the collection map.
     * If found ('notfound' set to false) then the possibility that the
     * maps are the same still holds.
     */
    private boolean testForOverlap(List<RMap> map, List<RMap> cmap) {
        Iterator<RMap> iter = map.iterator();
        boolean match = true;
        while (iter.hasNext() && match) {
            RMap pair = iter.next();
            int id = pair.getId1();
            Iterator<RMap> citer = cmap.iterator();
            boolean notfound = true;
            while (notfound && citer.hasNext()) {
                RMap cpair = citer.next();
                int cid = cpair.getId1();
                if (cid == id) {
                    notfound = false;
                }
            }
            match = !notfound;
        }
        return match;
    }

    private SubStructure getSubStructure(String name) throws SQLException {
        HashSet<SubStructure> vec = sqlSubStructure.retrieveStructuresFromDatabase(name);
        SubStructure structure = null;
        Iterator<SubStructure> iter = vec.iterator();
        if (vec.size() == 1) {
            structure = iter.next();
        } else {
            throw new SQLException("Substructure with name '"
                    + name + "' not unqique, found "
                    + vec.size() + " instances");
        }
        return structure;
    }

    /**
     *
     * @param name
     * @return
     * @throws SQLException
     * @throws CDKException
     */
    public List<List<RMap>> findSubstructureMatch(String name) throws SQLException, CDKException {

        AtomCounts counts = sqlAtomicCounts.getAtomCounts(name);
        return findSubstructureMatch(counts);
    }

    /**
     *
     * @param counts
     * @return
     * @throws SQLException
     * @throws CDKException
     */
    public List<List<RMap>> findSubstructureMatch(AtomCounts counts) throws SQLException, CDKException {
        List<List<RMap>> maps = new ArrayList<List<RMap>>();
        if (atomCounts.strictlyLessThanOrEqual(counts)) {
            String name = counts.getMoleculeID();
            SubStructure structure = getSubStructure(name);
            IAtomContainer substructure = (IAtomContainer) structure.getSubstructure();
            List<List<RMap>> atommaps = matches.getAtomMatches(moleculeToCompare, substructure);
            Iterator<List<RMap>> iter = atommaps.iterator();
            while (iter.hasNext()) {
                List<RMap> atommap = iter.next();
                if (atommap.size() == substructure.getAtomCount()) {
                    maps.add(atommap);
                }
            }
        }
        return maps;


    }

    /**
     *
     * @param name
     * @return
     * @throws SQLException
     * @throws CDKException
     */
    public boolean isSubstructure(String name) throws SQLException, CDKException {
        boolean ans = false;
        List<List<RMap>> maps = findSubstructureMatch(name);
        if (maps.size() > 0) {
            ans = true;
        }
        return ans;
    }

    /**
     *
     * @param counts
     * @return
     * @throws SQLException
     * @throws CDKException
     */
    public boolean isSubstructure(AtomCounts counts) throws SQLException, CDKException {
        boolean ans = false;
        List<List<RMap>> maps = findSubstructureMatch(counts);
        if (maps.size() > 0) {
            ans = true;
        }
        return ans;
    }

    /**
     * 
     * @param names
     * @return
     * @throws SQLException
     */
    protected List<AtomCounts> findSubStructureCandidates(List<String> names) throws SQLException {
        List<AtomCounts> map = new ArrayList<AtomCounts>();
        Iterator<String> iter = names.iterator();
        //System.out.println(atomCounts.toString());
        while (iter.hasNext()) {
            String name = iter.next();
            //System.out.println("Cycle to test: " + name);
            AtomCounts count = sqlAtomicCounts.getAtomCounts(name);
            //System.out.println("Candidate: " + name + "\n" + count.toString());
            if (atomCounts.strictlyLessThanOrEqual(count)) {
                map.add(count);
                //System.out.println("Add to candidates");
            }
        }
        return map;
    }

    /**
     *
     * @param counts
     * @return
     * @throws SQLException
     * @throws CDKException
     */
    public String findLargestSubStructure(List<AtomCounts> counts) throws SQLException, CDKException {
        Iterator<AtomCounts> cntiter = counts.iterator();
//        while(cntiter.hasNext()) {
//            System.out.print("\t" + cntiter.next().getMoleculeID());
//        }
//        System.out.println("");
        Collections.sort(counts);
        cntiter = counts.iterator();
        //while(cntiter.hasNext()) {
        //System.out.print("\t" + cntiter.next().getMoleculeID());
        //}
        //System.out.println("");
        Iterator<AtomCounts> iter = counts.iterator();
        boolean notfound = true;
        AtomCounts cnts = null;
        while (iter.hasNext() && notfound) {
            cnts = iter.next();
            //System.out.println("findLargestSubStructure: " + cnts.getMoleculeID());
            if (isSubstructure(cnts)) {
                System.out.println("Matched!");
                notfound = false;
            }
        }
        String substructureS = null;
        if (!notfound) {
            substructureS = cnts.getMoleculeID();
        }

        return substructureS;
    }
}
