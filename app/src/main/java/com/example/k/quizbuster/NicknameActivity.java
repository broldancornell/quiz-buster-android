package com.example.k.quizbuster;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NicknameActivity extends AppCompatActivity {

    TextView txtFixedQuizCode;
    EditText txtNickname;
    Button   btnEnter;
    Button   btnGoBack;
    String   quizcode;
    String   saveNickname;

    Typeface fontStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        txtFixedQuizCode = (TextView) findViewById(R.id.txtFixedQuizCode);
        txtNickname      = (EditText) findViewById(R.id.txtNickname);
        btnEnter         = (Button)   findViewById(R.id.btnEnter);
        btnGoBack        = (Button)   findViewById(R.id.btnGoBack);

        fontStyle = Typeface.createFromAsset(getAssets(), "TimKid.ttf");
        txtFixedQuizCode.setTypeface(fontStyle);
        txtNickname.setTypeface(fontStyle);
        btnEnter.setTypeface(fontStyle);
        btnGoBack.setTypeface(fontStyle);


        quizcode = this.getIntent().getExtras().getString("entered_quiz_code");

        txtFixedQuizCode.setText(quizcode);
    }

    public void onNicknameEnterClicked(View v) {
        Intent questionActivity = new Intent(this, QuestionActivity.class);

        saveNickname = txtNickname.getText().toString();  //convert entered quiz code into string type

        /*
        saveNickname을 DB로 보내서 저장한다.

        서버컴퓨터에서는 MySQL에 닉네임을 저장하면서 동시에 모니터에 보여준다.

        DB 입장에서는 닉네임에 맞는 테이블들을 준비한다.

         */
        Toast.makeText(this,"Your Nickname : " + saveNickname, Toast.LENGTH_SHORT).show();
        questionActivity.putExtra("userNickname", saveNickname);  //pass the value of variable quizcode into NicknameActivity
        this.startActivity(questionActivity);
    }

    public void onGoBackClicked(View v) {
        Intent mainActivity = new Intent(this, MainActivity.class);
        this.startActivity(mainActivity);
    }
}
