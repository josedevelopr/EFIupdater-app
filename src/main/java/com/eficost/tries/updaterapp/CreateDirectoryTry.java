package com.eficost.tries.updaterapp;

import java.io.File;

public class CreateDirectoryTry {

	public void writeFile(){
	    String PATH = "C:/Sistemas_Eficost/Contabilidad/Prod";
	    //String directoryName = PATH.concat(this.getClassName());
	    //String fileName = id + getTimeStamp() + ".txt";

	    File directory = new File(PATH);
	    if (! directory.exists()){
	    	System.out.print("The Path doesn't exist\n");
	    	System.out.print("Creating the path...\n");
	        directory.mkdirs();
	        System.out.print("Path creation finished...\n");
	        // If you require it to make the entire directory path including parents,
	        // use directory.mkdirs(); here instead.
	    }else {
	    	System.out.print("The Path EXISTS");
	    }
/*
	    File file = new File(directoryName + "/" + fileName);
	    try{
	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(value);
	        bw.close();
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.exit(-1);
	    }*/
	}
	
	public static void main(String[] args) {

		CreateDirectoryTry directoryCreator = new CreateDirectoryTry();
		directoryCreator.writeFile();

	}

}
