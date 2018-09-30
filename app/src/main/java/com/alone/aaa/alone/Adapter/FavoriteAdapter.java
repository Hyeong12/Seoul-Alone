package com.alone.aaa.alone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alone.aaa.alone.Activity.HomeActivity;
import com.alone.aaa.alone.MyData;
import com.alone.aaa.alone.R;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private ArrayList<MyData> mDataset;
    public ArrayList<MyData> checkedDataset = new ArrayList<>();
    //ArrayList<Integer> index = new ArrayList<>();

    private Context context;




    interface ItemClickListener{
        void onItemCheck(View v, int pos);
    }




    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public LinearLayout mLayout;
        public Button mShare;
        public Button mDelete;



        public ViewHolder(View view) {
            super(view);
            mLayout = (LinearLayout) view.findViewById(R.id.parent_layout1);
            mImageView = (ImageView) view.findViewById(R.id.image1);
            mTextView1 = (TextView) view.findViewById(R.id.text_view1);
            mTextView2 = (TextView) view.findViewById(R.id.text_view2);
            mShare = (Button) view.findViewById(R.id.btnShare);
            mDelete = (Button) view.findViewById(R.id.btnDelete);
            //mCheckBox = (CheckBox)view.findViewById(R.id.check_box);

            // mCheckBox.setOnClickListener(this);


        }
       /* public void setItemCheckListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }

        public void onClick(View v){

            this.itemClickListener.onItemCheck(v,getLayoutPosition());

        }*/
    }





    public FavoriteAdapter(Context context, ArrayList<MyData> myDataset){
        this.context = context;
        mDataset = myDataset;


    }





    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_fv, parent, false);

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
        holder.mDelete.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                // 정말 지울것인지 창 띄우기.

                delete_Item(mDataset.get(position).ID,mDataset.get(position).name);
                mDataset.remove(position);



                notifyDataSetChanged();


            }



        });

        holder.mShare.setOnClickListener(new View.OnClickListener(){


            public void onClick(View v){

                Intent shareIntent = new Intent();

                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,"your text");
                shareIntent.setType("text/plain");
                context.startActivity(shareIntent);

                //sharedKakao(v,mDataset,position);


            }


        });

        /*holder.setItemCheckListener(new ItemClickListener() {
            @Override
            public void onItemCheck(View v, int pos) {
                CheckBox chk = (CheckBox) v;

                if(chk.isChecked()) {

                    checkedDataset.add(mDataset.get(pos));
                    index.add(pos);
                    Log.e("Checked is",(mDataset.get(pos).name));
                    Log.e("position",String.valueOf(pos));
                    //Log.e("position is",);
                }else if(!chk.isChecked()) {
                    checkedDataset.remove(mDataset.get(pos));
                    index.remove(pos);
                    Log.e("unChecked is",(mDataset.get(pos).name));
                }


            }


        });
*/


        Picasso.with(context).load(mDataset.get(position).img).placeholder(R.drawable.error).
                into(holder.mImageView);

    }

   /* public ArrayList<Integer> getChecked(){

        return index ;
    }*/

    public int getItemCount(){
        return mDataset.size();
    }

    public void delete_Item(String userID, String name){
        BackTask connection = new BackTask("http://192.168.43.170:8090/sample/favorite_remove.jsp");

        try {

            String result = connection.execute(userID, name).get(); // result = true or false

        }catch(Exception e){

            e.printStackTrace();

        }



    }
    class BackTask extends AsyncTask<String, Void, String> {
        // class의 목적 : 각각의 url에 저장된 이미지 파일의 정보를 가져오기 위함.
        String URL;

        public BackTask(String URL){
            this.URL = URL;

        }
        protected String doInBackground(String... strings){
            String sendMsg;
            String rs = null;


            try{
                String str;

                java.net.URL url = new URL(URL);
                // ex) http://localhost:8090/sample/1.jpg,


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                //Log.e("string",strings[0]);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "userID="+strings[0]+"&name="+strings[1];
                // ex) strings[0] = "http://localhost:8090/sample/1.jpg"

                osw.write(sendMsg);
                osw.flush();

                // String 배열로 가져오는 방법.
                if(conn.getResponseCode() == conn.HTTP_OK){

                    BufferedReader tmp = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                    StringBuffer buffer = new StringBuffer();
                    while ((str = tmp.readLine()) != null){
                        buffer.append(str);


                    }

                    rs = buffer.toString();

                }

            }catch(MalformedURLException e){
                e.printStackTrace();

            }catch(IOException e){
                e.printStackTrace();
            }

            return rs;


        }



    }



    public void sharedKakao(View v, ArrayList<MyData> mydata, int pos){
        try{
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaoBuilder.addText("링크테스트");
            kakaoBuilder.addImage(mydata.get(pos).img,100,100);

            kakaoLink.sendMessage(kakaoBuilder,context);


        }catch(KakaoParameterException e){
            e.printStackTrace();
        }




    }

}







