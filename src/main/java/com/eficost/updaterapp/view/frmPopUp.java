package com.eficost.updaterapp.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class frmPopUp extends JFrame {

	private JPanel contentPane;
	int lastX, lastY;
	public JLabel lblAlertTitle;
	public JLabel lblAlertMessage;
	public JPanel pnlBckButton;
	public JPanel pnlDragBar;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmPopUp frame = new frmPopUp();
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
	public frmPopUp() {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 222);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(UIManager.getBorder("RadioButton.border"));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		pnlDragBar = new JPanel();
		pnlDragBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
		        int y = e.getYOnScreen();
		        // Move frame by the mouse delta
		        setLocation(getLocationOnScreen().x + x - lastX,
		                getLocationOnScreen().y + y - lastY);
		        lastX = x;
		        lastY = y;
			}
		});
		pnlDragBar.addMouseListener(new MouseAdapter() {		    
			@Override
			public void mousePressed(MouseEvent e) {
				lastX = e.getXOnScreen();
		        lastY = e.getYOnScreen();
			}
		});
		pnlDragBar.setBackground(new Color(70, 130, 180));
		pnlDragBar.setBounds(0, 0, 450, 10);
		contentPane.add(pnlDragBar);
		
		JLabel lblAceptar = new JLabel("ACEPTAR");
		lblAceptar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Component component = (Component) e.getSource();
		        JFrame frame = (JFrame) SwingUtilities.getRoot(component);
		        frame.dispose();
			}
		});
		lblAceptar.setBounds(140, 165, 175, 23);
		contentPane.add(lblAceptar);
		lblAceptar.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 10));
		lblAceptar.setForeground(new Color(255, 255, 255));
		lblAceptar.setHorizontalAlignment(SwingConstants.CENTER);
		
		pnlBckButton = new JPanel();
		pnlBckButton.setBorder(null);
		pnlBckButton.setBackground(new Color(70, 130, 180));
		pnlBckButton.setBounds(140, 165, 175, 23);
		contentPane.add(pnlBckButton);
		pnlBckButton.setLayout(null);
		
		lblAlertTitle = new JLabel("Test Alert");
		lblAlertTitle.setForeground(new Color(105, 105, 105));
		lblAlertTitle.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 15));
		lblAlertTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblAlertTitle.setBounds(97, 32, 254, 40);
		contentPane.add(lblAlertTitle);
		
		lblAlertMessage = new JLabel("<html>"
										+ "This is my alert message, I wrote a lot"
										+ "<br>Second part"
										+ "<br>Third part"
										+ "<br>Fourth part"
									+ "</html>");
		lblAlertMessage.setForeground(new Color(169, 169, 169));
		lblAlertMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblAlertMessage.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 10));
		lblAlertMessage.setBounds(0, 68, 450, 86);
		contentPane.add(lblAlertMessage);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);		
	}
	
}
