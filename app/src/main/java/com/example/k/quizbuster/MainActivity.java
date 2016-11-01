package com.example.k.quizbuster;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.k.quizbuster.utility.ConnectionManager;
import com.example.k.quizbuster.utility.Constants;
import com.example.k.quizbuster.utility.ValidationCallback;

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

        prepareInitialValues();

        prepareWidgets();

    }

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

        GameDao.getInstance().validateGameCode(url, new ValidationCallback() {
            @Override
            public void onValid() {
                moveOnToNicknameActivity();
            }

            @Override
            public void onInvalid(String message) {
                Toast.makeText(MainActivity.this, "Game code is invalid", Toast.LENGTH_LONG).show();
            }
        }, this.progressDialog);
    }

    private void moveOnToNicknameActivity(){

        Intent nicknameActivity = new Intent(this, NicknameActivity.class);

        nicknameActivity.putExtra("entered_quiz_code", gameCode);
        this.startActivity(nicknameActivity);
    }
}
