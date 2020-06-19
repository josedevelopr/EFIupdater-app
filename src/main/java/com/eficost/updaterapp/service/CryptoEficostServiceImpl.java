package com.eficost.updaterapp.service;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.net.util.Base64;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

import com.eficost.updaterapp.entities.CryptoEficost;

public class CryptoEficostServiceImpl implements CryptoEficostService{
	
	private static final double[] ARRAY_ENCRYPT_VALUES = {3,24,8,10,34,17,20,21,21,3,24,8,10,34,17,20,1,2,6,9,11,3,31,25,16,11,5,22,15,29,11,12,13,16,11,5,2,8,1,18,6,7,31,23,22,10,9,17,33,35,13,27,8,21,20,18,14,3,4,12,20,27,37};
	private static final String UNICODE_FORMAT = "UTF8"; // Formato Unicode de Texto
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede"; // Tipo de Encriptaci�n
    private KeySpec ks;
    private SecretKeyFactory skf; 
    private Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;
	
	public CryptoEficostServiceImpl() throws Exception{
		// Enviando palabra clave para la encriptaci�n, debe ser tener un lenght al menos de 24
        myEncryptionKey = "EFIcostFTPserverUpdater03062020";//"myphraseISEFIcost03062020"; 
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME; // Seteando el esquema
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT); // Obteniendo los bytes de nuestra palabra clave
        ks = new DESedeKeySpec(arrayBytes); // Obteniendo la encriptaci�n de nuestro array
        skf = SecretKeyFactory.getInstance(myEncryptionScheme); // Instancia de la encriptaacion 
        /*cipher = cifrador*/
        cipher = Cipher.getInstance(myEncryptionScheme); // Entregando la instancia al cifrador 
        key = skf.generateSecret(ks); // Generando el secret key para el key spec
	}
	
	@Override
	public String dupterEncrypt(CryptoEficost objCE) {
		String encryptWord = "";
		for(int i = 0; i < objCE.getUnencryptedText().length(); i++) {
			
			int asciiCharacter = (int) objCE.getUnencryptedText().charAt(i);
			int newAscii = (int) (asciiCharacter);
			newAscii+= ARRAY_ENCRYPT_VALUES[i];
			String newCharacter = Character.toString ((char) newAscii) ;
			encryptWord += newCharacter;  
		}
		return encryptWord;
	}

	@Override
	public String dupterDecrypt(CryptoEficost objCE) {				
		String encryptWord = "";
		for(int i = 0; i < objCE.getUnencryptedText().length(); i++) {
			
			char asciiCharacter = objCE.getUnencryptedText().charAt(i);
			int newAscii = (int) (asciiCharacter);
			newAscii-=ARRAY_ENCRYPT_VALUES[i];
			char newCharacter = (char) newAscii;
			encryptWord += newCharacter;  
		}
		return encryptWord;
	}

	@Override
	public String jdupterEncrypt(CryptoEficost objCE) {
		String encryptedString = null;
		
		try 
		{
			cipher.init(Cipher.ENCRYPT_MODE,key);
			byte[] plainText = objCE.getUnencryptedText().getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = new String(Base64.encodeBase64(encryptedText));
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		return encryptedString;
	}

	@Override
	public String jdupterDecrypt(CryptoEficost objCE) {
		String decryptedText = null;
		
		try
		{
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptedText = Base64.decodeBase64(objCE.getUnencryptedText());
			byte[] plainText = cipher.doFinal(encryptedText);
			decryptedText = new String(plainText);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return decryptedText;
	}

	@Override
	public String getDecryptedPropertieFromSection(String localIniFileDIR, String section,  String propertieName) {
		 Ini ini = new Wini(); 
		 Config conf = new Config();
		 CryptoEficost crypto = new CryptoEficost();
		 
		 String encryptedPropertie = "";
		 //String property = "";
		 String propertieValue = "";
		 
		 conf.setMultiOption(true);
		 ini.setConfig(conf);
		 try {
			 ini.load(new File(localIniFileDIR));
			 Section iniSection = ini.get(section);	
			 crypto.setUnencryptedText(propertieName);
			 encryptedPropertie = sha256Encrypt(crypto);
			 crypto.setUnencryptedText(iniSection.get(encryptedPropertie));
			 propertieValue = jdupterDecrypt(crypto);			 
			 
		 } catch (IOException e) {			
			 e.printStackTrace();
		 } 
		
		return propertieValue;
	}

	@Override
	public String sha256Encrypt(CryptoEficost objCE) {		
		String encryptedString = "";
		MessageDigest md = null;
    	try {
    		md = MessageDigest.getInstance("SHA-256");
    	} 
    	catch (NoSuchAlgorithmException e) {		
    		e.printStackTrace();    		
    	}
    	    
    	byte[] hash = md.digest(objCE.getUnencryptedText().getBytes());
    	StringBuffer sb = new StringBuffer();
    	    
    	for(byte b : hash) {        
    		sb.append(String.format("%02x", b));
    	}		
    	encryptedString = sb.toString();
		return encryptedString;
	}

}
