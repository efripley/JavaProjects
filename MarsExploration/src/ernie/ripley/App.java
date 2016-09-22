package ernie.ripley;

import java.util.Scanner;
import static kiss.API.*;

// https://www.hackerrank.com/challenges/mars-exploration
// Ernie Ripley



public class App {
  
  String message;
  Scanner input;

  void run(){
    message = null;
    input = new Scanner(System.in);

    println("Mars Exploration: By Ernie Ripley");
    println("\n");
    println("What is Sami's garbled message");
    
    message = input.next();

    assert message.length() % 3 == 0;
    assert message.length() / 3 > 0;
    assert message.length() / 3 < 100;

    int numErrors = messageErrors(message);

    if(numErrors == 0)
      println("Sami's message appears to be perfect!");
    if(numErrors == 1)
      println("It looks like there was only", numErrors, "error in Sami's SOS message.");
    else
      println("Whoa, it looks like there were", numErrors, "errors in Sami's SOS message!");
  }

  int messageErrors(String input){
    int numErrors = 0;

    for(int a = 0; a < input.length(); a+=3){
      if(input.charAt(a) != 'S')
        ++numErrors;
      if(input.charAt(a + 1) != 'O')
        ++numErrors;
      if(input.charAt(a + 2) != 'S')
        ++numErrors;
    }

    return numErrors;
  }

  void testMessageErrors(){
    outExpect("3");
    println(messageErrors("SOSSASSOISSS"));
    outClose(); 
  }
}
