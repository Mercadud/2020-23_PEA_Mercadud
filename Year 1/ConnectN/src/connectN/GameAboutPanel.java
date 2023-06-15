package connectN;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.SwingConstants;

public class GameAboutPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public GameAboutPanel() {
		setBackground(Color.LIGHT_GRAY);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblAboutMenu = new JLabel("About Menu");
		lblAboutMenu.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblAboutMenu = new GridBagConstraints();
		gbc_lblAboutMenu.insets = new Insets(0, 0, 5, 5);
		gbc_lblAboutMenu.anchor = GridBagConstraints.NORTH;
		gbc_lblAboutMenu.gridx = 3;
		gbc_lblAboutMenu.gridy = 0;
		add(lblAboutMenu, gbc_lblAboutMenu);
		
		JLabel lblNameMercadudmarc = new JLabel("Name: Mercadud (Marc Daaboul)");
		lblNameMercadudmarc.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNameMercadudmarc = new GridBagConstraints();
		gbc_lblNameMercadudmarc.insets = new Insets(0, 0, 5, 5);
		gbc_lblNameMercadudmarc.gridx = 3;
		gbc_lblNameMercadudmarc.gridy = 1;
		add(lblNameMercadudmarc, gbc_lblNameMercadudmarc);
		
		JLabel lblHeritageCollege = new JLabel("Heritage College");
		lblHeritageCollege.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblHeritageCollege = new GridBagConstraints();
		gbc_lblHeritageCollege.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeritageCollege.gridx = 3;
		gbc_lblHeritageCollege.gridy = 2;
		add(lblHeritageCollege, gbc_lblHeritageCollege);
		
		JLabel label = new JLabel("2021");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 3;
		gbc_label.gridy = 3;
		add(label, gbc_label);

	}

}
