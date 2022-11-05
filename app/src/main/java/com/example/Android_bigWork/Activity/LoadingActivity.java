package com.example.Android_bigWork.Activity;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Android_bigWork.Database.DishDao;
import com.example.Android_bigWork.Database.DishDatabase;
import com.example.Android_bigWork.Database.PersonDao;
import com.example.Android_bigWork.Database.PersonDatabase;
import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.StringUtil;
import com.example.Android_bigWork.action.HandlerAction;

/**
 * @author Anduin9527
 * @Type LoadingActivity
 * @Desc 欢迎界面
 * @date 2022/10/10 20:09
 */
public class LoadingActivity extends AppCompatActivity
        implements HandlerAction {
    private static final String TAG = "LoadingActivity";
    private LottieAnimationView loadingAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        //去掉标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        loadingAnimationView = findViewById(R.id.animationView);
        loadingAnimationView.setAnimation(R.raw.animation_loading);//设置动画文件
        loadingAnimationView.playAnimation();//开始播放
        loadingAnimationView.setRepeatCount(12);//设置重复次数
        //连接数据库dish
        //获取数据库
        DishDatabase dishDatabase = DishDatabase.getDatabase(this);
        DishDao dishDao = dishDatabase.getDishDao();
        PersonDatabase personDatabase = PersonDatabase.getDatabase(this);
        PersonDao personDao = personDatabase.getPersonDao();
        //检测数据库是否为空
        if (dishDao.getDishCount() == 0) {
            //初始化数据库
            initDishDatabase(dishDao);
        }
        //延迟2400ms跳转到登录界面
        postDelayed(() -> {
            Intent intent = new Intent(this, LoginActivity.class).
                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }, 2400);
        Log.d(TAG, "onCreate: " + StringUtil.getCurrentDateAndTime());
        Log.d(TAG, "onCreate: " + StringUtil.getCurrentTime());
    }

    private void initDishDatabase(DishDao dishDao) {
        //插入数据
        dishDao.insert(new Dish(1, getRString(R.string.dish_1), getRString(R.string.desc_1), Double.parseDouble(getRString(R.string.price_1)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(5, getRString(R.string.dish_5), getRString(R.string.desc_5), Double.parseDouble(getRString(R.string.price_5)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(12, getRString(R.string.dish_12), getRString(R.string.desc_12), Double.parseDouble(getRString(R.string.price_12)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(24, getRString(R.string.dish_24), getRString(R.string.desc_24), Double.parseDouble(getRString(R.string.price_24)), getRString(R.string.cate_1), 1, false, false));
        dishDao.insert(new Dish(27, getRString(R.string.dish_27), getRString(R.string.desc_27), Double.parseDouble(getRString(R.string.price_27)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(31, getRString(R.string.dish_31), getRString(R.string.desc_31), Double.parseDouble(getRString(R.string.price_31)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(35, getRString(R.string.dish_35), getRString(R.string.desc_35), Double.parseDouble(getRString(R.string.price_35)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(36, getRString(R.string.dish_36), getRString(R.string.desc_36), Double.parseDouble(getRString(R.string.price_36)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(43, getRString(R.string.dish_43), getRString(R.string.desc_43), Double.parseDouble(getRString(R.string.price_43)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(48, getRString(R.string.dish_48), getRString(R.string.desc_48), Double.parseDouble(getRString(R.string.price_48)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(50, getRString(R.string.dish_50), getRString(R.string.desc_50), Double.parseDouble(getRString(R.string.price_50)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(57, getRString(R.string.dish_57), getRString(R.string.desc_57), Double.parseDouble(getRString(R.string.price_57)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(62, getRString(R.string.dish_62), getRString(R.string.desc_62), Double.parseDouble(getRString(R.string.price_62)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(74, getRString(R.string.dish_74), getRString(R.string.desc_74), Double.parseDouble(getRString(R.string.price_74)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(77, getRString(R.string.dish_77), getRString(R.string.desc_77), Double.parseDouble(getRString(R.string.price_77)), getRString(R.string.cate_1), 1, true, false));
        dishDao.insert(new Dish(2, getRString(R.string.dish_2), getRString(R.string.desc_2), Double.parseDouble(getRString(R.string.price_2)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(4, getRString(R.string.dish_4), getRString(R.string.desc_4), Double.parseDouble(getRString(R.string.price_4)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(8, getRString(R.string.dish_8), getRString(R.string.desc_8), Double.parseDouble(getRString(R.string.price_8)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(23, getRString(R.string.dish_23), getRString(R.string.desc_23), Double.parseDouble(getRString(R.string.price_23)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(28, getRString(R.string.dish_28), getRString(R.string.desc_28), Double.parseDouble(getRString(R.string.price_28)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(44, getRString(R.string.dish_44), getRString(R.string.desc_44), Double.parseDouble(getRString(R.string.price_44)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(45, getRString(R.string.dish_45), getRString(R.string.desc_45), Double.parseDouble(getRString(R.string.price_45)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(47, getRString(R.string.dish_47), getRString(R.string.desc_47), Double.parseDouble(getRString(R.string.price_47)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(51, getRString(R.string.dish_51), getRString(R.string.desc_51), Double.parseDouble(getRString(R.string.price_51)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(59, getRString(R.string.dish_59), getRString(R.string.desc_59), Double.parseDouble(getRString(R.string.price_59)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(64, getRString(R.string.dish_64), getRString(R.string.desc_64), Double.parseDouble(getRString(R.string.price_64)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(68, getRString(R.string.dish_68), getRString(R.string.desc_68), Double.parseDouble(getRString(R.string.price_68)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(84, getRString(R.string.dish_84), getRString(R.string.desc_84), Double.parseDouble(getRString(R.string.price_84)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(86, getRString(R.string.dish_86), getRString(R.string.desc_86), Double.parseDouble(getRString(R.string.price_86)), getRString(R.string.cate_2), 2, false, true));
        dishDao.insert(new Dish(90, getRString(R.string.dish_90), getRString(R.string.desc_90), Double.parseDouble(getRString(R.string.price_90)), getRString(R.string.cate_2), 2, false, false));
        dishDao.insert(new Dish(3, getRString(R.string.dish_3), getRString(R.string.desc_3), Double.parseDouble(getRString(R.string.price_3)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(7, getRString(R.string.dish_7), getRString(R.string.desc_7), Double.parseDouble(getRString(R.string.price_7)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(14, getRString(R.string.dish_14), getRString(R.string.desc_14), Double.parseDouble(getRString(R.string.price_14)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(19, getRString(R.string.dish_19), getRString(R.string.desc_19), Double.parseDouble(getRString(R.string.price_19)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(21, getRString(R.string.dish_21), getRString(R.string.desc_21), Double.parseDouble(getRString(R.string.price_21)), getRString(R.string.cate_3), 3, false, true));
        dishDao.insert(new Dish(22, getRString(R.string.dish_22), getRString(R.string.desc_22), Double.parseDouble(getRString(R.string.price_22)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(29, getRString(R.string.dish_29), getRString(R.string.desc_29), Double.parseDouble(getRString(R.string.price_29)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(30, getRString(R.string.dish_30), getRString(R.string.desc_30), Double.parseDouble(getRString(R.string.price_30)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(37, getRString(R.string.dish_37), getRString(R.string.desc_37), Double.parseDouble(getRString(R.string.price_37)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(39, getRString(R.string.dish_39), getRString(R.string.desc_39), Double.parseDouble(getRString(R.string.price_39)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(46, getRString(R.string.dish_46), getRString(R.string.desc_46), Double.parseDouble(getRString(R.string.price_46)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(49, getRString(R.string.dish_49), getRString(R.string.desc_49), Double.parseDouble(getRString(R.string.price_49)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(54, getRString(R.string.dish_54), getRString(R.string.desc_54), Double.parseDouble(getRString(R.string.price_54)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(55, getRString(R.string.dish_55), getRString(R.string.desc_55), Double.parseDouble(getRString(R.string.price_55)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(61, getRString(R.string.dish_61), getRString(R.string.desc_61), Double.parseDouble(getRString(R.string.price_61)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(63, getRString(R.string.dish_63), getRString(R.string.desc_63), Double.parseDouble(getRString(R.string.price_63)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(70, getRString(R.string.dish_70), getRString(R.string.desc_70), Double.parseDouble(getRString(R.string.price_70)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(71, getRString(R.string.dish_71), getRString(R.string.desc_71), Double.parseDouble(getRString(R.string.price_71)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(78, getRString(R.string.dish_78), getRString(R.string.desc_78), Double.parseDouble(getRString(R.string.price_78)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(80, getRString(R.string.dish_80), getRString(R.string.desc_80), Double.parseDouble(getRString(R.string.price_80)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(81, getRString(R.string.dish_81), getRString(R.string.desc_81), Double.parseDouble(getRString(R.string.price_81)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(85, getRString(R.string.dish_85), getRString(R.string.desc_85), Double.parseDouble(getRString(R.string.price_85)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(87, getRString(R.string.dish_87), getRString(R.string.desc_87), Double.parseDouble(getRString(R.string.price_87)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(91, getRString(R.string.dish_91), getRString(R.string.desc_91), Double.parseDouble(getRString(R.string.price_91)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(93, getRString(R.string.dish_93), getRString(R.string.desc_93), Double.parseDouble(getRString(R.string.price_93)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(94, getRString(R.string.dish_94), getRString(R.string.desc_94), Double.parseDouble(getRString(R.string.price_94)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(95, getRString(R.string.dish_95), getRString(R.string.desc_95), Double.parseDouble(getRString(R.string.price_95)), getRString(R.string.cate_3), 3, false, false));
        dishDao.insert(new Dish(17, getRString(R.string.dish_17), getRString(R.string.desc_17), Double.parseDouble(getRString(R.string.price_17)), getRString(R.string.cate_4), 4, false, true));
        dishDao.insert(new Dish(38, getRString(R.string.dish_38), getRString(R.string.desc_38), Double.parseDouble(getRString(R.string.price_38)), getRString(R.string.cate_4), 4, false, true));
        dishDao.insert(new Dish(56, getRString(R.string.dish_56), getRString(R.string.desc_56), Double.parseDouble(getRString(R.string.price_56)), getRString(R.string.cate_4), 4, false, true));
        dishDao.insert(new Dish(65, getRString(R.string.dish_65), getRString(R.string.desc_65), Double.parseDouble(getRString(R.string.price_65)), getRString(R.string.cate_4), 4, false, true));
        dishDao.insert(new Dish(75, getRString(R.string.dish_75), getRString(R.string.desc_75), Double.parseDouble(getRString(R.string.price_75)), getRString(R.string.cate_4), 4, false, true));
        dishDao.insert(new Dish(82, getRString(R.string.dish_82), getRString(R.string.desc_82), Double.parseDouble(getRString(R.string.price_82)), getRString(R.string.cate_4), 4, false, true));
        dishDao.insert(new Dish(88, getRString(R.string.dish_88), getRString(R.string.desc_88), Double.parseDouble(getRString(R.string.price_88)), getRString(R.string.cate_4), 4, false, true));
        dishDao.insert(new Dish(6, getRString(R.string.dish_6), getRString(R.string.desc_6), Double.parseDouble(getRString(R.string.price_6)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(9, getRString(R.string.dish_9), getRString(R.string.desc_9), Double.parseDouble(getRString(R.string.price_9)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(10, getRString(R.string.dish_10), getRString(R.string.desc_10), Double.parseDouble(getRString(R.string.price_10)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(11, getRString(R.string.dish_11), getRString(R.string.desc_11), Double.parseDouble(getRString(R.string.price_11)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(15, getRString(R.string.dish_15), getRString(R.string.desc_15), Double.parseDouble(getRString(R.string.price_15)), getRString(R.string.cate_5), 5, false, true));
        dishDao.insert(new Dish(20, getRString(R.string.dish_20), getRString(R.string.desc_20), Double.parseDouble(getRString(R.string.price_20)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(25, getRString(R.string.dish_25), getRString(R.string.desc_25), Double.parseDouble(getRString(R.string.price_25)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(26, getRString(R.string.dish_26), getRString(R.string.desc_26), Double.parseDouble(getRString(R.string.price_26)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(32, getRString(R.string.dish_32), getRString(R.string.desc_32), Double.parseDouble(getRString(R.string.price_32)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(41, getRString(R.string.dish_41), getRString(R.string.desc_41), Double.parseDouble(getRString(R.string.price_41)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(52, getRString(R.string.dish_52), getRString(R.string.desc_52), Double.parseDouble(getRString(R.string.price_52)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(58, getRString(R.string.dish_58), getRString(R.string.desc_58), Double.parseDouble(getRString(R.string.price_58)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(60, getRString(R.string.dish_60), getRString(R.string.desc_60), Double.parseDouble(getRString(R.string.price_60)), getRString(R.string.cate_5), 5, false, true));
        dishDao.insert(new Dish(67, getRString(R.string.dish_67), getRString(R.string.desc_67), Double.parseDouble(getRString(R.string.price_67)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(69, getRString(R.string.dish_69), getRString(R.string.desc_69), Double.parseDouble(getRString(R.string.price_69)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(76, getRString(R.string.dish_76), getRString(R.string.desc_76), Double.parseDouble(getRString(R.string.price_76)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(92, getRString(R.string.dish_92), getRString(R.string.desc_92), Double.parseDouble(getRString(R.string.price_92)), getRString(R.string.cate_5), 5, true, false));
        dishDao.insert(new Dish(16, getRString(R.string.dish_16), getRString(R.string.desc_16), Double.parseDouble(getRString(R.string.price_16)), getRString(R.string.cate_6), 6, false, true));
        dishDao.insert(new Dish(33, getRString(R.string.dish_33), getRString(R.string.desc_33), Double.parseDouble(getRString(R.string.price_33)), getRString(R.string.cate_6), 6, false, false));
        dishDao.insert(new Dish(34, getRString(R.string.dish_34), getRString(R.string.desc_34), Double.parseDouble(getRString(R.string.price_34)), getRString(R.string.cate_6), 6, false, false));
        dishDao.insert(new Dish(40, getRString(R.string.dish_40), getRString(R.string.desc_40), Double.parseDouble(getRString(R.string.price_40)), getRString(R.string.cate_6), 6, false, false));
        dishDao.insert(new Dish(42, getRString(R.string.dish_42), getRString(R.string.desc_42), Double.parseDouble(getRString(R.string.price_42)), getRString(R.string.cate_6), 6, false, false));
        dishDao.insert(new Dish(66, getRString(R.string.dish_66), getRString(R.string.desc_66), Double.parseDouble(getRString(R.string.price_66)), getRString(R.string.cate_6), 6, false, false));
        dishDao.insert(new Dish(73, getRString(R.string.dish_73), getRString(R.string.desc_73), Double.parseDouble(getRString(R.string.price_73)), getRString(R.string.cate_6), 6, false, false));
        dishDao.insert(new Dish(79, getRString(R.string.dish_79), getRString(R.string.desc_79), Double.parseDouble(getRString(R.string.price_79)), getRString(R.string.cate_6), 6, false, false));
        dishDao.insert(new Dish(83, getRString(R.string.dish_83), getRString(R.string.desc_83), Double.parseDouble(getRString(R.string.price_83)), getRString(R.string.cate_6), 6, false, false));
        dishDao.insert(new Dish(13, getRString(R.string.dish_13), getRString(R.string.desc_13), Double.parseDouble(getRString(R.string.price_13)), getRString(R.string.cate_7), 7, true, true));
        dishDao.insert(new Dish(18, getRString(R.string.dish_18), getRString(R.string.desc_18), Double.parseDouble(getRString(R.string.price_18)), getRString(R.string.cate_7), 7, true, true));
        dishDao.insert(new Dish(53, getRString(R.string.dish_53), getRString(R.string.desc_53), Double.parseDouble(getRString(R.string.price_53)), getRString(R.string.cate_7), 7, true, true));
        dishDao.insert(new Dish(72, getRString(R.string.dish_72), getRString(R.string.desc_72), Double.parseDouble(getRString(R.string.price_72)), getRString(R.string.cate_7), 7, true, true));
        dishDao.insert(new Dish(89, getRString(R.string.dish_89), getRString(R.string.desc_89), Double.parseDouble(getRString(R.string.price_89)), getRString(R.string.cate_7), 7, true, true));
    }

    private String getRString(@StringRes int id) {
        return getResources().getString(id);
    }

}