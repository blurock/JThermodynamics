#!/bin/csh
# ---------------------------------------------------------------------------
#
# Script to Read in a molecule properties to the database
#
# ---------------------------------------------------------------------------
#set verbose on
if ( $#argv != 4 ) then
  echo "Usage: setup.sh File"
  echo "        mysqlhost     :    The mySQL host"
  echo "        mysqlport     :    The port of mySQL"
  echo "        mysqldatabase :    The database name"
  echo "        mysqlpassword :    The root password"
  exit(1)
  endif

  set HOST = $1
  set PORT = $2
  set DATABASE = $3
  set PASSWORD = $4
set MYSQL = jdbc:mysql://$HOST\:$PORT/$DATABASE
echo MSQL database: \'$MYSQL\'
echo password for root:  \'$PASSWORD\'
cat <<EOF > Jthermodynamic.properties
#The name of the Standard Thergas Benson Thermo info
thermo.types.name.thergasbenson=StandardThergasBenson
#Standard temperature for Benson
thermo.data.bensonstandard.temperatures=300,400,500,600,800,1000,1500
#database user (for opening the connection)
thermo.database.dbuser=root
#The password for the databse
thermo.database.dbpassword=$PASSWORD
#The thermodyanamics database
thermo.database.connection=$MYSQL
EOF

cat Jthermodynamic.properties
mvn clean appengine:run


