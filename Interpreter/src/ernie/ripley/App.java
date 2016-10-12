package ernie.ripley;

import static kiss.API.*;
import java.util.TreeMap;

/*
Ernie Ripley
Assembler for my 8bit processor that uses a map from string instruction to byte values.
Assembler expects an instructions followed by a value.
Values can represent locations or numbers depending on the instruction.

Language Functions include
	LDPP --- 0X01
	LDDP --- 0X02
	STOR --- 0X03
	ADPP --- 0X04
	ADDP --- 0X05
	IFLT --- 0X06
	IFEQ --- 0X07
	IFGT --- 0X08
	INPP --- 0X09
	INDP --- 0X0A
	OTPP --- 0X0B
	OTDP --- 0X0C
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

