package com.eficost.updaterapp.service;

import java.awt.Color;
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

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.eficost.updaterapp.controller.ApplicationController;
import com.eficost.updaterapp.controller.CryptoEficostController;
import com.eficost.updaterapp.entities.Application;
import com.eficost.updaterapp.entities.FtpUpdater;
import com.eficost.updaterapp.entities.IniFile;
import com.eficost.updaterapp.helper.EFIcostHelper;
import com.eficost.updaterapp.view.frmPopUp;
import com.eficost.updaterapp.view.frmUpdaterEFIcost;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;

public class FTPEficostUpdaterServiceImpl implements FTPEficostUpdaterService{

	CryptoEficostController ceController = new CryptoEficostController(); 
	ApplicationController appController = new ApplicationController();
	
	// View
	frmUpdaterEFIcost updaterView = new frmUpdaterEFIcost(); 
	
	@Override
	public void updateEFIcostFiles() {
		// Obteniendo el listado objetos de Aplicaciones con sus datos
		String iniFileName = System.getProperty("user.dir")+"/eficost.ini";
		//String iniFileName  = "C:/Sistemas_Eficost/RRHH/Desa/eficost.ini";
		//System.out.print();
		EFIcostHelper helper = new EFIcostHelper();
		Application[] lstApplications = helper.ARR_APPLICATION;		
		Application objApplication = new Application(); 
		IniFile ini = new IniFile();
		File fileIni = new File(iniFileName); 
		FtpUpdater objFtpUpdater = new FtpUpdater(); 		 
		String initFileDIR = iniFileName;
		
		// Validating the eficost.ini exists in the current path
		if(!fileIni.exists())
		{
			//System.out.print("No hay eficost.ini");
			frmPopUp popUp = new frmPopUp();
			popUp.pnlBckButton.setBackground(new Color(235, 64, 52));
			popUp.pnlDragBar.setBackground(new Color(235, 64, 52));			
			popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
			popUp.lblAlertMessage.setText("<html><font color='red'>No se encuentra el archivo "+iniFileName+"<br>");
			popUp.setVisible(true);
			return;
		}
		
		try 
		{
			// Variables para la actualizacion
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
			
			// Enviando los valores para la actualizacion al objeto IniFile
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
			
			//validating the connection with the server
			boolean connection = validateFTPConnection(objFtpUpdater);
			if(connection == false) {
				frmPopUp popUp = new frmPopUp();
				popUp.pnlBckButton.setBackground(new Color(235, 64, 52));
				popUp.pnlDragBar.setBackground(new Color(235, 64, 52));
				popUp.setVisible(true);
				popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
				popUp.lblAlertMessage.setText("<html><font color='red'>Error al conectarse con el servidor."+
											  "<br>Validar conexión a internet.</font></html>");
				return;
			}
					
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			ini = new IniFile();
			frmPopUp popUp = new frmPopUp();
			popUp.pnlBckButton.setBackground(new Color(235, 64, 52));
			popUp.pnlDragBar.setBackground(new Color(235, 64, 52));
			popUp.setVisible(true);
			popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
			popUp.lblAlertMessage.setText("<html><font color='red'>Error al leer la información del "+iniFileName+
										  "<br>Validar la estructura de "+iniFileName+"</font></html>");
			return;
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

		frmPopUp popUp = new frmPopUp();
		// Definiendo el entorno del usuario
		switch(ini.getEnviromentUser()){
			case "SRV" :
				popUp.setVisible(true);
				popUp.lblAlertTitle.setText("Alerta!");
				popUp.lblAlertMessage.setText("Esta versión del actualizador aplicación no maneja el ambiente de usuario : SRV.");				
				break;
			case "USU" :
				int validateVersion =  appController.compararVersion(objFtpUpdater, objApplication, ini);
				if(validateVersion == -1) 
				{
					int numberOfOpenSessions = appController.validarSesionActivas(objApplication.getExeAppName());
					if(numberOfOpenSessions > 0) 
					{
						popUp.setVisible(true);
						popUp.lblAlertTitle.setText("Mensaje :");
						popUp.lblAlertMessage.setText("Tiene ventanas del sistema ejecutándose, cierrelas para actualizar por favor.");
						return;
					}
					else 
					{
						// ACTUALIZAR CARPETAS
						String[] arrFlgsActApp = appController.obtenerFlgsActDeLaAplicacionServer(objFtpUpdater, objApplication, ini);
						objFtpUpdater.setFlgActProd(arrFlgsActApp[0]);
						objFtpUpdater.setFlgActDesa(arrFlgsActApp[1]);
						objFtpUpdater.setFlgActSist(arrFlgsActApp[2]);
						updateClient(objFtpUpdater,objApplication,ini);
						
					}
				}
				else if(validateVersion == 1)
				{
					// SHOW SUCCESSFUL MESSAGE : "THE EFICOST UPDATE WAS SUCCESSFUL"					
					/*popUp.setVisible(true);
					popUp.lblAlertTitle.setText("Mensaje :");
					popUp.lblAlertMessage.setText("El sistema está actualizado.");*/
					// RUN .EXE FILE
					appController.ejecutarAplicación(objApplication, ini);
					return;
				}
				else if(validateVersion == 0)
				{	
					// RUN .EXE FILE
					appController.ejecutarAplicación(objApplication, ini);
					return;
				}
				break;
			default : break;
		}
		return;
	}

	@Override
	public void updateClient(FtpUpdater updater, Application objApp, IniFile objIni) {
		frmPopUp popUp = new frmPopUp();		 
		updaterView.setVisible(true);
		updaterView.lblNombreArchivoDescarga.setText("Preparando descarga...");
		int downloadAFprod = -1, downloadRSprod = -1;
		int downloadAFdesa = -1, downloadRSdesa = -1;
		
		// Validate what is going to be updated (PROD , DESA , SIST)
		if(updater.getFlgActProd().equals("S")) 
		{
			// 1. Copy the app files from  the ftp server
			updaterView.lblTitulo.setText("EFIcost "+objApp.getName()+" - Actualizando Producción");
			downloadAFprod = copyAppFilesToUser("Prod",updater,objApp,objIni);			
			// 2. Copy the app resources from  the ftp server
			updaterView.lblTitulo.setText("EFIcost Recursos - Actualizando Producción");
			downloadRSprod = copyAppResourcesToUser("Prod", updater, objApp, objIni);			
		}
		if(updater.getFlgActDesa().equals("S")) 
		{
			// 1. Copy the app files from  the ftp server
			updaterView.lblTitulo.setText("EFIcost "+objApp.getName()+" - Actualizando Desarrollo");
			downloadAFdesa = copyAppFilesToUser("Desa",updater,objApp,objIni);
			// 2. Copy the app resources from  the ftp server
			updaterView.lblTitulo.setText("EFIcost Recursos - Actualizando Desarrollo");
			downloadRSdesa = copyAppResourcesToUser("Desa", updater, objApp, objIni);
			
		}
		// Solo se utiliza Desarrollo y producción
		/*if(updater.getFlgActSist().equals("S")) 
		{
			// 1. Copy the app files from  the ftp server
			updaterView.lblTitulo.setText("EFIcost "+objApp.getName()+" - Actualizando Sistemas");
			copyAppFilesToUser("Sist",updater,objApp,objIni);
			// 2. Copy the app resources from  the ftp server
			updaterView.lblTitulo.setText("EFIcost Recursos - Actualizando Sistemas");
			copyAppResourcesToUser("Sist", updater, objApp, objIni);
			
		}*/
		// Validando la descarga correcta de los sistemas y sus recursos
		
		String mensajeErrorDeDescarga = "<html><font color='red'>";

		if(downloadAFprod != 1) {
			mensajeErrorDeDescarga += "Error : Al descargar archivos de aplicación de prod.<br>";
		}
		if(downloadRSprod != 1){
			mensajeErrorDeDescarga += "Error : Al descargar recursos de prod.<br>";
		}
		if(downloadAFdesa != 1) {
			mensajeErrorDeDescarga += "Error : Al descargar archivos de aplicación de prod.<br>";
		}
		if(downloadRSdesa != 1){
			mensajeErrorDeDescarga += "Error : Al descargar recursos de desa.<br>";
		}
		mensajeErrorDeDescarga += "Comunicarse con el soporte.";
		mensajeErrorDeDescarga += "</font></html>";
		
		if(downloadAFprod!=1 || downloadRSprod!=1 || downloadAFdesa!=1 || downloadRSdesa!=1) {
			popUp.setVisible(true);			
			popUp.lblAlertTitle.setText("<html><font color='red'>Alerta :</font></html>");
			popUp.lblAlertMessage.setText(mensajeErrorDeDescarga);
			updaterView.dispose();
			return;
		}else {
			boolean download = copyFileVersion(updater, objApp, objIni);
			if(!download) {
				// SHOW ERROR MESSAGE : "THE VERSION FILE DOWNLOAD WAS UNSUCCESSFUL"
				popUp.setVisible(true);
				popUp.lblAlertTitle.setText("<html><font color='red'>Alerta :</font></html>");
				popUp.lblAlertMessage.setText("<html>"
												+ "<font color='red'>Error con el archivo de versión.</font>"
												+ "<br><font color='red'>Revisar el archivo version.ini.</font>"
												+ "<br><font color='red'>Ubicado en el servidor :</font>"
												+ "<br><font color='red'>"+updater.getRemoteDIR()+objIni.getRutades2exesapls()+"/"+objApp.getExeFieldNameApp()+"</font>"
											+ "</html>");
			}			
		}
		// SHOW SUCCESSFUL MESSAGE : "THE EFICOST UPDATE WAS SUCCESSFUL"
		popUp.setVisible(true);
		popUp.lblAlertTitle.setText("Mensaje :");
		popUp.lblAlertMessage.setText("El sistema "+objApp.getName()+" fue actualizado exitosamente.");
		updaterView.dispose();
		// RUN .EXE FILE
		appController.ejecutarAplicación(objApp, objIni);
		return;
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
			frmPopUp popUp = new frmPopUp();
			popUp.setVisible(true);
			popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
			popUp.lblAlertMessage.setText("<html><font color='red'>Error al descargar los archivos de la aplicación.</font></html>");	
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
			frmPopUp popUp = new frmPopUp();
			popUp.setVisible(true);
			popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
			popUp.lblAlertMessage.setText("<html><font color='red'>Error al descargar los archivos de la aplicación.</font></html>");	
			return 0;
		}
	}

	@Override
	public int createLocalDirectoryFolders(String localDirectory) {
		File directory = new File(localDirectory);
		boolean createDir = false;
		// Validate if the directory doesn't exist
		try
		{
			boolean existsDir = directory.exists();			
			if(!existsDir)
			{
				// We create the directory we specified as the parameter "localDirectory"
				// and then, we validate if the creation was successful
				createDir = directory.mkdirs();
				if(createDir)
				{
					// Set to the log we created the path
				}
				else 
				{
					frmPopUp popUp = new frmPopUp();
					popUp.setVisible(true);
					popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
					popUp.lblAlertMessage.setText("<html><font color='red'>No se pudo crear carpeta.</font></html>");	
				}
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			// Set to the log error
			frmPopUp popUp = new frmPopUp();
			popUp.setVisible(true);
			popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
			popUp.lblAlertMessage.setText("<html><font color='red'>Error al crear la carpeta "+localDirectory+"</font></html>");
			return 0;
		}		
	
		return 1;
	}

	@Override
	public boolean downloadAppFilesFTP(String oriFilesPath, String downFilesPath,String type,FtpUpdater updater, Application objApp, IniFile objIni) {
		List<FTPFile> lstFiles = new ArrayList<FTPFile>();
		FTPClient ftp = new FTPClient();
		String iniFilePath = "";
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
						if(fl.getName().equals(objApp.getExeFieldNameApp().toLowerCase()+".zip")) {
							lstFiles.add(fl);
							break;
						}
					}
				}
				
				
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
							if(type.equals("Desa") || type.equals("Sist")) {
								ftp.changeWorkingDirectory("/");
								ftp.changeWorkingDirectory(iniFilePath);
								FTPFile[] iniFileList = ftp.listFiles();
								for(FTPFile iniFile : iniFileList) {
									if(iniFile.getName().equals(objIni.getConfigurationFileName())) {
										lstFiles.add(iniFile);
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
				frmPopUp popUp = new frmPopUp();
				popUp.setVisible(true);
				popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
				popUp.lblAlertMessage.setText("<html><font color='red'>No hay archivos para descargar el Servidor.</font></html>");
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
					
					if((type.equals("Desa") && fileToDownload.getName().equals(objIni.getConfigurationFileName()))) {
						serverDIR = iniFilePath;
					}
					
					ftp.changeWorkingDirectory(serverDIR);		 			
					
					String currentFilePathtoDownload = downFilesPath;
					updaterView.lblNombreArchivoDescarga.setText("Descargando "+fileToDownload.getName());
					outFiletoDownload = new FileOutputStream(currentFilePathtoDownload+"/"+fileToDownload.getName());
					
					if(ftp.retrieveFile(fileToDownload.getName(), outFiletoDownload)) {
						// To  debug
						/*System.out.print("\n");
						System.out.print(time+"\n");
						System.out.print("Descargando en :"+currentFilePathtoDownload+" \n");					
						System.out.print(fileToDownload.getName()+"\n");
						System.out.print(downloadPercent+"% \n");					
						System.out.print("\n");
						System.out.print("============================\n");*/
						
						// To  debug
						countDownloadedFiles++;
						myWriter.write(currentFilePathtoDownload+"/"+fileToDownload.getName()+" - Se descargó el archivo con éxito."+"\n");
					}else {
						countErrFiles++;
						myWriter.write(currentFilePathtoDownload+"/"+fileToDownload.getName()+" - Problemas al descargar el archivo."+"\n");							
											
					}					
					
					countFiles++;
					downloadPercent = 100 * (countFiles + 1) / total;
					updaterView.pbDescargaArchivo.setValue(downloadPercent);

					ftp.logout();
					ftp.disconnect();	 
				}
				Date finishDownloadDate = Calendar.getInstance().getTime();
			    strDate = dateFormat.format(finishDownloadDate); 
				myWriter.write("\n");
				myWriter.write("Total de archivos para descargar	: "+countFiles+"\n");
				myWriter.write("Total de archivos actualizados		: "+countDownloadedFiles+"\n");
				myWriter.write("Total de archivos actualizados		: "+countErrFiles+"\n");
				myWriter.write("\n");
				myWriter.write("Fin del proceso: "+strDate+"\n");
				
				// DOWNLOAD FILES <<<<<<<<<<<<<<<<<<======================================================
				
				// EXTRACT COMPRESSED FILES ======================================================>>>>>>>>>>>>>>>>>>>>>>
				Date zipExtractDate = Calendar.getInstance().getTime();
			    strDate = dateFormat.format(zipExtractDate); 
				
				myWriter.write("\n");
			    myWriter.write("ACCIÓN: Descomprimir archivos\n");
			    myWriter.write("\n");
			    myWriter.write("Inicio del proceso: " + strDate+"\n");				    
			    myWriter.write("\n");
			     
			    // The app zip file
			    int countDescompressFiles = 0;
			    int countErrorDescompressFiles = 0;
			    String zipPath = downFilesPath+"/"+objApp.getExeFieldNameApp().toLowerCase()+".zip";
			    File file = new File(zipPath);			    
			    List<FileHeader> fileHeaderList = new ArrayList<FileHeader>();
			    if(file.exists() && !file.isDirectory()) 
			    {	
			    	try
			    	{	
			    		ZipFile zipFile = new ZipFile(zipPath);
				    	//zipFile.setRunInThread(true);
				    	//ProgressMonitor progressMonitor = zipFile.getProgressMonitor();				    	
			    		fileHeaderList = zipFile.getFileHeaders();
			    		int totalExtract = fileHeaderList.size();//files.length;
						int downloadPercentExtract = 0;
			    		for(FileHeader fileHeader : fileHeaderList) 
			    		{
			    			if(fileHeader.getFileName().endsWith(".pbd") || fileHeader.getFileName().equals(objApp.getExeAppName())) 
			    			{
			    				updaterView.lblNombreArchivoDescarga.setText("Descomprimiendo "+fileHeader.getFileName());			    			
				    			zipFile.extractFile(fileHeader, downFilesPath+"/");
				    			myWriter.write(downFilesPath+"/"+fileHeader.getFileName()+" - Se descomprimió el archivo con éxito."+"\n");

				    			countDescompressFiles++;
				    			downloadPercentExtract = 100 * (countDescompressFiles +1) / totalExtract;
								updaterView.pbDescargaArchivo.setValue(downloadPercentExtract);
			    			}			    			
			    		}
			    		
			    	}
			    	catch(Exception e)
			    	{
			    		e.printStackTrace();
			    	}
			    	finally 
			    	{
			    		try
			    		{
			    			file.delete();
			    		}
			    		catch(Exception e)
			    		{
			    			frmPopUp popUp = new frmPopUp();
							popUp.setVisible(true);
							popUp.lblAlertTitle.setText("Alerta!");
							popUp.lblAlertMessage.setText("Error al eliminar "+objApp.getExeFieldNameApp()+".zip.");
			    		}
			    		
			    	}
			    }
			    else {
			    	frmPopUp popUp = new frmPopUp();
			    	popUp.setVisible(true);
					popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
					popUp.lblAlertMessage.setText("<html><font color='red'>Error al obtener archivo ZIP.</font></html>");
			    }
			    
			    Date finishExtractDate = Calendar.getInstance().getTime();
			    strDate = dateFormat.format(finishExtractDate); 
				myWriter.write("\n");
				myWriter.write("Total de archivos para descomprimir	      : "+fileHeaderList.size()+"\n");
				myWriter.write("Total de archivos descomprimidos		  : "+countErrorDescompressFiles+"\n");
				myWriter.write("Total de archivos descomprimidos con error: "+countErrorDescompressFiles+"\n");
				myWriter.write("\n");
				myWriter.write("Fin del proceso: "+strDate+"\n");
			    
				// EXTRACT COMPRESSED FILES <<<<<<<<<<<<<<<<<<======================================================
				
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
		String resourcesZip = "recursos.zip";
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
						if(fl.getName().equals(resourcesZip)) {
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
				frmPopUp popUp = new frmPopUp();
				popUp.setVisible(true);
				popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
				popUp.lblAlertMessage.setText("<html><font color='red'>No hay recursos para descargar el Servidor.</font></html>");
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
				/*	System.out.print("\n");
					System.out.print(time+"\n");
					System.out.print("Descargando en :"+currentFilePathtoDownload+" \n");
					System.out.print(fileToDownload.getName()+"\n");
					System.out.print(downloadPercent+"% \n");					
					System.out.print("\n");
					System.out.print("============================\n");
					*/
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
				
				
				myWriter.write("\n");
			    myWriter.write("ACCIÓN: Descomprimir archivos\n");
			    myWriter.write("\n");
			    myWriter.write("Inicio del proceso: " + strDate+"\n");				    
			    myWriter.write("\n");
			     
			    // The app zip file
			    int countDescompressFiles = 0;
			    int countErrorDescompressFiles = 0;
			    String zipPath = downFilesPath+"/"+resourcesZip;
			    File file = new File(zipPath);			    
			    List<FileHeader> fileHeaderList = new ArrayList<FileHeader>();
			    if(file.exists() && !file.isDirectory()) 
			    {	
			    	try
			    	{
			    		
						int downloadPercentExtract = 0;
						
			    		ZipFile zipFile = new ZipFile(zipPath);
				    	//zipFile.setRunInThread(true);
				    	//ProgressMonitor progressMonitor = zipFile.getProgressMonitor();				    	
			    		fileHeaderList = zipFile.getFileHeaders();
			    		int totalExtract = fileHeaderList.size();//files.length;
			    		for(FileHeader fileHeader : fileHeaderList) 
			    		{
			    			updaterView.lblNombreArchivoDescarga.setText("Descomprimiendo "+fileHeader.getFileName());	
			    			
		    				myWriter.write(downFilesPath+"/"+fileHeader.getFileName()+" - Se descomprimió el archivo con éxito."+"\n");
		    				zipFile.extractFile(fileHeader, downFilesPath+"/");
		    				countDescompressFiles++;			    						
			    			downloadPercentExtract = 100 * (countDescompressFiles + 1) / totalExtract;
							updaterView.pbDescargaArchivo.setValue(downloadPercentExtract);
			    		}
			    	}
			    	catch(Exception e)
			    	{
			    		e.printStackTrace();
			    	}
			    	finally 
			    	{
			    		try
			    		{
			    			file.delete();
			    		}
			    		catch(Exception e)
			    		{
			    			frmPopUp popUp = new frmPopUp();
							popUp.setVisible(true);
							popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
							popUp.lblAlertMessage.setText("<html><font color='red'>Error al eliminar "+objApp.getExeFieldNameApp()+".zip.</font></html>");
			    		}
			    		
			    		
			    	}
			    }
			    else {
			    	// SHOW MESSAGE "FILE ZIP DOESN'T EXIST"
			    }
			    
			    Date finishExtractDate = Calendar.getInstance().getTime();
			    strDate = dateFormat.format(finishExtractDate); 
				myWriter.write("\n");
				myWriter.write("Total de archivos para descomprimir	      : "+fileHeaderList.size()+"\n");
				myWriter.write("Total de archivos descomprimidos		  : "+countErrorDescompressFiles+"\n");
				myWriter.write("Total de archivos descomprimidos con error: "+countErrorDescompressFiles+"\n");
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
		
		updaterView.lblTitulo.setText("EFIcost | Estamos culminando la actualización.");
		
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
				//System.out.print(file.getName()+"\n");
				if(file.getName().equals("version.ini")) {
					updaterView.pbDescargaArchivo.setValue(0);
					updaterView.lblNombreArchivoDescarga.setText("Descargando "+file.getName());	
					String pathToDownloadIniFileVersion = toDownloadPath+"/"+objApp.getExeFieldNameApp()+"/"+file.getName(); 
					out = new FileOutputStream(pathToDownloadIniFileVersion);
					ftp.retrieveFile(file.getName(), out);
					updaterView.pbDescargaArchivo.setValue(100);
					break;
				}				
			}

			return true;
		}
		catch(Exception e)
		{
			frmPopUp popUp = new frmPopUp();
			popUp.setVisible(true);
			popUp.lblAlertTitle.setText("<html><font color='red'>Alerta!</font></html>");
			popUp.lblAlertMessage.setText("<html><font color='red'>Error al descargar archivo de versión.</font></html>");
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean validateFTPConnection(FtpUpdater updater) {

		FTPClient ftp = new FTPClient();
		boolean connection = false;
		try
		{
			ftp.connect(updater.getServerHost());
			boolean login = ftp.login(updater.getUser(), updater.getPassword());
			if(login) {		
				connection = true;
			}else{
				connection = false;
			}
			ftp.logout();
			ftp.disconnect();
		}catch(Exception e) {
			connection = false;
		}
		return connection;
	}

}
