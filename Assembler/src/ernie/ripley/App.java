package ernie.ripley;

import static kiss.API.*;
import java.util.TreeMap;
import java.lang.StringBuilder;

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
		
Macros can be used in the following form [PUTM] [4 letter macro name]
	using a macro...
		PUTM OUTZ
		
Program pointers can be saved for later use in the following way [DFPP] [4 letter pointer name]
	for example
		DFPP LOOP
		
Program pointers can be used in the following way [JUMP] [4 letter pointer name]
	for example...
		JUMP LOOP
		
Macros and Program Pointer variables are allowed to have the same name since access to them depends on the context
*/

public class App{
	TreeMap keywords = new TreeMap();
	int charIndex = 0;
	void Run(){
		this.initKeywords();
	}
	
	void initKeywords(){
		keywords.put("LDPP", 0x01);
		keywords.put("LDDP", 0x02);
		keywords.put("STOR", 0x03);
		keywords.put("ADPP", 0x04);
		keywords.put("ADDP", 0x05);
		keywords.put("IFLT", 0x06);
		keywords.put("IFEQ", 0x07);
		keywords.put("IFGT", 0x08);
		keywords.put("JUMP", 0X09);
		keywords.put("OTPP", 0x0A);
		keywords.put("OTDP", 0x0B);
		keywords.put("INPP", 0x0C);
		keywords.put("INDP", 0x0D);
		keywords.put("MCRB", 0xF1);
		keywords.put("MCRE", 0xF2);
		keywords.put("DEFP", 0xF3);
	}
	
	void testNextLine(){
		String code = "LDPP 0X01\n" +
									"ADPP 0X01\n" +
									"ADPP 0X01\n" +
									"STOR 0XFF\n";
		
		assert this.nextLine(code).equals("LDPP 0X01");
		assert this.nextLine(code).equals("ADPP 0X01");
		assert this.nextLine(code).equals("ADPP 0X01");
		assert this.nextLine(code).equals("STOR 0XFF");
		
		charIndex = 0;
	}
	
	String nextLine(String code){
		StringBuilder line = new StringBuilder();
		boolean done = false;
		char codeChar = (char)0;
		
		while(!done){
			codeChar = code.charAt(charIndex);
			if(codeChar == '\n')
				done = true;
			else
				line.append(codeChar);
			++charIndex;
		}
		
		return line.toString();
	}
	
	void testGetInstruction(){
		String code = "LDPP 0X01\n" +
									"ADPP 0X01\n" +
									"ADPP 0X01\n" +
									"STOR 0XFF\n";
		
		String line = nextLine(code);
		assert getInstruction(line).equals("LDPP");
		
		line = nextLine(code);
		assert getInstruction(line).equals("ADPP");
		
		line = nextLine(code);
		assert getInstruction(line).equals("ADPP");
		
		line = nextLine(code);
		assert getInstruction(line).equals("STOR");
		
		charIndex = 0;
	}
	
	String getInstruction(String line){
		StringBuilder instruction = new StringBuilder();
		boolean done = false;
		int index = 0;
		char codeChar = (char)0;
		
		while(!done){
			codeChar = line.charAt(index);
			
			if(codeChar == ' ')
				done = true;
			else
				instruction.append(codeChar);
			
			++index;
		}
		
		return instruction.toString();
	}
	
	void testGetValue(){
		String code = "LDPP 0X01\n" +
									"ADPP 0X01\n" +
									"ADPP 0X01\n" +
									"STOR 0XFF\n";
		
		String line = nextLine(code);
		assert getValue(line).equals("0X01");

		line = nextLine(code);
		assert getValue(line).equals("0X01");

		line = nextLine(code);
		assert getValue(line).equals("0X01");

		line = nextLine(code);
		assert getValue(line).equals("0XFF");
	}
	
	String getValue(String line){
		StringBuilder value = new StringBuilder();
		boolean done = false;
		boolean instruction = true;;
		int index = 0;
		char codeChar = (char)0;
		
		while(!done){
			codeChar = line.charAt(index);
			
			if(codeChar == ' '){
				if(instruction)
					instruction = false;
			}
			else if(instruction == false)
				value.append(codeChar);
			
			++index;
			if(index >= line.length())
				done = true;
		}
		
		return value.toString();
	}
}
