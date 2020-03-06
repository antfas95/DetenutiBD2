package DataBase.detenuti;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.plaf.BorderUIResource;
import javax.swing.table.DefaultTableModel;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.*;

public class MenuFrame extends JFrame {
	
	//Dichiarazione delle variabili di istanza per la rappresentazione del JDialog di riferiment
	private JPanel panel_area;
	private JDialog dialog_area;
	private JTextArea area;
	private JScrollPane scroll;
	
	//Dichiarazione degli arrayList per l'inserimento all'interno delle relative Combo
	private ArrayList<String> regions;
	private ArrayList<String> range_età;

	//Cursori per l'esecuzione delle varie query
	private DBCursor cursor_sesso;
	private DBCursor cursor_region;
	
	//Dichiarazione delle variabili di istanza per il controllo degli errori
	private boolean check_anno= false;

	//Dichiarazione delle variabili di istanza della classe inerenti alla connessione
	private DB db;
	private DBCollection collection_sesso;
	private DBCollection collection_regione;
	private DBCollection collection_stranieri;
	private Container c;

	//Dichiarazione delle variabili di istanza per la view dei diversi valori
	private JPanel panel_img;
	private JPanel panel_anno;
	private JPanel panel_casa;
	private JPanel panel_regione;
	private JPanel panel_sesso;
	private JPanel panel_età;

	//Dichiarazione delle varie ChechBox
	private JCheckBox box_2010;
	private JCheckBox box_2011;
	private JCheckBox box_2012;
	private JCheckBox box_2013;
	private JCheckBox box_2014;
	//CheckBox per casa circondariale
	private JRadioButton casa_Vittore;
	private JRadioButton casa_Bollate;
	private JRadioButton casa_Opera;
	private ButtonGroup group_casa= new ButtonGroup();

	private JButton bottone_submit;

	//Dichiarazione della combo delle nazioni
	private JComboBox combo;
	private JComboBox combo_età;

	//Dichiarazione della radioButton
	private ButtonGroup gruop_sesso= new ButtonGroup();
	private JRadioButton radio_uomo= new JRadioButton("maschi");
	private JRadioButton radio_donna= new JRadioButton("femmine");
	private JRadioButton radio_tutt= new JRadioButton("maschi e femmine");


	//Dichiarazione delle variabili di istanza per reperire i dati scelti dall'utente
	private String casa_Scelta;
	private ArrayList<Integer> anno_Scelto;
	private String sesso_Scelto;
	private String regione_Scelta;
	private String età_Scelta;

	//Inserisco la classe costruttore
	public MenuFrame(DB d, DBCollection coll, DBCollection coll1, DBCollection coll2) throws IOException {

		this.setTitle ("Costruisci la tua query");
		this.setSize (600, 400);

		bottone_submit= new JButton("Submit");
		bottone_submit.setSize(300, 300);
		regions= new ArrayList<String>();
		range_età= new ArrayList<String>();
		anno_Scelto= new ArrayList<Integer>();
		this.db= d;
		this.collection_sesso= coll;
		this.collection_regione= coll1;
		this.collection_stranieri= coll2;
		c= this.getContentPane();

		createPanelImg();
		createPanelAnno();
		createPanelCasa();
		createPanelRegione();
		createPanelSesso();
		createPanelEtà();
		//createPanelTable();


		class Operazione_prova implements ActionListener{

			public void actionPerformed(ActionEvent arg0) {

				// TODO Auto-generated method stub
				//Preleviamo i dati inseriti all'interno della form
				//if per il ritorno della casa selezionata
				anno_Scelto.clear();
				area= new JTextArea();
				area.setPreferredSize(new Dimension(700, 200));
				scroll= new JScrollPane(area);
				//area.setText("");
				System.out.println(anno_Scelto);
				if (casa_Vittore.isSelected()) {
					casa_Scelta= "Casa Circondariale S.Vittore";
				}else if(casa_Opera.isSelected()) {
					casa_Scelta= "Casa di Reclusione Opera";
				}else {
					casa_Scelta= "Casa di Reclusione Bollate";
				}

				//If per il ritorno degli anni scelti
				if (box_2010.isSelected()) {
					anno_Scelto.add(2010);
					check_anno=true;
				}
				if (box_2011.isSelected()) {
					anno_Scelto.add(2011);
					check_anno=true;
				}
				if (box_2012.isSelected()) {
					anno_Scelto.add(2012);
					check_anno=true;
				}
				if (box_2013.isSelected()) {
					anno_Scelto.add(2013);
					check_anno=true;
				}
				if (box_2014.isSelected()) {
					anno_Scelto.add(2014);
					check_anno=true;
				}
				
				/*
				if (check_anno==false) {
					JOptionPane.showMessageDialog(null, "Devi selezionare almeno un anno");
				}
				*/

				//if per il ritorno del sesso scelto
				if (radio_uomo.isSelected()) {
					sesso_Scelto= "maschi";
				}else if(radio_donna.isSelected()) {
					sesso_Scelto= "femmine";
				}else {
					sesso_Scelto= "maschi e femmine";
				}
				regione_Scelta= combo.getSelectedItem().toString();
				età_Scelta= combo_età.getSelectedItem().toString();

				System.out.println("Casa scelta: " + casa_Scelta);
				for (Integer i: anno_Scelto) {
					System.out.println(i);
				}
				System.out.println("Sesso scelto: " + sesso_Scelto);
				System.out.println("Regione scelta: " + regione_Scelta);
				System.out.println("Età scelta: "+ età_Scelta);

				//Prelevati i dati iniziamo ad eseguire la query di interesse per l'utente
				BasicDBObject query= new BasicDBObject("casa circondariale", casa_Scelta);
				BasicDBObject query_regione= new BasicDBObject("casa circondariale", casa_Scelta);
				if (regione_Scelta=="Tutte le regioni") {
					for (String x: regions) {
						query_regione.append("detenuti_regione_nascita", x);
						System.out.println(x);
					}
				}else {
					query_regione.append("detenuti_regione_nascita", regione_Scelta);
				}
				query.append("detenuti_sesso", sesso_Scelto);
				query.append("detenuti_eta", età_Scelta);
				
				for(Integer i: anno_Scelto) {
					query.append("anno_rilevamento_detenuti", i);
					query_regione.append("anno_rilevamento_detenuti", i);
					cursor_sesso= collection_sesso.find(query);
					cursor_region= collection_regione.find(query_regione);
					if(cursor_sesso==null) {
						System.out.println("Non ci sono risultati");
					}else {
						while(cursor_sesso.hasNext()) {
							DBObject oggetto= cursor_sesso.next();
							area.append("   Casa: " + oggetto.get("casa circondariale") + ", Anno: " + oggetto.get("anno_rilevamento_detenuti") + ", Sesso: " + oggetto.get("detenuti_sesso") + ", Numero di detenuti: " + oggetto.get("detenuti") + ", Età: " + oggetto.get("detenuti_eta"));
							area.append("\n");
							System.out.println("Casa: " + oggetto.get("casa circondariale") + ", Anno: " + oggetto.get("anno_rilevamento_detenuti") + ", Sesso: " + oggetto.get("detenuti_sesso") + ", Numero di detenuti: " + oggetto.get("detenuti") + ", Età: " + oggetto.get("detenuti_eta"));
						}
					}

					if(cursor_region==null) {
						System.out.println("Non ci sono risultati per le regioni");
					}else {
						while(cursor_region.hasNext()) {
							DBObject oggetto= cursor_region.next();
							area.append("   Casa: " + oggetto.get("casa circondariale") + ", Anno: " + oggetto.get("anno_rilevamento_detenuti") + ", regione: " + oggetto.get("detenuti_regione_nascita") + ", Numero detenuti: " + oggetto.get("detenuti"));
							area.append("\n");
							System.out.println("Casa: " + oggetto.get("casa circondariale") + ", Anno: " + oggetto.get("anno_rilevamento_detenuti") + "Regione: " + oggetto.get("detenuti_regione_nascita") + ", Numero detenuti: " + oggetto.get("detenuti"));
						}
					}
				}
				
				if (check_anno==false) {
					JOptionPane.showMessageDialog(null, "Devi selezionare almeno un anno");
				}else {
					dialog_area= new JDialog();
					panel_area= new JPanel();
					panel_area.setLayout(new FlowLayout());
					JLabel label= new JLabel("Risultati Carceri della Lombardia");
					label.setForeground(Color.RED);
					label.setFont(new Font("Courier", Font.ITALIC, 30));
					JButton bottone_Ok= new JButton("Ok");
					
					class Operazione_Ok implements ActionListener{

						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							dialog_area.dispose();
						}
						
					}
					ActionListener listener= new Operazione_Ok();
					bottone_Ok.addActionListener(listener);
					
					if (area.getText().equals("")) {
						area.append("   Non sono presenti risultati per la query scelta, prova ad inserire il range di età con la caratteristica maschi e femmine");
					}
					
					panel_area.add(label);
					panel_area.add(scroll);
					panel_area.add(bottone_Ok);
					dialog_area.add(panel_area);
					dialog_area.setSize(700, 350);
					dialog_area.setVisible(true);
					check_anno=false;
				}
				
			}
		}
		ActionListener actionListener= new Operazione_prova();
		bottone_submit.addActionListener(actionListener);
		getContentPane().setLayout(new GridLayout(8, 1));
		JLabel label= new JLabel("DataBase Carceri");
		label.setForeground(Color.BLUE);
		getContentPane().add(panel_img);
		getContentPane().add(panel_casa);
		getContentPane().add(panel_anno);
		getContentPane().add(panel_regione);
		getContentPane().add(panel_sesso);
		getContentPane().add(panel_età);
		getContentPane().add(bottone_submit);
		this.setTitle("Carceri della Lombardia");
		this.setVisible(true);
	}

	private void createPanelImg() throws IOException {
		// TODO Auto-generated method stub
		panel_img= new JPanel();
		String path= "dd.png";
		File file= new File(path);
		BufferedImage bi= ImageIO.read(file);
		ImageIcon i= new ImageIcon("sbarre.jpg");
		JLabel label= new JLabel("DataBase Carceri Lombarde");
		label.setForeground(Color.RED);
		label.setFont(new Font("Courier", Font.ITALIC, 30));
		//label.setPreferredSize(new Dimension(300, 300));
		panel_img.add(label);
	}

	public void createPanelEtà() {
		// TODO Auto-generated method stub
		panel_età= new JPanel();
		combo_età= new JComboBox<String>();
		JLabel label_anno= new JLabel("Scegli il range di età: ");
		label_anno.setForeground(Color.BLUE);
		BasicDBObject query= new BasicDBObject();
		DBCursor cursor= collection_sesso.find(query);
		while (cursor.hasNext()) {
			String regione= (String) cursor.next().get("detenuti_eta");
			System.out.println(regione);
			boolean verifica= true;
			for (String s: range_età) {
				if (s.equals(regione)) {
					verifica= false;
				}
			}
			if (verifica==true) {
				range_età.add(regione);
			}
		}
		for (String s: range_età) {
			combo_età.addItem(s);
		}
		panel_età.add(label_anno);
		panel_età.add(combo_età);
	}

	public void createPanelSesso() {
		panel_sesso = new JPanel();
		gruop_sesso.add(radio_uomo);
		gruop_sesso.add(radio_donna);
		gruop_sesso.add(radio_tutt);
		radio_tutt.setSelected(true);
		JLabel label_anno= new JLabel("Scegli il sesso: ");
		label_anno.setForeground(Color.BLUE);
		panel_sesso.add(label_anno);
		panel_sesso.add(radio_tutt);
		panel_sesso.add(radio_donna);
		panel_sesso.add(radio_uomo);
	}

	public void createPanelRegione() {
		// TODO Auto-generated method stub
		panel_regione= new JPanel();
		panel_regione.setLayout(new FlowLayout());
		JLabel label_anno= new JLabel("Scegli la regione di interesse: ");
		label_anno.setForeground(Color.BLUE);
		BasicDBObject query= new BasicDBObject();
		DBCursor cursor= collection_regione.find(query);
		while (cursor.hasNext()) {
			String regione= (String) cursor.next().get("detenuti_regione_nascita");
			boolean verifica= true;
			for (String s: regions) {
				if (s.equals(regione)) {
					verifica= false;
				}
			}
			if (verifica==true) {
				regions.add(regione);
			}
		}
		combo= new JComboBox<String>();
		combo.addItem("Nessuna regione");
		for (String s: regions) {
			combo.addItem(s);
		}
		panel_regione.add(label_anno);
		panel_regione.add(combo);
	}

	public void createPanelCasa() {
		// TODO Auto-generated method stub
		panel_casa= new JPanel();
		panel_casa.setLayout(new FlowLayout());
		JLabel label_anno= new JLabel("Scegli il carcere di interesse: ");
		label_anno.setForeground(Color.BLUE);
		//label_anno.setFont(new Font);
		casa_Vittore= new JRadioButton("Carcere di S.Vittore");
		casa_Opera= new JRadioButton("Carcere di Opera");
		casa_Bollate= new JRadioButton("Carcere di Bollate");
		casa_Vittore.setSelected(true);
		group_casa.add(casa_Bollate);
		group_casa.add(casa_Opera);
		group_casa.add(casa_Vittore);
		panel_casa.add(label_anno);
		panel_casa.add(casa_Vittore);
		panel_casa.add(casa_Opera);
		panel_casa.add(casa_Bollate);
	}

	public void createPanelAnno() {
		// TODO Auto-generated method stub
		panel_anno= new JPanel();
		panel_anno.setLayout(new FlowLayout());
		JLabel label_anno= new JLabel("Scegli gli anni di interesse: ");
		label_anno.setForeground(Color.BLUE);
		box_2010= new JCheckBox("2010");
		box_2011= new JCheckBox("2011");
		box_2012= new JCheckBox("2012");
		box_2013= new JCheckBox("2013");
		box_2014= new JCheckBox("2014");
		panel_anno.add(label_anno);
		panel_anno.add(box_2010);
		panel_anno.add(box_2011);
		panel_anno.add(box_2012);
		panel_anno.add(box_2013);
		panel_anno.add(box_2014);
	}
}