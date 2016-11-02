.PHONY: buildTest
buildTest :
	javac -cp .:lib/*:src/ernie/ripley/* -d build/test/ test/ernie/ripley/AppTest.java

runTest : buildTest
	java -cp .:lib/*:build/test/ org.junit.runner.JUnitCore ernie.ripley.AppTest
