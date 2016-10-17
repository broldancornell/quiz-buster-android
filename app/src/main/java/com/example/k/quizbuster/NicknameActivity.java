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
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.quizbuster.utility.Constants;
import com.example.k.quizbuster.utility.JsonHttpRequest;
import com.example.k.quizbuster.utility.JsonHttpRequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class NicknameActivity extends AppCompatActivity {

    private EditText editTextNickname;
    private String quizCode;
    private String nickname;

    private final String joinEndPoint = Constants.HOST_NAME + "/service/buster/join.php?";

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

        textViewQuizCode.setText(quizCode);
    }

    public void onNicknameEnterClicked(View view) {
        String nickname = editTextNickname.getText().toString();  //convert entered quiz code into string type

        if(nickname.isEmpty()) {
            Toast.makeText(view.getContext(), "Please provide a nickname", Toast.LENGTH_LONG).show();
            return;
        }

        this.nickname = nickname;
        this.progressDialog.show();

        String parameters = "game_code=" + this.quizCode + "&nickname=" + this.nickname;

        JsonHttpRequest request = new JsonHttpRequest(joinEndPoint + parameters, new JsonHttpRequestCallback() {
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
            progressDialog.hide();
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
                moveToWaitingActivity();
            }else{
                String message = data.getString("message");
                if(message == null || message.trim().isEmpty()){
                    message = "The nickname is invalid";
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException exception) {
            Log.e(this.getClass().getSimpleName(), "JSON exception encountered", exception);
        }finally {

            progressDialog.hide();

        }

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
        Intent questionActivity = new Intent(this, QuestionActivity.class);

        questionActivity.putExtra("entered_nickname", this.nickname);  //pass the value of variable quizCode into NicknameActivity
        questionActivity.putExtra("entered_quiz_code", this.quizCode);  //pass the value of variable quizCode into NicknameActivity
        this.startActivity(questionActivity);
    }
}
