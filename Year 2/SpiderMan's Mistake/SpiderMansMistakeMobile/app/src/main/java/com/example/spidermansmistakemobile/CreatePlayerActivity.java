package com.example.spidermansmistakemobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import gameLogic.Player;
import gameLogic.Singleton;

public class CreatePlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);
    }

    public void onCreatePlayer(View v) {
        String text = ((TextView) findViewById(R.id.InputPlayerNameET)).getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
        else if (text.length() >= 25) {
            Toast.makeText(this, "Max Character length is 25", Toast.LENGTH_SHORT).show();
        }
        else if (Singleton.getScorage().getPlayerByName(text) != null) {
            Toast.makeText(this, "Player name already taken", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Singleton.getScorage().addPlayer(text);
                Intent i = new Intent(this, GameActivity.class);
                Singleton.setPlayer(Singleton.getScorage().getPlayerByName(text));
                Singleton.getScorage().saveInfo();
                startActivity(i);
            } catch (Throwable e) {
                Toast.makeText(this, "Dictionary file non existent", Toast.LENGTH_SHORT).show();
            }
        }
    }
}