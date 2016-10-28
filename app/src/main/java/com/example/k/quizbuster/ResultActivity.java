package com.example.k.quizbuster;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.quizbuster.utility.Constants;

public class ResultActivity extends AppCompatActivity {

    private TextView textViewFinalScore;
    private TextView textViewFinalScoreNumber;
    private Button   buttonGoMain;

    private String   gameCode;
    private String   nickname;
    private int      bustPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getActivityParameters();
        prepareWidgets();

        // Code for ActionBar
        ActionBar bar = this.getSupportActionBar();
        //bar.setDisplayHomeAsUpEnabled(true);  //set Up button
        bar.setTitle("More Info.");   //set title of Action Bar
        bar.setDisplayShowTitleEnabled(true);
        bar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.aboutKiwiDigital) {
            Toast.makeText(ResultActivity.this, "You Clicked Kiwi Digital", Toast.LENGTH_SHORT).show();

            Intent fragmentActivity = new Intent(this, FragmentActivity.class);
            this.startActivity(fragmentActivity);
        }
        else if (id == R.id.locations) {
            Toast.makeText(ResultActivity.this, "You clicked Locations", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
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
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(mainActivity);
        this.finish();
    }

    @Override
    public void onBackPressed() {
    }
}
