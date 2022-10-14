package com.example.Android_bigWork.Fragments;

import static com.example.Android_bigWork.Utils.RelativePopupWindow.makeDropDownMeasureSpec;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.widget.PopupWindowCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Android_bigWork.Activity.MainActivity;
import com.example.Android_bigWork.Adapters.FoodCategoryAdapter;
import com.example.Android_bigWork.Adapters.FoodStickyAdapter;
import com.example.Android_bigWork.Adapters.ShoppingCarAdapter;
import com.example.Android_bigWork.Database.DishDao;
import com.example.Android_bigWork.Database.DishDatabase;
import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.Entity.UserDish;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.RelativePopupWindow;
import com.example.Android_bigWork.Utils.StringUtil;
import com.example.Android_bigWork.ViewModels.DetailViewModel;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class DishMenuFragment extends Fragment {

    private final String TAG = "my";
    private DetailViewModel mViewModel;
    private StickyListHeadersListView stickyListView;
    private ListView listView;
    LinearLayout shoppingCar;
    Button payment;
    private String userName;

    //for test
    private ArrayList<Dish> dishList;
    private ArrayList<FoodCategoryAdapter.CategoryItem> categoryItems;
    private ArrayList<UserDish> userDishList;

    //数据库
    private DishDatabase dishDatabase;

    public static DishMenuFragment newInstance() {
        return new DishMenuFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.userName=((MainActivity)requireActivity()).getUsername();
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
        // init ViewModel
        mViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        userDishList=new ArrayList<>();
        // TODO: Use the ViewModel

        // bind Views
        bindViews(view);

        // 菜品栏初始化
        FoodStickyAdapter foodStickyAdapter = new FoodStickyAdapter(getContext(),this, dishList,userDishList,userName);
        stickyListView.setAdapter(foodStickyAdapter);
        // 分类栏初始化
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
                // 提醒左栏变化
                int firstVisibleCID = ((Dish) stickyListView.getAdapter().getItem(firstVisibleItem)).getCID();
                foodCategoryAdapter.updateCategorySelectionByCID(firstVisibleCID);

            }
        });

        // 类别栏按钮点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedCID = ((FoodCategoryAdapter.CategoryItem) foodCategoryAdapter.getItem(position)).getCID();
                int selectedPosition = foodStickyAdapter.getPositionByCID(selectedCID);
//                stickyListView.smoothScrollToPosition(selectedPosition);
                stickyListView.setSelection(selectedPosition);
//                stickyListView.smoothScrollToPositionFromTop(selectedPosition,0);
//                stickyListView.smoothScrollByOffset(foodStickyAdapter.getPositionByCID(selectedCID));
                Log.d(TAG, "onItemClick: click and set selection");
            }
        });

        // 支付按钮点击事件
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showNewPopupWindow();
                // 点击后生成确认对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(getRString(R.string.confirm_to_pay));
                builder.setMessage(getRString(R.string.confirm_message));
                builder.setNegativeButton(getRString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "dialogNo: payment cancel");
                    }
                });
                builder.setPositiveButton(getRString(R.string.confirm), (dialogInterface, i) -> {
                    Log.d(TAG, "dialogYes: payment succeed");
                    // TODO: 2022/10/12 生成订单
                });
                builder.create().show();
            }
        });

        // 初始化购物车已购金额
        setShoppingCarAccount(0);
        // 购物车栏点击事件
        shoppingCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShoppingCar();
            }
        });


        // 监听购物车栏的布局变化，获取高度
//        shoppingCar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                shoppingCarHeight=shoppingCar.getMeasuredHeight();
//            }
//        });

    }

    /**
     * 更新购物车已购金额、
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/14 21:03
     * @commit
     */
    public void updateShoppingCarAccount(){
        double total=0;
        for (UserDish ud:userDishList){
            total+=ud.getPrice();
        }
        setShoppingCarAccount(total);
    }

    /**
     * 设置购物车已购金额
     *
     * @param money 设置的金额
     * @return void
     * @Author Bubu
     * @date 2022/10/14 19:55
     * @commit
     */
    public void setShoppingCarAccount(double money){
        TextView totalAccount= shoppingCar.findViewById(R.id.account_in_car);
        totalAccount.setText(StringUtil.getSSMoney(money,72));
    }

    /**
     * 绑定视图
     *
     * @param view
     * @return void
     * @Author Bubu
     * @date 2022/10/12 20:51
     * @commit none
     */
    private void bindViews(View view) {
        stickyListView = view.findViewById(R.id.showdishes);
        listView = view.findViewById(R.id.category_list);
        payment = view.findViewById(R.id.shopping_commit);
        shoppingCar = view.findViewById(R.id.shopping_car);
    }

    /**
     * 获取string里商品属性
     *
     * @param id
     * @return String
     * @Author Anduin9527
     * @date 2022/10/12 8:29
     * @commit
     */
    private String getRString(@StringRes int id) {
        return getResources().getString(id);
    }

    /**
     * 测试用：初始化添加商品列表
     *
     * @return void
     * @description
     * @Author Bubu
     * @date 2022/10/12 17:45
     * @commit
     */
    private void initDishListForTest() {
        Resources r = getResources();
        //连接数据库
        dishList = new ArrayList<>();
        dishDatabase = DishDatabase.getDatabase(getContext());
        DishDao dishDao = dishDatabase.getDishDao();

        //获取数据库中的菜品
        dishList = (ArrayList<Dish>) dishDao.getAllDish();

        //输出内容
        for (Dish dish : dishList) {
            Log.d(TAG, "initDishListForTest: " + dish.toString());
        }
    }

    /**
     * 初始化类别列表，从商品列表中提取分类
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/13 0:51
     * @commit
     */
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

    /**
     * 显示购物车
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/12 17:45
     * @commit
     */
    public void showShoppingCar() {
        RelativePopupWindow shoppingCar = new RelativePopupWindow(getContext());
        // 绑定视图
        View contentView = shoppingCar.getContentView();
        Button button = contentView.findViewById(R.id.clear_shopping);
        RecyclerView shoppingList=contentView.findViewById(R.id.shopping_list);
        // 设置 RecyclerView
        shoppingList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        ShoppingCarAdapter shoppingCarAdapter=new ShoppingCarAdapter(getContext(),this,userDishList,dishList);
        shoppingList.setAdapter(shoppingCarAdapter);
        //需要先测量，PopupWindow还未弹出时，宽高为0
        contentView.measure(makeDropDownMeasureSpec(shoppingCar.getWidth()),
                makeDropDownMeasureSpec(shoppingCar.getHeight()));
        // 计算偏移量
        int offsetX = -contentView.getMeasuredWidth();
//        int offsetY = (contentView.getMeasuredHeight() + payment.getHeight());
        int offsetY=0;
        // 设置显隐动画
        shoppingCar.setAnimationStyle(R.style.shoppingCar_anim_style);
        // 显示购物车弹窗
        PopupWindowCompat.showAsDropDown(shoppingCar, payment, offsetX, offsetY, Gravity.END);
        Log.d(TAG, "showShoppingCar: X,Y="+offsetX+","+offsetY);
        // "清空"按钮点击事件
        button.setOnClickListener(v -> {
            // TODO: 清空购物车
            Log.d(TAG, "onClick: 清空");
        });
    }

    public ArrayList<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(ArrayList<Dish> dishList) {
        this.dishList = dishList;
    }

    public ArrayList<UserDish> getUserDishList() {
        return userDishList;
    }

    public void setUserDishList(ArrayList<UserDish> userDishList) {
        this.userDishList = userDishList;
    }

    public StickyListHeadersListView getStickyListView() {
        return stickyListView;
    }

    public void setStickyListView(StickyListHeadersListView stickyListView) {
        this.stickyListView = stickyListView;
    }

    /**
     * show shopping car popupWindow
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/12 17:45
     * @commit
     */
//    private void showPopupWindow() {
//        //加载弹出框的布局
//        contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow, null);
//        // 设置按钮的点击事件
//        Button button = contentView.findViewById(R.id.clear_shopping);
//        button.setOnClickListener(v -> {
//            // TODO: 清空购物车
//            Log.d(TAG, "onClick: 清空");
//        });
//        popupWindow = new PopupWindow(contentView,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//
//        popupWindow.setFocusable(true);// 取得焦点
//        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
//        //点击外部消失
//        popupWindow.setOutsideTouchable(true);
//        //设置可以点击
//        popupWindow.setTouchable(true);
//        //设置进入退出的动画，指定刚才定义的style
//        popupWindow.setAnimationStyle(R.style.ipopwindow_anim_style);
//        // 获取高度
//
//        // 显示PopWindow
//        int deltaY=bottomNavigationBarHeight+shoppingCarHeight;
//        Log.d(TAG, "showPopupWindow: offSetY="+(-popupWindow.getHeight()+deltaY));
//        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, deltaY);
////        popupWindow.showAsDropDown(showPopup,0,);
//    }
}