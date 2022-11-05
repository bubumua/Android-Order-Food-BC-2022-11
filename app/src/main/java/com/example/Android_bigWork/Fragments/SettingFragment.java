package com.example.Android_bigWork.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.Android_bigWork.Database.PersonDao;
import com.example.Android_bigWork.Database.PersonDatabase;
import com.example.Android_bigWork.Entity.Person;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.ViewModels.SettingViewModel;

public class SettingFragment extends Fragment {

    private SettingViewModel mViewModel;
    private Person user;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //初始化数据库

        //获取MainActivity的Bundle数据
        Intent intent = ((Activity) context).getIntent();
        Bundle bundle = intent.getExtras();
        user = (Person) bundle.getSerializable("user");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button button1 = (Button) getActivity().findViewById(R.id.button_user);
        Button button2 = (Button) getActivity().findViewById(R.id.button_update);
        TextView  tv1 =(TextView) getActivity().findViewById(R.id.editTextTextPassword_old);
        TextView  tv2 =(TextView) getActivity().findViewById(R.id.editTextTextPassword_new);
        TextView  tv3 =(TextView) getActivity().findViewById(R.id.textView5);
        tv3.setText(user.username);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str1 = tv1.getText().toString();
                String str2 = tv2.getText().toString();

                //获取Person数据库
                PersonDatabase personDatabase = PersonDatabase.getDatabase(getActivity());
                PersonDao personDao = personDatabase.getPersonDao();
                personDao.changePassword(str1,str2, user.username);

                //修改密码
                Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_LONG).show();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "已是最新版本！", Toast.LENGTH_LONG).show();
            }
        });
        mViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        // TODO: Use the ViewModel
    }


}