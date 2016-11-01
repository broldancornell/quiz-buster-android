package com.example.k.quizbuster.utility;

import org.json.JSONObject;

/**
 * Created by brian on 18/10/2016.
 */

public interface ValidationCallback {

    void onValid();
    void onInvalid(String message);

}
