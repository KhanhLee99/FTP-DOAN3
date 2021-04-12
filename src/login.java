import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class login implements ActionListener {
	public static void main(String[] args) {
		ClientTransferView tf = new ClientTransferView();
		tf.setVisible(false);
		MyFTP_Client mc = new MyFTP_Client(tf.getTextAreaResult());
		new login(mc, tf);
	}
	String IpAddress = "192.168.31.152";
	int controlPort = 21;
	
	private Socket controlConnection;
	private PrintWriter controlOutWriter;
	
	private Socket dataConnection;
    private PrintWriter dataOutWriter;
    
	private static JFrame jf;
	private static JLabel lbUsername, lbPassword;
	private static JTextField username, password;
	private static JButton login;
	public MyFTP_Client mc1;
	public ClientTransferView view;
	public login(MyFTP_Client mc, ClientTransferView tf) {
		this.mc1 = mc;
		this.view = tf;
		mc1.openControlConnection();
		mc1.start();
		jf = new JFrame();
		
		jf.setTitle("LOGIN");
		jf.setSize(300, 300);
		jf.setLayout(null);
		
		lbUsername =  new JLabel("USER: ");
		lbUsername.setBounds(30, 30, 35, 25);
		lbUsername.setFont(new java.awt.Font("Century Gothic", 0, 12));
		lbUsername.setForeground(new Color(47,79,79));
		jf.add(lbUsername);
		
		username = new JTextField("vanhuy");
		username.setBounds(70, 30, 150, 25);
		username.setFont(new java.awt.Font("Century Gothic", 0, 12));
		username.setForeground(new Color(47,79,79));
		username.setBackground(Color.WHITE);
		jf.add(username);
		
		lbPassword =  new JLabel("PASS: ");
		lbPassword.setBounds(30, 70, 35, 25);
		lbPassword.setFont(new java.awt.Font("Century Gothic", 0, 12));
		lbPassword.setForeground(new Color(47,79,79));
		
		password = new JPasswordField("dut123");
		password.setBounds(70,70, 150, 25);
		password.setFont(new java.awt.Font("Century Gothic", 0, 12));
		password.setForeground(new Color(47,79,79));
		password.setBackground(Color.WHITE);
		
		jf.add(lbUsername);
		jf.add(username);
		jf.add(lbPassword);
		jf.add(password);
		
		login = new JButton();
		login.setText("LOG IN");
		login.setBounds(100, 150, 100, 30);
		login.setFont(new java.awt.Font("Century Gothic", 0, 12));
		
		jf.add(login);
		login.addActionListener(this);
		jf.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==login) {
			String user = username.getText();
			String pass = password.getText();
			mc1.cmd("user " + user);
			mc1.cmd("pass " + pass);
			jf.setVisible(false);
			view.setVisible(true);
			ClientTransferController control = new ClientTransferController(view, mc1);
            /*try {
    			while(true) {
    				controlOutWriter = new PrintWriter(controlConnection.getOutputStream(), true);
    				controlOutWriter.println("user " + user + "/" + "pass " + pass);
    		        System.out.println("Create Control **************");
    				BufferedReader controlIn = new BufferedReader(new InputStreamReader(controlConnection.getInputStream()));		
    				String result = controlIn.readLine();
    				System.out.println("Getting from server: "+result);	
    				checkPassiveConnect(result);
    			}
    		}catch(Exception e1) {
    			System.out.println(e1);
    			System.out.println("Cannot read from server");
    		}*/
		}
	}
	
}