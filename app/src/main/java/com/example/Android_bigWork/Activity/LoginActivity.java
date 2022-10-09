package com.example.Android_bigWork.Activity;

import static com.example.Android_bigWork.Utils.KeyboardUtils.hideKeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Android_bigWork.Database.PersonDao;
import com.example.Android_bigWork.Database.PersonDatabase;
import com.example.Android_bigWork.R;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.login_activity);
        username = findViewById(R.id.textView_username);
        password = findViewById(R.id.textView_password);

        //获取数据库
        PersonDatabase personDatabase = PersonDatabase.getDatabase(this);
        PersonDao personDao = personDatabase.getPersonDao();

        //添加按钮监听器
        findViewById(R.id.btn_login).setOnClickListener(v -> {
            String username = this.username.getText().toString();
            String password = this.password.getText().toString();
            Log.d("Login", "username: " + username + " password: " + password);
            //判断是否为空
            if (username.isEmpty()) {
                Toast.makeText(this, "用户名或手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (password.isEmpty()) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            //检测用户名是否为纯数字
            boolean isNumber = true;
            for (int i = 0; i < username.length(); i++) {
                if (!Character.isDigit(username.charAt(i))) {
                    isNumber = false;
                    break;
                }
            }
            //查询数据库
            if (username.equals("admin") && password.equals("password")
                    || personDao.checkLogin(username, password) != null
                    || isNumber && personDao.checkLoginByPhoneNumber(Integer.parseInt(username), password) != null) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        });
        //点击到img则收起键盘
        findViewById(R.id.imageView_bg).setOnClickListener(v -> {
            //检测是否有焦点
            if (username.isFocused() || password.isFocused()) {
                //清除焦点
                username.clearFocus();
                password.clearFocus();
            }
            //收起键盘
            hideKeyboard(this);
        });

    }


}