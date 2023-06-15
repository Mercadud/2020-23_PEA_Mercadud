package com.example.spidermansmistakemobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import gameLogic.Player;
import gameLogic.Scorage;
import gameLogic.Singleton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Singleton.setFile(getFilesDir());
        Singleton.setAssets(getAssets());
        addExistingPlayers();
    }

    public void onCreatePlayerClick(View v) {
        Intent i = new Intent(this, CreatePlayerActivity.class);
        startActivity(i);
    }

    public void onLeaderboard(View v) {
        Intent i = new Intent(this, LeaderboardActivity.class);
        startActivity(i);
    }

    public void onHelp(View v) {
        Intent i = new Intent(this, HelpActivity.class);
        startActivity(i);
    }

    public void onExit(View v) {
        finish();
        System.exit(1);
    }

    private void addExistingPlayers() {
        Singleton.getScorage().sortPlayersAlpha();
        TableLayout tl = (TableLayout) findViewById(R.id.table);
        tl.setStretchAllColumns(true);
        for (int i = 0; i < Singleton.getScorage().getPlayers().getLength(); i++) {
            Player p = Singleton.getScorage().getPlayers().getElementAt(i);
            if (p.getNumWordsRemaining() > 0) {
                TableRow row = new TableRow(this);
                TextView name = new TextView(this);
                name.setText(p.getName());
                row.addView(name);
                TextView wordsRemaining = new TextView(this);
                wordsRemaining.setText(p.getNumWordsRemaining() + " words remaining");
                row.addView(wordsRemaining);
                Button b = new Button(this);
                b.setText("play");
                b.setBackgroundColor(Color.rgb(68, 123, 190));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Singleton.setPlayer(Singleton.getScorage().getPlayerByName(p.getName()));
                        goToGame();
                    }
                });
                row.addView(b);
                row.setPadding(0, 10, 0, 10);
                tl.addView(row);
            }
        }
    }

    private void goToGame() {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }
}