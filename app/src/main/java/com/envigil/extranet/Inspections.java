package com.envigil.extranet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.WebService.WebService;
import com.envigil.extranet.models.EmployeesPojo;
import com.envigil.extranet.models.TVAPojo;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.PendingIntent.getActivity;


public class Inspections extends AppCompatActivity implements View.OnClickListener {

    EditText ed_lunch, ed_travel, ed_admin, ed_repair, ed_reinspect;
    TextView tv_start_ampm,tv_end_ampm, tv_insp_strt, tv_insp_end,tv_arrival,tv_departure,insp_progress_txt;
    TextInputEditText tv_insp_date;
    LinearLayout arrival_LL,departure_LL;
    ImageView imgInspDate, imgStrtInsp, imgEndInsp;
    Button sveInspection;
    private int mDay, mMonth, mYear;
    private int mHour, mMinute, mSeconds;
    List<String> Names = new ArrayList<>();
    List<Integer> EmpIds = new ArrayList<>();
    List<String> TVANames = new ArrayList<>();
    List<Integer> TVAIDList = new ArrayList<>();
    int MnId,MnTVAId;
    int WorkID,RouteID;
    String result,ac;
    Spinner empName,tvaList;
    DrawerLayout drawerLayout;
     Calendar StartTime=null;
     Calendar EndTime=null;
    String ampm;
    int starthr,endhr;
    SQLiteHelper sqLiteHelper;
    ProgressDialog progressDialog;
    LinearLayout insp_layout;
    ProgressBar insp_progress_bar;
    boolean partial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspections);
        tv_insp_date = findViewById(R.id.tv_insp_id);
        tv_insp_date.setOnClickListener(this);
        ed_lunch = findViewById(R.id.ed_lunch_time);
        ed_travel = findViewById(R.id.ed_travel);
        ed_admin = findViewById(R.id.ed_admin);
        ed_repair = findViewById(R.id.ed_repair);
        ed_reinspect = findViewById(R.id.ed_reinspect);
        tv_insp_strt = findViewById(R.id.tv_strt_time);
        tv_insp_end = findViewById(R.id.tv_end_time);
        imgInspDate = findViewById(R.id.img_insp_date);
        imgStrtInsp = findViewById(R.id.img_strt_time);
        imgEndInsp = findViewById(R.id.img_end_time);
        sveInspection = findViewById(R.id.btn_sve_insp);
        empName = findViewById(R.id.emp1_spinner);
        tvaList = findViewById(R.id.TVA_spinner);
        tv_start_ampm=findViewById(R.id.StartAmpm);
        tv_end_ampm=findViewById(R.id.EndAmpm);
        tv_arrival=findViewById(R.id.tv_arrival);
        tv_arrival.setOnClickListener(this);
        insp_layout=findViewById(R.id.insp_layout);
        insp_progress_bar=findViewById(R.id.insp_progress_layout);
        insp_progress_txt=findViewById(R.id.insp_progress_txt);
        tv_departure=findViewById(R.id.tv_departure);
        tv_departure.setOnClickListener(this);
        arrival_LL=findViewById(R.id.arrival_LL);
        arrival_LL.setOnClickListener(this);
        departure_LL=findViewById(R.id.departure_LL);
        departure_LL.setOnClickListener(this);
        WebService webService =new WebService();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        configureNavDrawer();
        configureToolbar();
        sqLiteHelper=new SQLiteHelper(this);

        //Intent Get workid.
        Intent intent=getIntent();
        WorkID = intent.getIntExtra("WorkID",0);
        RouteID = intent.getIntExtra("RouteID", 0);
        partial=intent.getBooleanExtra("Partially",false);
        //Toast.makeText(this, "Work ID : : "+WorkID, Toast.LENGTH_SHORT).show();
        Names.add("Select Employee Name");
        EmpIds.add(0);
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getNames(sqLiteHelper.getEmployeeName()));
        adp1.setDropDownViewResource(R.layout.spinner_layout);
        empName.setAdapter(adp1);

        empName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             MnId=EmpIds.get(position);
                //Toast.makeText(Inspections.this, "Id : : "+ MnId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Spinner for TVA names.
        TVANames.add("Select TVA");
        TVAIDList.add(0);
        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getTVADetails(sqLiteHelper.getTVA()));
        adp2.setDropDownViewResource(R.layout.spinner_layout);
        tvaList.setAdapter(adp2);

        tvaList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MnTVAId = TVAIDList.get(position);
                //Toast.makeText(Inspections.this, "TVA ID : : "+ MnTVAId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String curr_date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        tv_insp_date.setText(curr_date);

        imgInspDate.setOnClickListener(this);
        imgStrtInsp.setOnClickListener(this);
        imgEndInsp.setOnClickListener(this);
        sveInspection.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {

        if (view == imgInspDate|| view== tv_insp_date) {
            final Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mMonth = calendar.get(Calendar.MONTH);

            DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthofyear, int dayofmonth) {
                    tv_insp_date.setText((monthofyear+1)+"/"+dayofmonth+"/"+year);
                }
            },mYear, mMonth, mDay);
            pickerDialog.show();
        }
        if (view == imgStrtInsp || view == arrival_LL || view==tv_insp_strt || view == tv_arrival) {
            StartTime = Calendar.getInstance();
            mHour = StartTime.get(Calendar.HOUR_OF_DAY);
            mMinute = StartTime.get(Calendar.MINUTE);
            mSeconds = StartTime.get(Calendar.SECOND);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    starthr=hourOfDay;
                    System.out.println("starthr ::"+starthr);
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
                    String Date =hr_precede+ hourOfDay + ":" + mm_precede + minute;
                    tv_insp_strt.setText(Date.trim());
                    tv_start_ampm.setText(AM_PM);
                }
            }, mHour, mMinute,false);
            timePickerDialog.show();
        }

        if ((view == imgEndInsp || view== departure_LL ||view==tv_departure||view ==tv_insp_end )) {
             EndTime = Calendar.getInstance();
            mHour = EndTime.get(Calendar.HOUR_OF_DAY);
            mMinute = EndTime.get(Calendar.MINUTE);
            mSeconds = EndTime.get(Calendar.SECOND);
/*
            TimePickerDialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    endhr=hourOfDay;
                if(hourOfDay>=12){
                    ampm="PM";
                }else {
                    ampm="AM";
                }
                    tv_insp_end.setText(hourOfDay+":"+minute+"  "+ampm);
                }
            },mHour,mMinute,false);
            dialog.show();*/
           TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    endhr=hourOfDay;
                    System.out.println("endhr ::"+endhr);
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
                    tv_insp_end.setText(Date);
                    tv_end_ampm.setText(AM_PM);
                    view.setIs24HourView(false);
                    tv_insp_end.setText(hourOfDay + ":" + minute);
                }
            }, mHour, mMinute,false);
            timePickerDialog.show();
        }

        if (view == sveInspection) {
            String empname=empName.getSelectedItem().toString();
            String tvaname=tvaList.getSelectedItem().toString();
            if(empname.equals("Select Employee Name")){
                AlertDialog.Builder builder=new AlertDialog.Builder(Inspections.this);
                builder.setTitle("Please Select Employee Name");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
            else if(tvaname.equals("Select TVA")){
                AlertDialog.Builder builder=new AlertDialog.Builder(Inspections.this);
                builder.setTitle("Please Select TVA");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }else if(starthr<endhr) {
                String inspec_date = tv_insp_date.getText().toString();
                String start_time = tv_insp_strt.getText().toString();
                String end_time = tv_insp_end.getText().toString();
                String lunch = ed_lunch.getText().toString();
                String travel = ed_travel.getText().toString();
                String admin = ed_admin.getText().toString();
                String repair = ed_repair.getText().toString();
                String reinspect = ed_reinspect.getText().toString();
                if (inspec_date.equals(""))
                    tv_insp_date.setError("Date Cannot be Empty ");
                else if (start_time.equals(""))
                    tv_insp_strt.setError("Time Cannot Be Empty ");
                else if (end_time.equals(""))
                    tv_insp_end.setError("Time Cannot Be Empty ");
                else if (lunch.equals(""))
                    ed_lunch.setError("Lunch Time Cannot Be Empty ");
                else if (travel.equals(""))
                    ed_travel.setError("Enter Travel Time");
                else if (admin.equals(""))
                    ed_admin.setError("Enter Admin");
                else if (repair.equals(""))
                    ed_repair.setError("Repair cannot be empty");
                else if (reinspect.equals(""))
                    ed_reinspect.setError("Reinspection Cannot be empty");
                else {
                    //Upload WebService.
                    String InspDate = tv_insp_date.getText().toString();
                    int WorkId = WorkID;
                    int EmpID = MnId;
                    int TVAID = MnTVAId;
                    String StartTime = InspDate + " " + tv_insp_strt.getText().toString() + tv_start_ampm.getText().toString();
                    String EndTime = InspDate + " " + tv_insp_end.getText().toString() + tv_end_ampm.getText().toString();
                    double LunchTime = Double.parseDouble(ed_lunch.getText().toString());
                    double TravelTime = Double.parseDouble(ed_travel.getText().toString());
                    double AdminTime = Double.parseDouble(ed_admin.getText().toString());
                    double RepairTime = Double.parseDouble(ed_repair.getText().toString());
                    double ReInspectTime = Double.parseDouble(ed_reinspect.getText().toString());
                    System.out.println("Work ID : :" + WorkId);
                    System.out.println("Emp ID : :" + EmpID);
                    System.out.println("TVA ID : :" + TVAID);
                    System.out.println("Start Time : :" + StartTime);
                    System.out.println("End Time : :" + EndTime);
                    System.out.println("Lunch time : :" + LunchTime);
                    System.out.println("Travel time : :" + TravelTime);
                    System.out.println("Admin Time : :" + AdminTime);
                    System.out.println("Repair Time : :" + RepairTime);
                    System.out.println("Reinspect Time  : :" + ReInspectTime);
                    insp_layout.setVisibility(View.GONE);
                    insp_progress_bar.setVisibility(View.VISIBLE);
                    insp_progress_txt.setVisibility(View.VISIBLE);
                    new async(WorkId, EmpID, TVAID, StartTime, EndTime, LunchTime, TravelTime, AdminTime, RepairTime, ReInspectTime).execute();
                }
            }
            else {
                AlertDialog.Builder builder=new AlertDialog.Builder(Inspections.this);
                builder.setTitle("Please Select Appropriate Time");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        }
    }
     public List<String> getNames(List<EmployeesPojo> employeesPojoList){
        for(int i=0;i<employeesPojoList.size();i++){
            EmployeesPojo employeesPojo=employeesPojoList.get(i);
            Names.add(employeesPojo.getEmpFirst()+" "+employeesPojo.getEmpLast());
            EmpIds.add(employeesPojo.getEmpID());
        }
        return Names;
     }
     public  List<String> getTVADetails(List<TVAPojo> tvaPojoList){
        for (int i=0;i<tvaPojoList.size();i++){
            TVAPojo tvaPojo = tvaPojoList.get(i);
            TVANames.add(tvaPojo.getTVAName());
            TVAIDList.add(tvaPojo.getTVAID());
        }
        return TVANames;
     }
     public class async extends AsyncTask{
         int workId,empId,tvaId;
         String inspStart,inspEnd;
         double inspLunch,inspTravel,inspAdmin,inspRepair,inspReinsp;
         public async(int WorkID,int EmpID,int TVAID,String InspStart,String InspEnd,double InspLunch,double InspTravel,double InspAdmin,double InspRepair,double InspReInsp){
             workId=WorkID;
             empId=EmpID;
             tvaId=TVAID;
             inspStart=InspStart;
             inspEnd=InspEnd;
             inspLunch=InspLunch;
             inspAdmin=InspAdmin;
             inspTravel=InspTravel;
             inspRepair=InspRepair;
             inspReinsp=InspReInsp;
         }

         @SuppressLint("WrongThread")
         @Override
         protected Object doInBackground(Object[] objects) {
             WebService webService=new WebService();
             result= webService.validateInspecion(empId,inspStart,inspEnd);
             if (result.equals("true")){
                    new async2(workId,empId,tvaId,inspStart,inspEnd,inspLunch,inspTravel,inspAdmin,inspRepair,inspReinsp).execute();
                 System.out.println(result);
             }
             else{
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         AlertDialog.Builder builder = new AlertDialog.Builder(Inspections.this);
                         //Setting message manually and performing action on button click
                         builder.setMessage("Please Enter A Valid Inspection")
                                 .setCancelable(false)
                                 .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int id) {
                                         //  Action for 'NO' Button
                                         insp_layout.setVisibility(View.VISIBLE);
                                         insp_progress_bar.setVisibility(View.GONE);
                                         insp_progress_txt.setVisibility(View.GONE);
                                         dialog.dismiss();

                                     }
                                 });
                         AlertDialog alert = builder.create();
                         alert.show();

                     }
                 });
             }
             return null;
         }

     }
     public class async2 extends AsyncTask{
        int workId,empId,tvaId;
        String inspStart,inspEnd;
        double inspLunch,inspTravel,inspAdmin,inspRepair,inspReinsp;
        public async2(int WorkID,int EmpID,int TVAID,String InspStart,String InspEnd,double InspLunch,double InspTravel,double InspAdmin,double InspRepair,double InspReInsp){
            workId=WorkID;
            empId=EmpID;
            tvaId=TVAID;
            inspStart=InspStart;
            inspEnd=InspEnd;
            inspLunch=InspLunch;
            inspAdmin=InspAdmin;
            inspTravel=InspTravel;
            inspRepair=InspRepair;
            inspReinsp=InspReInsp;
        }
         @Override
         protected Object doInBackground(Object[] objects) {
            WebService webService=new WebService();
            ac= webService.uploadInspection(workId,empId,tvaId,inspStart,inspEnd,inspLunch,inspTravel,inspAdmin,inspRepair,inspReinsp);

             return null;
         }

         @Override
         protected void onPostExecute(Object o) {
             super.onPostExecute(o);
             System.out.println("AC ::"+ac);
             if(!ac.isEmpty()){
                 /*progressDialog.dismiss();*/
                 AlertDialog.Builder builder = new AlertDialog.Builder(Inspections.this);
                 //Setting message manually and performing action on button click
                 builder.setMessage("Inspection Created Sucessfully")
                         .setCancelable(false)
                         .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 //  Action for 'NO' Button
                                 dialog.dismiss();
                                 insp_layout.setVisibility(View.VISIBLE);
                                 insp_progress_bar.setVisibility(View.GONE);
                                 insp_progress_txt.setVisibility(View.GONE);
                                 startActivity(new Intent(Inspections.this,PrevInspection.class).putExtra("WorkId",WorkID).putExtra("RouteID",RouteID).putExtra("Partially",partial));
                                 finish();
                             }
                         });
                 AlertDialog alert = builder.create();
                 alert.show();
             }else {
                 /*progressDialog.dismiss();*/
                 insp_layout.setVisibility(View.VISIBLE);
                 insp_progress_bar.setVisibility(View.GONE);
                 insp_progress_txt.setVisibility(View.GONE);
                 AlertDialog.Builder builder = new AlertDialog.Builder(Inspections.this);
                 //Setting message manually and performing action on button click
                 builder.setMessage("Inspection Is Not Created ")
                         .setCancelable(false)
                         .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 //  Action for 'NO' Button
                                 dialog.dismiss();
                             }
                         });
                 AlertDialog alert = builder.create();
                 alert.show();
             }
         }
     }

    //Drawer Layout
    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.nav_toolbar_inspection);
        setSupportActionBar(nav_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.activity_inspections);
        NavigationView navigationView = findViewById(R.id.nav_view_inspections);
        navigationView.getMenu().removeItem(R.id.home_app);
        navigationView.getMenu().removeItem(R.id.delete_route);
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
//                    fraglayout.setCurrentItem(1);
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    HomeActivity.fraglayout.setCurrentItem(1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.upload_routes){
//                    fraglayout.setCurrentItem(0);
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.bt_config){
                    startActivity(new Intent(Inspections.this, Bluetooth.class));
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
        startActivity(new Intent(Inspections.this,PrevInspection.class).putExtra("WorkId",WorkID).putExtra("RouteID",RouteID));
        finish();
    }
}
