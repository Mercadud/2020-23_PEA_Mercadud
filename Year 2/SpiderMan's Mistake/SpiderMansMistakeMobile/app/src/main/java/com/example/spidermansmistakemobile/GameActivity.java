package com.example.spidermansmistakemobile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import gameLogic.Player;
import gameLogic.Singleton;
import gameLogic.Word;
import linked_data_structures.SinglyLinkedList;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        updateGame();
    }

    private void updateGame() {
        if (!Singleton.getPlayer().getEndOfLifeStatus()) {
            Word word = Singleton.getPlayer().getCurrentWord();
            if (word != null) {
                Word.CheckIfWon circumstance = word.checkIfWon();
                if (circumstance == Word.CheckIfWon.Win) {
                    updateDisplay();
                    new AlertDialog.Builder(this)
                            .setTitle("You Won!")
                            .setMessage("Click anywhere to start the next word!")
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    Singleton.getPlayer().startNextWord();
                                    Singleton.getPlayer().addWin();
                                    updateDisplay();
                                }
                            })
                            .create()
                            .show();
                } else if (circumstance == Word.CheckIfWon.Loss) {
                    updateDisplay();
                    final boolean[] clicked = {false};
                    new AlertDialog.Builder(this)
                            .setTitle("You Lost!")
                            .setMessage("Answer: " + Singleton.getPlayer().getCurrentWord().getAnswer())
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    Singleton.getPlayer().startNextWord();
                                    updateDisplay();
                                }
                            })
                            .create()
                            .show();

                } else if (circumstance == Word.CheckIfWon.Null) {
                    updateDisplay();
                } else {
                    System.err.println("HORRIBLE ERROR HAS OCCURRED, PLEASE BEGIN THE CRYING SEQUENCE");
                }
            } else {
                Singleton.getPlayer().startNextWord();
                updateDisplay();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Game End!")
                    .setMessage("No more words saved on this name\nRecords will be saved in the leaderboard.")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private void updateDisplay() {
        TextView lbl = ((TextView) findViewById(R.id.word));
        lbl.setText(Singleton.getPlayer().getCurrentWord().getLetterString());
        TableRow tr1 = findViewById(R.id.tr1);
        tr1.removeAllViews();
        TableRow tr2 = findViewById(R.id.tr2);
        tr2.removeAllViews();
        TableRow tr3 = findViewById(R.id.tr3);
        tr3.removeAllViews();

        // create all the letters
        SinglyLinkedList<Character> guessedLetters = Singleton.getPlayer().getCurrentWord().getGuessedLetters();
        try {
            ((TableLayout) tr3.getParent()).removeViewAt(3);
        } catch (Throwable ignored) {
        }
        int row;
        Button b;
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            boolean isGuessed = false;
            for (int i = 0; i < guessedLetters.getLength(); i++) {
                if (Character.toLowerCase(letter) == Character.toLowerCase(guessedLetters.getElementAt(i))) {
                    isGuessed = true;
                }
            }
            b = new Button(this);
            b.setText(String.valueOf(letter));
            final char finalLetter = letter;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Singleton.getPlayer().getCurrentWord().guessLetter(finalLetter);
                    updateGame();
                }
            });
            if (letter <= 'I') {
                row = 1;
            } else if (letter <= 'R') {
                row = 2;
            } else {
                row = 3;
            }
            b.setLayoutParams(new TableRow.LayoutParams(101, TableRow.LayoutParams.WRAP_CONTENT));
            switch (row) {
                case 1:
                    tr1.addView(b);
                    break;
                case 2:
                    tr2.addView(b);
                    break;
                case 3:
                    tr3.addView(b);
                    break;
            }
            b.setBackgroundColor(Color.rgb(223, 31, 45));
            if (isGuessed) {
                b.setEnabled(false);
                b.setBackgroundColor(Color.rgb(68, 123, 190));
            }
        }
        b = new Button(this);
        b.setText("Use Hint");
        if (Singleton.getPlayer().getCurrentWord().getLives() <= 1) {
            b.setEnabled(false);
        }
        ((TableLayout) tr1.getParent()).addView(b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Singleton.getPlayer().getCurrentWord().activateHint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateGame();
            }
        });
        ImageView img = (ImageView) findViewById(R.id.spederDie);
        img.setImageResource(getResources().getIdentifier("_" + Singleton.getPlayer().getCurrentWord().getLives(), "drawable", getPackageName()));

        ((TextView)findViewById(R.id.livesRemaining)).setText(Singleton.getPlayer().getCurrentWord().getLives() + "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Singleton.getScorage().saveInfo();
        finish();
    }
}