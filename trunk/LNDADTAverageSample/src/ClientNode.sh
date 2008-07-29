
WD=~/Documents/MasterThesis/Code/LNDADTproject/LNDADTAverageSample
DADT=~/Documents/workspace/DADT2Update

CLASSPATH=$WD:$WD/libs:$WD/libs/bcel.jar:$WD/libs/bsh.jar:$WD/libs/checkstyle-all.jar:$WD/libs/jargs.jar:$WD/libs/jython.jar:$WD/libs/log4j.jar:$WD/libs/jist.jar:$WD/libs/swans.jar:$DADT/bin:$DADT/bin/polimi/ln/runtime:$DADT/bin/DADT:$DADT/bin/DADT/LnSupport

cd $WD

javac -d bin -sourcepath src -classpath $CLASSPATH src/unitn/dadtln/samples/*.java

java -classpath $CLASSPATH jist.runtime.Main jist.swans.Main unitn.dadtln.samples.ClientNode
