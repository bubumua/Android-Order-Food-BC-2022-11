package com.example.Android_bigWork.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Android_bigWork.R;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * @Type FoodCategoryAdapter
 * @Desc 菜品类别适配器
 * @author Bubu
 * @date 2022/10/16 14:42
 */
public class FoodCategoryAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<CategoryItem> categoryItems;
    private int selectedPosition;

    public FoodCategoryAdapter(Context context, ArrayList<CategoryItem> categoryItems) {
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
        this.categoryItems=categoryItems;
        this.selectedPosition=0;
    }

    @Override
    public int getCount() {
        return categoryItems.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_category,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设置视图
        viewHolder.categoryNameTextView.setText(categoryItems.get(position).getCategoryName());
        viewHolder.categoryCountTextView.setText(String.valueOf(categoryItems.get(position).getCategoryCount()));
        viewHolder.categoryCountTextView.setVisibility(categoryItems.get(position).getCategoryCount() == 0 ? View.GONE : View.VISIBLE);
        viewHolder.categoryPointer.setVisibility(position==selectedPosition ? View.VISIBLE : View.INVISIBLE);
        return convertView;
    }

    public void updateCategorySelectionByPosition(int position){
        this.selectedPosition=position;
        notifyDataSetChanged();
    }

    public void updateCategorySelectionByCID(int CID){
        for (int i = 0; i < categoryItems.size(); i++) {
            if (CID==categoryItems.get(i).getCID()){
                updateCategorySelectionByPosition(i);
                break;
            }
        }
//        Log.d("FoodCategoryAdapter", "updateCategorySelectionByCID: no such CID");
    }

    static class ViewHolder {

        TextView categoryNameTextView;
        TextView categoryCountTextView;
        ImageView categoryPointer;

        public ViewHolder(View view) {
            categoryNameTextView = view.findViewById(R.id.categoryText);
            categoryCountTextView = view.findViewById(R.id.categoryCountText);
            categoryPointer=view.findViewById(R.id.categoryPointer);
        }
    }

    public static class CategoryItem{

        String categoryName;
        int CID;
        int categoryCount;

        public CategoryItem(String name,int CID) {
            setCategoryName(name);
            setCID(CID);
            setCategoryCount(0);
        }

        public int getCID() {
            return CID;
        }

        public void setCID(int CID) {
            this.CID = CID;
        }

        public CategoryItem(String categoryName,int CID, int categoryCount) {
            this.categoryName = categoryName;
            this.CID=CID;
            this.categoryCount = categoryCount;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public int getCategoryCount() {
            return categoryCount;
        }

        public void setCategoryCount(int categoryCount) {
            this.categoryCount = categoryCount;
        }
    }
}
