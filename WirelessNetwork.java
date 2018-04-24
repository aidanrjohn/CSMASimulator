import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WirelessNetwork 
{
	private Set<Future<Integer>> stationSet;
	private ExecutorService pool;
	private WirelessMedium wirelessMedium;
	private Random rand;
	private int numStations;
	
	// Creates a WirelessNetwork object, initiating a thread pool of WirelessStations
	public WirelessNetwork(int numStations, float[] dataReadyProbs, 
						   float transmissionTimeWeight, int numPackets,
						   int logLevel)
	{
		if (dataReadyProbs.length != numStations)
		{
			throw new InvalidParameterException("Stations and probablities " +
					"should be a 1-to-1 relation");
		}
		if (transmissionTimeWeight < 0.3 || transmissionTimeWeight > 0.6)
		{
			throw new InvalidParameterException("transmissionTimeWeight should be " +
					"between 0.3 and 0.6");
		}
		this.numStations = numStations;
		wirelessMedium = new WirelessMedium();
		pool = Executors.newFixedThreadPool(numStations);
		stationSet = new HashSet<Future<Integer>>();
		int unitTime = WirelessStation.getUnitTime();
		int ceilingTime = unitTime * 2 * numStations;
		rand = new Random();
		// generate integer in range (tS, 2Nts)
		int congestionWindow = rand.nextInt(ceilingTime - (unitTime - 1)) + (unitTime + 1);
		for (int i = 0; i < numStations; i++)
		{
			String stationName = "Station-" + i;
			float dataReadyProb = dataReadyProbs[i];
			@SuppressWarnings("unchecked")
			Callable<Integer> station = 
					new WirelessStation(wirelessMedium, stationName, 
										dataReadyProb,transmissionTimeWeight, 
										numPackets, congestionWindow, logLevel);
			Future<Integer> stationFuture = pool.submit(station);
			stationSet.add(stationFuture);
		}
		
	}
	
	// Returns the average total time for the WirelessStations once their tasks are completed
	public float getAverageTotalTime()
	{
		int sum = 0;
		for (Future<Integer> future : stationSet)
		{
			try 
			{
				sum += future.get();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			} 
			catch (ExecutionException e) 
			{
				e.printStackTrace();
			}
		}
		float average = sum / numStations;
		pool.shutdown();
		return average;
	}

}
