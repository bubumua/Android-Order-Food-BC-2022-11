package com.example.Android_bigWork.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Android_bigWork.Entity.Coupon;
import com.example.Android_bigWork.R;
import com.example.Android_bigWork.Utils.StringUtil;

import java.util.List;
import java.util.Locale;

/**
 * @author Bubu
 * @Type CouponAdapter
 * @Desc 优惠券下拉框适配器
 * @date 2022/10/26 14:42
 */
public class CouponAdapter extends BaseAdapter {
    Context context;
    List<Coupon> coupons;
    Resources resources;

    public CouponAdapter(Context context, List<Coupon> coupons) {
        this.context = context;
        this.coupons = coupons;
        resources = context.getResources();
    }

    @Override
    public int getCount() {
        if (coupons != null) {
            return coupons.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return coupons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return coupons.get(position).CID;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CouponViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_coupon, parent, false);
            holder = new CouponViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CouponViewHolder) convertView.getTag();
        }
        // 获取Coupon对象
        Coupon coupon = (Coupon) getItem(position);
        // 根据Coupon对象，设置视图
        double discount = coupon.getDiscount();
        double condition = coupon.getCondition();
        double reduction = coupon.getReduction();
        String language = Locale.getDefault().getLanguage();
        Log.d("my", "getView: language=" + language);
        if (language.equals("CN") || language.equals("zh")) {
            switch (coupon.getType()) {
                case 0:
                    holder.textView.setText(discount + "折");
                    break;
                case 1:
                    holder.textView.setText("满" + condition + "减" + reduction);
                    break;
                default:
                    break;
            }
        } else {
            switch (coupon.getType()) {
                case 0:
                    holder.textView.setText((10 - discount) * 10 + "% OFF");
                    break;
                case 1:
                    holder.textView.setText("Over" + condition + "Minus" + reduction);
                    break;
                default:
                    break;
            }
        }

        return convertView;
    }

    static class CouponViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public CouponViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_coupon_detail);
        }
    }
}
