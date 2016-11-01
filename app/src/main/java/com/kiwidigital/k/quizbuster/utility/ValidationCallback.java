package com.kiwidigital.k.quizbuster.utility;

/**
 * Created by brian on 18/10/2016.
 */

public interface ValidationCallback {

    void onValid();
    void onInvalid(String message);

}
