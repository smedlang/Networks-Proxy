/**
 * 
 * Store IP addresses and TTL
 * 
 * @author Savi Medlang
 *
 */
public class CacheStorage {
	private String IP;
	private float timeCreated;
	
	/* CONVERTING TO NANOSEC */
	private static final double SEC_TO_NANO = (1.0 * 1000 * 1000 * 1000);	//1 x 10^9 s
	private static final int TTL_SEC = 30;
	
	/* constructor to create a cache using the IP address */
	public CacheStorage(String IP){
		this.IP = IP;
		timeCreated = System.nanoTime();
	}
	
	public String getIP() {
		return IP;
	}
	
	public void setIp(String IP) {
		this.IP = IP;
	}
	
	public float getTimeCreated() {
		return timeCreated;
	}
	
	public void setTimeCreated(float timeCreated) {
		this.timeCreated = timeCreated;
	}
	
	/**
	 * Checks if cache has expired
	 * @return true when still live, false for expired
	 */
	public boolean lessThan30Sec(){
		return System.nanoTime() - timeCreated < TTL_SEC * SEC_TO_NANO;
	}
}