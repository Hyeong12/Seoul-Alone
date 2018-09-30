package com.alone.aaa.alone;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionServer extends AsyncTask<String, Void, String> {

    String URL;

    public ConnectionServer(String URL){
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

            //Log.e("String 값",strings[0]);
           /* if(strings !=null) {
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "name=" + strings[0];
                // ex) strings[0] = "http://localhost:8090/sample/1.jpg"

                osw.write(sendMsg);
                osw.flush();
            }*/
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
