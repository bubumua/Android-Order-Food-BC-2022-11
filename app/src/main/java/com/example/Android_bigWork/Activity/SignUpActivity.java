package com.example.Android_bigWork.Activity;

import static com.example.Android_bigWork.Utils.KeyboardUtils.hideKeyboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Android_bigWork.Database.PersonDao;
import com.example.Android_bigWork.Database.PersonDatabase;
import com.example.Android_bigWork.Entity.PersonEntity;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.SubmitButton;
import com.example.Android_bigWork.Utils.SwitchButton;

/**
 * @author Anduin9527
 * @Type SignUpActivity
 * @Desc 注册界面
 * @date 2022/10/9 20:09
 */
public class SignUpActivity extends AppCompatActivity {
    EditText mUsername, mPassword, mPhoneNumber;
    SubmitButton mSignUpButton;
    SwitchButton mGender;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.signup_activity);

        mUsername = findViewById(R.id.textView_username);
        mPassword = findViewById(R.id.textView_password);
        mPhoneNumber = findViewById(R.id.textView_phoneNumber);
        mGender = findViewById(R.id.SwitchButton_gender);
        mSignUpButton = findViewById(R.id.btn_signup);

        //设置电话号码输入框只能输入数字
        mPhoneNumber.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        final int[] isFemale = {0};
        //获取数据库
        PersonDatabase personDatabase = PersonDatabase.getDatabase(this);
        PersonDao personDao = personDatabase.getPersonDao();
        //添加男女选择监听器
        mGender.setOnClickListener(v -> {
            if (mGender.isChecked())
                isFemale[0] = 1;
            else
                isFemale[0] = 0;
        });
        //注册按钮监听器
        findViewById(R.id.btn_signup).setOnClickListener(v -> {
            mSignUpButton.showProgress();
            String username = this.mUsername.getText().toString();
            String password = this.mPassword.getText().toString();
            String phoneNumber = this.mPhoneNumber.getText().toString();
            //判断是否为空
            if (username.isEmpty()) {
                Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                mSignUpButton.showError(3000);
            } else if (password.isEmpty()) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                mSignUpButton.showError(3000);
            } else if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                mSignUpButton.showError(3000);
            } else {
                //查询数据库
                if (personDao.checkUsername(username) != null) {
                    Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
                    mSignUpButton.showError(3000);
                } else if (personDao.checkPhoneNumber(Long.parseLong(phoneNumber)) != null) {
                    Toast.makeText(this, "手机号已存在", Toast.LENGTH_SHORT).show();
                    mSignUpButton.showError(3000);
                } else if (phoneNumber.length() != 11) {
                    Toast.makeText(this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                    mSignUpButton.showError(3000);
                } else {
                    //添加用户
                    PersonEntity personEntity = new PersonEntity(username, password, Long.parseLong(phoneNumber), isFemale[0]);
                    personDao.insert(personEntity);
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    mSignUpButton.showSucceed();
                    //跳转到登录界面
                    Intent intent = new Intent(this, LoginActivity.class);
                    //携带数据并填入
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);

                }
            }
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
}
