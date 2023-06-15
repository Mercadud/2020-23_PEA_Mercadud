package DataSomething;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TitleSelect extends JPanel {
	private JLabel lblSelectSong;
	private JComboBox comboBox;

	private String[] options;
	/**
	 * Create the panel.
	 */
	public TitleSelect(ArrayList<Music> list) {
		setBackground(Color.DARK_GRAY);
		setLayout(null);
		
		lblSelectSong = new JLabel("Select Song:");
		lblSelectSong.setForeground(Color.WHITE);
		lblSelectSong.setBounds(12, 12, 89, 15);
		add(lblSelectSong);
		
		options = new String[list.size()];
		for (int i = 0; i < options.length; i++) {
			options[i] = list.get(i).getSong();
		}
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(options));
		comboBox.setBounds(119, 7, 169, 24);
		add(comboBox);
	}
	
	public String getSongSelected() {
		return options[comboBox.getSelectedIndex()];
	}

}
