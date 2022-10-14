package com.example.Android_bigWork.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.StringUtil;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class FoodStickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Dish> dishList;
    private Resources resources;

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
            this.dishCardView=view.findViewById(R.id.dish_cardView);
            this.count=view.findViewById(R.id.dish_count);
        }
    }

    static class HeaderViewHolder {
        TextView category;

        public HeaderViewHolder(View view) {
            this.category = view.findViewById(R.id.dish_category);
        }
    }

    public FoodStickyAdapter(Context context, List<Dish> dishList) {
        this.context=context;
        this.inflater = LayoutInflater.from(context);
        this.dishList = dishList;
        this.resources = context.getResources();
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
        holder.price.setText(String.valueOf(dish.getPrice()));
        holder.count.setText(String.valueOf(dish.getCount()));
        holder.img.setImageResource(resources.getIdentifier("dish_" + dish.getGID(), "drawable", "com.example.Android_bigWork"));
        // 加号点击事件
        holder.add.setOnClickListener(v -> {
            showDishDetail(dish);
        });
        // 减号点击事件
        holder.sub.setOnClickListener(v -> {
            // TODO: 2022/10/10 将菜从购物车中取出
        });
        // 菜品卡片点击事件
        holder.dishCardView.setOnClickListener(v -> {
            showDishDetail(dish);
        });

        return convertView;
    }

    private void showDishDetail(Dish dish) {
        Log.d("TAG", "showDishDetail: ");
        View contentView =LayoutInflater.from(context).inflate(R.layout.popupwindow_dish_detail, null,false);
        PopupWindow dishDetail= new PopupWindow(contentView,
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
        //设置进入退出的动画，指定刚才定义的style
//        dishDetail.setAnimationStyle(R.style.ipopwindow_anim_style);
//        RecyclerView detailCustom=contentView.findViewById(R.id.custom_list);

        // 绑定视图
        TextView desc=contentView.findViewById(R.id.dish_desctiption);
        TextView name=contentView.findViewById(R.id.dish_name);
        TextView price=contentView.findViewById(R.id.dish_price);
        ImageView img=contentView.findViewById(R.id.dish_img);
        ImageButton add=contentView.findViewById(R.id.dish_add);
        ImageButton sub=contentView.findViewById(R.id.dish_sub);
        ViewStub spicyOption=contentView.findViewById(R.id.spicy_option);
        ViewStub sweetOption=contentView.findViewById(R.id.sweet_option);
        // 设置组件内容、事件
        desc.setText(dish.getDescription());
        name.setText(dish.getName());
        price.setText(String.valueOf(dish.getPrice()));
        img.setImageResource(resources.getIdentifier("dish_" + dish.getGID(), "drawable", "com.example.Android_bigWork"));
        if(dish.isSpicy()){
            try{
                View v= spicyOption.inflate();
            }catch (Exception e){
                spicyOption.setVisibility(View.VISIBLE);
            }
        }
        if(dish.isSweet()){
            try{
                View v= sweetOption.inflate();
            }catch (Exception e){
                sweetOption.setVisibility(View.VISIBLE);
            }
        }
        add.setOnClickListener(v -> {
            Log.d("FoodStickyAdapter", "showDishDetail: add clicked");
        });
        // 显示
        dishDetail.showAtLocation(contentView, Gravity.TOP, 0, 0);

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


    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }


}
