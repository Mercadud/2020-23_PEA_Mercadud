package com.example.spidermansmistakemobile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import gameLogic.Player;
import gameLogic.Singleton;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        addExistingPlayers();
    }

    private void addExistingPlayers() {
        TableLayout tl = (TableLayout) findViewById(R.id.tableLeader);
        tl.setStretchAllColumns(true);
        for (int i = 0; i < Singleton.getScorage().getPlayers().getLength(); i++) {
            Player p = Singleton.getScorage().getPlayers().getElementAt(i);
            TableRow row = new TableRow(this);
            TextView name = new TextView(this);
            name.setText(p.getName());
            row.addView(name);
            TextView wordsRemaining = new TextView(this);
            wordsRemaining.setText(p.getNumWordsRemaining() + " words remaining");
            row.addView(wordsRemaining);
            row.setPadding(0, 10, 0, 10);
            TextView wins = new TextView(this);
            wins.setText(p.getGamesWon() + " wins");
            row.addView(wins);
            tl.addView(row);

        }
    }
}