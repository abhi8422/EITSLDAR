package com.envigil.extranet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Pair;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.TableOfComponents.ComponentsTable;
import com.envigil.extranet.models.ComponentsListPojo;
import com.envigil.extranet.models.ReasonSkipped;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ComponentReading extends AppCompatActivity implements View.OnClickListener {

    Context context;
    Spinner reasonSpinner;
    private DrawerLayout drawerLayout;
    TextView tvAreaName,tvampm,tvComponent,tvSubArea, tvSize,txtreading,tvLocation,tvBackground,tvCritical,tvTAG,tvppm,tvdpm,tvlel,tvunit;
    ImageView ComponentReadingBackBtn;
    Button btnSave;
    ImageButton btnNext,btnPrevious;
    static EditText edReading,livereading;
    EditText lelreading,dpmreading;
    String sub_name,inspected;
    SwitchMaterial skip_comp_switch;
    int compId,Count,LeakTypeID=1,PermRateId,CompTypeId,StrId,StrTypeId,RuleId,RuleCompTypeId,SelectedReasonSkippedId,SubID,RouteId, InvId, PermId,invOrder,maxOrder;
    int[] FacId,CompStrTypeIDs;
    float BackgroundReading,PermStartRate,LeakRateStart,reading,LeakRateStartStr;
    static Double max = 0.0;
    List<ComponentsListPojo> readinglistPojos = new ArrayList<>();
    List<ReasonSkipped> reasonSkippedListPojos = new ArrayList<>();
    List<String> reasonSkippedList = new ArrayList<>();
    List<Integer> reasonSkippedListID = new ArrayList<>();
    SQLiteHelper sqLiteHelper;
    AlertDialog.Builder builder;
    //Permit threshold or leak thershold
    boolean PermOrLeak;
    boolean grid;
    static Chronometer chronometer;
    boolean running=false;
    static int i=0;
    boolean next=false,last = false;
    ImageView InspTick;
    ArrayAdapter<String> adapter;
    LinearLayout spinner_skip_reason_layout;
    String check;
    //ppm,dpm,lel flag
    private static boolean skip_cpm=true;
    public static boolean ppm_flag;
    int FirstOrder;
    TextInputLayout live_layout,max_layout;
    TextView txt_ppm;
    static ConstraintLayout layout;
    static ScrollView scrollView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_reading);
        ppm_flag=true;
        //db connection happening here
        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        live_layout=findViewById(R.id.live_layout);
        max_layout=findViewById(R.id.max_layout);
        txt_ppm=findViewById(R.id.textView7);
        tvComponent = findViewById(R.id.tv_component);
        tvSubArea = findViewById(R.id.tv_sub_area);
        tvSize = findViewById(R.id.tv_size_);
        tvLocation=findViewById(R.id.tv_location);
        InspTick= findViewById(R.id.InspTick);
        tvBackground = findViewById(R.id.tv_bckgrnd);
        tvAreaName = findViewById(R.id.tv_area_name);
        lelreading=findViewById(R.id.lel_reading);
        dpmreading=findViewById(R.id.dpm_reading);
        layout=findViewById(R.id.progress_layout);
        scrollView=findViewById(R.id.scroll_layout);
        //Drawer
        configureNavDrawer();
        configureToolbar();

        txtreading=findViewById(R.id.reading);
        edReading = findViewById(R.id.ed_reading);
        livereading=findViewById(R.id.liveReading);
        livereading.setEnabled(false);
        ComponentReadingBackBtn=findViewById(R.id.ComponentReadingBackBtn);
        ComponentReadingBackBtn.setOnClickListener(this);
        btnSave = findViewById(R.id.btn_save);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        tvCritical = findViewById(R.id.tv_critical);
        tvTAG = findViewById(R.id.Tag_No);
        tvppm=findViewById(R.id.tv_comp_ppm);
        tvppm.setOnClickListener(this);
        tvdpm=findViewById(R.id.tv_comp_dpm);
        tvdpm.setOnClickListener(this);
        tvdpm.setBackgroundColor(Color.GRAY);
        tvlel=findViewById(R.id.tv_comp_lel);
        tvlel.setOnClickListener(this);
        tvlel.setBackgroundColor(Color.GRAY);
        spinner_skip_reason_layout=findViewById(R.id.spinner_skip_reason_layout);

        tvunit = findViewById(R.id.unit);
        tvampm=findViewById(R.id.am_pm);
        //Timer.
        chronometer=findViewById(R.id.chronometer);

        if (!running){
            chronometer.start();
        }

        //make maxreading edit text clera when use click to enter value
        edReading.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edReading.getText().clear();
                return false;
            }
        });


        // to get the compid from component list screen
        Intent intent = this.getIntent();
        compId = intent.getIntExtra("CompId",0);
        SubID=intent.getIntExtra("SubId",0);
        RouteId=intent.getIntExtra("RouteID", 0);
        InvId = intent.getIntExtra("InvID", 0);
        next = intent.getBooleanExtra("next",false);
        grid = intent.getBooleanExtra("Grid",false);
        reading=intent.getFloatExtra("Reading",0.0f);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        reasonSpinner = findViewById(R.id.spinner_skip_reason);
        reasonSkippedList.add("Select Skip Reason");
        reasonSkippedListID.add(0);
        //Populating ReasonSkipped Dropdown
        reasonSkippedListPojos = sqLiteHelper.getReasonSkipped();
        for(int i=0;i<reasonSkippedListPojos.size();i++){
            ReasonSkipped reasonSkipped=reasonSkippedListPojos.get(i);

            if (!reasonSkipped.getReasonSkipped().equals(" ")){
                reasonSkippedList.add(reasonSkipped.getReasonSkipped());
                reasonSkippedListID.add(reasonSkipped.getReasonSkippedID());
            }

        }

        /* Reason Skipped Spinner Working */
       adapter = new ArrayAdapter<>(this, R.layout.spinner_layout,reasonSkippedList);
        reasonSpinner.setAdapter(adapter);
        //spinnner click listner
        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedReasonSkippedId=reasonSkippedListID.get(position);
                if(position==0){
                    edReading.setEnabled(true);
                    skip_cpm=true;
                }else {
                    edReading.setEnabled(false);
                    skip_cpm=false;
                    edReading.setText(" ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        invOrder=sqLiteHelper.getInvOrder(RouteId,SubID,InvId);
        if(next){
            nextComp();
            next=false;
        }

        else{
            readinglistPojos = sqLiteHelper.getCompReadingValues(RouteId,SubID,InvId);

            for (int i = 0; i < readinglistPojos.size(); i++) {
                ComponentsListPojo componentsListPojo1 = readinglistPojos.get(i);
                tvComponent.setText(String.valueOf(componentsListPojo1.getCompName()));
                tvSize.setText(String.valueOf(componentsListPojo1.getInvSize()));
                tvAreaName.setText(componentsListPojo1.getAreaName());
                tvLocation.setText(String.valueOf(componentsListPojo1.getInvLocation()));
                tvBackground.setText(String.format("%.2f",componentsListPojo1.getBackread()+0.00));
                tvSubArea.setText(componentsListPojo1.getSubName());
                inspected = componentsListPojo1.getInspected();
                int comp=Float.compare(0.0f,componentsListPojo1.getReading());
                if(comp > 0) {
                    txtreading.setText(componentsListPojo1.getReading()+" ppm");
                } else if(comp < 0) {
                    txtreading.setText(componentsListPojo1.getReading()+" ppm");
                } else {
                    txtreading.setText("");
                }

                if (inspected.equals("true")||inspected.equals("1")){
                    InspTick.setVisibility(View.VISIBLE);

                }
                else{
                    InspTick.setVisibility(View.GONE);
                }
                if (!(componentsListPojo1.getSkippedID()==0)){
                        int skipppedId=componentsListPojo1.getSkippedID();
                        String Reason=sqLiteHelper.getReason(skipppedId);
                        int pos=adapter.getPosition(Reason);
                        reasonSpinner.setSelection(pos);
                }
                else{
                    reasonSpinner.setSelection(0);
                }
                boolean Critical = componentsListPojo1.getCritical();
                if (!Critical){
                    tvCritical.setTextColor(Color.parseColor("#ff669900"));
                    tvCritical.setText("NO");
                }
                else{
                    tvCritical.setTextColor(Color.parseColor("#ff0000"));
                    tvCritical.setText("YES");
                }
                tvTAG.setText(componentsListPojo1.getInvTag());
                invOrder = sqLiteHelper.getInvOrder(RouteId,SubID,componentsListPojo1.getInvID());
                InvId=componentsListPojo1.getInvID();
                break;  //to get out of the FOR loop
            }

        }
        //Populating screen values using InvID
        /* Sending data to the leak report activity */
        // to Find AM PM
         maxOrder=sqLiteHelper.getLastInvOrder(RouteId,SubID);
        FirstOrder=sqLiteHelper.getFirstOrder(RouteId,SubID);

        if (invOrder==maxOrder){
             last=true;
             btnNext.setBackgroundResource(R.drawable.button_layout_onclick);
             btnNext.setClickable(false);
         }
         else if (invOrder==FirstOrder){
             btnPrevious.setBackgroundResource(R.drawable.button_layout_onclick);
             btnPrevious.setClickable(false);
         }

//Next Button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                max=0.0;

                btnPrevious.setClickable(true);
//                InvId=sqLiteHelper.getInvIdNext(RouteId,SubID,invOrder);
                btnPrevious.setBackgroundResource(R.drawable.button_layout);
                edReading.setText(" ");
                readinglistPojos = sqLiteHelper.NextCompReadingValues(RouteId,SubID,InvId,invOrder);
                maxOrder=sqLiteHelper.getLastInvOrder(RouteId,SubID);
                if (invOrder==maxOrder){

                    builder = new AlertDialog.Builder(ComponentReading.this);
                    //Setting message manually and performing action on button click
                    builder.setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                    startActivity(new Intent(getApplicationContext(),ComponentDashboard.class).putExtra("RouteID",RouteId).putExtra("SubId",SubID));
                                    dialog.cancel();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("This is the last component !!");
                    alert.show();
                    btnNext.setBackgroundResource(R.drawable.button_layout_onclick);
                    btnNext.setClickable(false);
                }


                else{
                    layout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    new Handler().postDelayed(new ProgressRunnable(),4000);

//                Toast.makeText(ComponentReading.this, "InvID : "+InvId,  Toast.LENGTH_SHORT).show();
                    v.startAnimation(AnimationUtils.loadAnimation(ComponentReading.this, R.animator.animate));

                    for (int i = 0; i < readinglistPojos.size(); i++) {

//            Toast.makeText(getApplicationContext(),"Value of CompName: "+ componentsListPojo.getInspected(),Toast.LENGTH_SHORT).show();
//                    tvComponent.setText();
                        ComponentsListPojo componentsListPojo = readinglistPojos.get(i);
                        String reading=String.valueOf(componentsListPojo.getReading());
                        tvComponent.setText(String.valueOf(componentsListPojo.getCompName()));
                        /*tvSize.setText(String.valueOf(componentsListPojo.getInvSize()));*/
                        tvSize.setText(String.valueOf(componentsListPojo.getReading()));
                        tvLocation.setText(String.valueOf(componentsListPojo.getInvLocation()));
                        tvAreaName.setText(componentsListPojo.getAreaName());
                        tvBackground.setText(String.format("%.2f",componentsListPojo.getBackread()+0.00));
                        tvSubArea.setText(componentsListPojo.getSubName());
                        inspected = componentsListPojo.getInspected();
                        int comp=Float.compare(0.0f,componentsListPojo.getReading());
                        if(comp > 0) {
                            txtreading.setText(componentsListPojo.getReading()+" PPM");
                        } else if(comp < 0) {
                            txtreading.setText(componentsListPojo.getReading()+" PPM");
                        } else {
                            txtreading.setText("");
                        }
                        if (inspected.equals("true")||inspected.equals("1")){

                            InspTick.setVisibility(View.VISIBLE);
                        }
                        else{
                            InspTick.setVisibility(View.GONE);
                        }
                        if (!(componentsListPojo.getSkippedID() ==0)){
                            int skipppedId=componentsListPojo.getSkippedID();
                            String Reason=sqLiteHelper.getReason(skipppedId);
                            int pos=adapter.getPosition(Reason);
                            reasonSpinner.setSelection(pos);
                        }
                        else{
                            reasonSpinner.setSelection(0);
                        }

                        boolean Critical = componentsListPojo.getCritical();
                        if (!Critical){
                            tvCritical.setTextColor(Color.parseColor("#ff669900"));
                            tvCritical.setText("NO");
                        }
                        else{
                            tvCritical.setTextColor(Color.parseColor("#ff0000"));
                            tvCritical.setText("YES");
                        }
                        tvTAG.setText(componentsListPojo.getInvTag());
                        InvId=componentsListPojo.getInvID();
                        invOrder = sqLiteHelper.getInvOrder(RouteId,SubID,componentsListPojo.getInvID());
//            Toast.makeText(ComponentReading.this, "InvOrder :: "+invOrder, Toast.LENGTH_SHORT).show();
                        break;  //to get out of the FOR loop
                    }
                }


            }
        });


        //Previous Button

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                max=0.0;
                btnNext.setBackgroundResource(R.drawable.button_layout);
                readinglistPojos = sqLiteHelper.PreviousCompReadingValues(RouteId,SubID,InvId,invOrder);
                if (invOrder==FirstOrder){
                    builder = new AlertDialog.Builder(ComponentReading.this);
                    //Setting message manually and performing action on button click
                    builder.setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("This is the first component !!");
                    alert.show();
                  btnPrevious.setBackgroundResource(R.drawable.button_layout_onclick);
                  btnPrevious.setClickable(false);
                }
                else{
                    layout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    new Handler().postDelayed(new ProgressRunnable(),4000);
                    btnNext.setClickable(true);
                    v.startAnimation(AnimationUtils.loadAnimation(ComponentReading.this, R.animator.animate));
                    edReading.setText(" ");

                    for (int i = 0; i < readinglistPojos.size(); i++) {
                        ComponentsListPojo componentsListPojo = readinglistPojos.get(0);
                        tvComponent.setText(String.valueOf(componentsListPojo.getCompName()));
                        tvSize.setText(String.valueOf(componentsListPojo.getInvSize()));
                        tvLocation.setText(String.valueOf(componentsListPojo.getInvLocation()));
                        tvAreaName.setText(componentsListPojo.getAreaName());
                        tvBackground.setText(String.format("%.2f",componentsListPojo.getBackread()+0.00));
                        tvSubArea.setText(componentsListPojo.getSubName());
                        inspected = componentsListPojo.getInspected();
                        int comp=Float.compare(0.0f,componentsListPojo.getReading());
                        if(comp > 0) {
                            txtreading.setText(componentsListPojo.getReading()+" ppm");
                        } else if(comp < 0) {
                            txtreading.setText(componentsListPojo.getReading()+" ppm");
                        } else {
                            txtreading.setText("");
                        }
                        if (inspected.equals("true")||inspected.equals("1")){
                            InspTick.setVisibility(View.VISIBLE);
                        }
                        else{
                            InspTick.setVisibility(View.GONE);
                        }
                        if (!(componentsListPojo.getSkippedID()==0)){
                            int skipppedId=componentsListPojo.getSkippedID();
                            String Reason=sqLiteHelper.getReason(skipppedId);
                            int pos=adapter.getPosition(Reason);
                            reasonSpinner.setSelection(pos);
                        }
                        else{
                            reasonSpinner.setSelection(0);
                        }
                        boolean Critical = componentsListPojo.getCritical();
                        if (!Critical){
                            tvCritical.setTextColor(Color.parseColor("#ff669900"));
                            tvCritical.setText("NO");
                        }
                        else{
                            tvCritical.setTextColor(Color.parseColor("#ff0000"));
                            tvCritical.setText("YES");
                        }
                        tvTAG.setText(componentsListPojo.getInvTag());
                        InvId=componentsListPojo.getInvID();
                        invOrder = sqLiteHelper.getInvOrder(RouteId,SubID,componentsListPojo.getInvID());
//                    Toast.makeText(ComponentReading.this, "InvOrder :: "+invOrder, Toast.LENGTH_SHORT).show();
                        break;  //to get out of the FOR loop
                    }
                }
            }
        });

//Save Button.
        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                max=0.0;
                view.startAnimation(AnimationUtils.loadAnimation(ComponentReading.this, R.animator.animate));
                chronometer.setBase(SystemClock.elapsedRealtime());
                check=edReading.getText().toString();
                final float Reading = getNumber(check);
                BackgroundReading=Float.parseFloat(tvBackground.getText().toString());
                btnPrevious.setClickable(true);
                btnPrevious.setBackgroundResource(R.drawable.button_layout);

                if (reasonSpinner.getSelectedItem().toString().trim().equals("Select Skip Reason") && (edReading.getText().toString().trim().equals(""))){
                    //reason skipped is nothing and entered reading is null
                    builder = new AlertDialog.Builder(ComponentReading.this);
                    //Setting message manually and performing action on button click
                    builder.setMessage("Please Enter Reading OR Select Skip Reason")
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
                else {
                    layout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    new Handler().postDelayed(new ProgressRunnable(),4000);

                }
                /*if (reasonSpinner.getSelectedItem().toString().trim().equals("Select Skip Reason") && (edReading.getText().toString().equals(" "))){
                    //reason skipped is nothing and entered reading is null
                    edReading.setError("Reading cannot be empty !");
                }
                else if (!reasonSpinner.getSelectedItem().toString().trim().equals("Select Skip Reason")&& !(check.equals(" "))){
                    builder = new AlertDialog.Builder(ComponentReading.this);
                    //Setting message manually and performing action on button click
                    builder.setMessage("Please select only one !")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Skipped Reason and Reading both entered !!");
                    alert.show();
                }
*/

                if (!reasonSpinner.getSelectedItem().toString().trim().equals("Select Skip Reason") && (edReading.getText().toString().trim().equals(""))){
                    //reason skipped is something and entered reading is null
//                    Toast.makeText(ComponentReading.this, "2 reason skipped is selected and entered reading is null", Toast.LENGTH_SHORT).show();
                    if (inspected.equals("true")|| inspected.equals("1")){
                        //Alert Dialog box
                        builder = new AlertDialog.Builder(ComponentReading.this);

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to proceed ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //update the inventory table with skipped reason values
                                        sqLiteHelper.UpdateReasonSkippedID(RouteId,SubID,InvId,SelectedReasonSkippedId);
                                        // Update RouteConfig Inspected Flag
                                        int countRouteInsp = 0;
                                        countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                        if (countRouteInsp == 0) {
                                            sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                        }
                                        //Update Sub Areas Inspected Flag
                                        int countInspected = 0;
                                        countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                        if (countInspected == 0) {
                                            sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                        }
                                        //redirecting to next component.
                                        nextComp();
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button

                                        dialog.dismiss();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("The Component is already inspected !");
                        alert.show();
                    }
                    else{

                        sqLiteHelper.UpdateReasonSkippedID(RouteId,SubID,InvId,SelectedReasonSkippedId);
                        // Update RouteConfig Inspected Flag
                        int countRouteInsp = 0;
                        countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                        if (countRouteInsp == 0) {
                            sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                        }
                        //Update Sub Areas Inspected Flag
                        int countInspected = 0;
                        countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                        if (countInspected == 0) {
                            sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                        }
                        //redirecting to next component.
                        nextComp();
                    }

                }

                if (reasonSpinner.getSelectedItem().toString().trim().equals("Select Skip Reason") && !(edReading.getText().toString().trim().equals(""))) {

                    if (inspected.equals("true") || inspected.equals("1")) {
                        //Alert Dialog box
                        builder = new AlertDialog.Builder(ComponentReading.this);
                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to proceed ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //Delete Previous LeakRepair and Leaks.
                                        // Delete Leak Image if existed.
                                        String path=sqLiteHelper.LeakImagePath(InvId);
                                        if (path!=null){
                                            File fp=new File(path);
                                            if (fp.exists()){
                                                fp.delete();
                                            }
                                        }
                                        sqLiteHelper.deleteReInspectLeakRepair(InvId);
                                        sqLiteHelper.deleteReInspectLeak(InvId);

                                        //Get Facility ID
                                        FacId = sqLiteHelper.getFacId(RouteId);
                                        //FacId in Int Array [] FacId[0] is FacID and FacId[1] is RuleID.
                                        RuleId = FacId[1];
                                        //Check Permit ID for Facility.
                                        PermId = sqLiteHelper.CheckPermId(FacId[0]);


                                            if (!(PermId == 0)) {
                                                Count = sqLiteHelper.getPermIdCount(RouteId, InvId, PermId);
                                                if (Count != 0) {
                                                    PermOrLeak = true;
                                                    Pair<Integer, Float> integerFloatPair = sqLiteHelper.getPemitThresholdValue(PermId, LeakTypeID);
                                                    PermRateId = integerFloatPair.first;
                                                    PermStartRate = integerFloatPair.second;
                                                } else {
                                                    PermOrLeak = false;
                                                }
                                            } else {
                                                PermOrLeak = false;
                                            }
                                            if (PermOrLeak) {
                                                if (Reading > PermStartRate) {
                                                    final String Unit = tvunit.getText().toString();
                                                    sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading,SelectedReasonSkippedId);
                                                    // Update RouteConfig Inspected Flag
                                                    int countRouteInsp = 0;
                                                    countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                                    if (countRouteInsp == 0) {
                                                        sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                                    }
                                                    //Update Sub Areas Inspected Flag
                                                    int countInspected = 0;
                                                    countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                                    if (countInspected == 0) {
                                                        sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                                    }
                                                    builder = new AlertDialog.Builder(ComponentReading.this);
                                                    //Setting message manually and performing action on button click
                                                    builder.setMessage("Navigating to Leak Summary !")
                                                            .setCancelable(false)
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    startActivity(new Intent(ComponentReading.this, LeakReportActivity.class).putExtra("CompId", compId).putExtra("SubId", SubID).putExtra("LeakRate", Reading).putExtra("Unit", Unit).putExtra("RouteID", RouteId).putExtra("InvID", InvId).putExtra("PermOrLeak", PermOrLeak).putExtra("last", last).putExtra("Grid",grid));
                                                                    finish();
                                                                    dialog.cancel();
                                                                }
                                                            });

                                                    //Creating dialog box
                                                    AlertDialog alert = builder.create();
                                                    //Setting the title manually
                                                    alert.setTitle("A Leak has been Identified !!");
                                                    alert.show();
                                                } else {

                                                    sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading,SelectedReasonSkippedId);
                                                    // Update RouteConfig Inspected Flag
                                                    int countRouteInsp = 0;
                                                    countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                                    if (countRouteInsp == 0) {
                                                        sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                                    }
                                                    //Update Sub Areas Inspected Flag
                                                    int countInspected = 0;
                                                    countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                                    if (countInspected == 0) {
                                                        sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                                    }
                                                    nextComp();
                                                }
                                            }
                                            //If Not By Permit
                                            else {
                                                //get CompTypeId ,StrID, StrTypeId.
                                                CompStrTypeIDs = sqLiteHelper.getCompStrTypeIDs(RouteId, SubID, InvId);
                                                //CompStrTypeIDs [0] = CompTypeId, [1] = StrID,[2] = StrTypeID;
                                                CompTypeId = CompStrTypeIDs[0];
                                                StrId = CompStrTypeIDs[1];
                                                StrTypeId = CompStrTypeIDs[2];
                                                //get RuleCompTypeId RuleComponentTypes and use CompTypeId and RuleID
                                                RuleCompTypeId = sqLiteHelper.getRuleCompTypeId(CompTypeId, RuleId);
                                                //get Minimum LeakStartRate for RuleCompTypeId.
                                                LeakRateStart = sqLiteHelper.getLeakRateStart(RuleCompTypeId, LeakTypeID, StrId, StrTypeId);
                                                if (LeakRateStart == 0.0) {
                                                    LeakRateStartStr = sqLiteHelper.getLeakRateStartStr(RuleCompTypeId, LeakTypeID, StrTypeId);
                                                    if (LeakRateStartStr > 0.0) {
                                                        if (Reading > LeakRateStartStr) {
                                                            final String Unit = tvunit.getText().toString();
                                                            sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading, SelectedReasonSkippedId);

                                                            // Update RouteConfig Inspected Flag
                                                            int countRouteInsp = 0;
                                                            countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                                            if (countRouteInsp == 0) {
                                                                sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                                            }

                                                            //Update Sub Areas Inspected Flag
                                                            int countInspected = 0;
                                                            countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                                            if (countInspected == 0) {
                                                                sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                                            }

                                                            builder = new AlertDialog.Builder(ComponentReading.this);
                                                            //Setting message manually and performing action on button click
                                                            builder.setMessage("Navigating to Leak Summary !")
                                                                    .setCancelable(false)
                                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            //Passing flag to Leak Summary Screen.
                                                                            startActivity(new Intent(ComponentReading.this, LeakReportActivity.class).putExtra("CompId", compId).putExtra("SubId", SubID).putExtra("LeakRate", Reading).putExtra("Unit", Unit).putExtra("RouteID", RouteId).putExtra("InvID", InvId).putExtra("PermOrLeak", PermOrLeak).putExtra("last", last).putExtra("Grid",grid));
                                                                            finish();
                                                                            dialog.cancel();
                                                                        }
                                                                    });

                                                            //Creating dialog box
                                                            AlertDialog alert = builder.create();
                                                            //Setting the title manually
                                                            alert.setTitle("A Leak has been Identified !!");
                                                            alert.show();
                                                        }
                                                        else {

                                                            sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading, SelectedReasonSkippedId);
                                                            // Update RouteConfig Inspected Flag
                                                            int countRouteInsp = 0;
                                                            countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                                            if (countRouteInsp == 0) {
                                                                sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                                            }
                                                            //Update Sub Areas Inspected Flag
                                                            int countInspected = 0;
                                                            countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                                            if (countInspected == 0) {
                                                                sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                                            }
                                                            nextComp();
                                                        }
                                                    }
                                                    else {

                                                        sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading, SelectedReasonSkippedId);
                                                        // Update RouteConfig Inspected Flag
                                                        int countRouteInsp = 0;
                                                        countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                                        if (countRouteInsp == 0) {
                                                            sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                                        }
                                                        //Update Sub Areas Inspected Flag
                                                        int countInspected = 0;
                                                        countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                                        if (countInspected == 0) {
                                                            sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                                        }
                                                        nextComp();
                                                    }

                                                }
                                                else {
                                                    if (Reading > LeakRateStart) {
                                                        final String Unit = tvunit.getText().toString();
                                                        sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading, SelectedReasonSkippedId);

                                                        // Update RouteConfig Inspected Flag
                                                        int countRouteInsp = 0;
                                                        countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                                        if (countRouteInsp == 0) {
                                                            sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                                        }

                                                        //Update Sub Areas Inspected Flag
                                                        int countInspected = 0;
                                                        countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                                        if (countInspected == 0) {
                                                            sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                                        }

                                                        builder = new AlertDialog.Builder(ComponentReading.this);
                                                        //Setting message manually and performing action on button click
                                                        builder.setMessage("Navigating to Leak Summary !")
                                                                .setCancelable(false)
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        //Passing flag to Leak Summary Screen.
                                                                        startActivity(new Intent(ComponentReading.this, LeakReportActivity.class).putExtra("CompId", compId).putExtra("SubId", SubID).putExtra("LeakRate", Reading).putExtra("Unit", Unit).putExtra("RouteID", RouteId).putExtra("InvID", InvId).putExtra("PermOrLeak", PermOrLeak).putExtra("last", last).putExtra("Grid",grid));
                                                                        finish();
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                        //Creating dialog box
                                                        AlertDialog alert = builder.create();
                                                        //Setting the title manually
                                                        alert.setTitle("A Leak has been Identified !!");
                                                        alert.show();
                                                    }
                                                    else {

                                                        sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading, SelectedReasonSkippedId);
                                                        // Update RouteConfig Inspected Flag
                                                        int countRouteInsp = 0;
                                                        countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                                        if (countRouteInsp == 0) {
                                                            sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                                        }
                                                        //Update Sub Areas Inspected Flag
                                                        int countInspected = 0;
                                                        countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                                        if (countInspected == 0) {
                                                            sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                                        }
                                                        nextComp();
                                                    }
                                                }

                                            }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button

                                        dialog.dismiss();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("The Component is already inspected !");
                        alert.show();
                    }
                    //If Not Inspected
                    else {
                        //reason skipped is nothing and entered reading is not null

                        //Get Facility ID
                        FacId = sqLiteHelper.getFacId(RouteId);
                        //FacId in Int Array [] FacId[0] is FacID and FacId[1] is RuleID.
                        RuleId = FacId[1];
                        //Check Permit ID for Facility.
                        PermId = sqLiteHelper.CheckPermId(FacId[0]);


                            if (!(PermId == 0)) {
                                //Check If Permit is applicable to Inventory

                                Count = sqLiteHelper.getPermIdCount(RouteId, InvId, PermId);
                                if (Count != 0) {
                                    PermOrLeak = true;
                                    Pair<Integer, Float> integerFloatPair = sqLiteHelper.getPemitThresholdValue(PermId, LeakTypeID);
                                    PermRateId = integerFloatPair.first;
                                    PermStartRate = integerFloatPair.second;
                                } else {
                                    PermOrLeak = false;

                                }
                            }
                            else {
                                PermOrLeak = false;
                            }
                            if (PermOrLeak) {

                                if (Reading > PermStartRate) {
                                    final String Unit = tvunit.getText().toString();
                                    sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading,SelectedReasonSkippedId);
                                    // Update RouteConfig Inspected Flag
                                    int countRouteInsp = 0;
                                    countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                    if (countRouteInsp == 0) {
                                        sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                    }

                                    //Update Sub Areas Inspected Flag
                                    int countInspected = 0;
                                    countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                    if (countInspected == 0) {
                                        sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                    }
                                    builder = new AlertDialog.Builder(ComponentReading.this);
                                    //Setting message manually and performing action on button click
                                    builder.setMessage("Navigating to Leak Summary !")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    startActivity(new Intent(ComponentReading.this, LeakReportActivity.class).putExtra("CompId", compId).putExtra("SubId", SubID).putExtra("LeakRate", Reading).putExtra("Unit", Unit).putExtra("RouteID", RouteId).putExtra("InvID", InvId).putExtra("PermOrLeak", PermOrLeak).putExtra("last", last).putExtra("Grid",grid));
                                                    finish();
                                                    dialog.cancel();
                                                }
                                            });

                                    //Creating dialog box
                                    AlertDialog alert = builder.create();
                                    //Setting the title manually
                                    alert.setTitle("A Leak has been Identified !!");
                                    alert.show();
                                }
                                else {

                                    sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading,SelectedReasonSkippedId);
                                    // Update RouteConfig Inspected Flag
                                    int countRouteInsp = 0;
                                    countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                    if (countRouteInsp == 0) {
                                        sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                    }
                                    //Update Sub Areas Inspected Flag
                                    int countInspected = 0;
                                    countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                    if (countInspected == 0) {
                                        sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                    }
                                    nextComp();
                                }
                            }
                            //IF Not by Permit

                            else {
                                //get CompTypeId ,StrID, StrTypeId.
                                CompStrTypeIDs = sqLiteHelper.getCompStrTypeIDs(RouteId, SubID, InvId);
                                //CompStrTypeIDs [0] = CompTypeId, [1] = StrID,[2] = StrTypeID;
                                CompTypeId = CompStrTypeIDs[0];
                                StrId = CompStrTypeIDs[1];
                                StrTypeId = CompStrTypeIDs[2];
                                //get RuleCompTypeId RuleComponentTypes and use CompTypeId and RuleID
                                RuleCompTypeId = sqLiteHelper.getRuleCompTypeId(CompTypeId, RuleId);
                                //get Minimum LeakStartRate for RuleCompTypeId.
                                LeakRateStart = sqLiteHelper.getLeakRateStart(RuleCompTypeId, LeakTypeID, StrId, StrTypeId);
//                                Toast.makeText(ComponentReading.this, "LeakRateStart:: "+LeakRateStart, Toast.LENGTH_SHORT).show();
                                if (LeakRateStart == 0.0) {

                                    LeakRateStartStr = sqLiteHelper.getLeakRateStartStr(RuleCompTypeId, LeakTypeID, StrTypeId);
//                                    Toast.makeText(ComponentReading.this, "LeakRateStartStr :: "+LeakRateStartStr, Toast.LENGTH_SHORT).show();
                                    if (LeakRateStartStr > 0.0) {
                                        if (Reading > LeakRateStartStr) {
                                            final String Unit = tvunit.getText().toString();
                                            sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading, SelectedReasonSkippedId);

                                            // Update RouteConfig Inspected Flag
                                            int countRouteInsp = 0;
                                            countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                            if (countRouteInsp == 0) {
                                                sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                            }

                                            //Update Sub Areas Inspected Flag
                                            int countInspected = 0;
                                            countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                            if (countInspected == 0) {
                                                sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                            }

                                            builder = new AlertDialog.Builder(ComponentReading.this);
                                            //Setting message manually and performing action on button click
                                            builder.setMessage("Navigating to Leak Summary !")
                                                    .setCancelable(false)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            //Passing flag to Leak Summary Screen.
                                                            startActivity(new Intent(ComponentReading.this, LeakReportActivity.class).putExtra("CompId", compId).putExtra("SubId", SubID).putExtra("LeakRate", Reading).putExtra("Unit", Unit).putExtra("RouteID", RouteId).putExtra("InvID", InvId).putExtra("PermOrLeak", PermOrLeak).putExtra("last", last).putExtra("Grid",grid));
                                                            finish();
                                                            dialog.cancel();
                                                        }
                                                    });

                                            //Creating dialog box
                                            AlertDialog alert = builder.create();
                                            //Setting the title manually
                                            alert.setTitle("A Leak has been Identified !!");
                                            alert.show();
                                        }
                                        else {

                                            sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading, SelectedReasonSkippedId);
                                            // Update RouteConfig Inspected Flag
                                            int countRouteInsp = 0;
                                            countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                            if (countRouteInsp == 0) {
                                                sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                            }
                                            //Update Sub Areas Inspected Flag
                                            int countInspected = 0;
                                            countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                            if (countInspected == 0) {
                                                sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                            }
                                            nextComp();
                                        }
                                    }
                                    else {

                                        sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading, SelectedReasonSkippedId);
                                        // Update RouteConfig Inspected Flag
                                        int countRouteInsp = 0;
                                        countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                        if (countRouteInsp == 0) {
                                            sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                        }
                                        //Update Sub Areas Inspected Flag
                                        int countInspected = 0;
                                        countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                        if (countInspected == 0) {
                                            sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                        }
                                        nextComp();
                                    }

                                }
                                else {
                                    if (Reading > LeakRateStart) {
                                        final String Unit = tvunit.getText().toString();
                                        sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading, SelectedReasonSkippedId);

                                        // Update RouteConfig Inspected Flag
                                        int countRouteInsp = 0;
                                        countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                        if (countRouteInsp == 0) {
                                            sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                        }

                                        //Update Sub Areas Inspected Flag
                                        int countInspected = 0;
                                        countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                        if (countInspected == 0) {
                                            sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                        }

                                        builder = new AlertDialog.Builder(ComponentReading.this);
                                        //Setting message manually and performing action on button click
                                        builder.setMessage("Navigating to Leak Summary !")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //Passing flag to Leak Summary Screen.
                                                        startActivity(new Intent(ComponentReading.this, LeakReportActivity.class).putExtra("CompId", compId).putExtra("SubId", SubID).putExtra("LeakRate", Reading).putExtra("Unit", Unit).putExtra("RouteID", RouteId).putExtra("InvID", InvId).putExtra("PermOrLeak", PermOrLeak).putExtra("last", last).putExtra("Grid",grid));
                                                        finish();
                                                        dialog.cancel();
                                                    }
                                                });

                                        //Creating dialog box
                                        AlertDialog alert = builder.create();
                                        //Setting the title manually
                                        alert.setTitle("A Leak has been Identified !!");
                                        alert.show();
                                    }
                                    else {

                                        sqLiteHelper.UpdateCompReading(RouteId, SubID, InvId, Reading, SelectedReasonSkippedId);
                                        // Update RouteConfig Inspected Flag
                                        int countRouteInsp = 0;
                                        countRouteInsp = sqLiteHelper.getRouteInspCount(RouteId);
                                        if (countRouteInsp == 0) {
                                            sqLiteHelper.UpdateRouteConfigInspected(RouteId);
                                        }
                                        //Update Sub Areas Inspected Flag
                                        int countInspected = 0;
                                        countInspected = sqLiteHelper.getInspectedCount(RouteId, SubID);
                                        if (countInspected == 0) {
                                            sqLiteHelper.UpdateSubAreaInspected(RouteId, SubID);
                                        }
                                        nextComp();
                                    }
                                }
                            }
                    }
                }

            }

        });
        Bluetooth.CompBackReading=true;
    }

    private float getNumber(String check) {

        try {
            return Float.parseFloat(check);
        }catch (NumberFormatException e){
            return 0;
        }

    }

    @Override
    public void onClick(View view) {
        if (view==tvppm){
            LeakTypeID=1;
            tvppm.setBackgroundColor(Color.parseColor("#8BC34A"));
            tvdpm.setBackgroundColor(Color.GRAY);
            tvlel.setBackgroundColor(Color.GRAY);
            tvunit.setText("PPM");
            live_layout.setVisibility(View.VISIBLE);
            txt_ppm.setVisibility(View.VISIBLE);
            ppm_flag=true;
        }
        if (view==tvdpm){
            ppm_flag=false;
            live_layout.setVisibility(View.GONE);
            txt_ppm.setVisibility(View.GONE);
            LeakTypeID=2;
            edReading.getText().clear();
            tvppm.setBackgroundColor(Color.GRAY);
            tvdpm.setBackgroundColor(Color.parseColor("#8BC34A"));
            tvlel.setBackgroundColor(Color.GRAY);
            tvunit.setText("DPM");

        }
        if (view==tvlel){
            ppm_flag=false;
            edReading.getText().clear();
            live_layout.setVisibility(View.GONE);
            txt_ppm.setVisibility(View.GONE);
            LeakTypeID=3;
            tvppm.setBackgroundColor(Color.GRAY);
            tvdpm.setBackgroundColor(Color.GRAY);
            tvlel.setBackgroundColor(Color.parseColor("#8BC34A"));
            tvunit.setText("LEL");

        }

        if(view==ComponentReadingBackBtn){
            startActivity(new Intent(ComponentReading.this, ComponentDashboard.class).putExtra("SubId",SubID));
            finish();
        }
    }


    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.nav_toolbar_screening_values);
        setSupportActionBar(nav_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.ComponentReadingDrawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_screening_value);
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
                }
                else if (menuId == R.id.down_routes) {
//                    fraglayout.setCurrentItem(1);
//                    startActivity(new Intent(ComponentDashboard.this,HomeActivity.class));
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    HomeActivity.fraglayout.setCurrentItem(1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.upload_routes) {
//                    fraglayout.setCurrentItem(0);
                    TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.bt_config) {
                    startActivity(new Intent(ComponentReading.this, Bluetooth.class));

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
        if (grid){
            startActivity(new Intent(ComponentReading.this, ComponentsTable.class).putExtra("SubId",SubID).putExtra("RouteID",RouteId));
            finish();
        }
        else {
            startActivity(new Intent(ComponentReading.this, ComponentDashboard.class).putExtra("SubId",SubID).putExtra("RouteID",RouteId));
            finish();
        }


    }


    public static Handler CompBackReadingHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 7:
                    if(skip_cpm){
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
                                            if(ppm_flag){
                                                edReading.setText(String.valueOf(max));
                                            }
                                        }else {
                                            max=Double.valueOf(fString);
                                            if(ppm_flag){
                                                edReading.setText(String.valueOf(max));
                                            }
                                        }
                                        livereading.setText(fString);
                                    }
                                }
                            }
                        }catch (NumberFormatException ex){
                            ex.printStackTrace();
                            break;
                        }
                    }
            }
            return true;
        }
    });
    public static boolean isNumber(String num){
        try{
            Double.parseDouble(num);
            return true;
        }catch (NumberFormatException e){
            return false;
        }

    }

    public void nextComp(){
        edReading.setText(" ");
        readinglistPojos = sqLiteHelper.NextCompReadingValues(RouteId,SubID,InvId,invOrder);
        maxOrder=sqLiteHelper.getLastInvOrder(RouteId,SubID);
        if (invOrder==maxOrder){

            builder = new AlertDialog.Builder(ComponentReading.this);
            //Setting message manually and performing action on button click
            builder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (grid){
                                startActivity(new Intent(ComponentReading.this, ComponentsTable.class).putExtra("SubId",SubID).putExtra("RouteID",RouteId));
                                finish();
                            }
                            else {
                                startActivity(new Intent(ComponentReading.this, ComponentDashboard.class).putExtra("SubId",SubID).putExtra("RouteID",RouteId));
                                finish();
                            }
                            dialog.cancel();
                        }
                    });

            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("This is the last component !!");
            alert.show();
            btnNext.setBackgroundResource(R.drawable.button_layout_onclick);
        }


        for (int i = 0; i < readinglistPojos.size(); i++) {

//            Toast.makeText(getApplicationContext(),"Value of CompName: "+ componentsListPojo.getInspected(),Toast.LENGTH_SHORT).show();
//                    tvComponent.setText();
            ComponentsListPojo componentsListPojo = readinglistPojos.get(i);
            String reading=String.valueOf(componentsListPojo.getReading());
            tvComponent.setText(String.valueOf(componentsListPojo.getCompName()));
            /*tvSize.setText(String.valueOf(componentsListPojo.getInvSize()));*/
            tvSize.setText(String.valueOf(componentsListPojo.getReading()));
            tvLocation.setText(String.valueOf(componentsListPojo.getInvLocation()));
            tvAreaName.setText(componentsListPojo.getAreaName());
            tvBackground.setText(String.format("%.2f",componentsListPojo.getBackread()+0.00));
            tvSubArea.setText(componentsListPojo.getSubName());
            inspected = componentsListPojo.getInspected();
            int comp=Float.compare(0.0f,componentsListPojo.getReading());
            if(comp > 0) {
                txtreading.setText(componentsListPojo.getReading()+" PPM");
            } else if(comp < 0) {
                txtreading.setText(componentsListPojo.getReading()+" PPM");
            } else {
                txtreading.setText("");
            }
            if (inspected.equals("true")||inspected.equals("1")){

              InspTick.setVisibility(View.VISIBLE);
            }
            else{
                InspTick.setVisibility(View.GONE);
            }
            if (!(componentsListPojo.getSkippedID() ==0)){
                int skipppedId=componentsListPojo.getSkippedID();
                String Reason=sqLiteHelper.getReason(skipppedId);
                int pos=adapter.getPosition(Reason);
                reasonSpinner.setSelection(pos);
            }
            else{
                reasonSpinner.setSelection(0);
            }

            boolean Critical = componentsListPojo.getCritical();
            if (!Critical){
                tvCritical.setTextColor(Color.parseColor("#ff669900"));
                tvCritical.setText("NO");
            }
            else{
                tvCritical.setTextColor(Color.parseColor("#ff0000"));
                tvCritical.setText("YES");
            }
            tvTAG.setText(componentsListPojo.getInvTag());
            InvId=componentsListPojo.getInvID();
            invOrder = sqLiteHelper.getInvOrder(RouteId,SubID,componentsListPojo.getInvID());
//            Toast.makeText(ComponentReading.this, "InvOrder :: "+invOrder, Toast.LENGTH_SHORT).show();
            break;  //to get out of the FOR loop
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        i=0;
        Bluetooth.CompBackReading=false;
        CompBackReadingHandler.removeCallbacksAndMessages(null);
    }
     static class ProgressRunnable implements Runnable {

         @Override
         public void run() {
             layout.setVisibility(View.GONE);
             scrollView.setVisibility(View.VISIBLE);
             chronometer.setBase(SystemClock.elapsedRealtime());
         }
     }


}