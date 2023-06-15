package DataSomething;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RankSelect extends JPanel {
	private JLabel lblSelectRank;
	private JComboBox comboBox;

	
	private String[] options;
	/**
	 * Create the panel.
	 */
	public RankSelect(int year, ArrayList<Music> list) {
		setBackground(Color.DARK_GRAY);
		setLayout(null);
		
		lblSelectRank = new JLabel("Select Rank:");
		lblSelectRank.setForeground(Color.WHITE);
		lblSelectRank.setBounds(12, 12, 88, 15);
		add(lblSelectRank);
		
		int rankNum = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getYear() == year) {
				rankNum++;
			}
		}
		options = new String[rankNum];
		for (int i = 1; i <= options.length; i++) {
			options[i-1] = i + "";
		}
		
		comboBox = new JComboBox();
		comboBox.setForeground(Color.WHITE);
		comboBox.setBackground(Color.GRAY);
		comboBox.setModel(new DefaultComboBoxModel(options));
		comboBox.setBounds(118, 7, 62, 24);
		add(comboBox);
	}
	
	public String getRankSelected() {
		return comboBox.getSelectedItem() + "";
	}

}
