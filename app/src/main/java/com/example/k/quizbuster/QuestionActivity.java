package com.example.k.quizbuster;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends AppCompatActivity {

    TextView    txtQuestion;
    TextView    txtQuestionNumber;
    TextView    txtTime;
    TextView    txtTimeNumber;

    ImageButton btnAnswer1;
    ImageButton btnAnswer2;
    ImageButton btnAnswer3;
    ImageButton btnAnswer4;
    ImageButton btnShowResult;

    Typeface fontStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        txtQuestion       = (TextView)    findViewById(R.id.txtQuestion);
        txtQuestionNumber = (TextView)    findViewById(R.id.txtQuestionNumber);
        txtTime           = (TextView)    findViewById(R.id.txtTime);
        txtTimeNumber     = (TextView)    findViewById(R.id.txtTimeNumber);

        btnAnswer1        = (ImageButton) findViewById(R.id.btnAnswer1);
        btnAnswer2        = (ImageButton) findViewById(R.id.btnAnswer2);
        btnAnswer3        = (ImageButton) findViewById(R.id.btnAnswer3);
        btnAnswer4        = (ImageButton) findViewById(R.id.btnAnswer4);
        btnShowResult     = (ImageButton) findViewById(R.id.btnShowResult);

        fontStyle = Typeface.createFromAsset(getAssets(), "TimKid.ttf");

        txtQuestion.setTypeface(fontStyle);
        txtQuestionNumber.setTypeface(fontStyle);
        txtTime.setTypeface(fontStyle);
        txtTimeNumber.setTypeface(fontStyle);
    }

    public void onBtnAnswer1(View v) {
        Toast.makeText(this,"Answer 1 Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onBtnAnswer2(View v) {
        Toast.makeText(this,"Answer 2 Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onBtnAnswer3(View v) {
        Toast.makeText(this,"Answer 3 Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onBtnAnswer4(View v) {
        Toast.makeText(this,"Answer 4 Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onResultClicked(View v) {
        Intent resultActivity = new Intent(this, ResultActivity.class);
        this.startActivity(resultActivity);
    }

}
