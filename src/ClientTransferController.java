

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;




public class ClientTransferController implements ActionListener {
	private boolean checkLogin = false;
    private ClientTransferView view;
    private MyFTP_Client mc;
    String sourceFilePath;
    
//    public static void main(String[] args) {
//        ClientTransferView view = new ClientTransferView();
//        MyFTP_Client mc = new MyFTP_Client(view.getTextAreaResult());
////        MyFTP_Client mc = new MyFTP_Client();
//        new ClientTransferController(view, mc);
//        mc.openControlConnection();
//        mc.start();
//        
//    }

    public ClientTransferController(ClientTransferView view, MyFTP_Client mc) {
        this.view = view;
        this.mc = mc;
        
        view.getBtnBrowse().addActionListener(this);
        view.getBtnSendFile().addActionListener(this); //getBtnPasv  getBtnSendFileI getBtnList
        view.getBtnPasv().addActionListener(this);
        view.getBtnTypeI().addActionListener(this);
        view.getBtnTypeA().addActionListener(this);
        view.getBtnReceive().addActionListener(this);  
        view.getBtnSendFile2().addActionListener(this);
        view.getBtnSendFileI().addActionListener(this);
        view.getBtnSendFileA().addActionListener(this);
        view.getBtnList().addActionListener(this);
        view.getBtnDownload().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(view.getBtnBrowse().getText())) {
            view.chooseFile();
        }
        
        //stor
        if (e.getActionCommand().equals(view.getBtnSendFile().getText())) {
            String host = view.getTextFieldHost().getText().trim();
            int port = Integer.parseInt(view.getTextFieldPort().getText().trim());
             sourceFilePath = view.getTextFieldFilePath().getText();
            if (sourceFilePath != "") {
            	mc.cmd("stor "+sourceFilePath);
            	
            } else {
                JOptionPane.showMessageDialog(view, "Bạn chưa chọn file");
            }
        }
        
        //pasv + type I/A + stor
        if(e.getActionCommand().equals(view.getBtnSendFile2().getText())) {
   		 		sourceFilePath = view.getTextFieldFilePath().getText();
   		 		mc.cmd("pasv");
   		 		String ext1 = sourceFilePath.substring(sourceFilePath.lastIndexOf(".") + 1);
   		 		if(ext1.equals("jpg") || ext1.equals("bmp") ||ext1.equals("png") ||ext1.equals("mp3") ||ext1.equals("zip") || ext1.equals("rar") || ext1.equals("tar") || ext1.equals("exe") ||ext1.equals("doc") ||ext1.equals("xls") ||ext1.equals("pdf")) {
	   		 		mc.cmd("type I");
   		 		}
   		 		else {
   		 			mc.cmd("type A");
   		 		}
   		 		mc.cmd("stor "+sourceFilePath);
        }	
        
        //pasv
        if(e.getActionCommand().equals(view.getBtnPasv().getText())) {
        	if(checkLogin) {
        		mc.cmd("pasv");
        		
        	}
        	else {
        		JOptionPane.showMessageDialog(view, "Bạn cần phải đăng nhập !!!");
        		checkLogin = true;
        	}
        }
        
        
        //type I
        if(e.getActionCommand().equals(view.getBtnTypeI().getText())) {
        	if(checkLogin) {
        		mc.cmd("type I");
        		
        	}
        	else {
        		JOptionPane.showMessageDialog(view, "Bạn cần phải đăng nhập !!!");
        		checkLogin = true;
        	}
        }
        
        //type A
        if(e.getActionCommand().equals(view.getBtnTypeA().getText())) {
        	if(checkLogin) {
        		mc.cmd("type A");
        		
        	}
        	else {
        		JOptionPane.showMessageDialog(view, "Bạn cần phải đăng nhập !!!");
        		checkLogin = true;
        	}
        }
        
        //retr
        if(e.getActionCommand().equals(view.getBtnReceive().getText())) {
        		 sourceFilePath = view.getTextFieldFilePath().getText();
    			 mc.cmd("retr "+sourceFilePath);
        }
        
        
        //type I + stor
        if(e.getActionCommand().equals(view.getBtnSendFileI().getText())) {
		 		sourceFilePath = view.getTextFieldFilePath().getText();
		 		mc.cmd("type I");
		 		mc.cmd("stor "+sourceFilePath);
        }
        
        //type A + stor
        if(e.getActionCommand().equals(view.getBtnSendFileA().getText())) {
	 		sourceFilePath = view.getTextFieldFilePath().getText();
	 		mc.cmd("type A");
	 		mc.cmd("stor "+sourceFilePath);
        }
        
      //list 
        if(e.getActionCommand().equals(view.getBtnList().getText())) {
        	mc.cmd("pasv");
	 		mc.cmd("list1");
        } 
        
        //pasv + type I/A + retr
        if(e.getActionCommand().equals(view.getBtnDownload().getText())) {
   		 		String sourceFilePathDownload = view.getTextFieldReceive().getText();
   		 		mc.cmd("pasv");
   		 		String ext1 = sourceFilePathDownload.substring(sourceFilePathDownload.lastIndexOf(".") + 1);
   		 		if(ext1.equals("jpg") || ext1.equals("bmp") ||ext1.equals("png") ||ext1.equals("mp3") ||ext1.equals("zip") || ext1.equals("rar") || ext1.equals("tar") || ext1.equals("exe") ||ext1.equals("doc") ||ext1.equals("xls") ||ext1.equals("pdf")) {
	   		 		mc.cmd("type I");
   		 		}
   		 		else {
   		 			mc.cmd("type A");
   		 		}
   		 		mc.cmd("retr "+sourceFilePathDownload);
        }	
    }
}
