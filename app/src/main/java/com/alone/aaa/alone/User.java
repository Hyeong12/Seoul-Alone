package com.alone.aaa.alone;

import android.content.Context;
import android.content.SharedPreferences;

public class User {

    Context context;
    private String userID;
    SharedPreferences sharedPreferences;


    public void removeUser(){


        sharedPreferences.edit().clear().commit();
    }



    public String getUserID(){
        userID = sharedPreferences.getString("userdata","");
        return userID;
    }

    public void setUserID(String userID){

        this.userID = userID;
        sharedPreferences.edit().putString("userdata",userID).commit();

    }




    public User(Context context){


        this.context = context;
        sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);

    }

}
