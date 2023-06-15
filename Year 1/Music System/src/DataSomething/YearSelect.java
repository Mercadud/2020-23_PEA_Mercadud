package DataSomething;

import java.util.ArrayList;

import javax.swing.JPanel;

import javafx.scene.control.ComboBox;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;

public class YearSelect extends JPanel {
	private JComboBox comboBox;
	private JLabel lblSelectYear;
	String[] options;

	public YearSelect(ArrayList<Music> list) {
		setBackground(Color.DARK_GRAY);
		setLayout(null);

		lblSelectYear = new JLabel("Select Year:");
		lblSelectYear.setForeground(Color.WHITE);
		lblSelectYear.setBounds(12, 12, 92, 15);
		add(lblSelectYear);

		int numYears = 0;
		int currentYear = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getYear() != currentYear) {
				numYears++;
				currentYear = list.get(i).getYear();
			}
		}
		options = new String[numYears];
		currentYear = 0;
		numYears = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getYear() != currentYear) {
				currentYear = list.get(i).getYear();
				options[numYears] = currentYear + "";
				numYears++;
			}
		}
		comboBox = new JComboBox();
		comboBox.setForeground(Color.WHITE);
		comboBox.setModel(new DefaultComboBoxModel(options));
		comboBox.setBackground(Color.GRAY);
		comboBox.setBounds(122, 7, 116, 24);
		add(comboBox);
	}

	public String getYearSelected() {
		return options[comboBox.getSelectedIndex()];
	}

}
