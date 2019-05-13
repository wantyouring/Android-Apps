package com.example.tutorial4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //activity_main.xml로 현재 activity 화면 구성.

        String[] items = {"apple", "tomato", "hahaha"};
        ListView listView = (ListView)findViewById(R.id.listView); //id로 listview객체 가져오기
        //사용자 정의 adapter 사용.
        ListAdapter adapter = new ImageAdapter(this,items); //현재 activity와 string list를 인자로 adapter객체 생성.
        //안드로이드 기본 adapter 사용.(글자만 listview출력)
        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, //액티비티 인스턴스. (=MainActivity.this)
                android.R.layout.simple_list_item_1, //안드로이드 기본 제공 양식. 사용자가 직접 만들 수도 있음.
                items);
                */

        listView.setAdapter(adapter); //만든 adapter객체 listview로 전달.(string list -> string adapter -> listview)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(MainActivity.this,selected_item,Toast.LENGTH_SHORT).show();
                //여기서는 24line과 다르게 MainActivity.this로 쓴 이유 : Inner class이므로 그냥 this가 MainActivity를 가르키지 않음!
            }
        });
    }
}
