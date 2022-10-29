package com.example.Android_bigWork.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.Entity.UserDish;
import com.example.Android_bigWork.Fragments.DishMenuFragment;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * @Type FoodStickyAdapter
 * @Desc 用于菜品显示的适配器
 * @author Bubu
 * @date 2022/10/29 11:55
 * @version
 */
public class FoodStickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private static final String TAG = "my";
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Dish> dishList;
    private Resources resources;
    private ArrayList<UserDish> userDishList;
    private String userName;
    private DishMenuFragment dishMenuFragment;

    static class ViewHolder {
        TextView name;
        TextView price;
        TextView count;
        ImageButton add;
        ImageButton sub;
        ImageView img;
        CardView dishCardView;

        public ViewHolder(View view) {
            this.name = view.findViewById(R.id.dish_name);
            this.price = view.findViewById(R.id.dish_price);
            this.add = view.findViewById(R.id.dish_add);
            this.sub = view.findViewById(R.id.dish_sub);
            this.img = view.findViewById(R.id.dish_img);
            this.dishCardView = view.findViewById(R.id.dish_cardView);
            this.count = view.findViewById(R.id.dish_count);
        }
    }

    static class HeaderViewHolder {
        TextView category;

        public HeaderViewHolder(View view) {
            this.category = view.findViewById(R.id.dish_category);
        }
    }

    public FoodStickyAdapter(Context context, DishMenuFragment dishMenuFragment, ArrayList<Dish> dishList, ArrayList<UserDish> userDishList, String userName) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dishList = dishList;
        this.resources = context.getResources();
        this.userDishList = userDishList;
        this.userName = userName;
        this.dishMenuFragment = dishMenuFragment;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder headerViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_header, null);
            headerViewHolder = new HeaderViewHolder(convertView);
            convertView.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }
        headerViewHolder.category.setText(StringUtil.replaceToBlank(dishList.get(position).getCategory()));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return this.dishList.get(position).getCID();
    }

    @Override
    public int getCount() {
        return dishList.size();
    }

    @Override
    public Object getItem(int position) {
        return dishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dishList.get(position).getGID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_dish, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 在视图上设置文本、图片
        Dish dish = dishList.get(position);
        holder.name.setText(dish.getName());
//        holder.price.setText(String.valueOf(dish.getPrice()));
        holder.price.setText(StringUtil.getSSMoney(dish.getPrice(), 54));
        holder.count.setText(String.valueOf(dish.getCount()));
        holder.img.setImageResource(resources.getIdentifier("dish_" + dish.getGID(), "drawable", "com.example.Android_bigWork"));
        // 加号点击事件
        holder.add.setOnClickListener(v -> {
            showDishDetail(dish);
        });
        // 减号点击事件
        holder.sub.setOnClickListener(v -> {
            if (dish.getCount() == 1) {
                // 数据层-1
                dish.setCount(dish.getCount() - 1);
                // 视图层-1
//                TextView count_sub = contentView.findViewById(R.id.dish_count);
                holder.count.setText(String.valueOf(dish.getCount()));
                // 从购物车中移除
                removeSingleDishFromShoppingCar(dish);
                // 通知视图改变
                notifyDataSetChanged();
            }
            if (dish.getCount() > 1) {
                dishMenuFragment.showShoppingCar();
            }

        });
        // 菜品卡片点击事件
        holder.dishCardView.setOnClickListener(v -> {
            showDishDetail(dish);
        });

        return convertView;
    }

    private PopupWindow showDishDetail(Dish dish) {
        Log.d(TAG, "showDishDetail: dish.count="+dish.getCount());
        View contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_dish_detail, null, false);
        // 创建弹窗
        PopupWindow dishDetail = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 取得焦点
        dishDetail.setFocusable(true);
        //注意：要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
//        dishDetail.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //点击外部消失
        dishDetail.setOutsideTouchable(true);
        //设置可以点击
        dishDetail.setTouchable(true);
        //设置进入退出的动画
        dishDetail.setAnimationStyle(R.style.dishDetail_anim_style);
        // 绑定视图
        TextView desc = contentView.findViewById(R.id.dish_desctiption);
        TextView name = contentView.findViewById(R.id.dish_name);
        TextView price = contentView.findViewById(R.id.dish_price);
        TextView count= contentView.findViewById(R.id.dish_count);
        ImageView img = contentView.findViewById(R.id.dish_img);
        ImageButton add = contentView.findViewById(R.id.dish_add);
        ImageButton sub = contentView.findViewById(R.id.dish_sub);
        ViewStub spicyOption = contentView.findViewById(R.id.spicy_option);
        ViewStub sweetOption = contentView.findViewById(R.id.sweet_option);

        // 设置组件内容、事件
        desc.setText(dish.getDescription());
        name.setText(dish.getName());
        price.setText(String.valueOf(dish.getPrice()));
        count.setText(String.valueOf(dish.getCount()));
        img.setImageResource(resources.getIdentifier("dish_" + dish.getGID(), "drawable", "com.example.Android_bigWork"));
        // 辣度和甜度
        final int[] spicy = {0};
        final int[] sweet = {0};
        final String[] spicyStr = {getRString(R.string.defaultValue)};
        final String[] sweetStr = {getRString(R.string.defaultValue)};
        // 判断是否显示口味选项
        // 若有辣度选项，展开辣度单选题
        if (dish.isSpicy()) {
            try {
                View v = spicyOption.inflate();
                RadioGroup spicyRadioGroup = v.findViewById(R.id.spicy_RadioGroup);
                // 默认选择不辣
                spicy[0] = 1;
                spicyStr[0] = getRString(R.string.spicy_1);
                // 设置单选框监听器
                spicyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.RadioButton1:
                                spicy[0] = 1;
                                spicyStr[0] = getRString(R.string.spicy_1);
                                break;
                            case R.id.RadioButton2:
                                spicy[0] = 2;
                                spicyStr[0] = getRString(R.string.spicy_2);
                                break;
                            case R.id.RadioButton3:
                                spicy[0] = 3;
                                spicyStr[0] = getRString(R.string.spicy_3);
                                break;
                            case R.id.RadioButton4:
                                spicy[0] = 4;
                                spicyStr[0] = getRString(R.string.spicy_4);
                                break;
                            default:
                                break;
                        }

                    }
                });
            } catch (Exception e) {
                spicyOption.setVisibility(View.VISIBLE);
            }
        }
        // 若有甜度选项，展开甜度单选题
        if (dish.isSweet()) {
            try {
                View v = sweetOption.inflate();
                RadioGroup sweetRadioGroup = v.findViewById(R.id.sweet_RadioGroup);
                // 默认0分甜
                sweet[0] = 1;
                sweetStr[0] = getRString(R.string.sweet_1);
                // 设置单选框监听器
                sweetRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.RadioButton1:
                                sweet[0] = 1;
                                sweetStr[0] = getRString(R.string.sweet_1);
                                break;
                            case R.id.RadioButton2:
                                sweet[0] = 2;
                                sweetStr[0] = getRString(R.string.sweet_2);
                                break;
                            case R.id.RadioButton3:
                                sweet[0] = 3;
                                sweetStr[0] = getRString(R.string.sweet_3);
                                break;
                            case R.id.RadioButton4:
                                sweet[0] = 4;
                                sweetStr[0] = getRString(R.string.sweet_4);
                                break;
                            default:
                                break;
                        }
                    }
                });
            } catch (Exception e) {
                sweetOption.setVisibility(View.VISIBLE);
            }
        }
        // detail加号点击事件
        add.setOnClickListener(v -> {
            Log.d(TAG, "showDishDetail: add clicked");
            // 将自定义口味拼接为一个字符串
            ArrayList<String> customList=new ArrayList<>();
            if(spicy[0]>0 ){
                if(!Objects.equals(spicyStr[0], getRString(R.string.defaultValue))){
                    customList.add(spicyStr[0]);
                    Log.d(TAG, "add value: "+spicyStr[0]);

                }else {
                    customList.add(getRString(R.string.defaultValue));
                    Log.d(TAG, "add default: ");
                }
            }
            if(sweet[0]>0 ){
                if(!Objects.equals(sweetStr[0], getRString(R.string.defaultValue))){
                    customList.add(sweetStr[0]);
                    Log.d(TAG, "add value: "+sweetStr[0]);

                }else {
                    customList.add(getRString(R.string.defaultValue));
                    Log.d(TAG, "add default: ");

                }
            }
            String customText= StringUtil.join(customList,",");
            // 数据层上，将菜加入购物车
            addDishToShoppingCar(dish, spicy[0], sweet[0], customText);
            dish.setCount(dish.getCount() + 1);
//            TextView count_add = contentView.findViewById(R.id.dish_count);
            count.setText(String.valueOf(dish.getCount()));
            dishMenuFragment.updateShoppingCarAccount();
            notifyDataSetChanged();
            // 点击加号后，新建一个弹窗作为动画
            PopupWindow temp= showDishDetail(dish);
            new Handler().postDelayed(() -> {
                Log.d(TAG, "run: delay dismiss");
                temp.dismiss();
            }, 300);
        });
        // detail减号点击事件
        sub.setOnClickListener(v -> {
            Log.d(TAG, "showDishDetail: dishCount="+dish.getCount());
            if (dish.getCount() == 1) {
                // 数据层-1
                dish.setCount(dish.getCount() - 1);
                // 视图层-1
//                TextView count_sub = contentView.findViewById(R.id.dish_count);
                count.setText(String.valueOf(dish.getCount()));
                // 从购物车中移除
                removeSingleDishFromShoppingCar(dish);
                // 通知视图改变
                notifyDataSetChanged();
            }
            if (dish.getCount() > 1) {
                dishDetail.dismiss();
                dishMenuFragment.showShoppingCar();
            }
        });
        // 显示
        dishDetail.showAtLocation(contentView, Gravity.CENTER, 0, 0);
return dishDetail;
    }

    private void removeSingleDishFromShoppingCar(Dish dish) {
        for(UserDish ud:userDishList){
            if(ud.getGID()==dish.getGID()){
                userDishList.remove(ud);
                break;
            }
        }
        dishMenuFragment.updateShoppingCarAccount();
    }

    public int getPositionByCID(int CID) {
        for (int i = 0; i < dishList.size(); i++) {
            Dish dish = dishList.get(i);
            if (CID == dish.getCID()) {
                return i;
            }
        }
        return 0;
    }


    public void addDishToShoppingCar(Dish dish, int spicy, int sweet,String customText) {
        UserDish userDish = new UserDish(
                dish.getGID(),
                dish.getName(),
                dish.getDescription(),
                dish.getPrice(),
                dish.getCategory(),
                dish.getCID(),
                spicy,
                sweet,
                customText,
                1,
                userName);
        // 如果购物车没有菜，直接添加
        if (userDishList.size() == 0) {
            userDishList.add(userDish);
        }
        // 如果有菜，判断是否有相同的。有则数量、价格改变；没有则添加新菜
        else {
            boolean existSameUserDish = false;
            for (UserDish ud : userDishList) {
                if (ud.equals(userDish)) {
                    existSameUserDish = true;
                    ud.setCount(ud.getCount() + 1);
                    ud.setPrice(ud.getPrice() + userDish.getPrice());
                    break;
                }
            }
            if (!existSameUserDish) {
                userDishList.add(userDish);
            }
        }
        dishMenuFragment.setUserDishList(userDishList);
        Log.d(TAG, "addDishToShoppingCar: userDishList length=" + userDishList.size());
    }

    int transformDishGID(Dish dish, int spicy, int sweet) {
        return dish.getGID() * 100 + spicy * 10 + sweet;
    }

    public Resources getResources() {
        return resources;
    }

    private String getRString(int id) {
        return getResources().getString(id);
    }
}
