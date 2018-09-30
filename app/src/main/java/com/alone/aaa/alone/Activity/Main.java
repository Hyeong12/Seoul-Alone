package com.alone.aaa.alone.Activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alone.aaa.alone.Adapter.ViewPagerAdapter;
import com.alone.aaa.alone.Adapter.BottomNavigationViewHelper;
import com.alone.aaa.alone.Fragment.CommunityFragment;
import com.alone.aaa.alone.Fragment.HomeFragment;
import com.alone.aaa.alone.Fragment.LoveFragment;
import com.alone.aaa.alone.Fragment.SearchFragment;
import com.alone.aaa.alone.R;
import com.alone.aaa.alone.Fragment.SetFragment;


public class Main extends AppCompatActivity {

    private TextView mTextMessage;
    //private Intent intent;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    BottomNavigationViewHelper bottomNavigationViewHelper;

    FragmentManager fragmentManager =getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;

    private TabLayout tablayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Alone");*/





        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    1 );
        }

        mTextMessage = (TextView) findViewById(R.id.message);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);

        if (bottomNavigationView.getSelectedItemId() == R.id.action_home){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_place, new HomeFragment()).commit();
        }



        //bottomNavigationViewHelper = new BottomNavigationViewHelper();
        //bottomNavigationViewHelper.disableShiftMode(this,bottomNavigationView);




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_fragment_place, new HomeFragment()).commit();


                        break;
                    case R.id.action_search:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_fragment_place, new SearchFragment()).commit();
                        break;
                    case R.id.action_suda:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_fragment_place, new CommunityFragment()).commit();
                        break;
                    case R.id.action_love:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_fragment_place, new LoveFragment()).commit();
                        break;
                    case R.id.action_settings:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_fragment_place, new SetFragment()).commit();
                        break;
                }
                return true;
            }
        });

    }


}