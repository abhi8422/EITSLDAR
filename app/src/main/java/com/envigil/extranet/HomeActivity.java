package com.envigil.extranet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.LogFileHandling.FileLogTimberTree;
import com.envigil.extranet.WebService.WebService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity {

    /*GPS data*/
    int PERMISSION_ID = 44;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    FusedLocationProviderClient fusedLocationProviderClient;
    float leakLAT, leakLNG;
    /*GPS data*/

    TabLayout tabLayout;
    TextView devTag,versionname;
    public static ViewPager fraglayout;
    List<PermissionDeniedResponse> deniedPermissions = new ArrayList<PermissionDeniedResponse>();
    private DrawerLayout drawerLayout;
    SharedPreferences prefs = null;
    String version;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.e("Starting  HomeActivity");
        devTag = findViewById(R.id.dev_tag);
        versionname=findViewById(R.id.versionname);
        setVersionName(HomeActivity.this);

        if (WebService.WSDL_URL.equals("http://98.173.13.62:8080/web/getroutes.asmx?WSDL")) {
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.blinking_animation);
            devTag.startAnimation(animation);
        }
        else {
            devTag.setText("*");
            devTag.setTextSize(16);
            devTag.setPadding(0,0,0,0);
            devTag.setBackgroundColor(Color.parseColor("#8BC34A"));
        }

        prefs=this.getSharedPreferences("com.envigil.extranet", Context.MODE_PRIVATE);
        tabLayout = findViewById(R.id.tab);
        fraglayout = findViewById(R.id.fraglayout);
        setupviewpager(fraglayout);
        configureNavDrawer();
        configureToolbar();
//      tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
//      tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
        tabLayout.setupWithViewPager(fraglayout);
        //new UpdateAsync().execute();

        Dexter.withActivity(this).withPermissions(READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,ACCESS_FINE_LOCATION,CAMERA).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()){
                    /* Location API Calling*/
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
                    /* Location API Calling*/

                    /* Location Changes */
                    getLastLocation();
                    /* Location Changes */
                }
                if (report.isAnyPermissionPermanentlyDenied()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Need Permissions");
                    builder.setMessage("This app needs these permissions. You can grant them in app settings.");
                    builder.setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            openSettings();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                            dialog.cancel();
                            finish();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).onSameThread().check();
    }

    private void setVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionname.setText("Ver."+version);
    }


    private void openSettings() {
        Timber.e("opening device setting");
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivityForResult(intent,101);
    }

    /* Changes for the GPS Co-ordinates */
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (isLocationEnabled()){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null){
                        requestNewLocation();
                        Timber.e("Location Timber Mesaage");
                    }else {
                        leakLAT = Float.parseFloat((location.getLatitude()+""));
                        leakLNG = Float.parseFloat((location.getLongitude()+""));
                    }
                }
            });
        }
        else {
            displayLocationSettingsRequest(getApplicationContext());
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(2000 / 2);

        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        settingsBuilder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> settingsResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient,settingsBuilder.build());
        settingsResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            private String TAG;

            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()){
                    case LocationSettingsStatusCodes.SUCCESS:

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(HomeActivity.this,REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e){
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocation(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult){
            Location lastLocation = locationResult.getLastLocation();
            leakLAT = Float.parseFloat((lastLocation.getLatitude()+""));
            leakLNG = Float.parseFloat((lastLocation.getLongitude()+""));
        }
    };

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
            else {
            }
        }
    }
    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(nav_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.home_drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
/*              FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction, fragmentTransaction1, fragmentTransaction2;*/
                int menuId = item.getItemId();
                if (menuId == R.id.home_app){
                    Timber.e("(Nav Drawer )Opening the home app");
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.mainapplication");
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    finish();
                }
                else if (menuId == R.id.inspect_routes){
                    Timber.e("(Nav Drawer )Opening inspect routes");
                    fraglayout.setCurrentItem(0);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.down_routes){
                    Timber.e("(Nav Drawer )Opening Downloaded Route");
                    fraglayout.setCurrentItem(1);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.upload_routes){
                    Timber.e("(Nav Drawer)Opening Uploaded Route");
                    fraglayout.setCurrentItem(0);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.bt_config){
                    Timber.e("(Nav Drawer)Opening TVA Connect");
                    startActivity(new Intent(HomeActivity.this, Bluetooth.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (menuId==R.id.delete_route){
                    Timber.e("(Nav Drawer)Opening Delete Route");
                    startActivity(new Intent(HomeActivity.this,DeleteRoute.class));
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

    private void setupviewpager(ViewPager fraglayout) {
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.AddFragment(new DownloadedWorkOrderFrag(), "BROWSE");
        viewPagerAdapter.AddFragment(new AvailableWorkOrderFrag(HomeActivity.this), "DOWNLOAD");
        fraglayout.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Timber.e("Back pressed exit app");
        moveTaskToBack(true);
        finish();
    }
}
