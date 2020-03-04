package com.envigil.extranet.TableOfComponents;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.ComponentsInspectedFragment;
import com.envigil.extranet.DownloadedWorkOrderFrag;
import com.envigil.extranet.HomeActivity;
import com.envigil.extranet.R;
import com.envigil.extranet.RouteDashboard;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.ScannerActivity;
import com.envigil.extranet.TableOfComponents.adapter.ContentAdapter;
import com.envigil.extranet.TableOfComponents.adapter.FirstColumnAdapter;
import com.envigil.extranet.TestRecycler;
import com.envigil.extranet.ViewComponentAdapter;
import com.envigil.extranet.models.ComponentsListPojo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import ysn.com.view.TableView;

public class ComponentsTable extends AppCompatActivity {

    private List<ComponentsListPojo> dataList = new ArrayList<>();

    String TAG,LOCATION,NAME,scannedResult;
    int compId;
    float SIZE;
    String INSPECTED;
    int subID,routeID;
//    private FirstColumnAdapter firstColumnAdapter;
    private ContentAdapter contentAdapter;
    private SQLiteHelper sqLiteHelper;
    private TableView tableView;
    TextView routeName;
    EditText TagSearch;
    DrawerLayout drawerLayout;
    FloatingActionButton barcode_fab_inspected;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.components_table);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent = this.getIntent();
        subID = intent.getIntExtra("SubId",0);
        routeID = intent.getIntExtra("RouteID",0);
        routeName=findViewById(R.id.txt_cmptable);
        tableView = findViewById(R.id.main_activity_table_view);
        sqLiteHelper = new SQLiteHelper(this);
//        firstColumnAdapter = new FirstColumnAdapter(sqLiteHelper.getGridComponents(subID,routeID));
        contentAdapter = new ContentAdapter(sqLiteHelper.getGridComponents(subID,routeID));
//        tableView.setFirstColumnAdapter(firstColumnAdapter);
        tableView.setContentAdapter(contentAdapter);
        TagSearch=findViewById(R.id.TagSearch);
        barcode_fab_inspected=findViewById(R.id.barcode_fab_inspected);
        routeName.setText(sqLiteHelper.getRouteName(routeID));
        //search tag
        TagSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String Tagno= TagSearch.getText().toString();
                    if(Tagno.equals("")){
                        TagSearch.setError("Please Enter a Tag no.");
                    }
                    else{
                       if(sqLiteHelper.getGridSearchComponents(subID,routeID,Tagno).isEmpty()){

                           Toast.makeText(ComponentsTable.this,"Nothing To Show, Please Check The Tag Number",Toast.LENGTH_LONG).show();
                       }
                       else {
                           if(isNumber(Tagno)){
//                               firstColumnAdapter = new FirstColumnAdapter(sqLiteHelper.getGridSearchComponents(subID,routeID,Tagno));
                               contentAdapter = new ContentAdapter(sqLiteHelper.getGridSearchComponents(subID,routeID,Tagno));
//                               tableView.setFirstColumnAdapter(firstColumnAdapter);
                               tableView.setContentAdapter(contentAdapter);
                           }else {

                               Toast.makeText(ComponentsTable.this,"Nothing To Show, Please Check The Tag Number",Toast.LENGTH_LONG).show();
                           }

                       }
                    }
                    handled = true;
                }
                return handled;
            }
        });
        TagSearch.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if((event.getRawX()+TagSearch.getPaddingRight()) >= (TagSearch.getRight() - TagSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        TagSearch.setText("");
//                        firstColumnAdapter = new FirstColumnAdapter(sqLiteHelper.getGridComponents(subID,routeID));
                        contentAdapter = new ContentAdapter(sqLiteHelper.getGridComponents(subID,routeID));
//                        tableView.setFirstColumnAdapter(firstColumnAdapter);
                        tableView.setContentAdapter(contentAdapter);
                    }
                }
                return false;
            }
        });

        //barcode scan
        barcode_fab_inspected.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.animator.animate));
                IntentIntegrator integrator =new IntentIntegrator(ComponentsTable.this);
                integrator.setCaptureActivity(ScannerActivity.class);
                integrator.initiateScan();
            }
        });


        setNewData();
        configureNavDrawer();
        configureToolbar();
    }

    private void setNewData() {
        dataList.clear();
        for (int i = 0; i < contentAdapter.getItemCount(); i++) {
            dataList.add(new ComponentsListPojo(compId,subID,NAME,TAG,INSPECTED,SIZE,LOCATION));
        }
//        firstColumnAdapter.setNewData(dataList);
        contentAdapter.setNewData(dataList);
    }

    //Drawer Layout
    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.nav_toolbar_cmptable_dashboard);
        setSupportActionBar(nav_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.cmptable_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_cmptable);
        navigationView.getMenu().removeItem(R.id.home_app);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                    startActivity(new Intent(ComponentsTable.this, Bluetooth.class));

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
        startActivity(new Intent(this,RouteDashboard.class).putExtra("RouteID",routeID));
        finish();
    }

    //barcode scan
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
            } else {
                scannedResult = result.getContents();
                TagSearch.setText(scannedResult);
            }
        }
    }

    public boolean isNumber(String num){
        try {
            Double.parseDouble(num);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }
}