/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ernie.ripley;

import static kiss.API.*;
import java.util.ArrayList;
/**
 *
 * @author Ernie
 */
public class Clock implements Comparable<Clock> {
    //instance variable: (1 for each object)
    private double hours;
    private double minutes;
    private double seconds;
    private boolean started=false;
    private double t0;

    Clock(){
      setHours(0);
    }
    Clock(double _hours){
      setHours(_hours);
    }

    Clock(double _hours, double _minutes){
      setHours(_hours + _minutes);
    }
   
  @Override
  public int compareTo(Clock clock1){
		if(this.hours == clock1.hours)
			return 0;
		else if(this.hours < clock1.hours)
			return -1;
		else
			return 1;
  }
   
   void start() {
        started = true;
        t0=time();

    }

    void setHours(double _hours) {
        hours=_hours;
        //look up the floor operation later.
        minutes = (hours-Math.floor(hours))*60.0;
        seconds = (minutes-Math.floor(minutes))*60.0;

    }
    double getHours() {
        //trinary operator. if (started) {return hours + (time()-t0)/3600.0; else {return hours;}
        return started ? (hours + (time() -t0)/3600.0) : hours;
    }
    double getMinutes() {
        double _hours = getHours();
        return (_hours-Math.floor(_hours))*60.0;
    }
    double getSeconds() {
        double _minutes = getMinutes();
        return (_minutes-Math.floor(_minutes))*60.0;
    }

    void testGetTime() {
        Clock clock = new Clock();
        double hours = clock.getHours();
        double minutes = clock.getMinutes();
        double seconds = clock.getSeconds();

    }

    void testGetCorrectTime() {
        Clock clock = new Clock();
        clock.setHours(6.50);
        assert clock.getHours() == 6.50;
        assert clock.getMinutes() == 30.0;
        assert clock.getSeconds() == 0.0;


    }

    void testGetFlowingTime(){
      Clock clock = new Clock();
      clock.setHours(1.00);
      clock.start();
      pause(1.0);
      double now = clock.getHours();
      double shouldBe = 1.00 + 1.0/3600.0;
      assert abs(now-shouldBe) < (0.1/3600.0);
    }

    @Override
    public boolean equals(Object object){
      if(object instanceof Clock){
        Clock clock = (Clock)object;
        return getHours() == clock.getHours();
      }
      else{
        return false;
      }
    }

    void testEquals(){
      Clock clock1 = new Clock();
      Clock clock2 = clock1;
      Clock clock3 = new Clock();

      clock1.setHours(1.00);
      clock3.setHours(1.00);
      assert clock2.getHours() == 1.00;
      assert clock1.equals(clock2);
      assert clock1.equals(clock3) == true;
      assert (clock1 == clock3) == false;
    }

  @Override
  public String toString(){
    return String.format("%02d:%02d", (int) getHours(), (int) getMinutes());
  }

  void testSortClocks(){
    ArrayList<Clock> clocks = new ArrayList<Clock>();
    clocks.add(new Clock(1));
    clocks.add(new Clock(1, 30));
    clocks.add(new Clock(2));
    clocks.add(new Clock(1, 15));
    clocks.sort(null);
    for(Clock clock : clocks){
      println(clock);
    }
  }
}
