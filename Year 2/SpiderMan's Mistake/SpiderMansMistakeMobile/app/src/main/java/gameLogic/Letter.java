package gameLogic;

import java.io.Serializable;

public class Letter implements Serializable {

	private char letter;
	private boolean isGuessed;

	public Letter(char c) {
		letter = c;
		if (Character.isLetter(letter)) {
			isGuessed = false;
		} else {
			isGuessed = true;
		}
	}

	public boolean checkLetter(char c) {
		if (Character.toLowerCase(c) == Character.toLowerCase(letter)) {
			isGuessed = true;
			return true;
		}
		return false;
	}

	public boolean getIsGuessed() {
		return isGuessed;
	}

	public char getLetter() {
		return letter;
	}
}
