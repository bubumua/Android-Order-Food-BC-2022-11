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

    // ????????????
    private StickyListHeadersListView stickyListView;
    private ListView listView;
    LinearLayout shoppingCar;
    Button payment;
    private String userName;
    private Banner banner;

    // ????????????(??????)
    private ArrayList<Dish> dishList;
    private ArrayList<FoodCategoryAdapter.CategoryItem> categoryItems;
    private ArrayList<UserDish> userDishList;
    double total;
    private OrderViewModel orderViewModel;
    private Coupon selectedCoupon;

    //?????????
    private DishDatabase dishDatabase;
    private DishDao dishDao;
    private PersonDatabase personDatabase;
    private PersonDao personDao;

    private CouponDatabase couponDatabase;
    private CouponDao couponDao;
    private Person user;//MainActivity??????????????????

    public static DishMenuFragment newInstance() {
        return new DishMenuFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //??????????????????
        dishDatabase = DishDatabase.getDatabase(context);
        dishDao = dishDatabase.getDishDao();
        personDatabase = PersonDatabase.getDatabase(context);
        personDao = personDatabase.getPersonDao();
        couponDatabase = CouponDatabase.getDatabase(context);
        couponDao = couponDatabase.getCouponDao();
        //??????MainActivity???Bundle??????
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

        // ??????????????????
        FoodStickyAdapter foodStickyAdapter = new FoodStickyAdapter(getContext(), this, dishList, userDishList, user.username);
        stickyListView.setAdapter(foodStickyAdapter);
        // ??????????????????
        FoodCategoryAdapter foodCategoryAdapter = new FoodCategoryAdapter(getContext(), categoryItems);
        listView.setAdapter(foodCategoryAdapter);

// ?????????????????????
        stickyListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // ??????????????????
                int firstVisibleCID = ((Dish) stickyListView.getAdapter().getItem(firstVisibleItem)).getCID();
                foodCategoryAdapter.updateCategorySelectionByCID(firstVisibleCID);

            }
        });

// ???????????????????????????
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ?????????????????????CID
                int selectedCID = ((FoodCategoryAdapter.CategoryItem) foodCategoryAdapter.getItem(position)).getCID();
                // ??????CID????????????????????????????????????????????????????????????
                int selectedPosition = foodStickyAdapter.getPositionByCID(selectedCID);
                // ???????????????????????????
                stickyListView.setSelection(selectedPosition);
                Log.d(TAG, "onItemClick: click and set selection");
            }
        });

// ????????????????????????
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ??????????????????????????????
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(getRString(R.string.confirm_to_pay));
                builder.setMessage(getRString(R.string.confirm_message));
                // ????????????
                builder.setNegativeButton(getRString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "dialogNo: payment cancel");
                    }
                });
                // ????????????
                builder.setPositiveButton(getRString(R.string.confirm), (dialogInterface, i) -> {
                    //?????????????????????????????????
                    double price = 0;
                    for (UserDish userDish : userDishList) {
                        price += userDish.getPrice();
                    }
                    if (price == 0) {
                        Toast.makeText(getContext(), "??????,????????????", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //?????????????????????????????????
                    new PayPasswordDialog.Builder(requireActivity())
                            .setTitle(R.string.pay_title)
                            .setSubTitle(R.string.pay_sub_title)
                            .setMoney(StringUtil.getSSMoney(total, 72))// ??????????????????
                            .setAutoDismiss(true)//?????????6???????????????
                            .setListener(new PayPasswordDialog.OnListener() {
                                @Override
                                public void onCompleted(BaseDialog dialog, String payPassword) {
                                    if (Integer.parseInt(payPassword) == user.payPassword) {
//                                        Toast.makeText(requireActivity(), getRString(R.string.pay_success), Toast.LENGTH_SHORT).show();
                                        //new XToast
                                        //??????MainActivity??????
                                        MainActivity mainActivity = (MainActivity) getActivity();
                                        //??????
                                        Log.d(TAG, "onCompleted: " + mainActivity);
                                        new XToast<>(requireActivity())
                                                .setContentView(R.layout.window_hint)
                                                .setDuration(1000)
                                                .setImageDrawable(android.R.id.icon, R.drawable.icon_success)
                                                .setText(R.string.pay_success)
                                                //??????????????????
                                                .setAnimStyle(R.style.IOSAnimStyle)
                                                // ??????????????????????????????
                                                .setOutsideTouchable(false)
                                                // ??????????????????????????????
                                                .setBackgroundDimAmount(0.5f)
                                                .show();
                                        // ??? userDishList ???????????????????????????????????????????????????????????????????????????
                                        long currentTime = System.currentTimeMillis();
                                        for (UserDish ud : userDishList) {
                                            ud.setCreatedTime(currentTime);
                                            Log.d(TAG, "after payment: " + ud.display());
                                            orderViewModel.insert(ud);
                                        }
                                        // ????????????????????????
                                        clearShoppingCar();
                                        // ???????????????
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
                                                //??????????????????
                                                .setAnimStyle(R.style.IOSAnimStyle)
                                                // ??????????????????????????????
                                                .setOutsideTouchable(false)
                                                // ??????????????????????????????
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
                                            //??????????????????
                                            .setAnimStyle(R.style.IOSAnimStyle)
                                            // ??????????????????????????????
                                            .setOutsideTouchable(false)
                                            // ??????????????????????????????
                                            .setBackgroundDimAmount(0.5f)
                                            .show();
                                }
                            })
                            .show();
                });
                builder.create().show();
            }

        });

        // ??????????????????????????????
        setShoppingCarAccount(0);

        // ??????????????????????????????
        shoppingCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShoppingCar();
            }
        });

        // ???????????????
        // ???????????????????????????
        banner.addBannerLifecycleObserver(this);
        // ???????????????
        banner.setAdapter(new ImageAdapter(dishList, getContext()));
        // ??????????????????
        banner.setBannerGalleryMZ(100, 0.6f);
    }

    /**
     * ??????????????????????????????
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
     * ???????????????????????????
     *
     * @param money ???????????????
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
     * ????????????
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
     * ???????????????
     *
     * @return void
     * @Author Anduin9527
     * @date 2022/10/29 10:18
     * @commit
     */
    private void redPackInit() {
        //?????????
        final int[] count = {0};
        new XToast<>(requireActivity())
                .setContentView(R.layout.window_redpack)
                .setAnimStyle(R.style.IOSAnimStyle)
                .setImageDrawable(android.R.id.icon, R.drawable.redpack)
                // ?????????????????????
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
                                        //??????id???R.id.iv_open???ImageView
                                        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
                                        animationDrawable.start();
                                        //???????????????
                                        String couponText = geneCoupon();
                                        toast.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //new XToast ??????????????????
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
        //????????????????????????????????????
        Log.d(TAG, "redPackInit: " + couponDao.getAllCoupon(user.username));
    }

    /**
     * ??????????????????????????????????????????
     *
     * @return String
     * @Author Anduin9527
     * @date 2022/10/18 20:47
     * @commit
     */
    private String geneCoupon() {
        //?????????????????????
        //?????????????????????0~1
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
                couponText = "???" + condition + "???" + reduction;
            } else {
                couponText = "Over " + condition + " Minus " + reduction;
            }
        } else {
            discount = (int) (Math.random() * 4) + 2;
            if (isChinese) {
                couponText = discount + "???";
            } else {
                couponText = (10 - discount) * 10 + "% OFF";
            }
        }
        //???????????????

        couponDao.addCoupon(user.username, type, discount, condition, reduction);

        return couponText;
    }

    /**
     * ??????string???????????????
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
     * ???????????????????????????????????????
     *
     * @return void
     * @description
     * @Author Bubu
     * @date 2022/10/12 17:45
     * @commit
     */
    private void initDishList() {
        Resources r = getResources();
        //???????????????
        dishList = new ArrayList<>();
        dishDatabase = DishDatabase.getDatabase(getContext());
        DishDao dishDao = dishDatabase.getDishDao();

        //???????????????????????????
        dishList = (ArrayList<Dish>) dishDao.getAllDish();

        //????????????
        for (Dish dish : dishList) {
            Log.d(TAG, "initDishListForTest: " + dish.toString());
        }
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/13 0:51
     * @commit
     */
    private void initCategoryItems() {
        categoryItems = null;
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        dishList.forEach(dish -> {
            // ???????????????????????????????????????
            if (categoryItems == null) {
                categoryItems = new ArrayList<>();
                categoryItems.add(new FoodCategoryAdapter.CategoryItem(dish.getCategory(), dish.getCID()));
            }
            // ???????????????????????????????????????????????????????????????
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
     * ???????????????
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/12 17:45
     * @commit
     */
    public void showShoppingCar() {
        RelativePopupWindow shoppingCar = new RelativePopupWindow(getContext());
        // ????????????
        View contentView = shoppingCar.getContentView();
        Button button = contentView.findViewById(R.id.clear_shopping);
        RecyclerView shoppingList = contentView.findViewById(R.id.shopping_list);
        Spinner selectCoupon = contentView.findViewById(R.id.spinner_coupon);
        // ?????? RecyclerView
        shoppingList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        ShoppingCarAdapter shoppingCarAdapter = new ShoppingCarAdapter(getContext(), this, userDishList, dishList);
        shoppingList.setAdapter(shoppingCarAdapter);
        // ???????????????????????? Spinner
        List<Coupon> coupons = couponDao.getAllCoupon(user.username);
        CouponAdapter couponAdapter = new CouponAdapter(getContext(), coupons);
        selectCoupon.setAdapter(couponAdapter);
        /*?????????????????????????????????*/
        int position = -1;
        for (int i = 0; i < coupons.size(); i++) {
            if (selectedCoupon != null && selectedCoupon.getCID() == coupons.get(i).getCID()) {
                position = i;
            }
        }
        if (position > -1) {
            selectCoupon.setSelection(position);
        }
        // ????????????????????????????????????
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
        //???????????????PopupWindow?????????
        contentView.measure(makeDropDownMeasureSpec(shoppingCar.getWidth()),
                makeDropDownMeasureSpec(shoppingCar.getHeight()));
        // ???????????????
        int offsetX = -contentView.getMeasuredWidth();
        // int offsetY = (contentView.getMeasuredHeight() + payment.getHeight());
        int offsetY = 0;
        // ??????????????????
        shoppingCar.setAnimationStyle(R.style.shoppingCar_anim_style);
        // ?????????????????????
        PopupWindowCompat.showAsDropDown(shoppingCar, payment, offsetX, offsetY, Gravity.END);
        Log.d(TAG, "showShoppingCar: X,Y=" + offsetX + "," + offsetY);
        // ??????"??????"?????????????????????
        button.setOnClickListener(v -> {
            Log.d(TAG, "onClick: ??????");
            // ???????????????
            clearShoppingCar();
            // ???????????????
            shoppingList.getAdapter().notifyDataSetChanged();
        });
    }


    /**
     * ???????????????
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/26 13:47
     * @commit
     */
    public void clearShoppingCar() {
        Log.d(TAG, "clear the shopping car!");
        userDishList.clear();
        // ????????????????????????
        for (Dish dish : dishList) {
            if (dish.getCount() > 0) {
                dish.setCount(0);
            }
        }
        // ??????????????????
        ((FoodStickyAdapter) stickyListView.getAdapter()).notifyDataSetChanged();
        // ???????????????
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