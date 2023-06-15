package gameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import linked_data_structures.SinglyLinkedList;

public class Word implements Serializable {

	private SinglyLinkedList<Letter> letters;
	private int lives;
	private int fontSize;
	private SinglyLinkedList<Character> guessedLetters;

	// i made it public, because a getter for an enum sounds dumb
	public enum CheckIfWon {
		Loss,
		Null,
		Win,
	}

	public Word(String s) {
		letters = new SinglyLinkedList<>();
		for (int i = 0; i < s.length(); i++) {
			letters.add(new Letter(s.charAt(i)));
		}
		lives = 6;
		if (s.length() < 11) {
			fontSize = 99;
		} else if (s.length() < 17) {
			fontSize = 75;
		} else if (s.length() < 21) {
			fontSize = 60;
		} else if (s.length() < 31) {
			fontSize = 45;
		} else {
			// for debugging purposes
			fontSize = 1;
		}
		guessedLetters = new SinglyLinkedList<Character>();
	}

	public int getLives() {
		return lives;
	}

	public String getLetterString() {
		String wordString = "";
		for (int i = 0; i < letters.getLength(); i++) {
			Letter let = letters.getElementAt(i);
			if (let.getIsGuessed()) {
				wordString = let.getLetter() + wordString;
			} else {
				wordString = "_" + wordString;
			}
		}
		return wordString;
	}

	public String getAnswer() {
		String wordString = "";
		for (int i = 0; i < letters.getLength(); i++) {
			Letter let = letters.getElementAt(i);
			wordString = let.getLetter() + wordString;
		}
		return wordString;
	}

	public void guessLetter(char c) {
		boolean found = false;
		for (int i = 0; i < letters.getLength(); i++) {
			if (letters.getElementAt(i).checkLetter(c)) {
				found = true;
			}
		}
		if (!found) {
			lives--;
		}
		guessedLetters.add(c);
	}

	public CheckIfWon checkIfWon() {
		if (lives < 1) {
			return CheckIfWon.Loss;
		}
		for (int i = 0; i < letters.getLength(); i++) {
			if (!letters.getElementAt(i).getIsGuessed()) {
				return CheckIfWon.Null;
			}
		}
		return CheckIfWon.Win;
	}

	public int getFontSize() {
		return fontSize;
	}

	public SinglyLinkedList<Character> getGuessedLetters() {
		return guessedLetters;
	}

	public void activateHint() throws Exception {
		if (lives < 2) {
			throw new Exception("You're not supposed to kill yourself!");
		}
		lives--;
		SinglyLinkedList<Character> availableLetters = new SinglyLinkedList<>();
		for (int i = 0; i < letters.getLength(); i++) {
			boolean alreadyTaken = false;
			for (int a = 0; a < availableLetters.getLength(); a++) {
				if (Character.toLowerCase(availableLetters.getElementAt(a)) == Character
						.toLowerCase(letters.getElementAt(i).getLetter())) {
					alreadyTaken = true;
				}
			}
			if (!alreadyTaken) {
				boolean alreadyGuessed = false;
				for (int g = 0; g < guessedLetters.getLength(); g++) {
					if (Character.toLowerCase(guessedLetters.getElementAt(g)) == Character
							.toLowerCase(letters.getElementAt(i).getLetter())) {
						alreadyGuessed = true;
					}
				}
				if (!alreadyGuessed) {
					availableLetters.add(letters.getElementAt(i).getLetter());
				}
			}
		}
		Random rand = new Random();
		guessLetter(availableLetters.getElementAt(rand.nextInt(availableLetters.getLength())));
	}
}
