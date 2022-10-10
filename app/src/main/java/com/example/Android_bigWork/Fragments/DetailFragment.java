package com.example.Android_bigWork.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.Android_bigWork.Adapters.FoodStickyAdapter;
import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.ViewModels.DetailViewModel;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class DetailFragment extends Fragment {

    private DetailViewModel mViewModel;
    private StickyListHeadersListView stickyList;

    //for test
    private ArrayList<Dish> dishListForTest;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false);
//        binding.setLifecycleOwner(getActivity());

        // for test
        dishListForTest=new ArrayList<>();
        dishListForTest.add(new Dish(111,"TEST DISH","none",2.5,"OEW",false));
        dishListForTest.add(new Dish(111,"TEST DISH","none",2.5,"OEW",false));
        dishListForTest.add(new Dish(111,"TEST DISH","none",2.5,"OEW",false));
        dishListForTest.add(new Dish(111,"TEST DISH","none",2.5,"OEW",false));
        dishListForTest.add(new Dish(111,"TEST DISH","none",2.5,"OEW",false));
        dishListForTest.add(new Dish(111,"TEST DISH","none",2.5,"OEW",false));
        dishListForTest.add(new Dish(111,"TEST DISH","none",2.5,"OEW",false));
        dishListForTest.add(new Dish(111,"TEST DISH","none",2.5,"OEW",false));
        dishListForTest.add(new Dish(111,"TEST DISH","none",2.5,"OEW",false));
        dishListForTest.add(new Dish(111,"TEST DISH","none",2.5,"OEW",false));

        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"NEW",false));
        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"NEW",false));
        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"NEW",false));
        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"NEW",false));
        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"NEW",false));
        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"NEW",false));
        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"OEW",false));
        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"NEW",false));
        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"NEW",false));
        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"NEW",false));

        dishListForTest.add(new Dish(211,"TEST DISH","none",2.5,"NEW",false));



//        return binding.getRoot();
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        stickyList=(StickyListHeadersListView) view.findViewById(R.id.showdishes);
        FoodStickyAdapter foodStickyAdapter=new FoodStickyAdapter(getContext(),dishListForTest);
        stickyList.setAdapter(foodStickyAdapter);
        // TODO: Use the ViewModel

    }

}