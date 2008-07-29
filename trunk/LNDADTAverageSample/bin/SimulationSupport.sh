
WD=~/Documents/workspace/jist-swans-1.0.6
JAVAME=~/Documents/workspace/javaME
DADT=~/Documents/workspace/DADT2Update

CLASSPATH=$WD:$WD/libs:$WD/libs/bcel.jar:$WD/libs/bsh.jar:$WD/libs/checkstyle-all.jar:$WD/libs/jargs.jar:$WD/libs/jython.jar:$WD/libs/log4j.jar:$WD/libs/jist.jar:$WD/libs/swans.jar:$JAVAME:$JAVAME/polimi/ln/runtime:$DADT/bin:$DADT/bin/DADT:$DADT/bin/DADT/LnSupport

cd ~/Documents/workspace/LNDADTAverageSample

javac -d bin -sourcepath src -classpath $CLASSPATH src/sensorApp/*.java
echo "Starting sensorApp.SimulationSupport, check LogNormal.txt and LogErrors.txt"
echo "Press Ctrl+C to stop"
java -classpath $CLASSPATH jist.runtime.Main jist.swans.Main  sensorApp.SimulationSupport 1>LogNormal.txt 2>LogErrors.txt
echo java -classpath $CLASSPATH jist.runtime.Main jist.swans.Main  sensorApp.SimulationSupport

