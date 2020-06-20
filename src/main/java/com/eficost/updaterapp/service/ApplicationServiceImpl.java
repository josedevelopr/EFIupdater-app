package com.eficost.updaterapp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

import com.eficost.updaterapp.entities.Application;
import com.eficost.updaterapp.entities.FtpUpdater;
import com.eficost.updaterapp.entities.IniFile;


public class ApplicationServiceImpl implements ApplicationService { 
	
	@Override
	public int checkVersion(FtpUpdater updater, Application objApp, IniFile objIni) {
		
		String serverVersion = null;
		String localVersion = null;  
		
		try 
		{
			serverVersion = appVersion(updater, objApp, objIni, "server");
			localVersion = appVersion(updater, objApp, objIni, "local"); 
			
			if(!serverVersion.equals(localVersion)) 
			{
				return -1;
			}
			else {
				return 1;
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		
		return 1;
	}

	@Override
	public String appVersion(FtpUpdater updater, Application objApp, IniFile objIni, String type) {
		
		FTPClient objFtpClient 	= new FTPClient(); // => Object to connect to a FTP Server
		Ini 	  ini 			= new Wini(); 	   // => Object to manage with a Ini File
		Config 	  conf 			= new Config();	   // => Configuration of a Ini File
		String    version 		= "";
		Section section;	// => Object to manage section of a Ini file
		try 
		{	
			switch(type) // Validating the type
			{
				case "local" :
					
					//ini.load(new File(updater.getLocalDIR()+"/"+objApp.getExeFieldNameApp()+"/version.ini"));
					String iniLocalPath = objIni.getRutadesexesaplsusu()+"/"+objApp.getExeFieldNameApp()+"/version.ini";
					ini.load(new File(iniLocalPath)); 
					section = ini.get("VERSION");
					version = section.get("Fecha");
					break;
					
				case "server" :
					
					objFtpClient.connect(updater.getServerHost()); // Connecting to the FTP Server
					boolean login = objFtpClient.login(updater.getUser(), updater.getPassword()); // Logging to the FTP 
					objFtpClient.enterLocalPassiveMode();  
					objFtpClient.changeWorkingDirectory(updater.getRemoteDIR()); // Changing the directory in the FTP Server
					FTPFile[] files = objFtpClient.listFiles(); // Getting all the files inside the directory we want
					
					if(login) // Validating the login 
					{
						if(files != null && files.length >0) 
						{
							/// Loading the file version.ini in the path we specify
							//ini.load(new InputStreamReader(objFtpClient.retrieveFileStream("/"+updater.getRemoteDIR()+"/"+objApp.getExeFieldNameApp()+"/version.ini")));
							String iniPath = "/"+updater.getRemoteDIR()+objIni.getRutades2exesapls()+"/"+objApp.getExeFieldNameApp()+"/version.ini";
							ini.load(new InputStreamReader(objFtpClient.retrieveFileStream(iniPath)));
							section = ini.get("VERSION");
							version = section.get("Fecha");							
						}
					}
					objFtpClient.logout();
					objFtpClient.disconnect();
					
					break;
					
				default :
					version = "";
					break;
			}			
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//VALIDAR QUE EL ARCHIVO DE VERSIÓN NO SE ENCUENTRA
		}		
		return version;
	}

	@Override
	public int checkAppSessions(String exeAppName) {
		 
		String line; // => Variable to iterate each running app
		int validateRunningApp = 0;
		try
		{
			// Object to get all the current running app 
			Process process = Runtime.getRuntime().exec
		    	    (System.getenv("windir") +"\\system32\\"+"tasklist.exe /fo csv /nh");
			// Object to read the variable 'p'
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
						
			// Iterating all the running apps
			while ((line = input.readLine()) != null) 
			{
    	        String[] arrRunningApp =  line.split(","); // Parsing to Array to get only the name (position 0)
    	        String nameApp = arrRunningApp[0].replace("\"", ""); // Removing " (double quotes) from the beginning and the end of the app name
    	        if(nameApp.equals(exeAppName)) // Comparing the name of the app we are looking for
    	        {
    	        	validateRunningApp += 1;
    	        }
    	    }
			input.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return validateRunningApp;
	}

	@Override
	public String[] getFlgsActAppServer(FtpUpdater updater, Application objApp, IniFile objIni) {
		FTPClient objFtpClient 	= new FTPClient(); // => Object to connect to a FTP Server
		Ini 	  ini 			= new Wini(); 	   // => Object to manage with a Ini File
		Config 	  conf 			= new Config();	   // => Configuration of a Ini File
		String[] arrFlgsActAppServer = new String[3];
		Section section;	// => Object to manage section of a Ini file
		try 
		{						
			objFtpClient.connect(updater.getServerHost()); // Connecting to the FTP Server
			boolean login = objFtpClient.login(updater.getUser(), updater.getPassword()); // Logging to the FTP 
			objFtpClient.enterLocalPassiveMode();  
			objFtpClient.changeWorkingDirectory(updater.getRemoteDIR()); // Changing the directory in the FTP Server
			FTPFile[] files = objFtpClient.listFiles(); // Getting all the files inside the directory we want
			
			if(login) // Validating the login 
			{
				if(files != null && files.length >0) 
				{
					/// Loading the file version.ini in the path we specify
					//String iniPath = "/"+updater.getRemoteDIR()+objIni.getRutades2exesapls()+"/"+objApp.getExeFieldNameApp()+"/version.ini";
					String pathIniApp =  "/"+updater.getRemoteDIR()+objIni.getRutades2exesapls()+"/"+objApp.getExeFieldNameApp()+"/version.ini";
					ini.load(new InputStreamReader(objFtpClient.retrieveFileStream(pathIniApp)));
					section = ini.get("VERSION");
					arrFlgsActAppServer[0] = section.get("FlgActProd");
					arrFlgsActAppServer[1] = section.get("FlgActDesa");
					arrFlgsActAppServer[2] = section.get("FlgActSist");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
		return arrFlgsActAppServer;
	}

}
