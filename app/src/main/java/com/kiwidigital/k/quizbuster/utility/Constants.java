package com.kiwidigital.k.quizbuster.utility;

/**
 * Created by brian on 18/10/2016.
 */


/**
 * Anything that will be reused. A String, an integer, a boolean. like a settings file.
 * 1. Save memory by not creating String all the time.
 * 2. Keep in uniform.
 * 3. Easier modification.
 */
public class Constants {

    public final static String FONT_FILE_NAME = "TimKid.ttf";
    public final static String HOST_NAME = "http://quizbuster.co.nf";

    public final static String CURRENT_GAME_CODE_KEY = "CURRENT_GAME_CODE_KEY";
    public final static String CURRENT_NICKNAME_KEY = "CURRENT_NICKNAME_KEY";
    public final static String LAST_QUESTION_KEY = "LAST_QUESTION_KEY";
    public final static String FETCHED_QUESTIONS_KEY = "FETCHED_QUESTIONS_KEY";
    public final static String BUST_POINTS_KEY = "BUST_POINTS_KEY";

    public static final long DATA_FETCH_DELAY = 5 * 1000;
    public static final long PAUSE_FETCH_DELAY = 1 * 1000;
}
