package ernie.ripley;

import static kiss.API.*;
import java.util.*;

public class Lift{
  private boolean boarding = true;
  private List<String> trails;

  Lift(){
    trails = new ArrayList<String>();

    trails.add("Skip Hop Jump");
    trails.add("Slow Fast Stop");
    trails.add("Steep Crash Stop");
    trails.add("Up And Down");
    trails.add("Round And Round");
  }

  public boolean available(){
    return boarding;
  }

  public void board(){
    boarding = false;
    pause(.1);
    boarding = true;
  }

  public String getTrail(int trailNumber){
    if(trailNumber < 0)
      return "Not Yet";
    return trails.get(trailNumber);
  }
}