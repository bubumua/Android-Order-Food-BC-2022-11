package com.example.Android_bigWork.Activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Android_bigWork.Database.PersonDao;
import com.example.Android_bigWork.Database.PersonDatabase;
import com.example.Android_bigWork.Database.PersonEntity;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.SwitchButton;

/**
 * @author Anduin9527
 * @Type SignUpActivity
 * @Desc 注册界面
 * @date 2022/10/9 20:09
 */
public class SignUpActivity extends AppCompatActivity {
    EditText username, password, phoneNumber;
    SwitchButton gender;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.signup_activity);

        username = findViewById(R.id.textView_username);
        password = findViewById(R.id.textView_password);
        phoneNumber = findViewById(R.id.textView_phoneNumber);
        gender = findViewById(R.id.SwitchButton_gender);
        //设置gender的背景颜色


        final int[] isFemale = {0};
        //获取数据库
        PersonDatabase personDatabase = PersonDatabase.getDatabase(this);
        PersonDao personDao = personDatabase.getPersonDao();
        //添加男女选择监听器
        gender.setOnClickListener(v -> {
            if (gender.isChecked())
                isFemale[0] = 1;
            else
                isFemale[0] = 0;
        });
        //添加按钮监听器
        findViewById(R.id.btn_login).setOnClickListener(v -> {
            String username = this.username.getText().toString();
            String password = this.password.getText().toString();
            String phoneNumber = this.phoneNumber.getText().toString();
            //判断是否为空
            if (username.isEmpty())
                Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            else if (password.isEmpty())
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            else if (phoneNumber.isEmpty())
                Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            else {
                //查询数据库
                if (personDao.checkUsername(username) != null)
                    Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
                else if (personDao.checkPhoneNumber(Integer.parseInt(phoneNumber)) != null)
                    Toast.makeText(this, "手机号已存在", Toast.LENGTH_SHORT).show();
                else {
                    //添加到数据库
                    PersonEntity personEntity = new PersonEntity(username, password, Integer.parseInt(phoneNumber), isFemale[0]);
                    personDao.insert(personEntity);
                    Toast.makeText(this, "注册成功!", Toast.LENGTH_SHORT).show();
                    Log.d("SignUp", "username: " + username + " password: " + password + " phoneNumber: " + phoneNumber + " isFemale: " + isFemale[0]);
                }
            }
        });
    }
}
