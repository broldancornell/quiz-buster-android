package com.example.k.quizbuster;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.k.quizbuster.dao.QuizDao;
import com.example.k.quizbuster.objects.QuestionsHandler;
import com.example.k.quizbuster.utility.Constants;
import com.example.k.quizbuster.utility.CurrentQuestionFetchCallback;
import com.example.k.quizbuster.utility.QuestionFetchCallback;
import com.example.k.quizbuster.utility.ResultFetchCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class LoadActivity extends AppCompatActivity {

    private boolean fetched;
    private String gameCode;
    private int lastQuestion;
    private String nickname;
    private static int count = 1;

    private static boolean paused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        paused = false;

        Typeface fontStyle = Typeface.createFromAsset(getAssets(), Constants.FONT_FILE_NAME);
        ((TextView)findViewById(R.id.text_view_status)).setTypeface(fontStyle);
        getActivityArguments();

        determineAction();


        RelativeLayout loadActivityHeadLayout = (RelativeLayout)findViewById(R.id.activity_load);
        // onLongClickListener is for changing background color
        loadActivityHeadLayout.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {

                if      (count % 6 == 1) {
                    v.setBackgroundColor(Color.RED);
                    count++;
                }
                else if (count % 6 == 2){
                    v.setBackgroundColor(Color.GREEN);
                    count++;
                }
                else if (count % 6 == 3){
                    v.setBackgroundColor(Color.BLUE);
                    count++;
                }
                else if (count % 6 == 4){
                    v.setBackgroundColor(Color.YELLOW);
                    count++;
                }
                else if (count % 6 == 5){
                    v.setBackgroundColor(Color.GRAY);
                    count++;
                }
                else if (count % 6 == 0){
                    v.setBackgroundColor(Color.WHITE);
                    count++;
                }
                return false; //return true;
            }
        });


    }

    private void getActivityArguments(){
        fetched = this.getIntent().getExtras().getBoolean(Constants.FETCHED_QUESTIONS_KEY);
        gameCode = this.getIntent().getExtras().getString(Constants.CURRENT_GAME_CODE_KEY);
        lastQuestion = this.getIntent().getExtras().getInt(Constants.LAST_QUESTION_KEY);
        nickname = this.getIntent().getExtras().getString(Constants.CURRENT_NICKNAME_KEY);
    }

    private void determineAction(){
        if(!fetched){

            if(paused){
                Log.i(getClass().getSimpleName(), "Paused...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        determineAction();
                    }
                }, Constants.PAUSE_FETCH_DELAY);
            }else{
                QuizDao.getInstance().getQuestions(gameCode, new QuestionFetchCallback() {
                    @Override
                    public void onValid(JSONObject data) {
                        try {
                            QuestionsHandler.getInstance().parseQuestions(data.getJSONArray("questions"));
                        } catch (JSONException exception) {
                            Log.e(getClass().getSimpleName(), "An error occurred while trying to save questions", exception);
                        }
                        fetched = true;

                        getNextQuestion();
                    }

                    @Override
                    public void onInvalid(String message) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                determineAction();
                            }
                        }, Constants.PAUSE_FETCH_DELAY);
                    }
                });
            }

        }else{
            int count = QuestionsHandler.getInstance().getQuestionsCount();
            //get current question if has count
            if(count > 0){
                getNextQuestion();
            }else{
                //get result if no count
                fetchResults();
            }
        }
    }

    private void getNextQuestion() {
        if(paused){
            Log.i(getClass().getSimpleName(), "Paused...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getNextQuestion();
                }
            }, Constants.PAUSE_FETCH_DELAY);
        }else{
            QuizDao.getInstance().getNextQuestion(gameCode, lastQuestion, new CurrentQuestionFetchCallback() {
                @Override
                public void onValid(int currentQuestion) {
                    moveToAnswerActivity(currentQuestion);
                }

                @Override
                public void onInvalid() {
                    new Handler().postDelayed(new LoadActivity.QuestionFetchLoop(), Constants.DATA_FETCH_DELAY);
                }
            });
        }
    }

    private void fetchResults() {

        if(paused){
            Log.i(getClass().getSimpleName(), "Paused...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchResults();
                }
            }, Constants.PAUSE_FETCH_DELAY);
        }else{
            QuizDao.getInstance().getResults(this.gameCode, this.nickname, new ResultFetchCallback() {
                @Override
                public void onValid(int result) {
                    moveToResultActivity(result);
                }

                @Override
                public void onInvalid() {
                    new Handler().postDelayed(new LoadActivity.ResultFetchLoop(), Constants.DATA_FETCH_DELAY);
                }
            });
        }
    }

    private void moveToAnswerActivity(int questionNumber){

        Intent questionActivity = new Intent(this, QuestionActivity.class);

        questionActivity.putExtra(Constants.CURRENT_GAME_CODE_KEY, this.gameCode);
        questionActivity.putExtra(Constants.CURRENT_NICKNAME_KEY, this.nickname);
        questionActivity.putExtra(Constants.LAST_QUESTION_KEY, questionNumber);

        this.startActivity(questionActivity);
    }

    private void moveToResultActivity(int bustPoints){
        Intent resultActivity = new Intent(this, ResultActivity.class);

        resultActivity.putExtra(Constants.CURRENT_GAME_CODE_KEY, this.gameCode);
        resultActivity.putExtra(Constants.CURRENT_NICKNAME_KEY, this.nickname);
        resultActivity.putExtra(Constants.BUST_POINTS_KEY, bustPoints);

        this.startActivity(resultActivity);
    }

    class QuestionFetchLoop implements Runnable{

        @Override
        public void run() {
            getNextQuestion();
        }
    }

    class ResultFetchLoop implements Runnable{

        @Override
        public void run() {
            fetchResults();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
    }

    @Override
    public void onBackPressed() {
    }
}
