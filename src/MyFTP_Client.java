
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class MyFTP_Client extends Thread implements ActionListener{
	//Server information
	String IpAddress = "192.168.227.1";
	int controlPort = 21;
	
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
    
    //login
    private boolean checklogin = false;
    private JFrame jf;
    private static JLabel lbUsername, lbPassword, lbPort, lbServer, lbClient, lbFile;
   	private static JTextField username, port;
   	private static JPasswordField password;
   	private static JPanel pnHeader, pnLeft, pnServerReplay, pnTable, pnFL, pnFR;
   	private static JButton login, btnFile;
    //sendFile
    private static final long serialVersionUID = 1L;
    
    private JFrame jf2;
    private JLabel labelHost;
    private JTextField textFieldHost;
    private JLabel labelPort;
    private JTextField textFieldPort;
    private JButton btnBrowse;
    private JTextField textFieldFilePath;
    private JButton btnSendFile, btnPasv;
    private JTextArea textAreaResult;
    
    //
    private String command ;
    boolean socketControlRunning = true;
    private JTextArea textAreaLog;
    String result;
    String[] files;
   
    
//	public static void main(String[] args) {
//		new MyFTP_Client();
//		MyFTP_Client mc = new MyFTP_Client();
//		mc.openControlConnection();
//        
//		mc.start();
//		try {
//			while(true) {			
//				Scanner sc = new Scanner(System.in);
//				String command = sc.nextLine();
//				
//				
//				// Output to client, automatically flushed after each print
//	            mc.controlOutWriter = new PrintWriter(mc.controlConnection.getOutputStream(), true);
//	            mc.controlOutWriter.println(command);
//	            System.out.println("Create Control ****************");
//	            mc.executeCommand(command);
//	            
//				}
//
//				
//		}catch(Exception e) {
//			e.printStackTrace();
//			System.out.println("Cannot send to server");
//		}
//		finally {
//			mc.closeControlConnection();
//		}
//	}
    
//    public void getText(JTextArea textAreaLog) {
//    	this.textAreaLog = textAreaLog;
//    }
	
	public MyFTP_Client(JTextArea textAreaLog) {
//    public MyFTP_Client() {
		//
		this.textAreaLog = textAreaLog;
//		this.command = command;
//		formLogin();
//		sendFile();
		//
		this.currDirectory = System.getProperty("user.dir") + "/Ftp_client";
        this.root = System.getProperty("user.dir");        
//        try {
//	        InetAddress myHost = InetAddress.getLocalHost();
//			debugOutput(myHost.getHostAddress());
//        }catch(Exception e) {
//        	debugOutput("Cannot get IP Address from InetAddress");
//        }
//        openControlConnection();
//        
//		this.start();
//		try {
//			while(true) {			
//				Scanner sc = new Scanner(System.in);
//				String command = sc.nextLine();
//				
//				
//				// Output to client, automatically flushed after each print
//	            controlOutWriter = new PrintWriter(controlConnection.getOutputStream(), true);
//	            controlOutWriter.println(command);
//	            System.out.println("Create Control ****************");
//	            executeCommand(command);
//	            
//				}
//
//				
//		}catch(Exception e) {
//			e.printStackTrace();
//			System.out.println("Cannot send to server");
//		}
//		finally {
//			closeControlConnection();
//		}
	}
	
	public void cmd(String command) {
		try {
//			while(true) {			
//				Scanner sc = new Scanner(System.in);
//				String command = sc.nextLine();
					
				
				
				// Output to client, automatically flushed after each print
	            controlOutWriter = new PrintWriter(controlConnection.getOutputStream(), true);
	            controlOutWriter.println(command);
	            debugOutput("Create Control ****************");
	            executeCommand(command);
	            
//				}

//				System.out.println("Send to server");
				
//			}		
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Cannot send to server");
		}
//		finally {
//			closeControlConnection();
//		}
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
			System.out.println("Create Datasocket*******");
            dataConnection = dataSocket.accept();
            System.out.println("Accept Datasocket*******");
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
    	debugOutput("Excute Command "+c);
        // split command and arguments
        int index = c.indexOf(' ');
        String command = ((index == -1)? c.toUpperCase() : (c.substring(0, index)).toUpperCase());
        String args = ((index == -1)? null : c.substring(index+1, c.length()));

        debugOutput("Command: " + command + " Args: " + args);
        
        // dispatcher mechanism for different commands
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
            	
            case "LIST1":
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

		int num1 = Integer.parseInt(numSplit[4]);
		int num2 = Integer.parseInt(numSplit[5]);
		
		int dataPort = num1*256+num2;
		System.out.println("Create Checkactive*******");
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
//		debugOutput(file);
        if(!f.exists()){
        	debugOutput("550 File does not exist");
            return false;
        }
        
        // Binary mode
        if (transferMode == transferType.BINARY){
            BufferedOutputStream fout = null;
            BufferedInputStream fin = null;
            
            debugOutput("150 Opening binary mode data connection for requested file " + f.getName());
            
            try{
                //create streams
                fout = new BufferedOutputStream(dataConnection.getOutputStream());
                fin = new BufferedInputStream(new FileInputStream(f));
            }
            catch (Exception e){
                debugOutput("Could not create file streams");
                return false;
            }
                
            debugOutput("Starting file transmission of " + f.getName());
            
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
                           
            debugOutput("Completed file transmission of " + f.getName());
        }
        
        // ASCII mode
        else{
        	debugOutput("Opening ASCII mode data connection for requested file " + f.getName());

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
            debugOutput("File transfer ASCII successful. Closing data connection.");         
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
            	debugOutput("File already exists");
            }
            else{
                // Binary mode
                if (transferMode == transferType.BINARY){                
                	BufferedOutputStream fout = null;
                    BufferedInputStream fin = null;
                    debugOutput("Opening binary mode data connection for requested file " + f.getName());
                    try{
                        // create streams
                        fout = new BufferedOutputStream(new FileOutputStream(f));
                        fin = new BufferedInputStream(dataConnection.getInputStream());
                    }
                    catch (Exception e){
                        debugOutput("Could not create file streams");
                    }

                    debugOutput("Start receiving file " + f.getName());

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

                    debugOutput("Completed receiving file " + f.getName());
                    debugOutput("File transfer successful. Closing data connection.");
                }
                // ASCII mode
                else{
                    debugOutput("Opening ASCII mode data connection for requested file " + f.getName());

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
                        	//debugOutput("Read line from file: "+s);
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
                    debugOutput("File transfer successful. Closing data connection.");
                }
            }
            closeDataConnection();
        }
	}
	public void debugOutput(String name) {
    	System.out.println(name);
    	textAreaLog.append(name+"\n");
    }
	
	public void formLogin() {
		jf = new JFrame();
		
		jf.setTitle("FTP");
		jf.setSize(780, 620);
		jf.setLayout(null);
		
		pnHeader = new JPanel();
		pnHeader.setBounds(50, 0, 715, 50);
		pnHeader.setBackground(Color.WHITE);
		pnHeader.setLayout(null);
		jf.add(pnHeader);
		
		pnLeft = new JPanel();
		pnLeft.setLayout(null);
		pnLeft.setBounds(0, 0, 50, 600);
		pnLeft.setBackground(new Color(0,18,50));
		jf.add(pnLeft);
		
		pnServerReplay = new JPanel();
		pnServerReplay.setLayout(null);
		pnServerReplay.setBounds(50, 50, 715, 200);
		pnServerReplay.setBackground(new Color(204,204,204));
		jf.add(pnServerReplay);
		
		pnTable = new JPanel();
		pnTable.setLayout(null);
		pnTable.setBounds(50, 250, 715, 350);
		pnTable.setBackground(new Color(32, 47, 90));
		jf.add(pnTable);
		
		lbUsername =  new JLabel("USER: ");
		lbUsername.setBounds(175, 15, 35, 25);
		lbUsername.setFont(new java.awt.Font("Century Gothic", 0, 12));
		lbUsername.setForeground(new Color(47,79,79));
		
		username = new JTextField("tmq");
		username.setBounds(210,15, 120, 25);
		username.setFont(new java.awt.Font("Century Gothic", 0, 12));
		username.setForeground(new Color(47,79,79));
		username.setBackground(Color.WHITE);
		
		lbPassword =  new JLabel("PASS: ");
		lbPassword.setBounds(350, 15, 40, 25);
		lbPassword.setFont(new java.awt.Font("Century Gothic", 0, 12));
		lbPassword.setForeground(new Color(47,79,79));

		password = new JPasswordField("301199");
		password.setBounds(390,15, 120, 25);
		password.setFont(new java.awt.Font("Century Gothic", 0, 12));
		password.setForeground(new Color(47,79,79));
		password.setBackground(Color.WHITE);

		lbPort =  new JLabel("PORT: ");
		lbPort.setBounds(530, 15, 40, 25);
		lbPort.setFont(new java.awt.Font("Century Gothic", 0, 12));
		lbPort.setForeground(new Color(47,79,79));
		
		port = new JTextField("21");
		port.setBounds(580,15, 50, 25);
		port.setForeground(new Color(47,79,79));
		port.setFont(new java.awt.Font("Century Gothic", 0, 12));
		port.setBackground(Color.WHITE);
		
		pnHeader.add(lbUsername);
		pnHeader.add(username);
		pnHeader.add(lbPassword);
		pnHeader.add(password);
		pnHeader.add(lbPort);
		pnHeader.add(port);
		
		login = new JButton();
		login.setText("LOG IN");
		login.setBounds(660, 3, 45, 45);
		login.setFont(new java.awt.Font("Century Gothic", 0, 12));
		
		pnHeader.add(login);
		login.addActionListener(this);
		jf.setVisible(true);
	}
	
	public void sendFile() {
		jf2 = new JFrame();
		jf2.setTitle("Client - truyền file bằng thao tác TCP/IP");
        labelHost = new JLabel("Host:");
        textFieldHost = new JTextField("localhost");
        labelPort = new JLabel("Port:");
        textFieldPort = new JTextField("9900");
        labelHost.setBounds(20, 20, 50, 25);
        textFieldHost.setBounds(55, 20, 120, 25);
        labelPort.setBounds(190, 20, 50, 25);
        textFieldPort.setBounds(220, 20, 50, 25);

        textFieldFilePath = new JTextField();
        textFieldFilePath.setBounds(20, 50, 450, 25);
        btnBrowse = new JButton("Browse");
        btnBrowse.setBounds(470, 50, 80, 25);
        btnSendFile = new JButton("Send File");
        btnSendFile.setBounds(20, 80, 80, 25); 
        
        btnPasv = new JButton("PASV");
        btnPasv.setBounds(160, 80, 80, 25); //btnPasv
        textAreaResult = new JTextArea();
        textAreaResult.setBounds(20, 110, 490, 150);

        jf2.add(labelHost);
        jf2.add(textFieldHost);
        jf2.add(labelPort);
        jf2.add(textFieldPort);
        jf2.add(textFieldFilePath);
        jf2.add(btnBrowse);
        jf2.add(btnSendFile);
        jf2.add(btnPasv);
        jf2.add(textAreaResult);

        jf2.setLayout(null);
        jf2.setSize(600, 350);
//        btnBrowse.addActionListener(this);
//        btnSendFile.addActionListener(this);
//        btnPasv.addActionListener(this);
        jf2.setVisible(true);
        
//        jf2.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void chooseFile() {
        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(jf2);
        try {
            if (fc.getSelectedFile() != null) {
                textFieldFilePath.setText(fc.getSelectedFile().getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = "";
		if(e.getSource()==login) {
			String user = username.getText();
			String pass = password.getText();
			if(1==1) {
				checklogin  = true;
				jf.setVisible(false);
				sendFile();
				
			}
		}
		
		if(e.getSource() == btnSendFile) {
			String sourceFilePath = textFieldFilePath.getText();
			 command = "stor "+sourceFilePath;
			System.out.println(command);
		}
		
		if(e.getSource()==btnBrowse) {
			chooseFile();
		}
		
		if(e.getSource()==btnPasv) {
//			socketControlRunning = true;
			command = "pasv";
		}
		
	}
		
	
	public void hdlList(String args) {
		int i = 0;
		files=result.split("\\?");
		debugOutput("======= File in server u can download ======");
		for(String w:files){
			i++;
			debugOutput(i+". "+w);
		}
	}
	
	public String[] getFile() {
		return this.files;
	}
        
}
