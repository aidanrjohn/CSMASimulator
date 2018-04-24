
public class StationLogger 
{
	final int DEBUG_LEVEL = 0;
	final int STANDARD_LEVEL = 1;
	int currentLogLevel;
	public StationLogger(int currentLogLevel)
	{
		this.currentLogLevel = currentLogLevel;
	}
	public void logMessage(int logLevel, String stationName, String message)
	{
		if (logLevel == STANDARD_LEVEL || currentLogLevel == DEBUG_LEVEL)
		{
			System.out.println(stationName + ": " + message);
		}
	}
}
