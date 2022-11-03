package com.example.Android_bigWork.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.R;
import com.youth.banner.adapter.BannerAdapter;


import java.util.List;

/**
 * @Type ImageAdapter
 * @Desc 滚动推荐栏适配器
 * @author Bubu
 * @date 2022/10/29 14:42
 */
public class ImageAdapter extends BannerAdapter<Dish,ImageAdapter.BannerViewHolder> {
    private List<Dish> recommends;
    private Context context;
    private Resources resources;

    public ImageAdapter(List<Dish> mData, Context context) {
        super(mData);
        recommends=mData;
        this.context=context;
        this.resources=context.getResources();
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.item_banner_recommend,parent,false);
        itemView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        // itemView.findViewById(R.id.imageView8)
        BannerViewHolder holder=new BannerViewHolder(itemView);
        ImageView imageView=new ImageView(parent.getContext());
        // 根据viewpager2强制要求，设置为匹配父元素
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        return new BannerViewHolder(imageView);
        return holder;
    }

    @Override
    public void onBindView(BannerViewHolder holder, Dish dish, int position, int size) {
        holder.img.setImageResource(resources.getIdentifier("dish_" + dish.getGID(), "drawable", "com.example.Android_bigWork"));
        holder.name.setText(dish.getName());
        holder.desc.setText(dish.getDescription());
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;
        TextView desc;


        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.img =itemView.findViewById(R.id.imageView7);
//            img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            this.name=itemView.findViewById(R.id.dish_name);
            this.desc=itemView.findViewById(R.id.dish_desc);

        }
    }
}
