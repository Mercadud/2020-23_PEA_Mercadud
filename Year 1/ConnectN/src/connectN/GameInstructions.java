package connectN;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

public class GameInstructions extends JPanel {
	private JLabel lblInstrustions;
	private JLabel lblSetup;
	private JLabel lblRowsAndColumns;
	private JLabel lblNMustAlso;
	private JLabel lblNCantBe;
	private JLabel lblHowToWin;
	private JLabel lblTryToGet;
	private JLabel lblInAllDirections;
	private JLabel lblPreventYourOpponent;
	private JSeparator separator;
	private JLabel label;

	/**
	 * Create the panel.
	 */
	public GameInstructions() {
		setBackground(Color.LIGHT_GRAY);
		setLayout(new GridLayout(0, 1, 0, 0));
		
		lblInstrustions = new JLabel("Instructions");
		lblInstrustions.setFont(new Font("Dialog", Font.BOLD, 16));
		lblInstrustions.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblInstrustions);
		
		lblSetup = new JLabel("Setup:");
		lblSetup.setFont(new Font("Dialog", Font.BOLD, 14));
		add(lblSetup);
		
		lblRowsAndColumns = new JLabel("Rows and Columns MUST be between 4 and 14");
		add(lblRowsAndColumns);
		
		lblNMustAlso = new JLabel("N MUST also be between 4 and 14");
		add(lblNMustAlso);
		
		lblNCantBe = new JLabel("N CANT be bigger than Rows and Columns");
		add(lblNCantBe);
		
		separator = new JSeparator();
		add(separator);
		
		lblHowToWin = new JLabel("How to Win:");
		lblHowToWin.setFont(new Font("Dialog", Font.BOLD, 14));
		add(lblHowToWin);
		
		lblTryToGet = new JLabel("Try to get your colour N amount of times in a row");
		add(lblTryToGet);
		
		lblInAllDirections = new JLabel("in a direction to win!");
		add(lblInAllDirections);
		
		lblPreventYourOpponent = new JLabel("Prevent your opponent from winning at the same time!");
		add(lblPreventYourOpponent);
		
		label = new JLabel("");
		add(label);

	}
}
