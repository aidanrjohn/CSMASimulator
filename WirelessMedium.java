
public class WirelessMedium 
{
	private boolean isBusy;
	
	public WirelessMedium()
	{
		isBusy = false;
	}
	
	public boolean isBusy()
	{
		return isBusy;
	}
	
	public synchronized void setBusy()
	{
		isBusy = true;
	}
	
	public synchronized void setIdle()
	{
		isBusy = false;
	}
	
}
