package com.eficost.tries.updaterapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

import com.eficost.updaterapp.view.frmUpdaterEFIcost;

public class ftpDownloadToLocal {  
 
	public void ftpDownload() throws IOException{
		// FTP connection variables
		
		String serverHost = "190.81.116.163"; 
		String userFtp 	  = "userht";
		String passwordFtp= "qwerty";
		String remoteDIR  = "ht_test";
		String localDIR   = "D:\\ftp";
		
		// Object for FTPClient Class
		
		FTPClient ftp = new FTPClient();
		
		// Establishing connection to the FTP server 
		ftp.connect(serverHost);
		// Storing the login
		boolean login = ftp.login(userFtp, passwordFtp);
		ftp.enterLocalPassiveMode();
		ftp.changeWorkingDirectory(remoteDIR);
		FTPFile[] files = ftp.listFiles();
		
		try {
			// Validating the login
			if(login) {
				System.out.print("Logged with "+ftp.getStatus()+"\n");
				System.out.print("Now the directory is "+ftp.printWorkingDirectory()+"\n");
				System.out.print("Local directory is "+localDIR+"\n");
				System.out.print("Total files are "+files.length+"\n");
				// Validating files in the server directory
				if(files != null && files.length > 0) {
					// Reading each file inside the directory
					for(FTPFile fl : files) {
						if(!fl.isFile()) {
							continue;
						}
						System.out.print(fl.getName()+"\n");
						// Object to download 
						OutputStream out;
						out = new FileOutputStream(localDIR+"/"+fl.getName());
						ftp.retrieveFile(fl.getName(), out);
						out.close();
					}
				}
			}else {
				System.out.print("The user can't connect to "+serverHost+"\n");
			}
			
			ftp.logout();
			ftp.disconnect();			
			
		}catch(Exception e) {
			
			System.out.print(e+"\n");
			
		}
		
	}
	
	public void ftpGetVersion() throws IOException{
		// FTP connection variables
		
		String serverHost = "190.81.116.163"; 
		String userFtp 	  = "efiupdater";
		String passwordFtp= "ef1updater123";
		String nombreapp  = "Contabilidad";
		String remoteDIR  = "Sistemas/Eficost_126";
		String localDIR   = "C:/Sistemas_Eficost";
		
		// Object for FTPClient Class
		
		FTPClient ftp = new FTPClient();
		
		// Establishing connection to the FTP server 
		ftp.connect(serverHost);
		// Storing the login
		boolean login = ftp.login(userFtp, passwordFtp);
		ftp.enterLocalPassiveMode();
		ftp.changeWorkingDirectory(remoteDIR);
		FTPFile[] files = ftp.listFiles();
		
		try {
			// Validating the login
			if(login) {
				System.out.print("Logged with "+ftp.getStatus()+"\n");
				System.out.print("Now the directory is "+ftp.printWorkingDirectory()+"\n");
				System.out.print("Local directory is "+localDIR+"\n");
				System.out.print("Total files are "+files.length+"\n");
				// Validating files in the server directory
				if(files != null && files.length > 0) {
					// Reading each file inside the directory
					for(FTPFile fl : files) {
						if(!fl.isFile()) {
							continue;
						}
						if(fl.getName().equals("version.ini")) {													
							Ini ini = new Wini(); 
							Config conf = new Config();
							conf.setMultiOption(true);
							ini.setConfig(conf);
							try {
								ini.load(new InputStreamReader(ftp.retrieveFileStream("/"+remoteDIR+"/"+nombreapp+"/version.ini"), "UTF-8"));
								Section serversection = ini.get("VERSION");
								String serverVersion = serversection.get("Fecha");
								
								ini.load(new File(localDIR+"/"+nombreapp+"/version.ini"));
								Section usersection = ini.get("VERSION");
								String userVersion = usersection.get("Fecha");
								
								System.out.println("SERVER VERSION : "+serverVersion);
								System.out.println("USER VERSION : "+userVersion);
								
							} catch (IOException e) {			
								System.out.println(e);
							} 
						}						
						// Object to download 
						/*OutputStream out;
						out = new FileOutputStream(localDIR+"/"+fl.getName());
						ftp.retrieveFile(fl.getName(), out);
						out.close();*/
					}
				}
			}else {
				System.out.print("The user can't connect to "+serverHost+"\n");
			}
			
			ftp.logout();
			ftp.disconnect();			
			
		}catch(Exception e) {
			
			System.out.print(e+"\n");
			
		}
		
	}
	
	public List<FTPFile> getListAppFilesFTP() {
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
											    
						if(fl.getName().endsWith(".pbd")) {
							lstFiles.add(fl);
						}	
						if(fl.getName().endsWith(".exe")){
							System.out.print(fl.getName()+" - "+count);
						}
						if(fl.getName().contentEquals(nombreExe)) {
							lstFiles.add(fl);
						}
						
						if(type == "Prod" && fl.getName().equals(configurationFileName)) { 
							lstFiles.add(fl);
						}
						
						// Downloading files
						// Object to download 
						OutputStream out;
						out = new FileOutputStream(localDIR+"/"+fl.getName());
						System.out.print("Downloading file "+fl.getName()+"\n");
					    
					    updaterView.lblNombreArchivoDescarga.setText("Descargando "+fl.getName());
					    
						ftp.retrieveFile(fl.getName(), out);
						
						countFiles++;
					    currentPorent = 100 * (count+1)/total;
						updaterView.pbDescargaArchivo.setValue(currentPorent);
						
						out.close();						
						if(count == 5) {
							
							break;
						}
					}					
				}
				String iniFilePath = "";				
				if(type == "Desa") { 
					iniFilePath = remoteDIR+"/"+nombreapp+"/Desa";
					ftp.changeWorkingDirectory(iniFilePath);
					FTPFile[] filesToGetIni = ftp.listFiles();
					if(filesToGetIni != null && filesToGetIni.length > 0) {
						for(FTPFile flIni : filesToGetIni) {
							if(flIni.getName().equals(configurationFileName)) {
								lstFiles.add(flIni);
							}
						}
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
		return lstFiles;
	}
	
	public static void main(String[] args) {		
		//ftpDownloadToLocal ftp = new ftpDownloadToLocal();
		// System.out.print("It Works!");
		/*
		try {
			
			ftp.ftpGetVersion();
			
		} catch (IOException e) {
			
			System.out.print(e+"\n");
			
		}*/
		
		//List<FTPFile> lstFiles = ftp.getListAppFilesFTP();
		
		OutputStream outLogFile = null;
		String logFilePath = "D:/ftp/LOG/Contabilidad";
		try {
			
			/*outLogFile = new FileOutputStream(logFilePath+"/file.log");*/	
			Path logpath = Paths.get(logFilePath+"/file.log");
			boolean result = Files.deleteIfExists(logpath);
			if(result) {
				System.out.print("Se elimin� el archivo file.log\n");
			}
			File logFile = new File(logFilePath+"/file.log");
			if(logFile.createNewFile()) {
				System.out.print("Se CRE� EL ARCHIVO file.log\n");
				
				FileWriter myWriter = new FileWriter(logFilePath+"/file.log");
				
			    myWriter.write("First line\n");
			    myWriter.write("Second line\n");
			    myWriter.write("Third line\n");
			    myWriter.close();
			}else {
				System.out.print("NO SE PUDO CREAR EL ARCHIVO file.log\n");
			}
			
			//System.out.print(result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
