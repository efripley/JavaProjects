package ernie.ripley;

import java.util.TreeMap;
import java.lang.StringBuilder;

/*
Ernie Ripley
Assembler for my 8bit processor that uses a map from string instruction to byte values.
Assembler expects an instructions followed by a value.
Values can represent locations or numbers depending on the instruction.
Assembler can recognize macros by using the MCRB(macro begin), MCRE(macro end) and MCRO(use macro) instructions
Assembler can set a variable to a jump location by using the DEFJ(define jump) instruction
Assembler can set a variable to a value by using the DEFV(define variable) instruction

Language Functions include
	LDPP --- 0X01
	LDDP --- 0X02
	STOR --- 0X03
	ADPP --- 0X04
	ADDP --- 0X05
	IFLT --- 0X06
	IFEQ --- 0X07
	JUMP --- 0X09
	INPP --- 0X0A
	INDP --- 0X0B
	OTPP --- 0X0C
	OTDP --- 0X0D

Language supports hex, numbers and charachters
	for example
	LDPP 0X5A loads value 01011010
	LDPP CH-Z loads value 01011010
	LDPP #127 loads value 01111111

Language must be written in the from [INSTRUCTION] [VALUE]
	for example...
		LDDP 0X32
		ADDP 0X01
		STOR 0XFF
		OTPP 0XFF

Macros are defined by the following form [Macro begin or end] [4 letter macro name]
	for example...
		MCRB OUTZ
		LDDP 0X5A
		OTPP 0XFF
		MCRE OUTZ
		
Macros can be used in the following form [MCRO] [4 letter macro name]
	using a macro...
		MCRO OUTZ
		
Program pointers can be saved for later use in the following way [DFPP] [4 letter pointer name]
	for example
		DEFJ LOOP
		
Program pointers can be used in the following way [JUMP] [4 letter pointer name]
	for example...
		JUMP LOOP
		
Macros and Jump Pointer variables are allowed to have the same name since access to them depends on the context
*/

public class App{
	TreeMap<String, Byte> keywords = new TreeMap<String, Byte>();
	TreeMap<String, String> macros = new TreeMap<String, String>();
	TreeMap<String, Byte> jumpVars = new TreeMap<String, Byte>();
	TreeMap<String, Byte> valueVars = new TreeMap<String, Byte>();
	int[] cpuInstructionRange = {(byte)0x01, (byte)0x0D};
	int[] asmInstructionRange = {(byte)0xF1, (byte)0xF5};
	
	int index = 0;
	byte memoryLocation = (byte)0x00;

	int READ = 0;
	int DEFJ = 1;
	int JUMP = 2;
	int mode = READ;
	
	String inFile;
	StringBuilder outFile = new StringBuilder();
	
  public static void main(String [] args){
    App app = new App();

    app.testInitTestVars();
    app.testInitKeywords();
    app.testNextToken();
    app.testTotalEvaluateWriteFunctions();
  }
	
	void testInitTestVars(){
		inFile = "MCRB NAME\n" +
						 "LDPP CH-E\n" +
						 "OTDP 0XFF\n" +
						 "LDPP CH-R\n" +
						 "OTDP 0XFF\n" +
						 "LDPP CH-N\n" +
						 "OTDP 0XFF\n" +
						 "LDPP CH-I\n" +
						 "OTDP 0XFF\n" +
						 "LDPP CH-E\n" +
						 "OTDP 0XFF\n" +
						 "LDPP #013\n" +
						 "OTDP 0XFF\n" +
						 "MCRE NAME\n" +
						 "DEFJ STRT\n" +
						 "MCRO NAME\n" +
						 "LDPP 0X00\n" +
						 "IFEQ 0x00\n" +
						 "JUMP STRT\n";
	}
	
	void testInitKeywords(){
		initKeywords();
	
		assert (keywords.get("LDPP").byteValue() == (byte)0x01);
		assert (keywords.get("DEFJ").byteValue() == (byte)0xF4);
	}
	
	void initKeywords(){
		keywords.put("LDPP",(byte)0x01);
		keywords.put("LDDP",(byte)0x02);
		keywords.put("STOR",(byte)0x03);
		keywords.put("ADPP",(byte)0x04);
		keywords.put("ADDP",(byte)0x05);
		keywords.put("IFLT",(byte)0x06);
		keywords.put("IFEQ",(byte)0x07);
		keywords.put("IFGT",(byte)0x08);
		keywords.put("JUMP",(byte)0X09);
		keywords.put("INPP",(byte)0x0A);
		keywords.put("INDP",(byte)0x0B);
		keywords.put("OTPP",(byte)0x0C);
		keywords.put("OTDP",(byte)0x0D);
		keywords.put("MCRB",(byte)0xF1);
		keywords.put("MCRE",(byte)0xF2);
		keywords.put("MCRO",(byte)0xF3);
		keywords.put("DEFJ",(byte)0xF4);
	}
	
	void testNextToken(){
		assert this.nextToken().equals("MCRB");
		assert this.nextToken().equals("NAME");
		assert this.nextToken().equals("LDPP");
		assert this.nextToken().equals("CH-E");
	}
	
	String nextToken(){
		StringBuilder token = new StringBuilder();
		boolean done = false;
		int index = 0;
		char tokenChar = (char)0;
		
		while(!done){
			tokenChar = inFile.charAt(index);
			
			if(tokenChar == ' '){
				while(tokenChar == ' ' || tokenChar == '\t' || tokenChar == '\n'){
					index++;
					tokenChar = inFile.charAt(index);
				}
				done = true;
			}
			else
				token.append(tokenChar);
			
			index++;
		}
		
		memoryLocation++;
		
		return token.toString();
	}
	
	void testTotalEvaluateWriteFunctions(){
		outFile = new StringBuilder();
		
		evaluateToken("0X05");
		assert outFile.charAt(outFile.length() - 1) == (char)0x05;
		
		evaluateToken("CH-E");
		assert outFile.charAt(outFile.length() - 1) == 'E';
		
		evaluateToken("CH-$");
		assert outFile.charAt(outFile.length() - 1) == '$';
		
		evaluateToken("LDPP");
		assert outFile.charAt(outFile.length() - 1) == (char)0x01;

		evaluateToken("JUMP");
		assert mode == JUMP;
		mode = READ;
		assert outFile.charAt(outFile.length() - 1) == (char)0x09;
	}

	void evaluateToken(String token){
		if(memoryLocation % 2 == 0){
			if(token == "JUMP"){
				writeByte(keywords.get(token));
				mode = JUMP;
			}
			else if(token =="DEFJ"){
				mode = DEFJ;
			}
			else if(token == "MCRB"){
				//Not yet implemented
			}
			else if(token == "MCRO"){
				//Not yet iplemented
			}
		}
		else{
			interpretAsValue(token);
		}
	}

	void writeByte(byte outByte){
		outFile.append((char)outByte);
	}

	void interpretAsValue(String value){
		if(value.charAt(0) == '0' && value.charAt(1) == 'X')
			writeByte((byte)((Character.digit(value.charAt(2), 16) << 4) + Character.digit(value.charAt(3), 16)));
		else if(value.charAt(0) == '#')
			writeByte((byte)Integer.parseInt(value.substring(1)));
		else if(value.charAt(0) == 'C' && value.charAt(1) == 'H' && value.charAt(2) == '-')
			writeByte((byte)value.charAt(3));
		else if(mode == JUMP){
			if(jumpVars.containsKey(value))
				writeByte((byte)(jumpVars.get(value).byteValue() - memoryLocation));
		}
		else if(mode == DEFJ){
			memoryLocation -= 2;
			jumpVars.put(value, (byte)memoryLocation);
		}
	}
}
