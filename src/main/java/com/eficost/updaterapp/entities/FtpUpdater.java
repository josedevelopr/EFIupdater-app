package com.eficost.updaterapp.entities;

public class FtpUpdater {
	private String serverHost;
	private String user;
	private String password;
	private String remoteDIR;
	private String localDIR;
	private String flgActProd;
	private String flgActDesa;
	private String flgActSist;
	
	public String getServerHost() {
		return serverHost;
	}
	public String getFlgActProd() {
		return flgActProd;
	}
	public void setFlgActProd(String flgActProd) {
		this.flgActProd = flgActProd;
	}
	public String getFlgActDesa() {
		return flgActDesa;
	}
	public void setFlgActDesa(String flgActDesa) {
		this.flgActDesa = flgActDesa;
	}
	public String getFlgActSist() {
		return flgActSist;
	}
	public void setFlgActSist(String flgActSist) {
		this.flgActSist = flgActSist;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRemoteDIR() {
		return remoteDIR;
	}
	public void setRemoteDIR(String remoteDIR) {
		this.remoteDIR = remoteDIR;
	}
	public String getLocalDIR() {
		return localDIR;
	}
	public void setLocalDIR(String localDIR) {
		this.localDIR = localDIR;
	}	
}


