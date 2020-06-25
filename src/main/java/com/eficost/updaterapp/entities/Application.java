package com.eficost.updaterapp.entities;

public class Application {
	private String id;
	private String name;
	private String exeAppName;
	private String pbdApplicationName;
	private String pbdGlobalName;
	private String exeFieldNameApp;
	private String empExClu;
	private String exeDesaAppDiracc;
	private String exeProdAppDiracc;
	
	public String getExeDesaAppDiracc() {
		return exeDesaAppDiracc;
	}
	public void setExeDesaAppDiracc(String exeDesaAppDiracc) {
		this.exeDesaAppDiracc = exeDesaAppDiracc;
	}
	public String getExeProdAppDiracc() {
		return exeProdAppDiracc;
	}
	public void setExeProdAppDiracc(String exeProdAppDiracc) {
		this.exeProdAppDiracc = exeProdAppDiracc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExeAppName() {
		return exeAppName;
	}
	public void setExeAppName(String exeAppName) {
		this.exeAppName = exeAppName;
	}
	public String getPbdApplicationName() {
		return pbdApplicationName;
	}
	public void setPbdApplicationName(String pbdApplicationName) {
		this.pbdApplicationName = pbdApplicationName;
	}
	public String getPbdGlobalName() {
		return pbdGlobalName;
	}
	public void setPbdGlobalName(String pbdGlobalName) {
		this.pbdGlobalName = pbdGlobalName;
	}
	public String getExeFieldNameApp() {
		return exeFieldNameApp;
	}
	public void setExeFieldNameApp(String exeFieldNameApp) {
		this.exeFieldNameApp = exeFieldNameApp;
	}
	public String getEmpExClu() {
		return empExClu;
	}
	public void setEmpExClu(String empExClu) {
		this.empExClu = empExClu;
	}
	
}
