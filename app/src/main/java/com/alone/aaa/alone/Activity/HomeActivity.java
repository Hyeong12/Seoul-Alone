package com.alone.aaa.alone.Activity;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alone.aaa.alone.Adapter.HomeAdapter;
import com.alone.aaa.alone.Adapter.MyAdapter;
import com.alone.aaa.alone.ConnectionServer;
import com.alone.aaa.alone.MyData;
import com.alone.aaa.alone.R;
import com.alone.aaa.alone.RepeatData;
import com.alone.aaa.alone.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.MapView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity{


    private String URL = "http://192.168.43.170:8090/sample/Fuc_Home/StoreInfo.jsp";
    private String url = "http://192.168.43.170:8090/sample/Fuc_Home/HomeRepeat.jsp";

    private String name;
    private TextView addr;
    private TextView sale_time;
    private TextView number;
    EditText repeat_txt;

    private ImageView image,favorite,location, repeat;
    private User user;
    private Boolean isChecked = false;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<RepeatData> myDataset = new ArrayList<>();
    ;
    int index;
    int repeat_index;
    double lat,lng;
    String image_url;

    public static final String IS_CHECKED = "false";

    SharedPreferences prefs;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = new User(HomeActivity.this);





        addr = (TextView)findViewById(R.id.addr);
        sale_time = (TextView)findViewById(R.id.sale_time);
        number = (TextView)findViewById(R.id.number);
        image = (ImageView)findViewById(R.id.image);
        favorite = (ImageView)findViewById(R.id.image_btn3);
        location = (ImageView) findViewById(R.id.image_btn1);
        repeat = (ImageView) findViewById(R.id.add);
        repeat_txt = (EditText)findViewById(R.id.edit_repeat);


        mRecyclerView = (RecyclerView) findViewById(R.id.home_recycler) ;
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HomeAdapter(this,myDataset);
        mRecyclerView.setAdapter(mAdapter);


        String store_info = getIncomingIntent();



        Connection(url);



        setImageViewfromDB();

        prefs = getSharedPreferences(getIntent().getStringExtra("store_name"),MODE_PRIVATE);





        if(prefs.getString(IS_CHECKED,"").equals("false")){

            favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);


        }else{

            favorite.setImageResource(R.drawable.ic_favorite_black_24dp);

        }


        ImageViewOnClickListener btn = new ImageViewOnClickListener(store_info);

        favorite.setOnClickListener(btn);



        // 위치정보띄우기
        location.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(HomeActivity.this,MapViewActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                startActivity(intent);
                //Intent intent = new Intent(HomeActivity.this,);
                // 위도,경도 데이터 넘기기
                //


            }



        });
        repeat.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String add_url ="http://192.168.43.170:8090/sample/Fuc_Home/RepeatAdd.jsp";
                long mNow = System.currentTimeMillis();
                Date mdate = new Date(mNow);
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                mFormat.format(mdate); // 현재시간


                //sendToServer(add_url ,repeat_index,index,repeat_txt.getText().toString(),mFormat.format(mdate));
                Back_Task connect = new Back_Task(add_url);
                try {
                    String a = connect.execute(user.getUserID(), String.valueOf(repeat_index), String.valueOf(index), repeat_txt.getText().toString(),
                            mFormat.format(mdate)).get();
                }catch(Exception e){
                    e.printStackTrace();
                }

                myDataset.add(new RepeatData(repeat_index,user.getUserID(),index,repeat_txt.getText().toString(),
                        mFormat.format(mdate)));
                repeat_txt.setText("");
                mAdapter = new HomeAdapter(HomeActivity.this,myDataset);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

    }




    public void setImageViewfromDB(){
        //DB 검사(좋아요 리스트 테이블)를 통해
        String favorite_URL = "http://192.168.43.170:8090/sample/Fuc_Home/isChecked_favorite.jsp";
        getUser_favorite Info = new getUser_favorite(favorite_URL);


        try {
            String boolean_check = Info.execute(name, user.getUserID()).get();
            Log.e("checked","^^^^");
            prefs = getSharedPreferences(name,MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if(boolean_check.equals("true")){

                editor.putString(IS_CHECKED, "true").commit();
            }else{

                editor.putString(IS_CHECKED, "false").commit();
            }

        }catch (Exception e){
            e.printStackTrace();

        }









    }

    class getUser_favorite extends AsyncTask<String, Void, String> {
        // class의 목적 : 각각의 url에 저장된 이미지 파일의 정보를 가져오기 위함.
        String URL;

        public getUser_favorite(String URL){
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
                sendMsg = "name="+strings[0]+"&id="+strings[1];
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


    public String getIncomingIntent(){

        if(getIntent().hasExtra("store_name")){

            name = getIntent().getStringExtra("store_name");


            BackTask info = new BackTask(URL);

            try {
                String result = info.execute(name).get();
                JSONObject store_info = new JSONObject(result);

                index = Integer.parseInt(store_info.get("idx").toString());
                addr.setText(store_info.get("addr").toString());
                sale_time.setText(store_info.get("time").toString());
                number.setText(store_info.get("number").toString());
                image_url = store_info.get("images_url").toString();
                Picasso.with(this).load(store_info.get("images_url").toString()).error(R.drawable.error).into(image);
                lat = Double.parseDouble(store_info.get("lat").toString());
                lng = Double.parseDouble(store_info.get("lng").toString());

                Log.e("lat",String.valueOf(lat));
                Log.e("lng",String.valueOf(lng));

                return result;
            }catch(Exception e){

                e.printStackTrace();

            }




            //setImages();
        }
        return null;
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

                URL url = new URL(URL);
                // ex) http://localhost:8090/sample/1.jpg,


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                //Log.e("string",strings[0]);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "name="+strings[0];
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



    class Favorite_BG extends AsyncTask<String, Void, String> {
        // class의 목적 : 각각의 url에 저장된 이미지 파일의 정보를 가져오기 위함.
        String URL;

        public Favorite_BG(String URL){
            this.URL = URL;

        }
        protected String doInBackground(String... strings){
            String sendMsg;
            String rs = null;


            try{
                String str;

                URL url = new URL(URL);
                // ex) http://localhost:8090/sample/1.jpg,


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                //Log.e("string",strings[0]);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&store_info="+strings[1]+ "&image="+strings[2]+"&type="+strings[3];
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


    class ImageViewOnClickListener implements ImageView.OnClickListener{
        // 좋아요 아이콘을 클릭했을시 이벤트설정
        // HomeActivity의 가게정보를 서버로부터 획득한 후,
        // 획득한 정보를 "좋아요"테이블에 insert
        // 좋아요 색깔구분하기 위한 변수 isChecked을 이용하여 true,false값으로
        // 테이블에 insert할것인지 delete할것인지 결정
        // 주의할점: 사용자마다(아이디에 따른) "좋아요"테이블 구분 + 혼밥,혼술,혼여,혼링 구분
        //

        String favorite_URL = "http://192.168.43.170:8090/sample/Fuc_Home/favorite.jsp";
        String store_info;




        public ImageViewOnClickListener(String store_info){
            this.store_info = store_info;

        }


        Favorite_BG info = new Favorite_BG(favorite_URL);


        public void onClick(View view){

            prefs = getSharedPreferences(getIntent().getStringExtra("store_name"),MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            if(isChecked == false){

                favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                Favorite_BG info = new Favorite_BG(favorite_URL);

                try {

                    String result = info.execute(user.getUserID(), name, image_url,"true").get();



                }catch(Exception e){
                    e.printStackTrace();
                }


                editor.putString(IS_CHECKED, "true").commit();
                isChecked = true;


            }else{
                favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                Favorite_BG info = new Favorite_BG(favorite_URL);
                try {

                    String result = info.execute(user.getUserID(), name, image_url,"false").get();
                }catch(Exception e){
                    e.printStackTrace();
                }
                editor.putString(IS_CHECKED, "false").commit();


                isChecked = false;


            }



        }





    }



    void Connection(String url){

        BackTask info = new BackTask(url);

        try {
            String result = info.execute(String.valueOf(index)).get();

            if(result.equals("null")){
                repeat_index = 1;


            }else {

                JSONArray results = new JSONArray(result);
                int i = 0;
                for (i = 0; i < results.length(); i++) {

                    JSONObject store_info = (JSONObject) results.get(i);
                    myDataset.add(new RepeatData(Integer.parseInt(store_info.get("idx").toString()),
                            store_info.get("userid").toString(),
                            Integer.parseInt(store_info.get("info_indx").toString()),
                            store_info.get("comment").toString(), store_info.get("mdate").toString()));

                }
                JSONObject store_info = (JSONObject) results.get(i - 1);

                repeat_index = Integer.parseInt(store_info.get("idx").toString())+1; // 마지막행 인덱스
                //Log.e("myDataset Size:",String.valueOf(myDataset.size());
                for(int j =0; j<myDataset.size(); j++) {
                    Log.e("myDataset", myDataset.get(j).toString());
                }
            }
        }catch(Exception e){

            e.printStackTrace();

        }


    }





    class Back_Task extends AsyncTask<String, Void, String> {
        // class의 목적 : 각각의 url에 저장된 이미지 파일의 정보를 가져오기 위함.
        String URL;

        public Back_Task(String URL){
            this.URL = URL;

        }
        protected String doInBackground(String... strings){
            String sendMsg;
            String rs = null;


            try{
                String str;

                URL url = new URL(URL);
                // ex) http://localhost:8090/sample/1.jpg,


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                //Log.e("string",strings[0]);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&idx="+strings[1]+"&info_indx="+strings[2]+"&comment="+strings[3]
                        +"&mdate="+strings[4];
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
