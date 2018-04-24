import java.util.Random;
import java.util.concurrent.Callable;

public class WirelessStation implements Callable
{
	private StationLogger logger;
	private WirelessMedium wirelessMedium;
	private Random rand;
	private int randCeiling;
	private int numPackets;
	private String stationName;
	private final static int UNIT_TIME_MS = 100;
	private final int DIFS = UNIT_TIME_MS * 30;
	private final int DATA_READY_TIME = UNIT_TIME_MS * 240;
	private final int ACK_RECEIVE_TIME = 10 * UNIT_TIME_MS;
	private int totalTime;
	private int congestionWindowTime;
	private int packetWaitTime;
	private int k;
	private int W; // congestion window
	
	public WirelessStation(WirelessMedium wirelessMedium, String stationName, 
						   float probDataReady, float transmissionTimeWeight, 
						   int numPackets, int congestionWindow, int logLevel)
	{
		this.wirelessMedium = wirelessMedium;
		logger = new StationLogger(logLevel);
		this.stationName = stationName;
		rand = new Random();
		randCeiling = (int)(100 * probDataReady);
		this.numPackets = numPackets;
		totalTime = 0;
		k = 1;
		W = congestionWindow;
		congestionWindowTime = 0;
		packetWaitTime = (int)(transmissionTimeWeight * DATA_READY_TIME);
	}
	
	// Execution of WirelessStation tasks
	// TODO: Better logging for debug/program
	// TODO: Clean up code (nesting!)
	// TODO: Output graph?
	public Integer call() 
	{
		logger.logMessage(1, stationName, "running");
		do
		{
			sleep();
		}
		while (isDataReady() == false);	
		for (int i = 0; i < numPackets; i++)
		{
			while (isMediumBusy())
			{
				waitForMedium();
			}
			difsWait();
			congestionWindowTime = k*W;
			do
			{
				congestionWindowWait();
				if (isMediumBusy())
				{
					do
					{
						waitForMedium();
					}
					while (isMediumBusy());
					difsWait();
					if (congestionWindowTime <= 0)
					{
						k *= 2;
						if (k > 16) k = 16;
						congestionWindowTime = k * W;
					}
				}
			}
			while (congestionWindowTime > 0);
			wirelessMedium.setBusy();
			packetSendWait();
			packetAckWait();
			wirelessMedium.setIdle();
			difsWait();
		}
		displayTotalTime();
		return totalTime;
	}
	
	// Makes station sleep for 240tS
	public void sleep()
	{
		logger.logMessage(0, stationName, "sleeping for 240tS");
		try 
		{
			Thread.sleep(DATA_READY_TIME);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	// Makes station sleep for tS
	public void waitForMedium()
	{
		logger.logMessage(0, stationName, "waiting for medium for tS");
		try 
		{
			Thread.sleep(UNIT_TIME_MS);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	// Makes station sleep for tDIFS
	public void difsWait()
	{
		logger.logMessage(0, stationName, "waiting for tDIFS");
		totalTime += DIFS;
		try
		{
			Thread.sleep(DIFS);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	// Simulates the time it takes to send a packet
	public void packetSendWait()
	{
		logger.logMessage(0, stationName, "waiting for tP");
		totalTime += packetWaitTime;
		try
		{
			Thread.sleep(packetWaitTime);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	// Simulates the time it takes to receive an ACK packet
	public void packetAckWait()
	{
		logger.logMessage(0, stationName, "waiting for tIFS");
		totalTime += ACK_RECEIVE_TIME;
		try
		{
			Thread.sleep(ACK_RECEIVE_TIME);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	// Waits for tS and subtracts tS from tCW
	public void congestionWindowWait()
	{
		totalTime += UNIT_TIME_MS;
		congestionWindowTime -= UNIT_TIME_MS;
		waitForMedium();
	}
	
	// Simulates whether a packet is ready for sending based on probability
	public boolean isDataReady()
	{
		logger.logMessage(0, stationName, "checking for data to send...");
		int randNumber = rand.nextInt(100);
		boolean isInRange = randNumber < randCeiling;
		if (isInRange)
		{
			logger.logMessage(0, stationName, "is ready to send data.");
		}
		else
		{
			logger.logMessage(0, stationName, "does not have data to send");
		}
		return isInRange;
	}
	
	// Checks whether the wireless medium is currently being accessed
	public boolean isMediumBusy()
	{
		logger.logMessage(0, stationName, "is checking medium status...");
		boolean mediumBusy = wirelessMedium.isBusy();
		if (mediumBusy)
		{
			logger.logMessage(0, stationName, "ran into busy medium.");
		}
		else
		{
			logger.logMessage(0, stationName, "found idle medium.");
		}
		return mediumBusy;
	}
	
	// Used for getting tS from outside classes
	public static int getUnitTime()
	{
		return UNIT_TIME_MS;
	}
	
	// Displays the total time used to send data
	public void displayTotalTime()
	{
		logger.logMessage(1, stationName, "finished sending data. Total Time: " + totalTime);
	}
	
}
