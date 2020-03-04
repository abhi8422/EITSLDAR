package com.envigil.extranet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.TableOfComponents.ComponentsTable;
import com.envigil.extranet.models.ComponentsListPojo;
import com.envigil.extranet.models.Employees;
import com.envigil.extranet.models.EmployeesPojo;
import com.envigil.extranet.models.LeakRepairTypes;
import com.envigil.extranet.models.LeakRepairTypesPojo;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RepairRequest extends AppCompatActivity implements View.OnClickListener {

    TextView tvunit,tvampm,tvTagNo,tvCompoRepair, tvSizeRepair, tvLocaRepair, tvDateRepair, tvTimeRepair;
    static EditText edPostLeak;
    static EditText liveReading;
    Button btnSveRepair;
    ImageView imgRepairDate, imgRepairTime,RepairBackBtn;
    static Double max = 0.0;
    static int i=0;
    String datetimeLeak;
    String[] repairDateTime;
    String repairDateTime1,repairDateTime2,pickedTime;
    Date date,time,leaktime;
    int addid,LeakRepairId,empId,leakRepairTypeId;
    String dateTime;
    float leakRepairRate;
    Spinner repairtypeSpinner,employeeSpinner;
    boolean last,PermOrLeak;
    boolean grid;
    int repaircompID,SubId,RouteID,InvID;
    int employeeID,LeakRepairTypeId,LeakId;
    private int mDay, mMonth, mYear;
    private int mHour, mMinute, mSeconds;
    SQLiteHelper sqLiteHelper;
    List<ComponentsListPojo> repairComponents = new ArrayList<>();
    List<LeakRepairTypesPojo> repairTypesList = new ArrayList<>();
    List<Integer> leakRepairTypeID = new ArrayList<>();
    List<String> leaskReapirTypeName = new ArrayList<>();
    List<EmployeesPojo> employeesList = new ArrayList<>();
    List<Integer> employeesListID = new ArrayList<>();
    List<String> employeesListFirst = new ArrayList<>();
    List<String> employeesListLast = new ArrayList<>();
    String Unit,AM_PM;
    private DrawerLayout drawerLayout;
    float leakRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_request);

        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        tvCompoRepair = findViewById(R.id.tv_component_repair);
        tvSizeRepair = findViewById(R.id.tv_size_repair_);
        tvDateRepair = findViewById(R.id.date_repair_);
        tvTimeRepair = findViewById(R.id.time_repair);
        edPostLeak = findViewById(R.id.ed_post_leak);
        /*liveReading=findViewById(R.id.liveReading);
        liveReading.setEnabled(false);*/
        imgRepairDate = findViewById(R.id.repair_date_reading);
        imgRepairTime = findViewById(R.id.repair_time_reading);
        btnSveRepair = findViewById(R.id.btn_sve_repair);
        RepairBackBtn = findViewById(R.id.repairback);
        tvLocaRepair = findViewById(R.id.tv_location_repair);
        tvTagNo=findViewById(R.id.TagNo);
        tvampm=findViewById(R.id.AM_PM);
        tvunit=findViewById(R.id.unit);
        configureNavDrawer();
        configureToolbar();

        Intent intent = this.getIntent();
        repaircompID = intent.getIntExtra("CompId", 0);
        SubId = intent.getIntExtra("SubID",0);
        Unit=intent.getStringExtra("Unit");
        LeakId=intent.getIntExtra("LeakID",0);
        tvunit.setText(Unit);
        PermOrLeak=intent.getBooleanExtra("PermOrLeak",false);
        RouteID=intent.getIntExtra("RouteID",0);
        InvID = intent.getIntExtra("InvID",0);
        grid = intent.getBooleanExtra("Grid",false);
        datetimeLeak = intent.getStringExtra("LeakDateTime");
        repairDateTime = datetimeLeak.split(" ");
        repairDateTime1 = repairDateTime[0];
        repairDateTime2 = repairDateTime[1];

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        try {
            leaktime = simpleDateFormat.parse(datetimeLeak);
            System.out.println("LeakTime ::"+leaktime);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = dateFormat.parse(repairDateTime1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        leakRate = intent.getFloatExtra("LeakRate",0.0f);
        edPostLeak.setHint(String.valueOf(leakRate));
        last = intent.getBooleanExtra("last", false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        repairComponents = sqLiteHelper.getCompReadingValues(RouteID,SubId,InvID);
        for (int i = 0; i < repairComponents.size(); i++){
            ComponentsListPojo repairComponentListPojo = repairComponents.get(i);
            tvCompoRepair.setText(String.valueOf(repairComponentListPojo.getCompName()));
            tvSizeRepair.setText(String.valueOf(repairComponentListPojo.getInvSize()));
            tvLocaRepair.setText(String.valueOf(repairComponentListPojo.getInvLocation()));
            tvTagNo.setText(String.valueOf(repairComponentListPojo.getInvTag()));
            InvID=repairComponentListPojo.getInvID();
            break;
        }

        edPostLeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edPostLeak.getText().clear();
                return false;
            }
        });

        RepairBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RepairRequest.this, LeakReportActivity.class));
                finish();
            }
        });

        tvDateRepair.setText(getDate(RouteID));

        final Calendar calendar1 = Calendar.getInstance();
        mHour = calendar1.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar1.get(Calendar.MINUTE);
        mSeconds = calendar1.get(Calendar.SECOND);
        AM_PM = " AM";
        String mm_precede1 = "",hr_precede1="";
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
            mm_precede1 = "0";
        }
        if (mHour<10){
            hr_precede1 ="0";
        }
        pickedTime = hr_precede1 + mHour + ":" + mm_precede1 + mMinute +":00";

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
        String Date = hr_precede + mHour + ":" + mm_precede + mMinute + ":00";
        tvTimeRepair.setText(Date);
        tvampm.setText(AM_PM);


        imgRepairDate.setOnClickListener(this);
        imgRepairTime.setOnClickListener(this);
        btnSveRepair.setOnClickListener(this);

        List<String> employeeFullName = new ArrayList<>();
        employeeSpinner = findViewById(R.id.spinner_employee);
        employeeFullName.add("Select Employee Name");
        //Emp firstname list
        employeesListFirst.add(" ");
        //Emp lastname list
        employeesListLast.add(" ");
        employeesListID.add(0);
        employeesList = sqLiteHelper.getEmployeesList();
        for (int i = 0; i < employeesList.size(); i++){
            EmployeesPojo employees = employeesList.get(i);
            employeesListID.add(employees.getEmpID());
            employeesListFirst.add(employees.getEmpFirst());
            employeesListLast.add(employees.getEmpLast());
            employeeFullName.add(employees.getEmpFirst() + " " + employees.getEmpLast());
        }
        /* Employee Spinner Working*/
        ArrayAdapter<String> employeeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, employeeFullName);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeSpinner.setAdapter(employeeAdapter);

        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employeeID = employeesListID.get(position);
                /*String employeeFirst = employeesListFirst.get(position);
                String employeeLast = employeesListLast.get(position);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        repairtypeSpinner = findViewById(R.id.spinner_repair);
        repairTypesList = sqLiteHelper.getLeakRepairTypes();
        leaskReapirTypeName.add("Select Repair Type");
        leakRepairTypeID.add(0);
        for (int i = 0;i < repairTypesList.size(); i++){
            LeakRepairTypesPojo leakRepairTypes = repairTypesList.get(i);
            leakRepairTypeID.add(leakRepairTypes.getLeakRepairTypeID());
            leaskReapirTypeName.add(leakRepairTypes.getLeakRepairTypeName());
        }
        /* Repair Type Spinner Working */
        ArrayAdapter<String> repairAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, leaskReapirTypeName);
        repairAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repairtypeSpinner.setAdapter(repairAdapter);

        repairtypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LeakRepairTypeId=leakRepairTypeID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Bluetooth.RepairRequest=true;
    }


    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        if (view == imgRepairDate) {
            final Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mMonth = calendar.get(Calendar.MONTH);

            DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthofyear, int dayofmonth) {
                    tvDateRepair.setText((monthofyear + 1) + "/" + dayofmonth + "/" + year);
                }
            },mYear, mMonth, mDay);
            pickerDialog.getDatePicker().setMinDate(date.getTime());
            pickerDialog.show();
        }

        if (view == imgRepairTime) {
             Calendar calendar = Calendar.getInstance();
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
            mSeconds = calendar.get(Calendar.SECOND);
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
            pickedTime = hr_precede + mHour + ":" + mm_precede + mMinute +":00";
            System.out.println("pickedTime ::"+pickedTime);


            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    AM_PM = " AM";
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
                    pickedTime = hr_precede + hourOfDay + ":" + mm_precede + minute +":00";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                    try {
                        time = simpleDateFormat.parse(pickedTime);
                        System.out.println("time (on timepicker listener) ::"+time);
                    }
                    catch(ParseException e){
                        e.printStackTrace();
                    }
                    tvTimeRepair.setText(pickedTime);
                    tvampm.setText(AM_PM);
                }
            },Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),false);
            timePickerDialog.show();
        }

        if (view == btnSveRepair) {
            view.startAnimation(AnimationUtils.loadAnimation(RepairRequest.this, R.animator.animate));

            String postleak = edPostLeak.getText().toString().trim();

           // System.out.println("Postleak ::"+postleak);
            System.out.println("Comparing Time"+leaktime+" && "+time);
            if (employeeSpinner.getSelectedItem().toString().equals("Select Employee Name")){

               AlertDialog.Builder builder = new AlertDialog.Builder(RepairRequest.this);
                builder.setCancelable(false);
                builder.setTitle("Please Select Employee Name ");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();

            }
            else if(repairtypeSpinner.getSelectedItem().toString().equals("Select Repair Type")){
                AlertDialog.Builder builder = new AlertDialog.Builder(RepairRequest.this);
                builder.setCancelable(false);
                builder.setTitle("Please Select Repair Type ");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
            }
            else if(postleak.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(RepairRequest.this);
                builder.setCancelable(false);
                builder.setTitle("Please Enter Post Leak Rate");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                builder.create();
                builder.show();
            }
            /*pickedTime.compareTo(repairDateTime2) < 0*/
            else if (leaktime.before(time)){
                AlertDialog.Builder builder = new AlertDialog.Builder(RepairRequest.this);
                builder.setCancelable(false);
                builder.setTitle("Invalid Repair Time");
                builder.setMessage("Repair time cannot be previous to leak time.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
            }
            else{
                //Get All Data And Insert Into Leak Repairs table
                //Get LeakRepair Id from the table and whose value is max +1.
                addid = sqLiteHelper.getmaxLeakRepairID();
                if (addid==0){
                    LeakRepairId=1;
                }
                else {
                    LeakRepairId = addid+1;
                }
                //LeakId Will Come From LeakReport Activity.
                empId = employeeID;
                dateTime = tvDateRepair.getText().toString()+" "+tvTimeRepair.getText().toString() + AM_PM;
                System.out.println(dateTime);
                leakRepairTypeId = LeakRepairTypeId;
                leakRepairRate = Float.parseFloat(edPostLeak.getText().toString());
//&& dateTime.compareTo(datetimeLeak) < 0
                AlertDialog.Builder builder = new AlertDialog.Builder(RepairRequest.this);
                builder.setCancelable(false);
                builder.setTitle("Repair Request Successful");
                builder.setMessage("Repair request was sent successfully");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteHelper.InsertLeakRepair(LeakRepairId,LeakId,empId,leakRepairTypeId,leakRepairRate,dateTime);
                        if (last){
                            if (grid){
                                startActivity(new Intent(RepairRequest.this, ComponentsTable.class).putExtra("SubId",SubId).putExtra("RouteID",RouteID).putExtra("InvID",InvID));
                                finish();
                            }
                            else {
                                startActivity(new Intent(RepairRequest.this, ComponentDashboard.class).putExtra("SubId",SubId).putExtra("RouteID",RouteID).putExtra("InvID",InvID));
                                finish();
                            }
                            dialog.cancel();
                            finish();
                        }
                        else {
                            boolean next=true;
                            startActivity(new Intent(RepairRequest.this, ComponentReading.class).putExtra("SubId",SubId).putExtra("RouteID",RouteID).putExtra("InvID",InvID).putExtra("CompId",repaircompID).putExtra("next",next).putExtra("Grid",grid));
                            dialog.cancel();
                            finish();
                        }

                    }
                });
                builder.create();
                builder.show();
                /*if (leakRepairRate < leakRate ){
                }
//                else if (leakRepairRate < leakRate && dateTime.compareTo(datetimeLeak) > 0){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(RepairRequest.this);
//                    builder.setCancelable(false);
//                    builder.setTitle("Date Time Error");
//                    builder.setMessage("Repair DateTime cannot be previous than Leak DateTime");
//                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                           dialog.dismiss();
//                        }
//                    });
//                    builder.create();
//                    builder.show();
//                }
                else{
                    edPostLeak.setError("Leak value Cannot be greater than Leak Rate");
                }*/
            }

        }
    }

    //Navigation Drawer

    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.nav_toolbar_leak_repair);
        setSupportActionBar(nav_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.activity_repair_request);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_leak_repair);
        navigationView.getMenu().removeItem(R.id.home_app);
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
                    startActivity(new Intent(RepairRequest.this, Bluetooth.class));

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        boolean next=true;
        startActivity(new Intent(RepairRequest.this,LeakReportActivity.class).putExtra("SubId",SubId).putExtra("RouteID",RouteID).putExtra("InvID",InvID).putExtra("CompId",repaircompID).putExtra("Unit",Unit).putExtra("PermOrLeak",PermOrLeak).putExtra("LeakRate",leakRate).putExtra("Grid",grid));
//        startActivity(new Intent(RepairRequest.this, ComponentReading.class).putExtra("SubId",SubId).putExtra("RouteID",RouteID).putExtra("InvID",InvID).putExtra("CompId",repaircompID).putExtra("next",next).putExtra("Grid",grid));
        finish();
    }

   /* public static Handler RepairRequestHadler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 7:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempmsg=new String(readBuff,0,msg.arg1);
                    tempmsg=tempmsg.replace("\r\n","#");
                    String[] FilterString=tempmsg.split("#");
                    String fString;
                    try{
                        for(String s:FilterString){
                            fString=s.replace("OK", " ").trim();
                            if(fString.length()!=0){
                                if(isNumber(fString)){
                                    if(i==0){
                                        max=Double.valueOf(fString);
                                        i++;
                                    }
                                    if (max > Double.valueOf(fString)) {
                                        edPostLeak.setText(String.valueOf(max));
                                    }else {
                                        max=Double.valueOf(fString);
                                        edPostLeak.setText(String.valueOf(max));
                                    }
                                    liveReading.setText(fString);
                                }
                            }
                        }
                    }catch (NumberFormatException ex){
                        ex.printStackTrace();
                        break;
                    }
            }
            return true;
        }
    });*/

   /* public static boolean isNumber(String num){
        try{
            Double.parseDouble(num);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }*/

    public String getDate(int RouteID){
        String date;
        SQLiteHelper sqLiteHelper=new SQLiteHelper(getApplicationContext());
        date=sqLiteHelper.getRouteConfigDate(RouteID);
        return date;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        max=0.0;
        i=0;
        Bluetooth.RepairRequest=false;
       // RepairRequestHadler.removeCallbacksAndMessages(null);
    }
}
