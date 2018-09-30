package com.alone.aaa.alone.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;

import com.alone.aaa.alone.Activity.Main;
import com.alone.aaa.alone.Activity.UploadActivity;
import com.alone.aaa.alone.Adapter.ComAdapter;
import com.alone.aaa.alone.Adapter.MyAdapter;
import com.alone.aaa.alone.ComData;
import com.alone.aaa.alone.ConnectionServer;
import com.alone.aaa.alone.MyData;
import com.alone.aaa.alone.R;
import com.alone.aaa.alone.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {


    public CommunityFragment(){



    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ComData> myDataset = new ArrayList<>();;
    private User user;
    ImageView img;




    String URL= "http://192.168.43.170:8090/sample/Community/getTable.jsp";

    View view;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_com, container, false);

        img = (ImageView) view.findViewById(R.id.create);

        //Add Fragment

        mRecyclerView = (RecyclerView) view.findViewById(R.id.com_recycer_view);


        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new ComAdapter(getActivity(),myDataset); // getApplicationContext() 할때는 intent 오류가 생김.
        // 서버에서 가져온 JSONArray를 전달.그래야 intent를 통해 해당 정보를 넘길수있음.
        // or 가게이름(name)값만 넘기고 intent받는 액티비티에서 서버연결을 통해 name값 비교후 서버로부터 데이터받기.
        mRecyclerView.setAdapter(mAdapter);


        img.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                //로그인 후 이용할수있는
                if(user.getUserID().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("로그인");
                    builder.setMessage("로그인 후 이용하실 수 있습니다");

                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });


                    builder.show();


                }
                else {
                    Intent intent = new Intent(getActivity(), UploadActivity.class);

                    startActivity(intent);
                }

            }




        });







        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User(getActivity());
        Log.e("user",user.getUserID());
        connection();




        //myDataset.add(new MyData("싸움의 고수","http://192.168.43.170:8090/sample/image/1.jpg"));
        //myDataset.add(new MyData("싸움의 고수","http://192.168.43.170:8090/sample/image/2.jpg"));
    }

    public void connection(){
        //
        ConnectionServer image_info = new  ConnectionServer(URL);

        //for문


        // 2) JSONArray를 통해 한번에 가게이름과 주소 얻어오기

        try {
            String result = image_info.execute().get();
            JSONArray results = new JSONArray(result);
            for(int i=0; i<results.length(); i++){

                JSONObject store_info = (JSONObject) results.get(i);
                myDataset.add(new ComData(store_info.get("userid").toString(),store_info.get("name").toString(),
                        store_info.get("addr").toString(), store_info.get("kindsof").toString(),
                        store_info.get("image_url").toString(),store_info.get("ment").toString()));

            }
        }catch(Exception e){

            e.printStackTrace();

        }





    }




}
