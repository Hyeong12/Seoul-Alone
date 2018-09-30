package com.alone.aaa.alone.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alone.aaa.alone.R;
import com.alone.aaa.alone.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateFragment extends Fragment {
    User user;

    String name;
    String password;
    String passwordCheck;
    String number;

    EditText username,pwd,pwdcheck,num;
    Button btn;



    public UpdateFragment() {
        // Required empty public constructor
    }
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_update, container, false);


        username = (EditText) view.findViewById(R.id.edit_name);
        pwd = (EditText) view.findViewById(R.id.password);
        pwdcheck = (EditText) view.findViewById(R.id.passwordcheck);
        num = (EditText) view.findViewById(R.id.phoneNumber);
        btn = (Button) view.findViewById(R.id.regbtn);

        username.setHint(name);

        //pwd.setHint(password);
        //pwdcheck.setHint(passwordCheck);

        num.setHint(number);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //Log.e("확인", pwd.getText().toString());
                //Log.e("확인1",pwdcheck.getText().toString());
                if (pwd.getText().toString().equals(pwdcheck.getText().toString())) {
                    reg_Connect(username.getText().toString(), pwd.getText().toString(), pwdcheck.getText().toString()
                            , num.getText().toString(), user.getUserID());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("회원정보수정");
                    builder.setMessage("회원정보 수정이 되었습니다.");

                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });


                    //Intent intent = new Intent(getActivity(),);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment_place, new HomeFragment());
                    ft.addToBackStack(null);
                    ft.commit();


                }else {
                    //Log.e("안들어오나?","ㅁㄴㅇㄹ");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("회원정보수정");
                    builder.setMessage("비밀번호가 일치하지 않습니다.");

                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });



                }










            }
        });




        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User(getActivity());
        Connection();
    }


    void Connection(){

        String url = "http://192.168.43.170:8090/sample/User/getUserInfo.jsp";
        BackTask connect = new BackTask(url);

        try {
            String result = connect.execute(user.getUserID()).get();
            JSONObject info = new JSONObject(result);

            name = info.get("userName").toString();
            password = info.get("userPassword").toString();
            passwordCheck = info.get("userPasswordCheck").toString();
            number = info.get("number").toString();




        }catch(Exception e){
            e.printStackTrace();
        }




    }


    void reg_Connect(String name, String pwd, String pwdcheck, String number, String id){

        String url = "http://192.168.43.170:8090/sample/User/user_update.jsp";
        RegTask connect = new RegTask(url);

        try{

            String result = connect.execute(name,pwd,pwdcheck,number,id).get();



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

    class RegTask extends AsyncTask<String, Void, String> {
        // class의 목적 : 각각의 url에 저장된 이미지 파일의 정보를 가져오기 위함.
        String URL;

        public RegTask(String URL){
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
                sendMsg = "name="+strings[0] + "&pwd="+strings[1] + "&pwdcheck="+strings[2] + "&number="+strings[3] + "&id=" + strings[4];
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
