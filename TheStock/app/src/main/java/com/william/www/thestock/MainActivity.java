package com.william.www.thestock;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.william.www.thestock.api.StockDataHttp;
import com.william.www.thestock.SharedPreferencesHelper;

import org.json.JSONException;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import layout.InvestFragment;
import layout.Login;
import layout.MinuteFragment;
import layout.NewsFragment;
import layout.SelfInfo;
import layout.StockInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
                implements NavigationView.OnNavigationItemSelectedListener,
        StockInfo.OnFragmentInteractionListener,Login.CallBackValue{

    //@Bind(R.id.tablayout)
    //TabLayout mTabLayout;

    private TextView tv_username;
    private TextView textView;
    private ImageView iv_usericon;
    private Button button;
    private View headerView;

    // Fragment管理对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StockDataHttp stockDataHttp=new StockDataHttp();
        try {
            System.out.println(stockDataHttp.GetKLineData("600008"));
            System.out.println("-------------------------"+stockDataHttp.getRes());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("\n11111111111111\n11111111111\n1111111111111\n1");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initView();

        headerView = navigationView.getHeaderView(0);
//        View drawview = navigationView.inflateHeaderView(R.layout.nav_header_main);
//        //ImageView user_pic = (ImageView) drawview.findViewById(R.id.imageViewIcon);
//        //user_pic.setOnClickListener(this);

        tv_username= (TextView) headerView.findViewById(R.id.tv_username);
        iv_usericon= (ImageView) headerView.findViewById(R.id.iv_usericon);
        textView =(TextView)headerView.findViewById(R.id.textView);
        button = (Button)headerView.findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesHelper share = new SharedPreferencesHelper();
                share.putValue(getApplicationContext(),"shared","name","");
                share.putValue(getApplicationContext(),"shared","id","");
                SendMessageValue1("","登录");
                button.setVisibility(View.INVISIBLE);
            }
        });
        tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(tv_username.getText().toString());
                if (tv_username.getText().toString().equals("登录")){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, Login.newInstance())
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .commitAllowingStateLoss();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment=null;

        if (id == R.id.nav_stockinfo) {
            fragment=new StockInfo();
        } else if (id == R.id.nav_stockinvest) {
            SharedPreferencesHelper share = new SharedPreferencesHelper();
            String name = share.getValue(this,"shared","name","");
            if(name.equals(""))
            {
                System.out.println(name);
                fragment = new Login();
            }else{
                System.out.println(name);
                fragment=new InvestFragment();
            }
        } else if (id == R.id.nav_stockpredict) {
            fragment=new NewsFragment();
        } else if (id == R.id.nav_aboutus) {

        } else if (id == R.id.nav_self) {
            SharedPreferencesHelper share = new SharedPreferencesHelper();
            String name = share.getValue(this,"shared","name","");
            if(name.equals(""))
            {
                //System.out.println(name);
                fragment = new Login();
            }else{
                //System.out.println(name);
                fragment=new SelfInfo();
            }

        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .commitAllowingStateLoss();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void initView(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, StockInfo.newInstance()).commitAllowingStateLoss();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void SendMessageValue(String uid, String uname) {
        tv_username.setText(uname);
        button.setVisibility(View.VISIBLE);
        textView.setText("祝君好运！");

        ImageOptions options=new ImageOptions.Builder()
                //设置加载过程中的图片
                .setLoadingDrawableId(R.mipmap.default_icon)
                //设置加载失败后的图片
                .setFailureDrawableId(R.mipmap.default_icon)
                //设置使用缓存
                .setUseMemCache(true)
                //设置显示圆形图片
                .setCircular(true)
                //设置支持gif
                .setIgnoreGif(false)    //以及其他方法
                .build();
        x.image()
                .bind(iv_usericon,"http://47.93.227.240:8080/SSMDemo/usert/download.action?logo=user"+uid+".png",options);
        System.out.println("success");
    }
    public void SendMessageValue1(String uid, String uname) {
        tv_username.setText(uname);
        button.setVisibility(View.VISIBLE);
        textView.setText("...");

        iv_usericon.setImageResource(R.mipmap.default_icon);
    }
}
