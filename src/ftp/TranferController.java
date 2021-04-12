package ftp;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;




public class TranferController implements ActionListener {
	private boolean checkLogin = false;
    private ClientTransferView view;
    private MyFTP_Client mc;
    String sourceFilePath;
    private boolean checkAdd = false;

    public TranferController(ClientTransferView view, MyFTP_Client mc) {
        this.view = view;
        this.mc = mc;
//        String[] files = mc.getFile();
//        for(String w:files) {
//        	view.getChoice().add(w);
//        }
        view.getBtnBrowse().addActionListener(this);
        view.getBtnSendFile().addActionListener(this); //
        view.getBtnPasv().addActionListener(this);
        view.getBtnTypeI().addActionListener(this);
        view.getBtnTypeA().addActionListener(this);
        view.getBtnReceive().addActionListener(this);  
        view.getBtnSendFile2().addActionListener(this);
        view.getBtnSendFileI().addActionListener(this);
        view.getBtnSendFileA().addActionListener(this);
        view.getBtnList().addActionListener(this);
        view.getBtnDownload().addActionListener(this);
        view.getBtnQuit().addActionListener(this);
        view.getBtnPort().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(view.getBtnBrowse().getText())) {
            view.chooseFile();
        }
        
        //stor
        if (e.getActionCommand().equals(view.getBtnSendFile().getText())) {
             sourceFilePath = view.getTextFieldFilePath().getText();
            if (!sourceFilePath.equals("")) {
            	mc.cmd("stor "+sourceFilePath);
            	
            } else {
                JOptionPane.showMessageDialog(view, "Bạn cần phải chọn file");
            }
        }
        
        //pasv + type I/A + stor
        if(e.getActionCommand().equals(view.getBtnSendFile2().getText())) {
   		 		sourceFilePath = view.getTextFieldFilePath().getText();
	   		 	if(!sourceFilePath.equals("")) {
//	   		 		mc.cmd("pasv");
	   		 		String ext1 = sourceFilePath.substring(sourceFilePath.lastIndexOf(".") + 1);
	   		 		if(ext1.equals("jpg") || ext1.equals("bmp") ||ext1.equals("png") ||ext1.equals("mp3") ||ext1.equals("mp4") ||ext1.equals("zip") || ext1.equals("rar") || ext1.equals("tar") || ext1.equals("exe") ||ext1.equals("doc") ||ext1.equals("docx")||ext1.equals("xls") ||ext1.equals("pdf")) {
		   		 		mc.cmd("type I");
	   		 		}
	   		 		else if(ext1.equals("html") || ext1.equals("php") ||ext1.equals("js") ||ext1.equals("css") ||ext1.equals("htaccess") ||ext1.equals("pwd") || ext1.equals("txt") || ext1.equals("forward ")){
	   		 			mc.cmd("type A");
	   		 		}
	   		 		mc.cmd("stor "+sourceFilePath);
	   		 	}
	   		 	else {
	   		 		JOptionPane.showMessageDialog(view, "Bạn cần phải chọn file");
	   		 	}
        }	
        
        //pasv
        if(e.getActionCommand().equals(view.getBtnPasv().getText())) {
    		mc.cmd("pasv");
        }
        
        //type I
        if(e.getActionCommand().equals(view.getBtnTypeI().getText())) {
    		mc.cmd("type I");
        }
        
        //type A
        if(e.getActionCommand().equals(view.getBtnTypeA().getText())) {
    		mc.cmd("type A");
        }
        
        //retr
        if(e.getActionCommand().equals(view.getBtnReceive().getText())) {
        		 sourceFilePath = view.getIndexChoice(); 
        		 if(!sourceFilePath.equals("")) {
        			 mc.cmd("retr "+sourceFilePath);
        		 }
        		 else {
        			 JOptionPane.showMessageDialog(view, "Bạn cần phải chọn file");
        		 }
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
	 		mc.cmd("list");
 			String[] files = mc.getFile();
	        for(String w:files) {
	        	view.getChoice().add(w);
	        }	
        } 
        
        //pasv + type I/A + retr
        if(e.getActionCommand().equals(view.getBtnDownload().getText())) {
	 		String sourceFilePathDownload = view.getIndexChoice();
   		 	if(!sourceFilePathDownload.equals("")) {
//   		 		mc.cmd("pasv");
   		 		String ext1 = sourceFilePathDownload.substring(sourceFilePathDownload.lastIndexOf(".") + 1);
   		 		if(ext1.equals("jpg") || ext1.equals("bmp") ||ext1.equals("png") ||ext1.equals("mp3") ||ext1.equals("mp4") ||ext1.equals("zip") || ext1.equals("rar") || ext1.equals("tar") || ext1.equals("exe") ||ext1.equals("doc")||ext1.equals("docx") ||ext1.equals("xls") ||ext1.equals("pdf")) {
	   		 		mc.cmd("type I");
   		 		}
   		 		else if(ext1.equals("html") || ext1.equals("php") ||ext1.equals("js") ||ext1.equals("css") ||ext1.equals("htaccess") ||ext1.equals("pwd") || ext1.equals("txt") || ext1.equals("forward ")){
   		 			mc.cmd("type A");
   		 		}
   		 		mc.cmd("retr "+sourceFilePathDownload);
   		 	}
   		 	else {
   		 		JOptionPane.showMessageDialog(view, "Bạn cần phải chọn file");
   		 	}
        }	
        
        //quit 
        if(e.getActionCommand().equals(view.getBtnQuit().getText())) {
	 		mc.cmd("quit");
        } 
        
        //port
        if(e.getActionCommand().equals(view.getBtnPort().getText())) {
          String host = view.getTextFieldHost().getText().trim();
          String port = view.getTextFieldPort().getText().trim();
          if(port.equals("")) {
        	  JOptionPane.showMessageDialog(view, "Bạn cần phải nhập port");
          }
          else {
        	  String args = host+","+port;
    	 	  mc.cmd("port "+args);
          }
          
        } 

    }
}
