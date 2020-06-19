package com.eficost.updaterapp.helper;

import com.eficost.updaterapp.entities.Application;

public class EFIcostHelper { 
	public  Application[] ARR_APPLICATION = null;
	private Application CONTABILIDAD = new Application();	
	private Application FACTURACION = new Application();
	private Application RRHH = new Application();
	private Application CRON = new Application();
	
	public EFIcostHelper() {	
		// Setting Contabilidad properties values
		CONTABILIDAD.setId("2");
		CONTABILIDAD.setName("CONTABILIDAD");
		CONTABILIDAD.setExeAppName("contabilidad.exe");
		CONTABILIDAD.setPbdApplicationName("2_CONT");
		CONTABILIDAD.setPbdGlobalName("0_APGL");
		CONTABILIDAD.setExeFieldNameApp("Contabilidad");
		CONTABILIDAD.setEmpExClu("");
		// Setting Facturacion properties values
		FACTURACION.setId("3");
		FACTURACION.setName("FACTURACION");
		FACTURACION.setExeAppName("facturacion.exe");
		FACTURACION.setPbdApplicationName("3_FACT");
		FACTURACION.setPbdGlobalName("0_APGL");
		FACTURACION.setExeFieldNameApp("Facturacion");
		FACTURACION.setEmpExClu("");
		// Setting RRHH properties values
		RRHH.setId("7");
		RRHH.setName("RECURSOS_HUMANOS");
		RRHH.setExeAppName("recursos_humanos.exe");
		RRHH.setPbdApplicationName("4_RRHH");
		RRHH.setPbdGlobalName("0_FMWK");
		RRHH.setExeFieldNameApp("RRHH");
		RRHH.setEmpExClu("");
		// Setting CRON properties values
		CRON.setId("9");
		CRON.setName("CRONOGRAMA");
		CRON.setExeAppName("cronos.exe");
		CRON.setPbdApplicationName("8_CRON");
		CRON.setPbdGlobalName("");
		CRON.setExeFieldNameApp("Cronos");
		CRON.setEmpExClu("");
		
		ARR_APPLICATION = new Application[]{CONTABILIDAD,FACTURACION,RRHH,CRON};
	}
}
