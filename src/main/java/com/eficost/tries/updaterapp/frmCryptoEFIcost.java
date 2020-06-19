package com.eficost.tries.updaterapp;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.eficost.updaterapp.controller.CryptoEficostController;
import com.eficost.updaterapp.entities.CryptoEficost;


public class frmCryptoEFIcost extends JFrame {

	private JPanel contentPane;
	private JTextField txtToEncrypt;
	private JTextArea txtResult;
	private JComboBox cboAction;
	private ButtonGroup rbtGroupEncrypTypes;
	private JRadioButton rdbtnDupter;
	private JRadioButton rdbtnJdupter;
	private JRadioButton rdbtnSha;

	CryptoEficostController objCrypto = new CryptoEficostController();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmCryptoEFIcost frame = new frmCryptoEFIcost();
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
	public frmCryptoEFIcost() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 395, 500);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtToEncrypt = new JTextField();
		txtToEncrypt.setBounds(20, 117, 342, 20);
		contentPane.add(txtToEncrypt);
		txtToEncrypt.setColumns(10);
		
		JLabel lblTexto = new JLabel("Texto");
		lblTexto.setBounds(20, 92, 46, 14);
		contentPane.add(lblTexto);
		
		JLabel lblResultado = new JLabel("Resultado");
		lblResultado.setBounds(20, 225, 63, 14);
		contentPane.add(lblResultado);
		
		JButton btnProcesar = new JButton("Procesar");
		btnProcesar.setBounds(20, 381, 342, 28);
		btnProcesar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtToEncrypt.getText().length() == 0) 
				{
					showMessageBox("El campo 'Texto' se encuentra vac�o. Ingrese alg�n caracter, por favor.");
				}
				else
				{
					if(cboAction.getSelectedIndex() == 0 ) 
					{
						encrypt();
					}
					else if(cboAction.getSelectedIndex() == 1 ) 
					{
						decrypt();
					}	
				}							
			}
		});
		contentPane.add(btnProcesar);
		
		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clean();
			}
		});
		btnLimpiar.setBounds(20, 420, 342, 28);
		contentPane.add(btnLimpiar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 250, 342, 116);
		contentPane.add(scrollPane);
		
		txtResult = new JTextArea();
		scrollPane.setViewportView(txtResult);
		
		cboAction = new JComboBox();
		cboAction.setModel(new DefaultComboBoxModel(new String[] {"Encriptar", "Desencriptar"}));
		cboAction.setBounds(20, 52, 342, 20);
		contentPane.add(cboAction);
		
		JLabel lblAccin = new JLabel("Acci\u00F3n");
		lblAccin.setBounds(20, 21, 46, 14);
		contentPane.add(lblAccin);
		
		JLabel lblTipoEncriptacin = new JLabel("Tipo Encriptaci\u00F3n");
		lblTipoEncriptacin.setBounds(20, 159, 145, 14);
		contentPane.add(lblTipoEncriptacin);
		
		rdbtnDupter = new JRadioButton("Dupter");
		rdbtnDupter.setBounds(20, 181, 109, 23);
		contentPane.add(rdbtnDupter);
		
		rdbtnJdupter = new JRadioButton("Jdupter");
		rdbtnJdupter.setBounds(131, 181, 109, 23);
		contentPane.add(rdbtnJdupter);
		
		rdbtnSha = new JRadioButton("SHA256");
		rdbtnSha.setBounds(242, 181, 109, 23);
		contentPane.add(rdbtnSha);
		
		// Radio Button Group
		rbtGroupEncrypTypes = new ButtonGroup();
		rbtGroupEncrypTypes.add(rdbtnDupter);
		rbtGroupEncrypTypes.add(rdbtnJdupter);
		rbtGroupEncrypTypes.add(rdbtnSha);
		rdbtnDupter.setSelected(true);
	}
	
	public void encrypt() {
		
		CryptoEficost objCE = new CryptoEficost();
		String cryptoResult = "";		
		String textToEncrypt = txtToEncrypt.getText();
		objCE.setUnencryptedText(textToEncrypt);		
		
		if(rdbtnDupter.isSelected()) 
		{
			cryptoResult = objCrypto.encriptadoDavid(objCE);
		}
		else if(rdbtnJdupter.isSelected())
		{
			cryptoResult = objCrypto.encriptadoJose(objCE);
		}
		else if(rdbtnSha.isSelected())
		{			
			cryptoResult = objCrypto.encriptadoSHA256(objCE);
		}		
		
		txtResult.setText(cryptoResult);
	}
	
	public void decrypt() {
		CryptoEficost objCE = new CryptoEficost();
		String cryptoResult = "";		
		String textToEncrypt = txtToEncrypt.getText();
		objCE.setUnencryptedText(textToEncrypt);		
		
		if(rdbtnDupter.isSelected()) 
		{
			cryptoResult = objCrypto.desencriptadoDavid(objCE);
		}
		else if(rdbtnJdupter.isSelected())
		{
			cryptoResult = objCrypto.desencriptadoJose(objCE);
		}
		else if(rdbtnSha.isSelected())
		{
			showMessageBox("No existe una desencriptaci�n para el SHA256");
			cryptoResult = "";
		}		
				
		txtResult .setText(cryptoResult);
	}
	
	public void clean() {
		txtResult.setText("");
		txtToEncrypt.setText("");
		cboAction.setSelectedIndex(0);
	}
	
	private void showMessageBox(String text) {
		JOptionPane.showMessageDialog(null, text);
	}
}
