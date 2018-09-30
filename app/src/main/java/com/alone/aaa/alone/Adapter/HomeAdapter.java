package com.alone.aaa.alone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alone.aaa.alone.Activity.HomeActivity;
import com.alone.aaa.alone.MyData;
import com.alone.aaa.alone.R;
import com.alone.aaa.alone.RepeatData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<RepeatData> mDataset;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public RelativeLayout mLayout;

        public ViewHolder(View view){
            super(view);
            //mLayout = (RelativeLayout)view.findViewById(R.id.parent_layout);
            mTextView1 = (TextView)view.findViewById(R.id.txt_id) ;
            mTextView2 = (TextView)view.findViewById(R.id.txt_context) ;
            mTextView3 = (TextView)view.findViewById(R.id.txt_date);


        }


    }


    public HomeAdapter(Context context, ArrayList<RepeatData> myDataset){
        this.context = context;
        mDataset = myDataset;
    }





    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_home, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    public void onBindViewHolder(ViewHolder holder, final int position){

        //Test a = new Test();


        holder.mTextView1.setText(mDataset.get(position).userID);
        holder.mTextView2.setText(mDataset.get(position).comment);
        holder.mTextView3.setText(mDataset.get(position).mDate);



    }



    public int getItemCount(){
        return mDataset.size();
    }


}
