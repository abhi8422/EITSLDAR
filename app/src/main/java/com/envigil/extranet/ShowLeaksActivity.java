package com.envigil.extranet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.google.android.material.navigation.NavigationView;

import static com.envigil.extranet.HomeActivity.fraglayout;

public class ShowLeaksActivity extends AppCompatActivity {

    TextView tvRouteShow;
    public int routeID;
    String routeName;
    SQLiteHelper sqLiteHelper;
    RecyclerView rvShowLeaks;
    ShowLeaksAdapter showLeaksAdapter;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_leaks);

        Intent intent = this.getIntent();
        routeID = intent.getIntExtra("RouteID",0);

        tvRouteShow = findViewById(R.id.tv_route_show);
        rvShowLeaks = findViewById(R.id.rv_show_leaks);
        sqLiteHelper = new SQLiteHelper(this);
        routeName = sqLiteHelper.getRouteName(routeID);
        tvRouteShow.setText(routeName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvShowLeaks.setLayoutManager(layoutManager);
        showLeaksAdapter = new ShowLeaksAdapter(sqLiteHelper.viewAllLeaks(routeID));

        rvShowLeaks.setAdapter(showLeaksAdapter);
        configureNavDrawer();
        configureToolbar();
        int adapterSize = showLeaksAdapter.getItemCount();
        if (adapterSize == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Leaks Found.");
            builder.setMessage("No leaks were found for the selected route.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    dialog.dismiss();
                }
            });
            builder.show();

        }
    }
    //Drawer Layout
    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.nav_toolbar_show_leaks);
        setSupportActionBar(nav_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.activity_show_leaks);
        NavigationView navigationView = findViewById(R.id.nav_view_show_leaks);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction, fragmentTransaction1, fragmentTransaction2;
                int menuId = item.getItemId();

                if (menuId == R.id.home_app){
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.mainapplication");
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    finish();
                }
                else if (menuId == R.id.inspect_routes){

                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.down_routes){

                    HomeActivity.fraglayout.setCurrentItem(1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.upload_routes){

                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.bt_config){
                    startActivity(new Intent(ShowLeaksActivity.this, Bluetooth.class));

//                    Toast.makeText(getApplicationContext(),"Redirecting to bluetooth configuration",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
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
        startActivity(new Intent(ShowLeaksActivity.this,HomeActivity.class));
        finish();
    }
}