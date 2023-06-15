package gameLogic;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Scanner;

import linked_data_structures.DoublyLinkedList;
import linked_data_structures.SinglyLinkedList;

public class Scorage {

    private DoublyLinkedList<Player> players;
    private final String DICTIONARYLOC = "dictionary.txt";
    private final String PLAYERSLOC = "players.txt";

    public Scorage() {
        loadInfo();
        sortPlayersAlpha();
    }

    public void loadInfo() {
        players = new DoublyLinkedList<>();
        try {
            File file = new File(Singleton.getFile(), PLAYERSLOC);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream is = new ObjectInputStream(fis);
            players = (DoublyLinkedList<Player>) is.readObject();
            is.close();
        } catch (Exception e) {
            System.err.println("ERR: " + e);
        }
    }

    public void saveInfo() {
        try {
            File file = new File(Singleton.getFile(), PLAYERSLOC);
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(players);
            out.close();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player getPlayerByName(String name) {
        for (int i = 0; i < players.getLength(); i++) {
            if (name.equalsIgnoreCase(players.getElementAt(i).getName())) {
                return players.getElementAt(i);
            }
        }
        return null;
    }

    public void sortPlayersAlpha() {
        if (players.getLength() > 1) {
            int n = players.getLength();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (players.getElementAt(j).getName()
                            .compareToIgnoreCase(players.getElementAt(j + 1).getName()) > 0) {
                        try {
                            players.swapNodesBesideEachOther(j, j + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public DoublyLinkedList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(String s) throws Exception{
        try {
            SinglyLinkedList<Word> dictionary = new SinglyLinkedList<>();
            InputStream fis = Singleton.getAssets().open(DICTIONARYLOC);
            Scanner scan = new Scanner(fis);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!line.isEmpty() && line.length() < 31) {
                    dictionary.add(new Word(line));
                }
            }
            scan.close();
            SinglyLinkedList<Word> smallerDictionary = new SinglyLinkedList<>();
            Random rand = new Random();
            int amount;
            if (dictionary.getLength() < 50) {
                amount = dictionary.getLength();
            } else {
                amount = 50;
            }
            for (int i = 0; i < amount; i++) {
                smallerDictionary.add(dictionary.remove(rand.nextInt(dictionary.getLength())));
            }
            players.add(new Player(s, smallerDictionary));
            sortPlayersAlpha();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Dic non-existent");
        }
    }
}
