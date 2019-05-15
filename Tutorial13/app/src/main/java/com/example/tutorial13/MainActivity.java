package com.example.tutorial13;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 처음 앱 실행했을 시 권한 요청하는 부분
        requirePermission();

        Button button = (Button)findViewById(R.id.camera_button);
        //사진찍기 클릭 시 해당 권한 확인
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean camera = ContextCompat.checkSelfPermission(v.getContext(),Manifest.permission.CAMERA)
                                    == PackageManager.PERMISSION_GRANTED;
                boolean write = ContextCompat.checkSelfPermission(v.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED;
                if(camera && write) {
                    //사진찍는 인텐트 코드 넣기
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,0);
                } else {
                    Toast.makeText(MainActivity.this, "카메라 권한 및 쓰기 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void requirePermission() {
        String[] permissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ArrayList<String> listPermissionNeeded = new ArrayList<>();

        for(String permission : permissions) {
            // 권한이 허가 안됐을 경우 요청할 권한 모집.
            if(ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_DENIED) {
                listPermissionNeeded.add(permission);
            }
        }
        //Toast.makeText(this,listPermissionNeeded.size()+"",Toast.LENGTH_SHORT).show();

        if(!listPermissionNeeded.isEmpty()){
            //권한 요청하는 부분
            ActivityCompat.requestPermissions(this,listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),1);
        }
    }
}
