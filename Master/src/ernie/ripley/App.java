package ernie.ripley;

import static kiss.API.*;
import java.util.*;

public class App{
  private Skier[] skiers = new Skier[100];
	private Lift lift = new Lift();

  void run(){
    println("Running");
	  for(int a = 0; a < 100; a++){
      skiers[a] = new Skier(a, lift);
      new Thread(skiers[a]).start();
    }
	}
}
