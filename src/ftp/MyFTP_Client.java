package ftp;

//import java.awt.Color;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
//import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.Scanner;

//import javax.swing.JButton;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JPasswordField;
import javax.swing.JTextArea;
//import javax.swing.JTextField;


public class MyFTP_Client extends Thread{
	//Server information
//	String IpAddress = "192.168.227.1";
	String IpAddress;
	int controlPort = 21;
	
	String[] files;
	// Path information
	private String root;
    private String currDirectory;
    private String fileSeparator = "/";
	    
	//CONTROL connection
    private Socket controlConnection;
    private PrintWriter controlOutWriter;
    
    //DATA connection
    private ServerSocket dataSocket;
	private Socket dataConnection;
    private PrintWriter dataOutWriter;
    private enum transferType {
        ASCII, BINARY
    }
    private transferType transferMode = transferType.ASCII;
    

    
    //
//    private String command ;
    boolean socketControlRunning = true;
    private JTextArea textAreaLog;
    String result;
    String resultData;
   
    
//	public static void main(String[] args) {
//		new MyFTP_Client();
//		MyFTP_Client mc = new MyFTP_Client();
//		mc.openControlConnection();
//        
//		mc.start();
//	}
    
	
	public MyFTP_Client(JTextArea textAreaLog, String IpAddress) {
		this.IpAddress = IpAddress;
		this.textAreaLog = textAreaLog;
		this.currDirectory = System.getProperty("user.dir") + "/Ftp_client";
        this.root = System.getProperty("user.dir");        
	}
	
	public void cmd(String command) {
		try {
			// Output to client, automatically flushed after each print
            controlOutWriter = new PrintWriter(controlConnection.getOutputStream(), true);
            controlOutWriter.println(command);
            System.out.println("");
            textAreaLog.append("\n");
            executeCommand(command);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Cannot send to server");
		}
	}
	
	public void run() {
		try {
			while(true) {
				BufferedReader controlIn = new BufferedReader(new InputStreamReader(controlConnection.getInputStream()));		
				result = controlIn.readLine();
				if (result == null) {
					System.out.println("---------Disconnection---------");
					closeControlConnection();
					System.exit(0);
				}
				debugOutput("Getting from server: "+result);	
				
				checkPassiveConnect(result);
			}
		}catch(Exception e) {
			System.out.println(e);
			System.out.println("Cannot read from server");
		}
	}
	
	public void openControlConnection() {
		try {
			controlConnection = new Socket(IpAddress,controlPort);
			debugOutput("Open control connection");
		}catch(Exception e) {
			debugOutput("Cannot open Control Connection");
		}
	}
	
	public void openDataConnectionPassive(int dataPort) {
		try {
			dataConnection = new Socket(IpAddress,dataPort);
			dataOutWriter = new PrintWriter(dataConnection.getOutputStream(), true);

			debugOutput("Open data connection");
		}catch(Exception e) {
			debugOutput("Cannot create data connection");
		}
	}
	
	public void openDataConnectionActive(int dataPort) {
		try{
			dataSocket = new ServerSocket(dataPort);
            dataConnection = dataSocket.accept();
            dataOutWriter = new PrintWriter(dataConnection.getOutputStream(), true);
            debugOutput("Open data connection");
        }catch(IOException e){
            debugOutput("Could not create data connection.");
            e.printStackTrace();
        }
	}
	
	public void closeDataConnection() {
		try {
			dataOutWriter.close();
			dataConnection.close();
		} catch (IOException e) {
			System.out.println("Cannot close data connection");
			e.printStackTrace();
		}
	}
	
	public void closeControlConnection() {
		try {
			controlConnection.close();
			controlOutWriter.close();
		} catch (IOException e) {
			System.out.println("Cannot close control connection");
			e.printStackTrace();
		}
	}
	
	private void executeCommand(String c){
    	debugOutput("------- "+c+" -------");
        int index = c.indexOf(' ');
        String command = ((index == -1)? c.toUpperCase() : (c.substring(0, index)).toUpperCase());
        String args = ((index == -1)? null : c.substring(index+1, c.length()));
//        debugOutput("Command: " + command + " Args: " + args);
        
        switch(command)  {   
            case "STOR":
            	if (hdlSendFile(args)) {
	            	debugOutput("Send file successfully");
	            }
            	else {
            		debugOutput("Cannot send file");
            	}
                break;
            case "TYPE":
            	handleType(args);
            	break;
            case "PORT":
            	checkActiveConnect(args);
            	break;
            case "RETR":
            	hdlReceiveFile(args);
            	break;
            case "LIST":
            	hdlList(args);
            	break;
            default:
                debugOutput("501 Unknown command");
                break;
        }
    }
	
	public void checkPassiveConnect(String result) {
		if (result.contains("227 Entering Passive Mode")) {
			//227 Entering Passive Mode (10,10,42,222,19,137)
			String[] stringSplit = result.split("\\(");										
			stringSplit[1] = stringSplit[1].substring(0,stringSplit[1].length()-1);
			String[] numSplit = stringSplit[1].split("\\,");

			int num1 = Integer.parseInt(numSplit[4]);
			int num2 = Integer.parseInt(numSplit[5]);
			
			int dataPort = num1*256+num2;
			openDataConnectionPassive(dataPort);
		}
	}
	
	private void checkActiveConnect(String result) {
		String[] numSplit = result.split("\\,");
//		int num1 = Integer.parseInt(numSplit[1]);
//		int num2 = Integer.parseInt(numSplit[2]);
//		int dataPort = num1*256+num2;
		int dataPort = Integer.parseInt(numSplit[1]);
		
		openDataConnectionActive(dataPort);
		
	}
	
	private void handleType(String mode){
        if(mode.toUpperCase().equals("A")){
            transferMode = transferType.ASCII;
            debugOutput("Change type ASCII successfully");
        }
        else if(mode.toUpperCase().equals("I")){
            transferMode = transferType.BINARY;
            debugOutput("Change type BINARY successfully");
        }
        else
        	debugOutput("Change type unsuccessfully");
    }
	
	public boolean hdlSendFile(String file) {
//		File f =  new File(currDirectory + fileSeparator + file);
		File f =  new File(file);
        if(!f.exists()){
        	debugOutput("550 File does not exist");
            return false;
        }
        
        // Binary mode
        if (transferMode == transferType.BINARY){
            BufferedOutputStream fout = null;
            BufferedInputStream fin = null;
            
//            debugOutput("150 Opening binary mode data connection for requested file " + f.getName());
            
            try{
                //create streams
                fout = new BufferedOutputStream(dataConnection.getOutputStream());
                fin = new BufferedInputStream(new FileInputStream(f));
            }
            catch (Exception e){
                debugOutput("Could not create file streams");
                return false;
            }
                
//            debugOutput("Starting file transmission of " + f.getName());
            
            // write file with buffer
            byte[] buf = new byte[1024];
            int l = 0;
            try{
                while ((l = fin.read(buf,0,1024)) != -1){
                    fout.write(buf,0,l);
                }
            }catch (IOException e){
                debugOutput("Could not read from or write to file streams");
                e.printStackTrace();
                return false;
            }
            
            //close streams
            try{
                fin.close();
                fout.close();
            }catch (IOException e){
                debugOutput("Could not close file streams");
                e.printStackTrace();
                return false;
            }
//            debugOutput("Completed file transmission of " + f.getName());
        }
        
        // ASCII mode
        else{
//        	debugOutput("Opening ASCII mode data connection for requested file " + f.getName());
            BufferedReader rin = null;
            PrintWriter rout = null;
            try{
                rin = new BufferedReader(new FileReader(f));
                rout = new PrintWriter(dataConnection.getOutputStream(),true);           
            }catch (IOException e){
                debugOutput("Could not create file streams");
                return false;
            }
            String s;
            try{
                while((s = rin.readLine()) != null){
                    rout.println(s);
                }
            }catch (IOException e){
                debugOutput("Could not read from or write to file streams");
                e.printStackTrace();
                return false;
            }           
            try{
                rout.close();
                rin.close();
            }catch (IOException e){
                debugOutput("Could not close file streams");
                e.printStackTrace();
                return false;
            }
//            debugOutput("File transfer ASCII successful. Closing data connection.");         
        }
        closeDataConnection();  
        return true;
    }
	public void hdlReceiveFile(String file){
        if (file == null){
            debugOutput("No filename given");
        }
        else{
            File f =  new File(currDirectory + fileSeparator + file);
            if(f.exists()){
            	debugOutput("File da ton tai");
            }
            else{
                // Binary mode
                if (transferMode == transferType.BINARY){                
                	BufferedOutputStream fout = null;
                    BufferedInputStream fin = null;
//                    debugOutput("Opening binary mode data connection for requested file " + f.getName());
                    try{
                        // create streams
                        fout = new BufferedOutputStream(new FileOutputStream(f));
                        fin = new BufferedInputStream(dataConnection.getInputStream());
                    }
                    catch (Exception e){
                        debugOutput("Could not create file streams");
                    }
//                    debugOutput("Start receiving file " + f.getName());
                    // write file with buffer
                    byte[] buf = new byte[1024];
                    int l = 0;
                    try{
                        while ((l = fin.read(buf,0,1024)) != -1){
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
//                    debugOutput("Completed receiving file " + f.getName());
//                    debugOutput("File transfer successful. Closing data connection.");
                }
                // ASCII mode
                else{
//                    debugOutput("Opening ASCII mode data connection for requested file " + f.getName());
                    BufferedReader rin = null;
                    PrintWriter rout = null;
                    try{
                        rin = new BufferedReader(new InputStreamReader(dataConnection.getInputStream()));
                        rout = new PrintWriter(new FileOutputStream(f),true);
                    }
                    catch (IOException e){
                        debugOutput("Could not create file streams");
                    }
                    String s;
                    try{
                        while((s = rin.readLine()) != null){
//                        	debugOutput("Read line from file: "+s);
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
//                    debugOutput("File transfer successful. Closing data connection.");
                }
            }
            closeDataConnection();
        }
	}
	public void debugOutput(String name) {
    	System.out.println(name);
    	textAreaLog.append(name+"\n");
    }
	
	public void hdlList(String args) {
		if(result.indexOf('?') >= 0) {
			int i = 0;
			files=result.split("\\?");
			debugOutput("======= File from server u can download ======");
			for(String w:files){
				i++;
				debugOutput(i+". "+w);
			}
		}
	}
	
	public String[] getFile() {
		return files;
	}
        
}
