package com.example.k.quizbuster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.quizbuster.objects.Question;
import com.example.k.quizbuster.objects.QuestionsHandler;
import com.example.k.quizbuster.utility.Constants;
import com.example.k.quizbuster.utility.JsonHttpRequest;
import com.example.k.quizbuster.utility.JsonHttpRequestCallback;

import org.json.JSONObject;

public class QuestionActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private TextView textViewTimeLabel;
    private TextView textViewTimeValue;

    private ImageButton buttonAnswerOne;
    private ImageButton buttonAnswerTwo;
    private ImageButton buttonAnswerThree;
    private ImageButton buttonAnswerFour;

    private int questionNumber;
    private String gameCode;
    private String nickname;

    private Question currentQuestion;

    private ProgressDialog progressDialog;
    private Timer timer;

    private final String answerEndPoint = Constants.HOST_NAME + "/service/buster/answer.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        getActivityArguments();
        prepareWidgets();
        prepareQuestion();
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

        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Please wait");
    }

    private void getActivityArguments(){
        questionNumber = this.getIntent().getExtras().getInt(Constants.LAST_QUESTION_KEY);
        gameCode = this.getIntent().getExtras().getString(Constants.CURRENT_GAME_CODE_KEY);
        nickname = this.getIntent().getExtras().getString(Constants.CURRENT_NICKNAME_KEY);
    }

    private void prepareQuestion(){
        currentQuestion = QuestionsHandler.getInstance().getQuestion(questionNumber);

        String secondsString;

        if(currentQuestion.getSecondsToAnswer() == -1){
            secondsString = "-";
        }else{
            secondsString = String.valueOf(currentQuestion.getSecondsToAnswer());
        }

        this.textViewTimeValue.setText(secondsString);

        //determine the number of the options to be shown.
        switch(currentQuestion.getNumberOfOptions()){
            case 3:
                TableRow.LayoutParams layoutParameters = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                buttonAnswerThree.setLayoutParams(layoutParameters);
                buttonAnswerFour.setVisibility(View.GONE);
                break;
            case 2:
                buttonAnswerThree.setVisibility(View.GONE);
                buttonAnswerFour.setVisibility(View.GONE);
                break;
        }

        this.timer = new Timer(new TimerEndListener() {
            @Override
            public void performAction() {
                triggerTimeRanOut();
            }
        }, this.textViewTimeValue);
        if(currentQuestion.getSecondsToAnswer() != -1){
            timer.start();
            timer.setTime(currentQuestion.getSecondsToAnswer());
        }
    }

    private void triggerTimeRanOut() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeRanOutAction();
            }
        });
    }

    private void timeRanOutAction(){
        moveToWaitingActivity();
    }

    public void onBtnAnswer1(View view) {
        sendAnswer(1);
    }

    public void onBtnAnswer2(View view) {
        sendAnswer(2);
    }

    public void onBtnAnswer3(View view) {
        sendAnswer(3);
    }

    public void onBtnAnswer4(View view) {
        sendAnswer(4);
    }

    private void sendAnswer(int answerNumber){

        this.progressDialog.show();
        this.timer.unset();

        timer.unset();
        int timeLeft = timer.getSeconds();
        Log.i(this.getClass().getSimpleName(), "Time recorded: " + timeLeft);
        String parameters = "game_code=" + this.gameCode + "&nickname=" + this.nickname + "&question_number=" + questionNumber + "&answer=" + answerNumber + "&time_left=" + timeLeft;

        JsonHttpRequest request = new JsonHttpRequest(answerEndPoint + parameters, new JsonHttpRequestCallback() {
            @Override
            public void onCompleted(JSONObject data) {
                moveToWaitingActivity();
            }

            @Override
            public void onError(String message) {
                progressDialog.hide();
                Log.e(this.getClass().getSimpleName(), message);
            }
        });

        request.execute();
    }

    private void moveToWaitingActivity(){

        progressDialog.hide();
        Intent loadActivity = new Intent(this, LoadActivity.class);

        //prepare quiz values
        loadActivity.putExtra(Constants.CURRENT_GAME_CODE_KEY, this.gameCode);
        loadActivity.putExtra(Constants.CURRENT_NICKNAME_KEY, this.nickname);
        loadActivity.putExtra(Constants.LAST_QUESTION_KEY, this.questionNumber);
        loadActivity.putExtra(Constants.FETCHED_QUESTIONS_KEY, true);

        this.startActivity(loadActivity);
    }

    class Timer extends Thread {

        private TimerEndListener listener;
        private int seconds;
        private boolean set;
        private boolean alive;

        private TextView timerTextView;

        Timer(TimerEndListener listener, TextView timerTextView){
            this.listener = listener;
            this.seconds = 0;
            this.set = false;
            this.alive = true;
            this.timerTextView = timerTextView;
        }

        public void setTime(int seconds){
            this.set = true;
            this.seconds = seconds;
            updateTextView();
        }

        public int getSeconds(){
            return this.seconds;
        }

        public void unset(){
            this.set = false;
        }

        public void kill(){
            this.alive = false;
        }

        @Override
        public void run() {

            while(alive){

                if(set){
                    this.seconds--;
                    if(this.seconds == -1){
                        this.set = false;
                        this.listener.performAction();
                    }else{
                        updateTextView();
                    }

                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        private void updateTextView(){
            String secondsString = String.valueOf(this.seconds);
            if(secondsString.length() < 2)
                secondsString = 0 + secondsString;

            final String finalSecondsString = secondsString;

            QuestionActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timerTextView.setText(finalSecondsString);
                }
            });
        }

    }

    interface TimerEndListener{
        void performAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(this.timer != null)
            timer.kill();
    }

    @Override
    public void onBackPressed() {
    }

}
