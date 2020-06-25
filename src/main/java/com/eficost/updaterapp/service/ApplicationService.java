package com.eficost.updaterapp.service;

import com.eficost.updaterapp.entities.Application;
import com.eficost.updaterapp.entities.FtpUpdater;
import com.eficost.updaterapp.entities.IniFile;

public interface ApplicationService{
	String appVersion(FtpUpdater updater, Application  objApp, IniFile objIni, String type);	
	int checkVersion(FtpUpdater updater, Application objApp, IniFile objIni);
	int checkAppSessions(String exeAppName);
	String[] getFlgsActAppServer(FtpUpdater updater, Application objApp, IniFile objIni);
	void openApplication(Application objApp, IniFile objIni);
}
