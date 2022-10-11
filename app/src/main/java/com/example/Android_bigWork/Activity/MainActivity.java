package com.example.Android_bigWork.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
//import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Fragments.DetailFragment;
import com.example.Android_bigWork.Fragments.OrderFragment;
import com.example.Android_bigWork.Fragments.SettingFragment;
import com.example.Android_bigWork.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private BottomNavigationBar mBottomNavigationBar;
    private ArrayList<Fragment> fragmentArrayList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取到fragment的管理对象
        fragmentManager = getSupportFragmentManager();

        // init BottomNavigationBar
        initBottomNavigationBar();

        // init FragmentArrayList
        initFragmentArrayList();

        // init FragmentTransaction and select the first fragment to show
        initFragmentTransaction();

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    private void initFragmentTransaction() {
        //开启事务
        fragmentTransaction=fragmentManager.beginTransaction();
        for (int i = 0; i < fragmentArrayList.size(); i++) {
            fragmentTransaction.add(R.id.fragmentContainer,fragmentArrayList.get(i));
            fragmentTransaction.hide(fragmentArrayList.get(i));
        }
        fragmentTransaction.show(fragmentArrayList.get(0));
        // commit FragmentTransaction to apply changes
        fragmentTransaction.commit();
    }

    private void initFragmentArrayList() {
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new DetailFragment());
        fragmentArrayList.add(new OrderFragment());
        fragmentArrayList.add(new SettingFragment());
    }

    private void initBottomNavigationBar() {
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomNavigationBar);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_home_24, "Detail"))
                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_home_24, "Order"))
                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_home_24, "Setting"))
                .setFirstSelectedPosition(0)
                .initialise();
        // BottomNavigationBar的点击监听器
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Log.d(TAG, "onTabSelected: " + position);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.show(fragmentArrayList.get(position));
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {
                Log.d(TAG, "onTabUnselected: " + position);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(fragmentArrayList.get(position));
                fragmentTransaction.commit();
            }

            @Override
            public void onTabReselected(int position) {
                Log.d(TAG, "onTabReselected: " + position);
            }
        });
    }

}