package ernie.ripley;

import static kiss.API.*;
import java.util.*;

public class App{
  private Skier[] skiers = new Skier[100];
	private Lift lift = new Lift();
  private boolean verbose;

  void run(){
    println("Choose Mode:");
    println("a for verbose");
    println("b for minimum");
    Scanner scanner = new Scanner(System. in); 
    String input = scanner. nextLine();

    if(input.equals("a"))
      verbose = true;
    else
      verbose = false;

    println("Running");
	  for(int a = 0; a < 100; a++){
      skiers[a] = new Skier(a, lift, verbose);
      new Thread(skiers[a]).start();
    }
	}
}
