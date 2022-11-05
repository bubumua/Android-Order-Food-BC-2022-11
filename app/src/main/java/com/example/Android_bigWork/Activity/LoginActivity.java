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
import com.example.Android_bigWork.Entity.Person;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.SubmitButton;
import com.example.Android_bigWork.action.HandlerAction;
import com.hjq.xtoast.XToast;

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
        //判断是否有传入的Bundle数据
        if (initIntent.getExtras() != null) {
            //获取Bundle数据
            Bundle bundle = initIntent.getExtras();
            //获取Bundle中的数据
            Person user = (Person) bundle.getSerializable("user");
            //判断是否有传入的用户数据
            if (user != null) {
                //将用户数据显示在界面上
                mUsername.setText(user.username);
                mPassword.setText(user.password);
            }
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
//                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                //跳转到主界面
                postDelayed(() -> {
                    //查询该用户
                    Person user = personDao.queryPerson(username);
                    //将用户数据传入Bundle
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    //将Bundle数据传入Intent
                    navigateToHome.putExtras(bundle);
                    startActivity(navigateToHome);
                }, 1000);
            } else {
                mLoginButton.showError(3000);
                new XToast<>(this)
                        .setContentView(R.layout.window_hint)
                        .setDuration(1000)
                        .setImageDrawable(android.R.id.icon, R.drawable.icon_error)
                        .setText(R.string.login_fail)
                        //设置动画效果
                        .setAnimStyle(R.style.IOSAnimStyle)
                        // 设置外层是否能被触摸
                        .setOutsideTouchable(false)
                        // 设置窗口背景阴影强度
                        .setBackgroundDimAmount(0.5f)
                        .show();
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
        if (personDao.checkLogin(username, password) != null
                || isNumber(username) && personDao.checkLoginByPhoneNumber(Long.parseLong(username), password) != null) {
            return true;
        }
        return false;
    }

    private boolean checkEmpty(String username, String password) {
        //判断是否为空
        if (username.isEmpty()) {
            new XToast<>(this)
                    .setContentView(R.layout.window_hint)
                    .setDuration(1000)
                    .setImageDrawable(android.R.id.icon, R.drawable.icon_error)
                    .setText(R.string.login_username_empty)
                    //设置动画效果
                    .setAnimStyle(R.style.IOSAnimStyle)
                    // 设置外层是否能被触摸
                    .setOutsideTouchable(false)
                    // 设置窗口背景阴影强度
                    .setBackgroundDimAmount(0.5f)
                    .show();
            mLoginButton.showError(3000);
            return true;
        } else if (password.isEmpty()) {
            new XToast<>(this)
                    .setContentView(R.layout.window_hint)
                    .setDuration(1000)
                    .setImageDrawable(android.R.id.icon, R.drawable.icon_error)
                    .setText(R.string.login_password_empty)
                    //设置动画效果
                    .setAnimStyle(R.style.IOSAnimStyle)
                    // 设置外层是否能被触摸
                    .setOutsideTouchable(false)
                    // 设置窗口背景阴影强度
                    .setBackgroundDimAmount(0.5f)
                    .show();
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