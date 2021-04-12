import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MyFTP_Server {
	private int controlPort = 21;
	private ServerSocket ss;
	boolean serverRunning = true;
	
	public static void main(String[] args) {
		new MyFTP_Server();
	}
	
	public MyFTP_Server() {
		try {
			ss = new ServerSocket(controlPort);
			InetAddress myHost = InetAddress.getLocalHost();
    		System.out.println(myHost.getHostAddress()); //192.168.227.1
    		System.out.println(myHost.getHostName());    //PC
    		System.out.println("FTP Server started listening on port " + controlPort);
		}
		catch(IOException e) {
			System.out.println("Could not create server socket"); 
            System.exit(-1);
		}
		
		int noOfThreads = 0;
		
		while(serverRunning) {
			try {
				Socket client = ss.accept();
				
				 // Port for incoming dataConnection (for passive mode) is the controlPort + number of created threads + 1
                int dataPort = 6000 + noOfThreads + 1;
                
             // Create new worker thread for new connection
                MyFTP_Server_Worker w = new MyFTP_Server_Worker(client, dataPort);
                
                System.out.println("New connection received. Worker was created. Data port "+dataPort);
                noOfThreads++;
                w.start();
			}
			catch (IOException e){
                System.out.println("Exception encountered on accept");  
                e.printStackTrace();
            }  
		}
		
		
		try {
			 ss.close();
            System.out.println("Server was stopped"); 
		}catch (IOException e){
            System.out.println("Problem stopping server"); 
            System.exit(-1);
        }
	}
}
