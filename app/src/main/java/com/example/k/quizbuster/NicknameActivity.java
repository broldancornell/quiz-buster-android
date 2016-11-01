package com.example.k.quizbuster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.quizbuster.utility.ConnectionManager;
import com.example.k.quizbuster.utility.Constants;
import com.example.k.quizbuster.utility.ValidationCallback;

public class NicknameActivity extends AppCompatActivity {

    private EditText editTextNickname;
    private String quizCode;
    private String nickname;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        getActivityArguments();
        prepareWidgets();
    }

    private void getActivityArguments(){
        quizCode = this.getIntent().getExtras().getString("entered_quiz_code");
    }

    private void prepareWidgets(){
        TextView textViewQuizCode = (TextView) findViewById(R.id.text_view_quiz_code);
        editTextNickname = (EditText) findViewById(R.id.edit_text_nickname);
        Button buttonEnter = (Button) findViewById(R.id.button_enter);
        Button buttonGoBack = (Button) findViewById(R.id.button_go_back);

        Typeface fontStyle = Typeface.createFromAsset(getAssets(), Constants.FONT_FILE_NAME);
        textViewQuizCode.setTypeface(fontStyle);
        editTextNickname.setTypeface(fontStyle);
        buttonEnter.setTypeface(fontStyle);
        buttonGoBack.setTypeface(fontStyle);

        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Please wait");
        this.progressDialog.setCancelable(false);

        textViewQuizCode.setText(quizCode);

        buttonGoBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(ConnectionManager.isConnectedToTheInternet(v.getContext())){
                    onGoBackClicked(v);
                }else{
                    ConnectionManager.showConnectionAlert(v.getContext());
                }
                return true;
            }
        });
    }

    public void onNicknameEnterClicked(View view) {
        if(!ConnectionManager.isConnectedToTheInternet(view.getContext())){
            ConnectionManager.showConnectionAlert(view.getContext());
            return;
        }

        String nickname = editTextNickname.getText().toString();  //convert entered quiz code into string type

        if(nickname.isEmpty()) {
            Toast.makeText(view.getContext(), "Please provide a nickname", Toast.LENGTH_LONG).show();
            return;
        }

        this.nickname = nickname;

        this.progressDialog.show();

        GameDao.getInstance().joinGame(quizCode, nickname, new ValidationCallback() {
            @Override
            public void onValid() {
                moveToWaitingActivity();
            }

            @Override
            public void onInvalid(String message) {
                if(message == null || message.trim().isEmpty()){
                    message = "The nickname is invalid";
                }
                Toast.makeText(NicknameActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }, progressDialog);

    }

    public void onGoBackClicked(View view) {
        moveToMainActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveToMainActivity();
    }

    private void moveToMainActivity(){
        Intent mainActivity = new Intent(this, MainActivity.class);
        this.startActivity(mainActivity);
    }

    private void moveToWaitingActivity(){

        Intent loadActivity = new Intent(this, LoadActivity.class);
        //prepare quiz values
        loadActivity.putExtra(Constants.CURRENT_GAME_CODE_KEY, this.quizCode);
        loadActivity.putExtra(Constants.CURRENT_NICKNAME_KEY, this.nickname);
        loadActivity.putExtra(Constants.LAST_QUESTION_KEY, 0);
        loadActivity.putExtra(Constants.FETCHED_QUESTIONS_KEY, false);

        this.startActivity(loadActivity);
    }
}
