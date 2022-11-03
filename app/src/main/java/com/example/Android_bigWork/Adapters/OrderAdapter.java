package com.example.Android_bigWork.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Android_bigWork.Entity.UserDish;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.BaseDialog;
import com.example.Android_bigWork.Utils.StringUtil;
import com.example.Android_bigWork.ViewModels.OrderViewModel;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * @Type OrderAdapter
 * @Desc 订单栏适配器
 * @author Bubu
 * @date 2022/10/26 14:42
 */
public class OrderAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private static final String TAG = "my";
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_EMPTY = 0;
    public int viewType;
    private LayoutInflater inflater;
    Context context;
    OrderViewModel orderViewModel;
    Resources resources;
    List<UserDish> userDishList;

    public OrderAdapter(Context context, OrderViewModel orderViewModel) {
        this.context = context;
        this.orderViewModel = orderViewModel;
        this.resources = context.getResources();
        this.inflater = LayoutInflater.from(context);
        this.userDishList = orderViewModel.getUserDishes().getValue();
        this.viewType=0;
    }

    public void setUserDishList(List<UserDish> userDishList) {
        this.userDishList = userDishList;
        if(userDishList.size()>0){
            viewType=1;
        }else {
            viewType=0;
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder headerViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_order_header, null);
            headerViewHolder = new HeaderViewHolder(convertView);
            convertView.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }
        headerViewHolder.createdTime.setText(StringUtil.getCurrentTimeByMills(userDishList.get(position).getCreatedTime()));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return userDishList.get(position).getCreatedTime();
    }

    @Override
    public int getCount() {
        if (userDishList == null) {
            return 0;
        } else {
            return userDishList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return userDishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userDishList.get(position).getGID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_order, parent, false);
            holder = new OrderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (OrderViewHolder) convertView.getTag();
        }
        UserDish userDish = (UserDish) getItem(position);
        // 设置视图
        holder.name.setText(userDish.getName());
        holder.price.setText(String.valueOf(userDish.getPrice()));
        holder.count.setText(String.valueOf(userDish.getCount()));
        holder.customText.setText(userDish.getCustomText());
        holder.img.setImageResource(resources.getIdentifier("dish_" + userDish.getGID(), "drawable", "com.example.Android_bigWork"));
        return convertView;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        TextView count;
        TextView customText;
        ImageView img;

        public OrderViewHolder(@NonNull View view) {
            super(view);
            this.name = view.findViewById(R.id.dish_name);
            this.price = view.findViewById(R.id.dish_price);
            this.img = view.findViewById(R.id.dish_img);
            this.count = view.findViewById(R.id.dish_count);
            this.customText = view.findViewById(R.id.dish_custom);
        }
    }

    static class HeaderViewHolder {
        TextView createdTime;

        public HeaderViewHolder(View view) {
            this.createdTime = view.findViewById(R.id.tv_createdTime);
        }
    }
}
