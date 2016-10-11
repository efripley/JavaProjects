package ernie.ripley;

import static kiss.API.*;
import java.util.TreeMap;

/*
Ernie Ripley
Interpreter for language X that uses a map from variable names to values

This interpreter only recognizes a few things. Integers Functions Booleans Variables

Language Functions include
	VAR - a function to set a variable to a value
	ADD - a function to add two variables
	SUB - a function to subtract two variables
	OUT - a function to print a variable
	INP - a function to input a value to a variable
*/

public class App{
	TreeMap vars = new TreeMap();
	
	void Run(){
		
	}
	
	String getToken(String code, int tokenIndex){
		String token = "";
		char codeChar = (char)0;
		int count = -1;
		int index = 0;
		
		while(count != tokenIndex){
			if(codeChar == ' '){
				token = "";
			}
			codeChar = code.charAt(index);
			index++;
			if(codeChar >= 'A' && codeChar <= 'Z')
				token += codeChar;
			if(codeChar >= '0' && codeChar <= '9')
				token += codeChar;
			if(codeChar == ' ' || index == code.length()){
				count++;
			}
		}
		
		return token;
	}
	
	void testValidParser(){
		String codeInX = "VAR X 15";
		assert getToken(codeInX, 0).equals("VAR");
		assert getToken(codeInX, 1).equals("X");
		assert getToken(codeInX, 2).equals("15");
	}
	
	void testInvalidParser(){
		String codeInX = "VAR X 15";
		assert (getToken(codeInX, 0).equals("X") == false);
		assert (getToken(codeInX, 2).equals("VAR") == false);
		assert (getToken(codeInX, 2).equals("X") == false);
	}
}
