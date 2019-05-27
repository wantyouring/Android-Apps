package com.example.tutorial15;

import android.Manifest;
import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_SIGN_UP = 100;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";

    private DatabaseReference databaseReference =
            FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requirePermission();//시작할 때 필요한 권한 요청

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.et_email);
        editTextPassword = findViewById(R.id.et_password);

        if (ActivityCompat.checkSelfPermission(this, permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "번호 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        editTextEmail.setText(getPhoneNumber(MainActivity.this));
    }

    //유저 class
    public class User {
        public int age;
        public String gender;
        public String name;

        public User() {

        }

        public User(int age, String gender, String name) {
            this.age = age;
            this.gender = gender;
            this.name = name;
        }
    }

    // 새로운 user 추가
    private void writeNewUser(String userid, int age, String gender, String name) {
        User user = new User(age, gender, name);
        databaseReference.child("user_id").child(userid).setValue(user); // 지정 id 하위노드 포함 모두 덮어쓰기
        //@@@@@@@@@@추가수정
    }

    //핸드폰 번호 가져오기
    @RequiresPermission(permission.READ_PHONE_STATE)
    public static String getPhoneNumber(Context context){

        String phoneNumber = "";

        TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String tmpPhoneNumber = mgr.getLine1Number();
            phoneNumber = tmpPhoneNumber.replace("+82", "0");
        } catch (Exception e) {
            phoneNumber = "";
        }
        return phoneNumber;
    }

    //필요한 permission 허가
    void requirePermission() {
        String[] permissions = new String[] {permission.READ_PHONE_STATE, permission.INTERNET};//필요한 권한 여기에 추가
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

    public void singUp(View view) {
        Intent intent = new Intent(getApplicationContext(),SignUp.class);
        intent.putExtra("email",editTextEmail.getText().toString());
        intent.putExtra("password",editTextPassword.getText().toString());
        startActivityForResult(intent,REQUEST_SIGN_UP);
        //startActivity(intent);

        /*
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            createUser(email, password);
        } else {
            Toast.makeText(this, "이메일,패스워드 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) {
            Toast.makeText(this, "오류 발생", Toast.LENGTH_SHORT).show();
            return;
        }
        if(requestCode == REQUEST_SIGN_UP) {
            String rec_email = data.getStringExtra("email");
            String rec_password = data.getStringExtra("password");
            String rec_name = data.getStringExtra("name");
            String rec_age = data.getStringExtra("age");
            String rec_gender = data.getStringExtra("gender");

            Toast.makeText(this, "테스트\n"+rec_email+rec_password
                    +rec_name+rec_age+rec_gender, Toast.LENGTH_SHORT).show();
        }
    }

    public void signIn(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
        } else {
            Toast.makeText(this, "이메일,패스워드 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 회원가입
    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공. database에 회원정보 추가하기



                            Toast.makeText(MainActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(MainActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 로그인
    private void loginUser(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}