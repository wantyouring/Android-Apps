package com.example.imageslider;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    Adapter adapter;
    ViewPager viewPager;
    EditText editText;
    Button button;

    private int display_index;

    public int getDisplay_index() {
        return display_index;
    }
    private void setDisplay_index(int i) {
        display_index = i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        editText : 제목
        viewPager : viewpager
        button : 저장
         */
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);

        adapter = new Adapter(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                String str = adapter.getCaption(i);
                editText.setText(str);
                setDisplay_index(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int display_i = getDisplay_index() + 1;
                Bitmap bm = null;
                if (display_i == 1){
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
                } else if (display_i == 2){
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
                } else if (display_i == 3){
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.img3);
                }


                try{
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "imageslider";
                    File outputDir= new File(path);
                    outputDir.mkdirs();
                    File newFile = new File(path + File.separator + editText.getText()+".png");
                    FileOutputStream fos = new FileOutputStream(newFile);
                    //FileOutputStream fos = openFileOutput("test.png" , MODE_WORLD_READABLE);
                    bm.compress(Bitmap.CompressFormat.PNG, 100 , fos);
                    fos.flush();
                    fos.close();
                    Toast.makeText(getApplicationContext(), editText.getText()+" 파일 생성 완료", Toast.LENGTH_SHORT).show();
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+path+"/"+ editText.getText()+".png")));
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 저장 에러", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
