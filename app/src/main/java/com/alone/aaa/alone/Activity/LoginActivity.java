package com.alone.aaa.alone.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alone.aaa.alone.R;
import com.alone.aaa.alone.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText userID,userPassword;
    Button loginBtn;
    TextView reg, lookingfor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        userID = (EditText) findViewById(R.id.userId);
        userPassword = (EditText)findViewById(R.id.userPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        reg = (TextView)findViewById(R.id.txt_reg);
        lookingfor = (TextView)findViewById(R.id.txt_look);


        loginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String loginid = userID.getText().toString();
                String loginpwd = userPassword.getText().toString();

                try {

                    String result = new CustomTask().execute(loginid, loginpwd, "login").get();
                    if(result.equals("true")){
                        Toast.makeText(LoginActivity.this,"로그인", Toast.LENGTH_SHORT).show();
                        User user = new User(LoginActivity.this);
                        user.setUserID(loginid);

                        Intent intent = new Intent(LoginActivity.this,Main.class);
                        startActivity(intent);
                        finish();
                    }else if(result.equals("false")){
                        Toast.makeText(LoginActivity.this,"아이디 또는 비밀번호가 틀렸음",Toast.LENGTH_SHORT).show();
                        userID.setText("");
                        userPassword.setText("");
                    } else if(result.equals("noId")) {
                        Toast.makeText(LoginActivity.this,"존재하지 않는 아이디",Toast.LENGTH_SHORT).show();
                        userID.setText("");
                        userPassword.setText("");
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }

            }

        });


        reg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
                finish();

            }

        });

        lookingfor.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                Intent intent = new Intent(LoginActivity.this,IDSearchActivity.class);
                startActivity(intent);
                finish();

            }

        });






    }
    class CustomTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings){
            String sendMsg, receiveMsg=null;
            try{
                String str;

                URL url = new URL("http://192.168.43.170:8090/sample/login.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");



                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&type="+strings[2];

                osw.write(sendMsg);
                osw.flush();

                if(conn.getResponseCode() == conn.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    // BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null){
                        buffer.append(str);
                        Log.i("?",str);

                    }

                    receiveMsg = buffer.toString();

                }else{
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }



            }catch(MalformedURLException e){
                e.printStackTrace();

            }catch(IOException e){
                e.printStackTrace();
            }
            return receiveMsg;
        }

    }






}
