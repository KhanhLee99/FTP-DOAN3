

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;



public class ClientTransferView extends JFrame {
    private static final long serialVersionUID = 1L;

    private JLabel labelHost;
    private JTextField textFieldHost, textFieldReceive;
    private JLabel labelPort;
    private JTextField textFieldPort;
    private JButton btnBrowse, btnDownload;
    private JTextField textFieldFilePath;
    private JButton btnSendFile, btnPasv, btnTypeI, btnTypeA, btnReceive, btnSendFile2 ,  btnSendFileI, btnSendFileA, btnList;
    private JTextArea textAreaResult;
    

    

    public ClientTransferView() {
        setTitle("Client - truyền file bằng thao tác TCP/IP");
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
        textFieldFilePath.setEditable(false);
        
        textFieldReceive = new JTextField();
        textFieldReceive.setBounds(20, 80, 450, 25);
        
        btnBrowse = new JButton("Browse");
        btnBrowse.setBounds(470, 50, 80, 25);
        
        btnDownload = new JButton("Download");
        btnDownload.setBounds(470, 80, 100, 25);
        
        btnSendFile = new JButton("Send File");
        btnSendFile.setBounds(20, 110, 80, 25);
        
        btnPasv = new JButton("PASV");
        btnPasv.setBounds(120, 110, 80, 25); //btnPasv btnTypeI btnTypeA btnReceive btnSendFile2 textFieldReceive
        
        btnTypeI = new JButton("Type I");
        btnTypeI.setBounds(220, 110, 80, 25); //btnPasv 
        
        btnTypeA = new JButton("Type A");
        btnTypeA.setBounds(320, 110, 80, 25); //btnTypeA 
        
        btnReceive = new JButton("Receive");
        btnReceive.setBounds(420, 110, 80, 25); //btnReceive 
        
        btnSendFile2 = new JButton("Send2");
        btnSendFile2.setBounds(520, 110, 80, 25); //btnReceive 
        
        btnSendFileI = new JButton("Send I");
        btnSendFileI.setBounds(520, 160, 80, 25); //btnSendFileI 
        
        btnSendFileA = new JButton("Send A");
        btnSendFileA.setBounds(520, 230, 80, 25); //btnSendFileA 
        
        btnList = new JButton("LIST");
        btnList.setBounds(520, 280, 80, 25); //btnSendFileA 
        
        textAreaResult = new JTextArea();
        textAreaResult.setBounds(20, 150, 490, 580);
        textAreaResult.setEditable(false);
        

        add(labelHost);
        add(textFieldHost);
        add(labelPort);
        add(textFieldPort);
        add(textFieldFilePath);
        add(btnBrowse);
        add(btnSendFile);
        add(btnPasv);
        add(btnTypeI);
        add(btnTypeA);
        add(btnReceive); 
        add(btnSendFile2);
        add(btnSendFileI);
        add(btnSendFileA);
        add(btnList);
        add(textAreaResult);
        add(btnDownload);
        add(textFieldReceive);

        setLayout(null);
        setSize(700, 750);
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
}
