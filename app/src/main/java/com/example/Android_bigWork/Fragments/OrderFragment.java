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
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.Switch;


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

    private static String TAG = "my";
    // Data
    private OrderViewModel orderViewModel;
    private Person user;

    // View
    private StickyListHeadersListView stickyListView;
    public OrderAdapter orderAdapter;
    private Switch switchButton;
    private boolean showHistory;
    private long createdTime;
    private TextView ordersHeader;

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

        TextView tv4 = getActivity().findViewById(R.id.textView12);
        tv4.setText(user.username);
//        mViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        stickyListView = view.findViewById(R.id.show_orders);
        orderAdapter = new OrderAdapter(getContext(), orderViewModel);

        switchButton = view.findViewById(R.id.show_history);
        ordersHeader=view.findViewById(R.id.textView8);

        showHistory = false;
        createdTime = 0;

        stickyListView.setAdapter(orderAdapter);
        orderViewModel.getUserDishes().observe(getViewLifecycleOwner(), userDishes -> {
            Log.d(TAG, "OrderViewModelObserver: " + userDishes.size());
            // 获取时间
            updateOrders();
        });


        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showHistory = isChecked;
                updateOrders();
                if(isChecked){
                    ordersHeader.setText(getResources().getString(R.string.history_orders));
                }else {
                    ordersHeader.setText(getResources().getString(R.string.recent_order));
                }
            }
        });

    }

    /**
     * 当观察到用户付款引起的数据变化时，更新订单列表
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/19 14:04
     * @commit
     */
    public void updateOrders() {
        // 设置 RecyclerView 显示的内容
        createdTime = orderViewModel.lastOrderCreatedTime.getValue();
        List<UserDish> list = orderViewModel.getUserDishes().getValue();
        if (showHistory) {
            orderAdapter.setUserDishList(list);
            orderAdapter.setUserDishList(list);
        } else {
            List<UserDish> filter = new ArrayList<>();
            for (UserDish ud :
                    list) {
                if (ud.getCreatedTime() == createdTime) {
                    filter.add(ud);
                }
            }
            orderAdapter.setUserDishList(filter);
        }
        orderAdapter.notifyDataSetChanged();

    }

}