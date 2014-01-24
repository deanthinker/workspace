import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;


public abstract class PanelCustSalesRecord extends JPanel {
	private KYdb db = new KYdb();
	private KYutil u = new KYutil();
	
	public static void main(String[] args) {

		try {
			JFrame frame = new JFrame();
			PanelCustSalesRecord pcust = new PanelCustSalesRecord(){
				public void update(){
					
				}
			};
						
			frame.getContentPane().add(pcust);
			frame.setVisible(true);
			frame.setSize(500, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public PanelCustSalesRecord() {
		JTable atable = new JTable();
		
		JPanel tablepane = new JPanel(new BorderLayout());
		JPanel titlepane = new JPanel(new GridLayout(1,2,0,0));
		JLabel title = new JLabel("客戶期間交易紀錄");
		JComboBox cbxCrop = new JComboBox();
		
		titlepane.add(title);
		titlepane.add(cbxCrop);
		
		tablepane.add(titlepane, BorderLayout.NORTH);
		

		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)atable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment( JLabel.RIGHT );
		
		JScrollPane scrollPane = new JScrollPane(atable);
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		atable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(final MouseEvent e)
			{
				if (e.getClickCount() == 1)
				{
					final JTable target = (JTable)e.getSource();
					final int row = target.getSelectedRow();
					String pcode = (String)target.getValueAt(row, 0);
					u.debug (pcode);
					//------------IMPORTANT----------------
					update(); //need to be implemented!!!!!
					//-------------------------------------
					
				}
			}

		});	
		tablepane.add(scrollPane, BorderLayout.CENTER);
		
		this.add(titlepane);
		this.add(tablepane);
	}
	public abstract void update();
}
