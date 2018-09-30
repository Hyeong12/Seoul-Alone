package com.alone.aaa.alone.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alone.aaa.alone.Fragment.CommunityFragment;
import com.alone.aaa.alone.Fragment.UpdateFragment;
import com.alone.aaa.alone.User;
import com.alone.aaa.alone.db.Memory;
import com.alone.aaa.alone.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    Button btnSave, btnCancel;
    EditText editTextContent, editTextName, editTextAddr;
    RadioButton rdoBtn1, rdoBtn2, rdoBtn3, rdoBtn4, rdoBtn5;
    ImageView imgBtn,imgview;
    TextView txt;
    String imgPath;
    String rdoBtn;


    private User user;
    private static final int GALLERY_REQUEST = 100;
    private static final int CAMERA_REQUEST = 200;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        user = new User(UploadActivity.this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAddr = (EditText) findViewById(R.id.editTextAddr);
        editTextContent = (EditText) findViewById(R.id.editTextContent);

        rdoBtn1 = (RadioButton) findViewById(R.id.rdoBtn1);
        rdoBtn2 = (RadioButton) findViewById(R.id.rdoBtn2);
        rdoBtn3 = (RadioButton) findViewById(R.id.rdoBtn3);
        rdoBtn4 = (RadioButton) findViewById(R.id.rdoBtn4);
        rdoBtn5 = (RadioButton) findViewById(R.id.rdoBtn5);

        imgBtn = (ImageView) findViewById(R.id.imgBtn);
        imgview = (ImageView)findViewById(R.id.imgview);


        rdoBtn1.setOnClickListener(optionOnClickListener);
        rdoBtn2.setOnClickListener(optionOnClickListener);
        rdoBtn3.setOnClickListener(optionOnClickListener);
        rdoBtn4.setOnClickListener(optionOnClickListener);
        rdoBtn5.setOnClickListener(optionOnClickListener);

        rdoBtn1.setChecked(true);




        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectGallery(v);



            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                save(v);
            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                finish();
          }


        });

    }

    RadioButton.OnClickListener optionOnClickListener
            = new RadioButton.OnClickListener(){
        public void onClick(View v){

            if(rdoBtn1.isChecked()){
                rdoBtn = rdoBtn1.getText().toString();

            }else if(rdoBtn2.isChecked()){
                rdoBtn = rdoBtn2.getText().toString();

            }else if(rdoBtn3.isChecked()){
                rdoBtn = rdoBtn3.getText().toString();

            }else if(rdoBtn4.isChecked()){
                rdoBtn = rdoBtn4.getText().toString();

            }else if(rdoBtn5.isChecked()) {
                rdoBtn = rdoBtn5.getText().toString();

            }



        }


    };





    private void selectGallery(View v){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Select Picture"),GALLERY_REQUEST);


    }

    private void selectCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,CAMERA_REQUEST);
        }


    }

    public void save(View view){ //유저ID, 이름,위치정보,항목체크,사진,코멘트
        if(imgview.getDrawable() == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setTitle("확인");
            builder.setMessage("입력되지 않은 사항이 있습니다.");

            builder.setPositiveButton("예",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
        }


        else if (editTextName.getText().toString().isEmpty() ||
                editTextAddr.getText().toString().isEmpty() ||
                rdoBtn.isEmpty() || editTextContent.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setTitle("확인");
            builder.setMessage("입력되지 않은 사항이 있습니다.");

            builder.setPositiveButton("예",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();

        }else{
            Bitmap image = ((BitmapDrawable)imgview.getDrawable()).getBitmap();
            Memory memory = new Memory(editTextName.getText().toString(),image);

            String connec_url = "http://192.168.43.170:8090/sample/Community/getImage.jsp";
            sendToServer(memory.getImageAsString(), connec_url);
            Toast.makeText(this,"등록되었습니다", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,Main.class);

            startActivity(intent);



        }



        //Log.e("string",memory.getImageAsString());


        //finish();

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);

        if(resultCode == RESULT_OK && resultCode == RESULT_OK){
            try{
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                imgview.setImageBitmap(BitmapFactory.decodeStream(imageStream));

            }catch(IOException e){
                e.printStackTrace();
            }

        }

        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
             Bundle extras = data.getExtras();
             Bitmap image = (Bitmap)extras.get("data");
             imgBtn.setImageBitmap(image);

        }

    }








    private void sendToServer(final String file, String url){

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            public void onResponse(String response){
                Log.e("result",response);
            }



        }, new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error){


            }


        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("id",user.getUserID());
                params.put("name",editTextName.getText().toString());
                params.put("addr",editTextAddr.getText().toString());
                params.put("kindsof",rdoBtn);
                params.put("ment",editTextContent.getText().toString());
                params.put("image",file);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(sr);





    }





}

