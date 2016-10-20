package com.example.k.quizbuster;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private TextView    txtQuestionNumber;
    private TextView textViewTimeLabel;
    private TextView textViewTimeValue;

    private ImageButton buttonAnswerOne;
    private ImageButton buttonAnswerTwo;
    private ImageButton buttonAnswerThree;
    private ImageButton buttonAnswerFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        prepareWidgets();
        
    }

    private void prepareWidgets(){
        textViewQuestion = (TextView) findViewById(R.id.text_view_question);
        textViewTimeLabel = (TextView) findViewById(R.id.text_view_time_label);
        textViewTimeValue = (TextView) findViewById(R.id.text_view_time_value);

        buttonAnswerOne = (ImageButton) findViewById(R.id.button_answer_1);
        buttonAnswerTwo = (ImageButton) findViewById(R.id.button_answer_2);
        buttonAnswerThree = (ImageButton) findViewById(R.id.button_answer_3);
        buttonAnswerFour = (ImageButton) findViewById(R.id.button_answer_4);

        Typeface fontStyle = Typeface.createFromAsset(getAssets(), "TimKid.ttf");

        textViewQuestion.setTypeface(fontStyle);
        textViewTimeLabel.setTypeface(fontStyle);
        textViewTimeValue.setTypeface(fontStyle);
    }

    public void onBtnAnswer1(View view) {
        answerSelected(1);
    }

    public void onBtnAnswer2(View view) {
        answerSelected(2);
    }

    public void onBtnAnswer3(View view) {
        answerSelected(3);
    }

    public void onBtnAnswer4(View view) {
        answerSelected(4);
    }

    private void answerSelected(int answerNumber){
        Toast.makeText(this,"Answer " + answerNumber + " Clicked", Toast.LENGTH_SHORT).show();
    }

}
