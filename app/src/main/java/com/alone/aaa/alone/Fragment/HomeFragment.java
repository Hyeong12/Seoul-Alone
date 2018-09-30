package com.alone.aaa.alone.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alone.aaa.alone.Adapter.MyAdapter;
import com.alone.aaa.alone.ConnectionServer;
import com.alone.aaa.alone.MyData;
import com.alone.aaa.alone.R;
import com.alone.aaa.alone.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyData> myDataset = new ArrayList<>();;
    private User user;

    Button btn1,bnt2;

    String url = "http://192.168.43.170:8090/sample/getStore.jsp";
    String sub_url="http://192.168.43.170:8090/sample/get";

    public HomeFragment(){


    }





    View view;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_activity, container, false);



        //Add Fragment

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycer_view);


        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        btn1 = (Button) view.findViewById(R.id.tab1);
        bnt2 = (Button) view.findViewById(R.id.tab2);

        btn1.setClickable(true);

        if(btn1.isClickable()){
            connection(url);
            mAdapter = new MyAdapter(getActivity(),myDataset); // getApplicationContext() 할때는 intent 오류가 생김.
            // 서버에서 가져온 JSONArray를 전달.그래야 intent를 통해 해당 정보를 넘길수있음.
            // or 가게이름(name)값만 넘기고 intent받는 액티비티에서 서버연결을 통해 name값 비교후 서버로부터 데이터받기.
            mRecyclerView.setAdapter(mAdapter);


        }

        btn1.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                connection(url);
                mAdapter = new MyAdapter(getActivity(),myDataset); // getApplicationContext() 할때는 intent 오류가 생김.
                // 서버에서 가져온 JSONArray를 전달.그래야 intent를 통해 해당 정보를 넘길수있음.
                // or 가게이름(name)값만 넘기고 intent받는 액티비티에서 서버연결을 통해 name값 비교후 서버로부터 데이터받기.
                mRecyclerView.setAdapter(mAdapter);
            }

        });

        bnt2.setOnClickListener(new View.OnClickListener(){



            public void onClick(View v){

                //connection(url);



            }


        });
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User(getActivity());










        //myDataset.add(new MyData("싸움의 고수","http://192.168.43.170:8090/sample/image/1.jpg"));
        //myDataset.add(new MyData("싸움의 고수","http://192.168.43.170:8090/sample/image/2.jpg"));
    }

    public void connection(String URL){
        //
        ConnectionServer image_info = new  ConnectionServer(URL);

        //for문


        // 2) JSONArray를 통해 한번에 가게이름과 주소 얻어오기

        try {
            String result = image_info.execute().get();
            JSONArray results = new JSONArray(result);
            for(int i=0; i<results.length(); i++){

                JSONObject store_info = (JSONObject) results.get(i);
                myDataset.add(new MyData(user.getUserID(),store_info.get("name").toString(),
                        store_info.get("addr").toString(),store_info.get("images_url").toString()));

            }
        }catch(Exception e){

            e.printStackTrace();

        }





    }



}