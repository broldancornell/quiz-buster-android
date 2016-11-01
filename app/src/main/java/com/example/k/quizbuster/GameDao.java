package com.example.k.quizbuster;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.k.quizbuster.utility.Constants;
import com.example.k.quizbuster.utility.JsonHttpRequest;
import com.example.k.quizbuster.utility.JsonHttpRequestCallback;
import com.example.k.quizbuster.utility.ValidationCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brian on 01/11/2016.
 */

public class GameDao {
    private final String validationEndPoint = Constants.HOST_NAME + "/service/buster/validate.php?game_code=";
    private final String joinEndPoint = Constants.HOST_NAME + "/service/buster/join.php?";

    private static GameDao instance;

    private GameDao(){}

    public static GameDao getInstance(){
        if(instance == null)
            instance = new GameDao();
        return instance;
    }

    public void validateGameCode(String gameCode, final ValidationCallback callback, final ProgressDialog progressDialog){

        String url = validationEndPoint + gameCode;

        new JsonHttpRequest(url, new JsonHttpRequestCallback() {
            @Override
            public void onCompleted(JSONObject data) {
                processValidationResult(data, callback);
                progressDialog.hide();
            }

            @Override
            public void onError(String message) {
                progressDialog.hide();
            }
        }).execute();
    }

    public void joinGame(String gameCode, String nickname, final ValidationCallback callback, final ProgressDialog progressDialog){
        String parameters = "game_code=" + gameCode + "&nickname=" + nickname;
        String url = joinEndPoint + parameters;

        JsonHttpRequest request = new JsonHttpRequest(url, new JsonHttpRequestCallback() {
            @Override
            public void onCompleted(JSONObject data) {
                processJoinResult(data, callback);
                progressDialog.hide();
            }

            @Override
            public void onError(String message) {
                progressDialog.hide();
            }
        });

        request.execute();
    }

    private void processValidationResult(JSONObject result, ValidationCallback callback){
        if(result == null){
            Log.e(this.getClass().getSimpleName(), "Result JSON is null.");
            return;
        }

        try {
            int status = result.getInt("status");
            String statusMessage = result.getString("status_message");
            // 200 = success standard HTTP code
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
                callback.onValid();
            }else{
                callback.onInvalid("");
            }

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
        }

    }

    private void processJoinResult(JSONObject result, ValidationCallback callback) {

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

            boolean success = data.getBoolean("success");

            if(success){
                callback.onValid();
            }else{
                callback.onInvalid(data.getString("message"));
            }

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
        }

    }
}
