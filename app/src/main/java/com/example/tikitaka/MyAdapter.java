package com.example.tikitaka;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<recyclerData> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView_title;
        public TextView textView_date;
        public TextView textView_writer;

        public MyViewHolder(View v) {
            super(v);
            textView_title = (TextView)v.findViewById(R.id.list_TextView_title);
            textView_date = (TextView)v.findViewById(R.id.list_TextView_date);
            textView_writer = (TextView)v.findViewById(R.id.list_TextView_writer);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<recyclerData> myDataset) {

        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listlayout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView_title.setText(mDataset.get(position).title);
        holder.textView_date.setText(mDataset.get(position).date);
        holder.textView_writer.setText(mDataset.get(position).writer);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
class recyclerData{
    public String title,date,writer;
    public recyclerData(String title,String date, String writer){
        this.title=title;
        this.date=date;
        this.writer=writer;
    }
}
