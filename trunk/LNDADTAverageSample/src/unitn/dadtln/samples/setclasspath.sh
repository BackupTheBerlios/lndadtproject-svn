JADTCOMPILER=/Users/galiyashka/Documents/workspace/JADTPreProcessor

CLASSPATH=$CLASSPATH:$JADTCOMPILER:$JADTCOMPILER/lib/antlr.jar:$JADTCOMPILER/lib/jargs.jar

echo "Be sure you have a backup, only then uncomment the following lines"
rem for i in *.dadt ; 
rem	do java  -cp $CLASSPATH jadt.Jadtc $i --networkplugin LNView; 
rem	echo $i
rem done
