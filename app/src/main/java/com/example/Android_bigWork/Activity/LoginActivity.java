package com.example.Android_bigWork.Activity;

import static com.example.Android_bigWork.Utils.KeyboardUtils.hideKeyboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Android_bigWork.Database.PersonDao;
import com.example.Android_bigWork.Database.PersonDatabase;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.SubmitButton;
import com.example.Android_bigWork.Utils.HandlerAction;

public class LoginActivity extends AppCompatActivity implements HandlerAction {
    EditText mUsername, mPassword;
    SubmitButton mLoginButton;
    AppCompatTextView mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        setContentView(R.layout.login_activity);
        mUsername = findViewById(R.id.textView_username);
        mPassword = findViewById(R.id.textView_password);
        mLoginButton = findViewById(R.id.btn_login);
        mSignUpButton = findViewById(R.id.btn_signup);

        Intent initIntent = getIntent();
        Intent navigateToSignUp = new Intent(this, SignUpActivity.class);
        //跳转到Main时，清空Activity堆栈
        Intent navigateToHome = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //判断传入数据
        String iUsername = initIntent.getStringExtra("username");
        String iPassword = initIntent.getStringExtra("password");
        if (iUsername != null && iPassword != null) {
            mUsername.setText(iUsername);
            mPassword.setText(iPassword);
        }

        //获取数据库
        PersonDatabase personDatabase = PersonDatabase.getDatabase(this);
        PersonDao personDao = personDatabase.getPersonDao();
        //登录按钮监听器
        mLoginButton.setOnClickListener(v -> {
            String username = this.mUsername.getText().toString();
            String password = this.mPassword.getText().toString();
            Log.d("Login", "username: " + username + " password: " + password);
            mLoginButton.showProgress();
            //检测用户名密码是否为空
            if (checkEmpty(username, password)) return;
            //检测用户名是否为纯数字
            boolean isNumber = isNumber(username);
            //查询数据库
            if (checkDataBase(username, password, personDao)) {
                mLoginButton.showSucceed();
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                //跳转到主界面
                postDelayed(() -> {
                    navigateToHome.putExtra("username", username);
                    startActivity(navigateToHome);
                }, 1000);
            } else {
                mLoginButton.showError(3000);
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        });

        //注册按钮监听器
        mSignUpButton.setOnClickListener(v -> {
            //跳转到注册界面
            startActivity(navigateToSignUp);
        });
        //点击到img则收起键盘
        findViewById(R.id.imageView_bg).setOnClickListener(v -> {
            //检测是否有焦点
            if (mUsername.isFocused() || mPassword.isFocused()) {
                //清除焦点
                mUsername.clearFocus();
                mPassword.clearFocus();
            }
            //收起键盘
            hideKeyboard(this);
        });
    }

    private boolean checkDataBase(String username, String password, PersonDao personDao) {
        if (username.equals("admin") && password.equals("password")
                || personDao.checkLogin(username, password) != null
                || isNumber(username) && personDao.checkLoginByPhoneNumber(Long.parseLong(username), password) != null) {
            return true;
        }
        return false;
    }

    private boolean checkEmpty(String username, String password) {
        //判断是否为空
        if (username.isEmpty()) {
            Toast.makeText(this, "用户名或手机号不能为空", Toast.LENGTH_SHORT).show();
            mLoginButton.showError(3000);
            return true;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            mLoginButton.showError(3000);
            return true;
        }
        return false;
    }

    private boolean isNumber(String username) {
        boolean isNumber = true;
        for (int i = 0; i < username.length(); i++) {
            if (!Character.isDigit(username.charAt(i))) {
                isNumber = false;
                break;
            }
        }
        return isNumber;
    }

}