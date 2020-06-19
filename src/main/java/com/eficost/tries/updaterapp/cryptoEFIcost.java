package com.eficost.tries.updaterapp;

import java.io.File;
import java.io.IOException;

import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

public class cryptoEFIcost { 
	
	public String encrypt(String valueToEncrypt) {
		double[] arrCryptoValues = {3,24,8,10,34,17,20,21,21,3,24,8,10,34,17,20,1,2,6,9,11,3,31,25,16,11,5,22,15,29,11,12,13,16,11,5,2,8,1,18,6,7,31,23,22,10,9,17,33,35,13,27,8,21,20,18,14,3,4,12,20,27,37};		
		String encryptWord = "";
		for(int i = 0; i < valueToEncrypt.length(); i++) {
			
			int asciiCharacter = (int) valueToEncrypt.charAt(i);
			int newAscii = (int) (asciiCharacter);
			newAscii+= arrCryptoValues[i];
			String newCharacter = Character.toString ((char) newAscii) ;
			encryptWord += newCharacter;  
		}
		return encryptWord;
	}
	
	public String decrypt(String valueToEncrypt) {
		double[] arrCryptoValues = {3,24,8,10,34,17,20,21,21,3,24,8,10,34,17,20,1,2,6,9,11,3,31,25,16,11,5,22,15,29,11,12,13,16,11,5,2,8,1,18,6,7,31,23,22,10,9,17,33,35,13,27,8,21,20,18,14,3,4,12,20,27,37};		
		String encryptWord = "";
		for(int i = 0; i < valueToEncrypt.length(); i++) {
			
			char asciiCharacter = valueToEncrypt.charAt(i);
			int newAscii = (int) (asciiCharacter);
			newAscii-=arrCryptoValues[i];
			char newCharacter = (char) newAscii;
			encryptWord += newCharacter;  
		}
		return encryptWord;
	}
	
	public static void main(String[] args) {	
		cryptoEFIcost crypto = new cryptoEFIcost();
		//System.out.print("It Works!");		
		
		/*
		System.out.print(crypto.decrypt("GRdK’}}xvfwx‡„p4aKspf”qmq{‚y<kRVTHQ[Uq79UsbYP"));*/
		/*
		BufferedReader buff = 
				  new BufferedReader(new InputStreamReader(System.in));
				  System.out.println("Enter the Decimal number:");
				  char str = 0 ;
				  try {
					str = buff.readLine().charAt(0);
				  } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }				  
				  int c = (int) str ;
				  System.out.println("Enter decimal number is:=" + str);
				  System.out.println("ASCII OF:=" + c );*/
		Ini ini = new Wini();
		Config conf = new Config();
		conf.setMultiOption(true);
		ini.setConfig(conf);
		try {
			ini.load(new File("D:/ftp/EFIcost/eficost.ini"));
		} catch (IOException e) {			
			System.out.println(e);
		}
		Section section = ini.get("EFIUPDATER");
		String propertie01 = crypto.encrypt("RUTAORIPBDSAPLS").replace("\\", "");
		String propertieValue = section.get(propertie01);		
		System.out.println(propertie01);
		System.out.println(propertieValue);
		System.out.println(crypto.decrypt(propertieValue.substring(1, propertieValue.length())));
		
				
	}
}
