#
# Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
#
# This code is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License version 2 only, as
# published by the Free Software Foundation.  Oracle designates this
# particular file as subject to the "Classpath" exception as provided
# by Oracle in the LICENSE file that accompanied this code.
#
# This code is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# version 2 for more details (a copy is included in the LICENSE file that
# accompanied this code).
#
# You should have received a copy of the GNU General Public License version
# 2 along with this work; if not, write to the Free Software Foundation,
# Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
#
# Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
# or visit www.oracle.com if you need additional information or have any
# questions.
#


echo "JAVA_HOME ${JAVA_HOME}"
echo "JMH_HOME ${JMH_HOME}"

OS=`uname`
case "$OS" in
    Darwin)
        OS_NAME="darwin"
        LIB_NAME="libjni.dylib"
    ;;
    Linux)
        OS_NAME="linux"
        LIB_NAME="libjni.so"
    ;;
    *)
        echo "Unknown OS ${OS}"
        exit 1
esac

${JAVA_HOME}/bin/javac -cp ${JMH_HOME}/target/benchmarks.jar -h . ${JMH_HOME}/src/main/java/compiler/JNI.java
gcc -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/${OS_NAME} -shared -o ${LIB_NAME} ${JMH_HOME}/src/scripts/jni/jni.c

${JAVA_HOME}/bin/java -jar ${JMH_HOME}/target/benchmarks.jar -jvmArgsAppend -Djava.library.path=`pwd` compiler.JNI