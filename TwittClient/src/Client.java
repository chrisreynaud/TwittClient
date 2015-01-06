import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client class: this class is in charge to contact Tweets server via socket,
 * to send request and to display response
 * 
 * @author creynaud
 *
 */
public class Client {

	// Constructor
	public Client(String operation, String param) {

		if (operation.equalsIgnoreCase("retreive")){
			// Get tweets
			System.out.println("Get tweets from client\n");
			this.listenSocket("tweets", null);
			
		} else if (operation.equalsIgnoreCase("account")) {
			// Get Tweets from account
			System.out.println("Get Tweets from account\n");
			this.listenSocket("account", param);
			
		} else if (operation.equalsIgnoreCase("followers")) {
			// Get followers
			System.out.println("Get followers\n");
			this.listenSocket("followers", param);
			
		} else {
			System.out.println("Bad parameters !!\n");
			System.exit(0);
		}
	}
	
	/**
	 * The main method for client : call Tweets server by socket to do:
	 *  - get a list of tweets (limit fixed by the server)
	 *  - get a list of tweets for a specific account (limit fixed by the server)
	 *  - get a list a followers for a specific account (limit fixed by the server)
	 * 
	 * @param args retreive/account/followers [account]
	 * 
	 */
	public static void main(String[] args) {
		String operation;
		String param;
		System.out.println("Start main client ...\n");
		if (args.length == 1){
			operation = args[0];
			System.out.println("Operation : " + operation + "\n");
			new Client(operation, null);
		} else if (args.length == 2){
			operation = args[0];
			param = args[1];
			System.out.println("Operation : " + operation + ", parameter : " + param + "\n");
			new Client(operation, param);
		} else {
			System.out.println("Usage: java Client retreive/account/followers [account]\n");
			System.exit(0);
		}
	}
	
	/**
	 * The listenSocket method for the client:
	 *  - open a socket connection to the server
	 *  - call the server to execute a request
	 *  - display the result
	 * When receive a "STOP" message from server, the socket is close and the programm is stopped
	 * 
	 * @param param1 retreive/account/followers
	 * @param param2 account, may be null
	 * @throws UnknownHostException
	 * @throws IOException
	 * 
	 */
	public void listenSocket(String param1, String param2) {
		Socket socketClient = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String line;
		
		// Create socket connection
		try {
			// Socket client
			socketClient = new Socket("localhost", 4321);
			
			// Parameters sent to server
			out = new PrintWriter(socketClient.getOutputStream(), true);
			if (param2 != null){
				param1 += "|" + param2;
			}
			out.println(param1);
			
			// Response from server
			in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
			while ((line = in.readLine()) != null )
			{
				if(line.equals("STOP")){
					System.out.println("STOP received");
					socketClient.close();
					break;
				} else {
					System.out.println(line);
				}
			}
			if (!socketClient.isClosed())
				socketClient.close();
			
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: localhost");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		}
	}
	
}
