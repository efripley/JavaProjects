package com.rip.framework;

public class TilexTest{
	void fileWriter(){
		boolean fileOpenSuccess = txOpenWriter("TestOutput.txt");
		assert fileOpenSuccess == true;

		boolean fileWriteSuccess = txWrite("This is a test");
		assert fileWriteSuccess == true;

		txCloseWriter();
	}

	void fileReader(){
		txOpenWriter("TestOutput.txt");
		txWrite("This is a test");
		txCloseWriter();

		boolean fileOpenSuccess = txOpenReader("TestOutput.txt");
		assert fileOpenSuccess == true;

		String fileString = null;
		fileString = txRead();
		assert fileString != "eof";
		assert fileString != "exception";

		txCloseReader();
	}

	void fileContents(){
		txOpenWriter("TestOutput.txt");
		txWrite("This is a test");
		txCloseWriter();

		txOpenReader("TestOutput.txt");
		assert txRead().equals("This is a test");
		txCloseReader();
	}
}