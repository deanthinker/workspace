import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.ScrollPaneConstants;

import java.sql.ResultSet;


public class RecordView extends JDialog {


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private KYdb db = new KYdb();
	private KYutil u = new KYutil();
	private MyTableModel model;
	
	
	JTable atable = new JTable()
    {
        @Override
        public Component prepareRenderer(TableCellRenderer renderer,
                                         int row, int col)
        {
          Component comp = super.prepareRenderer(renderer, row, col);
          ((JLabel) comp).setHorizontalAlignment(JLabel.RIGHT);
          return comp;
        }
      };			
/*
	public static void main(String[] args) {
		KYdb db = new KYdb();
		try {
			RecordView dialog = new RecordView(db.getResultset_vege_prod(),"ด๚ธี");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/

      
	public RecordView() {
		setBounds(50, 50, 800, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		contentPanel.setLayout(null);
		{
			 JScrollPane scrollPane = new JScrollPane(atable);
			 scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			 scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			 getContentPane().add(scrollPane);
					
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
	
	public RecordView(ResultSet rs, String title) {
		setTitle(title);
		setBounds(50, 50, 800, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		contentPanel.setLayout(null);
		{

			model = new MyTableModel(rs);
			atable.setModel(model);
			RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(model);
			atable.setRowSorter(sorter);

			JScrollPane scrollPane = new JScrollPane(atable);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			getContentPane().add(scrollPane);

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Close");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						closeWindow();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}

		}
		
	}
	
	private void closeWindow(){
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));		
	}
	
	public void setResultSet(ResultSet rs){
		 model = new MyTableModel(rs);
		 atable.setModel(model);		
	}
	
}


