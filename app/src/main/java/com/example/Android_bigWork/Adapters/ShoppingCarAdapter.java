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

import java.util.ArrayList;

public class ShoppingCarAdapter extends RecyclerView.Adapter<ShoppingCarAdapter.ShoppingItemViewHolder> {

    private static final String TAG = "my";
    ArrayList<UserDish> userDishList;
    ArrayList<Dish> dishList;
    Resources resources;
    DishMenuFragment dishMenuFragment;
    Context context;

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
    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // 设定列表项的布局
        View itemView = layoutInflater.inflate(R.layout.item_user_dish, parent, false);
        final ShoppingItemViewHolder holder = new ShoppingItemViewHolder(itemView);
        // TODO: 2022/10/14 设置监听器
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
        Log.d(TAG, "onCreateViewHolder: ");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, int position) {
        final UserDish userDish = userDishList.get(position);
        // 设置Tag，给onCreateViewHolder方法提供数据操作对象
        holder.itemView.setTag(R.id.tagForUserDish,userDish);
        // 设置视图
        holder.name.setText(userDish.getName());
        holder.price.setText(String.valueOf(userDish.getPrice()));
        holder.count.setText(String.valueOf(userDish.getCount()));
        holder.customText.setText(userDish.getCustomText());
        holder.img.setImageResource(resources.getIdentifier("dish_" + userDish.getGID(), "drawable", "com.example.Android_bigWork"));
//        Log.d(TAG, "onBindViewHolder: " + position + "--userDish:" + userDish.getName());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + userDishList.size());
        return userDishList.size();
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
        ImageButton add;
        ImageButton sub;
        ImageView img;

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
