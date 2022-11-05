package com.example.Android_bigWork.Fragments;


import static com.example.Android_bigWork.Utils.RelativePopupWindow.makeDropDownMeasureSpec;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.widget.PopupWindowCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Android_bigWork.Activity.MainActivity;
import com.example.Android_bigWork.Adapters.CouponAdapter;
import com.example.Android_bigWork.Adapters.FoodCategoryAdapter;
import com.example.Android_bigWork.Adapters.FoodStickyAdapter;
import com.example.Android_bigWork.Adapters.ImageAdapter;
import com.example.Android_bigWork.Adapters.ShoppingCarAdapter;
import com.example.Android_bigWork.Database.CouponDao;
import com.example.Android_bigWork.Database.CouponDatabase;
import com.example.Android_bigWork.Database.DishDao;
import com.example.Android_bigWork.Database.DishDatabase;
import com.example.Android_bigWork.Database.PersonDao;
import com.example.Android_bigWork.Database.PersonDatabase;
import com.example.Android_bigWork.Entity.Coupon;
import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.Entity.Person;
import com.example.Android_bigWork.Entity.UserDish;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.BaseDialog;
import com.example.Android_bigWork.Utils.PayPasswordDialog;
import com.example.Android_bigWork.Utils.RelativePopupWindow;
import com.example.Android_bigWork.Utils.StringUtil;
import com.example.Android_bigWork.ViewModels.OrderViewModel;
import com.hjq.xtoast.XToast;
import com.hjq.xtoast.draggable.SpringDraggable;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class DishMenuFragment extends Fragment {

    private final String TAG = "my";

    // 布局控件
    private StickyListHeadersListView stickyListView;
    private ListView listView;
    LinearLayout shoppingCar;
    Button payment;
    private String userName;
    private Banner banner;

    // 界面数据(列表)
    private ArrayList<Dish> dishList;
    private ArrayList<FoodCategoryAdapter.CategoryItem> categoryItems;
    private ArrayList<UserDish> userDishList;
    double total;
    private OrderViewModel orderViewModel;
    private Coupon selectedCoupon;

    //数据库
    private DishDatabase dishDatabase;
    private DishDao dishDao;
    private PersonDatabase personDatabase;
    private PersonDao personDao;

    private CouponDatabase couponDatabase;
    private CouponDao couponDao;
    private Person user;//MainActivity中的用户信息

    public static DishMenuFragment newInstance() {
        return new DishMenuFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //初始化数据库
        dishDatabase = DishDatabase.getDatabase(context);
        dishDao = dishDatabase.getDishDao();
        personDatabase = PersonDatabase.getDatabase(context);
        personDao = personDatabase.getPersonDao();
        couponDatabase = CouponDatabase.getDatabase(context);
        couponDao = couponDatabase.getCouponDao();
        //获取MainActivity的Bundle数据
        Intent intent = ((Activity) context).getIntent();
        Bundle bundle = intent.getExtras();
        user = (Person) bundle.getSerializable("user");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        initDishList();
        initCategoryItems();
        userDishList = new ArrayList<>();
        total = 0;
        selectedCoupon = null;
        return inflater.inflate(R.layout.fragment_dish_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init ViewModel
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        orderViewModel.getUserDishesForUser(user.username).observe(requireActivity(), new Observer<List<UserDish>>() {
            @Override
            public void onChanged(List<UserDish> userDishes) {
                Log.d(TAG, "userDishesObserver: data changed");
            }
        });

        // bind Views
        bindViews(view);

        // 菜品栏初始化
        FoodStickyAdapter foodStickyAdapter = new FoodStickyAdapter(getContext(), this, dishList, userDishList, user.username);
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
                // 提醒左栏变化
                int firstVisibleCID = ((Dish) stickyListView.getAdapter().getItem(firstVisibleItem)).getCID();
                foodCategoryAdapter.updateCategorySelectionByCID(firstVisibleCID);

            }
        });

// 类别栏按钮点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获得点击类别的CID
                int selectedCID = ((FoodCategoryAdapter.CategoryItem) foodCategoryAdapter.getItem(position)).getCID();
                // 根据CID，获取右侧菜单中该类别的第一个菜品的位置
                int selectedPosition = foodStickyAdapter.getPositionByCID(selectedCID);
                // 根据位置，进行跳转
                stickyListView.setSelection(selectedPosition);
                Log.d(TAG, "onItemClick: click and set selection");
            }
        });

// 支付按钮点击事件
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击后生成确认对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(getRString(R.string.confirm_to_pay));
                builder.setMessage(getRString(R.string.confirm_message));
                // 点击取消
                builder.setNegativeButton(getRString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "dialogNo: payment cancel");
                    }
                });
                // 点击确认
                builder.setPositiveButton(getRString(R.string.confirm), (dialogInterface, i) -> {
                    //获取当前购物车中的价格
                    double price = 0;
                    for (UserDish userDish : userDishList) {
                        price += userDish.getPrice();
                    }
                    if (price == 0) {
                        Toast.makeText(getContext(), "买点,多少买点", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //确认订单则弹出支付窗口
                    new PayPasswordDialog.Builder(requireActivity())
                            .setTitle(R.string.pay_title)
                            .setSubTitle(R.string.pay_sub_title)
                            .setMoney(StringUtil.getSSMoney(total, 72))// 设置订单金额
                            .setAutoDismiss(true)//支付满6位自动关闭
                            .setListener(new PayPasswordDialog.OnListener() {
                                @Override
                                public void onCompleted(BaseDialog dialog, String payPassword) {
                                    if (Integer.parseInt(payPassword) == user.payPassword) {
//                                        Toast.makeText(requireActivity(), getRString(R.string.pay_success), Toast.LENGTH_SHORT).show();
                                        //new XToast
                                        //获取MainActivity对象
                                        MainActivity mainActivity = (MainActivity) getActivity();
                                        //输出
                                        Log.d(TAG, "onCompleted: " + mainActivity);
                                        new XToast<>(requireActivity())
                                                .setContentView(R.layout.window_hint)
                                                .setDuration(1000)
                                                .setImageDrawable(android.R.id.icon, R.drawable.icon_success)
                                                .setText(R.string.pay_success)
                                                //设置动画效果
                                                .setAnimStyle(R.style.IOSAnimStyle)
                                                // 设置外层是否能被触摸
                                                .setOutsideTouchable(false)
                                                // 设置窗口背景阴影强度
                                                .setBackgroundDimAmount(0.5f)
                                                .show();
                                        // 为 userDishList 中所有菜品添加时间戳（订单生成时间），并插入数据库
                                        long currentTime = System.currentTimeMillis();
                                        for (UserDish ud : userDishList) {
                                            ud.setCreatedTime(currentTime);
                                            Log.d(TAG, "after payment: " + ud.display());
                                            orderViewModel.insert(ud);
                                        }
                                        // 支付后清空购物车
                                        clearShoppingCar();
                                        // 消耗优惠券
                                        if (selectedCoupon != null) {
                                            couponDao.deleteCoupon(selectedCoupon.CID);
                                            selectedCoupon = null;
                                        }
                                    } else {
//                                        Toast.makeText(requireActivity(), getRString(R.string.pay_fail), Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onPay: " + payPassword + " " + personDao.queryPayPassword(user.username));
                                        new XToast<>(requireActivity())
                                                .setContentView(R.layout.window_hint)
                                                .setDuration(1000)
                                                .setImageDrawable(android.R.id.icon, R.drawable.icon_error)
                                                .setText(R.string.pay_fail)
                                                //设置动画效果
                                                .setAnimStyle(R.style.IOSAnimStyle)
                                                // 设置外层是否能被触摸
                                                .setOutsideTouchable(false)
                                                // 设置窗口背景阴影强度
                                                .setBackgroundDimAmount(0.5f)
                                                .show();
                                    }
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                    new XToast<>(requireActivity())
                                            .setContentView(R.layout.window_hint)
                                            .setDuration(1000)
                                            .setImageDrawable(android.R.id.icon, R.drawable.icon_warning)
                                            .setText(R.string.pay_cancel)
                                            //设置动画效果
                                            .setAnimStyle(R.style.IOSAnimStyle)
                                            // 设置外层是否能被触摸
                                            .setOutsideTouchable(false)
                                            // 设置窗口背景阴影强度
                                            .setBackgroundDimAmount(0.5f)
                                            .show();
                                }
                            })
                            .show();
                });
                builder.create().show();
            }

        });

        // 初始化购物车已购金额
        setShoppingCarAccount(0);

        // 设置购物车栏点击事件
        shoppingCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShoppingCar();
            }
        });

        // 设置滚动栏
        // 设置生命周期观察者
        banner.addBannerLifecycleObserver(this);
        // 设置适配器
        banner.setAdapter(new ImageAdapter(dishList, getContext()));
        // 设置展示方式
        banner.setBannerGalleryMZ(100, 0.6f);
    }

    /**
     * 更新购物车已购金额、
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/14 21:03
     * @commit
     */
    public void updateShoppingCarAccount() {
        double total = 0;
        for (UserDish ud : userDishList) {
            total += ud.getPrice();
        }
        this.total = total;
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
    public void setShoppingCarAccount(double money) {
        TextView totalAccount = shoppingCar.findViewById(R.id.account_in_car);
        if (selectedCoupon == null || money < 0.01) {
            totalAccount.setText(StringUtil.getSSMoney(money, 72));
        } else {
            totalAccount.setText(StringUtil.getSSMoneyAfterDiscount(money, 72, selectedCoupon));
            switch (selectedCoupon.getType()) {
                case 0:
                    this.total = selectedCoupon.getDiscount() * this.total / 10;
                    break;
                case 1:
                    if (this.total >= selectedCoupon.getCondition()) {
                        this.total -= selectedCoupon.getReduction();
                    }
                    break;
                default:
                    break;
            }
        }
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
        banner = view.findViewById(R.id.banner_rec);
        redPackInit();
    }

    /**
     * 初始化红包
     *
     * @return void
     * @Author Anduin9527
     * @date 2022/10/29 10:18
     * @commit
     */
    private void redPackInit() {
        //计数器
        final int[] count = {0};
        new XToast<>(requireActivity())
                .setContentView(R.layout.window_redpack)
                .setAnimStyle(R.style.IOSAnimStyle)
                .setImageDrawable(android.R.id.icon, R.drawable.redpack)
                // 设置成可拖拽的
                .setDraggable(new SpringDraggable())
                .setOnClickListener(android.R.id.icon, new XToast.OnClickListener<ImageView>() {
                    @Override
                    public void onClick(final XToast<?> toast, ImageView view) {
                        new XToast<>(requireActivity())
                                .setContentView(R.layout.dialog_red_packet)
                                .setAnimStyle(R.style.IOSAnimStyle)
                                .setOnClickListener(R.id.iv_close, new XToast.OnClickListener<ImageView>() {
                                    @Override
                                    public void onClick(XToast<?> toast, ImageView view) {
                                        toast.cancel();
                                        count[0] -= 1;
                                    }
                                })
                                .setOnClickListener(R.id.iv_open, new XToast.OnClickListener<ImageView>() {
                                    @Override
                                    public void onClick(XToast<?> toast, ImageView view) {
                                        //获取id为R.id.iv_open的ImageView
                                        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
                                        animationDrawable.start();
                                        //生成优惠券
                                        String couponText = geneCoupon();
                                        toast.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //new XToast 显示领取成功
                                                new XToast<>(requireActivity())
                                                        .setDuration(2000)
                                                        .setContentView(R.layout.window_hint)
                                                        .setAnimStyle(R.style.IOSAnimStyle)
                                                        .setImageDrawable(android.R.id.icon, R.drawable.yanhua)
                                                        .setText(android.R.id.message, getRString(R.string.successfullyReceived)
                                                                + "\n" +
                                                                couponText
                                                                + " " + getRString(R.string.coupon))
                                                        .show();
                                                toast.cancel();
                                            }
                                        }, 900);
                                        Log.d(TAG, "redPack: " + couponDao.getAllCoupon(user.username));
                                    }
                                })
                                .show();
                        count[0] += 1;
                        if (count[0] == 3) {
                            toast.cancel();
                        }
                    }
                })
                .show();
        //查询用户目前拥有的优惠券
        Log.d(TAG, "redPackInit: " + couponDao.getAllCoupon(user.username));
    }

    /**
     * 随机生成优惠券，并插入数据库
     *
     * @return String
     * @Author Anduin9527
     * @date 2022/10/18 20:47
     * @commit
     */
    private String geneCoupon() {
        //随机生成优惠卷
        //生成优惠券类型0~1
        String couponText = "";
        double condition = 0, reduction = 0, discount = 0;
        int type = (int) (Math.random() * 2);
        boolean isChinese = false;
        String language = Locale.getDefault().getLanguage();
        if (language.equals("CN") || language.equals("zh")) {
            isChinese = true;
        }
        if (type == 1) {
            condition = (int) (Math.random() * 100) + 1;
            reduction = (int) (Math.random() * condition * 0.7) + 1;
            if (isChinese) {
                couponText = "满" + condition + "减" + reduction;
            } else {
                couponText = "Over " + condition + " Minus " + reduction;
            }
        } else {
            discount = (int) (Math.random() * 4) + 2;
            if (isChinese) {
                couponText = discount + "折";
            } else {
                couponText = (10 - discount) * 10 + "% OFF";
            }
        }
        //插入数据库

        couponDao.addCoupon(user.username, type, discount, condition, reduction);

        return couponText;
    }

    /**
     * 获取string中的属性值
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
    private void initDishList() {
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
        // 遍历菜单列表，如果该菜品所属类别尚未添加到类别列表中，则将此菜品的类别添加。
        dishList.forEach(dish -> {
            // 若类别列表为空，则直接添加
            if (categoryItems == null) {
                categoryItems = new ArrayList<>();
                categoryItems.add(new FoodCategoryAdapter.CategoryItem(dish.getCategory(), dish.getCID()));
            }
            // 若不为空，则遍历类别列表，若无此类，则添加
            else {
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
        RecyclerView shoppingList = contentView.findViewById(R.id.shopping_list);
        Spinner selectCoupon = contentView.findViewById(R.id.spinner_coupon);
        // 设置 RecyclerView
        shoppingList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        ShoppingCarAdapter shoppingCarAdapter = new ShoppingCarAdapter(getContext(), this, userDishList, dishList);
        shoppingList.setAdapter(shoppingCarAdapter);
        // 设置优惠券下拉框 Spinner
        List<Coupon> coupons = couponDao.getAllCoupon(user.username);
        CouponAdapter couponAdapter = new CouponAdapter(getContext(), coupons);
        selectCoupon.setAdapter(couponAdapter);
        /*初始化用户选择的优惠券*/
        int position = -1;
        for (int i = 0; i < coupons.size(); i++) {
            if (selectedCoupon != null && selectedCoupon.getCID() == coupons.get(i).getCID()) {
                position = i;
            }
        }
        if (position > -1) {
            selectCoupon.setSelection(position);
        }
        // 设置下拉框选项的点击事件
        selectCoupon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCoupon = coupons.get(position);
                String couponString = selectedCoupon.toString();
                Log.d(TAG, "onCouponItemClick: select " + couponString);
                updateShoppingCarAccount();
        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //需要先测量PopupWindow的宽高
        contentView.measure(makeDropDownMeasureSpec(shoppingCar.getWidth()),
                makeDropDownMeasureSpec(shoppingCar.getHeight()));
        // 计算偏移量
        int offsetX = -contentView.getMeasuredWidth();
        // int offsetY = (contentView.getMeasuredHeight() + payment.getHeight());
        int offsetY = 0;
        // 设置显隐动画
        shoppingCar.setAnimationStyle(R.style.shoppingCar_anim_style);
        // 显示购物车弹窗
        PopupWindowCompat.showAsDropDown(shoppingCar, payment, offsetX, offsetY, Gravity.END);
        Log.d(TAG, "showShoppingCar: X,Y=" + offsetX + "," + offsetY);
        // 设置"清空"按钮的点击事件
        button.setOnClickListener(v -> {
            Log.d(TAG, "onClick: 清空");
            // 清空购物车
            clearShoppingCar();
            // 更新购物车
            shoppingList.getAdapter().notifyDataSetChanged();
        });
    }


    /**
     * 清空购物车
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/26 13:47
     * @commit
     */
    public void clearShoppingCar() {
        Log.d(TAG, "clear the shopping car!");
        userDishList.clear();
        // 将选择的份数清零
        for (Dish dish : dishList) {
            if (dish.getCount() > 0) {
                dish.setCount(0);
            }
        }
        // 更新菜单列表
        ((FoodStickyAdapter) stickyListView.getAdapter()).notifyDataSetChanged();
        // 更新购物车
        updateShoppingCarAccount();
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
}