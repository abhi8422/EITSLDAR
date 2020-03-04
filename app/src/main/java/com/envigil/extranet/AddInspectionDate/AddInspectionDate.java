package com.envigil.extranet.AddInspectionDate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.envigil.extranet.HomeActivity;
import com.envigil.extranet.R;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.DatePickerDialog.OnDateSetListener;
import static com.envigil.extranet.HomeActivity.fraglayout;

public class AddInspectionDate extends AppCompatActivity {
TextInputEditText inspection_date;
int mYear,mDay,mMonth;
int routeID;
String routeName;
TextView insc_date_route_name;
    final String[] dateString = new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_inspection_date);
        routeID=getIntent().getIntExtra("RouteID",0);
        routeName=getIntent().getStringExtra("RouteName");
        inspection_date=findViewById(R.id.inspection_date);
        insc_date_route_name=findViewById(R.id.insc_date_route_name);
        insc_date_route_name.setText("Set Inspection Date For Route:"+routeName);
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMonth = calendar.get(Calendar.MONTH);
        String strmMonth,strmDay;
        if(String.valueOf(mMonth).length()==1){
            strmMonth="0"+(mMonth+1);
        }else {
            strmMonth=String.valueOf(mMonth+1);
        }
        if(String.valueOf(mDay).length()==1){
            strmDay="0"+mDay;
        }else {
            strmDay= String.valueOf(mDay);
        }
        inspection_date.setText((strmMonth)+"/"+strmDay+"/"+mYear);
        dateString[0]=(strmMonth)+"/"+strmDay+"/"+mYear;
        inspection_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog;
                datePickerDialog=new DatePickerDialog(AddInspectionDate.this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mMonth,mYear,mDay;
                        if(String.valueOf(month).length()==1){
                            mMonth="0"+(month+1);
                        }else {
                            mMonth=String.valueOf(month+1);
                        }
                        if(String.valueOf(dayOfMonth).length()==1){
                            mDay="0"+dayOfMonth;
                        }else {
                            mDay= String.valueOf(dayOfMonth);
                        }
                        if(String.valueOf(year).length()==1){
                            mYear="0"+year;
                        }else {
                            mYear=String.valueOf(year);
                        }
                        dateString[0] =mMonth+"/"+mDay+"/"+mYear;
                    inspection_date.setText(mMonth+"/"+mDay+"/"+mYear);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        findViewById(R.id.btn_inspDate_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inspection_date.getText().toString().equals("")){
                    inspection_date.setError("Date cannot be empty!");
                }
                else{
                    @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        Date date = df.parse(dateString[0]);
                        SQLiteHelper sqLiteHelper=new SQLiteHelper(AddInspectionDate.this);
                        sqLiteHelper.UpdateInspectionDate(dateString[0],routeID);
                        System.out.println(df.format(date));
                        System.out.println(routeID);
                        AlertDialog.Builder builder=new AlertDialog.Builder(AddInspectionDate.this);
                        builder.setTitle("Inspection Date is set sucessfully");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(AddInspectionDate.this, HomeActivity.class));
                                fraglayout.setCurrentItem(0);
                                finish();
                            }
                        });
                        builder.show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
}
