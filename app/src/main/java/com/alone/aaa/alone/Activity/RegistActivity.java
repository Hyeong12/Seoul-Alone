package com.alone.aaa.alone.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegistActivity extends AppCompatActivity {


    EditText regID, regName, regPassword, regPasswordCheck, phoneNumber;
    Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        BtnOnClickListener onClickListener = new BtnOnClickListener();

        regID = (EditText) findViewById(R.id.userId);
        regName = (EditText) findViewById(R.id.userName);
        regPassword = (EditText) findViewById(R.id.userPassword);
        regPasswordCheck = (EditText) findViewById(R.id.userPasswordCheck);
        phoneNumber = (EditText) findViewById(R.id.userNumber);


        regBtn = (Button) findViewById(R.id.Btn);
        regBtn.setOnClickListener(onClickListener);

    }

    class CustomTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings) {
            String sendMsg, receiveMsg = null;
            try {
                String str;

                URL url = new URL("http://192.168.43.170:8090/sample/join.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "userID=" + strings[0] + "&userName=" + strings[1] +"&userpassword=" + strings[2] + "&userpasswordcheck=" + strings[3] + "&phoneNumber=" + strings[4];

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


    class BtnOnClickListener implements Button.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.Btn:
                    String userID = regID.getText().toString();
                    String userName = regName.getText().toString();
                    String userPassword = regPassword.getText().toString();
                    String userPasswordCheck = regPasswordCheck.getText().toString();
                    String number =  phoneNumber.getText().toString();
                    Log.i("비밀번호",userPassword);
                    Log.i("비밀번호확인",userPasswordCheck);

                    try {
                        String result = new RegistActivity.CustomTask().execute(userID, userName, userPassword, userPasswordCheck, number).get();
                        Log.i("result", result);
                        if (result.equals("true")) {
                            Toast.makeText(RegistActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                            User user = new User(RegistActivity.this);
                            user.setUserID(userID);
                            Intent intent = new Intent(RegistActivity.this, Main.class);
                            startActivity(intent);
                            finish();
                        } else if (result.equals("Id_error")) {
                            Toast.makeText(RegistActivity.this, "존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                            regID.setText("");
                            regPassword.setText("");
                        } else if (result.equals("passwordError")) {
                            Toast.makeText(RegistActivity.this, "비밀번호를 올바르게 입력하세요", Toast.LENGTH_SHORT).show();
                            regPasswordCheck.setText("");
                            regPassword.setText("");
                        }


                    } catch (Exception e) {
                    }
                    break;


            }


        }


    }
}
