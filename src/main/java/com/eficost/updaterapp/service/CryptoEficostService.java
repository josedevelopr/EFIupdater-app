package com.eficost.updaterapp.service;

import com.eficost.updaterapp.entities.CryptoEficost;

public interface CryptoEficostService {
	String sha256Encrypt(CryptoEficost objCE);
	String dupterEncrypt(CryptoEficost objCE);
	String dupterDecrypt(CryptoEficost objCE);
	String jdupterEncrypt(CryptoEficost objCE);
	String jdupterDecrypt(CryptoEficost objCE);	
	String getDecryptedPropertieFromSection(String DIRLocalIniFile, String section,  String PropertieName);	
}
