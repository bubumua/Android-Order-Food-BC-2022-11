package com.example.Android_bigWork.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.Entity.UserDish;
import com.example.Android_bigWork.Fragments.DishMenuFragment;
import com.example.Android_bigWork.R;

import java.security.AlgorithmConstraints;
import java.util.ArrayList;


/**
 * @Type ShoppingCarAdapter
 * @Desc 购物车 RecyclerView的适配器。注意这里的写法：若列表itemCount==0，设置ViewType为空。利用继承关系，根据ViewType，返回不同的ViewHolder。只有当返回我们自己的ViewHolder时，才设置对应视图。
 * @author Bubu
 * @date 2022/10/16 16:17
 * @version
 */
public class ShoppingCarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "my";
    ArrayList<UserDish> userDishList;
    ArrayList<Dish> dishList;
    Resources resources;
    DishMenuFragment dishMenuFragment;
    Context context;
    // viewType: 1--normal item, 0--empty item
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_EMPTY = 0;

    public ShoppingCarAdapter(Context context, DishMenuFragment dishMenuFragment, ArrayList<UserDish> userDishList, ArrayList<Dish> dishList) {
        this.userDishList = userDishList;
        this.resources = context.getResources();
        this.context = context;
        this.dishMenuFragment = dishMenuFragment;
        this.dishList = dishList;
        Log.d(TAG, "ShoppingCarAdapter: userDishList length=" + userDishList.size());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        // 在这里根据不同的viewType进行引入不同的布局
        if (viewType == VIEW_TYPE_EMPTY) {
            View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_car_empty, parent, false);
            return new RecyclerView.ViewHolder(emptyView){};
        }
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // 设定列表项的布局
        View itemView = layoutInflater.inflate(R.layout.item_user_dish, parent, false);
        final ShoppingItemViewHolder holder = new ShoppingItemViewHolder(itemView);
        // 设置监听器
        // 加号事件
        holder.add.setOnClickListener(v -> {
            UserDish userDish=(UserDish) holder.itemView.getTag(R.id.tagForUserDish);
            userDish.setPrice(userDish.getPrice()+userDish.getPrice()/userDish.getCount());
            userDish.setCount(userDish.getCount()+1);
            holder.count.setText(String.valueOf(userDish.getCount()));
            holder.price.setText(String.valueOf(userDish.getPrice()));
            ((FoodStickyAdapter)dishMenuFragment.getStickyListView().getAdapter()).notifyDataSetChanged();
            updateShoppingCarAccount();
        });
        // 减号事件
        holder.sub.setOnClickListener(v -> {
            UserDish userDish=(UserDish) holder.itemView.getTag(R.id.tagForUserDish);
            userDish.setPrice(userDish.getPrice()-userDish.getPrice()/userDish.getCount());
            userDish.setCount(userDish.getCount()-1);
            // 若菜品数量降为0，则从购物车中删除
            for (int i = 0; i < userDishList.size(); i++) {
                if(userDishList.get(i).getCount()==0){
                    userDishList.remove(i);
                    break;
                }
            }
            // 修改dishList数据
            for (Dish dish :dishList) {
                if (dish.getGID() == userDish.getGID()) {
                    dish.setCount(dish.getCount()-1);
                    break;
                }
            }
            // 通知菜品栏数据变更
            ((FoodStickyAdapter)dishMenuFragment.getStickyListView().getAdapter()).notifyDataSetChanged();
            // 通知购物车数据变更
            notifyDataSetChanged();
            // 更新购物车金额
            updateShoppingCarAccount();
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ShoppingItemViewHolder){
            ShoppingItemViewHolder hd=(ShoppingItemViewHolder)holder;
            final UserDish userDish = userDishList.get(position);
            // 设置Tag，给onCreateViewHolder方法提供数据操作对象
            holder.itemView.setTag(R.id.tagForUserDish,userDish);
            // 设置视图
            hd.name.setText(userDish.getName());
            hd.price.setText(String.valueOf(userDish.getPrice()));
            hd.count.setText(String.valueOf(userDish.getCount()));
            hd.customText.setText(userDish.getCustomText());
            hd.img.setImageResource(resources.getIdentifier("dish_" + userDish.getGID(), "drawable", "com.example.Android_bigWork"));
//        Log.d(TAG, "onBindViewHolder: " + position + "--userDish:" + userDish.getName());
        }
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: " + userDishList.size());
        // 列表为空返回 1 是为了显示 empty view，若为0，则不会显示任何对象
        if(userDishList.size()==0){
            return 1;
        }
        return userDishList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        Log.d(TAG, "getItemViewType: "+userDishList.size());
        if(userDishList.size()==0){
            return VIEW_TYPE_EMPTY;
        }else {
            return VIEW_TYPE_ITEM;
        }
    }

    /**
     * 遍历购物车，更新购物车金额
     *
     * @return void
     * @Author Bubu
     * @date 2022/10/14 21:34
     * @commit
     */
    private void updateShoppingCarAccount(){
        double total=0;
        for (UserDish ud:userDishList){
            total+=ud.getPrice();
        }
        dishMenuFragment.setShoppingCarAccount(total);
    }

    static class ShoppingItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        TextView count;
        TextView customText;
        ImageView img;
        ImageButton add;
        ImageButton sub;

        public ShoppingItemViewHolder(@NonNull View view) {
            super(view);
            this.name = view.findViewById(R.id.dish_name);
            this.price = view.findViewById(R.id.dish_price);
            this.add = view.findViewById(R.id.dish_add);
            this.sub = view.findViewById(R.id.dish_sub);
            this.img = view.findViewById(R.id.dish_img);
            this.count = view.findViewById(R.id.dish_count);
            this.customText = view.findViewById(R.id.dish_custom);
        }
    }
}
