package ernie.ripley;

import static kiss.API.*;
import java.util.*;

public class Skier implements Runnable{
  private int health;
  private String status;
  private String trail;
  private boolean running;
  private int id;
  private int runs;
  private int time;
  private Lift lift;
  private boolean verbose;

  Skier(int argId, Lift argLift, boolean argVerbose){
    println("Skier", argId, "Has Arrived!");
    running = true;
    health = 10;
    status = SkiingActivity.initialStatus();
    trail = "";
    id = argId;
    runs = 1;
    time = 0;
    lift = argLift;
    verbose = argVerbose;
  }

  public void run(){
    while(running){
      update();
    }
  }

  public void update(){
    if(status == "Waiting For Lift"){
      pause(.1);
      if(lift.available()){
        lift.board();
        status = SkiingActivity.updateStatus(status);
        trail = lift.getTrail(random(4));
        time = 0;
        if(verbose)
          println("Skier", id, ": I boarded the ski lift and I'm heading to", trail, "trail.");
      }
    }
    else if(status.equals("Riding Lift")){
      pause(.1);
      time++;
      if(lift.getTrail(time - 5).equals(trail)){
        status = SkiingActivity.updateStatus(status);
        if(verbose)
          println("Skier", id, ": Finaly made it to", trail, "YeeHaa!");
      }
    }
    else if(status == "Skiing"){
      pause(.1);
      time--;

      if(random(3) == 0){
        health--;
        if(verbose)
          println("Skier", id, ": Ouch! I crashed! Health:", health);
        if(health == 0){
          println("Skier", id, "died after", runs, "runs! ... :(");
          running = false;
        }
      }

      if(time == 0){
        println("Skier", id, "Completed run", runs, "with", health, "health");
        status = SkiingActivity.updateStatus(status);
        runs++;
      }
    }
    else if(status.equals("Idle")){
      pause(.1);
      status = SkiingActivity.updateStatus(status);
    }
  }
}