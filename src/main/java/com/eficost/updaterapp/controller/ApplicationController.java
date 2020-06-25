package com.eficost.updaterapp.controller;

import com.eficost.updaterapp.entities.Application;
import com.eficost.updaterapp.entities.FtpUpdater;
import com.eficost.updaterapp.entities.IniFile;
import com.eficost.updaterapp.service.ApplicationServiceImpl;

public class ApplicationController {
	private ApplicationServiceImpl service = new ApplicationServiceImpl();
	
	public int compararVersion(FtpUpdater updater, Application objApp, IniFile objIni) {
		return service.checkVersion(updater, objApp, objIni);
	}
	
	public String obtenerVersionDeAplicacion(FtpUpdater updater, Application objApp, IniFile objIni, String type) {
		return service.appVersion(updater, objApp, objIni, type);
	}
	
	public int validarSesionActivas(String exeAppName) {
		return service.checkAppSessions(exeAppName);
	}	
	
	public String[] obtenerFlgsActDeLaAplicacionServer(FtpUpdater updater, Application objApp, IniFile objIni) {
		return service.getFlgsActAppServer(updater, objApp, objIni);
	}
	
	public void ejecutarAplicación(Application objApp, IniFile objIni) {
		service.openApplication(objApp, objIni);
	}
	
}
