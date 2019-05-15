package com.example.tutorial12;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        final TextView user_name = (TextView)findViewById(R.id.user_name);
        user_name.setText(getIntent().getStringExtra("login_id") + "님");

        Button exitButton = (Button)findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.putExtra("logout_id",user_name.getText().toString());
                setResult(10,intent);
                finish();
            }
        });
    }
}
