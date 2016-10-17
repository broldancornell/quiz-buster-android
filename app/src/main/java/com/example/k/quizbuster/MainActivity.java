package com.example.k.quizbuster;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    Typeface fontStyle;  // Typeface for Font sytle

    EditText txtgame_code; //game code
    Button   btnSendGameCode;  // button for sending game code
    private static final String SERVER_ADDRESS = "http://localhost/service/buster/validate.php?game_code=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtgame_code    = (EditText) findViewById(R.id.edit_text_game_code);
        btnSendGameCode = (Button)   findViewById(R.id.btnSendGameCode);

        fontStyle = Typeface.createFromAsset(getAssets(), "TimKid.ttf");
        txtgame_code.setTypeface(fontStyle);
        btnSendGameCode.setTypeface(fontStyle);
    }

    public void enterGameCode(View v) {
        String game_code = txtgame_code.getText().toString();


        Toast.makeText(this, "Game Code : " + game_code, Toast.LENGTH_SHORT).show();  // to check game code

        try {
            URL url = new URL(SERVER_ADDRESS + URLEncoder.encode(game_code, "UTF-8"));  // make server address with game code
            url.openStream();  // send game code to php
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        Toast.makeText(this, "Game URL  : " + SERVER_ADDRESS + game_code, Toast.LENGTH_SHORT).show();
        //To check URL by eyes

        Intent nicknameActivity = new Intent(this, NicknameActivity.class);

        Toast.makeText(this,"Welcome to Quiz Buster ! " + game_code, Toast.LENGTH_SHORT).show();
        nicknameActivity.putExtra("entered_quiz_code", game_code);
        this.startActivity(nicknameActivity);
    }
}
