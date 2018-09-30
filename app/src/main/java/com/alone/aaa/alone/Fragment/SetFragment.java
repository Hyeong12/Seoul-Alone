package com.alone.aaa.alone.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alone.aaa.alone.Activity.LoginActivity;
import com.alone.aaa.alone.Activity.Main;
import com.alone.aaa.alone.R;
import com.alone.aaa.alone.User;


public class SetFragment extends Fragment {

    User user;

    public SetFragment() {
        // Required empty public constructor
    }
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        if(user.getUserID().isEmpty()) {

            view = inflater.inflate(R.layout.fragment_set, container, false);

            Button btn = (Button)view.findViewById(R.id.login_Btn);

            btn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                    Intent intent = new Intent(getActivity(),LoginActivity.class);

                    getActivity().startActivity(intent);


                }



            });



        }else{
            view = inflater.inflate(R.layout.fragment_loginset,container,false);

            TextView username = (TextView)view.findViewById(R.id.user_name);

            TextView update = (TextView)view.findViewById(R.id.update);

            TextView logout = (TextView)view.findViewById(R.id.logout);


            username.setText(user.getUserID()+"님");

            update.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment_place, new UpdateFragment());
                    ft.addToBackStack(null);
                    ft.commit();


                }



            });

            logout.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){

                show();



                }


            });






        }
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User(getActivity());

    }



    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("로그아웃");
        builder.setMessage("정말로 로그아웃하시겠습니까?");

        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new User(getActivity()).removeUser();


                        Intent intent = new Intent(getActivity(),Main.class);
                        startActivity(intent);
                    }
                });

        builder.show();



    }

}