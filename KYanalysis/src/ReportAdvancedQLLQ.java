import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import java.awt.GridLayout;

import java.sql.Connection;
import java.awt.Font;




import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalExclusionType;

public class ReportAdvancedQLLQ extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//ReportAdvancedQLLQ frame = new ReportAdvancedQLLQ();
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void showUI(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setFont(new Font("Dialog", Font.PLAIN, 14));
		setType(Type.UTILITY);
		setResizable(false);
		setTitle("品種價值分析表");
		setBounds(100, 100, 450, 262);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(3, 1, 0, 0));
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel label = new JLabel("\u54C1\u7A2E");
		label.setBounds(10, 10, 55, 18);
		label.setFont(new Font("新細明體", Font.PLAIN, 14));
		panel_2.add(label);
		
		JComboBox comboBox_4 = new JComboBox();
		comboBox_4.setBounds(47, 9, 73, 21);
		panel_2.add(comboBox_4);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblgp = new JLabel("毛利GP%");
		lblgp.setFont(new Font("新細明體", Font.PLAIN, 14));
		lblgp.setBounds(10, 16, 67, 15);
		panel.add(lblgp);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("新細明體", Font.PLAIN, 14));
		comboBox.setBounds(75, 10, 47, 21);
		panel.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"<=", ">=", "=", "<", ">"}));
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setFont(new Font("新細明體", Font.PLAIN, 14));
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "95", "100"}));
		comboBox_1.setBounds(132, 10, 47, 21);
		panel.add(comboBox_1);
		
		JLabel label_1 = new JLabel("%");
		label_1.setFont(new Font("新細明體", Font.PLAIN, 14));
		label_1.setBounds(189, 16, 23, 15);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("過去");
		label_2.setFont(new Font("新細明體", Font.PLAIN, 14));
		label_2.setBounds(10, 48, 35, 15);
		panel.add(label_2);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setFont(new Font("新細明體", Font.PLAIN, 14));
		comboBox_2.setBounds(43, 45, 47, 21);
		panel.add(comboBox_2);
		
		JLabel label_3 = new JLabel("年, 總銷售重量");
		label_3.setFont(new Font("新細明體", Font.PLAIN, 14));
		label_3.setBounds(97, 48, 100, 15);
		panel.add(label_3);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setModel(new DefaultComboBoxModel(new String[] {"<=", ">=", "=", "<", ">"}));
		comboBox_3.setFont(new Font("新細明體", Font.PLAIN, 14));
		comboBox_3.setBounds(199, 45, 47, 21);
		panel.add(comboBox_3);
		
		JLabel label_4 = new JLabel("公斤");
		label_4.setFont(new Font("新細明體", Font.PLAIN, 14));
		label_4.setBounds(256, 48, 35, 15);
		panel.add(label_4);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JButton button = new JButton("顯示報表");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		panel_1.add(button);		
	}

	public ReportAdvancedQLLQ(Connection con) {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		
		showUI();

	}
}
