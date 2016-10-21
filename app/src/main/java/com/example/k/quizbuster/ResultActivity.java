package com.example.k.quizbuster;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.k.quizbuster.utility.Constants;
import com.example.k.quizbuster.utility.JsonHttpRequest;
import com.example.k.quizbuster.utility.JsonHttpRequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    private TextView textViewFinalScore;
    private TextView textViewFinalScoreNumber;
    private Button buttonGoMain;

    private String gameCode;
    private String nickname;
    private int bustPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getActivityParameters();
        prepareWidgets();
    }

    private void prepareWidgets(){
        textViewFinalScore = (TextView) findViewById(R.id.text_view_final_score_label);
        textViewFinalScoreNumber = (TextView) findViewById(R.id.text_view_final_score_value);
        buttonGoMain = (Button) findViewById(R.id.button_go_home);

        Typeface fontStyle = Typeface.createFromAsset(getAssets(), "TimKid.ttf");

        textViewFinalScore.setTypeface(fontStyle);
        textViewFinalScoreNumber.setTypeface(fontStyle);
        buttonGoMain.setTypeface(fontStyle);

        textViewFinalScoreNumber.setText(String.valueOf(bustPoints));
    }

    private void getActivityParameters() {
        gameCode = this.getIntent().getExtras().getString(Constants.CURRENT_GAME_CODE_KEY);
        nickname = this.getIntent().getExtras().getString(Constants.CURRENT_NICKNAME_KEY);
        bustPoints = this.getIntent().getExtras().getInt(Constants.BUST_POINTS_KEY);
    }

    public void backToMainButtonClicked(View v) {
        Intent mainActivity = new Intent(this, MainActivity.class);
        this.startActivity(mainActivity);
    }

    @Override
    public void onBackPressed() {
    }
}
