package com.kiwidigital.k.quizbuster.dao;

import android.util.Log;

import com.kiwidigital.k.quizbuster.utility.AnswerCallback;
import com.kiwidigital.k.quizbuster.utility.Constants;
import com.kiwidigital.k.quizbuster.utility.CurrentQuestionFetchCallback;
import com.kiwidigital.k.quizbuster.utility.JsonHttpRequest;
import com.kiwidigital.k.quizbuster.utility.JsonHttpRequestCallback;
import com.kiwidigital.k.quizbuster.utility.QuestionFetchCallback;
import com.kiwidigital.k.quizbuster.utility.ResultFetchCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brian on 02/11/2016.
 */

public class QuizDao {

    private static QuizDao instance;

    private QuizDao(){}

    public static QuizDao getInstance(){
        if(instance == null)
            instance = new QuizDao();
        return instance;
    }

    private final String questionsEndPoint = Constants.HOST_NAME + "/service/buster/questions.php?game_code=";
    private final String currentQuestionEndPoint = Constants.HOST_NAME + "/service/buster/current.php?game_code=";
    private final String resultEndPoint = Constants.HOST_NAME + "/service/buster/results.php?";
    private final String answerEndPoint = Constants.HOST_NAME + "/service/buster/answer.php?";

    public void getQuestions(String gameCode, final QuestionFetchCallback callback){
        JsonHttpRequest request = new JsonHttpRequest(questionsEndPoint + gameCode, new JsonHttpRequestCallback() {

            @Override
            public void onCompleted(JSONObject data) {
                parseQuizzes(data, callback);
            }

            @Override
            public void onError(String message) {
                Log.e(this.getClass().getSimpleName(), message);
            }
        });
        request.execute();
    }

    public void getNextQuestion(String gameCode, final int lastQuestion, final CurrentQuestionFetchCallback callback){
        JsonHttpRequest request = new JsonHttpRequest(currentQuestionEndPoint + gameCode, new JsonHttpRequestCallback() {

            @Override
            public void onCompleted(JSONObject data) {
                parseCurrentQuestion(data, lastQuestion, callback);
            }

            @Override
            public void onError(String message) {
                Log.e(this.getClass().getSimpleName(), message);
            }
        });
        request.execute();
    }

    public void getResults(String gameCode, String nickname, final ResultFetchCallback callback){
        String parameters = "game_code=" + gameCode + "&nickname=" + nickname;

        JsonHttpRequest request = new JsonHttpRequest(resultEndPoint + parameters, new JsonHttpRequestCallback() {
            @Override
            public void onCompleted(JSONObject result) {
                parseResultData(result, callback);
            }

            @Override
            public void onError(String message) {
                Log.e(this.getClass().getSimpleName(), message);
            }
        });
        request.execute();
    }

    public void sendAnswer(String gameCode, String nickname, int questionNumber, int answerNumber, int timeLeft, final AnswerCallback callback){
        String parameters = "game_code=" + gameCode + "&nickname=" + nickname + "&question_number=" + questionNumber + "&answer=" + answerNumber + "&time_left=" + timeLeft;

        JsonHttpRequest request = new JsonHttpRequest(answerEndPoint + parameters, new JsonHttpRequestCallback() {
            @Override
            public void onCompleted(JSONObject data) {
                callback.onValid();
            }

            @Override
            public void onError(String message) {
                callback.onInvalid();
            }
        });

        request.execute();
    }

    private void parseCurrentQuestion(JSONObject result, int lastQuestion, CurrentQuestionFetchCallback callback) {

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
                callback.onValid(currentQuestion);
            }else{
                callback.onInvalid();
            }

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
        }

    }

    private void parseQuizzes(JSONObject result, QuestionFetchCallback callback) {
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

            callback.onValid(data);

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
        }
    }

    private void parseResultData(JSONObject result, ResultFetchCallback callback) {
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
                callback.onValid(Integer.valueOf(data.getString("total_points")));
            }else{
                callback.onInvalid();
            }

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
        }
    }

}
