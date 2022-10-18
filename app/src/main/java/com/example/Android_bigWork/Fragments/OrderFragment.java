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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.Android_bigWork.Adapters.OrderAdapter;
import com.example.Android_bigWork.Entity.Person;
import com.example.Android_bigWork.Entity.UserDish;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.ViewModels.OrderViewModel;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class OrderFragment extends Fragment {

    private static String TAG="my";
    // Data
    private OrderViewModel orderViewModel;
    private Person user;

    // View
    private StickyListHeadersListView stickyListView;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        stickyListView = view.findViewById(R.id.show_orders);
        OrderAdapter orderAdapter=new OrderAdapter(getContext(),orderViewModel);
        stickyListView.setAdapter(orderAdapter);
        orderViewModel.getUserDishes().observe(getViewLifecycleOwner(), userDishes -> {
            Log.d(TAG, "OrderViewModelObserver: "+userDishes.size());
            // TODO: 设置 RecyclerView 显示的内容
            orderAdapter.setUserDishList(orderViewModel.getUserDishes().getValue());
            ((OrderAdapter)stickyListView.getAdapter()).notifyDataSetChanged();
        });

    }


}