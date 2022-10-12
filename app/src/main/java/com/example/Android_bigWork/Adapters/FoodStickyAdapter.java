package com.example.Android_bigWork.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.R;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class FoodStickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private LayoutInflater inflater;
    private List<Dish> dishList;
    private Resources resources ;

    public FoodStickyAdapter(Context context, List<Dish> dishList) {
        inflater = LayoutInflater.from(context);
        this.dishList = dishList;
        this.resources=context.getResources();
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
        headerViewHolder.category.setText(dishList.get(position).getCategory());
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
        holder.price.setText(String.valueOf(dish.getPrice()));
//        holder.img.setImageResource(R.drawable.dish_1);
        holder.img.setImageResource(resources.getIdentifier("dish_1","drawable","com.example.Android_bigWork"));
        holder.add.setOnClickListener(v -> {
            // TODO: 2022/10/10 将菜加入购物车
        });
        holder.sub.setOnClickListener(v -> {
            // TODO: 2022/10/10 将菜从购物车中取出
        });

        return convertView;
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

    static class ViewHolder {
        TextView name;
        TextView price;
        ImageButton add;
        ImageButton sub;
        ImageView img;

        public ViewHolder(View view) {
            this.name = view.findViewById(R.id.dish_name);
            this.price = view.findViewById(R.id.dish_price);
            this.add = view.findViewById(R.id.dish_add);
            this.sub = view.findViewById(R.id.dish_sub);
            this.img=view.findViewById(R.id.dish_img);
        }
        public ViewHolder() {}
    }

    static class HeaderViewHolder {
        TextView category;

        public HeaderViewHolder(View view) {
            this.category = (TextView) view.findViewById(R.id.dish_category);
        }
    }
}
