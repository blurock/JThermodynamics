# JThermodynamics
# JThermodynamics
JThermodynamics estimates temperature dependent thermodynamic information from two dimensional graphical representations of molecules and radicals based on the Benson additivity method. The main use of this method is within automatic combustion mechanism generation systems where fast estimation of a large number and variety of chemical species is needed. The implementation strategy is based on meta-atom definitions and substructure analysis allowing a highly extensible database without modification of the core algorithms.
JThermodynamics can calculate the temperature thermodynamic information using three different methods:
* [JTHERGAS](https://doi.org/10.1016/j.energy.2012.01.072): This calculates radicals based on the difference between the parent molecule (hydrogen added to radical). Losses dependent on symmetry, vibrational moments, disassociation energies, and rotational (still in progress).
* [Benson Radical Rules](https://books.google.se/books/about/Thermochemical_Kinetics.html?id=a48-AAAAIAAJ&redir_esc=y): Benson rules for radicals are explicitely given (based on Table  2.14 from Benson's book and corrections given in the literature).
* [THERM](https://doi.org/10.1002/kin.550230903): Structural dependent corrections, which include vibrational moments, disassociation energies, and rotational moments, are made with one correction (Hydrogen Bond Increments - HBI) to the parent structure.

Document of the software, including installation and usage can be found here:
[JThermodynamics Documentation](https://sites.google.com/view/jthergas/home)

This version can be used with line commands using the JAVA jar file and the mySQL database file. The prerequisites for using these files is to install [JAVA](https://www.java.com/en/download/) and [mySQL](https://sites.google.com/view/jthergas/installation/mysqlsetup).  Using the line commands the thermodynamics can be calculated and the database queried and modified. The Jthermodynamic.properties file should be in the same directory. This file also gives the information about the mySQL database (see the [mySQL setup instructions](https://sites.google.com/view/jthergas/installation/mysqlsetup)).

The line command for computing the thermodynamics is:
java -jar JThermodynamicData-1.0-jar-with-dependencies.jar Thermo 'ch3/ch2/ch2/ch2(.)' '1-butyl'
This calculates the molecule, given in Nancy Linear form, using the JTHERGAS method and sends the output to the terminal. The user can modify these parameters using additional commands. For example, to calculate with THERM and send the output to a file in csv format, then the following command is used:
java -jar .JThermodynamicData-1.0-jar-with-dependencies.jar Thermo 'ch3/ch2/ch2/ch2(.)' '1-butyl' OutputFormat=csv OutputFile=/Users/edwardblurock/THERMButyl.csv method=THERM.
Documentation and examples of all the available can be found in the [Line Command documentation](https://sites.google.com/view/jthergas/usage/linecommands/compute-thermodynamics). 
Several molecules can be calculated at once using the ThermoSet command. For the full set of commands available see the [Line Command Documentation](https://sites.google.com/view/jthergas/usage/linecommands)


## Software Prerequisites

In order to use this jar file there are two software prerequisites:
 * Setup JAVA to your local computer, for example [here](https://www.java.com/en/download/)
 * Setup a mySQL database (see [here](https://sites.google.com/view/jthergas/installation/4-2mysqlsetup) for details to get you started)

## Downloads and setup

The installation of the software has three steps:
 * Download (see files below) read in the JThermodynamics_20110706.sql file  into the mySQL database you set up (see [here](https://sites.google.com/view/jthergas/installation/4-2mysqlsetup) for details to get you started)
 * Download and modify the Jthermodyanmics.properties file
 * Download (in the same directory) the jar file

You may need to modify may parameters in the Jthermodyanmics.properties file. First is the connector to the mySQL database:
```
thermo.database.connection=jdbc:mysql://localhost:3306/thermodynamics
```
 * If the database is on another host, you would replace 'localhost' with the IP of where the database is running.
 * 3306 is the default port of a mySQL installation.
 * 'thermodynamics' is the name I use for the JThermodynamic information (where you read in the SQL file)

The other line you may have to modify is:
```
thermo.database.dbpassword=password
```
Replace 'password' with the root password you gave when you installed the mySQL database.

## Running and calculating

The java command looks like this:
```
       java -jar JThermodynamicData-1.0-jar-with-dependencies.jar <commands>
```
where <commands> are the input parameters

Typing just:
```
java -jar JThermodynamicData-1.0-jar-with-dependencies.jar
```
will give a hint as to the input.

For example: 
```
java -jar JThermodynamicData-1.0-jar-with-dependencies.jar Thermo 'ch3/ch2/ch2/ch2(.)' '1-butyl' method=THERM
```

Will give an output like:
```
BensonThermodynamicBase(RCCJ):	Reference: 'HBI-Bozzelli'
H298: 101.10 S298:   1.24 [  300.0, -0.77] [  400.0, -1.36] [  500.0, -1.91] [  600.0, -2.40] [  800.0, -3.16] [ 1000.0, -3.74]
BensonThermodynamicBase(Spin Contribution: Rln(2) = (1.98587755)*(0.6931471805599453)):	Reference: 'Radical Correction'
H298:   0.00 S298:   1.38
BensonThermodynamicBase(Hydrogen Radical):	Reference: 'Radical Correction'
H298: -52.10 S298:   0.00
BensonThermodynamicBase(c-(h)/3-(c)):	Reference: 'Benson20210129'
H298: -10.01 S298:  30.29 [  300.0,  7.74] [  400.0,  9.24] [  500.0, 10.62] [  600.0, 12.84] [  800.0, 14.59] [ 1000.0, 14.77] [ 1500.0, 17.58]
BensonThermodynamicBase(c-(h)/2-(c)/2):	Reference: 'Benson20210129'
H298:  -5.00 S298:   9.65 [  300.0,  5.59] [  400.0,  7.08] [  500.0,  8.43] [  600.0,  9.53] [  800.0, 11.23] [ 1000.0, 12.48] [ 1500.0, 14.25]
BensonThermodynamicBase(c-(h)/2-(c)/2):	Reference: 'Benson20210129'
H298:  -5.00 S298:   9.65 [  300.0,  5.59] [  400.0,  7.08] [  500.0,  8.43] [  600.0,  9.53] [  800.0, 11.23] [ 1000.0, 12.48] [ 1500.0, 14.25]
BensonThermodynamicBase(c-(h)/3-(c)):	Reference: 'Benson20210129'
H298: -10.01 S298:  30.29 [  300.0,  7.74] [  400.0,  9.24] [  500.0, 10.62] [  600.0, 12.84] [  800.0, 14.59] [ 1000.0, 14.77] [ 1500.0, 17.58]
BensonThermodynamicBase(InternalSymmetry-B(B1)(B2)(B2)(B2) (9)):	Reference: 'Symmetry'
H298:   0.00 S298:  -4.36
BensonThermodynamicBase(RCCJ):	Reference: 'HBI-Bozzelli'
H298: 101.10 S298:   1.24 [  300.0, -0.77] [  400.0, -1.36] [  500.0, -1.91] [  600.0, -2.40] [  800.0, -3.16] [ 1000.0, -3.74]
BensonThermodynamicBase(Spin Contribution: Rln(2) = (1.98587755)*(0.6931471805599453)):	Reference: 'Radical Correction'
H298:   0.00 S298:   1.38
BensonThermodynamicBase(Hydrogen Radical):	Reference: 'Radical Correction'
H298: -52.10 S298:   0.00
BensonThermodynamicBase(Total):	Reference: 'Sum Total'
H298:  18.98 S298:  78.13 [  300.0, 25.89] [  400.0, 31.28] [  500.0, 36.19] [  600.0, 42.34] [  800.0, 48.48] [ 1000.0, 50.76] [ 1500.0, 59.92]
```
If you want it in another form, for example csv (or html) then you could use the command:
```
java -jar JThermodynamicData-1.0-jar-with-dependencies.jar Thermo 'ch3/ch2/ch2/ch2(.)' '1-butyl' OutputFormat=csv OutputFile=THERMButyl.csv method=THERM
```
Which would produce:

Name,"Reference" ,"Hf298" ,"S298" ,"Cp300" ,"Cp400" ,"Cp500" ,"Cp600" ,"Cp800" ,"Cp1000" ,"Cp1500" 
"c-(h)/3-(c)",""Benson20210129"" ,"-10.01" ," 30.29" ,"  7.74" ,"  9.24" ," 10.62" ," 12.84" ," 14.59" ," 14.77" ," 17.58" 
"c-(h)/2-(c)/2",""Benson20210129"" ," -5.00" ,"  9.65" ,"  5.59" ,"  7.08" ,"  8.43" ,"  9.53" ," 11.23" ," 12.48" ," 14.25" 
"c-(h)/2-(c)/2",""Benson20210129"" ," -5.00" ,"  9.65" ,"  5.59" ,"  7.08" ,"  8.43" ,"  9.53" ," 11.23" ," 12.48" ," 14.25" 
"c-(h)/3-(c)",""Benson20210129"" ,"-10.01" ," 30.29" ,"  7.74" ,"  9.24" ," 10.62" ," 12.84" ," 14.59" ," 14.77" ," 17.58" 
"InternalSymmetry-B(B1)(B2)(B2)(B2) (9)",""Symmetry"" ,"  0.00" ," -4.36" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" 
"RCCJ",""HBI-Bozzelli"" ,"101.10" ,"  1.24" ," -0.77" ," -1.36" ," -1.91" ," -2.40" ," -3.16" ," -3.74" ," -3.74" 
"Spin Contribution: Rln(2) = (1.98587755)*(0.6931471805599453)",""Radical Correction"" ,"  0.00" ,"  1.38" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" 
"Hydrogen Radical",""Radical Correction"" ,"-52.10" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" ,"  0.00" 
"Total",""Sum Total"" ," 18.98" ," 78.13" ," 25.89" ," 31.28" ," 36.19" ," 42.34" ," 48.48" ," 50.76" ," 59.92" 
```
