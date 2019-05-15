package com.example.tutorial12;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        TextView user_name = (TextView)findViewById(R.id.user_name);
        user_name.setText(getIntent().getStringExtra("login_id") + "님");
    }
}
