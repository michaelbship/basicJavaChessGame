//this class makes a timer for the game
public class GameTimer implements Runnable
{
	public static Thread gameTimer;
	public boolean running = true;
	
	public GameTimer()
	{
		gameTimer = new Thread(this);
		gameTimer.start();
	}
	
	@Override
	public void run() //while the thread is running increment the timer
	{
		int sec = 0;
		long seconds = 0;
		long beginTime = System.currentTimeMillis();
		String secStr = "0";
		String minStr = "0";
		String hourStr = "0";
		String time;
		
		while(running)
		{
			secStr = "" + seconds % 60;
			if(seconds % 60 < 10)
				secStr = "0" + secStr;
			
			minStr = "" + (seconds / 60) % 60;
			if(((seconds / 60) % 60)< 10)
				minStr = "0" + minStr;
			
			hourStr = "" + seconds / 3600;
			if((seconds / 3600) < 10)
				hourStr = "0" + hourStr;
			
			time = "" + hourStr + ":" + minStr + ":" + secStr; //make the timer string
			
			sec = (int) ((System.currentTimeMillis() - beginTime) / 1000);
			
			if(sec > seconds)
			{
				seconds++;
				Menu.timerLabel.setText(time); //change the time
			}
		}
	}
	
	//end the thread
	public synchronized void end()
	{
		if(!running)
			return;
		
		running = false;
	
		try{gameTimer.join();
		}catch (InterruptedException e) {e.printStackTrace();}
	}
}
