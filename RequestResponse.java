import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Creates Client to Server Connections
 * @author Savi Medlang
 *
 */
public class RequestResponse extends Thread {
	
	private byte[] requestBuffer = new byte[2048];
	private Socket clientSocket = null;
	private Socket serverSocket = null;
	
	/* CONSTRUCTOR FOR INITIALIZING CLIENT SOCKET */
	public RequestResponse(Socket socket){
		clientSocket = socket;
	}
	
	public void run(){
		try{
			/* READ REQUEST FROM CLIENT */
			BufferedInputStream from_client = new BufferedInputStream(clientSocket.getInputStream());
			from_client.read(requestBuffer, 0, 2048);
			
			/* MODIFY THE REQUEST HEADER */
			requestBuffer = setConnection(requestBuffer);
			
			// Get the InetAddress with destination hostname
			//InetAddress address = InetAddress.getByName(getHost(requestBuffer));
			
			String host = getHost(requestBuffer);
			// Get from cache or use host name
			
			host = Cache.getHostNameOrIP(host);
			
			/* CREATE SERVER SOCKET ON PORT 80 */
			serverSocket = new Socket(host, 80);
			
			Cache.add(host, serverSocket.getInetAddress().getHostAddress());
			
			/* WRITE REQUEST TO SERVER */
			DataOutputStream serverOutput = new DataOutputStream(serverSocket.getOutputStream());
			serverOutput.write(requestBuffer);
			serverOutput.flush();
			
			/* READ REPLY FROM SERVER, WRITE TO CLIENT */
			BufferedInputStream from_server = new BufferedInputStream(serverSocket.getInputStream());
			DataOutputStream to_server = new DataOutputStream(clientSocket.getOutputStream());
			int readByte = from_server.read();
			while(readByte != -1){
				to_server.write(readByte);
				to_server.flush();
				readByte = from_server.read();
			}
			
			/* CLOSE CONNECTIONS */
			clientSocket.close();
			serverSocket.close();
		}
		catch(Exception e){
			e.getStackTrace();
		}
	}
	
	/**
	 * helper method to find the hostname 
	 * @param request the header byte array
	 * @return the hostname 
	 */
	private String getHost(byte[] request){
		// find dest. address
		String inputString = new String(request, StandardCharsets.UTF_8);
		String[] splitInput = inputString.split(" ");
		
		// find dest. address
		String fullHostName = splitInput[1];
		URL url = null;
		
		// find hostname
		try {
			url = new URL(fullHostName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(url.getHost());
		return url.getHost();
	}
	
	/**
	 * helper method to modify the header
	 * @param input the byte array
	 * @return modified byte array
	 */
	private byte[] setConnection(byte[] input){

		String inputToString = new String(input, StandardCharsets.UTF_8);
		
		/* Changes each Connection: keep-alive to Connection: close */
		String modifiedHeader = inputToString.replaceAll("Connection: keep-alive" , "Connection: close");
		
		// String into bytes
		return modifiedHeader.getBytes(StandardCharsets.UTF_8);
	}
}