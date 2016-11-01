package com.kiwidigital.k.quizbuster;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kiwidigital.k.quizbuster.utility.Constants;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;


public class ResultActivity extends AppCompatActivity {

    private TextView textViewFinalScore;
    private TextView textViewFinalScoreNumber;

    private Button buttonShare;
    private Button buttonGoMain;

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

            Intent introduceActivity = new Intent(this, IntroduceActivity.class);
            this.startActivity(introduceActivity);
        }

        return super.onOptionsItemSelected(item);
    }



    private void getActivityParameters() {
        gameCode = this.getIntent().getExtras().getString(Constants.CURRENT_GAME_CODE_KEY);
        nickname = this.getIntent().getExtras().getString(Constants.CURRENT_NICKNAME_KEY);
        bustPoints = this.getIntent().getExtras().getInt(Constants.BUST_POINTS_KEY);
    }


    private void prepareWidgets(){
        textViewFinalScore = (TextView) findViewById(R.id.text_view_final_score_label);
        textViewFinalScoreNumber = (TextView) findViewById(R.id.text_view_final_score_value);
        buttonShare = (Button) findViewById(R.id.button_fb_share);
        buttonGoMain = (Button) findViewById(R.id.button_go_home);

        Typeface fontStyle = Typeface.createFromAsset(getAssets(), "TimKid.ttf");

        textViewFinalScore.setTypeface(fontStyle);
        textViewFinalScoreNumber.setTypeface(fontStyle);
        buttonShare.setTypeface(fontStyle); 
        buttonGoMain.setTypeface(fontStyle);

        textViewFinalScoreNumber.setText(String.valueOf(bustPoints));
    }

    public void backToMainButtonClicked(View v) {
        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(mainActivity);
        this.finish();
    }

    public void shareOnFBClicked(View v){
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.facebook.com/quizbusterapp"))
                .setContentTitle("Hey! I got " + bustPoints + " bust points!")
                .build();
        ShareDialog.show(this, content);
    }

    @Override
    public void onBackPressed() {
    }
}
