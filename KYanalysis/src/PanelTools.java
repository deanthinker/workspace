
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class PanelTools extends JPanel {
	private int YEAR_RANGE = 10;
	private int YE = Calendar.getInstance().get(Calendar.YEAR); // end year
	private int YS = YE - YEAR_RANGE; // start year
	
	public String pcode = "0000";
	private String pcodeTitle = "";
	private KYutil u = new KYutil();
	private KYdb db = new KYdb();
	
	private JTextField txf_keyword;


	JPanel chartpanel = new JPanel();

	
	
	private String convertKeyValue2geomapData(Vector<KeyValue_String_float> countrylist){
		String stringdata = "";
		KeyValue_String_float kv;
		for (int i=0;i<countrylist.size();i++){
			kv = countrylist.get(i);
			//u.debug(kv.key+":"+kv.value+"  "+kv.show);
			if (kv.show){
				if (stringdata == "")
					stringdata += "['" + kv.key + "', " + kv.value + "]";
				else
					stringdata += ", ['" + kv.key + "', " + kv.value + "]";
				
			}
		}
		//u.debug("stringdata:"+stringdata);
		return stringdata;
		
	}
	public PanelTools() {
		setLayout(new GridLayout(0, 1, 0, 0));
		//setLayout(new GridLayout(2, 0, 0, 0));
		//setBorder(new TitledBorder(null, "品種清單",TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JSplitPane splitpanel = new JSplitPane();
		JPanel settingpanel = new JPanel();
		JPanel filterpanel = new JPanel();
		

		
		filterpanel.setLayout(new GridLayout(4, 0, 0, 0));
		settingpanel.setLayout(new GridLayout(2, 0, 0, 0));

		settingpanel.add(filterpanel);

		
		splitpanel.setDividerLocation(350);
		splitpanel.add(settingpanel, "left");
		splitpanel.add(chartpanel, "right");
		add(splitpanel);

	    chartpanel.setLayout(new GridLayout(0, 1, 0, 0));
			
		JPanel panel_0 = new JPanel();
		filterpanel.add(panel_0);
		
		JLabel label = new JLabel("圖檔目錄位置");
		panel_0.add(label);
		
		JPanel panel_1 = new JPanel();
		filterpanel.add(panel_1);
				
		JPanel panel_2 = new JPanel();
		filterpanel.add(panel_2);
		
		
		txf_keyword = new JTextField();
		txf_keyword.setText("D:\\var_pics");
		txf_keyword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scanIntoDb(txf_keyword.getText());
			}
		});
		panel_0.add(txf_keyword);
		txf_keyword.setColumns(10);
		
	
		JButton btn_search_kw = new JButton("自動插入品種圖");
		btn_search_kw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scanIntoDb(txf_keyword.getText());
			}
		});
		
		panel_0.add(btn_search_kw);
	}
	

	private void scanIntoDb(String imgpath) {
		String fname = "";
		String pcode = "";
		 InputStream in = null;  
         
		int count = 0;
		File dir = new File(imgpath);
		if (dir.exists() && dir.isDirectory()) {

			for (File f : dir.listFiles()) {
				fname = f.getName();
				pcode = db.getPcode_by_pcname(fname);
				if (fname.contains("250") && pcode != "") {// only size of 250 gets inserted to DB
					count++;
					u.debug(count + "\t" + fname + "\t pcode:" + pcode  + "\t len:" + f.length());
					try {
						in = new BufferedInputStream(new FileInputStream(f));
						
						db.update_vege_prod_img_by_pcode(pcode, in, f.length());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

				} else
					u.debug(fname + " N/A");

			}

		} else {
			u.debug(imgpath + " does not exist!");
		}

	} 
		  




}
