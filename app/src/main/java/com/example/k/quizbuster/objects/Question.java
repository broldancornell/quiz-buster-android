package com.example.k.quizbuster.objects;

/**
 * Created by brian on 19/10/2016.
 */

public class Question{

    private int questionNumber;
    private String questionText;
    private int secondsToAnswer;
    private int numberOfOptions;

    public Question(int questionNumber, String questionText, int secondsToAnswer, int numberOfOptions) {
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.secondsToAnswer = secondsToAnswer;
        this.numberOfOptions = numberOfOptions;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getSecondsToAnswer() {
        return secondsToAnswer;
    }

    public void setSecondsToAnswer(int secondsToAnswer) {
        this.secondsToAnswer = secondsToAnswer;
    }

    public int getNumberOfOptions() {
        return numberOfOptions;
    }

    public void setNumberOfOptions(int numberOfOptions) {
        this.numberOfOptions = numberOfOptions;
    }
}
