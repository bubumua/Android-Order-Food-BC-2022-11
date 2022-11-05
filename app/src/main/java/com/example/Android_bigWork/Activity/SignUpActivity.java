package com.example.Android_bigWork.Activity;

import static com.example.Android_bigWork.Utils.KeyboardUtils.hideKeyboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Android_bigWork.Database.PersonDao;
import com.example.Android_bigWork.Database.PersonDatabase;
import com.example.Android_bigWork.Entity.Person;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.BaseDialog;
import com.example.Android_bigWork.Utils.PayPasswordDialog;
import com.example.Android_bigWork.action.HandlerAction;
import com.example.Android_bigWork.Utils.SubmitButton;
import com.example.Android_bigWork.Utils.SwitchButton;
import com.hjq.xtoast.XToast;

/**
 * @author Anduin9527
 * @Type SignUpActivity
 * @Desc 注册界面
 * @date 2022/10/9 20:09
 */
public class SignUpActivity extends AppCompatActivity
        implements HandlerAction {
    EditText mUsername, mPassword, mPhoneNumber;
    SubmitButton mSignUpButton;
    SwitchButton mGender;
    private String TAG = "SignUpActivity";

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

        Intent navigateToLogin = new Intent(this, LoginActivity.class);

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
            if (checkEmpty(username, password, phoneNumber)) {
                //查询数据库
                if (checkDataBase(username, password, phoneNumber, personDao)) {
                    //添加用户
                    new PayPasswordDialog.Builder(this)
                            .setTitle(getRString(R.string.pay_set_title))
                            .setSubTitle(R.string.pay_set_sub_title)
                            .setAutoDismiss(true)
                            .setListener(new PayPasswordDialog.OnListener() {
                                @Override
                                public void onCompleted(BaseDialog dialog, String payPassword) {
                                    //添加用户
                                    Person person = new Person(username, password, Long.parseLong(phoneNumber), isFemale[0], Integer.parseInt(payPassword));
                                    personDao.insert(person);
                                    mSignUpButton.showSucceed();
                                    Log.d(TAG, "onSignUp: " + person.toString());
//                                    Toast.makeText(SignUpActivity.this, getRString(R.string.register_success), Toast.LENGTH_SHORT).show();
                                    //使用HandlerAction接口的PostDelayed方法实现延时跳转
                                    postDelayed(() -> {
                                        //构建Bundle对象传递person
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("user", person);

                                        navigateToLogin.putExtras(bundle);
                                        startActivity(navigateToLogin);
                                        finish();
                                    }, 2000);
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                    postDelayed(() -> {
                                        mSignUpButton.reset();
                                    }, 1000);
                                    Toast.makeText(SignUpActivity.this, getRString(R.string.register_fail), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .show();
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

    private boolean checkDataBase(String username, String password, String phoneNumber, PersonDao personDao) {
        //查询数据库
        if (personDao.checkUsername(username) != null) {
            newErrorXToast(R.string.register_username_exist);
            mSignUpButton.showError(2000);
        } else if (personDao.checkPhoneNumber(Long.parseLong(phoneNumber)) != null) {
            newErrorXToast(R.string.register_phone_exist);
            mSignUpButton.showError(2000);
        } else if (phoneNumber.length() != 11) {
            newErrorXToast(R.string.register_phone_error);
            mSignUpButton.showError(2000);
        } else if (password.length() < 6) {
            newErrorXToast(R.string.register_password_less);
            mSignUpButton.showError(2000);
        } else {
            return true;
        }
        return false;
    }

    private boolean checkEmpty(String username, String password, String phoneNumber) {
        if (username.isEmpty()) {
            newErrorXToast(R.string.register_username_empty);
            mSignUpButton.showError(2000);
        } else if (password.isEmpty()) {
            newErrorXToast(R.string.register_password_empty);
            mSignUpButton.showError(2000);
        } else if (phoneNumber.isEmpty()) {
            newErrorXToast(R.string.register_phone_empty);
            mSignUpButton.showError(2000);
        } else {
            return true;
        }
        return false;
    }

    private String getRString(@StringRes int id) {
        return getResources().getString(id);
    }

    private void newErrorXToast(@StringRes int id) {
        new XToast<>(this)
                .setContentView(R.layout.window_hint)
                .setDuration(1000)
                .setImageDrawable(android.R.id.icon, R.drawable.icon_error)
                .setText(getRString(id))
                //设置动画效果
                .setAnimStyle(R.style.IOSAnimStyle)
                // 设置外层是否能被触摸
                .setOutsideTouchable(false)
                // 设置窗口背景阴影强度
                .setBackgroundDimAmount(0.5f)
                .show();
    }

}
