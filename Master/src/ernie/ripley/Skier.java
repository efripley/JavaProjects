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

  Skier(int argId, Lift argLift){
    println("Skier", argId, "Has Arrived!");
    running = true;
    health = 10;
    status = SkiingActivity.initialStatus();
    trail = "";
    id = argId;
    runs = 0;
    time = 0;
    lift = argLift;
  }

  public void run(){
    while(running){
      update();
    }
  }

  public void update(){
    if(status == "Waiting For Lift"){
      pause(1);
      if(lift.available()){
        lift.board();
        status = SkiingActivity.updateStatus(status);
        trail = lift.getTrail(random(5));
        time = 0;
        println("Skier", id, ": I boarded the ski lift and I'm heading to", trail, "trail.");
      }
    }
    else if(status.equals("Riding Lift")){
      pause(1);
      time++;
      if(lift.getTrail(time).equals(trail)){
        status = SkiingActivity.updateStatus(status);
        println("Skier", id, ": Finaly made it to", trail, "YeeHaa!");
      }
    }
    else if(status == "Skiing"){
      pause(1);
      time--;

      if(random(5) == 0){
        health--;
        println("Skier", id, ": Ouch! I crashed! Health:", health);
        if(health == 0){
          println("Skier", id, "died after", runs, "runs! ... :(");
          running = false;
        }
      }

      if(time == 0){
        println("Skier", id, "Ooooo, lets do it again");
        status = SkiingActivity.updateStatus(status);
        runs++;
      }
    }
    else if(status.equals("Idle")){
      pause(1);
      status = SkiingActivity.updateStatus(status);
    }
  }
}