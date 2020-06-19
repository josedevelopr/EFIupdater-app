package com.eficost.updaterapp.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;

public class frmUpdaterEFIcost extends JFrame {

	public JPanel contentPane;
	public JProgressBar pbDescargaArchivo;
	public JLabel lblNombreArchivoDescarga;
	public JLabel lblTitulo;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmUpdaterEFIcost frame = new frmUpdaterEFIcost();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
		});
	}

	/**
	 * Create the frame.
	 */
	public frmUpdaterEFIcost() {
		setBackground(Color.WHITE);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 449, 240);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new LineBorder(Color.DARK_GRAY));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		pbDescargaArchivo = new JProgressBar();
		pbDescargaArchivo.setForeground(new Color(30, 144, 255));
		pbDescargaArchivo.setBorderPainted(false);
		pbDescargaArchivo.setBorder(new EmptyBorder(0, 0, 0, 0));
		pbDescargaArchivo.setBackground(new Color(211, 211, 211));
		pbDescargaArchivo.setBounds(58, 123, 332, 4);
		contentPane.add(pbDescargaArchivo);
		
		//pbDescargaArchivo.setValue(80);
		
		lblNombreArchivoDescarga = new JLabel("Descargando contabilidad.exe");
		lblNombreArchivoDescarga.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNombreArchivoDescarga.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombreArchivoDescarga.setFont(new Font("Malgun Gothic", Font.BOLD, 9));
		lblNombreArchivoDescarga.setBounds(0, 79, 448, 22);
		contentPane.add(lblNombreArchivoDescarga);
		
		lblTitulo = new JLabel("    EFIcost Facturaci\u00F3n - Actualizando Producci\u00F3n");
		lblTitulo.setBounds(0, 148, 448, 22);
		contentPane.add(lblTitulo);
		lblTitulo.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Malgun Gothic", Font.BOLD, 10));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	}
}
