package com.kiwidigital.k.quizbuster.utility;

/**
 * Created by brian on 18/10/2016.
 */

public interface ResultFetchCallback {

    void onValid(int result);
    void onInvalid();

}
