package com.alone.aaa.alone.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alone.aaa.alone.Adapter.FavoriteAdapter;
import com.alone.aaa.alone.MyData;
import com.alone.aaa.alone.R;
import com.alone.aaa.alone.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class LoveFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView delete_img;
    private ImageView share_img;

    private ArrayList<MyData> myDataset = new ArrayList<>();;

    public User user;

    String URL = "http://192.168.43.170:8090/sample/favorite_list.jsp";







    public LoveFragment() {
        // Required empty public constructor
    }
    View view;
    @Nullable
    @Override


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        user = new User(getActivity());
        connection();




        //myDataset.add(new MyData("싸움의 고수","http://192.168.43.170:8090/sample/image/1.jpg"));
        //myDataset.add(new MyData("싸움의 고수","http://192.168.43.170:8090/sample/image/2.jpg"));
    }




    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        //setContentView(R.layout.activity_favorite);
        if(user.getUserID().isEmpty()){

            view = inflater.inflate(R.layout.fragment_love, container, false);


        }

        else if(myDataset.isEmpty()){

            view = inflater.inflate(R.layout.empty_favorite, container, false);
            return view;
        }

        else {




            view = inflater.inflate(R.layout.activity_favorite, container, false);

            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycer_view1);


            mRecyclerView.setHasFixedSize(true);


            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);


            mAdapter = new FavoriteAdapter(getActivity(), myDataset);
            mRecyclerView.setAdapter(mAdapter);


            /*delete_img = (ImageView) view.findViewById(R.id.image_delete);
            delete_img.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                    ArrayList<Integer> CheckedList = new ArrayList<>();
                    CheckedList = ((FavoriteAdapter)mRecyclerView.getAdapter()).getChecked();


                    Log.e("size",String.valueOf(myDataset.size()));
                    for(int i=0; i<myDataset.size(); i++){
                        Log.e("data ",myDataset.get(i).name);


                    }

                    for(int i=0; i<CheckedList.size(); i++){
                        Log.e("item",CheckedList.get(i).toString());
                        Log.e("item1",myDataset.get(CheckedList.get(i)).name);
                        removeItem(CheckedList.get(i).intValue());
                    }





                }


            });
            share_img = (ImageView) view.findViewById(R.id.image_share);
            share_img.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"your text");
                    shareIntent.setType("text/plain");
                    startActivity(shareIntent);




                }


            });



*/


        }

        return view;


    }

    public void removeItem(int position){



        myDataset.remove(position);
        mAdapter.notifyDataSetChanged();


    }




    public void connection(){
        //
        BackTask image_info = new BackTask(URL);

        //for문


        // 2) JSONArray를 통해 한번에 가게이름과 주소 얻어오기

        try {
            String result = image_info.execute(user.getUserID()).get();
            JSONArray results = new JSONArray(result);
            for(int i=0; i<results.length(); i++){

                JSONObject store_info = (JSONObject) results.get(i);
                myDataset.add(new MyData(user.getUserID(),store_info.get("store_name").toString(),
                        store_info.get("store_addr").toString(),store_info.get("image_url").toString()));

            }
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
                sendMsg = "userID="+strings[0];
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


}