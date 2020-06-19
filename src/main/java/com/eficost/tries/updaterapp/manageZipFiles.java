package com.eficost.tries.updaterapp;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.eficost.updaterapp.view.frmUpdaterEFIcost;

public class manageZipFiles {
	public static void main(String[] args) {
		List<FTPFile> lstFiles = new ArrayList<FTPFile>();
		FTPClient ftp = new FTPClient();
		String serverHost = "190.81.116.163"; 
		String userFtp 	  = "efiupdater";
		String passwordFtp= "ef1updater123";
		String nombreapp  = "Contabilidad";
		String remoteDIR  = "Sistemas/Eficost_126";
		String localDIR   = "D:/ftp/EFIcost";
		String nombreExe  = "contabilidad.exe";
		String type = "Prod";
		String configurationFileName = "eficost.ini";
		try
		{
			ftp.connect(serverHost);
			boolean login = ftp.login(userFtp, passwordFtp);
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(remoteDIR+"/"+nombreapp);
			FTPFile[] files = ftp.listFiles();
			
			if(login) {			
				int count = -1;
				// Validating files in the server directory
				if(files != null && files.length > 0) {
					
					frmUpdaterEFIcost updaterView = new frmUpdaterEFIcost(); 
					updaterView.setVisible(true);	
					int countFiles = -1;
					int percent = 0;
					int total = 6;//files.length;
					int currentPorent = 0;
					// Reading each file inside the directory
					for(FTPFile fl : files) {
						count ++;
											    
						if(fl.getName().equals(nombreapp+".zip")) {						
							
							// Downloading files
							// Object to download 
							OutputStream out;
							out = new FileOutputStream(localDIR+"/"+fl.getName());
							System.out.print("Downloading file "+fl.getName()+"\n");
						    
						    updaterView.lblNombreArchivoDescarga.setText("Descargando "+fl.getName());
						    Date startdate = Calendar.getInstance().getTime();
							DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
						    
							String time = dateFormat.format(startdate );
						    System.out.print("Inicio descarga : "+time+"\n");						    
						  
							ftp.retrieveFile(fl.getName(), out);	
							
							Date enddate = Calendar.getInstance().getTime();
						    dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
						    time = dateFormat.format(enddate );
						    System.out.print("Fin descarga : "+time+"\n");
						    
							out.close();
							currentPorent = 100;
							break;
						}	
						
						countFiles++;
					    currentPorent = 100 * (count+1)/total;
						updaterView.pbDescargaArchivo.setValue(currentPorent);
						
					}					
				}				
				ftp.logout();
				ftp.disconnect();	
				
			}
			
		}		
		catch(Exception e)
		{
			e.printStackTrace();
			// Save error in the log
		}
	}
}
