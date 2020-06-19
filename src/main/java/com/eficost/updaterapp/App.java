package com.eficost.updaterapp;

import com.eficost.updaterapp.controller.FTPEficostUpdaterController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	FTPEficostUpdaterController mainController = new FTPEficostUpdaterController();
		mainController.actualizarArchivosEficost(); 
    }
}
