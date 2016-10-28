package com.example.k.quizbuster;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.k.quizbuster.objects.QuestionsHandler;
import com.example.k.quizbuster.utility.Constants;
import com.example.k.quizbuster.utility.JsonHttpRequest;
import com.example.k.quizbuster.utility.JsonHttpRequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class LoadActivity extends AppCompatActivity {

    private final String questionsEndPoint = Constants.HOST_NAME + "/service/buster/questions.php?game_code=";
    private final String currentQuestionEndPoint = Constants.HOST_NAME + "/service/buster/current.php?game_code=";
    private final String resultEndPoint = Constants.HOST_NAME + "/service/buster/results.php?";

    private boolean fetched;
    private String gameCode;
    private int lastQuestion;
    private String nickname;
    private static int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        getActivityArguments();

        determineAction();


        RelativeLayout activity_load = (RelativeLayout)findViewById(R.id.activity_load);
        // onLongClickListener is for changing background color
        activity_load.setOnLongClickListener(new View.OnLongClickListener(){
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
            //get questions
            JsonHttpRequest request = new JsonHttpRequest(questionsEndPoint + gameCode, new JsonHttpRequestCallback() {

                @Override
                public void onCompleted(JSONObject data) {
                    parseQuizzes(data);
                }

                @Override
                public void onError(String message) {
                    Log.e(this.getClass().getSimpleName(), message);
                }
            });
            request.execute();

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

        JsonHttpRequest request = new JsonHttpRequest(currentQuestionEndPoint + gameCode, new JsonHttpRequestCallback() {

            @Override
            public void onCompleted(JSONObject data) {
                parseCurrentQuiz(data);
            }

            @Override
            public void onError(String message) {
                Log.e(this.getClass().getSimpleName(), message);
            }
        });

        request.execute();
    }

    private void parseQuizzes(JSONObject result) {
        if(result == null){
            Log.e(this.getClass().getSimpleName(), "Result JSON is null.");
            return;
        }

        try {
            int status = result.getInt("status");
            String statusMessage = result.getString("status_message");
            if(status != 200){
                Log.e(this.getClass().getSimpleName(), "HTTP result status indicated an error: " + statusMessage);
                return;
            }

            //decrypt JSON object
            JSONObject data = new JSONObject(result.getString("data"));
            if(data == null){
                Log.e(this.getClass().getSimpleName(), "HTTP result did not include data object");
                return;
            }

            QuestionsHandler.getInstance().parseQuestions(data.getJSONArray("questions"));
            fetched = true;

            getNextQuestion();

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
        }
    }

    private void parseCurrentQuiz(JSONObject result) {
        if(result == null){
            Log.e(this.getClass().getSimpleName(), "Result JSON is null.");
            return;
        }

        try {
            int status = result.getInt("status");
            String statusMessage = result.getString("status_message");
            if(status != 200){
                Log.e(this.getClass().getSimpleName(), "HTTP result status indicated an error: " + statusMessage);
                return;
            }

            //decrypt JSON object
            JSONObject data = new JSONObject(result.getString("data"));
            if(data == null){
                Log.e(this.getClass().getSimpleName(), "HTTP result did not include data object");
                return;
            }

            int currentQuestion = data.getInt("current_question");
            if(currentQuestion != lastQuestion){
                moveToAnswerActivity(currentQuestion);
            }else{
                new Handler().postDelayed(new QuestionFetchLoop(), Constants.DATA_FETCH_DELAY);
            }

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
        }
    }

    private void fetchResults() {
        String parameters = "game_code=" + this.gameCode + "&nickname=" + this.nickname;

        JsonHttpRequest request = new JsonHttpRequest(resultEndPoint + parameters, new JsonHttpRequestCallback() {
            @Override
            public void onCompleted(JSONObject result) {
                parseResultData(result);
            }

            @Override
            public void onError(String message) {
                Log.e(this.getClass().getSimpleName(), message);
            }
        });

        request.execute();
    }

    private void parseResultData(JSONObject result) {
        if(result == null){
            Log.e(this.getClass().getSimpleName(), "Result JSON is null.");
            return;
        }

        try {
            int status = result.getInt("status");
            String statusMessage = result.getString("status_message");
            if(status != 200){
                Log.e(this.getClass().getSimpleName(), "HTTP result status indicated an error: " + statusMessage);
                return;
            }

            //decrypt JSON object
            JSONObject data = new JSONObject(result.getString("data"));
            if(data == null){
                Log.e(this.getClass().getSimpleName(), "HTTP result did not include data object");
                return;
            }

            boolean available = data.getBoolean("available");
            if(available){
                moveToResultActivity(Integer.valueOf(data.getString("total_points")));
            }else{
                new Handler().postDelayed(new ResultFetchLoop(), Constants.DATA_FETCH_DELAY);
            }

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
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
    public void onBackPressed() {
    }
}
