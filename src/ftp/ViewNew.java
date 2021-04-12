package ftp;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ViewNew extends JFrame{
	 private static final long serialVersionUID = 1L;
	 	
	 	private JPanel pnHeader, pnLeft, pnRight;
	 	private JLabel labelTitle, labelServerReply;
	 	private JScrollPane sp;
	    private JLabel labelHost;
	    private JTextField textFieldHost, textFieldReceive;
	    private JLabel labelPort, labelUser;
	    private JTextField textFieldPort, textFieldUser;
	    private JButton btnBrowse, btnDownload;
	    private JTextField textFieldFilePath;
	    private JButton btnSendFile, btnPasv, btnTypeI, btnTypeA, btnReceive, btnSendFile2 ,  btnSendFileI, btnSendFileA, btnList, btnPort, btnQuit;
	    private JTextArea textAreaResult;
	    private Choice choice;
	    
	    public static void main(String[] args) {
	    	new ViewNew();
	    }
	    

	    public ViewNew() {
	    	setTitle("Client - truyền tải file bằng thao tác TCP/IP");
	        setLayout(null);
	        setSize(700, 680);
	        
	        pnHeader = new JPanel();
			pnHeader.setBounds(0, 0, 700, 50);
			pnHeader.setBackground(Color.WHITE);
			pnHeader.setLayout(null);
			
			pnLeft = new JPanel();
			pnLeft.setLayout(null);
			pnLeft.setBounds(0, 50, 100, 600);
			pnLeft.setBackground(new Color(0,17,50));
			
			pnRight = new JPanel();
			pnRight.setLayout(null);
			pnRight.setBounds(100, 50, 600, 600);
			pnRight.setBackground(new Color(204,204,204));
			
			labelTitle = new JLabel("ĐỒ ÁN MẠNG: TRUYỀN TẢI FILE - FTP"); //labelServerReply
			labelTitle.setBounds(170, 0, 600, 40);
			labelTitle.setFont(new Font("Segoe UI Light", 0, 22));
			labelTitle.setForeground(new Color(25,25,112));
			
			btnPasv = new JButton("PASV"); //btnPort
	        btnPasv.setBounds(10, 160, 80, 25);
	        
	        btnPort = new JButton("PORT"); //btnPort
	        btnPort.setBounds(10, 200, 80, 25);
	        
	        btnTypeI = new JButton("Type I");
	        btnTypeI.setBounds(10, 240, 80, 25);
	        
	        btnTypeA = new JButton("Type A");
	        btnTypeA.setBounds(10, 280, 80, 25);
	        
	        btnReceive = new JButton("Receive");
	        btnReceive.setBounds(10, 320, 80, 25);
	        
	        btnSendFile = new JButton("Send File");
	        btnSendFile.setBounds(10, 360, 80, 25);
	        
	        btnList = new JButton("LIST");
	        btnList.setBounds(10, 400, 80, 25);
	        
	        labelHost = new JLabel("Host:");
	        textFieldHost = new JTextField("192.168.177.31");
	        labelHost.setBounds(10, 10, 50, 25);
	        textFieldHost.setBounds(55, 10, 120, 25);
	        textFieldHost.setEditable(false);
	        
	        labelPort = new JLabel("Port:");
	        textFieldPort = new JTextField("21");
	        labelPort.setBounds(190, 10, 50, 25);
	        textFieldPort.setBounds(220, 10, 50, 25);
	        textFieldPort.setEditable(false); 
	        
	        labelUser = new JLabel("User:");
	        textFieldUser = new JTextField("vietkhanh");
	        labelUser.setBounds(300, 10, 50, 25);
	        textFieldUser.setBounds(340, 10, 153, 25);
	        textFieldUser.setEditable(false); //labelUser
	        
	        btnQuit = new JButton("Quit");
	        btnQuit.setBounds(497, 10, 80, 25); //btnQuit
	        
	        textFieldFilePath = new JTextField();
	        textFieldFilePath.setBounds(10, 50, 400, 25);
	        textFieldFilePath.setEditable(false);
	        
	        btnBrowse = new JButton("Browse");
	        btnBrowse.setBounds(413, 50, 80, 25); //
	        
	        btnSendFile2 = new JButton("Send"); 
	        btnSendFile2.setBounds(497, 50, 80, 25);
	        
	        choice = new Choice();
	        choice.setBounds(10, 500, 400, 25);
	        
	        btnDownload = new JButton("Download"); 
	        btnDownload.setBounds(415, 500, 160, 25);
	        
	        labelServerReply = new JLabel("Server Reply"); //
	        labelServerReply.setBounds(10, 75, 600, 40);
	        
	        textAreaResult = new JTextArea();
	        textAreaResult.setBounds(10, 105, 565, 380);
	        textAreaResult.setEditable(false);
	        
	        sp = new JScrollPane(textAreaResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	        sp.setBounds(10, 105, 525, 350);
	        
	        add(pnHeader);
	        add(pnLeft);
	        add(pnRight);
	        
	        
	        pnHeader.add(labelTitle);
	        
	        pnLeft.add(btnPasv);
	        pnLeft.add(btnPort);
	        pnLeft.add(btnTypeI);
	        pnLeft.add(btnTypeA);
	        pnLeft.add(btnReceive);
	        pnLeft.add(btnSendFile);
	        pnLeft.add(btnList);
	        
	        pnRight.add(labelHost);
	        pnRight.add(textFieldHost);
	        pnRight.add(labelPort);
	        pnRight.add(textFieldPort);
	        pnRight.add(labelUser);
	        pnRight.add(textFieldUser); 
	        pnRight.add(btnQuit); //btnQuit
	        pnRight.add(textFieldFilePath);
	        pnRight.add(btnBrowse);
	        pnRight.add(btnSendFile2);
	        pnRight.add(labelServerReply);
	        pnRight.add(sp);
	        pnRight.add(choice);
	        pnRight.add(btnDownload);
	        
	        
	        
	        setVisible(true);
	        
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	    }
	    
	    public void chooseFile() {
	        final JFileChooser fc = new JFileChooser();
	        fc.showOpenDialog(this);
	        try {
	            if (fc.getSelectedFile() != null) {
	                textFieldFilePath.setText(fc.getSelectedFile().getPath());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public JLabel getLabelHost() {
	        return labelHost;
	    }

	    public void setLabelHost(JLabel labelHost) {
	        this.labelHost = labelHost;
	    }

	    public JTextField getTextFieldHost() {
	        return textFieldHost;
	    }

	    public void setTextFieldHost(JTextField textFieldHost) {
	        this.textFieldHost = textFieldHost;
	    }

	    public JLabel getLabelPort() {
	        return labelPort;
	    }

	    public void setLabelPort(JLabel labelPort) {
	        this.labelPort = labelPort;
	    }

	    public JTextField getTextFieldPort() {
	        return textFieldPort;
	    }

	    public void setTextFieldPort(JTextField textFieldPort) {
	        this.textFieldPort = textFieldPort;
	    }

	    public JButton getBtnBrowse() {
	        return btnBrowse;
	    }

	    public void setBtnBrowse(JButton btnBrowse) {
	        this.btnBrowse = btnBrowse;
	    }

	    public JTextField getTextFieldFilePath() {
	        return textFieldFilePath;
	    }

	    public void setTextFieldFilePath(JTextField textFieldFilePath) {
	        this.textFieldFilePath = textFieldFilePath;
	    }
	    
	    public JTextField getTextFieldReceive() {
	        return textFieldReceive;
	    }

	    public void setTextFieldReceive(JTextField textFieldReceive) {
	        this.textFieldReceive = textFieldReceive;
	    }

	    public JButton getBtnSendFile() {
	        return btnSendFile;
	    }
	    
	    public JButton getBtnPasv() {
	        return btnPasv;
	    }
	    
	    public JButton getBtnTypeI() {
	        return btnTypeI;
	    }
	    
	    public JButton getBtnTypeA() {
	        return btnTypeA;
	    } 
	    
	    public JButton getBtnReceive() {
	        return btnReceive;  
	    } 
	    
	    public JButton getBtnSendFile2() {
	        return btnSendFile2;  
	    } 
	    
	    public JButton getBtnSendFileA() {
	        return btnSendFileA;  
	    } 
	    
	    public JButton getBtnDownload() {
	        return btnDownload;  
	    } 
	    
	    public JButton getBtnSendFileI() {
	        return btnSendFileI;  
	    } 
	    
	    public JButton getBtnList() {
	        return btnList;  
	    } 

	    public void setBtnSendFile(JButton btnSendFile) {
	        this.btnSendFile = btnSendFile;
	    }

	    public JTextArea getTextAreaResult() {
	        return textAreaResult;
	    }

	    public void setTextAreaResult(JTextArea textAreaResult) {
	        this.textAreaResult = textAreaResult;
	    }
	    
	    public Choice getChoice() {
	    	return choice;
	    }
	    
	    public String getIndexChoice() {
	    	return choice.getItem(choice.getSelectedIndex());
	    }
	    public void setChoice(Choice choice) {
	    	this.choice = choice;
	    }
}
