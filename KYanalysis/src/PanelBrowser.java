import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;




public class PanelBrowser extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String HTMLcode_header = "<html>";
	private String HTMLcode_script_header = "<head>  <script type='text/javascript' src='https://www.google.com/jsapi'></script>  <script type='text/javascript'>   google.load('visualization', '1', {'packages': ['geomap']});   google.setOnLoadCallback(drawMap);    function drawMap() {      var data = google.visualization.arrayToDataTable([['Country','Kg'],";
	private String HTMLcode_script_data = "['Country','test'],['Germany',200],['UnitedStates',300],['Brazil',400],['Canada',500],['FRANCE',600],['RU',700],['China',500],['Taiwan',1000],['Vietnam',0]";
	private String HTMLcode_script_footer = "]);      var options = {};  options['dataMode'] = 'regions';    options['width'] = 900;  options['height'] = 550;   var container = document.getElementById('map_canvas');      var geomap = new google.visualization.GeoMap(container);      geomap.draw(data, options);  };  </script></head>";
	private String HTMLcode_body_header = "<body>  <div id='map_canvas'></div>";
	private String HTMLcode_body_footer = "</body>";
	private String HTMLcode_footer = "</html>";
	

	JPanel chartpanel = new JPanel();
	final JWebBrowser webBrowser = new JWebBrowser();

	PanelSearchPcode panelSearchPcode = new PanelSearchPcode(PanelSearchPcode.SHOWIMAGE)
	{
		private static final long serialVersionUID = 1L;
		public void updateChart() {
			String ys =  panelyr.getYS();
			String ye =  panelyr.getYE();
			u.debug("pcode: "+ selectedpcode);
			u.debug("title: "+ title);
			u.debug("range: " + ys + " - " + ye);

			String HTMLsalesTable = db.getHTMLtable_String_float_country_weight(selectedpcode, ys, ye);
			
			Vector<KeyValue_String_float> vec = db.getKeyValue_String_float_country_weight(selectedpcode, String.valueOf(ys), String.valueOf(ye));
			if (vec.size()>0){
				HTMLcode_script_data = convertKeyValue2geomapData(vec);
				u.debug(HTMLcode_script_data);
				String h = "<B1>" + title +  " (資料期間: "+ ys + " - " + ye +") </B1><br>" + 
						HTMLcode_header + 
						HTMLcode_script_header +
						HTMLcode_script_data +
						HTMLcode_script_footer +
						HTMLcode_body_header +
						HTMLsalesTable +
						HTMLcode_body_footer +
						HTMLcode_footer;
				webBrowser.setHTMLContent(h);
				
			}else{
				webBrowser.setHTMLContent("<B1>"+title+"</B1><br>"+"<br> NO DATA");
			}
			
			
		}
	};							
	
	
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

	

	
	public PanelBrowser() {
		this.setLayout(new GridLayout(1, 1, 0, 0));
		JSplitPane splitpanel = new JSplitPane();
				
		splitpanel.setDividerLocation(350);

	    webBrowser.setBarsVisible(false);
	    webBrowser.setStatusBarVisible(true);
	    chartpanel.setLayout(new GridLayout(0, 1, 0, 0));
	    chartpanel.add(webBrowser);
		
		splitpanel.add(panelSearchPcode, "left");
		splitpanel.add(chartpanel, "right");
		
		this.add(splitpanel);

		
	}
	

	
	private boolean isInternetUp(){
		//does not work for Google URL but Ruten okay
		boolean up = true;
		int timeout = 2000;
		try {
			InetAddress[] addresses = InetAddress
					.getAllByName("www.google.com.tw");
			for (InetAddress address : addresses) {

				if (address.isReachable(timeout))
					System.out.printf("%s is reachable%n", address);
				else {
					up = false;
					System.out.printf("%s could not be contacted%n", address);
				}

			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			up = false;
		}
		catch (IOException e) {
			e.printStackTrace();
			up = false;
		}
		    
		return up;

	}



}


