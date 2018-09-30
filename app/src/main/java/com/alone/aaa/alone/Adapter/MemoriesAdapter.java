package com.alone.aaa.alone.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alone.aaa.alone.R;
import com.alone.aaa.alone.db.Memory;

/*
public class MemoriesAdapter extends CursorAdapter {


    public MemoriesAdapter(Context context, Cursor cursor, ViewGroup viewGroup){
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home,viewGroup,false);
        view.setTag(new ViewHolder(view));
        return view;
    }


    public void bindView(View view, Context context, Cursor cursor){
        ViewHolder holder = (ViewHolder)view.getTag();

        Memory memory = new Memory(cursor);

        holder.titleTextView.setText(memory.getTitle());
        holder.imageView.setImageBitmap(memory.getImage());




    }

    private class ViewHolder{
        ImageView imageView;
        TextView titleTextView;

        ViewHolder(View view){
            imageView = view.findViewById(R.id.);
            titleTextView = view.findViewById(R.id.);


        }


    }

}
*/
