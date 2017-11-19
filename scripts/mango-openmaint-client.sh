#!/bin/bash

#=============================================================================
#  Simulatore process Launch Script 
#
#  This script is invoked directly.  
#
#  -  buildClassPath(),return the classpath for only 1 folder
#  -  buildGeneralClassPath(),build the classpath inside the variable CLASSPATH1 of the application
#
#=============================================================================

#
#
#Return the classpath for only 1 folder
#
#
buildClassPath() {
        jar_dir=$1
        if [ $# -ne 1 ]; then
                echo "Jar directory must be specified."
               # exit 1
        fi
        class_path=
        c=1
        for i in `ls $jar_dir/*.jar`
        do
                if [ "$c" -eq "1" ]; then
                        class_path=${i}
                        c=2
                else
                        class_path=${class_path}:${i}
                fi
        done
        echo $class_path
}

#
#
#Build the classpath inside the variable CLASSPATH1 of the application
#
#
buildGeneralClassPath() {
	CLASSPATH1=
	CLASSPATH1=`buildClassPath lib`
}


#=============================================================================
#
#
#Start of the Main Program
#
#
#=============================================================================
#
#
#Building the classpath inside the variable CLASSPATH1 of the application
#
#
cd ..
buildGeneralClassPath

#echo "java -cp %CLASSPATH1%  it.storelink.openmaintmango.AllineatoreMain "
nohup java -cp config:src\main\resources:%CLASSPATH1%  it.storelink.openmaintmango.AllineatoreMain 1>> log\mango-openmaint-client.log 2>>&1


			
		

