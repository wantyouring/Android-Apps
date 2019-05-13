package com.example.tutorial5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText nameText = (EditText)findViewById(R.id.nameText);
        Button nameButton = (Button)findViewById(R.id.nameButton);

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString(); //EditText객체인 nameText에서 string정보 가져오기.
                Intent intent = new Intent(getApplicationContext(), SubActivity.class); //intent 인스턴스 생성
                intent.putExtra("nameText",name); //name스트링변수 전달
                startActivity(intent); //intent속성들에 따라 현재 context에서 SubActivity 액티비티로 전환. 그리고 string변수 전달작업.
            }
        });


    }
}
