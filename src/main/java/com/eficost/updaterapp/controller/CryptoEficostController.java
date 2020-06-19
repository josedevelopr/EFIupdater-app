package com.eficost.updaterapp.controller;

import com.eficost.updaterapp.entities.CryptoEficost;
import com.eficost.updaterapp.service.CryptoEficostServiceImpl;

public class CryptoEficostController {
	
	// Defining an explicit constructor
	private CryptoEficostServiceImpl service;
	{
		try 
		{
			service = new CryptoEficostServiceImpl();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public String encriptadoDavid(CryptoEficost objCE) {
		return service.dupterEncrypt(objCE);
	}
	
	public String desencriptadoDavid(CryptoEficost objCE) {
		return service.dupterDecrypt(objCE);
	}
	
	public String encriptadoJose(CryptoEficost objCE) {
		return service.jdupterEncrypt(objCE);
	}
	
	public String desencriptadoJose(CryptoEficost objCE) {
		return service.jdupterDecrypt(objCE);
	}
	
	public String obtenerValorDePropiedadDesencriptado(String localIniFileDIR, String section,  String propertieName){
		return service.getDecryptedPropertieFromSection(localIniFileDIR, section, propertieName);
	}
	public String encriptadoSHA256(CryptoEficost objCE) {
		return service.sha256Encrypt(objCE);		
	}
}
