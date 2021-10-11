package com.apos.socket;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

public class MyFrame extends JFrame {
	
	// mon interface contient un button et un label , je commence par les declares et les intialisés à null
	// je vais les instanciés par la suite  dans le constructeur
	JButton buttonCalcul = null;
	JLabel  labelResult = null;
	
	MyFrame(String title){
		super(title);
		
		//-------DEBUT INITIALISATION :  je commence l'instanciation des attributs-----------------
		buttonCalcul = new JButton("click et calcul");
		labelResult = new JLabel("j'affiche le resulat");
		//-------FIN INITIALISATION ----------------
		
		// je cree un conteneur et j'y ajoute mes composants----------------
		JPanel monConteneur = new JPanel();
		monConteneur.add(buttonCalcul);
		monConteneur.add(labelResult);
		monConteneur.setBackground(Color.blue);
		
		// j'ajoute mon conteneur dans le body de l'interface ----------------
		this.getContentPane().add(monConteneur);
		
		// je vais gerer l'evenement clik du button : je cree une fonction apart pour l'organisation
		this.gererClick();
		
	}
	
	private void gererClick() {
		
		//  DEBUT DEFINITION LISTENER: instation à partir d'une interface -------------------------------
		ActionListener clickListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				labelResult.setText(" le button est  clické !  ");
				
			}
		};
	//  FIN DEFINITION LISTENER ----------------------------------------------------------------------
		
		//j'ajoute un listener de l'evenement click sur le button : buttonCalcul
		buttonCalcul.addActionListener(clickListener);
	}

	public static void main(String argv[]) {
		HashMap<String, String > licenseList=null;
		  new  JSONObject(licenseList);
		  
		MyFrame f = new MyFrame("ma fenetre");
	    f.setSize(300,400);
	    f.setVisible(true);
	    
	  }
	
}
