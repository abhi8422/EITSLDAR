package com.envigil.extranet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.google.android.material.navigation.NavigationView;

import static com.envigil.extranet.HomeActivity.fraglayout;

public class DeleteRoute extends AppCompatActivity {

    public  static  RecyclerView rvDeleteRoute;
    SQLiteHelper sqLiteHelper;
    DeleteRouteAdapter deleteRouteAdapter;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_route);

        rvDeleteRoute=findViewById(R.id.RVDeleteRoute);
        rvDeleteRoute.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        sqLiteHelper=new SQLiteHelper(getApplicationContext());
        deleteRouteAdapter= new DeleteRouteAdapter(sqLiteHelper.getAll(),getApplicationContext());
        rvDeleteRoute.setAdapter(deleteRouteAdapter);
        configureNavDrawer();
        configureToolbar();
        int adapterSize = deleteRouteAdapter.getItemCount();
        if (adapterSize == 0){
            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Routes Are Downloaded");
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
    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.del_nav_toolbar);
        setSupportActionBar(nav_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.delete_drawer);
        NavigationView navigationView = findViewById(R.id.del_nav_view);
        navigationView.getMenu().removeItem(R.id.home_app);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
/*                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction, fragmentTransaction1, fragmentTransaction2;*/
                int menuId = item.getItemId();
               if (menuId == R.id.inspect_routes){
                    fraglayout.setCurrentItem(0);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.down_routes){
                    fraglayout.setCurrentItem(1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.upload_routes){
                    fraglayout.setCurrentItem(0);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.bt_config){
                    startActivity(new Intent(DeleteRoute.this, Bluetooth.class));
//                    Toast.makeText(getApplicationContext(),"Redirecting to bluetooth configuration",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId==R.id.delete_route){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
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
}
