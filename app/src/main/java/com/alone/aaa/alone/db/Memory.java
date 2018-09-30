package com.alone.aaa.alone.db;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class Memory {
    private String title;
    private String image;

    private static final int PREFERED_WIDTH = 250;
    private static final int PREFERED_HEIGHT = 250;



    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_IMAGE = 2;

    public Memory(Cursor cursor){
        this.title = cursor.getString(COL_TITLE);
        this.image = cursor.getString(COL_IMAGE);

    }

    public Memory(String title, Bitmap image){
        this.title = title;
        this.image = bitmapToString(resizeBitmap(image));

    }

    public Bitmap getImage(){
        return StringToBitmap(this.image);
    }


    public String getImageAsString(){

        return this.image;
    }


    public String getTitle(){
        return this.title;
    }

    private String bitmapToString(Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b,Base64.DEFAULT);


    }


    private Bitmap StringToBitmap(String string){
        Bitmap bitmap = null;

        try{

            byte[] encodeByte = Base64.decode(string,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);

        }catch(Exception e){
            e.printStackTrace();
        }


        return bitmap;
    }

    public Bitmap resizeBitmap(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float)PREFERED_WIDTH) / width;
        float scaleHeight = ((float)PREFERED_HEIGHT) / height;


        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,false);
        if(resizedBitmap != bitmap) {
            bitmap.recycle();
        }
        return resizedBitmap;

    }


}
