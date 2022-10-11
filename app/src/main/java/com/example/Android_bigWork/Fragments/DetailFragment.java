package com.example.Android_bigWork.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.Android_bigWork.Activity.MainActivity;
import com.example.Android_bigWork.Adapters.FoodCategoryAdapter;
import com.example.Android_bigWork.Adapters.FoodStickyAdapter;
import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.ViewModels.DetailViewModel;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class DetailFragment extends Fragment {

    private final String TAG = "DetailFragment";
    private DetailViewModel mViewModel;
    private StickyListHeadersListView stickyListView;
    private ListView listView;

    //for test
    private ArrayList<Dish> dishList;
    private ArrayList<FoodCategoryAdapter.CategoryItem> categoryItems;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // for test
        initDishListForTest();
        initCategoryItems();

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        // 菜品栏初始化
        stickyListView = view.findViewById(R.id.showdishes);
        FoodStickyAdapter foodStickyAdapter = new FoodStickyAdapter(getContext(), dishList);
        stickyListView.setAdapter(foodStickyAdapter);
        // 分类栏初始化
        listView = view.findViewById(R.id.category_list);
        FoodCategoryAdapter foodCategoryAdapter = new FoodCategoryAdapter(getContext(), categoryItems);
        listView.setAdapter(foodCategoryAdapter);

        // 菜品栏滑动监听
        stickyListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.d(TAG, "onScroll: firstVisibleItem=" + firstVisibleItem +
//                        ",visibleItemCount=" + visibleItemCount +
//                        ",totalItemCount=" + totalItemCount);
                // TODO: 提醒左栏变化
//                Log.d(TAG, "onScroll: "+(stickyListView.getAdapter()==foodStickyAdapter));
                int firstVisibleCID = ((Dish) stickyListView.getAdapter().getItem(firstVisibleItem)).getCID();
                foodCategoryAdapter.updateCategorySelectionByCID(firstVisibleCID);

            }
        });

        // 分类栏按钮点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedCID=((FoodCategoryAdapter.CategoryItem)foodCategoryAdapter.getItem(position)).getCID();
                int selectedPosition=foodStickyAdapter.getPositionByCID(selectedCID);
                stickyListView.smoothScrollToPosition(selectedPosition);
                stickyListView.setSelection(selectedPosition);
//                stickyListView.smoothScrollToPositionFromTop(selectedPosition,0);
//                stickyListView.smoothScrollByOffset(foodStickyAdapter.getPositionByCID(selectedCID));
                Log.d(TAG, "onItemClick: click and set selection");
            }
        });

        // TODO: Use the ViewModel

//        Dialog dialog = new Dialog(getContext(),R.style.Theme_AppCompat_Dialog);
        Dialog dialog = new Dialog(getContext(), androidx.databinding.library.baseAdapters.R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(LayoutInflater.from(getContext()).inflate(R.layout.dialog, null));
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);// 边距设为0
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);//背景透明，不然会有个白色的东东
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
        lp.height = 300; // 高度
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
        // 弹出dialog
        dialog.show();

    }

    private void initDishListForTest() {
        // for test
        dishList = new ArrayList<>();
        dishList.add(new Dish(111, getResources().getString(R.string.dish_01), "none", 2.5, "OEW", 0, false));
        dishList.add(new Dish(111, "TEST DISH", "none", 2.5, "OEW", 0, false));
        dishList.add(new Dish(111, "TEST DISH", "none", 2.5, "OEW", 0, false));
        dishList.add(new Dish(111, "TEST DISH", "none", 2.5, "OEW", 0, false));
        dishList.add(new Dish(111, "TEST DISH", "none", 2.5, "OEW", 0, false));
        dishList.add(new Dish(111, "TEST DISH", "none", 2.5, "OEW", 0, false));
        dishList.add(new Dish(111, "TEST DISH", "none", 2.5, "OEW", 0, false));
        dishList.add(new Dish(111, "TEST DISH", "none", 2.5, "OEW", 0, false));
        dishList.add(new Dish(111, "TEST DISH", "none", 2.5, "POPULAR", 1, false));
        dishList.add(new Dish(111, "TEST DISH", "none", 2.5, "POPULAR", 1, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "POPULAR", 1, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "POPULAR", 1, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "POPULAR", 1, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "POPULAR", 1, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "POPULAR", 1, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "CEW", 2, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "OEW", 2, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "NEW", 2, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "NEW", 2, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "NEW", 2, false));
        dishList.add(new Dish(211, "TEST DISH", "none", 2.5, "NEW", 2, false));

    }

    private void initCategoryItems() {
        categoryItems = null;
        dishList.forEach(dish -> {
            if (categoryItems == null) {
                categoryItems = new ArrayList<>();
                categoryItems.add(new FoodCategoryAdapter.CategoryItem(dish.getCategory(), dish.getCID()));
            } else {
                boolean addCategory = true;
                for (int i = 0; i < categoryItems.size(); i++) {
                    if (dish.getCID() == categoryItems.get(i).getCID()) {
                        addCategory = false;
                        break;
                    }
                }
                if (addCategory) {
                    categoryItems.add(new FoodCategoryAdapter.CategoryItem(dish.getCategory(), dish.getCID()));
                }
            }
        });
    }
}