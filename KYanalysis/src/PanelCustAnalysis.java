import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.sql.*;
import javax.sql.rowset.FilteredRowSet;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.xy.XYDataset;

import com.sun.rowset.providers.*;
import com.sun.rowset.internal.*;

public class PanelCustAnalysis extends JPanel {
	public static int EXPORT = 1;
	public static int DOMESTIC = 2;
	public int DATASRC = EXPORT;
	
	KYdb db = new KYdb();
	private JSplitPane leftsplit;
	private JSplitPane rightsplit;
	
	private JScrollPane lefttop, leftbot;
	private JPanel righttop, rightbot;
	
	private PanelSearchCust panelSearchCust;	
	private PanelSearchPcode panelSearchPcode;	
	
	private PanelCustInfo panelCustInfo;	
	
	private PanelCustSalesRecord panelCustSales = new PanelCustSalesRecord(){
		private static final long serialVersionUID = 1L;
		public void update() {
			
		}		
	};
	
	private JPanel createLeftTopPanel(){		//pcode search
		JPanel panel = new JPanel(new GridLayout(1, 1, 0, 0));
		panel.add(panelSearchPcode);
		return panel;
	}
	
	private JPanel createLeftBotPanel(){		//customer search
		JPanel panel = new JPanel(new GridLayout(1, 1, 0, 0));
		panel.add(panelSearchCust);
		return panel;
	}
	private JPanel createRightTopPanel(){		//crop statistics
		JPanel panel = new JPanel(new GridLayout(1, 1, 0, 0));
		panel.add(panelCustInfo);
		return panel;
	}
	private JPanel createRightBotPanel(){		//purchase history
		JPanel panel = new JPanel(new GridLayout(1, 1, 0, 0));
		panel.add(panelCustSales);
		return panel;
	}
	
	
	public PanelCustAnalysis(int dbsrc) { //src: EXPORT | DOMESTIC
		DATASRC = dbsrc;
		panelSearchCust = new PanelSearchCust(DATASRC){ 
			private static final long serialVersionUID = 1L;
			public void update() {
				panelCustInfo.setCustcode(DATASRC,panelyr.getYS() ,panelyr.getYE() ,selectedcustcode,null,"all"); 
				panelCustSales.setGroupDisabled();
				panelCustSales.setCustcode(DATASRC,panelyr.getYS(), panelyr.getYE(), selectedcustcode, null, false);
			}
		};	
		
		panelSearchPcode = new PanelSearchPcode(PanelSearchPcode.NOIMAGE, DATASRC){
			private static final long serialVersionUID = 1L;
			public void update() {
				panelSearchCust.setPcode(selectedpcode);
			}
		};	
		
		panelCustInfo = new PanelCustInfo(DATASRC){
			private static final long serialVersionUID = 1L;
			public void update() {
				
			}
		};	
		
		
		this.setLayout(new GridLayout(1, 1, 0, 0));
		//split frame into left and right panel, each panel is then split into top and bottom
		lefttop = new JScrollPane(createLeftTopPanel());
		leftbot = new JScrollPane(createLeftBotPanel());
		righttop = createRightTopPanel();
		rightbot = createRightBotPanel();

		leftsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, lefttop, leftbot);
		rightsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, righttop, rightbot);

		rightsplit.setDividerLocation(250);
		leftsplit.setDividerLocation(100);
		
		leftsplit.add(lefttop, "top"); 
		leftsplit.add(leftbot, "bottom");
		

		JSplitPane splitpanel = new JSplitPane();
		
		splitpanel.setDividerLocation(300);
		splitpanel.add(leftsplit, "left");
		splitpanel.add(rightsplit, "right");
		
		this.add(splitpanel);
		
		//insert object into each panel
		
		

		
	}

	
	
}
