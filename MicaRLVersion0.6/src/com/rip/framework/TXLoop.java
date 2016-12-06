package com.rip.framework;

public class TXLoop implements Runnable
{
	Thread routineThread = null;
	volatile boolean running = false;
	TXRoutine routine;

	public TXLoop(TXRoutine routine)
	{
		this.routine = routine;
	}

	public void resume()
	{
		routineThread = new Thread(this);
		routineThread.start();
		running = true;
		routine.resume();
	}

	@Override
	public void run()
	{
		while(running)
		{
			routine.update();
		}
	}

	public void pause()
	{
		routine.pause();
		running = false;
		while(true)
		{
			try
			{
				routineThread.join();
				return;
			}
			catch(InterruptedException e)
			{
				//retry
			}
		}
	}
}