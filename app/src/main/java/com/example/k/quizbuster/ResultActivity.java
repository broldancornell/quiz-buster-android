package com.example.k.quizbuster;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView txtRanked;
    TextView txtRankedNumber;
    TextView txtFinalScore;
    TextView txtFinalScoreNumber;
    Button   btnGoMain;

    Typeface fontStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtRanked           = (TextView) findViewById(R.id.txtRanked);
        txtRankedNumber     = (TextView) findViewById(R.id.txtRankedNumber);
        txtFinalScore       = (TextView) findViewById(R.id.txtFinalScore);
        txtFinalScoreNumber = (TextView) findViewById(R.id.txtFinalScoreNumber);
        btnGoMain           = (Button)   findViewById(R.id.btnGoMain);

        fontStyle = Typeface.createFromAsset(getAssets(), "TimKid.ttf");

        txtRanked.setTypeface(fontStyle);
        txtRankedNumber.setTypeface(fontStyle);
        txtFinalScore.setTypeface(fontStyle);
        txtFinalScoreNumber.setTypeface(fontStyle);
        btnGoMain.setTypeface(fontStyle);


    }

    public void onGoMainClicked(View v) {
        Intent mainActivity = new Intent(this, MainActivity.class);
        this.startActivity(mainActivity);

    }
}
