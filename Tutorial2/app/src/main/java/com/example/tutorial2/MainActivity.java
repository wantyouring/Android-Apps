package com.example.tutorial2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String[] items = {"apple","tomato","mango"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button listButton = (Button) findViewById(R.id.listButton); //button 객체 등록
        listButton.setOnClickListener(new View.OnClickListener() { //xxx.onClickListener : 해당 xxx interface의 리스너 생성. 여기서 view는 현재 화면정보
            @Override
            public void onClick(View v) {   //button 눌렸을 시. 여기서 view는 클릭 당시의 화면 정보를 받음.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);   //AlertDialog의 Builder객체 생성.
                // 여기부터 알림창 속성 설정
                builder.setTitle("list");
                builder.setItems(items, new DialogInterface.OnClickListener() { //목록의 아이템 클릭 시 설정
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //아이템 클릭 시
                        Toast.makeText(getApplicationContext(), items[which] + "입니다", Toast.LENGTH_SHORT).show(); // toast 창 팝업.
                    }
                });
                AlertDialog alertDialog = builder.create(); //Builder객체로 alertDialog객체 생성.
                alertDialog.show();
            }
        });

        Button exitButton = (Button)findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {  //현재화면View에서 새로운 listener만들기
            @Override
            public void onClick(View v) {
                //전체 과정 : AlertDialog의 Builder객체 생성 후 AlertDialog객체에 create()로 정보들 넘겨주기.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //main에 종속되는 builder객체생성
                // builder객체의 속성들 설정
                builder.setMessage("really exit?")
                        .setTitle("exit")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() { //dialog화면에서의 리스너.
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //종료
                                //그냥 finish()해도됨
                                finishAffinity();
                                System.runFinalization();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //종료 안할거임
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
