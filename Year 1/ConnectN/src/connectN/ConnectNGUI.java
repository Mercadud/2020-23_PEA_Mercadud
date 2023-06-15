package connectN;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ConnectNGUI extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnEdit;
	private JMenu mnHelp;
	private JMenu mnFile;
	private JMenuItem mntmSave;
	private JMenuItem mntmLoad;
	private JMenuItem mntmExit;
	private JMenuItem mntmUndo;
	private JMenuItem mntmNew;
	private JMenuItem mntmInstructions;
	private JMenuItem mntmAbout;
	private JPanel gamePanel;
	private JPanel infoPanel;
	private JTextField fldRows;
	private JLabel lblCol;
	private JTextField fldCol;
	private JLabel lblN;
	private JTextField fldN;
	private JLabel lblRows;
	private JButton btnStart;
	private JTextField fldPlayerOne;
	private JLabel lblPlayerTwo;
	private JTextField fldPlayerTwo;
	private JLabel lblPlayerOne;

	private Game game;
	private JButton gameButton[][];
	private JLabel lblPlayerTurn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectNGUI frame = new ConnectNGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConnectNGUI() {
		setTitle("Connect-N by Mercadud");
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1920, 100, 1000, 700);

		menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBackground(Color.GRAY);
		setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		mnFile.setForeground(Color.WHITE);
		mnFile.setBackground(Color.GRAY);
		menuBar.add(mnFile);

		mntmNew = new JMenuItem("New");
		mntmNew.setEnabled(false);
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onNewPressed();
			}
		});
		mntmNew.setForeground(Color.WHITE);
		mntmNew.setBackground(Color.GRAY);
		mnFile.add(mntmNew);

		mntmSave = new JMenuItem("Save");
		mntmSave.setEnabled(false);
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onSavePressed();
			}
		});
		mntmSave.setForeground(Color.WHITE);
		mntmSave.setBackground(Color.GRAY);
		mnFile.add(mntmSave);

		mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onLoadPressed();
			}
		});
		mntmLoad.setForeground(Color.WHITE);
		mntmLoad.setBackground(Color.GRAY);
		mnFile.add(mntmLoad);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onExitPressed();
			}
		});
		mntmExit.setForeground(Color.WHITE);
		mntmExit.setBackground(Color.GRAY);
		mnFile.add(mntmExit);

		mnEdit = new JMenu("Edit");
		mnEdit.setForeground(Color.WHITE);
		menuBar.add(mnEdit);

		mntmUndo = new JMenuItem("Undo");
		mntmUndo.setEnabled(false);
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onUndoPressed();
			}
		});
		mntmUndo.setForeground(Color.WHITE);
		mntmUndo.setBackground(Color.GRAY);
		mnEdit.add(mntmUndo);

		mnHelp = new JMenu("Help");
		mnHelp.setForeground(Color.WHITE);
		menuBar.add(mnHelp);

		mntmInstructions = new JMenuItem("Instructions");
		mntmInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onInstructionsPressed();
			}
		});
		mntmInstructions.setForeground(Color.WHITE);
		mntmInstructions.setBackground(Color.GRAY);
		mnHelp.add(mntmInstructions);

		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onAboutpressed();
			}
		});
		mntmAbout.setForeground(Color.WHITE);
		mntmAbout.setBackground(Color.GRAY);
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		infoPanel = new JPanel();
		infoPanel.setBounds(0, 0, 992, 62);
		infoPanel.setBorder(new LineBorder(Color.BLACK, 1, true));
		infoPanel.setBackground(Color.DARK_GRAY);
		infoPanel.setLayout(null);
		contentPane.add(infoPanel);

		lblRows = new JLabel("Rows: ");
		lblRows.setForeground(Color.WHITE);
		lblRows.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRows.setBounds(12, 12, 70, 15);
		infoPanel.add(lblRows);

		fldRows = new JTextField();
		fldRows.setToolTipText("The number of rows you want in your game");
		fldRows.setBackground(Color.GRAY);
		fldRows.setBounds(100, 10, 114, 19);
		infoPanel.add(fldRows);
		fldRows.setColumns(10);

		lblCol = new JLabel("Columns: ");
		lblCol.setForeground(Color.WHITE);
		lblCol.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCol.setBounds(232, 12, 70, 15);
		infoPanel.add(lblCol);

		fldCol = new JTextField();
		fldCol.setToolTipText("The number of columns you want in your game");
		fldCol.setBackground(Color.GRAY);
		fldCol.setBounds(320, 10, 114, 19);
		infoPanel.add(fldCol);
		fldCol.setColumns(10);

		lblN = new JLabel("Connect- what?");
		lblN.setForeground(Color.WHITE);
		lblN.setHorizontalAlignment(SwingConstants.RIGHT);
		lblN.setBounds(452, 12, 126, 15);
		infoPanel.add(lblN);

		fldN = new JTextField();
		fldN.setToolTipText("number of times pieces that have to align for a player to win");
		fldN.setBackground(Color.GRAY);
		fldN.setBounds(596, 10, 114, 19);
		infoPanel.add(fldN);
		fldN.setColumns(10);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onPlayPressed();
			}
		});
		btnStart.setForeground(Color.BLACK);
		btnStart.setBackground(Color.GRAY);
		btnStart.setBounds(853, 7, 117, 25);
		infoPanel.add(btnStart);

		lblPlayerOne = new JLabel("Player One Name: ");
		lblPlayerOne.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPlayerOne.setForeground(Color.WHITE);
		lblPlayerOne.setBounds(110, 41, 132, 15);
		infoPanel.add(lblPlayerOne);

		fldPlayerOne = new JTextField();
		fldPlayerOne.setBackground(Color.GRAY);
		fldPlayerOne.setBounds(260, 39, 114, 19);
		infoPanel.add(fldPlayerOne);
		fldPlayerOne.setColumns(10);

		lblPlayerTwo = new JLabel("Player Two Name: ");
		lblPlayerTwo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPlayerTwo.setForeground(Color.WHITE);
		lblPlayerTwo.setBounds(392, 41, 142, 15);
		infoPanel.add(lblPlayerTwo);

		fldPlayerTwo = new JTextField();
		fldPlayerTwo.setBackground(Color.GRAY);
		fldPlayerTwo.setBounds(552, 39, 114, 19);
		infoPanel.add(fldPlayerTwo);
		fldPlayerTwo.setColumns(10);

		lblPlayerTurn = new JLabel("Player Turn:");
		lblPlayerTurn.setEnabled(false);
		lblPlayerTurn.setForeground(Color.WHITE);
		lblPlayerTurn.setBounds(739, 41, 231, 15);
		infoPanel.add(lblPlayerTurn);

		gamePanel = new JPanel();
		gamePanel.setBounds(0, 67, 992, 577);
		gamePanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		gamePanel.setBackground(Color.DARK_GRAY);
		gamePanel.setLayout(null);
		contentPane.add(gamePanel);
	}

	private void onNewPressed() {
		gamePanel.removeAll();
		infoEnabled(true);
	}

	private void onSavePressed() {
		game.saveGame();
	}

	private void onLoadPressed() {
		game = new Game();
		infoEnabled(false);
		gameButton = new JButton[game.getBoard().length][game.getBoard()[0].length];
		gamePanel.setLayout(new GridLayout(game.getBoard().length, game.getBoard()[0].length));
		updateBoard();
	}

	private void onExitPressed() {
		System.exit(0);
	}

	private void onUndoPressed() {
		game.undoMove();
		updateBoard();
	}

	private void onInstructionsPressed() {
		JOptionPane.showMessageDialog(this, new GameInstructions(), "Instructions", JOptionPane.PLAIN_MESSAGE);
	}

	private void onAboutpressed() {
		JOptionPane.showMessageDialog(this, new GameAboutPanel(), "About", JOptionPane.PLAIN_MESSAGE);
	}

	private void onPlayPressed() {
		// check if values are actually values
		boolean isTrue = true;
		if (!(fldRows.getText().isEmpty() || fldCol.getText().isEmpty() || fldN.getText().isEmpty()
				|| fldPlayerOne.getText().isEmpty() || fldPlayerTwo.getText().isEmpty())) {
			for (int i = 0; i < fldRows.getText().length(); i++) {
				if (!Character.isDigit(fldRows.getText().charAt(i))) {
					isTrue = false;
					JOptionPane.showMessageDialog(this, "Please enter a valid number (Rows)", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
			for (int i = 0; i < fldCol.getText().length(); i++) {
				if (!Character.isDigit(fldCol.getText().charAt(i))) {
					isTrue = false;
					JOptionPane.showMessageDialog(this, "Please enter a valid number (Columns)", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
			for (int i = 0; i < fldN.getText().length(); i++) {
				if (!Character.isDigit(fldN.getText().charAt(i))) {
					isTrue = false;
					JOptionPane.showMessageDialog(this, "Please enter a valid number (N)", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Missing values", "ERROR", JOptionPane.ERROR_MESSAGE);
			isTrue = false;
		}
		if (isTrue) {
			game = new Game(Integer.parseInt(fldRows.getText()), Integer.parseInt(fldCol.getText()),
					Integer.parseInt(fldN.getText()), fldPlayerOne.getText(), fldPlayerTwo.getText());
			if (game.checkBoard()) {
				gameButton = new JButton[game.getBoard().length][game.getBoard()[0].length];
				infoEnabled(false);
				gamePanel.setLayout(new GridLayout(game.getBoard().length, game.getBoard()[0].length));
				updateBoard();
//				 this makes sure the frame doesn't need to resize to display the
//				 buttons/update the frame
//				 weird issue, I know\.\.\.
				validate();
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		for (int row = 0; row < gameButton.length; row++) {
			for (int col = 0; col < gameButton[row].length; col++) {
				if (e.getSource() == gameButton[row][col]) {
					game.playTurn((row + 1) + ", " + (col + 1));
					updateBoard();
					char win = game.checkWinner();
					if (win == 'R') {
						JOptionPane.showMessageDialog(this, game.getPlayerTwo() + " has won!", "Announcement!",
								JOptionPane.INFORMATION_MESSAGE);
						endGame();
					} else if (win == 'Y') {
						JOptionPane.showMessageDialog(this, game.getPlayerOne() + " has won!", "Announcement!",
								JOptionPane.INFORMATION_MESSAGE);
						endGame();
					} else if (win == ' ') {
						JOptionPane.showMessageDialog(this, "Board full,\nNobody wins:(", "Announcement!",
								JOptionPane.INFORMATION_MESSAGE);
						endGame();
					}
				}
			}
		}
	}

	private void endGame() {
		infoEnabled(true);
		for (int row = 0; row < gameButton.length; row++) {
			for (int col = 0; col < gameButton[row].length; col++) {
				gameButton[row][col].setEnabled(false);
			}
		}
	}

	private void updateBoard() {
		gamePanel.removeAll();
		for (int row = gameButton.length - 1; row >= 0; row--) {
			for (int col = 0; col < gameButton[1].length; col++) {
				gameButton[row][col] = new JButton();
				if (game.getBoard()[row][col] == 'R') {
					gameButton[row][col].setBackground(Color.RED);
					gameButton[row][col].setEnabled(false);
				} else if (game.getBoard()[row][col] == 'Y') {
					gameButton[row][col].setBackground(Color.YELLOW);
					gameButton[row][col].setEnabled(false);
				} else {
					gameButton[row][col].setBackground(Color.DARK_GRAY);
					if (row > 0) {
						if (game.getBoard()[row - 1][col] == 'E') {
							gameButton[row][col].setBackground(Color.BLACK);
							gameButton[row][col].setEnabled(false);
						}
					}
				}
				gamePanel.add(gameButton[row][col]);
				gameButton[row][col].addActionListener(this);
			}
		}
		lblPlayerTurn.setText("Player Turn: " + game.getPlayerTurn());

		validate();
	}

	private void infoEnabled(boolean enabled) {
		lblRows.setEnabled(enabled);
		fldRows.setEnabled(enabled);
		lblCol.setEnabled(enabled);
		fldCol.setEnabled(enabled);
		lblN.setEnabled(enabled);
		fldN.setEnabled(enabled);
		lblPlayerTwo.setEnabled(enabled);
		fldPlayerTwo.setEnabled(enabled);
		lblPlayerOne.setEnabled(enabled);
		fldPlayerOne.setEnabled(enabled);
		btnStart.setEnabled(enabled);
		mntmLoad.setEnabled(enabled);

		mntmNew.setEnabled(!enabled);
		mntmSave.setEnabled(!enabled);
		mntmUndo.setEnabled(!enabled);
		lblPlayerTurn.setEnabled(!enabled);
	}
}
