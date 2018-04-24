
public class WirelessMedium 
{
	private boolean isBusy;
	
	// Represents the wireless medium, which is set to busy
	// when a wireless station is accessing it and idle otherwise
	public WirelessMedium()
	{
		isBusy = false;
	}
	
	// Returns whether the WirelessMedium is busy
	public boolean isBusy()
	{
		return isBusy;
	}
	
	// Sets the WirelessMedium to busy, 
	// making sure another thread cannot access this method while doing so
	public synchronized void setBusy()
	{
		isBusy = true;
	}
	
	// Sets the WirelessMedium to idle, 
	// making sure another thread cannot access this method while doing so
	public synchronized void setIdle()
	{
		isBusy = false;
	}
	
}
