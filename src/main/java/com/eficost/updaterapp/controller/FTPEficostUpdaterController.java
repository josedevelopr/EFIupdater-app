package com.eficost.updaterapp.controller;

import com.eficost.updaterapp.service.FTPEficostUpdaterServiceImpl;

public class FTPEficostUpdaterController {
	private FTPEficostUpdaterServiceImpl service = new FTPEficostUpdaterServiceImpl();
			
	public void actualizarArchivosEficost() { 
		service.updateEFIcostFiles();
	}
}
