package com.alone.aaa.alone.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alone.aaa.alone.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class IDSearchActivity extends AppCompatActivity{

    private EditText name,phone;
    private TextView txt_id1, txt_id2, txt_pwd1, txt_pwd2;


    private Button btn;

    String st_name,st_phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_id);

        name = (EditText)findViewById(R.id.sr_name);
        phone = (EditText)findViewById(R.id.sr_number);
        btn = (Button) findViewById(R.id.sr_btn);

        txt_id1 = (TextView)findViewById(R.id.set_id1);
        txt_id2 = (TextView)findViewById(R.id.set_id2);
        txt_pwd1 = (TextView)findViewById(R.id.set_pwd1);
        txt_pwd2 = (TextView)findViewById(R.id.set_pwd2);


        st_name = name.getText().toString();
        st_phone = phone.getText().toString();


        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    String result = new CustomTask().execute(st_name, st_phone).get();

                    if(result.equals("false")){
                        Log.e("틀렸나?","");
                        AlertDialog.Builder builder = new AlertDialog.Builder(IDSearchActivity.this);
                        builder.setTitle("아이디/패스워드 찾기");
                        builder.setMessage("일치하는 정보가 없습니다.");

                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });


                        builder.show();
                        name.setText("");
                        phone.setText("");



                    }else{
                        Log.e("맞았나?","");
                        JSONObject js = new JSONObject(result);

                        txt_id1.setText("아이디");
                        txt_id2.setText(js.get("userid").toString());
                        txt_pwd1.setText("비밀번호");
                        txt_pwd2.setText(js.get("password").toString());

                    }



                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


    }


    class CustomTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings) {
            String sendMsg, receiveMsg = null;
            try {
                String str;

                URL url = new URL("http://192.168.43.170:8090/sample/User/getSearchUser.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "name=" + strings[0] + "&number=" + strings[1];

                osw.write(sendMsg);
                osw.flush();

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);


                    }

                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }

    }




}
