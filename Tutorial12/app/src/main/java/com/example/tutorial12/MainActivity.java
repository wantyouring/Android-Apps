package com.example.tutorial12;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.login_button);
        final EditText loginText = (EditText)findViewById(R.id.id);
        final EditText passwordText = (EditText)findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordText.getText().toString().equals("1111")) {
                    Toast.makeText(MainActivity.this, "비밀번호 오류", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(v.getContext(),NextActivity.class);
                String login_id = loginText.getText().toString();
                intent.putExtra("login_id",login_id);
                startActivity(intent);
            }
        });



    }
}
