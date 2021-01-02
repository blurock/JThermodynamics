# JThermodynamics
JTHERGAS (a JAVA implementation of THERGAS) estimates thermodynamic information from two dimensional graphical representations of molecules and radicals based on the Benson additivity method. The main use of this method is within automatic combustion mechanism generation systems where fast estimation of a large number and variety of chemical species is needed. The implementation strategy is based on meta-atom definitions and substructure analysis allowing a highly extensible database without modification of the core algorithms. Several interfaces for the database and the calculations are provided from terminal line commands, to graphical interfaces to web-services. The first order estimation of thermodynamics is based summing up the contributions of each heavy atom bonding description. Second order corrections due to steric hindrance and ring strain are made. Automatic estimate of contributions due to internal, external and optical symmetries are also made. Radicals are calculated by taking the difference due to the lost of hydrogen radicals taking into account changes in symmetry, spin, rotations, vibrations and steric hindrances. 

The method of calculation can be found here:

[JTHERGAS: Thermodynamic estimation from 2D graphical representations of molecules](https://sites.google.com/view/jthergas/home)

Online documentation, including the various ways of installation can be found [here](https://sites.google.com/view/jthergas/home).


