package DataSomething;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;


/*
 * this program you a bunch of songs that are stored in music.txt
 */

public class MusicSystemFrame extends JFrame {
	/**
	 * So the challenge I gave myself for this assignment is to not allow the user
	 * to make a single error, so no error messages would pop up and make it pretty
	 * (by pretty I meant dark theme^v^).
	 */

	/*
	 * update: now that it's done, challenge success!
	 */
	private static final long serialVersionUID = 2836786238641159467L;
	private JMenu mnFile;
	private JMenuItem mntmWrite;
	private JMenuItem mntmExit;
	private JMenu mnSearch;
	private JMenuItem mntmByYearRanking;
	private JMenu mnReports;
	private JMenuItem mntmListAllMusic;
	private JMenuItem mntmListByYear;
	private JMenu mnSortMode;
	private JPanel contentPane;
	private JTextPane textPane;
	private JRadioButtonMenuItem rdbtnmntmBubbleSort;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButtonMenuItem rdbtnmntmExperimental;
	private JMenuItem mntmBySongTitle;
	private JMenuBar menuBar;
	private JMenuItem mntmReimportAndSort;
	private JScrollPane scroll;

	private MusicSystem thing;
	private Header head;
	private YearSelect yearSelect;
	private RankSelect rankSelect;
	private TitleSelect titleSelect;
	private StyledDocument document;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MusicSystemFrame frame = new MusicSystemFrame();
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
	public MusicSystemFrame() {
		setForeground(Color.WHITE);
		setTitle("Music Thing by Mercadud");
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1250, 600);
		// this makes sure the window will always start in the center, no more issues
		// with frame going off screen!
		setLocationRelativeTo(null);

		menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBackground(Color.BLACK);
		setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		mnFile.setForeground(Color.WHITE);
		menuBar.add(mnFile);

		mntmWrite = new JMenuItem("Write");
		mntmWrite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onWritePressed();
			}
		});
		mntmWrite.setForeground(Color.WHITE);
		mntmWrite.setBackground(Color.BLACK);
		mnFile.add(mntmWrite);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onExitPressed();
			}
		});
		mntmExit.setForeground(Color.WHITE);
		mntmExit.setBackground(Color.BLACK);
		mnFile.add(mntmExit);

		mnSearch = new JMenu("Search");
		mnSearch.setForeground(Color.WHITE);
		menuBar.add(mnSearch);

		mntmBySongTitle = new JMenuItem("By Song Title");
		mntmBySongTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSearchByTitlePressed();
			}
		});
		mntmBySongTitle.setForeground(Color.WHITE);
		mntmBySongTitle.setBackground(Color.BLACK);
		mnSearch.add(mntmBySongTitle);

		mntmByYearRanking = new JMenuItem("By Year/Ranking");
		mntmByYearRanking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSearchByYearPressed();
			}
		});
		mntmByYearRanking.setForeground(Color.WHITE);
		mntmByYearRanking.setBackground(Color.BLACK);
		mnSearch.add(mntmByYearRanking);

		mnReports = new JMenu("Reports");
		mnReports.setForeground(Color.WHITE);
		menuBar.add(mnReports);

		mntmListAllMusic = new JMenuItem("List All Music");
		mntmListAllMusic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onListAllPressed();
			}
		});
		mntmListAllMusic.setForeground(Color.WHITE);
		mntmListAllMusic.setBackground(Color.BLACK);
		mnReports.add(mntmListAllMusic);

		mntmListByYear = new JMenuItem("List by Year");
		mntmListByYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onListByYearPressed();
			}
		});
		mntmListByYear.setForeground(Color.WHITE);
		mntmListByYear.setBackground(Color.BLACK);
		mnReports.add(mntmListByYear);

		mnSortMode = new JMenu("Sort Mode");
		menuBar.add(mnSortMode);
		mnSortMode.setBackground(Color.BLACK);
		mnSortMode.setForeground(Color.WHITE);

		rdbtnmntmBubbleSort = new JRadioButtonMenuItem("Bubble Sort");
		rdbtnmntmBubbleSort.setForeground(Color.WHITE);
		rdbtnmntmBubbleSort.setBackground(Color.BLACK);
		rdbtnmntmBubbleSort.setSelected(true);
		buttonGroup.add(rdbtnmntmBubbleSort);
		mnSortMode.add(rdbtnmntmBubbleSort);

		rdbtnmntmExperimental = new JRadioButtonMenuItem("Experimental");
		rdbtnmntmExperimental.setForeground(Color.WHITE);
		rdbtnmntmExperimental.setBackground(Color.BLACK);
		buttonGroup.add(rdbtnmntmExperimental);
		mnSortMode.add(rdbtnmntmExperimental);

		mntmReimportAndSort = new JMenuItem("Re-import and Sort");
		mntmReimportAndSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onReloadPressed();
			}
		});
		mntmReimportAndSort.setForeground(Color.WHITE);
		mntmReimportAndSort.setBackground(Color.BLACK);
		mnSortMode.add(mntmReimportAndSort);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));

		textPane = new JTextPane();
		textPane.setFont(new Font("Andale Mono", Font.BOLD, 12));
		textPane.setForeground(Color.WHITE);
		textPane.setBackground(Color.DARK_GRAY);
		textPane.setEditable(false);
		contentPane.add(textPane);

		scroll = new JScrollPane(textPane);
		contentPane.add(scroll);
		document = (StyledDocument) textPane.getDocument();

		new UIManager();
		UIManager.put("OptionPane.background", Color.DARK_GRAY);
		UIManager.put("Panel.background", Color.DARK_GRAY);
		UIManager.put("Button.background", Color.GRAY);
		UIManager.put("Button.foreground", Color.WHITE);
		UIManager.put("Label.foreground", Color.WHITE);

		thing = new MusicSystem();
		thing.bubbleSort(false);
	}

	private void onWritePressed() {
		thing.write();
	}

	private void onExitPressed() {
		System.exit(-1);
	}

	private void onSearchByTitlePressed() {

		thing.sortByName();
		titleSelect = new TitleSelect(thing.getList());
		/*
		 * I really don't know why this is not showing the entire panel, but this will
		 * do for now...
		 */
		int input = JOptionPane.showOptionDialog(this, titleSelect, "Select", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (input == JOptionPane.OK_OPTION) {
			head = new Header("Song Details", 25);
			fancyPrintMusic(thing.findSongByTitle(titleSelect.getSongSelected()));
			setSize(300, 200);
		}
		thing.bubbleSort(false);
	}

	private void onSearchByYearPressed() {

		yearSelect = new YearSelect(thing.getList());
		int input = JOptionPane.showOptionDialog(this, yearSelect, "Select", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (input == JOptionPane.OK_OPTION) {
			rankSelect = new RankSelect(Integer.parseInt(yearSelect.getYearSelected()), thing.getList());
			input = JOptionPane.showOptionDialog(this, rankSelect, "Select", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, null, null);
			if (input == JOptionPane.OK_OPTION) {
				head = new Header("Song Details", 25);
				Music m = thing.findSongByYearRanking(Integer.parseInt(yearSelect.getYearSelected()),
						Integer.parseInt(rankSelect.getRankSelected()));
				fancyPrintMusic(m);
				setSize(300, 200);
			}
		}
	}

	private void onListAllPressed() {
		setSize(1250, 600);
		head = new Header("Listing All Songs", 150);
		ArrayList<Music> list = thing.getList();
		textPane.setText(head.getCentredTitleUnderlined());
		int currentYear = list.get(0).getYear();
		int totalDownloads = 0;

		try {
			document.insertString(document.getLength(), String.format("%-15s%-15s%-40s%-60s%-20s%n", "Year", "Ranking",
					"Artist", "Song Title", "# Downloads"), null);
			document.insertString(document.getLength(),
					String.format("%s%n",
							"-------------------------------------------------------------------"
									+ "--------------------------------------------------------------------------"),
					null);
		} catch (BadLocationException e1) {
			JOptionPane.showMessageDialog(this, e1, "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		for (int i = 0; i < list.size(); i++) {
			if (currentYear != list.get(i).getYear()) {

				try {
					document.insertString(document.getLength(),
							"\nTotal downloads for year: " + currentYear + " is " + totalDownloads + "\n\n", null);
				} catch (BadLocationException e) {
					JOptionPane.showMessageDialog(this, e, "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				currentYear = list.get(i).getYear();
				totalDownloads = 0;
			}
			printMusic(list.get(i));
			totalDownloads += list.get(i).getDownloads();
		}
		try {
			document.insertString(document.getLength(),
					"\nTotal downloads for year: " + currentYear + " is " + totalDownloads + "\n\n", null);
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(this, e, "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onListByYearPressed() {
		yearSelect = new YearSelect(thing.getList());
		int input = JOptionPane.showOptionDialog(this, yearSelect, "Select", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (input == JOptionPane.OK_OPTION) {
			head = new Header("Top Songs of " + yearSelect.getYearSelected(), 150);
			ArrayList<Music> list = thing.getList();
			textPane.setText(head.getCentredTitleUnderlined());
			try {
				document.insertString(document.getLength(), String.format("%-15s%-15s%-40s%-60s%-20s%n", "Year",
						"Ranking", "Artist", "Song Title", "# Downloads"), null);
				document.insertString(document.getLength(),
						String.format("%s%n",
								"-------------------------------------------------------------------"
										+ "--------------------------------------------------------------------------"),
						null);
			} catch (BadLocationException e1) {
				JOptionPane.showMessageDialog(this, e1, "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getYear() == Integer.parseInt(yearSelect.getYearSelected())) {
					printMusic(list.get(i));
				}
			}
			setSize(1250, 600);
		}
	}

	private void printMusic(Music m) {
		try {
			document.insertString(document.getLength(), String.format("%-15s%-15s%-40s%-60s%-20s%n", m.getYear(),
					m.getRank(), m.getArtist(), m.getSong(), m.getDownloads()), null);
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(this, e, "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void fancyPrintMusic(Music m) {
		textPane.setText(head.getCentredTitleUnderlined());
		try {
			document.insertString(document.getLength(),
					String.format("%-15s%s%n%-15s%s%n%-15s%s%n%-15s%s%n%-15s%s%n", "Year:", m.getYear(), "Ranking:",
							m.getRank(), "Artist:", m.getArtist(), "Song Title:", m.getSong(), "# Downloads:",
							m.getDownloads()),
					null);
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(this, e, "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * This only exists so I could flex how much my method is better than bubble sort
	 */
	private void onReloadPressed() {
		thing.load();
		if (rdbtnmntmBubbleSort.isSelected()) {
			thing.bubbleSort(true);
		} else if (rdbtnmntmExperimental.isSelected()) {
			thing.experimentalSort(true);
		}
	}
}
