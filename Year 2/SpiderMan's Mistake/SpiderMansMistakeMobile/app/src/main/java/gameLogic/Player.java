package gameLogic;

import java.io.Serializable;

import linked_data_structures.SinglyLinkedList;

public class Player implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1275441383653455841L;
    private SinglyLinkedList<Word> wordsRemaining;
    private Word currentWord;
    private int gamesPlayed;
    private int gamesWon;
    private String name;
    private boolean eol;

    public Player(String n, SinglyLinkedList<Word> dictionary) {
        wordsRemaining = dictionary;
        name = n;
        gamesPlayed = 0;
        gamesWon = 0;
        eol = false;
    }

    public boolean getEndOfLifeStatus() {
        return eol;
    }

    public void startNextWord() {
        if (wordsRemaining.getLength() > 0) {
            currentWord = wordsRemaining.remove(0);
        } else {
            eol = true;
        }
        gamesPlayed++;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getNumWordsRemaining() {
        return wordsRemaining.getLength();
    }

    public Word getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(Word thing) {
        currentWord = thing;
    }

    public String getName() {
        return name;
    }

    public void addWin() {
        gamesWon++;
    }
}
