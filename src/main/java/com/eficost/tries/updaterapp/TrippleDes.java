package com.eficost.tries.updaterapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.net.util.Base64;

public class TrippleDes {

    private static final String UNICODE_FORMAT = "UTF8"; // Formato Unicode de Texto
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede"; // Tipo de Encriptaci�n
    private KeySpec ks;
    private SecretKeyFactory skf; 
    private Cipher cipher;
    byte[] arrayBytes; 
    private String myEncryptionKey;
    private String myEncryptionScheme; 
    SecretKey key;

    public TrippleDes() throws Exception { 
    	// Enviando palabra clavepara la encriptaci�n, debe ser tener un lenght al menos de 24
        myEncryptionKey = "EFIcostFTPserverUpdater03062020";//"myphraseISEFIcost03062020"; 
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME; // Seteando el esquema
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT); // Obteniendo los bytes de nuestra palabra clave
        ks = new DESedeKeySpec(arrayBytes); // Obteniendo la encriptaci�n de nuestro array
        skf = SecretKeyFactory.getInstance(myEncryptionScheme); // Instancia de la encriptaacion 
        /*cipher = cifrador*/
        cipher = Cipher.getInstance(myEncryptionScheme); // Entregando la instancia al cifrador 
        key = skf.generateSecret(ks); // Generando el secret key para el key spec
    }


    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key); // Inicializando el cifrador
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT); // Obteniendo los bytes del string a encriptar
            byte[] encryptedText = cipher.doFinal(plainText); // Encriptando el string
            encryptedString = new String(Base64.encodeBase64(encryptedText)); // Convirtiendo a String nuestro texto encriptado 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }
 

    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key); // Inicializando el cifrador
            byte[] encryptedText = Base64.decodeBase64(encryptedString); // decodificando el texto encriptado en Base 64
            byte[] plainText = cipher.doFinal(encryptedText); // Desencriptando el texto
            decryptedText= new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }


    public static void main(String args []) throws Exception
    {
    	/*CryptoEficostController td= new CryptoEficostController();
       
       
        String target="2"; 
        CryptoEficost eficost = new CryptoEficost();
        eficost.setUnencryptedText(target);
        String encrypted=td.encriptadoJose(eficost);
        eficost.setUnencryptedText(encrypted);
        String decrypted=td.desencriptadoJose(eficost);

        System.out.println("String To Encrypt: "+ target);
        System.out.println("Encrypted String:" + encrypted);
        System.out.println("Decrypted String:" + decrypted);*/
         /*
        MessageDigest md = null;
    	try {
    		md = MessageDigest.getInstance("SHA-256");
    	} 
    	catch (NoSuchAlgorithmException e) {		
    		e.printStackTrace();    		
    	}
    	    
    	byte[] hash = md.digest(target.getBytes());
    	StringBuffer sb = new StringBuffer();
    	    
    	for(byte b : hash) {        
    		sb.append(String.format("%02x", b));
    	}
        // SHA256 Encrypt 
        System.out.println("SHA256 encrypt: " + sb.toString());*/
        
        /*
        Ini ini = new Wini(); 
		Config conf = new Config();
		conf.setMultiOption(true);
		ini.setConfig(conf);
		try {
			ini.load(new File("D:/ftp/EFIcost/Eficost_126/eficost.ini"));
		} catch (IOException e) {			
			System.out.println(e);
		} 
		
		CryptoEficost crypto = new CryptoEficost();		
		 
		Section section = ini.get("CNX_AGO");		
		
		crypto.setUnencryptedText("FILECONFIG");
		
		String propertie01 = td.encriptadoJose(crypto);
		//String propertieValue = section.get(td.obtenerPropiedadDeUnaSeccionInit(propertie01, "propertie"));		
		
		//String propertieValueDecrypted = td.obtenerPropiedadDeUnaSeccionInit(propertieValue, "value");
		//crypto.setUnencryptedText(propertieValueDecrypted);
		//String decrypted = td.desencriptarPropiedadSeccion(propertieValue);
		System.out.println(propertie01);
		//System.out.println(propertieValueDecrypted);
		System.out.println(decrypted);
		*/
    	/*String toprint = td.obtenerValorDePropiedadDesencriptado("D:/ftp/EFIcost/eficost.ini", 
												               	 "EFIUPDATER", 
												                 "RUTALOGPROCUSU");
       
        System.out.println(toprint);
        
        try {
        	String codigo = "2";
        	EFIcostHelper helper = new EFIcostHelper();
        	Application[] lstApps = helper.ARR_APPLICATION; 
            
            for(Application app : lstApps){
            	System.out.println(app.getName());
            	if(app.getId() == codigo) {
            		break;
            	}            	
            }
        }catch(Exception e) {
        	e.printStackTrace();
        }*/
    	/*
    	try {
    	    String line;
    	    Process p = Runtime.getRuntime().exec
    	    	    (System.getenv("windir") +"\\system32\\"+"tasklist.exe /fo csv /nh");
    	    BufferedReader input =
    	            new BufferedReader(new InputStreamReader(p.getInputStream()));
    	    String app = "TeamViewer.exe";  
    	    int validateRunningApp = 0;
    	    while ((line = input.readLine()) != null) {
    	        String[] arrRunningApp =  line.split(","); //<-- Parse data here.
    	        System.out.print(arrRunningApp[0].replace("\"", "")+" - "+arrRunningApp[0].length()+"\n");
    	        if(arrRunningApp[0].replace("\"", "").equals(app))
    	        {
    	        	validateRunningApp += 1;
    	        }
    	    }    	    
    	    input.close();
    	    if(validateRunningApp == 0) 
    	    {
    	    	System.out.print("No hay sesiones de la aplicaci�n: "+app);
    	    }
    	    else
    	    {
    	    	System.out.print("TIENE sesiones de la aplicaci�n : "+app);
    	    }
    	} catch (Exception err) {
    	    err.printStackTrace();
    	}*/
    	/*
    	Process p = Runtime.getRuntime().exec
    		    (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
    	
    	// wmic => Windows Management Instrumentation Command.
    	if(Runtime.getRuntime().exec("wmic.exe") == null)
    	{
    		System.out.print("No hay sesiones de la aplicaci�n.");
    	}
    	else
    	{
    		System.out.print("Hay sesiones de la aplicaci�n.");
    	}
    	
    	String line;
    	try {
    	        Process proc = Runtime.getRuntime().exec("wmic.exe");// wmic => Windows Management Instrumentation Command.
    	        BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    	        OutputStreamWriter oStream = new OutputStreamWriter(proc.getOutputStream());
    	        oStream .write("process where WindowsVersion='10.0.18362'");
    	        oStream .flush();
    	        oStream .close();
    	        while ((line = input.readLine()) != null) {
    	            System.out.println(line+"\n");
    	        }
    	        input.close();
    	    } catch (IOException ioe) {
    	        ioe.printStackTrace();
    	    }*/   	
    	
    	//Process p = Runtime.getRuntime().exec("C:\\Sistemas_Eficost\\Contabilidad\\Prod\\contabilidad.exe");
    	/*ProcessBuilder p = new ProcessBuilder("C:\\Sistemas_Eficost\\Contabilidad\\Prod\\contabilidad.exe");
    	p.start();*/
    	String cmd;
    	
    	//cmd = "C:\\Sistemas_Eficost\\Facturacion\\Desa\\facturacion.exe";
    	// FUNCIONA PERO SALE ERROR
    	/*Process p=Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " +"C:\\Sistemas_Eficost\\Contabilidad\\Desa\\contabilidad.exe");//The pif file contains the string as listed above
    	Thread needle =new Thread();
    	needle.sleep(((1/120)*1500+15000));*/
    	
    	ProcessBuilder pb = new ProcessBuilder("cmd", "/c",
		                "C:\\Sistemas_Eficost\\Contabilidad\\contabilidad.lnk");
		Process p = pb.start();
		p.waitFor();
    }
    
    private static void runProcess(ProcessBuilder pb) throws IOException {
        pb.redirectErrorStream(true);
        //pb.directory(new File("c:\\Users\\user\\Desktop"));
        Process p = pb.start();        
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

}