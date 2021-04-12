package ftp;

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
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class login implements ActionListener {
	
//	public String IpAddress = "192.168.227.1";
	private static JFrame jf;
	private static JLabel lbUsername, lbPassword;
	private static JTextField username, password;
	private static JButton login;
	public MyFTP_Client mc1;
	public ClientTransferView view;
	
	public static void main(String[] args) {
		String IpAddress = "192.168.43.91";
		ClientTransferView tf = new ClientTransferView();
		tf.getTextFieldHost().setText(tf.getTextFieldHost().getText() + IpAddress); 
		tf.setVisible(false);
		MyFTP_Client mc = new MyFTP_Client(tf.getTextAreaResult(), IpAddress);
		new login(mc, tf);
	}
	
	public login(MyFTP_Client mc, ClientTransferView tf ) {
		this.mc1 = mc;
		this.view = tf;
		mc1.openControlConnection();
		mc1.start();
		jf = new JFrame();
		
		jf.setTitle("LOGIN");
		jf.setSize(300, 250);
		jf.setLayout(null);
		
		lbUsername =  new JLabel("Username");
		lbUsername.setBounds(20, 30, 75, 25);
		lbUsername.setFont(new java.awt.Font("Century Gothic", 0, 12));
		lbUsername.setForeground(new Color(47,79,79));
		jf.add(lbUsername);
		
		username = new JTextField("vietkhanh");
		username.setBounds(100, 30, 150, 25);
		username.setFont(new java.awt.Font("Century Gothic", 0, 12));
		username.setForeground(new Color(47,79,79));
		username.setBackground(Color.WHITE);
		jf.add(username);
		
		lbPassword =  new JLabel("Password");
		lbPassword.setBounds(20, 70, 75, 25);
		lbPassword.setFont(new java.awt.Font("Century Gothic", 0, 12));
		lbPassword.setForeground(new Color(47,79,79));
		
		password = new JPasswordField("vietkhanh");
		password.setBounds(100,70, 150, 25);
		password.setFont(new java.awt.Font("Century Gothic", 0, 12));
		password.setForeground(new Color(47,79,79));
		password.setBackground(Color.WHITE);
		
		jf.add(lbUsername);
		jf.add(username);
		jf.add(lbPassword);
		jf.add(password);
		
		login = new JButton();
		login.setText("Login");
		login.setBounds(100, 130, 100, 30);
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
//			mc1.cmd("list");
			if(user.equals("vietkhanh") && pass.equals("vietkhanh")) {
				jf.setVisible(false);
				view.setVisible(true);
				TranferController control = new TranferController(view, mc1);
			}
			else {
   			 JOptionPane.showMessageDialog(view, "Đăng nhập không thành công");
   		 	}
		}
	}
	
}
