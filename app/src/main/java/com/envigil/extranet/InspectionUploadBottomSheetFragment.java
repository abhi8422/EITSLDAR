package com.envigil.extranet;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.WebService.WebService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.getSystemServiceName;
import static com.envigil.extranet.HomeActivity.fraglayout;
import static com.envigil.extranet.PrevInspection.rvPrevInsp;
import static com.envigil.extranet.PrevInspection.upload_progress_bar;
import static com.envigil.extranet.PrevInspection.upload_progress_txt;

/**
 * A simple {@link Fragment} subclass.
 */
public class InspectionUploadBottomSheetFragment extends BottomSheetDialogFragment {
    AlertDialog.Builder builder;
    TextView upload;
    ProgressDialog progressDialog;
    int InspID;
    JSONArray resultSet;
    JSONObject jsonObject=new JSONObject();
    String Result;
    SQLiteHelper sqLiteHelper;
    Logger logger;
    FileHandler fh;
    Context context;
    private long mLastClickTime = 0;

    public InspectionUploadBottomSheetFragment(Context context) {
        // Required empty public constructor
        this.context=context;
        logger = Logger.getLogger("MyLog");
        File logfile;
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        SimpleDateFormat month_date = new SimpleDateFormat("dd-MMM hh:mm:ss");
        String month_name = month_date.format(calendar.getTime());
        try {
            // This block configure the logger with handler and formatter
            File folder = new File(Environment.getExternalStorageDirectory()+File.separator + "ExtranetDAEP_Log");
            if(!folder.exists()){
                folder.mkdirs();
            }
            logfile = new File(folder, "UploadLog"+month_name+".txt");
            fh = new FileHandler(logfile.getPath());
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info("Logging Started");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(( getActivity().checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ) &&
                (getActivity().checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED )){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
        }
        //Logging

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_inspection_upload_bottom_sheet, container, false);
        sqLiteHelper=new SQLiteHelper(getContext());
        // Inflate the layout for this fragment
        Bundle bundle=getArguments();
        InspID=bundle.getInt("InspID",0);
        int RouteId=PrevInspection.RouteID;
        System.out.println(RouteId);
        upload = view.findViewById(R.id.tv_upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvailable() == true){
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    //Check If all sub areas have Background.
                    int subBackEnteredcheck=sqLiteHelper.checkSubBackEntered(PrevInspection.RouteID);
                    int subAreacnt=sqLiteHelper.getSubareasCnt(PrevInspection.RouteID);
                    System.out.println("SubBackEntered :"+subAreacnt);
                    if (subAreacnt==subBackEnteredcheck){
                        int AllCompInspected=sqLiteHelper.checkAllComponentsInspected(PrevInspection.RouteID);
                        int AllCompCnt=sqLiteHelper.getAllCompCnt(PrevInspection.RouteID);
                        System.out.println("AllCompInspected :"+AllCompCnt);
                        if (AllCompCnt==AllCompInspected){
                            try {
                                getInventoryJArray();
                                getLeaksJArray();
                                getLeakRepairJArray();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                StringWriter errors = new StringWriter();
                                e.printStackTrace(new PrintWriter(errors));
                                logger.info("Upload onClick():: "+errors.toString());
                            }
                            Result=jsonObject.toString();
                            System.out.println(Result);
                            logger.info("Upload Result "+Result);
                            progressDialog=new ProgressDialog(v.getContext(),ProgressDialog.STYLE_SPINNER);
                            progressDialog.setTitle("Uploading Route...");
                            progressDialog.setCancelable(false);
                            progressDialog.setContentView(R.layout.progress_dialog_upload_component);
                            progressDialog.show();/*
                            rvPrevInsp.setVisibility(View.GONE);
                            upload_progress_txt.setVisibility(View.VISIBLE);
                            upload_progress_bar.setVisibility(View.VISIBLE);*/
                            new Async(Result).execute();
                        }
                    }
                }
                else {
                builder = new AlertDialog.Builder(context);
                builder.setTitle("Not connected to internet!!");
                builder.setMessage("Check your internet connection and try again.");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isInternetAvailable() == true){
                            startActivity(new Intent(context, Inspections.class));
                        }
                        else {
                            startActivity(new Intent(context, PrevInspection.class));
                        }
                        dialogInterface.cancel();
                    }
                });
                builder.create();
                builder.show();
            }
            }
        });
        return view;
    }

    @SuppressLint("MissingPermission")
    private boolean isInternetAvailable(){
        getContext();
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,"Permission Accepted",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context,"User Refused To Accept The Permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getLeaksJArray() throws JSONException {

        String myPath = "/data/data/com.envigil.extranet/databases/Montrose.sqliteDatabase";// Set path to your database
//        File myPath = getContext().getDatabasePath("Montrose.sqliteDatabase.db");// Set path to your database

//        String myTable = "RoutesConfig";//Set name of your table

//or you can use `context.getDatabasePath("my_db_test.db")`

        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        String searchQuery = "SELECT LeakID, Leaks.InvID," + InspID + " As InspID, Leaks.InvTag, LeakPathTypeID, LeakTypeID, Leaks.CompID, LeakBit1, LeakBit2, LeakBit4, LeakBit5, LeakFloat1, LeakRate, LeakTime, LeakDate, Status, LeakLat, LeakLng FROM Leaks Inner Join Inventory On Inventory.InvID = Leaks.InvID Where Inventory.RouteID =" + PrevInspection.RouteID;
        Cursor cursor = myDataBase.rawQuery(searchQuery, null);

        resultSet = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME", cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }

            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TAG_NAME", String.valueOf(resultSet));
        jsonObject.put("Leaks",resultSet);
        return jsonObject.toString();
    }

    private String getInventoryJArray() throws JSONException {

        String myPath = "/data/data/com.envigil.extranet/databases/Montrose.sqliteDatabase";// Set path to your database
//        File myPath = getContext().getDatabasePath("Montrose.sqliteDatabase.db");// Set path to your database

        String myTable = "InspectionResults :::";//Set name of your table
int count=0;
//or you can use `context.getDatabasePath("my_db_test.db")`

        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        String searchQuery = "SELECT InvID, RouteID,"+InspID+" As InspID, CompTypeID, Timestamp, Reading, Background, Inspected, ReasonSkippedID FROM Inventory Where RouteID = "+PrevInspection.RouteID;
        Cursor cursor = myDataBase.rawQuery(searchQuery, null );

        resultSet     = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );

                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", e.getMessage()  );
                    }
                }

            }
            resultSet.put(rowObject);
            cursor.moveToNext();
            count++;
        }
        cursor.close();
        jsonObject.put("InspectionResults",resultSet);
        System.out.println("Count :"+count);
//        Log.d("TAG_NAME", resultSet.toString() );
        return jsonObject.toString();
    }

    private String getLeakRepairJArray() throws JSONException {

        String myPath = "/data/data/com.envigil.extranet/databases/Montrose.sqliteDatabase";// Set path to your database
//        File myPath = getContext().getDatabasePath("Montrose.sqliteDatabase.db");// Set path to your database

//        String myTable = "RoutesConfig";//Set name of your table

//or you can use `context.getDatabasePath("my_db_test.db")`

        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        String searchQuery = "SELECT Leaks.LeakID, LeakRepairTypeID, EmpID, LeakRepairRate As LeakRepairRate, LeakRepairDate As LeakRepairDate FROM LeakRepairs Inner Join Leaks On Leaks.LeakID = LeakRepairs.LeakID Inner Join Inventory On Inventory.InvID = Leaks.InvID Where Inventory.RouteID = "+PrevInspection.RouteID;
        Cursor cursor = myDataBase.rawQuery(searchQuery, null );

        resultSet     = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", e.getMessage()  );
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        jsonObject.put("LeakRepairs",resultSet);
//        Log.d("TAG_NAME", resultSet3.toString() );
        return jsonObject.toString();
    }

    public class Async extends AsyncTask{
        String UpResult,Response;
        public Async(String result){
            UpResult=result;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            WebService webService=new WebService();
            Response=webService.uploadComponents(UpResult);
            logger.info("Async uploadComponentsJSON Response:: "+Response);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            int Status;
            super.onPostExecute(o);

            if (Response.equals("success")){
                new Async2().execute();
                logger.info("Async Deleting Routes From Local DB Where RouteID:"+PrevInspection.RouteID);
                sqLiteHelper.deleteLeakRepair(PrevInspection.RouteID);
                sqLiteHelper.deleteLeaks(PrevInspection.RouteID);
                sqLiteHelper.deleteInventory(PrevInspection.RouteID);
                sqLiteHelper.deleteSubAreas(PrevInspection.RouteID);
                sqLiteHelper.deleteRoutesConfig(PrevInspection.RouteID);

                //sqLiteHelper.deleteInventoryRule(PrevInspection.RouteID);
              //sqLiteHelper.deleteRuleComponents(PrevInspection.RouteID);
                progressDialog.dismiss();
              /*  rvPrevInsp.setVisibility(View.VISIBLE);
                upload_progress_txt.setVisibility(View.GONE);
                upload_progress_bar.setVisibility(View.GONE);*/
                builder = new AlertDialog.Builder(getContext());
                //Setting message manually and performing action on button click
                builder.setMessage("Components were uploaded Successfully ")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().getSupportFragmentManager().beginTransaction().remove(InspectionUploadBottomSheetFragment.this).commit();
                                getActivity().finish();
                                /*TestRecycler testRecycler=new TestRecycler(sqLiteHelper.getAll());
                                DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                                testRecycler.notifyDataSetChanged();*/
                                fraglayout.setCurrentItem(0);
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Upload Successful ");
                alert.show();
            }
            else{
                progressDialog.dismiss();
             /*   rvPrevInsp.setVisibility(View.VISIBLE);
                upload_progress_txt.setVisibility(View.GONE);
                upload_progress_bar.setVisibility(View.GONE);*/
                builder = new AlertDialog.Builder(getContext());
                //Setting message manually and performing action on button click
                builder.setMessage("Upload failed please try again later ")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Upload failed !!");
                alert.show();
            }
        }
    }
    public class Async2 extends AsyncTask{
        String Status;

        @Override
        protected Object doInBackground(Object[] objects) {
            logger.info("Async2 setRoutesStatusAndroid to 'U' ");
            WebService webService=new WebService();
            Status=webService.setRoutesStatusAndroid(PrevInspection.RouteID,PrevInspection.WorkID,"U");
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Handler handler:logger.getHandlers()){
            handler.close();
        }
    }
}
