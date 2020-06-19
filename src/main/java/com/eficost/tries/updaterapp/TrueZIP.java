package com.eficost.tries.updaterapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.progress.ProgressMonitor;


public class TrueZIP {

	public void lstFilesInsideZIP(){
		List<String> lstFiles = new ArrayList<String>();		
		String path = "D:/ftp/EFIcost/Contabilidad.zip";
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) { 
			ZipFile zipFile = new ZipFile(path);
			try {
				for(FileHeader file : zipFile.getFileHeaders()) {
					System.out.print(file.getFileName()+"\n");
				}
				
			} catch (ZipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		else {
			System.out.print("The path doesn't exist");
		}		
	}
	
	public int extractAllFilesFromZIP() {
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile("D:/ftp/EFIcost/Contabilidad.zip");			
			
			zipFile.setRunInThread(true);
			
			ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
			
			// Get the list of file headers from the zip file
			List fileHeaderList = zipFile.getFileHeaders();
			
			// Loop through the file headers
			for (int i = 0; i < fileHeaderList.size(); i++) {
				
				FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);
				if(fileHeader.getFileName().endsWith(".pbd") || fileHeader.getFileName().endsWith(".exe")) {
					System.out.print("Extracting the file"+fileHeader.getFileName()+"\n");
					System.out.print("Porcent Done : "+progressMonitor.getPercentDone()+"\n");
					
					// Extract the file to the specified destination
					zipFile.extractFile(fileHeader,"D:/ftp/EFIcost/");
					
					System.out.print("Result : "+progressMonitor.getResult()+"\n");
					
					if(progressMonitor.getResult() == ProgressMonitor.Result.ERROR) {
						if(progressMonitor.getException() != null) {
							progressMonitor.getException().printStackTrace();
						}else {
							System.out.print("There was an error without any exception");
						}
					}
					
					while(progressMonitor.getState() == ProgressMonitor.State.BUSY) {
						System.out.print("Porcent Done : "+progressMonitor.getPercentDone()+"\n");
						System.out.print("File : "+progressMonitor.getFileName()+"\n");
						
						switch(progressMonitor.getCurrentTask()) {
						case NONE:
					        System.out.println("no operation being performed"+"\n");
					        break;
					    case ADD_ENTRY:
					        System.out.println("Add operation"+"\n");
					        break;
					    case EXTRACT_ENTRY:
					        System.out.println("Extract operation"+"\n");
					        break;
					    case REMOVE_ENTRY:
					        System.out.println("Remove operation"+"\n");
					        break;
					    case CALCULATE_CRC:
					        System.out.println("Calcualting CRC"+"\n");
					        break;
					    case MERGE_ZIP_FILES:
					        System.out.println("Merge operation"+"\n");
					        break;
					    default:
					        System.out.println("invalid operation"+"\n");
					        break;					
						}
					}
				}				
								
			}
		}catch(ZipException e) {
			e.printStackTrace();
			return 0;
		}
		
		return 1;
	}
}
