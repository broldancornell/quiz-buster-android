package com.example.k.quizbuster.objects;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by brian on 19/10/2016.
 */

public class QuestionsHandler {

    private static QuestionsHandler instance;

    public static QuestionsHandler getInstance(){
        if(instance == null)
            instance = new QuestionsHandler();

        return instance;
    }

    private QuestionsHandler(){
        questions = new ArrayList<>();
    }

    private ArrayList<Question> questions;

    public void parseQuestions(JSONArray array){

        int numberOfQuestions = array.length();
        if(numberOfQuestions < 1){
            return;
        }

        for(int questionIndex = 0; questionIndex < numberOfQuestions; questionIndex++){
            try {
                JSONObject questionJson = array.getJSONObject(questionIndex);

                this.questions.add(new Question(questionJson.getInt("question_number"), questionJson.getString("question_text"), questionJson.getInt("time_limit"), questionJson.getInt("options")));

            } catch (JSONException exception) {
                Log.e(super.getClass().getSimpleName(), "An exception occurred while trying to parse question data.", exception);
            }
        }

    }

    public Question getQuestion(int questionNumber){
        Question questionValue = null;

        for(Question question : this.questions){
            if(question.getQuestionNumber() == questionNumber){
                questionValue = question;
            }
        }

        if(questionValue != null){
            this.questions.remove(questionValue);
        }

        return questionValue;
    }

    public int getQuestionsCount(){
        return this.questions.size();
    }

    public void clearQuestions(){
        this.questions.clear();
    }

}
