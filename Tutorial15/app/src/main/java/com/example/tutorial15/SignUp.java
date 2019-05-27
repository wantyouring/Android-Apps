package com.example.tutorial15;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextAge;
    private RadioGroup radioGroupGender;

    private String email = "";
    private String password = "";
    private String name = "";
    private String age;
    private String gender = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        editTextEmail = (EditText)findViewById(R.id.et_email);
        editTextPassword = (EditText)findViewById(R.id.et_password);
        editTextName = (EditText)findViewById(R.id.et_name);
        editTextAge = (EditText)findViewById(R.id.et_age);
        radioGroupGender = (RadioGroup) findViewById(R.id.radioGroup);

        Intent getintent = getIntent();
        email = getintent.getExtras().getString("email");
        password = getintent.getExtras().getString("password");
        editTextEmail.setText(email);
        editTextPassword.setText(password);

    }

    //회원가입 누를 시 해당 정보들 조건 만족시 main activity로 result넘기기.
    public void singUp(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        name = editTextName.getText().toString();
        age = editTextAge.getText().toString(); //우선 string으로 넘겨주고 main에서 int로
        int id = radioGroupGender.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton)findViewById(id);
        gender = radioButton.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        intent.putExtra("name",name);
        intent.putExtra("age",age);
        intent.putExtra("gender",gender);
        setResult(RESULT_OK,intent);
        finish();
    }
}
