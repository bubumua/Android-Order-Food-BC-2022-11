package com.example.Android_bigWork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    //获取数据库
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_login);
        username = findViewById(R.id.textView_username);
        password = findViewById(R.id.textView_password);
        //获取数据库
        PersonDatabase personDatabase = PersonDatabase.getDatabase(context);
        PersonDao personDao = personDatabase.getPersonDao();

        //添加按钮监听器
        findViewById(R.id.btn_login).setOnClickListener(v -> {
            String username = this.username.getText().toString();
            String password = this.password.getText().toString();
            //判断是否为空
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            //查询数据库
            if (username.equals("admin") && password.equals("password") || personDao.checkLogin(username, password) != null) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });

    }


}