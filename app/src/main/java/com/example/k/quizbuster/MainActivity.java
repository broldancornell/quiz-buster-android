package com.example.k.quizbuster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.k.quizbuster.utility.JsonHttpRequest;
import com.example.k.quizbuster.utility.JsonHttpRequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private final String serverAddress = "http://quizbuster.co.nf//service/buster/validate.php?game_code=";

    private Typeface fontStyle;  // Typeface for Font style
    private EditText editTextGameCode; //game code
    private Button buttonSendGameCode;  // button for sending game code

    private ProgressDialog progressDialog;

    private String gameCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareWidgets();

    }

    private void prepareWidgets(){
        editTextGameCode = (EditText) findViewById(R.id.edit_text_game_code);
        buttonSendGameCode = (Button)   findViewById(R.id.btnSendGameCode);
        fontStyle = Typeface.createFromAsset(getAssets(), "TimKid.ttf");
        editTextGameCode.setTypeface(fontStyle);
        buttonSendGameCode.setTypeface(fontStyle);

        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Please wait");

    }

    public void enterGameCode(View view) {
        final String gameCode = editTextGameCode.getText().toString();

        if(gameCode.isEmpty()) {
            Toast.makeText(view.getContext(), "Please provide a game code", Toast.LENGTH_LONG).show();
            return;
        }

        this.gameCode = gameCode;
        this.progressDialog.show();

        JsonHttpRequest request = new JsonHttpRequest(serverAddress + gameCode, new JsonHttpRequestCallback() {
            @Override
            public void onCompleted(JSONObject data) {
                progressDialog.hide();
                processValidationResult(data);
            }

            @Override
            public void onError(String message) {
                progressDialog.hide();
                Log.e(this.getClass().getSimpleName(), message);
            }
        });

        request.execute();
    }

    private void processValidationResult(JSONObject result){
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
                moveOnToNickNameActivity();
            }else{
                Toast.makeText(this, "Game code is invalid", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
            return;
        }


    }

    private void moveOnToNickNameActivity(){

        Intent nicknameActivity = new Intent(this, NicknameActivity.class);

        nicknameActivity.putExtra("entered_quiz_code", gameCode);
        this.startActivity(nicknameActivity);
    }
}
