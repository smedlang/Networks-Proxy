import java.util.HashMap;

/**
 *	DNS Cache Class
 * 	Savi Medlang 

 */
public class Cache {
	// A map to store cache entries
	private static HashMap<String, CacheStorage> cache = new HashMap<String, CacheStorage>();

	/**
	 * 
	 * Adds a cached entry if it does not exist in the HashMap
	 * 
	 * @param host hostname
	 * @param IP the value to search the HashMap
	 */
	public static void add(String host, String ip){
		if(!cache.containsKey(host)){
			cache.put(host, new CacheStorage(ip));
		}
	}
	
	/**
	 * 
	 * Returns IP address or hostname if IP does not exist
	 * 
	 * @param host the hostname to check
	 * @return cached IP or hostname when IP is not found
	 */
	public static String getHostNameOrIP(String host) {
		CacheStorage entry = cache.get(host);
		if(entry != null && entry.lessThan30Sec()){
			return entry.getIP();
		} else {
			if(entry != null){
				/* removes the entry if it's more than 30 seconds old OR if it's null */
				cache.remove(host);
			}
			return host;
		}
	}
	
}