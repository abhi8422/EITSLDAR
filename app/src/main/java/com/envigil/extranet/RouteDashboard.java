package com.envigil.extranet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import static com.envigil.extranet.HomeActivity.fraglayout;

public class RouteDashboard extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public int RouteID;
    String RouteName;
    SQLiteHelper sqLiteHelper;
    TextView routeName;
    DrawerLayout drawerLayout;
    public static String DAEP;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_dashboard);
        routeName=findViewById(R.id.RouteName);
        Intent intent=getIntent();
        RouteID=intent.getIntExtra("RouteID",0);
        DAEP=intent.getStringExtra("DAEP");
        sqLiteHelper=new SQLiteHelper(this);
        RouteName = sqLiteHelper.getRouteName(RouteID);
        routeName.setText(RouteName);
        // Update RouteConfig Inspected Flag


        tabLayout=findViewById(R.id.route_TabLayout);
        viewPager=findViewById(R.id.route_view_pager);
        setUpViewPager(viewPager);
        configureNavDrawer();
        configureToolbar();
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
//        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setUpViewPager(ViewPager viewPager) {
        TabAdapter tabAdapter=new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new RouteAllFragment(RouteID),"ALL");
        tabAdapter.addFragment(new RouteInspectedFragment(RouteID),"INSPECTED");
        tabAdapter.addFragment(new RouteUnInspectedFragment(RouteID),"UNINSPECTED");
        viewPager.setAdapter(tabAdapter);
    }

    //Drawer Layout
    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.nav_toolbar_route_dashboard);
        setSupportActionBar(nav_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.activity_route_dashboard);
        NavigationView navigationView = findViewById(R.id.nav_view_route_dashboard);
        navigationView.getMenu().removeItem(R.id.home_app);
        navigationView.getMenu().removeItem(R.id.delete_route);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction, fragmentTransaction1, fragmentTransaction2;
                int menuId = item.getItemId();

//                if (menuId == R.id.home_app){
//                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.mainapplication");
//                    startActivity(intent);
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                    finish();
//                }
//                else
                if (menuId == R.id.inspect_routes){
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.down_routes){
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    HomeActivity.fraglayout.setCurrentItem(1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.upload_routes){
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.bt_config){
                    startActivity(new Intent(RouteDashboard.this, Bluetooth.class));

//                    Toast.makeText(getApplicationContext(),"Redirecting to bluetooth configuration",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                //Exit APP
                else {
                    moveTaskToBack(true);
                    finish();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RouteDashboard.this,HomeActivity.class));
        finish();
    }
}
