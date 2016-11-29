package ernie.ripley;

import static kiss.API.*;
import java.util.*;

public class SkiingActivity{
  public static String initialStatus(){
    return "Waiting For Lift";
  }

  public static String updateStatus(String currentStatus){
    if(currentStatus.equals("Waiting For Lift"))
      return "Riding Lift";
    else if(currentStatus.equals("Riding Lift"))
      return "Skiing";
    else if(currentStatus.equals("Skiing"))
      return "Idle";
    else if(currentStatus.equals("Idle"))
      return "Waiting for lift";
    else
      return "Huh?";
  }
}