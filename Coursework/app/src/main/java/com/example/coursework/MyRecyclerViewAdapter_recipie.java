package com.example.coursework;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyRecyclerViewAdapter_recipie extends RecyclerView.Adapter<MyRecyclerViewAdapter_recipie.ViewHolder> {

    private List<String> mItem;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter_recipie.ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapter_recipie(Context context, List<String> item) {
        this.mInflater = LayoutInflater.from(context);
        this.mItem = item;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyRecyclerViewAdapter_recipie.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerviewrecipie_row, parent, false);
        return new MyRecyclerViewAdapter_recipie.ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter_recipie.ViewHolder holder, int position) {
        String item = mItem.get(position);
        holder.myTextView1.setText(item);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mItem.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView1;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView1 = itemView.findViewById(R.id.recipieItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick_list(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mItem.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(MyRecyclerViewAdapter_recipie.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick_list(View view, int position);
    }
}
