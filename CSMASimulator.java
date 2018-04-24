import java.util.Scanner;


public class CSMASimulator 
{
	final static int NUM_STATIONS = 8;
	static float[] dataReadyProbabilities;
	static float transmissionTimeWeight;
	static int numPackets;
	static Scanner scanner;
	static StationLogger logger;
	static int logLevel;
	
	// Main method
	public static void main(String args[])
	{
		logLevel = 1;
		if (args.length > 0 && args[0].equals("-d"))
		{
			logLevel = 0;
		}
		getParameters();
		WirelessNetwork wirelessNetwork = createWirelessNetwork(NUM_STATIONS, dataReadyProbabilities, 
				transmissionTimeWeight, numPackets, logLevel);
		float averageTotalTime = wirelessNetwork.getAverageTotalTime();
		System.out.println("Average total time: " + averageTotalTime);
	}
	
	// Prompts the user for running parameters and sets them
	public static void getParameters()
	{
		scanner = new Scanner(System.in);
		dataReadyProbabilities = new float[8];
		float prob;
		for (int i = 0; i < NUM_STATIONS; i++)
		{
			do 
			{
				System.out.print("Data ready probability for Station "
						+ i + " (between 0 and 1): ");
				prob = scanner.nextFloat();
			}
			while (prob <= 0 || prob >= 1);
			dataReadyProbabilities[i] = prob;
			System.out.println();
		}
		do
		{
			System.out.print("Transmission time weight (between 0.3 and 0.6): ");
			transmissionTimeWeight = scanner.nextFloat();
		}
		while (transmissionTimeWeight <= 0.3 || transmissionTimeWeight >= 0.6);
		System.out.println();
		do
		{
			System.out.print("Number of packets (bewteen 1 and 6): ");
			numPackets = scanner.nextInt();
		}
		while (numPackets < 1 || numPackets > 6);
		System.out.println();
	}
	
	// Creates a WirelessNetwork object to execute WirelessStation tasks asynchronously
	public static WirelessNetwork createWirelessNetwork(int numStations, float[] dataReadyProbs, 
			float transmissionTimeWeight, int numPackets, int logLevel)
	{
		WirelessNetwork wirelessNetwork = new WirelessNetwork(numStations, dataReadyProbs, 
				transmissionTimeWeight, numPackets, logLevel);
		return wirelessNetwork;
	}

}
