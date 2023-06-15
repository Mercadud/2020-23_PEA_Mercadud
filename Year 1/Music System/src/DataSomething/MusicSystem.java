package DataSomething;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

/*
 * This class handles everything that has to do witht the music class
 */
public class MusicSystem {

	
	private ArrayList<Music> list;
	Stopwatch stopwatch;

	private static final String MUSICFILE = "music.txt";
	private static final String SORTEDFILE = "sortedMusic.txt";

	public MusicSystem() {
		list = new ArrayList<Music>();
		load();
		stopwatch = new Stopwatch();
	}

	/*
	 * well it obviously sorts the array by the name using bubble sort
	 */
	public void sortByName() {
		int arrLength = list.size();
		for (int i = 0; i < arrLength - 1; i++) {
			for (int j = 0; j < arrLength - i - 1; j++) {
				if (list.get(j).getSong().compareTo(list.get(j+1).getSong()) < 0) {
					Music temp = list.get(j);
					list.set(j, (list.get(j + 1)));
					list.set(j + 1, temp);
				}
			}
		}
	}
	
	/*
	 * sorts the array using bubble sort, from lowest to highest
	 */
	
	public void bubbleSort(boolean timed) {
		if (timed) {
			stopwatch.start();
		}

		int arrLength = list.size();
		for (int i = 0; i < arrLength - 1; i++) {
			for (int j = 0; j < arrLength - i - 1; j++) {
				if (list.get(j).getYear() > list.get(j + 1).getYear()) {
					Music temp = list.get(j);
					list.set(j, (list.get(j + 1)));
					list.set(j + 1, temp);
				} else if (list.get(j).getYear() == list.get(j + 1).getYear()) {
					if (list.get(j).getRank() > list.get(j + 1).getRank()) {
						Music temp = list.get(j);
						list.set(j, (list.get(j + 1)));
						list.set(j + 1, temp);
					}
				}
			}
		}
		if (timed) {
			stopwatch.stop();
			JOptionPane.showMessageDialog(null, "Bubble Sort took " + stopwatch.getTime() + " milliseconds long");
			stopwatch.reset();
		}
	}

	/*
	 * This is my attempt at making a brand new sorting algorithm, hoping its not
	 * already made and actually efficient (probably not...) I'm not even gonna try
	 * with the searching one...
	 */
	/*
	 * update: After I finished making an incredibly long algorithm, that isnt
	 * efficient... Charles-Etienne gave me the idea to use Binary Search on the
	 * second array to find the right spot for it, which may make it even faster
	 * than bubble sort
	 */
	public void experimentalSort(boolean timed) {
		if (timed) {
			stopwatch.start();
		}

		ArrayList<Music> sortedList = new ArrayList<Music>();
		int median = -1;
		while (!list.isEmpty()) {
			if (median != -1) {
				if (list.get(0).getYear() < sortedList.get(median).getYear()) {
					if (median == 0) {
						sortedList.add(0, list.get(0));
					} else {
						boolean placed = false;
						for (int i = 0; i <= median && !placed; i++) {
							if (list.get(0).getYear() < sortedList.get(i).getYear()) {
								placed = true;
								sortedList.add(i, list.get(0));
								list.remove(0);
							} else if (list.get(0).getYear() == sortedList.get(i).getYear()) {
								if (list.get(0).getRank() < sortedList.get(i).getRank()) {
									placed = true;
									sortedList.add(i, list.get(0));
									list.remove(0);
								}
							}
						}
						if (!placed) {
							sortedList.add(median, list.get(0));
							list.remove(0);
						}
					}
				} else if (list.get(0).getYear() > sortedList.get(median).getYear()) {
					if (median == 0) {
						sortedList.add(list.get(0));
					} else {
						boolean placed = false;
						for (int i = sortedList.size() - 1; i >= median && !placed; i--) {

							if (list.get(0).getYear() > sortedList.get(i).getYear()) {
								placed = true;
								sortedList.add(i + 1, list.get(0));
								list.remove(0);
							} else if (list.get(0).getYear() == sortedList.get(i).getYear()) {
								if (list.get(0).getRank() > sortedList.get(i).getRank()) {
									placed = true;
									sortedList.add(i + 1, list.get(0));
									list.remove(0);
								}
							}
						}
						if (!placed) {
							sortedList.add(median + 1, list.get(0));
							list.remove(0);
						}
					}
				} else if (list.get(0).getYear() == sortedList.get(median).getYear()) {
					int yearStart = -1;
					for (int i = median; i > 0; i--) {
						if (sortedList.get(i).getYear() == sortedList.get(median).getYear()) {
							yearStart = i;
						} else if (sortedList.get(i).getYear() != sortedList.get(median).getYear()) {
							break;
						}
					}
					boolean placed = false;
					for (int i = yearStart; sortedList.size() != i
							&& sortedList.get(i).getYear() == sortedList.get(yearStart).getYear() && !placed; i++) {
						if (list.get(0).getRank() < sortedList.get(i).getRank()) {
							sortedList.add(i, list.get(0));
							list.remove(0);
							placed = true;
						}
					}
					if (!placed) {
						sortedList.add(list.get(0));
						list.remove(0);
					}
				}
			} else if (median == -1) {
				sortedList.add(list.get(0));
				list.remove(0);
				median = 0;
			}
			median = sortedList.size() / 2;
		}
		list = sortedList;
		if (timed) {
			stopwatch.stop();
			JOptionPane.showMessageDialog(null, "Experimental Sort took " + stopwatch.getTime() + " milliseconds long");
			stopwatch.reset();
		}
		// this was a horrible idea...
	}

	/*
	 * does binary search to find the song u want
	 */
	public Music findSongByTitle(String title) {
		int bottom = 0, top = list.size(), middle;
		Music found = null;
		boolean isTrue = false;
		while (!isTrue) {
			middle = ((top - bottom)/2) + bottom;
			if (list.get(middle).getSong().compareTo(title) == 0) {
				isTrue = true;
				found = list.get(middle);
			}
			else if (list.get(middle).getSong().compareTo(title) < 0) {
				top = middle;
			}
			else if (list.get(middle).getSong().compareTo(title) > 0) {
				bottom = middle;
			}
		}
		return found;
	}
	
	/*
	 * does binary search to find the song u want
	 */

	public Music findSongByYearRanking(int yearS, int rankingS) {
		int bottom = 0, top = list.size(), middle;
		Music found = null;
		boolean isTrue = false;
		while (!isTrue) {
			middle = ((top - bottom)/2) + bottom;
			if (list.get(middle).getYear() == yearS) {
				if (list.get(middle).getRank() == rankingS) {
					found = list.get(middle);
					isTrue = true;
				}
				else if (list.get(middle).getRank() > rankingS) {
					top = middle;
				}
				else if (list.get(middle).getRank() < rankingS) {
					bottom = middle;
				}
			}
			else if (list.get(middle).getYear() > yearS) {
				top = middle;
			}
			else if (list.get(middle).getYear() < yearS) {
				bottom = middle;
			}
		}
		return found;
	}

	public ArrayList<Music> getByYear(int Year) {
		ArrayList<Music> listedYear = new ArrayList<Music>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getYear() == Year) {
				listedYear.add(list.get(i));
			}
		}
		return listedYear;
	}

	public void load() {
		list.clear();
		File file = new File(MUSICFILE);
		Scanner reader = null;
		try {
			reader = new Scanner(file);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, e, "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e, "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (reader != null) {
			boolean errorExists = false;
			reader.useDelimiter("\\*|\n");
			while (reader.hasNext()) {
				boolean isTrue = true;
				String y = reader.next();
				String r = reader.next();
				String a = reader.next();
				String s = reader.next();
				String d = reader.next();
				if (!checkIfNumber(y)) {
					isTrue = false;
				}
				if (!checkIfNumber(r)) {
					isTrue = false;
				}
				if (a.isEmpty()) {
					isTrue = false;
				}
				if (s.isEmpty()) {
					isTrue = false;
				}
				if (!checkIfNumber(d)) {
					isTrue = false;
				}

				if (isTrue) {
					list.add(new Music(Integer.parseInt(y), Integer.parseInt(r), a, s, Integer.parseInt(d)));
				} else {
					errorExists = true;
				}
			}
			if (errorExists) {
				JOptionPane.showMessageDialog(null, "File Corrupted, but we still tried to get as much information",
						"ERROR", JOptionPane.ERROR_MESSAGE);
			}
			reader.close();
		}
	}

	public void write() {
		File file = new File(SORTEDFILE);
		if (file.exists()) {
			file.delete();
		}
		try {
			FileWriter writer = new FileWriter(file, true);
			for (int i = 0; i < list.size(); i++) {
				Music m = list.get(i);
				writer.write(m.getYear() + "*" + m.getRank() + "*" + m.getArtist() + "*" + m.getSong() + "*"
						+ m.getDownloads() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e, "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public ArrayList<Music> getList() {
		return list;
	}

	private boolean checkIfNumber(String number) {
		boolean isTrue = true;
		for (int i = 0; i < number.length(); i++) {
			if (!Character.isDigit(number.charAt(i))) {
				isTrue = false;
				break;
			}
		}
		return isTrue;
	}
}
