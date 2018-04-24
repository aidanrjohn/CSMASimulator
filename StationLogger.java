
public class StationLogger 
{
	final int DEBUG_LEVEL = 0;
	final int STANDARD_LEVEL = 1;
	int currentLogLevel;
	
	// Creates a StationLogger object to log station events
	public StationLogger(int currentLogLevel)
	{
		this.currentLogLevel = currentLogLevel;
	}
	
	// Logs a message if the message has the same or higher log level as
	// the current level
	public void logMessage(int logLevel, String stationName, String message)
	{
		if (logLevel == STANDARD_LEVEL || currentLogLevel == DEBUG_LEVEL)
		{
			System.out.println(stationName + ": " + message);
		}
	}
}
