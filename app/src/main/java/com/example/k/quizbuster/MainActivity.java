package com.example.k.quizbuster;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.k.quizbuster.utility.ConnectionManager;
import com.example.k.quizbuster.utility.Constants;
import com.example.k.quizbuster.utility.JsonHttpRequest;
import com.example.k.quizbuster.utility.JsonHttpRequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String validationEndPoint = Constants.HOST_NAME + "/service/buster/validate.php?game_code=";

    private Typeface fontStyle;  // Typeface for Font style
    private EditText editTextGameCode; //game code
    private Button buttonSendGameCode;  // button for sending game code

    private ProgressDialog progressDialog;

    private String gameCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //check internet use  begin
        if(!isConnectingToInternet(MainActivity.this))
        {
            Toast.makeText(getApplicationContext(),"Internet is not connected",Toast.LENGTH_SHORT).show();
        }

        prepareInitialValues();

        prepareWidgets();

    }

    //check internet use  begin
    public static boolean isConnectingToInternet(Context context)
    {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }
    //check internet use  end




    private void prepareInitialValues(){
        SharedPreferences sharedPref = super.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.CURRENT_GAME_CODE_KEY, null);
        editor.putString(Constants.CURRENT_NICKNAME_KEY, null);
        editor.putInt(Constants.LAST_QUESTION_KEY, -1);
        editor.putBoolean(Constants.FETCHED_QUESTIONS_KEY, false);
        editor.apply();
    }

    private void prepareWidgets(){
        //get objects from layout
        editTextGameCode = (EditText) findViewById(R.id.edit_text_game_code);
        buttonSendGameCode = (Button)   findViewById(R.id.button_send_game_code);

        //changed font style
        fontStyle = Typeface.createFromAsset(getAssets(), Constants.FONT_FILE_NAME);

        editTextGameCode.setTypeface(fontStyle);
        buttonSendGameCode.setTypeface(fontStyle);

        //create progress dialog
        this.progressDialog = new ProgressDialog(this);
        //set the text
        this.progressDialog.setMessage("Please wait");

    }

    public void enterGameCode(View view) {
        if(!ConnectionManager.isConnectedToTheInternet(view.getContext())){
            ConnectionManager.showConnectionAlert(view.getContext());
            return;
        }
        //get the text from the edit text widget
        final String gameCode = editTextGameCode.getText().toString();

        //validate input by checking if it is empty.
        if(gameCode.isEmpty()) {
            //show the error to the user then exit
            Toast.makeText(view.getContext(), "Please provide a game code", Toast.LENGTH_LONG).show();
            return;
        }

        //save the game code as a local variable
        this.gameCode = gameCode;
        //show the progress dialog to 1. let the user know that it is processing. 2. Prevent further action from the user.
        this.progressDialog.show();

        String url = validationEndPoint + gameCode;

        //prepare HTTP request to validate the game code through the server
        JsonHttpRequest request = new JsonHttpRequest(url, new JsonHttpRequestCallback() {
            @Override
            public void onCompleted(JSONObject data) {
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
            progressDialog.hide();
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
                moveOnToNicknameActivity();
            }else{
                Toast.makeText(this, "Game code is invalid", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
        }finally {
            //after process data
            progressDialog.hide();

        }

    }

    private void moveOnToNicknameActivity(){

        Intent nicknameActivity = new Intent(this, NicknameActivity.class);

        nicknameActivity.putExtra("entered_quiz_code", gameCode);
        this.startActivity(nicknameActivity);
    }


}
