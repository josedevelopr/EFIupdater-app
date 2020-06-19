package com.eficost.tries.updaterapp;

import java.util.Timer;
import java.util.TimerTask;

import com.eficost.updaterapp.view.frmUpdaterEFIcost;


public class runViewApp {

	
	public static void main(String[] args) {
		final Timer t = new Timer();
		final frmUpdaterEFIcost updaterView = new frmUpdaterEFIcost(); 
		updaterView.setVisible(true);		
		final String[] nombres = {"Eficost.exe","file.pbd","contabilidad.ini","eficost.ini","cont_reg.pbd","Eficost.exe","file.pbd","contabilidad.ini","eficost.ini","cont_reg.pbd","Eficost.exe","file.pbd","contabilidad.ini","eficost.ini","cont_reg.pbd"};
		updaterView.pbDescargaArchivo.setValue(0);	
		t.schedule(new TimerTask() {
			int count = -1;
			int percent = 0;
			int total = nombres.length;
			int currentPorent = 0;
		    @Override
		    public void run() {
		       count++;
		       currentPorent = 100 * (count+1)/total;
		       if(count == nombres.length) {
		    	   t.cancel();
		    	   t.purge();
		    	   updaterView.dispose();
		       }else {
		    	   System.out.print(nombres[count]+"\n");
		    	   System.out.print(currentPorent+"\n");
		    	   System.out.print("--\n");
			       updaterView.lblNombreArchivoDescarga.setText("Descargando "+nombres[count]);
			       updaterView.pbDescargaArchivo.setValue(currentPorent);			       
		       }		       		       
		    }
		}, 0, 1000);
	}

}
