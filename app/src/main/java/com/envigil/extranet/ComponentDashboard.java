package com.envigil.extranet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.models.Inventory;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class ComponentDashboard extends AppCompatActivity {
   ViewPager viewPager;
    Context context;
   ImageView HomeBtn;
   TextView SubrouteName;
  public int  subId,RouteId;
   SQLiteHelper sqLiteHelper;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_component);

        Intent intent=this.getIntent();
        subId=intent.getIntExtra("SubId",0);
        RouteId = intent.getIntExtra("RouteID",0);

        viewPager=findViewById(R.id.sub_route_container);
        setUpViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.route_TabLayout);
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
//        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
        tabLayout.setupWithViewPager(viewPager);
        HomeBtn= findViewById(R.id.home_sub_route);
        SubrouteName=findViewById(R.id.txt_sub_route);
        sqLiteHelper=new SQLiteHelper(this);
        SubrouteName.setText(sqLiteHelper.getSubName(subId));
        configureNavDrawer();
        configureToolbar();



//        Toast.makeText(this, "Activity ::"+subId, Toast.LENGTH_SHORT).show();

        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ComponentDashboard.this, RouteDashboard.class));
            }
        });

    }
    //Drawer Layout
    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.nav_toolbar_Component_dashboard);
        setSupportActionBar(nav_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.activity_route_component);
        NavigationView navigationView = findViewById(R.id.nav_view_component_dashboard);
        navigationView.getMenu().removeItem(R.id.home_app);
        navigationView.getMenu().removeItem(R.id.delete_route);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction, fragmentTransaction1, fragmentTransaction2;
                int menuId = item.getItemId();

//                if (menuId == R.id.home_app) {
//                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.mainapplication");
//                    startActivity(intent);
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                    finish();
//                } else
                    if (menuId == R.id.inspect_routes) {
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuId == R.id.down_routes) {
//                    fraglayout.setCurrentItem(1);
//                    startActivity(new Intent(ComponentDashboard.this,HomeActivity.class));
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    HomeActivity.fraglayout.setCurrentItem(1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuId == R.id.upload_routes) {
//                    fraglayout.setCurrentItem(0);
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuId == R.id.bt_config) {
                    startActivity(new Intent(ComponentDashboard.this, Bluetooth.class));

//                    Toast.makeText(getApplicationContext(),"Redirecting to bluetooth configuration",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
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


    private void setUpViewPager(ViewPager viewPager) {
        TabAdapter tabAdapter=new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new ComponentsInspectedFragment(subId,RouteId),"INSPECTED");
        tabAdapter.addFragment(new ComponentsUninspectedFragment(subId,RouteId),"UNINSPECTED");
        viewPager.setAdapter(tabAdapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String DAEP = RouteDashboard.DAEP;
        startActivity(new Intent(ComponentDashboard.this,RouteDashboard.class).putExtra("RouteID",RouteId).putExtra("DAEP",DAEP));
        finish();
        //Route Dashboard
//        RouteDashboardAdapter routeDashboardAdapter =new RouteDashboardAdapter(new SQLiteHelper(getApplicationContext()).getSubareas(RouteId));
//        RouteAllFragment.RVSubRoute.setAdapter(routeDashboardAdapter);
//        routeDashboardAdapter.notifyDataSetChanged();
//
//        //Route Inspected
//        RouteInspectedAdapter routeInspectedAdapter=new RouteInspectedAdapter(new SQLiteHelper(getApplicationContext()).getSubareasInspected(RouteId));
//        RouteInspectedFragment.InspectedRV.setAdapter(routeInspectedAdapter);
//        routeInspectedAdapter.notifyDataSetChanged();
//
////        Route UnInspected
//        RouteUninspectedAdapter routeUninspectedAdapter=new RouteUninspectedAdapter(new SQLiteHelper(getApplicationContext()).getSubareasUnInspected(RouteId));
//        RouteUnInspectedFragment.UnInspectedRV.setAdapter(routeUninspectedAdapter);
//        routeUninspectedAdapter.notifyDataSetChanged();

    }
}
