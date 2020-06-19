package com.eficost.updaterapp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.eficost.updaterapp.controller.ApplicationController;
import com.eficost.updaterapp.controller.CryptoEficostController;
import com.eficost.updaterapp.entities.Application;
import com.eficost.updaterapp.entities.FtpUpdater;
import com.eficost.updaterapp.entities.IniFile;
import com.eficost.updaterapp.helper.EFIcostHelper;
import com.eficost.updaterapp.view.frmUpdaterEFIcost;

public class FTPEficostUpdaterServiceImpl implements FTPEficostUpdaterService{

	CryptoEficostController ceController = new CryptoEficostController(); 
	ApplicationController appController = new ApplicationController();
	
	// View
	frmUpdaterEFIcost updaterView = new frmUpdaterEFIcost(); 
	
	@Override
	public void updateEFIcostFiles() {
		// Obteniendo el listado objetos de Aplicaciones con sus datos
		EFIcostHelper helper = new EFIcostHelper();
		Application[] lstApplications = helper.ARR_APPLICATION;		
		Application objApplication = new Application(); 
		IniFile ini = new IniFile();
		FtpUpdater objFtpUpdater = new FtpUpdater(); 

		String initFileDIR = "D:/ftp/EFIcost/eficost.ini";
		try 
		{
			// Variables para la actualizaci�n
			String module 		= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","MODULO");
			String envApp 		= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","ENTORNOAPL");
			String envUser		= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","ENTORNOUSU");
			String envirBD		= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","ENTORNOBD");
			String confFilName	= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","NOMARCHCFG");
			
			String RUTAORIPBDSAPLS	= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","RUTAORIPBDSAPLS");
			String RUTAORIRCRSAPLS	= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","RUTAORIRCRSAPLS");
			String RUTADES1EXESAPLS	= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","RUTADES1EXESAPLS");
			String RUTADES2EXESAPLS	= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","RUTADES2EXESAPLS");
			String RUTADESRCRSAPLS	= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","RUTADESRCRSAPLS");
			String RUTADESEXESAPLSUSU	= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","RUTADESEXESAPLSUSU");
			String RUTALOGPROC	= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","RUTALOGPROC");
			String RUTALOGPROCUSU	= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","RUTALOGPROCUSU");
			
			String ip 		  = ceController.obtenerValorDePropiedadDesencriptado(initFileDIR, "EFIUPDATER", "IP");
			String userIP  	  = ceController.obtenerValorDePropiedadDesencriptado(initFileDIR, "EFIUPDATER", "USERIP");
			String passIP 	  = ceController.obtenerValorDePropiedadDesencriptado(initFileDIR, "EFIUPDATER", "PASSIP");
			String rutaRemota = ceController.obtenerValorDePropiedadDesencriptado(initFileDIR, "EFIUPDATER", "RUTAREMOTA");
			
			// Enviando los valores para la actualizaci�n al objeto IniFile
			ini.setModule(module);
			ini.setEnviromentApp(envApp);
			ini.setEnviromentUser(envUser);
			ini.setEnviromentDataBase(envirBD);
			ini.setConfigurationFileName(confFilName);
			ini.setRutaoripbdsapls(RUTAORIPBDSAPLS);
			ini.setRutaorircrsapls(RUTAORIRCRSAPLS);
			ini.setRutades1exesapls(RUTADES1EXESAPLS);
			ini.setRutades2exesapls(RUTADES2EXESAPLS);
			ini.setRutadesrcrsapls(RUTADESRCRSAPLS);
			ini.setRutadesexesaplsusu(RUTADESEXESAPLSUSU);
			ini.setRutalogproc(RUTALOGPROC);
			ini.setRutalogprocusu(RUTALOGPROCUSU);
			
			// Enviando los valores del objeto FTPUpdater
			objFtpUpdater.setServerHost(ip);
			objFtpUpdater.setUser(userIP);
			objFtpUpdater.setPassword(passIP);
			objFtpUpdater.setRemoteDIR(rutaRemota);
			
			// Definiendo la ruta del ejecutable que se abrir� al finalizar el proceso
			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			ini = new IniFile();
		}
		
		// Variables de la aplicacion
		String CODIGOAPLICACION	= ceController.obtenerValorDePropiedadDesencriptado(initFileDIR,"EFIUPDATER","CODIGOAPLICACION");		
		
		// Obteniendo los datos de la aplicaci�n
		
		for(Application app : lstApplications) {
			if(app.getId().equals(CODIGOAPLICACION)) {
				objApplication = app;
				break;
			}			
		}
		
		// Definiendo el entorno del usuario
		switch(ini.getEnviromentUser()){
			case "SRV" :
				// INICIAR EL ACTUALIZADOR
				break;
			case "USU" :
				int validateVersion =  appController.compararVersion(objFtpUpdater, objApplication, ini);
				if(validateVersion == -1) 
				{
					int numberOfOpenSessions = appController.validarSesionActivas(objApplication.getExeAppName());
					if(numberOfOpenSessions > 0) 
					{
						// MOSTRAR MENSAJE
					}
					else 
					{
						// ACTUALIZAR CARPETAS
						String[] arrFlgsActApp = appController.obtenerFlgsActDeLaAplicacionServer(objFtpUpdater, objApplication, ini);
						objFtpUpdater.setFlgActProd(arrFlgsActApp[0]);
						objFtpUpdater.setFlgActDesa(arrFlgsActApp[1]);
						objFtpUpdater.setFlgActSist(arrFlgsActApp[2]);
						updateClient(objFtpUpdater,objApplication,ini);
						// RUN .EXE FILE
						
					}
				}
				else if(validateVersion == 1)
				{
					// SHOW SUCCESSFUL MESSAGE : "THE EFICOST UPDATE WAS SUCCESSFUL"
					JOptionPane.showMessageDialog(null, "The app is updated. It will be run.");
					return;
				}
				break;
			default : break;
		}
	}

	@Override
	public void updateClient(FtpUpdater updater, Application objApp, IniFile objIni) {
		// Open the ProgressBar frame		 
		updaterView.setVisible(true);
		updaterView.lblNombreArchivoDescarga.setText("Preparando descarga...");
		
		// Validate what is going to be updated (PROD , DESA , SIST)
		if(updater.getFlgActProd().equals("S")) 
		{
			// 1. Copy the app files from  the ftp server
			updaterView.lblTitulo.setText("EFIcost "+objApp.getName()+" - Actualizando Producción");
			copyAppFilesToUser("Prod",updater,objApp,objIni);			
			// 2. Copy the app resources from  the ftp server
			updaterView.lblTitulo.setText("EFIcost Recursos - Actualizando Producción");
			copyAppResourcesToUser("Prod", updater, objApp, objIni);			
		}
		if(updater.getFlgActDesa().equals("S")) 
		{
			// 1. Copy the app files from  the ftp server
			updaterView.lblTitulo.setText("EFIcost "+objApp.getName()+" - Actualizando Desarrollo");
			copyAppFilesToUser("Desa",updater,objApp,objIni);
			// 2. Copy the app resources from  the ftp server
			updaterView.lblTitulo.setText("EFIcost Recursos - Actualizando Desarrollo");
			copyAppResourcesToUser("Desa", updater, objApp, objIni);
			
		}
		if(updater.getFlgActSist().equals("S")) 
		{
			// 1. Copy the app files from  the ftp server
			updaterView.lblTitulo.setText("EFIcost "+objApp.getName()+" - Actualizando Sistemas");
			copyAppFilesToUser("Sist",updater,objApp,objIni);
			// 2. Copy the app resources from  the ftp server
			updaterView.lblTitulo.setText("EFIcost Recursos - Actualizando Sistemas");
			copyAppResourcesToUser("Sist", updater, objApp, objIni);
			
		}
		boolean download = copyFileVersion(updater, objApp, objIni);
		if(!download) {
			// SHOW ERROR MESSAGE : "THE VERSION FILE DOWNLOAD WAS UNSUCCESSFUL"
		}
		// SHOW SUCCESSFUL MESSAGE : "THE EFICOST UPDATE WAS SUCCESSFUL"
		JOptionPane.showMessageDialog(null, "The applicatioin "+objApp.getName()+" will be opened.");
		updaterView.dispose();
	}

	@Override
	public int copyAppFilesToUser(String type,FtpUpdater updater, Application objApp, IniFile objIni) {
		String	ls_RutaOriArchs, ls_RutaDesArchs;
		
		ls_RutaOriArchs = objIni.getRutades2exesapls() + "/" + objApp.getExeFieldNameApp();
		ls_RutaDesArchs = objIni.getRutadesexesaplsusu()+ "/" + objApp.getExeFieldNameApp() + "/" + type;
		
		createLocalDirectoryFolders(ls_RutaDesArchs);
		
		// 1. Copy all the files from the FTP Server Directory we want to the local directory app we want to update
		boolean download = downloadAppFilesFTP(ls_RutaOriArchs, ls_RutaDesArchs, type, updater, objApp, objIni);
		
		if(download) {
			return 1;
		}else {
			// SHOW MESSAGE THE downloadAppFilesFTP WAS UNSUCCESS
			return 0;
		}
	}

	@Override
	public int copyAppResourcesToUser(String type,FtpUpdater updater, Application objApp, IniFile objIni) {
		String	ls_RutaOriArchs, ls_RutaDesArchs;
		
		//ls_RutaOriArchs = objIni.getRutadesrcrsapls()+ "/" + objApp.getExeFieldNameApp();
		ls_RutaOriArchs = objIni.getRutadesrcrsapls();
		ls_RutaDesArchs = objIni.getRutadesexesaplsusu()+ "/" + objApp.getExeFieldNameApp() + "/" + type;
		
		createLocalDirectoryFolders(ls_RutaDesArchs);
		
		// 1. Copy all the resources from the FTP Server Directory we want to the local directory app we want to update
		boolean download = downloadResourcesFTP(ls_RutaOriArchs, ls_RutaDesArchs, type, updater, objApp, objIni);
		
		if(download) {
			return 1;
		}else {
			// SHOW MESSAGE THE downloadAppFilesFTP WAS UNSUCCESS
			return 0;
		}
	}

	@Override
	public int createLocalDirectoryFolders(String localDirectory) {
		File directory = new File(localDirectory);
		// Validate if the directory doesn't exist
		try
		{
			if(!directory.exists())
			{
				// We create the directory we specified as the parameter "localDirectory"
				// and then, we validate if the creation was successful
				if(directory.mkdir())
				{
					// Set to the log we created the path
				}
				else 
				{
					// Set to the log there was an error while the creation of the path
				}
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			// Set to the log error
		}		
	
		return 1;
	}

	@Override
	public boolean downloadAppFilesFTP(String oriFilesPath, String downFilesPath,String type,FtpUpdater updater, Application objApp, IniFile objIni) {
		List<FTPFile> lstFiles = new ArrayList<FTPFile>();
		FTPClient ftp = new FTPClient();
		//ftp.setUseEPSVwithIPv4( true );
		updaterView.pbDescargaArchivo.setValue(0);
		try
		{
			ftp.connect(updater.getServerHost());
			boolean login = ftp.login(updater.getUser(), updater.getPassword());
			ftp.enterLocalPassiveMode();
			String remoteDIR = updater.getRemoteDIR()+oriFilesPath;
			ftp.changeWorkingDirectory(remoteDIR);
			FTPFile[] files = ftp.listFiles();
			
			if(login) {				
				// Validating files in the server directory
				if(files != null && files.length > 0) {
					// Reading each file inside the directory
					for(FTPFile fl : files) {
						if(fl.getName().equals(objApp.getExeFieldNameApp()+".zip")) {
							lstFiles.add(fl);
							break;
						}
					}
				}
				
				String iniFilePath = "";
				FTPFile[] filesToGetIni = ftp.listFiles();
				if(filesToGetIni != null && filesToGetIni.length > 0) {
					for(FTPFile file : filesToGetIni) {
						if(file.getName().equals(objIni.getConfigurationFileName())) {
							switch(type) {
								case "Desa":
										iniFilePath = updater.getRemoteDIR()+oriFilesPath+"/Desa";										
									break;
								case "Sist":
										iniFilePath = updater.getRemoteDIR()+oriFilesPath+"/Sist";										
									break; 
								case "Prod":
										lstFiles.add(file);										
									break;
								default:
									break;
									
							}
							if(type == "Desa" || type == "Sist") {
								ftp.changeWorkingDirectory("/");
								ftp.changeWorkingDirectory(iniFilePath);
								FTPFile[] iniFileList = ftp.listFiles();
								for(FTPFile iniFile : filesToGetIni) {
									if(iniFile.getName().equals(objIni.getConfigurationFileName())) {
										lstFiles.add(file);
										break;
									}
								}
							}							
							break;
						}					
					}
					ftp.logout();
					ftp.disconnect();
				}		
			}
			
			
			if(lstFiles.isEmpty()) 
			{
				// Show an alert  : "There are not files to download"
			}
			else 
			{
				
				String logFilePath = objIni.getRutalogprocusu()+"/"+objApp.getExeFieldNameApp();					
				createLocalDirectoryFolders(logFilePath);
				String logDIR = logFilePath+"/"+objApp.getName()+type+".log";
				Path logpath = Paths.get(logDIR);
				FileWriter myWriter = null;
				
				boolean delete = Files.deleteIfExists(logpath);
				if(!delete) {
					// MESSAGE TO ALERT THAT THE LOG FILE WASN'T DELETED
				}
				File logFile = new File(logDIR);
				if(logFile.createNewFile()) {						
					
					myWriter = new FileWriter(logDIR);
				}else {
					// MESSAGE TO ALERT THAT THE LOG FILE WASN'T CREATED						
				}
				Date date = Calendar.getInstance().getTime();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
                String strDate = dateFormat.format(date);  
			    myWriter.write("=====================================================================\n");
			    myWriter.write("PROCESO: ACTUALIZACIÓN DE ARCHIVOS DE " + objApp.getName()+"\n");
			    myWriter.write("=====================================================================\n");
			    myWriter.write("\n");
			    myWriter.write("ACCIÓN: Descarga de archivos\n");
			    myWriter.write("\n");
			    myWriter.write("Inicio del proceso: " + strDate+"\n");				    
			    myWriter.write("\n");
			    
				int countFiles = -1;
				int countDownloadedFiles = 0;
				int countErrFiles = 0;
				int percent = 0;
				int total = lstFiles.size();//files.length;
				int downloadPercent = 0;					
				// DOWNLOAD FILES ======================================================>>>>>>>>>>>>>>>>>>>>>>
				// Iterate the lstFiles and start to download each file inside the list
				OutputStream outFiletoDownload = null;
				for(FTPFile fileToDownload : lstFiles) {
					ftp = new FTPClient();
					ftp.setControlKeepAliveTimeout(600);
					Date currentdate = Calendar.getInstance().getTime();
					String time = dateFormat.format(currentdate ); 
					
					ftp.connect(updater.getServerHost());
					login = ftp.login(updater.getUser(), updater.getPassword());
					//ftp.setControlKeepAliveReplyTimeout(300);
					ftp.enterLocalPassiveMode();
					String serverDIR = updater.getRemoteDIR()+oriFilesPath;
					ftp.changeWorkingDirectory(serverDIR);		 			
					
					String currentFilePathtoDownload = downFilesPath;
					updaterView.lblNombreArchivoDescarga.setText("Descargando "+fileToDownload.getName());
					outFiletoDownload = new FileOutputStream(currentFilePathtoDownload+"/"+fileToDownload.getName());
					
					if(ftp.retrieveFile(fileToDownload.getName(), outFiletoDownload)) {
						countDownloadedFiles++;
						myWriter.write(currentFilePathtoDownload+"/"+fileToDownload.getName()+" - Se descargó el archivo con éxito."+"\n");
					}else {
						countErrFiles++;
						myWriter.write(currentFilePathtoDownload+"/"+fileToDownload.getName()+" - Problemas al descargar el archivo."+"\n");							
											
					}
					// To  debug
					System.out.print("\n");
					System.out.print(time+"\n");
					System.out.print("Descargando en :"+currentFilePathtoDownload+" \n");					
					System.out.print(fileToDownload.getName()+"\n");
					System.out.print(downloadPercent+"% \n");					
					System.out.print("\n");
					System.out.print("============================\n");
					
					// To  debug
					
					countFiles++;
					downloadPercent = 100 * (countFiles + 1) / total;
					updaterView.pbDescargaArchivo.setValue(downloadPercent);

					ftp.logout();
					ftp.disconnect();	 
				}
				
				myWriter.write("\n");
				myWriter.write("Total de archivos para descargar	: "+countFiles+"\n");
				myWriter.write("Total de archivos actualizados		: "+countDownloadedFiles+"\n");
				myWriter.write("Total de archivos actualizados		: "+countErrFiles+"\n");
				myWriter.write("\n");
				myWriter.write("Fin del proceso: "+strDate+"\n");
				
				// DOWNLOAD FILES <<<<<<<<<<<<<<<<<<======================================================
				myWriter.write("\n");
			    myWriter.write("ACCIÓN: Descomprimir archivos\n");
				
				
				myWriter.close();
				outFiletoDownload.close();					
			}
			
		}		
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
			// Save error in the log
		}
		return true;
	}

	@Override
	public boolean downloadResourcesFTP(String oriFilesPath, String downFilesPath,String type, FtpUpdater updater, Application objApp, IniFile objIni) {
		List<FTPFile> lstFiles = new ArrayList<FTPFile>();
		FTPClient ftp = new FTPClient();
		ftp.setUseEPSVwithIPv4( true );
		updaterView.pbDescargaArchivo.setValue(0);
		try
		{
			ftp.connect(updater.getServerHost());
			boolean login = ftp.login(updater.getUser(), updater.getPassword());
			ftp.enterLocalPassiveMode();
			String resourcesPath = updater.getRemoteDIR()+oriFilesPath;
			ftp.changeWorkingDirectory(resourcesPath);
			FTPFile[] files = ftp.listFiles();
			
			if(login) {				
				// Validating files in the server directory
				if(files != null && files.length > 0) {
					// Reading each file inside the directory
					for(FTPFile fl : files) {
						if(fl.getName().equals("Recursos.zip")) {
							lstFiles.add(fl);
							break;
						}						
					}
					
					ftp.logout();
					ftp.disconnect();	
				}
			}
			
			if(lstFiles.isEmpty()) 
			{
				// Show an alert  : "There are not files to download"
			}
			else 
			{
				
				OutputStream outFiletoDownload = null;
				String logFilePath = objIni.getRutalogprocusu()+"/"+objApp.getExeFieldNameApp();
				
				createLocalDirectoryFolders(logFilePath);
				String logDIR = logFilePath+"/"+objApp.getName()+type+"_REC_.log";
				Path logpath = Paths.get(logDIR);
				FileWriter myWriter = null;
				
				boolean delete = Files.deleteIfExists(logpath);
				if(!delete) {
					// MESSAGE TO ALERT THAT THE LOG FILE WASN'T DELETED
				}
				File logFile = new File(logDIR);
				if(logFile.createNewFile()) {						
					
					myWriter = new FileWriter(logDIR);
				}else {
					// MESSAGE TO ALERT THAT THE LOG FILE WASN'T CREATED						
				}
				Date date = Calendar.getInstance().getTime();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
                String strDate = dateFormat.format(date);  
			    myWriter.write("=====================================================================\n");
			    myWriter.write("PROCESO: DESCARGA DE ARCHIVOS RECURSOS DE " + objApp.getName()+"\n");
			    myWriter.write("=====================================================================\n");
			    myWriter.write("\n");
			    myWriter.write("ACCIÓN: Descargar recursos\n");
			    myWriter.write("\n");
			    myWriter.write("Inicio del proceso: " + strDate+"\n");				    
			    myWriter.write("\n");
			    
				int countFiles = -1;
				int countDownloadedFiles = 0;
				int countErrFiles = 0;
				int percent = 0;
				int total = lstFiles.size();//files.length;
				int downloadPercent = 0;					 
				
				// Iterate the lstFiles and start to download each file inside the list
				for(FTPFile fileToDownload : lstFiles) {
					ftp.setControlKeepAliveTimeout(600);
					ftp = new FTPClient();
					Date currentdate = Calendar.getInstance().getTime();
					String time = dateFormat.format(currentdate );
					
					ftp.connect(updater.getServerHost());
					login = ftp.login(updater.getUser(), updater.getPassword());
					//ftp.setControlKeepAliveReplyTimeout(300);
					ftp.enterLocalPassiveMode();
					ftp.changeWorkingDirectory(resourcesPath);
					
					String currentFilePathtoDownload = downFilesPath;
					updaterView.lblNombreArchivoDescarga.setText("Descargando "+fileToDownload.getName());
					outFiletoDownload = new FileOutputStream(currentFilePathtoDownload+"/"+fileToDownload.getName());
					
					if(ftp.retrieveFile(fileToDownload.getName(), outFiletoDownload)) {
						countDownloadedFiles++;
						myWriter.write(currentFilePathtoDownload+"/"+fileToDownload.getName()+" - Se descargó el archivo con éxito."+"\n");
					}else {
						countErrFiles++;
						myWriter.write(currentFilePathtoDownload+"/"+fileToDownload.getName()+" - Problemas al descargar el archivo."+"\n");							
					}
					
					countFiles++;
					downloadPercent = 100 * (countFiles + 1) / total;
					updaterView.pbDescargaArchivo.setValue(downloadPercent);
					
					// To  debug
					System.out.print("\n");
					System.out.print(time+"\n");
					System.out.print("Descargando en :"+currentFilePathtoDownload+" \n");
					System.out.print(fileToDownload.getName()+"\n");
					System.out.print(downloadPercent+"% \n");					
					System.out.print("\n");
					System.out.print("============================\n");
					
					// To  debug
					
					ftp.logout();
					ftp.disconnect();	
				}
				myWriter.write("\n");
				myWriter.write("Total de archivos para descargar	: "+countFiles+"\n");
				myWriter.write("Total de archivos actualizados		: "+countDownloadedFiles+"\n");
				myWriter.write("Total de archivos actualizados		: "+countErrFiles+"\n");
				myWriter.write("\n");
				myWriter.write("Fin del proceso: "+strDate+"\n");
				
				myWriter.close();
				outFiletoDownload.close();					
			}
			
		}		
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
			// Save error in the log
		}
		return true;
	}

	@Override
	public boolean copyFileVersion(FtpUpdater updater, Application objApp, IniFile objIni) {
		String originPath 	  = "";
		String toDownloadPath = "";
		FTPClient ftp = new FTPClient();
		try
		{
			ftp.connect(updater.getServerHost());
			
			boolean login = ftp.login(updater.getUser(), updater.getPassword());
			ftp.enterLocalPassiveMode();
			
			originPath = updater.getRemoteDIR()+objIni.getRutades2exesapls()+"/"+objApp.getExeFieldNameApp();
			ftp.changeWorkingDirectory("/");
			ftp.changeWorkingDirectory(originPath);
			
			toDownloadPath =objIni.getRutadesexesaplsusu();
			FTPFile[] files = ftp.listFiles();
			
			OutputStream out = null;
			for(FTPFile file : files) {
				System.out.print(file.getName()+"\n");
				if(file.getName().equals("version.ini")) {					
					String pathToDownloadIniFileVersion = toDownloadPath+"/"+objApp.getExeFieldNameApp()+"/"+file.getName(); 
					out = new FileOutputStream(pathToDownloadIniFileVersion);
					ftp.retrieveFile(file.getName(), out);
					break;
				}				
			}

			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}

}
