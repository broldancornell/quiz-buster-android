package com.example.k.quizbuster;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.k.quizbuster.utility.Constants;

public class NicknameActivity extends AppCompatActivity {

    private TextView textViewQuizCode;
    private EditText editTextNickname;
    private Button buttonEnter, buttonGoBack;
    private String quizCode;

    private Typeface fontStyle;

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
        textViewQuizCode = (TextView) findViewById(R.id.text_view_quiz_code);
        editTextNickname = (EditText) findViewById(R.id.edit_text_nickname);
        buttonEnter = (Button) findViewById(R.id.button_enter);
        buttonGoBack = (Button) findViewById(R.id.button_go_back);

        fontStyle = Typeface.createFromAsset(getAssets(), Constants.FONT_FILE_NAME);
        textViewQuizCode.setTypeface(fontStyle);
        editTextNickname.setTypeface(fontStyle);
        buttonEnter.setTypeface(fontStyle);
        buttonGoBack.setTypeface(fontStyle);

        textViewQuizCode.setText(quizCode);
    }

    public void onNicknameEnterClicked(View v) {
        Intent questionActivity = new Intent(this, QuestionActivity.class);

        String saveNickname = editTextNickname.getText().toString();  //convert entered quiz code into string type
        questionActivity.putExtra("userNickname", saveNickname);  //pass the value of variable quizCode into NicknameActivity
        this.startActivity(questionActivity);
    }

    public void onGoBackClicked(View v) {
        Intent mainActivity = new Intent(this, MainActivity.class);
        this.startActivity(mainActivity);
    }
}
