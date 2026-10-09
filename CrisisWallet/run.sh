#!/bin/sh
# Compile and run CrisisWallet (needs JDK installed)
mkdir -p out
javac -d out $(find src -name '*.java') || exit 1
java -cp out crisiswallet.Main
