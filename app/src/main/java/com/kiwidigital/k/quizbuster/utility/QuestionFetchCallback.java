package com.kiwidigital.k.quizbuster.utility;

import org.json.JSONObject;

/**
 * Created by brian on 18/10/2016.
 */

public interface QuestionFetchCallback {

    void onValid(JSONObject object);
    void onInvalid(String message);

}
