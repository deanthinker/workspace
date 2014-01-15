import java.awt.BorderLayout;
import java.awt.GridLayout;

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


public class PanelCustAnalysis extends JPanel {

	KYdb db = new KYdb();
	private JSplitPane leftsplit;
	private JSplitPane rightsplit;
	
	private JScrollPane lefttop, leftbot, righttop, rightbot;
	
	private PanelSearchPcode panelSearchPcode = new PanelSearchPcode(PanelSearchPcode.NOIMAGE){
		private static final long serialVersionUID = 1L;
		public void update() {
			
		}
	};	
	
	private PanelSearchCust panelSearchCust = new PanelSearchCust(){
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
		return panel;
	}
	private JPanel createRightBotPanel(){		//purchase history
		JPanel panel = new JPanel(new GridLayout(1, 1, 0, 0));
		return panel;
	}
	
	
	public PanelCustAnalysis() {
		//split frame into left and right panel, each panel is then split into top and bottom
		lefttop = new JScrollPane(createLeftTopPanel());
		leftbot = new JScrollPane(createLeftBotPanel());
		righttop = new JScrollPane(createRightTopPanel());
		rightbot = new JScrollPane(createRightBotPanel());
		
		leftsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, lefttop, leftbot);
		rightsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, righttop, rightbot);

		leftsplit.add(lefttop, "top"); 
		leftsplit.add(leftbot, "bottom");
		
		
		this.setLayout(new GridLayout(1, 1, 0, 0));
		JSplitPane splitpanel = new JSplitPane();
		
		splitpanel.setDividerLocation(350);
		splitpanel.add(leftsplit, "left");
		splitpanel.add(rightsplit, "right");
		
		this.add(splitpanel);
		
		//insert object into each panel
		
		
	}

	
	
}
