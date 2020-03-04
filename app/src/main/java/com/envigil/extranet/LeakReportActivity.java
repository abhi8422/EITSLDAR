package com.envigil.extranet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.KeyEventDispatcher;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.TableOfComponents.ComponentsTable;
import com.envigil.extranet.fragments.LeakReportBottom;
import com.envigil.extranet.models.ComponentsListPojo;
import com.envigil.extranet.models.LeakPathTypes;
import com.envigil.extranet.models.ReasonSkipped;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
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
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.envigil.extranet.ComponentReading.ppm_flag;

public class LeakReportActivity extends AppCompatActivity implements View.OnClickListener {

    /*GPS data*/
    int PERMISSION_ID = 44;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    FusedLocationProviderClient fusedLocationProviderClient;
    /*GPS data*/

    boolean PermOrLeak;
    boolean grid;
    TextView tvNetUnit,tvTagNo,tvBackRate,tvNetReading,tvReadingRepo,tvampm,tvComponentRepo, tvSizeRepo, tvDateRepo, tvTimeRepo,tvLocationReport, tvLeakInspection, tvPPM, tvDPM, tvLEL, tvUnit;
    ImageView  imgComponent,LeakReportBackBtn;
    Button btnSaveRepo;
    Switch criticalSwitch, esentialSwitch;
    EditText edReadingRepo;
    int leakcompId;
    int UnitNumber;
    int SubID,LeakId;
    String DateTime;
    private int mDay, mMonth, mYear;
    private int mHour, mMinute, mSeconds;
    static int SELECT_FROM_CAMERA = 0;
    LeakReportBottom leakReportBottom;
    SQLiteHelper sqLiteHelper;
    List<ComponentsListPojo> leaksListPojo = new ArrayList<>();
    List<LeakPathTypes> LeakPathTypePojo = new ArrayList<>();
    List<String> LeakPathTypeName =new ArrayList<>();
    List<Integer> LeakPathTypeID = new ArrayList<>();
    float BackgroundReading,LeakFloat1;
    String Unit;
    int RouteId,InvId;
    boolean leakBit1, leakBit5;
    float leakLAT, leakLNG;
    Spinner LeakPathSpinner;
    int PermId;
    int[] FacId,CompStrTypeIDs,PRIdPRTime;
    int RuleId,LeakTypeId,LeakTime,CompTypeId,StrId,StrTypeId,RuleCompTypeId,Status,LeakPathTypeid;
    String InvTag;
    AlertDialog.Builder builder;
    String AM_PM;
    float Reading;
    private DrawerLayout drawerLayout;
    public static boolean last,save_leak_flag;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_report);

        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        tvBackRate = findViewById(R.id.tv_back_rate);
        tvComponentRepo = findViewById(R.id.tv_component_report);
        tvLeakInspection = findViewById(R.id.comp_leak_inspection);
        tvSizeRepo = findViewById(R.id.tv_size_report_);
        tvLocationReport = findViewById(R.id.tv_location_report);
        LeakReportBackBtn=findViewById(R.id.LeakReportBackBtn);
        LeakReportBackBtn.setOnClickListener(this);
        tvDateRepo = findViewById(R.id.date_report_);
        tvTimeRepo = findViewById(R.id.time_report);
        btnSaveRepo = findViewById(R.id.btn_save_report);
        tvampm=findViewById(R.id.am_pm);
        tvUnit = findViewById(R.id.unit);
        criticalSwitch = findViewById(R.id.critical_switch);
        esentialSwitch = findViewById(R.id.essential_switch);
        imgComponent = findViewById(R.id.img_component);
        tvReadingRepo = findViewById(R.id.tv_leak_rate);
        tvNetReading=findViewById(R.id.NetReading);
        tvTagNo=findViewById(R.id.TAGno);
        tvNetUnit=findViewById(R.id.NetUnit);
        LeakPathSpinner=findViewById(R.id.LeakPathSpinner);
        /* Location API Calling*/
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        /* Location API Calling*/
        //Drawer
        /*configureNavDrawer();
        configureToolbar();*/
        Intent intent = this.getIntent();
        leakcompId = intent.getIntExtra("CompId",0);
        SubID = intent.getIntExtra("SubId",0);
        Reading=intent.getFloatExtra("LeakRate",0f);
        Unit=intent.getStringExtra("Unit");
        RouteId=intent.getIntExtra("RouteID",0);
        InvId=intent.getIntExtra("InvID",0);
        PermOrLeak=intent.getBooleanExtra("PermOrLeak", false);
        last=intent.getBooleanExtra("last",false);
        grid = intent.getBooleanExtra("Grid",false);

        //        Toast.makeText(this, "Leak Report Activity::"+SubID, Toast.LENGTH_SHORT).show();

        tvReadingRepo.setText(String.format("%.2f",Reading+0.00));
        tvUnit.setText(Unit);
        tvNetUnit.setText(Unit);

        /* Location Changes */
        getLastLocation();
        /* Location Changes */


        //Leak Path Type Spinner
        LeakPathTypeName.add("Select Leak Path ");
        LeakPathTypeID.add(0);
        //Populating ReasonSkipped Dropdown
        LeakPathTypePojo = sqLiteHelper.getLeakPathType(leakcompId);

        for(int i=0;i<LeakPathTypePojo.size();i++){
            LeakPathTypes leakPathTypes=LeakPathTypePojo.get(i);

            if (!leakPathTypes.getLeakPathTypeName().equals(" ")){
                LeakPathTypeName.add(leakPathTypes.getLeakPathTypeName());
                LeakPathTypeID.add(leakPathTypes.getLeakPathTypeID());
//
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout,LeakPathTypeName);
        LeakPathSpinner.setAdapter(adapter);
        LeakPathSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LeakPathTypeid=LeakPathTypeID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        leaksListPojo = sqLiteHelper.getCompReadingValues(RouteId,SubID,InvId);

        for (int i= 0 ; i < leaksListPojo.size(); i++){
            ComponentsListPojo leakComponentListPojo = leaksListPojo.get(i);
            tvComponentRepo.setText(leakComponentListPojo.getCompName());
            tvSizeRepo.setText(String.valueOf(leakComponentListPojo.getInvSize()));
            LeakFloat1 = leakComponentListPojo.getInvSize();
            tvLocationReport.setText(leakComponentListPojo.getInvLocation());
            tvBackRate.setText(String.valueOf(leakComponentListPojo.getBackread()));
            BackgroundReading=leakComponentListPojo.getBackread();
            if(ppm_flag){
                tvNetReading.setText(String.format("%.2f",(Reading-BackgroundReading)+0.00));
            }
            String Inspection=leakComponentListPojo.getInspected();
            if (Inspection.equals("true")|| Inspection.equals("1")){
                tvLeakInspection.setTextColor(Color.parseColor("#ff669900"));
                tvLeakInspection.setText("Yes");
            }
            else{
                tvLeakInspection.setTextColor(Color.RED);
                tvLeakInspection.setText("No");
            }
            InvTag=leakComponentListPojo.getInvTag();
            InvId=leakComponentListPojo.getInvID();
            tvTagNo.setText(InvTag);
            break;
        }




        String curr_date = new SimpleDateFormat("MM/dd/yyyy",Locale.US).format(new Date());
        tvDateRepo.setText(getDate(RouteId));

        Calendar calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        AM_PM = " AM";
        String mm_precede = "",hr_precede="";
        if (mHour >= 12) {
            AM_PM = " PM";
            if (mHour >=13 && mHour < 24) {
                mHour -= 12;
            }
            else {
                mHour = 12;
            }
        } else if (mHour == 0) {
            mHour = 12;
        }
        if (mMinute < 10) {
            mm_precede = "0";
        }
        if (mHour<10){
            hr_precede ="0";
        }
        String Time =hr_precede+ mHour + ":" + mm_precede + mMinute+":00";
        tvTimeRepo.setText(Time);
        tvampm.setText(AM_PM);

        criticalSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (criticalSwitch.isChecked()){
                    leakBit1 = true;
                }
                else {
                    leakBit1 = false;
                }
            }
        });
        esentialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (esentialSwitch.isChecked()){
                    leakBit5 = true;
                }
                else {
                    leakBit5 = false;
                }
            }
        });

        btnSaveRepo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(LeakReportActivity.this, R.animator.animate));
                String getDate = tvDateRepo.getText().toString();
                String getTime = tvTimeRepo.getText().toString()+AM_PM;
                System.out.println(getDate);
                System.out.println(getTime);
                //LeakTypeID.
                String getUnit = tvUnit.getText().toString();
                //Float Type LeakReading.

                //Boolean.
                boolean ifCritical = criticalSwitch.isSelected();
                boolean ifEssential = esentialSwitch.isSelected();
                //Time and Date.
                DateTime= getDate+" "+getTime;
                System.out.println(DateTime);
                final int getcompId = leakcompId;
                if (Unit.equals("PPM")){
                    LeakTypeId=1;
                }
                if (Unit.equals("DPM")){
                    LeakTypeId=2;
                }
                if (Unit.equals("LEL")){
                    LeakTypeId=3;
                }
                //Leak Time is the time from the table and not the actual time.
                //LeakTime Identifies how much time is granted to fix the component.


                float leakRate = Float.parseFloat(tvReadingRepo.getText().toString());
                //Get Facility ID
                FacId=sqLiteHelper.getFacId(RouteId);
                //FacId in Int Array [] FacId[0] is FacID and FacId[1] is RuleID.
                RuleId=FacId[1];
                //Check Permit ID for Facility.
                PermId=sqLiteHelper.CheckPermId(FacId[0]);
                if(!(LeakPathSpinner.getSelectedItem()=="Select Leak Path ")){
                    if (PermOrLeak){
                        PRIdPRTime=sqLiteHelper.getPrIdPrTime(PermId,LeakTypeId);
                        LeakTime=PRIdPRTime[1];
                    }
                    else{
                        //get CompTypeId ,StrID, StrTypeId.
                        CompStrTypeIDs=sqLiteHelper.getCompStrTypeIDs(RouteId,SubID,InvId);
                        //CompStrTypeIDs [0] = CompTypeId, [1] = StrID,[2] = StrTypeID;
                        CompTypeId=CompStrTypeIDs[0];
                        StrId=CompStrTypeIDs[1];
                        StrTypeId=CompStrTypeIDs[2];
                        //get RuleCompTypeId RuleComponentTypes and use CompTypeId and RuleID
                        RuleCompTypeId=sqLiteHelper.getRuleCompTypeId(CompTypeId,RuleId);
                        LeakTime=sqLiteHelper.getleakRateTime(RuleCompTypeId,LeakTypeId,StrId,StrTypeId,Reading);
                    }
                    /*Changes*/
                    //Get The max Id and if null start new id
                    LeakId=sqLiteHelper.getmaxLeakID();
                    if (LeakId==0){
                        LeakId=1;
                    }
                    else{
                        LeakId=LeakId+1;
                    }
                    int count = sqLiteHelper.countLeaks(RouteId,SubID,InvId);
                    if (count == 0){
                        sqLiteHelper.InsertLeaks(LeakId,InvId,InvTag,LeakPathTypeid,LeakTypeId,getcompId,leakBit1,leakBit5,LeakFloat1, leakRate,LeakTime,DateTime,leakLAT,leakLNG,false);
                    }
                    else {
                        sqLiteHelper.UpdateLeaks(InvId,LeakPathTypeid,LeakTypeId,leakBit1,leakBit5,leakRate,LeakTime,DateTime);
                    }

                    //Leak Report Bottom Fragment In Fragment Folder.
                    View dialogView = getLayoutInflater().inflate(R.layout.fragment_leak_reporting_bottom, null);
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LeakReportActivity.this);
                    bottomSheetDialog.setContentView(dialogView);
                    bottomSheetDialog.show();
                    Intent intent1 = new Intent();
                    intent1.putExtra("CompId",leakcompId);
                    intent1.putExtra("SubId",SubID);
                    intent1.putExtra("Unit",Unit);
                    intent1.putExtra("Grid",grid);
                }
                else{
                    builder = new AlertDialog.Builder(LeakReportActivity.this);
                    //Setting message manually and performing action on button click
                    builder.setMessage("Please Select A Leak Path !")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Passing flag to Leak Summary Screen.
                                   // startActivity(new Intent(LeakReportActivity.this, LeakReportActivity.class).putExtra("CompId",getcompId).putExtra("SubId",SubID).putExtra("LeakRate",Reading).putExtra("Unit",Unit).putExtra("RouteID",RouteId).putExtra("InvID",InvId).putExtra("PermOrLeak",PermOrLeak));

                                    dialog.cancel();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Leak Path Not Selected!!");
                    alert.show();
                }

                //int thresholdFirst = Integer.parseInt(edReadingRepo.getText().toString()) ;
                save_leak_flag=true;
            }

        });



        tvTimeRepo.setOnClickListener(this);
    }
    /* Changes for the GPS Co-ordinates */
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()){
            if (isLocationEnabled()){
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null){
                            requestNewLocation();
                        }else {
                            leakLAT = Float.parseFloat((location.getLatitude()+""));
                            leakLNG = Float.parseFloat((location.getLongitude()+""));
                        }
                    }
                });
            }
            else {
                displayLocationSettingsRequest(getApplicationContext());
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }
        else {
            requestPermissions();
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000 / 2);

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
                            status.startResolutionForResult(LeakReportActivity.this,REQUEST_CHECK_SETTINGS);
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

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissions()){
            getLastLocation();
        }
    }
    /* Changes for the GPS Co-ordinates */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {

        if (view == tvTimeRepo) {
            final Calendar calendar = Calendar.getInstance();
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
            mSeconds = calendar.get(Calendar.SECOND);

           /* TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String AM_PM = " AM";
                    String mm_precede = "",hr_precede="";
                    if (hourOfDay >= 12) {
                        AM_PM = " PM";
                        if (hourOfDay >=13 && hourOfDay < 24) {
                            hourOfDay -= 12;
                        }
                        else {
                            hourOfDay = 12;
                        }
                    } else if (hourOfDay == 0) {
                        hourOfDay = 12;
                    }
                    if (minute < 10) {
                        mm_precede = "0";
                    }
                    if (hourOfDay<10){
                        hr_precede ="0";
                    }
                    String Date =hr_precede+ hourOfDay + ":" + mm_precede + minute+":00";
                    tvTimeRepo.setText(Date);
                    tvampm.setText(AM_PM);
                }
            }, mHour, mMinute,false);
            timePickerDialog.show();*/
            MyTimePickerDialog myTimePickerDialog=new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                    tvTimeRepo.setText(String.format("%02d", hourOfDay)+
                            ":" + String.format("%02d", minute) +
                            ":" + String.format("%02d", seconds));
                    String AM_PM = " AM";
                    String mm_precede = "",hr_precede="";
                    if (hourOfDay >= 12) {
                        AM_PM = " PM";
                        if (hourOfDay >=13 && hourOfDay < 24) {
                            hourOfDay -= 12;
                        }
                        else {
                            hourOfDay = 12;
                        }
                    } else if (hourOfDay == 0) {
                        hourOfDay = 12;
                    }
                    if (minute < 10) {
                        mm_precede = "0";
                    }
                    if (hourOfDay<10){
                        hr_precede ="0";
                    }
                    tvampm.setText(AM_PM);
                    /*tvTimeRepo.setText(hourOfDay+":"+minute+":"+seconds);*/

                }
            },mHour,mMinute,mSeconds,false);
            myTimePickerDialog.show();
        }

        if(view == LeakReportBackBtn){
            startActivity(new Intent(LeakReportActivity.this,ComponentReading.class).putExtra("SubId",SubID).putExtra("RouteID",RouteId).putExtra("InvID",InvId));
            finish();
        }
        if (view == tvPPM){
            tvUnit.setText("PPM");
            UnitNumber = 1;
        }

        if (view == tvDPM){
            tvUnit.setText("DPM");
            UnitNumber = 2;
        }

        if (view == tvLEL){
            tvUnit.setText("LEL");
            UnitNumber = 3;
        }
    }

    public void repairCancel(View view){

    }

    @Override
    public void onBackPressed() {

        /*startActivity(new Intent(LeakReportActivity.this,ComponentReading.class).putExtra("CompId",leakcompId).putExtra("SubId",SubID).putExtra("RouteID",RouteId).putExtra("InvID",InvId));
        finish();*/

            builder = new AlertDialog.Builder(LeakReportActivity.this);
            String save = "<font color='#8BC34A'>SAVE</font>";
            builder.setMessage(Html.fromHtml("Click on "+save+" button (Don't use back button)"))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Saving Leak Summary is Mandatory !!");
            alert.show();

    }

    public void cameraTapped(View view) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, SELECT_FROM_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == SELECT_FROM_CAMERA)
            {
                Bundle extras = data.getExtras();
                Bitmap componentBitmap = (Bitmap) extras.get("data");
                imgComponent.setImageBitmap(componentBitmap);
            }
        }
    }
    private int readNum(String s){
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    public void OpenRepairRequest(View view) {
        startActivity(new Intent(this, RepairRequest.class).putExtra("CompId",leakcompId));
    }

    public void OpenRepair(View view) {
        leakReportBottom = new LeakReportBottom();
        startActivity(new Intent(this, RepairRequest.class).putExtra("CompId",leakcompId).putExtra("RouteID",RouteId).putExtra("InvID",InvId).putExtra("Grid",grid).putExtra("LeakID",LeakId).putExtra("SubID",SubID).putExtra("Unit",Unit).putExtra("PermOrLeak",PermOrLeak).putExtra("LeakDateTime",DateTime).putExtra("LeakRate",Reading).putExtra("last",last));
//        leakReportBottom.dismiss();
        finish();
    }

    public void Cancel(View view) {
        if (last){
            /*startActivity(new Intent(LeakReportActivity.this, ComponentDashboard.class).putExtra("SubId",SubID).putExtra("RouteID",RouteId));
            finish();*/
//            grid=ComponentReading.grid;
            if (grid){
                startActivity(new Intent(LeakReportActivity.this, ComponentsTable.class).putExtra("SubId",SubID).putExtra("RouteID",RouteId));
                finish();
            }
            else {
                startActivity(new Intent(LeakReportActivity.this, ComponentDashboard.class).putExtra("SubId",SubID).putExtra("RouteID",RouteId));
                finish();
            }            finish();
        }
        else {
            boolean next =true;
            startActivity(new Intent(LeakReportActivity.this, ComponentReading.class).putExtra("CompId",leakcompId).putExtra("SubId",SubID).putExtra("RouteID",RouteId).putExtra("InvID",InvId).putExtra("next",next).putExtra("Grid",grid));
            finish();
        }

    }

    public String getDate(int RouteID){
        String date;
        SQLiteHelper sqLiteHelper=new SQLiteHelper(getApplicationContext());
        date=sqLiteHelper.getRouteConfigDate(RouteID);
        return date;
    }

   /* private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.nav_toolbar_leak_summary);
        setSupportActionBar(nav_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.activity_leak_report);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_leak_report);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction, fragmentTransaction1, fragmentTransaction2;
                int menuId = item.getItemId();


                if (menuId == R.id.home_app) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.mainapplication");
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    finish();
                } else if (menuId == R.id.inspect_routes) {
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuId == R.id.down_routes) {
//                    fraglayout.setCurrentItem(1);
//                    startActivity(new Intent(ComponentDashboard.this,HomeActivity.class));
                    HomeActivity.fraglayout.setCurrentItem(1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuId == R.id.upload_routes) {
//                    fraglayout.setCurrentItem(0);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuId == R.id.bt_config) {
                    startActivity(new Intent(LeakReportActivity.this, Bluetooth.class));

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
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save_leak_flag=false;
    }
}
