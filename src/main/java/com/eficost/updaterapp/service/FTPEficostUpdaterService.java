package com.eficost.updaterapp.service;

import com.eficost.updaterapp.entities.Application;
import com.eficost.updaterapp.entities.FtpUpdater;
import com.eficost.updaterapp.entities.IniFile;

public interface FTPEficostUpdaterService {
	void updateEFIcostFiles();
	void updateClient(FtpUpdater updater, Application objApp, IniFile objIni);
	int copyAppFilesToUser(String type,FtpUpdater updater, Application objApp, IniFile objIni);
	int copyAppResourcesToUser(String type,FtpUpdater updater, Application objApp, IniFile objIni);
	int createLocalDirectoryFolders(String localDirectory);
	boolean downloadAppFilesFTP(String oriFilesPath, String downFilesPath,String type,FtpUpdater updater, Application objApp, IniFile objIni);
	boolean downloadResourcesFTP(String oriFilesPath, String downFilesPath,String type,FtpUpdater updater, Application objApp, IniFile objIni);
	boolean copyFileVersion(FtpUpdater updater, Application objApp, IniFile objIni);
	boolean validateFTPConnection(FtpUpdater updater);
}
