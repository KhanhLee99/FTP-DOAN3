package ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class MyFTP_Server_Worker extends Thread {
	private boolean debugMode = true;
	private enum transferType {
        ASCII, BINARY
    }
	private enum userStatus {
        NOTLOGGEDIN, ENTEREDUSERNAME, LOGGEDIN
    }
	// Path information
	private String root;
	private String currDirectory;
	private String fileSeparator = "/";
	// control connection
	private Socket controlSocket;
	private PrintWriter controlOutWriter;
    private BufferedReader controlIn;
    // data Connection
    private ServerSocket dataSocket;
    private Socket dataConnection;
    private PrintWriter dataOutWriter;
    
    private int dataPort;
    private transferType transferMode = transferType.ASCII;
    // user properly logged in?
    private userStatus currentUserStatus = userStatus.NOTLOGGEDIN;
    private String validUser = "vietkhanh";
    private String validPassword = "vietkhanh";
    
    private boolean quitCommandLoop = false;
    
    /**
     * Create new worker with given client socket
     * @param client the socket for the current client
     * @param dataPort the port for the data connection
     */
	public MyFTP_Server_Worker(Socket client, int dataPort){
        super();
        this.controlSocket = client;
        this.dataPort = dataPort;
        // Get the home directory
        this.currDirectory = System.getProperty("user.dir") + "/Ftp_server";
        this.root = System.getProperty("user.dir");
        debugOutput("Construct successfully");
    }
	/**
     * Run method required by Java thread model
     */
    public void run(){
    	try{
            // Input from client
            controlIn = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
            
            // Output to client, automatically flushed after each print
            controlOutWriter = new PrintWriter(controlSocket.getOutputStream(), true);
            
            // Greeting
            sendMsgToClient("220 Welcome to the FTP-Server");
            
            // Get new command from client
            while (!quitCommandLoop){
            	String command = controlIn.readLine();
            	System.out.println("Command: "+command);
                executeCommand(command);
            }
                        
        }catch (Exception e){
            e.printStackTrace();
        }finally{ 
            // Clean up 
            try{                    
                controlIn.close(); 
                controlOutWriter.close(); 
                controlSocket.close(); 
                debugOutput("Sockets closed and worker stopped"); 
            }catch(IOException e){ 
                e.printStackTrace();
                debugOutput("Could not close sockets");
            } 
        }     
    }
    
    /**
     * Main command dispatcher method.
     * Separates the command from the arguments and dispatches it to single handler functions.
     * @param c the raw input from the socket consisting of command and arguments
     */
    private void executeCommand(String c){
    	
    	debugOutput("Excute Command "+c);
        // split command and arguments
        int index = c.indexOf(' ');
        String command = ((index == -1)? c.toUpperCase() : (c.substring(0, index)).toUpperCase());
        String args = ((index == -1)? null : c.substring(index+1, c.length()));

        debugOutput("Command: " + command + " Args: " + args);
        
        // dispatcher mechanism for different commands
        switch(command) {
            case "USER":                
                handleUser(args);
                break;
                
            case "PASS":
                handlePass(args);
                break;
                }
        //##########################
        /*if (currentUserStatus != userStatus.LOGGEDIN) {
    		sendMsgToClient("You must loggin first");
    		return;
    	}*/
        switch(command)  {   
                
            case "QUIT":
                handleQuit();
                break;
                
            case "PASV":
                handlePasv();
                break;
                
            case "PORT":
                handlePort(args);
                break;
            
            case "RETR":
                handleRetr(args);
                break;
                
            case "TYPE":
                handleType(args);
                break;
                
            case "STOR":
                handleStor(args);
                break;
            
            case "LIST":
                handleList(args);
                break;
                
            default:
                sendMsgToClient("501 Unknown command");
                break;
        }
    }
    
    /**
     * Sends a message to the connected client over the control connection.
     * Flushing is automatically performed by the stream.
     * @param msg The message that will be sent
     */
	private void sendMsgToClient(String msg) {
		controlOutWriter.println(msg);
	}
	
	/**
     * Send a message to the connected client over the data connection.
     * @param msg Message to be sent
     */
//    private void sendDataMsgToClient(String msg){
//        if (dataConnection == null || dataConnection.isClosed()){
//        	sendMsgToClient("425 No data connection was established");
//            debugOutput("Cannot send message, because no data connection is established");
//        }
//        else{
//            dataOutWriter.print(msg + '\r' + '\n');
//        }   
//    }
    
    /**
     * Open a new data connection socket and wait for new incoming connection from client.
     * Used for passive mode.
     * @param port Port on which to listen for new incoming connection
     */
    private void openDataConnectionPassive(int port){
        try{
            dataSocket = new ServerSocket(port);
            dataConnection = dataSocket.accept();
            dataOutWriter = new PrintWriter(dataConnection.getOutputStream(), true);
            debugOutput("Data connection - Passive Mode - established");
        }catch(IOException e){
            debugOutput("Could not create data connection.");
            e.printStackTrace();
        }
    }
    
    /**
     * Connect to client socket for data connection.
     * Used for active mode.
     * @param ipAddress Client IP address to connect to
     * @param port Client port to connect to
     */
    private void openDataConnectionActive(String ipAddress, int port){
        try{
            dataConnection = new Socket(ipAddress, port);
            dataOutWriter = new PrintWriter(dataConnection.getOutputStream(), true);
            debugOutput("Data connection - Active Mode - established");
        }catch (IOException e){
            debugOutput("Could not connect to client data socket");
            e.printStackTrace();
        }

    }
    
    /**
     * Close previously established data connection sockets and streams
     */
    private void closeDataConnection(){
        try{
            dataOutWriter.close();
            dataConnection.close();
            if (dataSocket != null){
                dataSocket.close();
            }
            debugOutput("Data connection was closed");
        }catch (IOException e){
            debugOutput("Could not close data connection");
            e.printStackTrace();
        }
        dataOutWriter = null;
        dataConnection = null;
        dataSocket = null;          
    }
    
    /**
     * Debug output to the console. Also includes the Thread ID for better readability.
     * @param msg Debug message
     */
	private void debugOutput(String msg) {
		if (debugMode){
            System.out.println("Thread " + this.getId() + ": " + msg);
        }
		
	}
	
	/**
     * ##################################################
     * 			HANDLER FUNCTION of FTP command 
     * ##################################################
     */
    
    /**
     * Handler for USER command.
     * User identifies the client.
     * @param username Username entered by the user
     */
    private void handleUser(String username){
    	if (username == null) {
    		sendMsgToClient("USER command must have an argument");
    		return;
    	}
        if (username.toLowerCase().equals(validUser)){
            sendMsgToClient("331 Username okay");
            currentUserStatus = userStatus.ENTEREDUSERNAME;
        }
        else if (currentUserStatus == userStatus.LOGGEDIN){
            sendMsgToClient("530 User already logged in");
        }
        else{
            sendMsgToClient("530 Not logged in");
        }
    }
    
    /**
     * Handler for PASS command.
     * PASS receives the user password and checks if it's valid.
     * @param password Password entered by the user
     */
    private void handlePass(String password){
        // User has entered a valid username and password is correct
        if (currentUserStatus == userStatus.ENTEREDUSERNAME && password.equals(validPassword)){
            currentUserStatus = userStatus.LOGGEDIN;
            sendMsgToClient("230 User logged in successfully");  
        }       
        // User is already logged in
        else if (currentUserStatus == userStatus.LOGGEDIN){
            sendMsgToClient("530 User already logged in");
        }    
        // Wrong password
        else{
            sendMsgToClient("530 Not logged in");
        }
    }


    /**
     * Handler for the PORT command.
     * The client issues a PORT command to the server in active mode, so the
     * server can open a data connection to the client through the given address
     * and port number.
     * @param args The first four segments (separated by comma) are the IP address.
     *        The last two segments encode the port number (port = seg1*256 + seg2)
     */
    private void handlePort(String args){
        // Extract IP address and port number from arguments
        String[] stringSplit = args.split(",");
//        String hostName = stringSplit[0] + "." + stringSplit[1] + "." + 
//                stringSplit[2] + "." + stringSplit[3];
        String hostName = stringSplit[0];
    
//        int p = Integer.parseInt(stringSplit[1])*256 + Integer.parseInt(stringSplit[2]);
        int p = Integer.parseInt(stringSplit[1]);
        // Initiate data connection to client
        openDataConnectionActive(hostName, p);
        sendMsgToClient("200 Command OK");
        
    }

    /**
     * Handler for PASV command which initiates the passive mode.
     * In passive mode the client initiates the data connection to the server.
     * In active mode the server initiates the data connection to the client.
     */
    private void handlePasv(){
        // Using fixed IP for connections on the same machine
        // For usage on separate hosts, we'd need to get the local IP address from somewhere
        // Java sockets did not offer a good method for this
    	InetAddress myHost;
		try {
			myHost = InetAddress.getLocalHost();		
	        //String myIp = "127.0.0.1";
			String myIp = String.valueOf(myHost.getHostAddress());
	        String myIpSplit[] = myIp.split("\\.");
	        System.out.println(myIp);
	        int p1 = dataPort/256;
	        int p2 = dataPort%256;
	        
	        sendMsgToClient("227 Entering Passive Mode ("+ myIpSplit[0] +"," + myIpSplit[1] + "," + myIpSplit[2] + "," + myIpSplit[3] + "," + p1 + "," + p2 +")");
	        
	        openDataConnectionPassive(dataPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
    }

    /**
     * Handler for the QUIT command.
     */
    private void handleQuit(){
        sendMsgToClient("221 Closing connection");
        quitCommandLoop = true;
    }
    
    /**
     * Handler for the TYPE command.
     * The type command sets the transfer mode to either binary or ascii mode
     * @param mode Transfer mode: "a" for Ascii. "i" for image/binary.
     */
    private void handleType(String mode){
        if(mode.toUpperCase().equals("A")){
            transferMode = transferType.ASCII;
            sendMsgToClient("200 OK");
        }
        else if(mode.toUpperCase().equals("I")){
            transferMode = transferType.BINARY;
            sendMsgToClient("200 OK");
        }
        else
            sendMsgToClient("504 Not OK");
            
    }

    /**
     * Handler for the RETR (retrieve) command.
     * Retrieve transfers a file from the ftp server to the client.
     * @param file The file to transfer to the user
     */
    private void handleRetr(String file){
        File f =  new File(currDirectory + fileSeparator + file);
        if(!f.exists()){
            sendMsgToClient("550 File does not exist");
            return;
        }
        // Binary mode
        if (transferMode == transferType.BINARY){
            BufferedOutputStream fout = null;
            BufferedInputStream fin = null;
            
            sendMsgToClient("150 Opening binary mode data connection for requested file " + f.getName());
            
            try{
                //create streams
                fout = new BufferedOutputStream(dataConnection.getOutputStream());
                fin = new BufferedInputStream(new FileInputStream(f));
            }
            catch (Exception e){
                debugOutput("Could not create file streams");
            }
                
            debugOutput("Starting file transmission of " + f.getName());
            
            // write file with buffer
            byte[] buf = new byte[1024];
            int l = 0;
            try{
                while ((l = fin.read(buf,0,1024)) != -1){
                	debugOutput("Sending... ");
                    fout.write(buf,0,l);
                }
            }catch (IOException e){
                debugOutput("Could not read from or write to file streams");
                e.printStackTrace();
            }
            
            //close streams
            try{
                fin.close();
                fout.close();
            }catch (IOException e){
                debugOutput("Could not close file streams");
                e.printStackTrace();
            }
                           
            debugOutput("Completed file transmission of " + f.getName());

            sendMsgToClient("226 File transfer successful. Closing data connection.");
        }
        
        // ASCII mode
        else{
            sendMsgToClient("150 Opening ASCII mode data connection for requested file " + f.getName());

            BufferedReader rin = null;
            PrintWriter rout = null;
            
            try{
                rin = new BufferedReader(new FileReader(f));
                rout = new PrintWriter(dataConnection.getOutputStream(),true);           
            }catch (IOException e){
                debugOutput("Could not create file streams");
            }
            
            String s;
            
            try{
                while((s = rin.readLine()) != null){
                	debugOutput("Read line rout: "+s);
                    rout.println(s);
                }
            }catch (IOException e){
                debugOutput("Could not read from or write to file streams");
                e.printStackTrace();
            }           
            try{
                rout.close();
                rin.close();
            }catch (IOException e){
                debugOutput("Could not close file streams");
                e.printStackTrace();
            }
            sendMsgToClient("226 File transfer successful. Closing data connection.");
        }
        closeDataConnection();   
    }

    /**
     * Handler for STOR (Store) command.
     * Store receives a file from the client and saves it to the ftp server.
     * @param file The file that the user wants to store on the server
     */
    private void handleStor(String file){
        if (file == null){
            sendMsgToClient("501 No filename given");
        }
        else{
        	File f1 = new File(file);
        	file = f1.getName();
            File f =  new File(currDirectory + fileSeparator + file);
            if(f.exists()){
                sendMsgToClient("550 File already exists");
            }
            else{
                // Binary mode
                if (transferMode == transferType.BINARY){
                	BufferedOutputStream fout = null;
                    BufferedInputStream fin = null;
                    sendMsgToClient("150 Opening binary mode data connection for requested file " + f.getName());

                    try{
                        // create streams
                        fout = new BufferedOutputStream(new FileOutputStream(f));
                        fin = new BufferedInputStream(dataConnection.getInputStream());
                    }
                    catch (Exception e){
                        debugOutput("Could not create file streams");
                        e.printStackTrace();
                    }

                    debugOutput("Start receiving file " + f.getName());

                    // write file with buffer
                    byte[] buf = new byte[1024];
                    int l = 0;
                    try{
                        while ((l = fin.read(buf,0,1024)) != -1){
                        	debugOutput("Writing file by Binary mode");
                            fout.write(buf,0,l);
                        }
                    }
                    catch (IOException e){
                        debugOutput("Could not read from or write to file streams");
                        e.printStackTrace();
                    }

                    //close streams
                    try{
                        fin.close();
                        fout.close();
                    }catch (IOException e){
                        debugOutput("Could not close file streams");
                        e.printStackTrace();
                    }

                    debugOutput("Completed receiving file " + f.getName());
                    sendMsgToClient("226 File transfer successful. Closing data connection.");
                }
                // ASCII mode
                else{
                    sendMsgToClient("150 Opening ASCII mode data connection for requested file " + f.getName());

                    BufferedReader rin = null;
                    PrintWriter rout = null;
                    try{
                        rin = new BufferedReader(new InputStreamReader(dataConnection.getInputStream()));
                        rout = new PrintWriter(new FileOutputStream(f),true);
                    }
                    catch (IOException e){
                        debugOutput("Could not create file streams");
                    }
                 
                    debugOutput("OK file ...");
                    try{
                    	String s;
                        while((s = rin.readLine()) != null){
//                        	debugOutput("OK file lala ...");
                            rout.println(s);
                            debugOutput("Writing file ...");
                        }
                    }catch (IOException e){
                        debugOutput("Could not read from or write to file streams");
                        e.printStackTrace();
                    }
                    try{
                        rout.close();
                        rin.close();
                    }catch (IOException e){
                        debugOutput("Could not close file streams");
                        e.printStackTrace();
                    }
                    debugOutput("File transfer successful");
                    sendMsgToClient("226 File transfer successful. Closing data connection.");
                }
            }
            closeDataConnection();
        }

    }
    public void handleList(String args) {
        File f = new File(currDirectory);
        String s = readFileString(f);
        System.out.println(s);
        sendMsgToClient(s);
    }
    
    public String readFileString(final File fileOrDir) {
        // check xem fileOrDir la file hay foder
        if (fileOrDir.isDirectory()) {
//            System.out.println(fileOrDir.getAbsolutePath());
            final File[] children = fileOrDir.listFiles();
            String s = "";
            for(int i=0; i<children.length; i++) {
            	s+=children[i].getName()+"?";
            }
            return s;
        } 
        return null;
    }
}
