package com.envigil.extranet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import com.envigil.extranet.WebService.WebService;
import com.envigil.extranet.models.UploadRouteData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.envigil.extranet.DownloadedWorkOrderFrag.downloadedrecycler;
import static com.envigil.extranet.HomeActivity.fraglayout;

public class PrevInspection extends AppCompatActivity {

    PrevInspectionAdapter prevInspectionAdapter;
    FloatingActionButton AddInspection;
    AlertDialog.Builder builder;
    List<PrevInspectionModel> PrevInspectionsList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    SQLiteHelper sqLiteHelper;
    TextView routename,routedate;
    public static boolean partial;
    public static RecyclerView rvPrevInsp;
    public static TextView    upload_progress_txt;
    public static ProgressBar upload_progress_bar;



    public static int RouteID,WorkID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_inspection);
        AddInspection = findViewById(R.id.floatingActionAddInspection);
        rvPrevInsp = findViewById(R.id.rv_prev_insp);
        routename=findViewById(R.id.insp_upload_route_name);
        routedate=findViewById(R.id.insp_upload_route_insp_date);
        routename.setText("Route:"+UploadRouteData.routename);
        routedate.setText("Route Insp Dt:"+UploadRouteData.routeinspdate);
        Intent intent=getIntent();
        WorkID=intent.getIntExtra("WorkId",0);
        RouteID=intent.getIntExtra("RouteID",0);
        partial=intent.getBooleanExtra("Partially",false);
        upload_progress_bar=findViewById(R.id.upload_progress_layout);
        upload_progress_txt=findViewById(R.id.upload_progress_txt);

        configureNavDrawer();
        configureToolbar();
//        Toast.makeText(this, "Work ID :: "+WorkID, Toast.LENGTH_SHORT).show();
        new Aysnc(WorkID).execute();
        rvPrevInsp.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        sqLiteHelper=new SQLiteHelper(getApplicationContext());
        AddInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvailable() == true){
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(PrevInspection.this, Inspections.class).putExtra("WorkID",WorkID).putExtra("RouteID",RouteID).putExtra("Partially",partial));
                            finish();
                        }
                    },1500);
                }
                else {
                    builder = new AlertDialog.Builder(PrevInspection.this);
                    builder.setTitle("Not connected to internet!!");
                    builder.setMessage("Check your internet connection and try again.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (isInternetAvailable() == true){
                                startActivity(new Intent(PrevInspection.this, Inspections.class));
                                finish();
                            }
                            else {
                                startActivity(new Intent(PrevInspection.this, PrevInspection.class));
                                finish();
                            }
                            dialogInterface.cancel();
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private boolean isInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public class Aysnc extends  AsyncTask{
        public Aysnc(int WorkId){
            WorkID=WorkId;
        }

        String InResult;
        @Override
        protected Object doInBackground(Object[] objects) {
            WebService webService =new WebService();
            InResult=webService.getInspection(WorkID);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try{
                JSONObject jsonObject = new JSONObject(InResult);
                JSONArray InspectionArray = jsonObject.getJSONArray("Inspections");
                ShowInspection(InspectionArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void ShowInspection(JSONArray inspectionArray) {
        for (int i = 0; i < inspectionArray.length(); i++){
            JSONObject object = null;
            try {
                object = inspectionArray.getJSONObject(i);
                int InspectionID = object.getInt("InspID");
                String InspectionDate = object.getString("Insp");
                String EmployeeName = object.getString("Emp");
                String InspectionStartT = object.getString("InspStart");
                String InspectionEndT = object.getString("InspEnd");
                PrevInspectionModel prevInspectionModel =new PrevInspectionModel(InspectionDate,EmployeeName,InspectionStartT,InspectionEndT);
                prevInspectionModel.setInspectionID(InspectionID);
                PrevInspectionsList.add(prevInspectionModel);
                //System.out.println("Result List"+PrevInspectionsList);
                prevInspectionAdapter = new PrevInspectionAdapter(PrevInspectionsList);
                rvPrevInsp.setAdapter(prevInspectionAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.nav_toolbar_prev_inspection);
        setSupportActionBar(nav_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.activity_prev_inspection);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_perv_inspection);
        navigationView.getMenu().removeItem(R.id.home_app);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuId = item.getItemId();

                if (menuId == R.id.inspect_routes){
                    fraglayout.setCurrentItem(0);
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.down_routes){
                    fraglayout.setCurrentItem(1);
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.upload_routes){
                    fraglayout.setCurrentItem(0);
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.bt_config){
                    startActivity(new Intent(PrevInspection.this, Bluetooth.class));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TestRecycler testRecycler =new TestRecycler(new SQLiteHelper(getApplicationContext()).getAll());
        downloadedrecycler.setAdapter(testRecycler);
    }

}
