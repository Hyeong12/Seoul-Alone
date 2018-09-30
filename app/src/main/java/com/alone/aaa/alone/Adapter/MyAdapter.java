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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<MyData> mDataset;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public RelativeLayout mLayout;

        public ViewHolder(View view){
            super(view);
            mLayout = (RelativeLayout)view.findViewById(R.id.parent_layout);
            mImageView = (ImageView)view.findViewById(R.id.image);
            mTextView1 = (TextView)view.findViewById(R.id.textview1) ;
            mTextView2 = (TextView)view.findViewById(R.id.textview2) ;


        }


    }


    public MyAdapter(Context context, ArrayList<MyData> myDataset){
        this.context = context;
        mDataset = myDataset;
    }





    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    public void onBindViewHolder(ViewHolder holder, final int position){

        //Test a = new Test();


        holder.mTextView1.setText(mDataset.get(position).name);
        holder.mTextView2.setText(mDataset.get(position).addr);
        holder.mLayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(context,HomeActivity.class);
                intent.putExtra("store_name",mDataset.get(position).name);

                context.startActivity(intent);



            }
        });





        Picasso.with(context).load(mDataset.get(position).img).placeholder(R.drawable.error).
               into(holder.mImageView);

    }



    public int getItemCount(){
        return mDataset.size();
    }


}







