package com.example.Android_bigWork.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Android_bigWork.Entity.Dish;
import com.example.Android_bigWork.R;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.CustomItemViewHolder> {

    private Dish dish;

    public CustomListAdapter(Dish dish) {
        this.dish = dish;
    }

    @NonNull
    @Override
    public CustomItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        CustomItemViewHolder holder=new CustomItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.))

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class CustomItemViewHolder extends RecyclerView.ViewHolder {

        public CustomItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
