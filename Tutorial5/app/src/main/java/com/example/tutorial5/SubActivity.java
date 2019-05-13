package com.example.tutorial5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_sub);

        TextView nameText = (TextView)findViewById(R.id.nameText); //TextView id로 객체 생성
        Intent intent = getIntent(); //MainActivity에서 보낸 intent받기.
        nameText.setText(intent.getStringExtra("nameText").toString());
    }
}
