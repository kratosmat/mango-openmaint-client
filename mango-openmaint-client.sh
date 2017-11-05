#!/bin/bash

#=============================================================================
#  Simulatore process Launch Script 
#
#  This script is invoked directly.  
#
#  -  buildClassPath(),return the classpath for only 1 folder
#  -  buildGeneralClassPath(),build the classpath inside the variable CLASSPATH1 of the application
#  -  SetJavaHome(),set the Java Home variable APP_JAVA_HOME
#  -  GetShortProductName(), which is used in prompt, warning, and
#     error messages shown to the user.
#  -  SetSkipJ2SDKCheck(),set if yuo have to skip the check of JVM
#  -  CheckJDK(),main function asking for a JVM to the user if not found automatically
#  -  GetDefaultJDK(),this fuction searchs for JVM inside the server.
#  -  CheckJavaHome(), if the APP_JAVA_HOME is to check
#  -  GetDotJdkFileName(), which is the file in the user's home directory
#     that stores the path to the JDK or JRE used for the product.
#  -  GetDotJdkFileName(), where to store the information of the JVM location.
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
buildGeneralClassPath

#echo "java -cp %CLASSPATH1%  it.storelink.openmaintmango.AllineatoreMain "
nohup java -cp %CLASSPATH1%  it.storelink.openmaintmango.AllineatoreMain 1>> log\mango-openmaint-client.log 2>>&1


			
		

