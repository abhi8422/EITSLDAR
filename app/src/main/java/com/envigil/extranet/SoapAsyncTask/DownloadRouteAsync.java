package com.envigil.extranet.SoapAsyncTask;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import com.envigil.extranet.DownloadedWorkOrderFrag;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.TestRecycler;
import com.envigil.extranet.WebService.WebService;
import com.envigil.extranet.models.ComponentTypes;
import com.envigil.extranet.models.Components;
import com.envigil.extranet.models.Employees;
import com.envigil.extranet.models.Inventory;
import com.envigil.extranet.models.InventoryRules;
import com.envigil.extranet.models.LeakPathTypes;
import com.envigil.extranet.models.LeakPaths;
import com.envigil.extranet.models.LeakRates;
import com.envigil.extranet.models.LeakRepairTypes;
import com.envigil.extranet.models.PermitInventory;
import com.envigil.extranet.models.PermitRates;
import com.envigil.extranet.models.Permits;
import com.envigil.extranet.models.ReasonSkipped;
import com.envigil.extranet.models.RoutesConfig;
import com.envigil.extranet.models.RuleComponentTypes;
import com.envigil.extranet.models.RuleComponents;
import com.envigil.extranet.models.StreamTypes;
import com.envigil.extranet.models.Streams;
import com.envigil.extranet.models.Subarea;
import com.envigil.extranet.models.TVA;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static com.envigil.extranet.AvailableWorkOrderFrag.availableRecyclerView;
import static com.envigil.extranet.AvailableWorkOrderFrag.downaload_txt_view;
import static com.envigil.extranet.AvailableWorkOrderFrag.progressBar;

public class DownloadRouteAsync extends AsyncTask {
    String responce;
    Context context;
    String name;
    int routeID;
    String result;
    JSONObject jsonObject;
    SQLiteHelper sqLiteHelper;
    int workorderID;

    public DownloadRouteAsync(String name, Context context, int routeID, int workorderID) {
        this.context = context;
        this.name = name;
        this.routeID = routeID;
        this.workorderID = workorderID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
        downaload_txt_view.setVisibility(View.VISIBLE);
        availableRecyclerView.setVisibility(View.GONE);
        System.out.println("Lock The Route");
        new AsynLock().execute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }

    private void ParseStreams(JSONArray streamsArray) {
        if (sqLiteHelper.StreamsCnt() <= 0) {
            for (int i = 0; i < streamsArray.length(); i++) {
                JSONObject object = null;
                try {
                    object = streamsArray.getJSONObject(i);
                    int StrTypeID = object.getInt("StrTypeID");
                    int StrID = object.getInt("StrID");
                    String StrName = object.getString("StrName");
                    String StrAbbr = object.getString("StrAbbr");
//                    Log.d(TAG,"streamsArray :::"+streamsArray);
                    Streams streams = new Streams(StrTypeID, StrID, StrName, StrAbbr);
                    sqLiteHelper.Insertstreams(streams);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void ParseRouteConfig(JSONArray routesConfigArray) {

        for (int i = 0; i < routesConfigArray.length(); i++) {
            JSONObject object = null;
            try {
                object = routesConfigArray.getJSONObject(i);
                int RouteID = object.getInt("RouteID");
                int FacID = object.getInt("FacID");
                String Facility = object.getString("Facility");
                int RuleID = object.getInt("RuleID");
                String RuleNumber = object.getString("RuleNumber");
                String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
                String RouteName = object.getString("RouteName");
                String RouteDesc = object.getString("RouteDesc");
                boolean DAEP = object.getBoolean("DAEP");
                boolean OverwriteStaticTables = object.getBoolean("OverwriteStaticTables");
                String RouteStatus = object.getString("RouteStatus");
                boolean Inspected = object.getBoolean("Inspected");
                int RuleTypeID = object.getInt("RuleTypeID");
//                Log.d(TAG, "Routes Config :::" + routesConfigArray);
                RoutesConfig routesConfig = new RoutesConfig(RouteID, workorderID, FacID, RuleTypeID, RuleID, Facility, RuleNumber, date, RouteName, RouteDesc, RouteStatus, DAEP, OverwriteStaticTables, Inspected, false, 0.0f, null, 0, 0, false);
                sqLiteHelper.InsertRoutesConfig(routesConfig);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void ParseSubAreas(JSONArray JsonArray) {
        for (int i = 0; i < JsonArray.length(); i++) {
            JSONObject object = null;
            try {
                object = JsonArray.getJSONObject(i);
                int AreaID = object.getInt("AreaID");
                int RouteID = object.getInt("RouteID");
                int SubID = object.getInt("SubID");
                int SubOrder = object.getInt("SubOrder");
                String SubName = object.getString("SubName");
                String AreaName = object.getString("AreaName");
//                Log.d(TAG, "Routes Config :::" + JsonArray);
                Subarea subarea = new Subarea(AreaID, RouteID, SubID, SubName, false, SubOrder, null, false, 0.0f);
                subarea.setAreaName(AreaName);
                sqLiteHelper.InsertSubAreas(subarea);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void ParseInventory(JSONArray inventoryArray) {
        for (int i = 0; i < inventoryArray.length(); i++) {
            JSONObject object = null;
            try {
                object = inventoryArray.getJSONObject(i);
                int RouteID = object.getInt("RouteID");
                int SubID = object.getInt("SubID");
                int InvID = object.getInt("InvID");
                int CompID = object.getInt("CompID");
                String InvTag = object.getString("InvTag");
                int InvOrder = object.getInt("InvOrder");
                int CompTypeID = object.getInt("CompTypeID");
                int StrTypeID = object.getInt("StrTypeID");
                int StrID = object.getInt("StrID");
                float InvSize = (float) object.getInt("InvSize");
                float Total = (float) object.getInt("Total");
                String ScanSeconds = String.valueOf(object.getInt("ScanSeconds"));
                String InvLocation = object.getString("InvLocation");
                boolean Critical = Boolean.parseBoolean(String.valueOf(object.getInt("Critical")));
                Inventory inventory = new Inventory(RouteID, SubID, InvID, CompID, CompTypeID, StrTypeID, StrID, InvTag, InvLocation, InvOrder, InvSize, Total, ScanSeconds, Critical);
//                Log.d(TAG, "Routes Config :::" + inventoryArray);
                sqLiteHelper.InsertInventory(inventory);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void ParseInventoryRules(JSONArray inventoryRulesArray) {
        for (int i = 0; i < inventoryRulesArray.length(); i++) {
            JSONObject object = null;
            try {
                object = inventoryRulesArray.getJSONObject(i);
                int RouteID = object.getInt("RouteID");
                int InvID = object.getInt("InvID");
                int RuleID = object.getInt("RuleID");
                int comp_mon_cat_id = object.getInt("comp_mon_cat_id");
      System.out.println("inventoryRulesArray :::" + inventoryRulesArray);
                InventoryRules inventoryRules = new InventoryRules(RouteID, InvID, RuleID, comp_mon_cat_id);
                sqLiteHelper.InsertInventoryRules(inventoryRules);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void ParseComponentTypes(JSONArray componentTypesArray) {
        if (sqLiteHelper.getComponentTypesCnt() <= 0) {
            for (int i = 0; i < componentTypesArray.length(); i++) {
                JSONObject object = null;
                try {

                    object = componentTypesArray.getJSONObject(i);
                    int CompTypeID = object.getInt("CompTypeID");
                    String CompTypeName = object.getString("CompTypeName");
                    String CompAbbr = object.getString("CompAbbr");
//                    Log.d(TAG,"componentTypesArray :::"+componentTypesArray);
                    ComponentTypes componentTypes = new ComponentTypes(CompTypeID, CompTypeName, CompAbbr);
                    sqLiteHelper.InsertcomponentTypes(componentTypes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void ParseStreamTypes(JSONArray streamTypesArray) {
        if (sqLiteHelper.getStreamTypesCnt() <= 0) {
            for (int i = 0; i < streamTypesArray.length(); i++) {
                JSONObject object = null;
                try {
                    object = streamTypesArray.getJSONObject(i);
                    int StrTypeID = object.getInt("StrTypeID");
                    String StrTypeName = object.getString("StrTypeName");
                    String StrTypeAbbr = object.getString("StrTypeAbbr");
//                    Log.d(TAG,"streamTypesArray :::"+streamTypesArray);
                    StreamTypes streamTypes = new StreamTypes(StrTypeID, StrTypeName, StrTypeAbbr);
                    sqLiteHelper.InsertStreamTypes(streamTypes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void ParseRuleComponentTypes(JSONArray ruleComponentTypesArray) {
        RuleComponentTypes ruleComponentType = new RuleComponentTypes();
        int RuleId = ruleComponentType.getRuleID();
        if (sqLiteHelper.getRuleComponentTypesCnt(RuleId) <= 0) {
            for (int i = 0; i < ruleComponentTypesArray.length(); i++) {
                JSONObject object = null;
                try {
                    object = ruleComponentTypesArray.getJSONObject(i);
                    int RuleID = object.getInt("RuleID");
                    int CompTypeID = object.getInt("CompTypeID");
                    int RuleCompTypeID = object.getInt("RuleCompTypeID");
//                    Log.d(TAG,"ruleComponentTypesArray :::"+ruleComponentTypesArray);
                    RuleComponentTypes ruleComponentTypes = new RuleComponentTypes(RuleID, CompTypeID, RuleCompTypeID);
                    sqLiteHelper.InsertRuleComponentTypes(ruleComponentTypes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void ParseComponents(JSONArray componentsArray) {
        if (sqLiteHelper.getComponentsCnt() <= 0) {
            for (int i = 0; i < componentsArray.length(); i++) {
                JSONObject object = null;
                try {
                    object = componentsArray.getJSONObject(i);
                    int CompID = object.getInt("CompID");
                    int CompOrder = object.getInt("CompOrder");
                    String CompName = object.getString("CompName");
                    String CompAbbr = object.getString("CompAbbr");
//                    Log.d(TAG,"componentsArray :::"+componentsArray);
                    Components components = new Components(CompID, CompName, CompAbbr, CompOrder);
                    sqLiteHelper.Insertcomponents(components);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void ParseRuleComponents(JSONArray ruleComponentsArray) {
        RuleComponentTypes ruleComponentType = new RuleComponentTypes();
        int RuleId = ruleComponentType.getRuleID();
        System.out.println("RuleID"+RuleId);
        System.out.println("getRuleComponentsCnt:"+sqLiteHelper.getRuleComponentsCnt(RuleId));
        if (sqLiteHelper.getRuleComponentsCnt(RuleId) <= 0) {
            for (int i = 0; i < ruleComponentsArray.length(); i++) {
                JSONObject object = null;
                try {

                    object = ruleComponentsArray.getJSONObject(i);
                    int RuleID = object.getInt("RuleID");
                    int CompTypeID = object.getInt("CompTypeID");
                    int CompID = object.getInt("CompID");
                    int RuleCompID = object.getInt("RuleCompID");
//                    Log.d(TAG,"ruleComponentsArray :::"+ruleComponentsArray);
                    RuleComponents ruleComponents = new RuleComponents(RuleID, CompTypeID, CompID, RuleCompID);
                    sqLiteHelper.InsertRuleComponents(ruleComponents);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void ParseTVA(JSONArray tvaArray) {
        if (sqLiteHelper.getTVACount() <= 0) {
            for (int i = 0; i < tvaArray.length(); i++) {
                JSONObject object = null;
                try {
                    object = tvaArray.getJSONObject(i);
                    int TVAID = object.getInt("TVAID");
                    int TVAEmpID = object.getInt("TVAEmpID");
                    String TVAName = object.getString("TVAName");
//                    Log.i(TAG,"tvaArray :::"+tvaArray);
                    TVA tva = new TVA(TVAID, TVAEmpID, TVAName);
                    sqLiteHelper.InsertTVA(tva);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void ParseEmployees(JSONArray employeesArray) {
        if (sqLiteHelper.getEmployeeCnt() <= 0) {
            for (int i = 0; i < employeesArray.length(); i++) {
                JSONObject object = null;
                try {
                    object = employeesArray.getJSONObject(i);
                    int CompID = object.getInt("CompID");
                    int EmpID = object.getInt("EmpID");
                    String EmpFirst = object.getString("EmpFirst");
                    String EmpLast = object.getString("EmpLast");
//                    Log.d(TAG,"employeesArray :::"+employeesArray);
                    Employees employees = new Employees(EmpID, CompID, EmpFirst, EmpLast);
                    sqLiteHelper.Insertemployees(employees);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void ParseLeakRates(JSONArray leakRatesArray) {
        if (sqLiteHelper.getLeakRatesCnt() <= 0) {
            for (int i = 0; i < leakRatesArray.length(); i++) {
                JSONObject object = null;
                try {

                    object = leakRatesArray.getJSONObject(i);

                    int LeakTypeID = object.getInt("LeakTypeID");
                    int RuleCompTypeID = object.getInt("RuleCompTypeID");
                    int StrTypeID = object.getInt("StrTypeID");
                    int LeakRateID = object.getInt("LeakRateID");
                    float LeakRateStart = (float) object.getInt("LeakRateStart");
                    float LeakRateEnd = (float) object.getInt("LeakRateEnd");
                    int LeakRateTime = object.getInt("LeakRateTime");
                    int StrID = object.getInt("StrID");
//                    Log.d(TAG,"leakRatesArray :::"+leakRatesArray);
                    LeakRates leakRates = new LeakRates(LeakTypeID, RuleCompTypeID, StrTypeID, LeakRateID, StrID, LeakRateStart, LeakRateEnd, LeakRateTime);
                    sqLiteHelper.InsertLeakRates(leakRates);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void ParseLeakPaths(JSONArray leakPathsArray) {
        for (int i = 0; i < leakPathsArray.length(); i++) {
            JSONObject object = null;
            try {
                object = leakPathsArray.getJSONObject(i);
                int CompID = object.getInt("CompID");
                int LeakPathTypeID = object.getInt("LeakPathTypeID");
//                Log.d(TAG,"leakPathsArray :::"+leakPathsArray);
                LeakPaths leakPaths = new LeakPaths(CompID, LeakPathTypeID);
                sqLiteHelper.InsertLeakPaths(leakPaths);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void ParseLeakRepairTypesArray(JSONArray leakRepairTypesArray) {
        if (sqLiteHelper.getLeakRepairTypesCnt() <= 0) {
            for (int i = 0; i < leakRepairTypesArray.length(); i++) {
                JSONObject object = null;
                try {
                    object = leakRepairTypesArray.getJSONObject(i);
                    int LeakRepairTypeID = object.getInt("LeakRepairTypeID");
                    String LeakRepairTypeName = object.getString("LeakRepairTypeName");
                    String LeakRepairTypeAbbr = object.getString("LeakRepairTypeAbbr");
//                    Log.d(TAG,"leakRepairTypesArray :::"+leakRepairTypesArray);
                    LeakRepairTypes leakRepairTypes = new LeakRepairTypes(LeakRepairTypeID, LeakRepairTypeName, LeakRepairTypeAbbr);
                    sqLiteHelper.InsertLeakRepairTypes(leakRepairTypes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void ParseReasonSkipped(JSONArray reasonSkippedArray) {
        if (sqLiteHelper.getReasonSkippedCnt() <= 0) {
            for (int i = 0; i < reasonSkippedArray.length(); i++) {
                JSONObject object = null;
                try {
                    object = reasonSkippedArray.getJSONObject(i);
                    int ReasonSkippedID = object.getInt("ReasonSkippedID");
                    String ReasonSkipped = object.getString("ReasonSkipped");
//                    Log.d(TAG,"Routes Config :::"+reasonSkippedArray);
                    com.envigil.extranet.models.ReasonSkipped reasonSkipped = new ReasonSkipped(ReasonSkippedID, ReasonSkipped);
                    sqLiteHelper.InsertReasonSkipped(reasonSkipped);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void ParseLeakPathTypes(JSONArray leakPathTypeArray) {
        if (sqLiteHelper.getLeakPathTypeCnt() <= 0) {
            for (int i = 0; i < leakPathTypeArray.length(); i++) {
                JSONObject object = null;
                try {
                    object = leakPathTypeArray.getJSONObject(i);
                    int LeakPathTypeID = object.getInt("LeakPathTypeID");
                    String LeakPathTypeName = object.getString("LeakPathTypeName");
                    String LeakPathTypeAbbr = object.getString("LeakPathTypeAbbr");
                    int LeakPathTypeOrder = object.getInt("LeakPathTypeOrder");
                    LeakPathTypes leakPathTypes = new LeakPathTypes(LeakPathTypeID, LeakPathTypeName, LeakPathTypeAbbr, LeakPathTypeOrder);
                    sqLiteHelper.InsertLeakPathTypes(leakPathTypes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void ParsePermits(JSONArray permitArray) {
        Permits permit = new Permits();
        int FacId = permit.getFacID();
        if (sqLiteHelper.getPermitsCnt(FacId) <= 0) {
            for (int i = 0; i < permitArray.length(); i++) {
                JSONObject object = null;
                try {
                    object = permitArray.getJSONObject(i);
                    int FacID = object.getInt("FacID");
                    int PermID = object.getInt("PermID");
                    Permits permits = new Permits(FacID, PermID);
                    sqLiteHelper.InsertPermits(permits);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void ParsePermitInventory(JSONArray permitInventoryArray) {
        for (int i = 0; i < permitInventoryArray.length(); i++) {
            JSONObject object = null;
            try {
                object = permitInventoryArray.getJSONObject(i);
                int PermID = object.getInt("PermID");
                int InvID = object.getInt("InvID");
                int RouteID = object.getInt("RouteID");
                PermitInventory permitInventory = new PermitInventory(InvID, PermID, RouteID);
                sqLiteHelper.InsertPermitInventory(permitInventory);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void ParsePermitRates(JSONArray permitRatesArray) {

        for (int i = 0; i < permitRatesArray.length(); i++) {
            JSONObject object = null;
            try {
                object = permitRatesArray.getJSONObject(i);
                int PermId = object.getInt("PermID");
                int LeakTypeID = object.getInt("LeakTypeID");
                int PermRateId = object.getInt("PermRateID");
                float PermRateStart = (float) object.getInt("PermRateStart");
                float PermRateTime = (float) object.getInt("PermRateTime");
                PermitRates permitRates = new PermitRates(PermId, LeakTypeID, PermRateId, PermRateStart, PermRateTime);
                sqLiteHelper.InsertPermitRates(permitRates);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class AsynLock extends AsyncTask {

        boolean LResult,exception;

        @Override
        protected Object doInBackground(Object[] objects) {
            String Response;
            WebService webService = new WebService();
            Response = webService.setRoutesStatusAndroid(routeID, workorderID, "L");
            if (Response.equals("setRoutesStatusAndroidResponse{setRoutesStatusAndroidResult=true; }")) {
                sqLiteHelper = new SQLiteHelper(context);
                webService = new WebService(context);
                result = webService.getRoutesData(routeID, workorderID);
                System.out.println(result);
                LResult=true;
            }
            else{
               LResult=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
          /*  if (!LResult){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("There is problem with route downloading");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TestRecycler testRecycler = new TestRecycler(new SQLiteHelper(context).getAll());
                        DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                        testRecycler.notifyDataSetChanged();
                        DownloadedWorkOrderFrag.downloadedrecycler.smoothScrollToPosition(new SQLiteHelper(context).getAll().size());
                        dialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        downaload_txt_view.setVisibility(View.GONE);
                        availableRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
                AlertDialog alertDialog1 = alertDialogBuilder.create();
                alertDialog1.show();
            }*/
            if (result.trim().equals("")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("There is problem with route downloading");
                alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TestRecycler testRecycler = new TestRecycler(new SQLiteHelper(context).getAll());
                        DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                        testRecycler.notifyDataSetChanged();
                        DownloadedWorkOrderFrag.downloadedrecycler.smoothScrollToPosition(new SQLiteHelper(context).getAll().size());
                        dialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        downaload_txt_view.setVisibility(View.GONE);
                        availableRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
                AlertDialog alertDialog1 = alertDialogBuilder.create();
                alertDialog1.show();
               // new AsynD().execute();
            } else {
                try {
                    //    System.out.println("Api Result "+result);
                    jsonObject = new JSONObject(result);
                    JSONArray RoutesConfigArray = null;
                    RoutesConfigArray = jsonObject.getJSONArray("RoutesConfig");
                    System.out.println("RoutesConfigArray: " + RoutesConfigArray);
                    ParseRouteConfig(RoutesConfigArray);
                    JSONArray SubAreasArray = null;
                    SubAreasArray = jsonObject.getJSONArray("SubAreas");
                    System.out.println("SubAreasArray: " + SubAreasArray);
                    ParseSubAreas(SubAreasArray);
                    JSONArray InventoryArray = null;
                    InventoryArray = jsonObject.getJSONArray("Inventory");
                    ParseInventory(InventoryArray);
                    JSONArray InventoryRulesArray = null;
                    InventoryRulesArray = jsonObject.getJSONArray("InventoryRules");
                    ParseInventoryRules(InventoryRulesArray);
                    JSONArray LeakRepairTypesArray = null;
                    LeakRepairTypesArray = jsonObject.getJSONArray("LeakRepairTypes");
                    ParseLeakRepairTypesArray(LeakRepairTypesArray);
                    JSONArray ReasonSkippedArray = null;
                    ReasonSkippedArray = jsonObject.getJSONArray("ReasonSkipped");
                    ParseReasonSkipped(ReasonSkippedArray);
                    //change
                    JSONArray LeakPathTypeArray = null;
                    LeakPathTypeArray = jsonObject.getJSONArray("LeakPathTypes");
                    ParseLeakPathTypes(LeakPathTypeArray);
                    JSONArray PermitArray = null;
                    PermitArray = jsonObject.getJSONArray("Permits");
                    ParsePermits(PermitArray);
                    JSONArray PermitInventoryArray = null;
                    PermitInventoryArray = jsonObject.getJSONArray("PermitInventory");
                    ParsePermitInventory(PermitInventoryArray);
                    JSONArray PermitRatesArray = null;
                    PermitRatesArray = jsonObject.getJSONArray("PermitRates");
                    ParsePermitRates(PermitRatesArray);
                    //end
                    JSONArray LeakPathsArray = null;
                    LeakPathsArray = jsonObject.getJSONArray("LeakPaths");
                    ParseLeakPaths(LeakPathsArray);
                    JSONArray LeakRatesArray = null;
                    LeakRatesArray = jsonObject.getJSONArray("LeakRates");
                    ParseLeakRates(LeakRatesArray);
                    JSONArray EmployeesArray = null;
                    EmployeesArray = jsonObject.getJSONArray("Employees");
                    ParseEmployees(EmployeesArray);
                    JSONArray TVAArray = null;
                    TVAArray = jsonObject.getJSONArray("TVA");
                    ParseTVA(TVAArray);
                    JSONArray RuleComponentsArray = null;
                    RuleComponentsArray = jsonObject.getJSONArray("RuleComponents");
                    ParseRuleComponents(RuleComponentsArray);
                    JSONArray ComponentsArray = null;
                    ComponentsArray = jsonObject.getJSONArray("Components");
                    ParseComponents(ComponentsArray);
                    JSONArray RuleComponentTypesArray = null;
                    RuleComponentTypesArray = jsonObject.getJSONArray("RuleComponentTypes");
                    ParseRuleComponentTypes(RuleComponentTypesArray);
                    JSONArray StreamTypesArray = null;
                    StreamTypesArray = jsonObject.getJSONArray("StreamTypes");
                    ParseStreamTypes(StreamTypesArray);
                    JSONArray ComponentTypesArray = null;
                    ComponentTypesArray = jsonObject.getJSONArray("ComponentTypes");
                    ParseComponentTypes(ComponentTypesArray);
                    JSONArray StreamsArray = null;
                    StreamsArray = jsonObject.getJSONArray("Streams");
                    ParseStreams(StreamsArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                    exception=true;
                    Toast.makeText(context, "Catch Exception", Toast.LENGTH_SHORT).show();
                }
                if(exception){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage("There is problem with route downloading");
                    alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*sqLiteHelper.deleteLeaks(routeID);
                            sqLiteHelper.deleteInventory(routeID);
                            sqLiteHelper.deleteSubAreas(routeID);
                            sqLiteHelper.deleteRoutesConfig(routeID);
                            sqLiteHelper.deleteInventoryRule(routeID);
                            sqLiteHelper.deleteLeakRepair(routeID);
                            sqLiteHelper.deleteRuleComponents(routeID);*/
                            TestRecycler testRecycler = new TestRecycler(new SQLiteHelper(context).getAll());
                            DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                            testRecycler.notifyDataSetChanged();
                            DownloadedWorkOrderFrag.downloadedrecycler.smoothScrollToPosition(new SQLiteHelper(context).getAll().size());
                            dialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                            downaload_txt_view.setVisibility(View.GONE);
                            availableRecyclerView.setVisibility(View.VISIBLE);

                        }
                    });
                    AlertDialog alertDialog1 = alertDialogBuilder.create();
                    alertDialog1.show();
                   // new AsynD().execute();
                }else {
                    new AsynInsert().execute();
                }

            }
        }

        public class AsynInsert extends AsyncTask {
                boolean IResult;
            @Override
            protected Object doInBackground(Object[] objects) {
                String ResponseInsert;
                WebService webService = new WebService();
                ResponseInsert = webService.setRoutesStatusAndroid(routeID, workorderID, "I");
                if (ResponseInsert.equals("setRoutesStatusAndroidResponse{setRoutesStatusAndroidResult=true; }")) {
                    IResult=true;
                }
                //Delete All route data.
                else{
                    IResult=false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (IResult){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage(name + " Route has been downloaded successfully");
                    alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TestRecycler testRecycler = new TestRecycler(new SQLiteHelper(context).getAll());
                            DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                            testRecycler.notifyDataSetChanged();
                            DownloadedWorkOrderFrag.downloadedrecycler.smoothScrollToPosition(new SQLiteHelper(context).getAll().size());
                            dialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                            downaload_txt_view.setVisibility(View.GONE);
                            availableRecyclerView.setVisibility(View.VISIBLE);
                        }
                    });
                    AlertDialog alertDialog1 = alertDialogBuilder.create();
                    alertDialog1.show();
                    alertDialog1.setCancelable(false);
                }
                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage("There is problem with route downloading");
                    alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sqLiteHelper.deleteLeakRepair(routeID);
                            sqLiteHelper.deleteLeaks(routeID);
                            sqLiteHelper.deleteInventory(routeID);
                            sqLiteHelper.deleteSubAreas(routeID);
                            sqLiteHelper.deleteRoutesConfig(routeID);
                            //sqLiteHelper.deleteInventoryRule(routeID);
                           // sqLiteHelper.deleteRuleComponents(routeID);
                            TestRecycler testRecycler = new TestRecycler(new SQLiteHelper(context).getAll());
                            DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                            testRecycler.notifyDataSetChanged();
                            DownloadedWorkOrderFrag.downloadedrecycler.smoothScrollToPosition(new SQLiteHelper(context).getAll().size());
                            dialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                            downaload_txt_view.setVisibility(View.GONE);
                            availableRecyclerView.setVisibility(View.VISIBLE);

                        }
                    });
                    AlertDialog alertDialog1 = alertDialogBuilder.create();
                    alertDialog1.show();
                    //new AsynD().execute();
                }
            }
        }

      /* public class AsynD extends AsyncTask {

            @Override
            protected Object doInBackground(Object[] objects) {
                WebService webService = new WebService();
                webService.setRoutesStatusAndroid(routeID, workorderID, "D");
                return null;
            }
        }*/
    }
}