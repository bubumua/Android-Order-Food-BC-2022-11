package com.example.Android_bigWork.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.L;
import com.example.Android_bigWork.Adapters.OrderAdapter;
import com.example.Android_bigWork.Entity.Person;
import com.example.Android_bigWork.Entity.UserDish;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.SwitchButton;
import com.example.Android_bigWork.ViewModels.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class OrderFragment extends Fragment {

    RatingBar ratingBar;
    private static String TAG = "my";
    // Data
    private OrderViewModel orderViewModel;
    private Person user;

    // 视图控件
    private StickyListHeadersListView stickyListView;
    public OrderAdapter orderAdapter;
    private Switch switchButton;
    private boolean showHistory;
    private long createdTime;
    private TextView ordersHeader;
    private TextView commentTip;

    public static OrderFragment newInstance() {
        return new OrderFragment();
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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ratingBar = getActivity().findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                // 当评价大于0时显示提示消息，避免重置为0时触发消息弹出
                if(v>0) {
                    Toast.makeText(getActivity().getApplicationContext(), String.valueOf("本次服务评价为" + v + "星"), Toast.LENGTH_SHORT).show();
                }
                // 点击评价后使评价栏消失
                ratingBar.setVisibility(View.GONE);
                commentTip.setVisibility(View.GONE);
            }
        });

        // 初始化评价栏以及提示为隐藏
        commentTip.setVisibility(View.GONE);
        ratingBar.setVisibility(View.GONE);
        TextView tv4 = getActivity().findViewById(R.id.textView55);
        tv4.setText(user.username);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化视图
        stickyListView = view.findViewById(R.id.show_orders);
        switchButton = view.findViewById(R.id.show_history);
        ordersHeader = view.findViewById(R.id.textView8);
        commentTip=view.findViewById(R.id.textView15);
        // 设置数据
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        orderAdapter = new OrderAdapter(getContext(), orderViewModel);
        showHistory = false;
        createdTime = 0;
        // 设置订单列表展示内容
        stickyListView.setAdapter(orderAdapter);

        // 数据监听器，当数据改变时，更新视图内容
        orderViewModel.getUserDishes().observe(getViewLifecycleOwner(), userDishes -> {
            Log.d(TAG, "OrderViewModelObserver: " + userDishes.size());
            // 订单数据变化时，更新订单
            updateOrders();
            // 显示评价栏
            if (stickyListView.getAdapter().getCount() > 0) {
                ratingBar.setRating(0);
                ratingBar.setVisibility(View.VISIBLE);
                commentTip.setVisibility(View.VISIBLE);
            }
        });

        // 历史订单开关监听
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 改变是否显示历史订单的成员变量
                showHistory = isChecked;
                // 更新订单
                updateOrders();
                // 改变订单的标题
                if (isChecked) {
                    ordersHeader.setText(getResources().getString(R.string.history_orders));
                } else {
                    ordersHeader.setText(getResources().getString(R.string.recent_order));
                }
            }
        });

    }

    /**
     * 更新订单
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/25 21:20
     * @commit
     */
    public void updateOrders() {
        // 获取事件戳（订单生成时间）
        createdTime = orderViewModel.lastOrderCreatedTime.getValue();
        // 获取用户的所有订单列表
        List<UserDish> list = orderViewModel.getUserDishes().getValue();
        // 根据是否显示历史订单，显示不一样的数据
        if (showHistory) {
            // 设置显示所有历史订单
            orderAdapter.setUserDishList(list);
        }
        // 若不显示历史订单，则从所有订单中挑选最近的订单
        else {
            List<UserDish> filter = new ArrayList<>();
            for (UserDish ud : list) {
                if (ud.getCreatedTime() == createdTime) {
                    filter.add(ud);
                }
            }
            // 设置要显示的订单列表
            orderAdapter.setUserDishList(filter);
        }
        // 更新订单界面列表视图
        orderAdapter.notifyDataSetChanged();
    }
}