package com.example.k.quizbuster.utility;

/**
 * Created by brian on 18/10/2016.
 */

public interface CurrentQuestionFetchCallback {

    void onValid(int nextQuestion);
    void onInvalid();

}
