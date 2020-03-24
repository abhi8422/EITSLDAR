package com.envigil.extranet.SQLiteOpenHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.envigil.extranet.SubAreasPojo;
import com.envigil.extranet.models.ComponentTypes;
import com.envigil.extranet.models.Components;
import com.envigil.extranet.models.ComponentsListPojo;
import com.envigil.extranet.models.Employees;
import com.envigil.extranet.models.EmployeesPojo;
import com.envigil.extranet.models.GetAllRooutesPojo;
import com.envigil.extranet.models.Inventory;
import com.envigil.extranet.models.InventoryRules;
import com.envigil.extranet.models.LeakPathTypes;
import com.envigil.extranet.models.LeakPaths;
import com.envigil.extranet.models.LeakRates;
import com.envigil.extranet.models.LeakRepairTypes;
import com.envigil.extranet.models.LeakRepairTypesPojo;
import com.envigil.extranet.models.LeakRepairs;
import com.envigil.extranet.models.Leaks;
import com.envigil.extranet.models.PermitInventory;
import com.envigil.extranet.models.PermitRates;
import com.envigil.extranet.models.Permits;
import com.envigil.extranet.models.ReasonSkipped;
import com.envigil.extranet.models.RoutesConfig;
import com.envigil.extranet.models.RuleComponentTypes;
import com.envigil.extranet.models.RuleComponents;
import com.envigil.extranet.models.ShowLeaksPojo;
import com.envigil.extranet.models.StreamTypes;
import com.envigil.extranet.models.Streams;
import com.envigil.extranet.models.Subarea;
import com.envigil.extranet.models.TVA;
import com.envigil.extranet.models.TVAPojo;


import java.io.Reader;
import java.security.cert.CertPathValidatorException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SQLiteHelper extends SQLiteOpenHelper {
    List<GetAllRooutesPojo> resultstring=new ArrayList<>();

    public static final String DATABASE_NAME = "Montrose.sqliteDatabase";

    public static final String TAB_RoutesConfig = "RoutesConfig";
    public static final String COL_RC_RouteID = "RouteID";
    public static final String COL_RC_WorkID = "WorkID";
    public static final String COL_RC_FacID = "FacID";
    public static final String COL_RC_Facility = "Facility";
    public static final String COL_RC_RuleID = "RuleID";
    public static final String COL_RC_RuleNumber = "RuleNumber";
    public static final String COL_RC_RouteDateStamp = "RouteDateStamp";
    public static final String COL_RC_RouteName = "RouteName";
    public static final String COL_RC_RouteDesc = "RouteDesc";
    public static final String COL_RC_RouteStatus = "RouteStatus";
    public static final String COL_RC_DAEP = "DAEP";
    public static final String COL_RC_OverwriteStaticTables = "OverwriteStaticTables";
    public static final String COL_RC_Inspected = "Inspected";
    public static final String COL_RC_BackgroundEntered = "BackgroundEntered";
    public static final String COL_RC_Background = "Background";
    public static final String COL_RC_RuleTypeID = "RuleTypeID";
    public static final String COL_RC_InspectionDate = "InspectionDate";

    public static final String TAB_SubAreas = "SubAreas";
    public static final String COL_SubArea_AreaID = "AreaID";
    public static final String COL_SubArea_RouteID = "RouteID";
    public static final String COL_SubArea_SubID = "SubID";
    public static final String COL_SubArea_SubName = "SubName";
    public static final String COL_SubArea_AreaName = "AreaName";
    public static final String COL_SubArea_Inspected = "Inspected";
    public static final String COL_SubArea_SubOrder = "SubOrder";
    public static final String COL_SubArea_TimeStamp = "TimeStamp";
    public static final String COL_SubArea_BackgroundEntered = "BackgroundEntered";
    public static final String COL_SubArea_Background = "Background";

    public static final String TAB_Inventory = "Inventory";
    public static final String COL_INV_RouteID = "RouteID";
    public static final String COL_INV_SubID = "SubID";
    public static final String COL_INV_InvID = "InvID";
    public static final String COL_INV_CompID = "CompID";
    public static final String COL_INV_InvTag = "InvTag";
    public static final String COL_INV_InvLocation = "InvLocation";
    public static final String COL_INV_Inspected = "Inspected";
    public static final String COL_INV_InvOrder = "InvOrder";
    public static final String COL_INV_CompTypeID = "CompTypeID";
    public static final String COL_INV_StrTypeID = "StrTypeID";
    public static final String COL_INV_StrID = "StrID";
    public static final String COL_INV_InvSize = "InvSize";
    public static final String COL_INV_Background = "Background";
    public static final String COL_INV_Reading = "Reading";
    public static final String COL_INV_TimeStamp = "TimeStamp";
    public static final String COL_INV_Skipped = "Skipped";
    public static final String COL_INV_ReasonSkippedID = "ReasonSkippedID";
    public static final String COL_INV_Total = "Total";
    public static final String COL_INV_Critical = "Critical";

    public static final String TAB_LeakPaths = "LeakPaths";
    public static final String COL_LEAKPATHS_CompID = "CompID";
    public static final String COL_LEAKPATHS_LeakPathTypeID = "LeakPathTypeID";

    public static final String TAB_LeakPathTypes = "LeakPathTypes";
    public static final String COL_LEAKTYPE_LeakPathTypeID = "LeakPathTypeID";
    public static final String COL_LEAKTYPE_LeakPathTypeName = "LeakPathTypeName";
    public static final String COL_LEAKTYPE_LeakPathTypeAbbr = "LeakPathTypeAbbr";
    public static final String COL_LEAKTYPE_LeakPathTypeOrder = "LeakPathTypeOrder";

    public static final String TAB_Leaks = "Leaks";
    public static final String COL_LEAK_LeakID = "LeakID";
    public static final String COL_LEAK_InvID = "InvID";
    public static final String COL_LEAK_InvTag = "InvTag";
    public static final String COL_LEAK_LeakPathTypeID = "LeakPathTypeID";
    public static final String COL_LEAK_LeakTypeID = "LeakTypeID";
    public static final String COL_LEAK_CompID = "CompID";
    public static final String COL_LEAK_LeakBit1 = "LeakBit1";
    public static final String COL_LEAK_LeakBit2 = "LeakBit2";
    public static final String COL_LEAK_LeakBit4 = "LeakBit4";
    public static final String COL_LEAK_LeakBit5 = "LeakBit5";
    public static final String COL_LEAK_LeakFloat1 = "LeakFloat1";
    public static final String COL_LEAK_LeakRate = "LeakRate";
    public static final String COL_LEAK_LeakTime = "LeakTime";
    public static final String COL_LEAK_LeakDate = "LeakDate";
    public static final String COL_LEAK_Status = "Status";
    public static final String COL_LEAK_LeakLat = "LeakLat";
    public static final String COL_LEAK_LeakLng = "LeakLng";
    public static final String COL_LEAK_PRODUCT_IMAGE = "LeakImage";

    public static final String TAB_LeakRepairTypes = "LeakRepairTypes";
    public static final String COL_LRT_LeakRepairTypeID = "LeakRepairTypeID";
    public static final String COL_LRT_LeakRepairTypeName = "LeakRepairTypeName";
    public static final String COL_LRT_LeakRepairTypeAbbr = "LeakRepairTypeAbbr";

    public static final String TAB_LeakRepairs = "LeakRepairs";
    public static final String COL_LRepairs_LeakRepairID = "LeakRepairID";
    public static final String COL_LRepairs_LeakID = "LeakID";
    public static final String COL_LRepairs_EmpID = "EmpID";
    public static final String COL_LRepairs_LeakRepairTypeID = "LeakRepairTypeID";
    public static final String COL_LRepairs_LeakRepairRate = "LeakRepairRate";
    public static final String COL_LRepairs_LeakRepairDate = "LeakRepairDate";

    public static final String TAB_LeakRates = "LeakRates";
    public static final String COL_LR_LeakTypeID = "LeakTypeID";
    public static final String COL_LR_RuleCompTypeID = "RuleCompTypeID";
    public static final String COL_LR_StrTypeID = "StrTypeID";
    public static final String COL_LR_LeakRateID = "LeakRateID";
    public static final String COL_LR_LeakRateStart = "LeakRateStart";
    public static final String COL_LR_LeakRateEnd = "LeakRateEnd";
    public static final String COL_LR_LeakRateTime = "LeakRateTime";
    public static final String COL_LR_StrID = "StrID";

    public static final String TAB_Employees = "Employees";
    public static final String COL_EMP_EmpID = "EmpID";
    public static final String COL_EMP_EmpFirst = "EmpFirst";
    public static final String COL_EMP_EmpLast = "EmpLast";
    public static final String COL_EMP_CompID = "CompID";

    public static final String TAB_TVA = "TVA";
    public static final String COL_TVA_TVAID = "TVAID";
    public static final String COL_TVA_TVAName = "TVAName";
    public static final String COL_TVA_TVAEmpID = "TVAEmpID";

    public static final String TAB_RuleComponents = "RuleComponents";
    public static final String COL_RCs_RuleID = "RuleID";
    public static final String COL_RCs_CompTypeID = "CompTypeID";
    public static final String COL_RCs_CompID = "CompID";
    public static final String COL_RCs_RuleCompID = "RuleCompID";

    public static final String TAB_Components = "Components";
    public static final String COL_CompID = "CompID";
    public static final String COL_CompName = "CompName";
    public static final String COL_CompAbbr = "CompAbbr";
    public static final String COL_CompOrder = "CompOrder";

    public static final String TAB_Permits = "Permits";
    public static final String COL_PERMIT_FacID = "FacID";
    public static final String COL_PERMIT_PermID = "PermID";

    public static final String TAB_PermitInventory = "PermitInventory";
    public static final String COL_PI_PermID = "PermID";
    public static final String COL_PI_InvID = "InvID";
    public static final String COL_PI_RouteID = "RouteID";

    public static final String TAB_PermitRates = "PermitRates";
    public static final String COL_PR_PermID = "PermID";
    public static final String COL_PR_LeakTypeID = "LeakTypeID";
    public static final String COL_PR_PermRateID = "PermRateID";
    public static final String COL_PR_PermRateStart = "PermRateStart";
    public static final String COL_PR_PermRateTime = "PermRateTime";

    public static final String TAB_RuleComponentTypes = "RuleComponentTypes";
    public static final String COL_RCT_RuleID = "RuleID";
    public static final String COL_RCT_CompTypeID = "CompTypeID";
    public static final String COL_RCT_RuleCompTypeID = "RuleCompTypeID";

    public static final String TAB_ReasonSkipped = "ReasonSkipped";
    public static final String COL_Reason_SkippedID = "ReasonSkippedID";
    public static final String COL_Reason_Skipped = "ReasonSkipped";
    public static final String COL_Reason_SkippedAbbr = "ReasonSkippedAbbr";

    public static final String TAB_StreamTypes = "StreamTypes";
    public static final String COL_ST_StrTypeID = "StrTypeID";
    public static final String COL_ST_StrTypeName = "StrTypeName";
    public static final String COL_ST_StrTypeAbbr = "StrTypeAbbr";

    public static final String TAB_Streams = "Streams";
    public static final String COL_STREAM_StrTypeID = "StrTypeID";
    public static final String COL_STREAM_StrID = "StrID";
    public static final String COL_STREAM_StrName = "StrName";
    public static final String COL_STREAM_StrAbbr = "StrAbbr";

    public static final String TAB_ComponentTypes = "ComponentTypes";
    public static final String COL_COMP_CompTypeID = "CompTypeID";
    public static final String COL_COMP_CompTypeName = "CompTypeName";
    public static final String COL_COMP_CompAbbr = "CompAbbr";

    public static final String TAB_InspectionResults_Tally = "InspectionResults_Tally";
    public static final String COL_IRT_ReadingID = "ReadingID";
    public static final String COL_IRT_InvID = "InvID";
    public static final String COL_IRT_RouteID = "RouteID";
    public static final String COL_IRT_Count = "Count";
    public static final String COL_IRT_Reading = "Reading";


    public static final String TAB_InventoryRules = "InventoryRules";
    public static final String COL_IR_RouteID = "RouteID";
    public static final String COL_IR_InvID = "InvID";
    public static final String COL_IR_RuleID = "RuleID";
    public static final String COL_IR_Comp_Mon_Cat_id = "Comp_Mon_Cat_id";


    public SQLiteHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    String createRouteConfig = "create table " + TAB_RoutesConfig + " (" + COL_RC_RouteID + " INTEGER," + COL_RC_WorkID + " INTEGER" +
            "," + COL_RC_FacID + " INTEGER, " + COL_RC_Facility + " TEXT," + COL_RC_RuleID + " INTEGER, " + COL_RC_RuleNumber + " TEXT," +
            COL_RC_RouteDateStamp + " DATE, " + COL_RC_RouteName + " TEXT, " + COL_RC_RouteDesc + " TEXT, " + COL_RC_RouteStatus + " TEXT, " +
            COL_RC_DAEP + " BOOLEAN, " + COL_RC_OverwriteStaticTables + " BOOLEAN, " + COL_RC_Inspected + " BOOLEAN, " + COL_RC_BackgroundEntered + " BOOLEAN, " +
            COL_RC_Background + " FLOAT , " + COL_RC_RuleTypeID + " INTEGER, " + COL_RC_InspectionDate + " DATE)";
        sqLiteDatabase.execSQL(createRouteConfig);

    String IndexRouteConfig = "CREATE INDEX " + TAB_RoutesConfig + "_Index ON " + TAB_RoutesConfig + " (" + COL_RC_RouteID + ")";
    sqLiteDatabase.execSQL(IndexRouteConfig);

    String createSubArea = "create table " + TAB_SubAreas + " (" + COL_SubArea_AreaID + " INTEGER," + COL_SubArea_RouteID + " INTEGER" +
            "," + COL_SubArea_SubID + " INTEGER, " + COL_SubArea_SubName + " TEXT," +COL_SubArea_AreaName + " TEXT," + COL_SubArea_Inspected + " BOOLEAN, " + COL_SubArea_SubOrder + " INTEGER," +
            COL_SubArea_TimeStamp + " DATE, " + COL_SubArea_BackgroundEntered + " BOOLEAN, " + COL_SubArea_Background + " FLOAT)";
    sqLiteDatabase.execSQL(createSubArea);

        String createInventory = "create table " + TAB_Inventory + " (" + COL_INV_RouteID + " INTEGER," + COL_INV_SubID + " INTEGER" +
                "," + COL_INV_InvID + " INTEGER, " + COL_INV_CompID + " INTEGER," + COL_INV_InvTag + " TEXT, " + COL_INV_InvLocation + " TEXT," +
                COL_INV_Inspected + " BOOLEAN, " + COL_INV_InvOrder + " INTEGER, " + COL_INV_CompTypeID + " INTEGER, " + COL_INV_StrTypeID + " INTEGER, " +
                COL_INV_StrID + " INTEGER, " + COL_INV_InvSize + " FLOAT, " + COL_INV_Background + " FLOAT, " + COL_INV_Reading + " FLOAT, " +
                COL_INV_TimeStamp + " DATE , " + COL_INV_Skipped + " BOOLEAN, " + COL_INV_ReasonSkippedID + " INTEGER, " + COL_INV_Total + " FLOAT," +
                COL_INV_Critical + " BOOLEAN )";
        sqLiteDatabase.execSQL(createInventory);

    String createLeakPath = "create table " + TAB_LeakPaths + " (" + COL_LEAKPATHS_CompID + " INTEGER," + COL_LEAKPATHS_LeakPathTypeID + " INTEGER)";
    sqLiteDatabase.execSQL(createLeakPath);

    String createLeakPathTypes = "create table " + TAB_LeakPathTypes + " (" + COL_LEAKTYPE_LeakPathTypeID + " INTEGER," + COL_LEAKTYPE_LeakPathTypeName + " TEXT" +
            "," + COL_LEAKTYPE_LeakPathTypeAbbr + " TEXT, " + COL_LEAKTYPE_LeakPathTypeOrder + " INTEGER)";
    sqLiteDatabase.execSQL(createLeakPathTypes);

    String createLeaks = "create table " + TAB_Leaks + " (" + COL_LEAK_LeakID + " INTEGER," + COL_LEAK_InvID + " INTEGER" +
            "," + COL_LEAK_InvTag + " TEXT, " + COL_LEAK_LeakPathTypeID + " INTEGER," + COL_LEAK_LeakTypeID + " INTEGER, " + COL_LEAK_CompID + " INTEGER," +
            COL_LEAK_LeakBit1 + " BOOLEAN, " + COL_LEAK_LeakBit2 + " BOOLEAN, " + COL_LEAK_LeakBit4 + " BOOLEAN, " + COL_LEAK_LeakBit5 + " BOOLEAN, " +
            COL_LEAK_LeakFloat1 + " FLOAT, " + COL_LEAK_LeakRate + " FLOAT, " + COL_LEAK_LeakTime + " FLOAT, " + COL_LEAK_LeakDate + " DATE, " +
            COL_LEAK_Status + " INTEGER , " + COL_LEAK_LeakLat + " FLOAT, " + COL_LEAK_LeakLng + "  FLOAT, " + COL_LEAK_PRODUCT_IMAGE + " BLOZZZ)";
    sqLiteDatabase.execSQL(createLeaks);

    String createLeakRepairTypes = "create table " + TAB_LeakRepairTypes + " (" + COL_LRT_LeakRepairTypeID + " INTEGER," + COL_LRT_LeakRepairTypeName + " TEXT" +
            "," + COL_LRT_LeakRepairTypeAbbr + " TEXT)";
    sqLiteDatabase.execSQL(createLeakRepairTypes);

    String createLeakRepairs = "create table " + TAB_LeakRepairs + " (" + COL_LRepairs_LeakRepairID + " INTEGER," + COL_LRepairs_LeakID + " INTEGER" +
            "," + COL_LRepairs_EmpID + " INTEGER, " + COL_LRepairs_LeakRepairTypeID + " INTEGER," + COL_LRepairs_LeakRepairRate + " FLOAT, " + COL_LRepairs_LeakRepairDate + " DATE)";
    sqLiteDatabase.execSQL(createLeakRepairs);

    String createLeakRates = "create table " + TAB_LeakRates + " (" + COL_LR_LeakTypeID + " INTEGER," + COL_LR_RuleCompTypeID + " INTEGER" +
            "," + COL_LR_StrTypeID + " INTEGER, " + COL_LR_LeakRateID + " INTEGER," + COL_LR_LeakRateStart + " FLOAT, " + COL_LR_LeakRateEnd + " FLOAT," +
            COL_LR_LeakRateTime + " INTEGER, " + COL_LR_StrID + " INTEGER)";
    sqLiteDatabase.execSQL(createLeakRates);

    String createEmployees = "create table " + TAB_Employees + " (" + COL_EMP_EmpID + " INTEGER," + COL_EMP_EmpFirst + " TEXT" +
            "," + COL_EMP_EmpLast + " TEXT, " + COL_EMP_CompID + " INTEGER)";
    sqLiteDatabase.execSQL(createEmployees);

    String createTVA = "create table " + TAB_TVA + " (" + COL_TVA_TVAID + " INTEGER," + COL_TVA_TVAName + " TEXT" +
            "," + COL_TVA_TVAEmpID + " INTEGER)";
    sqLiteDatabase.execSQL(createTVA);

    String createRuleComponents = "create table " + TAB_RuleComponents + " (" + COL_RCs_RuleID + " INTEGER," + COL_RCs_CompTypeID + " INTEGER" +
            "," + COL_RCs_CompID + " INTEGER, " + COL_RCs_RuleCompID + " INTEGER)";
    sqLiteDatabase.execSQL(createRuleComponents);

    String createComponents = "create table " + TAB_Components + " (" + COL_CompID + " INTEGER," + COL_CompName + " TEXT" +
            "," + COL_CompAbbr + " TEXT, " + COL_CompOrder + " INTEGER)";
    sqLiteDatabase.execSQL(createComponents);

    String createPermits = "create table " + TAB_Permits + " (" + COL_PERMIT_FacID + " INTEGER, " + COL_PERMIT_PermID + " INTEGER)";
    sqLiteDatabase.execSQL(createPermits);

    String createPermitInventory = "create table " + TAB_PermitInventory + " (" + COL_PI_PermID + " INTEGER," + COL_PI_InvID + " INTEGER" +
            "," + COL_PI_RouteID + " INTEGER)";
    sqLiteDatabase.execSQL(createPermitInventory);

    String createPermitRates = "create table " + TAB_PermitRates + " (" + COL_PR_PermID + " INTEGER," + COL_PR_LeakTypeID + " INTEGER" +
            "," + COL_PR_PermRateID + " INTEGER, " + COL_PR_PermRateStart + " FLOAT," + COL_PR_PermRateTime + " FLOAT)";
    sqLiteDatabase.execSQL(createPermitRates);

    String createRuleComponentTypes = "create table " + TAB_RuleComponentTypes + " (" + COL_RCT_RuleID + " INTEGER," + COL_RCT_CompTypeID + " INTEGER" +
            "," + COL_RCT_RuleCompTypeID + " INTEGER)";
    sqLiteDatabase.execSQL(createRuleComponentTypes);

    String createReasonSkipped = "create table " + TAB_ReasonSkipped + " (" + COL_Reason_SkippedID + " INTEGER," + COL_Reason_Skipped + " TEXT" +
            "," + COL_Reason_SkippedAbbr + " TEXT)";
    sqLiteDatabase.execSQL(createReasonSkipped);

    String createStreamTypes = "create table " + TAB_StreamTypes + " (" + COL_ST_StrTypeID + " INTEGER," + COL_ST_StrTypeName + " TEXT" +
            "," + COL_ST_StrTypeAbbr + " TEXT)";
    sqLiteDatabase.execSQL(createStreamTypes);

    String createStreams = "create table " + TAB_Streams + " (" + COL_STREAM_StrTypeID + " INTEGER," + COL_STREAM_StrID + " INTEGER" +
            "," + COL_STREAM_StrName + " TEXT, " + COL_STREAM_StrAbbr + " TEXT)";
    sqLiteDatabase.execSQL(createStreams);

    String createComponentTypes = "create table " + TAB_ComponentTypes + " (" + COL_COMP_CompTypeID + " INTEGER," + COL_COMP_CompTypeName + " TEXT" +
            "," + COL_COMP_CompAbbr + " TEXT)";
    sqLiteDatabase.execSQL(createComponentTypes);

    String createInspectionResults_Tally = "create table " + TAB_InspectionResults_Tally + " (" + COL_IRT_ReadingID + " INTEGER," + COL_IRT_InvID + " INTEGER" +
            "," + COL_IRT_RouteID + " INTEGER, " + COL_IRT_Count + " INTEGER," + COL_IRT_Reading + " FLOAT)";
    sqLiteDatabase.execSQL(createInspectionResults_Tally);

    String createInventoryRules= "create table " + TAB_InventoryRules + " (" + COL_IR_RouteID + " INTEGER," + COL_IR_InvID + " INTEGER," +
                COL_IR_RuleID +" INTEGER," + COL_IR_Comp_Mon_Cat_id + " INTEGER)";
        sqLiteDatabase.execSQL(createInventoryRules);

        //For Upgrade to version 2.1 changing data type of LeakImage of Leaks table.
        sqLiteDatabase.execSQL("ALTER TABLE Leaks RENAME TO Leaks_old");
        sqLiteDatabase.execSQL("CREATE TABLE Leaks (LeakID INTEGER, InvID INTEGER, InvTag TEXT, LeakPathTypeID INTEGER, LeakTypeID INTEGER, CompID INTEGER, LeakBit1 BOOLEAN," +
                "LeakBit2 BOOLEAN, LeakBit4 BOOLEAN ,LeakBit5 BOOLEAN, LeakFloat1 FLOAT,LeakRate FLOAT, LeakTime FLOAT, LeakDate DATE," +
                "Status INTEGER,LeakLat FLOAT, LeakLng FLOAT, LeakImage TEXT)");
        sqLiteDatabase.execSQL("INSERT INTO Leaks (LeakID,InvID,InvTag,LeakPathTypeID,LeakTypeID,CompID,LeakBit1,LeakBit2,LeakBit4,LeakBit5,LeakFloat1,LeakRate,LeakTime,LeakDate," +
                "Status,LeakLat,LeakLng,LeakImage)" +
                "SELECT LeakID,InvID,InvTag,LeakPathTypeID,LeakTypeID,CompID,LeakBit1,LeakBit2,LeakBit4,LeakBit5,LeakFloat1,LeakRate,LeakTime,LeakDate," +
                "Status,LeakLat,LeakLng,LeakImage from Leaks_old");
        sqLiteDatabase.execSQL("DROP TABLE Leaks_old");

    }


    @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {

        //For Upgrade to version 2.1 changing data type of LeakImage of Leaks table.
        switch (newversion){
            case 2:sqLiteDatabase.execSQL("ALTER TABLE Leaks RENAME TO Leaks_old");
                    sqLiteDatabase.execSQL("CREATE TABLE Leaks (LeakID INTEGER, InvID INTEGER, InvTag TEXT, LeakPathTypeID INTEGER, LeakTypeID INTEGER, CompID INTEGER, LeakBit1 BOOLEAN," +
                            "LeakBit2 BOOLEAN, LeakBit4 BOOLEAN ,LeakBit5 BOOLEAN, LeakFloat1 FLOAT,LeakRate FLOAT, LeakTime FLOAT, LeakDate DATE," +
                            "Status INTEGER,LeakLat FLOAT, LeakLng FLOAT, LeakImage TEXT)");
                    sqLiteDatabase.execSQL("INSERT INTO Leaks (LeakID,InvID,InvTag,LeakPathTypeID,LeakTypeID,CompID,LeakBit1,LeakBit2,LeakBit4,LeakBit5,LeakFloat1,LeakRate,LeakTime,LeakDate," +
                            "Status,LeakLat,LeakLng,LeakImage)" +
                            "SELECT LeakID,InvID,InvTag,LeakPathTypeID,LeakTypeID,CompID,LeakBit1,LeakBit2,LeakBit4,LeakBit5,LeakFloat1,LeakRate,LeakTime,LeakDate," +
                            "Status,LeakLat,LeakLng,LeakImage from Leaks_old");
                    sqLiteDatabase.execSQL("DROP TABLE Leaks_old");
                    break;
        }
        }

    public void InsertRoutesConfig(RoutesConfig routesConfig){
        SQLiteDatabase database=getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("RouteID", routesConfig.getRouteID());
            contentValues.put("WorkID", routesConfig.getWorkID());
            contentValues.put("FacID", routesConfig.getFacID());
            contentValues.put("RuleTypeID", routesConfig.getRuleTypeID());
            contentValues.put("RuleID", routesConfig.getRuleID());
            contentValues.put("Facility", routesConfig.getFacility());
            contentValues.put("RuleNumber", routesConfig.getRuleNumber());
            contentValues.put("RouteDateStamp", String.valueOf(routesConfig.getRouteDateStamp()));
            contentValues.put("RouteName", routesConfig.getRouteName());
            contentValues.put("RouteDesc", routesConfig.getRouteDesc());
            contentValues.put("RouteStatus", routesConfig.getRouteStatus());
            contentValues.put("DAEP", routesConfig.getDAEP());
            contentValues.put("OverwriteStaticTables", routesConfig.getOverwriteStaticTables());
            contentValues.put("Inspected", routesConfig.getInspected());
            contentValues.put("BackgroundEntered", routesConfig.getBackgroundEntered());
            contentValues.put("Background", routesConfig.getBackground());
            contentValues.put("InspectionDate", "");
            database.insert(TAB_RoutesConfig, null, contentValues);
            database.close();
    }

    public void InsertTVA(TVA tva){
        SQLiteDatabase database = getReadableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("TVAID",tva.getTVAID());
        contentValues.put("TVAName",tva.getTVAName());
        contentValues.put("TVAEmpID",tva.getTVAEmpID());
        database.insert(TAB_TVA,null,contentValues);
        database.close();
    }

    public int getTVACount() {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from TVA";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;

    }

    public void InsertSubAreas(Subarea subarea){
        SQLiteDatabase database=getReadableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("AreaID", subarea.getAreaID());
            contentValues.put("RouteID", subarea.getRouteID());
            contentValues.put("SubID", subarea.getSubID());
            contentValues.put("Inspected", subarea.isInspected());
            contentValues.put("SubName", subarea.getSubName());
            contentValues.put("AreaName", subarea.getAreaName());
            contentValues.put("SubOrder", subarea.getSubOrder());
            database.insert(TAB_SubAreas, null, contentValues);
            database.close();

    }

    public List<GetAllRooutesPojo> getAll(){

        String Faci,RNum,RouteDateStamp,RName,inspect,inspectiondate;
        float Background,per,InspCount,DivCount;
        String DAEP;
        int WID,RID,Count;

        SQLiteDatabase db=getWritableDatabase();
        String query="select RouteID,RouteName,WorkID,Facility,RuleNumber,RouteDateStamp,DAEP,Background,Inspected,InspectionDate from RoutesConfig";

        Cursor result=db.rawQuery(query,null);
        while (result.moveToNext()){
            RID=(result.getInt(result.getColumnIndex("RouteID")));
            RName=(result.getString(result.getColumnIndex("RouteName")));
            WID=(result.getInt(result.getColumnIndex("WorkID")));
            Faci=(result.getString(result.getColumnIndex("Facility")));
            RNum=(result.getString(result.getColumnIndex("RuleNumber")));
            RouteDateStamp=(result.getString(result.getColumnIndex("RouteDateStamp")));
            inspect=result.getString(result.getColumnIndex("Inspected"));
            Background=result.getFloat(result.getColumnIndex("Background"));
            DAEP= result.getString(result.getColumnIndex("DAEP"));
            Count=getRouteCompCnt(RID);
            inspectiondate=result.getString(result.getColumnIndex("InspectionDate"));
            DivCount=(float)getRouteCompCnt(RID);
            InspCount=(float)getRouteInspComp(RID);
            per=(InspCount/DivCount)*100;

            GetAllRooutesPojo getAllRooutesPojo =new GetAllRooutesPojo(RID,RName,WID,Faci,RNum,RouteDateStamp,inspect);
            getAllRooutesPojo.setDAEP(DAEP);
            getAllRooutesPojo.setBackground(Background);
            getAllRooutesPojo.setCount(Count);
            getAllRooutesPojo.setPercent(per);
            getAllRooutesPojo.setInspdate(inspectiondate);
            resultstring.add(getAllRooutesPojo);
        }

        result.close();
        db.close();
        return resultstring;
    }

    public int getRouteInspComp(int rid) {
        int Count=0;
        SQLiteDatabase database =getWritableDatabase();
        String q = "Select COUNT(InvID) From Inventory Where (Inspected = 1 OR Inspected = 'true') AND RouteID = "+rid;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(InvID)"));
        }
        cursor.close();
        database.close();
        return Count;
    }

    public int getRouteCompCnt(int rid) {

        int Count = 0;
        SQLiteDatabase database=getWritableDatabase();
        String q="Select COUNT(InvID) from Inventory where RouteID = "+rid;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(InvID)"));
        }
        cursor.close();
        database.close();
        return Count;
    }

    public List<SubAreasPojo> arrayList=new ArrayList();

    public List<SubAreasPojo> getSubareas(int routeid) {
        int Subid,RouteId,count;
        String Date,AreaName;
        int inspected;
        float background,countDiv,inspCnt,per;
        String SubName;
        SQLiteDatabase database=getReadableDatabase();
        String query="Select SubID,RouteID,SubName,AreaName,Inspected,TimeStamp,Background from SubAreas where RouteID = "+routeid+ " ORDER BY SubOrder ASC";
        Cursor c=database.rawQuery(query,null);
        while(c.moveToNext()){
            Subid=c.getInt(c.getColumnIndex("SubID"));
            RouteId=c.getInt(c.getColumnIndex("RouteID"));
             inspected=c.getInt(c.getColumnIndex("Inspected"));
            background=c.getFloat(c.getColumnIndex("Background"));
           SubName=c.getString(c.getColumnIndex("SubName"));
           AreaName=c.getString(c.getColumnIndex("AreaName"));
           Date = c.getString(c.getColumnIndex("TimeStamp"));
           count=getCompCnt(Subid,RouteId);
           countDiv=(float)getCompCnt(Subid,RouteId);
           inspCnt=(float)getInspCnt(Subid,RouteId);
           per=(inspCnt/countDiv)*100;
           SubAreasPojo subAreasPojo = new SubAreasPojo(Subid,inspected,SubName,background,Date);
           subAreasPojo.setRouteId(RouteId);
           subAreasPojo.setCnt(count);
           subAreasPojo.setPer(per);
           subAreasPojo.setAreaName(AreaName);
            arrayList.add(subAreasPojo);
        }
        c.close();
        database.close();

        return arrayList ;
    }

    private int getInspCnt(int subid, int routeId) {

        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q="Select COUNT(InvID) From Inventory Where (Inspected = 1 OR Inspected = 'true') AND RouteID = "+routeId+" AND SubID = "+subid;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(InvID)"));
        }
        cursor.close();
        database.close();
        return Count;
    }

    private int getCompCnt(int subid, int routeId) {
        int Count=1;
        SQLiteDatabase database=getWritableDatabase();
        String q="Select COUNT(InvID) From Inventory Where RouteID = "+routeId+" AND SubID ="+subid;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(InvID)"));
        }
        cursor.close();
        database.close();
        return Count;
    }

    List<SubAreasPojo> subAreasPojoList=new ArrayList<>();
    public List<SubAreasPojo> getSubareasInspected(int routeid) {
        int Subid,RouteId,count;
        String Date;
        int inspected;
        float background,countDiv,inspCnt,per;
        String SubName,AreaName;
        SQLiteDatabase database=getReadableDatabase();
        String query="Select SubID,RouteID,SubName,AreaName,Inspected,TimeStamp,Background from SubAreas where Inspected =1 and RouteID = "+ routeid+ " ORDER BY SubOrder ASC";
        Cursor c=database.rawQuery(query,null);
        while(c.moveToNext()){
            Subid=c.getInt(c.getColumnIndex("SubID"));
            RouteId = c.getInt(c.getColumnIndex("RouteID"));
            inspected=c.getInt(c.getColumnIndex("Inspected"));
            background=c.getFloat(c.getColumnIndex("Background"));
            SubName=c.getString(c.getColumnIndex("SubName"));
            AreaName=c.getString(c.getColumnIndex("AreaName"));
            Date = c.getString(c.getColumnIndex("TimeStamp"));
            count=getCompCnt(Subid,RouteId);
            countDiv=(float)getCompCnt(Subid,RouteId);
            inspCnt=(float)getInspCnt(Subid,RouteId);
            per=(inspCnt/countDiv)*100;
            SubAreasPojo subAreasPojo=new SubAreasPojo(Subid,inspected,SubName,background,Date);
            subAreasPojo.setRouteId(RouteId);
            subAreasPojo.setCnt(count);
            subAreasPojo.setPer(per);
            subAreasPojo.setAreaName(AreaName);
            arrayList.add(subAreasPojo);
        }
        c.close();
        database.close();

        return arrayList ;
    }


    public List<SubAreasPojo> getSubareasUnInspected(int routeid) {
        int Subid,RouteId,count;
        String Date;
        int inspected;
        float background,countDiv,inspCnt,per;
        String SubName,AreaName;
        SQLiteDatabase database=getReadableDatabase();
        String query="Select SubID,RouteID,SubName,AreaName,Inspected,TimeStamp,Background from SubAreas where Inspected = 0 and RouteID = "+routeid+ " ORDER BY SubOrder ASC";
        Cursor c=database.rawQuery(query,null);
        while(c.moveToNext()){
            Subid=c.getInt(c.getColumnIndex("SubID"));
            RouteId = c.getInt(c.getColumnIndex("RouteID"));
            inspected=c.getInt(c.getColumnIndex("Inspected"));
            background=c.getFloat(c.getColumnIndex("Background"));
            SubName=c.getString(c.getColumnIndex("SubName"));
            AreaName=c.getString(c.getColumnIndex("AreaName"));
            Date = c.getString(c.getColumnIndex("TimeStamp"));
            count=getCompCnt(Subid,RouteId);
            countDiv=(float)getCompCnt(Subid,RouteId);
            inspCnt=(float)getInspCnt(Subid,RouteId);
            per=(inspCnt/countDiv)*100;
            SubAreasPojo subAreasPojo = new SubAreasPojo(Subid,inspected,SubName,background,Date);
            subAreasPojo.setRouteId(RouteId);
            subAreasPojo.setCnt(count);
            subAreasPojo.setPer(per);
            subAreasPojo.setAreaName(AreaName);
            arrayList.add(subAreasPojo);
        }
        c.close();
        database.close();

        return arrayList ;
    }


    public float getInvBackground(int Id){

        float Back=0.0f;
        SQLiteDatabase database = getWritableDatabase();
        String query = "Select Background from Inventory where InvID = " + Id;
        Cursor c = database.rawQuery(query,null);
        while (c.moveToNext()){
            Back=c.getFloat(c.getColumnIndex("Background"));

        }

        return Back;
    }


    public float getBackground(int Id){

        float Back=0.0f;
        SQLiteDatabase database = getWritableDatabase();
        String query = "Select Background from RoutesConfig where RouteID = " + Id;
        Cursor c = database.rawQuery(query,null);
        while (c.moveToNext()){
            Back=c.getFloat(c.getColumnIndex("Background"));

        }

        return Back;
    }

//    public List<>

    @SuppressLint("SimpleDateFormat")
    public void InsertInventory(Inventory inventory){
        SQLiteDatabase database=getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_INV_RouteID, inventory.getRouteID());
        values.put(COL_INV_SubID, inventory.getSubID());
        values.put(COL_INV_InvID, inventory.getInvID());
        values.put(COL_INV_CompID, inventory.getCompID());
        values.put(COL_INV_InvTag, inventory.getInvTag());
        values.put(COL_INV_InvLocation, inventory.getInvLocation());
        values.put(COL_INV_Inspected, "false");
        values.put(COL_INV_InvOrder, inventory.getInvOrder());
        values.put(COL_INV_CompTypeID, inventory.getCompTypeID());
        values.put(COL_INV_StrTypeID, inventory.getStrTypeID());
        values.put(COL_INV_StrID, inventory.getStrID());
        values.put(COL_INV_InvSize, inventory.getInvSize());
        values.put(COL_INV_Background, 0);
        values.put(COL_INV_Reading, 0.0);
        values.put(COL_INV_Skipped, 0.0);
        values.put(COL_INV_ReasonSkippedID, 0);
        values.put(COL_INV_Total, inventory.getTotal());
        values.put(COL_INV_Critical,inventory.getCritical());
        values.put(COL_INV_TimeStamp, new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(new Date()));
        database.insert(TAB_Inventory,null,values);
        database.close();

    }



    public  void UpdateBackgroundReadingSubAreas(int routeId,int subId,float backgroundReading){
        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("Background",backgroundReading);
        database.update(TAB_SubAreas,contentValues,"RouteID = "+routeId +" and SubID="+subId ,null);
        database.update(TAB_Inventory,contentValues,"RouteID = "+routeId + " and SubID="+subId  ,null);
        database.close();
    }
    public  void UpdateBackgroundReadingRoutesCofig(int routeId,float backgroundReading){
        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("Background",backgroundReading);
        database.update(TAB_RoutesConfig,contentValues,"RouteID="+routeId,null);
        database.update(TAB_SubAreas,contentValues,"RouteID="+routeId,null);
        database.update(TAB_Inventory,contentValues,"RouteID="+routeId,null);
        database.close();
    }
    public void UpdateBackgroundReadingInventory(int RouteId,int SubId,int InvId,float backgroundReading){
        SQLiteDatabase database =getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Background",backgroundReading);
        database.update(TAB_Inventory,contentValues,"RouteID = "+RouteId +" and SubID = "+SubId +" and InvID = "+InvId,null);
        database.close();
    }
    //Pk Update Background Entered Flag.
    public void UpdateBackgroundEnteredFlagRoutes(int routeId){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("BackgroundEntered",1);
        database.update(TAB_RoutesConfig,contentValues,"RouteID = "+routeId,null);
        database.update(TAB_SubAreas,contentValues,"RouteID = "+routeId,null);
        database.close();
    }
    public void UpdateBackgroundEnteredFlagSubAreas(int routeId,int subId){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("BackgroundEntered",1);
        database.update(TAB_SubAreas,contentValues,"SubID = "+subId + " and RouteID = "+routeId,null);
        database.close();
    }

    public List<ComponentsListPojo> getInspectedID(int id,int Rid){
        String LOCATION,NAME,TAG;
        int compId,subId,RouteId,InvId;
        float SIZE,Background,Reading;
        String INSPECTED;
        List<ComponentsListPojo> listPojos=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT Inventory.InvID AS InvID, Inventory.CompID AS CompID, Components.CompName AS CompName , Inventory.InvTag AS InvTag , " +
                "Inventory.Inspected AS Inspected,Inventory.Reading AS Reading,Inventory.Background AS Background,Inventory.RouteID as RouteID,Inventory.SubID as SubID, Inventory.InvSize as InvSize, Inventory.InvLocation AS InvLocation " +
                "FROM Inventory INNER JOIN Components ON Inventory.CompID = Components.CompID " +
                "WHERE Inventory.SubID = SubID and (Inspected = 1  OR Inspected = 'true') and Inventory.RouteID = "+Rid+" and Inventory.SubID = "+id+" ORDER BY Inventory.InvOrder ASC";

        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            do{
                compId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CompID")));
                subId=cursor.getInt(cursor.getColumnIndex("SubID"));
                RouteId=cursor.getInt(cursor.getColumnIndex("RouteID"));
                InvId=cursor.getInt(cursor.getColumnIndex("InvID"));
                NAME = cursor.getString(cursor.getColumnIndex("CompName"));
                TAG = cursor.getString(cursor.getColumnIndex("InvTag"));
                INSPECTED =cursor.getString(cursor.getColumnIndex("Inspected"));
                SIZE = cursor.getFloat(cursor.getColumnIndex("InvSize"));
                LOCATION = cursor.getString(cursor.getColumnIndex("InvLocation"));
                Background =cursor.getFloat(cursor.getColumnIndex("Background"));
                Reading = cursor.getFloat(cursor.getColumnIndex("Reading"));

                ComponentsListPojo componentsListPojo = new ComponentsListPojo(compId,subId, NAME, TAG, INSPECTED, SIZE, LOCATION);
                componentsListPojo.setRouteId(RouteId);
                componentsListPojo.setInvID(InvId);
                componentsListPojo.setBackread(Background);
                componentsListPojo.setReading(Reading);
                listPojos.add(componentsListPojo);
            }
            while(cursor.moveToNext());
        }
        else{
            //No records found in database
        }
        cursor.close();
        db.close();
        return listPojos;
    }
    public List<ComponentsListPojo> getInspectedIDSort(int id,int Rid,String tag){
        String TAG,LOCATION,NAME;
        int compId,subId,RouteId,InvId;
        float SIZE,Background,Reading;
        String INSPECTED;
        List<ComponentsListPojo> listPojos=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT Inventory.InvID AS InvID, Inventory.CompID AS CompID, Components.CompName AS CompName , Inventory.InvTag AS InvTag , " +
                "Inventory.Inspected AS Inspected,Inventory.Reading AS Reading,Inventory.Background AS Background,Inventory.RouteID as RouteID,Inventory.SubID as SubID, Inventory.InvSize as InvSize, Inventory.InvLocation AS InvLocation " +
                "FROM Inventory INNER JOIN Components ON Inventory.CompID = Components.CompID " +
                "WHERE Inventory.SubID = SubID and (Inspected = 1 OR Inspected = 'true') and Inventory.InvTag LIKE '%"+ tag +"%' AND Inventory.RouteID = "+Rid+" and Inventory.SubID = "+id+" ORDER BY Inventory.InvOrder ASC";

        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            do{
                compId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CompID")));
                subId = cursor.getInt(cursor.getColumnIndex("SubID"));
                RouteId = cursor.getInt(cursor.getColumnIndex("RouteID"));
                InvId = cursor.getInt(cursor.getColumnIndex("InvID"));
                NAME = cursor.getString(cursor.getColumnIndex("CompName"));
                TAG = cursor.getString(cursor.getColumnIndex("InvTag"));
                INSPECTED =cursor.getString(cursor.getColumnIndex("Inspected"));
                SIZE = cursor.getFloat(cursor.getColumnIndex("InvSize"));
                LOCATION = cursor.getString(cursor.getColumnIndex("InvLocation"));
                Background =cursor.getFloat(cursor.getColumnIndex("Background"));
                Reading =cursor.getFloat(cursor.getColumnIndex("Reading"));

                ComponentsListPojo componentsListPojo = new ComponentsListPojo(compId,subId, NAME, TAG, INSPECTED, SIZE, LOCATION);
                componentsListPojo.setRouteId(RouteId);
                componentsListPojo.setInvID(InvId);
                componentsListPojo.setBackread(Background);
                componentsListPojo.setReading(Reading);
                listPojos.add(componentsListPojo);
            }
            while(cursor.moveToNext());
        }
        else{
            //No records found in database
        }
        cursor.close();
        db.close();
        return listPojos;
    }

    public List<ComponentsListPojo> getGridComponents(int id,int routeID){
        String TAG,LOCATION,NAME,subAreaName,routeAreaName,compTypeName;
        int compId,subId,RouteId,InvId,compTypeId;
        float SIZE,Background,Reading;
        String INSPECTED;
        List<ComponentsListPojo> gridPojos = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        String query = "SELECT Inventory.InvID AS InvID, Inventory.CompID AS CompID, Components.CompName AS CompName," +
                "Inventory.InvTag AS InvTag, Inventory.Reading AS Reading, Inventory.CompTypeID," +
                "Inventory.Inspected AS Inspected,Inventory.Background AS Background,Inventory.RouteID AS RouteID," +
                "Inventory.SubID AS SubID, Inventory.InvSize AS InvSize, Inventory.InvLocation AS InvLocation," +
                "SubAreas.SubName AS SubName, SubAreas.AreaName AS AreaName," +
                "ComponentTypes.CompTypeID, ComponentTypes.CompTypeName " +
                "FROM Inventory " +
                "INNER JOIN Components ON Inventory.CompID = Components.CompID " +
                "INNER JOIN SubAreas ON SubAreas.RouteID = Inventory.RouteID AND SubAreas.SubID = Inventory.SubID " +
                "INNER JOIN ComponentTypes ON ComponentTypes.CompTypeID = Inventory.CompTypeID " +
                "WHERE Inventory.RouteID ="+routeID+" AND Inventory.SubID ="+id+" ORDER BY Inventory.InvOrder ASC";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do {
                compId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CompID")));
                subId = cursor.getInt(cursor.getColumnIndex("SubID"));
                subAreaName = cursor.getString(cursor.getColumnIndex("SubName"));
                RouteId = cursor.getInt(cursor.getColumnIndex("RouteID"));
                routeAreaName = cursor.getString(cursor.getColumnIndex("AreaName"));
                InvId = cursor.getInt(cursor.getColumnIndex("InvID"));
                NAME = cursor.getString(cursor.getColumnIndex("CompName"));
                TAG = cursor.getString(cursor.getColumnIndex("InvTag"));
                SIZE = cursor.getFloat(cursor.getColumnIndex("InvSize"));
                LOCATION = cursor.getString(cursor.getColumnIndex("InvLocation"));
                Background = cursor.getFloat(cursor.getColumnIndex("Background"));
                Reading = cursor.getFloat(cursor.getColumnIndex("Reading"));
                compTypeId = cursor.getInt(cursor.getColumnIndex("CompTypeID"));
                compTypeName = cursor.getString(cursor.getColumnIndex("CompTypeName"));
                int gridInsp = cursor.getInt(cursor.getColumnIndex("Inspected"));
                if (gridInsp == 1){
                    INSPECTED = "true";
                }
                else {
                    INSPECTED = "false";
                }

                ComponentsListPojo gridListPojo = new ComponentsListPojo(compId,subId, NAME, TAG, INSPECTED, SIZE, LOCATION);
                gridListPojo.setRouteId(RouteId);
                gridListPojo.setInvID(InvId);
                gridListPojo.setBackread(Background);
                gridListPojo.setInvReading(Reading);
                gridListPojo.setSubName(subAreaName);
                gridListPojo.setAreaName(routeAreaName);
                gridListPojo.setCompTypeID(compTypeId);
                gridListPojo.setCompTypeName(compTypeName);
                gridPojos.add(gridListPojo);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return gridPojos;
    }
    public List<ComponentsListPojo> getGridSearchComponents(int id,int routeID,String invtag){
        String TAG,LOCATION,NAME,subAreaName,routeAreaName,compTypeName;
        int compId,subId,RouteId,InvId,compTypeId;
        float SIZE,Background,Reading;
        String INSPECTED;
        List<ComponentsListPojo> gridPojos = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        String query = "SELECT Inventory.InvID AS InvID, Inventory.CompID AS CompID, Components.CompName AS CompName," +
                "Inventory.InvTag AS InvTag, Inventory.Reading AS Reading, Inventory.CompTypeID," +
                "Inventory.Inspected AS Inspected,Inventory.Background AS Background,Inventory.RouteID AS RouteID," +
                "Inventory.SubID AS SubID, Inventory.InvSize AS InvSize, Inventory.InvLocation AS InvLocation," +
                "SubAreas.SubName AS SubName, SubAreas.AreaName AS AreaName," +
                "ComponentTypes.CompTypeID, ComponentTypes.CompTypeName " +
                "FROM Inventory " +
                "INNER JOIN Components ON Inventory.CompID = Components.CompID " +
                "INNER JOIN SubAreas ON SubAreas.RouteID = Inventory.RouteID  AND SubAreas.SubID = Inventory.SubID " +
                "INNER JOIN ComponentTypes ON ComponentTypes.CompTypeID = Inventory.CompTypeID " +
                "WHERE Inventory.RouteID ="+routeID+" AND Inventory.SubID ="+id+" AND Inventory.InvTag LIKE '%"+invtag+"%' ORDER BY Inventory.InvOrder ASC";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do {
                compId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CompID")));
                subId = cursor.getInt(cursor.getColumnIndex("SubID"));
                subAreaName = cursor.getString(cursor.getColumnIndex("SubName"));
                RouteId = cursor.getInt(cursor.getColumnIndex("RouteID"));
                routeAreaName = cursor.getString(cursor.getColumnIndex("AreaName"));
                InvId = cursor.getInt(cursor.getColumnIndex("InvID"));
                NAME = cursor.getString(cursor.getColumnIndex("CompName"));
                TAG = cursor.getString(cursor.getColumnIndex("InvTag"));
                SIZE = cursor.getFloat(cursor.getColumnIndex("InvSize"));
                LOCATION = cursor.getString(cursor.getColumnIndex("InvLocation"));
                Background = cursor.getFloat(cursor.getColumnIndex("Background"));
                Reading = cursor.getFloat(cursor.getColumnIndex("Reading"));
                compTypeId = cursor.getInt(cursor.getColumnIndex("CompTypeID"));
                compTypeName = cursor.getString(cursor.getColumnIndex("CompTypeName"));
                int gridInsp = cursor.getInt(cursor.getColumnIndex("Inspected"));
                if (gridInsp == 1){
                    INSPECTED = "true";
                }
                else {
                    INSPECTED = "false";
                }

                ComponentsListPojo gridListPojo = new ComponentsListPojo(compId,subId, NAME, TAG, INSPECTED, SIZE, LOCATION);
                gridListPojo.setRouteId(RouteId);
                gridListPojo.setInvID(InvId);
                gridListPojo.setBackread(Background);
                gridListPojo.setInvReading(Reading);
                gridListPojo.setSubName(subAreaName);
                gridListPojo.setAreaName(routeAreaName);
                gridListPojo.setCompTypeID(compTypeId);
                gridListPojo.setCompTypeName(compTypeName);
                gridPojos.add(gridListPojo);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return gridPojos;
    }


    public List<ComponentsListPojo> getUnInspectedID(int id,int Rid){
        String TAG,LOCATION,NAME;
        int compId,subId,RouteId,InvId;
        float SIZE,Background;
        String INSPECTED;
        List<ComponentsListPojo> listPojos=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT Inventory.InvID AS InvID, Inventory.CompID AS CompID, Components.CompName AS CompName , Inventory.InvTag AS InvTag , " +
                "Inventory.Inspected AS Inspected,Inventory.Background AS Background,Inventory.RouteID as RouteID,Inventory.SubID as SubID, Inventory.InvSize as InvSize, Inventory.InvLocation AS InvLocation " +
                "FROM Inventory INNER JOIN Components ON Inventory.CompID = Components.CompID " +
                "WHERE Inventory.SubID = SubID and (Inspected = 'false'  OR Inspected = 0) and Inventory.RouteID = "+Rid+" and Inventory.SubID = "+id+" ORDER BY Inventory.InvOrder ASC";

        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            do{
                compId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CompID")));
                subId = cursor.getInt(cursor.getColumnIndex("SubID"));
                RouteId = cursor.getInt(cursor.getColumnIndex("RouteID"));
                InvId = cursor.getInt(cursor.getColumnIndex("InvID"));
                NAME = cursor.getString(cursor.getColumnIndex("CompName"));
                TAG = cursor.getString(cursor.getColumnIndex("InvTag"));
                INSPECTED = cursor.getString(cursor.getColumnIndex("Inspected"));
                SIZE = cursor.getFloat(cursor.getColumnIndex("InvSize"));
                LOCATION = cursor.getString(cursor.getColumnIndex("InvLocation"));
                Background = cursor.getFloat(cursor.getColumnIndex("Background"));

                ComponentsListPojo componentsListPojo = new ComponentsListPojo(compId,subId, NAME, TAG, INSPECTED, SIZE, LOCATION);
                componentsListPojo.setRouteId(RouteId);
                componentsListPojo.setInvID(InvId);
                componentsListPojo.setBackread(Background);
                listPojos.add(componentsListPojo);
            }
            while(cursor.moveToNext());
        }
        else{
            //No records found in database
        }
        cursor.close();
        db.close();
        return listPojos;
    }
    public List<ComponentsListPojo> getUnInspectedIDSort(int id,int Rid,String tag){
        String TAG,LOCATION,NAME;
        int compId,subId,RouteId,InvId;
        float SIZE,Background;
        String INSPECTED;
        List<ComponentsListPojo> listPojos=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT Inventory.InvID AS InvID, Inventory.CompID AS CompID, Components.CompName AS CompName , Inventory.InvTag AS InvTag , " +
                "Inventory.Inspected AS Inspected,Inventory.Background AS Background,Inventory.RouteID as RouteID,Inventory.SubID as SubID, Inventory.InvSize as InvSize, Inventory.InvLocation AS InvLocation " +
                "FROM Inventory INNER JOIN Components ON Inventory.CompID = Components.CompID " +
                "WHERE Inventory.SubID = SubID and (Inventory.Inspected = 'false'  OR Inventory.Inspected = 0) and Inventory.InvTag LIKE '%"+ tag +"%' AND Inventory.RouteID = "+Rid+" and Inventory.SubID = "+id+" ORDER BY Inventory.InvOrder ASC";

        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            do{
                compId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CompID")));
                subId = cursor.getInt(cursor.getColumnIndex("SubID"));
                RouteId = cursor.getInt(cursor.getColumnIndex("RouteID"));
                InvId = cursor.getInt(cursor.getColumnIndex("InvID"));
                NAME = cursor.getString(cursor.getColumnIndex("CompName"));
                TAG = cursor.getString(cursor.getColumnIndex("InvTag"));
                INSPECTED = cursor.getString(cursor.getColumnIndex("Inspected"));
                SIZE = cursor.getFloat(cursor.getColumnIndex("InvSize"));
                LOCATION = cursor.getString(cursor.getColumnIndex("InvLocation"));
                Background =cursor.getFloat(cursor.getColumnIndex("Background"));

                ComponentsListPojo componentsListPojo = new ComponentsListPojo(compId,subId, NAME, TAG, INSPECTED, SIZE, LOCATION);
                componentsListPojo.setRouteId(RouteId);
                componentsListPojo.setInvID(InvId);
                componentsListPojo.setBackread(Background);
                listPojos.add(componentsListPojo);
            }
            while(cursor.moveToNext());
        }
        else{
            //No records found in database
        }
        cursor.close();
        db.close();
        return listPojos;
    }


    //abhishek
    public  void InsertReasonSkipped(ReasonSkipped reasonSkipped){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ReasonSkippedID",reasonSkipped.getReasonSkippedID());
        contentValues.put("ReasonSkipped",reasonSkipped.getReasonSkipped());
        contentValues.put(COL_Reason_SkippedAbbr,reasonSkipped.getReasonSkippedAddress());
        sqLiteDatabase.insert(TAB_ReasonSkipped,null,contentValues);
        sqLiteDatabase.close();
    }

    public int getReasonSkippedCnt() {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from ReasonSkipped";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;
    }

    public  void InsertLeakPathTypes(LeakPathTypes leakPathTypes){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LeakPathTypeID",leakPathTypes.getLeakPathTypeID());
        contentValues.put("LeakPathTypeName",leakPathTypes.getLeakPathTypeName());
        contentValues.put("LeakPathTypeAbbr",leakPathTypes.getLeakPathTypeAbbr());
        contentValues.put("LeakPathTypeOrder",leakPathTypes.getLeakPathTypeOrder());
        sqLiteDatabase.insert(TAB_LeakPathTypes,null,contentValues);
        sqLiteDatabase.close();
    }

    public int getLeakPathTypeCnt() {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from LeakPathTypes";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;

    }




    public void InsertLeakRepairTypes(LeakRepairTypes leakRepairTypes) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LeakRepairTypeID",leakRepairTypes.getLeakRepairTypeID());
        contentValues.put("LeakRepairTypeName",leakRepairTypes.getLeakRepairTypeName());
        contentValues.put("LeakRepairTypeAbbr",leakRepairTypes.getLeakRepairTypeAbbr());
        sqLiteDatabase.insert(TAB_LeakRepairTypes,null,contentValues);
        sqLiteDatabase.close();
    }

    public int getLeakRepairTypesCnt() {

        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from LeakRepairTypes";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;
    }

    public void InsertLeakPaths(LeakPaths leakPaths) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CompID",leakPaths.getCompID());
        contentValues.put("LeakPathTypeID",leakPaths.getLeakPathTypeID());
        sqLiteDatabase.insert(TAB_LeakPaths,null,contentValues);
        sqLiteDatabase.close();
    }

    public void InsertLeakRates(LeakRates leakRates) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LeakTypeID",leakRates.getLeakTypeID());
        contentValues.put("RuleCompTypeID",leakRates.getRuleCompTypeID());
        contentValues.put("StrTypeID",leakRates.getStrTypeID());
        contentValues.put("LeakRateID",leakRates.getLeakRateID());
        contentValues.put("LeakRateStart",leakRates.getLeakRateStart());
        contentValues.put("LeakRateEnd",leakRates.getLeakRateEnd());
        contentValues.put("LeakRateTime",leakRates.getLeakRateTime());
        contentValues.put("StrID",leakRates.getStrID());
        sqLiteDatabase.insert(TAB_LeakRates,null,contentValues);
        sqLiteDatabase.close();
    }

    public int getLeakRatesCnt() {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from LeakRates";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;

    }

    public void Insertemployees(Employees employees) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CompID",employees.getCompID());
        contentValues.put("EmpID",employees.getEmpID());
        contentValues.put("EmpFirst",employees.getEmpFirst());
        contentValues.put("EmpLast",employees.getEmpLast());
        sqLiteDatabase.insert(TAB_Employees,null,contentValues);
        sqLiteDatabase.close();
    }

    public int getEmployeeCnt() {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from Employees";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;

    }

    public void InsertRuleComponents(RuleComponents ruleComponents) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("RuleID",ruleComponents.getRuleID());
        contentValues.put("CompTypeID",ruleComponents.getCompTypeID());
        contentValues.put("CompID",ruleComponents.getCompID());
        contentValues.put("RuleCompID",ruleComponents.getRuleCompID());
        sqLiteDatabase.insert(TAB_RuleComponents,null,contentValues);
        sqLiteDatabase.close();
    }

    public int getRuleComponentsCnt(int RuleId) {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from RuleComponents Where RuleID = "+RuleId;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;

    }

    public void Insertcomponents(Components components) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CompID",components.getCompID());
        contentValues.put("CompName",components.getCompName());
        contentValues.put("CompAbbr",components.getCompAbbr());
        contentValues.put("CompOrder",components.getCompOrder());
        sqLiteDatabase.insert(TAB_Components,null,contentValues);
        sqLiteDatabase.close();
    }

    public int getComponentsCnt() {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from Components";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;

    }

    public void InsertRuleComponentTypes(RuleComponentTypes ruleComponentTypes) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("RuleID",ruleComponentTypes.getRuleID());
        contentValues.put("CompTypeID",ruleComponentTypes.getCompTypeID());
        contentValues.put("RuleCompTypeID",ruleComponentTypes.getRuleCompTypeID());
        sqLiteDatabase.insert(TAB_RuleComponentTypes,null,contentValues);
        sqLiteDatabase.close();
    }

    public int getRuleComponentTypesCnt(int RuleId) {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from RuleComponentTypes Where RuleID = "+RuleId;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;

    }

    public void InsertStreamTypes(StreamTypes streamTypes) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("StrTypeID",streamTypes.getStrTypeID());
        contentValues.put("StrTypeName",streamTypes.getStrTypeName());
        contentValues.put("StrTypeAbbr",streamTypes.getStrTypeAbbr());
        sqLiteDatabase.insert(TAB_StreamTypes,null,contentValues);
        sqLiteDatabase.close();
    }

    public int getStreamTypesCnt() {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from StreamTypes";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;
    }

    public void InsertcomponentTypes(ComponentTypes componentTypes) {

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CompTypeID",componentTypes.getCompTypeID());
        contentValues.put("CompTypeName",componentTypes.getCompTypeName());
        contentValues.put("CompAbbr",componentTypes.getCompAbbr());
        sqLiteDatabase.insert(TAB_ComponentTypes,null,contentValues);
        sqLiteDatabase.close();


    }

    public int getComponentTypesCnt() {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from ComponentTypes";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;
    }
    public int getRuleComponentCnt() {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from RuleComponents";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;
    }


    public void Insertstreams(Streams streams) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("StrTypeID",streams.getStrTypeID());
            contentValues.put("StrID",streams.getStrID());
            contentValues.put("StrName",streams.getStrName());
            contentValues.put("StrAbbr",streams.getStrAbbr());
            sqLiteDatabase.insert(TAB_Streams,null,contentValues);
            sqLiteDatabase.close();

    }

    public int StreamsCnt() {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from Streams";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;
    }

    public void InsertInventoryRules(InventoryRules inventoryRules) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("RouteID",inventoryRules.getRouteID());
        contentValues.put("InvID",inventoryRules.getInvID());
        contentValues.put("RuleID",inventoryRules.getRuleID());
        contentValues.put("Comp_Mon_Cat_id",inventoryRules.getComp_mon_cat_id());
        sqLiteDatabase.insert(TAB_InventoryRules,null,contentValues);
        sqLiteDatabase.close();
    }


    public void InsertPermits(Permits permits){
        SQLiteDatabase sqLiteDatabase =getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FacID",permits.getFacID());
        contentValues.put("PermID",permits.getPermID());
        sqLiteDatabase.insert(TAB_Permits,null,contentValues);
        sqLiteDatabase.close();
    }

    public int getPermitsCnt(int FacId) {
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select COUNT(*) from Permits where FacID = "+FacId;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        database.close();
        return Count;
    }

    public void InsertPermitInventory(PermitInventory permitInventory){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("PermID",permitInventory.getPermID());
        contentValues.put("InvID",permitInventory.getInvID());
        contentValues.put("RouteID",permitInventory.getRouteID());
        sqLiteDatabase.insert(TAB_PermitInventory,null,contentValues);
        sqLiteDatabase.close();
    }
    public void InsertPermitRates(PermitRates permitRates){
        SQLiteDatabase sqLiteDatabase =getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("PermID",permitRates.getPermID());
        contentValues.put("LeakTypeID",permitRates.getLeakTypeID());
        contentValues.put("PermRateID",permitRates.getPermRateID());
        contentValues.put("PermRateStart",permitRates.getPermRateStart());
        contentValues.put("PermRateTime",permitRates.getPermRateTime());
        sqLiteDatabase.insert(TAB_PermitRates,null,contentValues);
        sqLiteDatabase.close();
    }

    //Prathamesh
    //If Route Manual Updatea all components Inspected.
    public void setAllCmpAsInspected(int routeid,int subid){
        SQLiteDatabase database=getWritableDatabase();
        String query="select InspectionDate from RoutesConfig where RouteID="+routeid;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        String date = cursor.getString(cursor.getColumnIndex("InspectionDate"));
        String time=new SimpleDateFormat(" hh:mm:ss a").format(new Date());
        System.out.println("DateTImeStamp ::"+date+time);
        cursor.close();
        ContentValues Inventory=new ContentValues();
        Inventory.put("Inspected",1);
        Inventory.put(COL_INV_TimeStamp, date+time);

        ContentValues SubAreas=new ContentValues();
        SubAreas.put("Inspected",1);
        SubAreas.put(COL_INV_TimeStamp, date+time);
        SubAreas.put("BackgroundEntered",1);

        database.update(TAB_Inventory,Inventory,"RouteID="+routeid+" AND SubID="+subid+" AND Inspected="+"'"+false+"'",null);
        database.update(TAB_SubAreas,SubAreas,"RouteID="+routeid+" AND SubID= "+subid+" AND Inspected="+0,null);
        database.close();
    }

    public  void markAllCompInspected(int RouteID){
        SQLiteDatabase database =getWritableDatabase();
        /*ContentValues contentValues=new ContentValues();
        contentValues.put("Inspected",1);
        ContentValues contentValues2=new ContentValues();
        contentValues2.put("Inspected",1);
        contentValues.put(COL_INV_TimeStamp, new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new Date()));
        ContentValues contentValues1=new ContentValues();
        contentValues1.put("BackgroundEntered","'"+true+"'");
        database.update(TAB_Inventory,contentValues,"RouteID = "+RouteID+" AND Inspected="+"'"+false+"'",null);
        database.update(TAB_SubAreas,contentValues,"RouteID = "+RouteID+" AND Inspected="+"'"+false+"'",null);
        database.update(TAB_RoutesConfig,contentValues2,"RouteID = "+RouteID+" AND Inspected="+"'"+false+"'",null);
        database.update(TAB_SubAreas,contentValues1,"RouteID = "+RouteID+" AND Inspected="+"'"+false+"'",null);
        database.close();*/
        String query="select InspectionDate from RoutesConfig where RouteID="+RouteID;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        String date = cursor.getString(cursor.getColumnIndex("InspectionDate"));
        String time=new SimpleDateFormat(" hh:mm:ss a").format(new Date());
        System.out.println("DateTImeStamp ::"+date+time);
        cursor.close();
        ContentValues inventory = new ContentValues();
        inventory.put("Inspected",1);
        inventory.put(COL_INV_TimeStamp,date+time);
        database.update(TAB_Inventory,inventory,"RouteID = "+RouteID+" AND Inspected="+"'"+false+"'",null);

        ContentValues subarea=new ContentValues();
        subarea.put("Inspected",1);
        subarea.put("BackgroundEntered",1);
        database.update(TAB_SubAreas,subarea,"RouteID = "+RouteID+" AND Inspected="+0,null);

        ContentValues routesconfig=new ContentValues();
        routesconfig.put("Inspected",1);
        routesconfig.put("BackgroundEntered",1);
        database.update(TAB_RoutesConfig,routesconfig,"RouteID = "+RouteID,null);
        database.close();
    }

    //Update Sub Areas Inspected Flag
    public void UpdateSubAreaInspected(int routeId,int subId){
        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("Inspected",true);
        database.update(TAB_SubAreas,contentValues,"RouteID="+routeId+ " AND SubID = "+subId,null);
        database.close();
    }

    //Update RouteConfig Inspected Flag
    public void UpdateRouteConfigInspected(int RouteId){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("Inspected",true);
        database.update(TAB_RoutesConfig,contentValues,"RouteID = "+RouteId,null);
        database.close();
    }
    //Get Count OF Inspected In inventory
    public  int getInspectedCount(int routeId,int subId){
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q =  "SELECT count(Inspected) from Inventory where RouteID="+routeId+" AND SubID = "+subId+" AND (Inspected='false' OR Inspected =0)";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("count(Inspected)"));
        }
        cursor.close();
        database.close();
        return Count;
    }
    //Get Count of route inspection in RouteConfig
    public int getRouteInspCount(int routeId){
        int count = 0;
        SQLiteDatabase database=getWritableDatabase();
        String q = "SELECT count(Inspected) as Insp from Inventory where RouteID="+routeId+" AND (Inspected='false' OR Inspected=0)";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            count=cursor.getInt(cursor.getColumnIndex("Insp"));

        }
        cursor.close();
        database.close();
        return count;
    }

    //Get Permit Rates.Permit rate Start and permit rate id.
    public Pair<Integer,Float> getPemitThresholdValue(int InvId,int leakTypeId){
        int PermRateID = 0;
        float PermRateStart = 0;
        SQLiteDatabase database=getWritableDatabase();
//        String q="SELECT PermRateID, PermRateStart FROM PermitRates WHERE InvID = "+InvId+" AND LeakTypeID = "+leakTypeId;
        String q="SELECT PermRateStart, PermRateTime FROM PermitRates WHERE PermID IN (SELECT Permits.PermID FROM Permits"+
                " INNER JOIN PermitInventory ON Permits.PermID = PermitInventory.PermID "+
                "WHERE InvID = " + InvId + " AND LeakTypeID = " + leakTypeId+ ")";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            PermRateID=cursor.getInt(cursor.getColumnIndex("PermRateID"));
            PermRateStart=cursor.getFloat(cursor.getColumnIndex("PermRateStart"));
        }
        cursor.close();
        database.close();
        return new Pair<>(PermRateID,PermRateStart);
    }
    //Check if inv has permit
    public  int getPermIdCount(int routeId,int invId,int permId){
        int Count=0;
        SQLiteDatabase database=getWritableDatabase();
        String q="SELECT PermID FROM PermitInventory WHERE RouteID ="+routeId+" AND InvID = "+invId;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Count=cursor.getInt(cursor.getColumnIndex("PermID"));
        }
        cursor.close();
        database.close();
        return Count;
    }
    //Get Comp Type Id , StrId, StrTypeId From Inventory
    public int[] getCompStrTypeIDs(int RouteId ,int SubId,int InvId){
        int[] IDs=new int[3];
        SQLiteDatabase database=getWritableDatabase();
        String q = "SELECT CompTypeID, StrID, StrTypeID FROM Inventory " +
                "WHERE RouteID = "+RouteId+" AND SubID = "+SubId+" AND InvID = "+InvId;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            IDs[0]=cursor.getInt(cursor.getColumnIndex("CompTypeID"));
            IDs[1]=cursor.getInt(cursor.getColumnIndex("StrID"));
            IDs[2]=cursor.getInt(cursor.getColumnIndex("StrTypeID"));
        }
        cursor.close();
        database.close();
        return IDs;
    }
    //Get PermRateId and PermRateTime from Permit Rates
    public int[] getPrIdPrTime(int permId,int leakTypeId){
        int[] IDTime=new int[2];
        SQLiteDatabase database=getWritableDatabase();
        String q="SELECT PermRateID, PermRateTime FROM PermitRates WHERE PermID = "+permId+" AND LeakTypeID = "+leakTypeId;
        Cursor cursor=database.rawQuery(q,null);
        while(cursor.moveToNext()){
            IDTime[0]=cursor.getInt(cursor.getColumnIndex("PermRateID"));
            IDTime[1]=cursor.getInt(cursor.getColumnIndex("PermRateTime"));

        }
        cursor.close();
        database.close();
        return IDTime;
    }
    //GEt LeakRateStart from LeakRates by StrId and StrTypeID
    public float getLeakRateStart(int RuleCompTypeId,int LeakTypeID,int StrId,int StrTypeId){
        float LeakRateStart = 0;
        SQLiteDatabase database=getWritableDatabase();
        //Limit 1 is used to get the leakratestart minimum value.
        String q="SELECT LeakRateStart  FROM LeakRates WHERE RuleCompTypeID  = "+RuleCompTypeId+" AND LeakTypeID = "+LeakTypeID+" AND  StrTypeID = "+StrTypeId+ " AND StrID = "+StrId+" ORDER BY LeakRateStart ASC LIMIT 1";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            LeakRateStart=cursor.getFloat(cursor.getColumnIndex("LeakRateStart"));

        }
        cursor.close();
        database.close();
        return LeakRateStart;
    }
    //Get LeakRateStart from LeakRates By StrTypeID
    public float getLeakRateStartStr(int RuleCompTypeId,int LeakTypeID,int StrTypeId){
        float LeakRateStart = 0;
        SQLiteDatabase database=getWritableDatabase();
        //Limit 1 is used to get the leakratestart minimum value.
        String q="SELECT LeakRateStart  FROM LeakRates WHERE RuleCompTypeID  = "+RuleCompTypeId+" AND LeakTypeID = "+LeakTypeID+" AND  StrTypeID = "+StrTypeId+ " ORDER BY LeakRateStart ASC LIMIT 1";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            LeakRateStart=cursor.getFloat(cursor.getColumnIndex("LeakRateStart"));

        }
        cursor.close();
        database.close();
        return LeakRateStart;
    }
    //get Max LeakID
    public int getmaxLeakID(){
        int maxid=0;
        SQLiteDatabase database = getWritableDatabase();
        String q= "Select MAX(LeakID) from Leaks";
        Cursor cursor=database.rawQuery(q,null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do {
                maxid = cursor.getInt(cursor.getColumnIndex("MAX(LeakID)"));
            }while(cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return maxid;
    }

    //get LeakRate from LeakRates
    public int getleakRateTime(int RuleCompTypeId,int LeakTypeId,int StrId,int StrTypeId,float Reading){
        int LeakTime=0;
        SQLiteDatabase database=getWritableDatabase();
        String q= "SELECT LeakRateTime FROM LeakRates WHERE RuleCompTypeID  = "+RuleCompTypeId+" AND LeakTypeID = "+LeakTypeId+" AND StrTypeID ="+StrTypeId+" AND "+Reading+" BETWEEN LeakRateStart AND LeakRateEnd";
        Cursor cursor=database.rawQuery(q,null);
        while(cursor.moveToNext()){
            LeakTime=cursor.getInt(cursor.getColumnIndex("LeakRateTime"));
        }
        cursor.close();
        database.close();
        return LeakTime;
    }
    //RESET INSPECTION DATE
    public void resetInspDate(int routeId){
        SQLiteDatabase database =getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("InspectionDate","");
        database.update(TAB_RoutesConfig,contentValues,"RouteID = "+routeId,null);
        database.close();
    }
    //Delete Leaks If Partial
    public void partialDeleteLeaks(int routeId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_Leaks,"InvID IN (Select InvID From Inventory Where RouteID = "+routeId+" AND Inspected = 1)",null);
        database.close();
    }
    //Delete LeakRepairs If Partial
    public void partialDeleteLeakRepairs(int routeId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_LeakRepairs,"LeakID In (Select LeakId from Leaks Where InvID IN (Select InvID From Inventory Where RouteID ="+routeId+" AND Inspected=1))",null);
        database.close();
    }
    //Delete Inventory If Partial
    public void  partialDeleteInventory(int routeId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_Inventory,"RouteID = "+routeId+" AND Inspected = 1",null);
        database.close();
    }
    //Delete Sub areas If partial
    public void partialDeleteSubArea(int routeId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_SubAreas,"RouteID = "+routeId +" AND Inspected = 1",null);
        database.close();
    }
    //Delete Leaks After Uploading.
    public  void deleteLeaks(int routeId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_Leaks,"InvID IN (Select InvID From Inventory Where RouteID = "+routeId+")",null);
        database.close();
    }
    //Delete Inventory after Upload.
    public  void deleteInventory(int routeId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_Inventory,"RouteID = "+routeId,null);
        database.close();
    }
    //Delete SubArea After Upload.
    public  void deleteSubAreas(int routeId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_SubAreas,"RouteID = "+routeId,null);
        database.close();
    }
    //Delete RouteConfig After Upload.
    public  void deleteRoutesConfig(int routeId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_RoutesConfig,"RouteID = "+routeId,null);
        database.close();
    }
    //Delete LeakRepair After Upload.
    public void deleteLeakRepair(int routeId){
        SQLiteDatabase database= getWritableDatabase();
        database.delete(TAB_LeakRepairs,"LeakID In (Select LeakId from Leaks Where InvID IN (Select InvID From Inventory Where RouteID ="+ routeId+"))",null);
        database.close();

    }
    //Delte Inventory Rule After Upload
    public void deleteInventoryRule(int routeId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_InventoryRules,"RouteID = "+routeId,null);
        database.close();
    }

    //Delte Inventory Rule After Upload
    public void deleteRuleComponents(int routeId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_RuleComponents,"RuleID In(Select RouteID from InventoryRules where   "+routeId,null);
        database.close();
    }

    //Get RuleCompTypeId from RuleComponentType
    public int getRuleCompTypeId(int compTypeId,int ruleId){
        int RuleCompTypeId = 0;
        SQLiteDatabase database=getWritableDatabase();
        String q="SELECT RuleCompTypeID FROM RuleComponentTypes " +
                "WHERE RuleID = "+ruleId+" AND CompTypeID ="+compTypeId+" LIMIT 1";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            RuleCompTypeId=cursor.getInt(cursor.getColumnIndex("RuleCompTypeID"));
        }
        cursor.close();
        database.close();
        return RuleCompTypeId;
    }

    //For Deleting LeakImage
    public String LeakImagePath(int InvId){
        String Path=null;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select LeakImage From Leaks Where InvID = "+InvId;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Path=cursor.getString(cursor.getColumnIndex("LeakImage"));
        }
        cursor.close();
        database.close();
        return Path;
    }

    //Get all leak image paths
    public List<String> getAllLeakImagePath(int routeId){
        List<String> ImagePathList = new ArrayList<>();
        String path;
        SQLiteDatabase database=getWritableDatabase();
        String q="Select LeakImage from Leaks where InvID IN (Select InvID from Inventory where RouteID = "+routeId+")";
        Cursor cursor=database.rawQuery(q, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do {
                path=cursor.getString(cursor.getColumnIndex("LeakImage"));
                ImagePathList.add(path);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return ImagePathList;
    }
    //Delete Previous LeakRepair
    public void deleteReInspectLeakRepair(int InvId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_LeakRepairs,"LeakID In (Select LeakId from Leaks Where InvID ="+InvId+")",null);
        database.close();
    }

    //Delete Previous Leaks
    public void deleteReInspectLeak(int InvId){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(TAB_Leaks,"InvID ="+InvId,null);
        database.close();
    }

    /*Check If FacID is having Permits*/
    public int[] getFacId(int routeId){
        int[] Ids=new int[2];
        int FacId=0;
        int RuleId=0;
        SQLiteDatabase database=getWritableDatabase();
        String q="SELECT FacID, RuleID FROM RoutesConfig WHERE RouteID ="+routeId;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Ids[0]=cursor.getInt(cursor.getColumnIndex("FacID"));
            Ids[1]=cursor.getInt(cursor.getColumnIndex("RuleID"));
        }
        cursor.close();
        database.close();
        return Ids;
    }

    public int CheckPermId(int facId){
        int PermID = 0;
        SQLiteDatabase database = getWritableDatabase();
        String q = "SELECT PermID FROM Permits WHERE FacID ="+facId;
        Cursor cursor =database.rawQuery(q,null);
        while(cursor.moveToNext()){
            PermID=cursor.getInt(cursor.getColumnIndex("PermID"));
        }
        cursor.close();
        database.close();
        return PermID;
    }

    public List<LeakPathTypes> getLeakPathType(int CompId){
        String LeakPathTypeName;
        int LeakPathTypeId;
        List<LeakPathTypes> LeakPathType = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        String q = "SELECT Distinct LeakPathTypes.LeakPathTypeID, LeakPathTypes.LeakPathTypeName " +
                "FROM LeakPathTypes INNER JOIN LeakPaths ON LeakPaths.LeakPathTypeID = LeakPathTypes.LeakPathTypeID " +
                "Where LeakPaths.CompID ="+CompId+" Order By LeakPathTypeName";
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            LeakPathTypeName=cursor.getString(cursor.getColumnIndex("LeakPathTypeName"));
            LeakPathTypeId=cursor.getInt(cursor.getColumnIndex("LeakPathTypeID"));

            LeakPathTypes leakPathTypes=new LeakPathTypes();
            leakPathTypes.setLeakPathTypeName(LeakPathTypeName);
            leakPathTypes.setLeakPathTypeID(LeakPathTypeId);
            LeakPathType.add(leakPathTypes);
        }
        cursor.close();
        database.close();
        return LeakPathType;

    }

    public List<EmployeesPojo> getEmployeeName(){
        String FirstName,LastName,Add;
        int compId,empId;

        List<EmployeesPojo> EmployeeName=new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        String q="SELECT EmpID,EmpFirst,EmpLast FROM Employees ORDER BY EmpFirst ASC";
        Cursor cursor = database.rawQuery(q,null);
        while (cursor.moveToNext()){
            FirstName=cursor.getString(cursor.getColumnIndex("EmpFirst"));
            LastName=cursor.getString(cursor.getColumnIndex("EmpLast"));
            empId = cursor.getInt(cursor.getColumnIndex("EmpID"));
            EmployeesPojo employeesPojo = new EmployeesPojo(empId,FirstName,LastName);
            EmployeeName.add(employeesPojo);
        }
        cursor.close();
        database.close();
        return EmployeeName;
    }

    public  List<TVAPojo> getTVA(){
        String TVAName;
        int TVAID;
        List<TVAPojo> tvanames =new ArrayList<>();
        SQLiteDatabase database =getWritableDatabase();
        String q = "SELECT TVAID,TVAName FROM TVA ORDER BY TVAName ASC";
        Cursor cursor = database.rawQuery(q,null);
        while (cursor.moveToNext()){
            TVAName=cursor.getString(cursor.getColumnIndex("TVAName"));
            TVAID=cursor.getInt(cursor.getColumnIndex("TVAID"));
            TVAPojo tvaPojo =new TVAPojo(TVAID,TVAName);
            tvanames.add(tvaPojo);
        }
        cursor.close();
        database.close();
        return tvanames;
    }

    public boolean isSubAreaInspected(int RouteID,int Subid){
        SQLiteDatabase database=getWritableDatabase();
        String query="Select Inspected from SubAreas where RouteID= "+RouteID+" AND SubID = "+Subid;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        int resutl=cursor.getInt(cursor.getColumnIndex("Inspected"));
        if(resutl==1){
            cursor.close();
            return true;
        }else {
            cursor.close();
            return false;
        }
    }

    public void updatedata(){

        //update Into tables to check Inspected and UnInspected.
    }

    public int getmaxLeakRepairID(){
        int maxid=0;
        SQLiteDatabase database = getWritableDatabase();
        String q= "Select MAX(LeakRepairID) from LeakRepairs";
        Cursor cursor=database.rawQuery(q,null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do {
                maxid = cursor.getInt(cursor.getColumnIndex("MAX(LeakRepairID)"));
            }while(cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return maxid;
    }
    public void InsertLeakRepair(int leakRepairId,int leakId,int empId,int leakRepairTypeId,float leakRepairRate,String leakRepairDate){

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LeakRepairID",leakRepairId);
        contentValues.put("LeakID",leakId);
        contentValues.put("EmpID",empId);
        contentValues.put("LeakRepairTypeID",leakRepairTypeId);
        contentValues.put("LeakRepairRate",leakRepairRate);
        contentValues.put("LeakRepairDate",leakRepairDate);
        sqLiteDatabase.insert(TAB_LeakRepairs,null,contentValues);
        sqLiteDatabase.close();

    }
    public String getRouteName(int RouteId){
        String RouteName = "";
        SQLiteDatabase database=getWritableDatabase();
        String q = "Select RouteName from RoutesConfig where RouteID = "+RouteId;
        Cursor cursor=database.rawQuery(q,null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do {
                RouteName = cursor.getString(cursor.getColumnIndex("RouteName"));
            }while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return RouteName;
    }

    public String getSubName(int subid){
        String SubName="";
        SQLiteDatabase database = getWritableDatabase();
        String q= "Select SubName from SubAreas where SubID = "+subid;
        Cursor cursor = database.rawQuery(q,null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do {
                SubName=cursor.getString(cursor.getColumnIndex("SubName"));
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return SubName;
    }


    //izhar
    public void UpdateLeaks(int InvId,int LeakPathTypeId,int LeakTypeId,boolean leakBit1,boolean leakBit5,float leakRate,float leakTime,String leakDate,String image){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("InvID",InvId);
        contentValues.put("LeakPathTypeID",LeakPathTypeId);
        contentValues.put("LeakTypeID",LeakTypeId);
        contentValues.put("LeakBit1",leakBit1);
        contentValues.put("LeakBit5",leakBit5);
        contentValues.put("LeakRate",leakRate);
        contentValues.put("LeakTime",leakTime);
        contentValues.put("LeakDate", leakDate);
        contentValues.put("LeakImage",image);
        database.update(TAB_Leaks,contentValues,"InvID = " + InvId,null);
        database.close();
    }
    public int countLeaks(int routeID, int subID, int invID){
        int noOfLeaks = 0;
        SQLiteDatabase database = getWritableDatabase();
        String countQuery = "SELECT COUNT(Leaks.InvID) AS invCount FROM Leaks INNER JOIN Inventory ON Leaks.InvID = Inventory.InvID " +
                "WHERE Inventory.RouteID = " + routeID + " AND Inventory.SubID = " + subID + " AND Inventory.InvID = " + invID;
        Cursor cursorCount = database.rawQuery(countQuery,null);
        cursorCount.moveToFirst();
        if (!cursorCount.isAfterLast()){
            do {
                noOfLeaks = cursorCount.getInt(cursorCount.getColumnIndex("invCount"));
            }
            while (cursorCount.moveToNext());
        }
        database.close();
        cursorCount.close();
        return noOfLeaks;
    }
    public void InsertLeaks(int LeakId,int InvId,String InvTag,int LeakPathTypeId,int LeakTypeId,int compId, Boolean leakBit1, Boolean leakBit5,float LeakFloat1,Float leakRate,float leakTime,String leakDate, Float lat, Float lng, Boolean status,String image) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LeakID",LeakId);
        contentValues.put("InvID",InvId);
        contentValues.put("InvTag",InvTag);
        contentValues.put("LeakPathTypeID",LeakPathTypeId);
        contentValues.put("LeakTypeID",LeakTypeId);
        contentValues.put("CompID",compId);
        contentValues.put("LeakBit1",leakBit1);
        contentValues.put("LeakBit2",false);
        contentValues.put("LeakBit4",false);
        contentValues.put("LeakBit5",leakBit5);
        contentValues.put("LeakFloat1",LeakFloat1);
        contentValues.put("LeakRate",leakRate);
        contentValues.put("LeakTime",leakTime);
        contentValues.put("LeakDate", leakDate);
        contentValues.put("LeakLat",lat);
        contentValues.put("LeakLng",lng);
        contentValues.put("Status",status);
        contentValues.put("LeakImage",image);
        database.insert(TAB_Leaks,null,contentValues);
        database.close();
        /*Changes*/
    }

    public List<ReasonSkipped> getReasonSkipped(){
        String ReasonSkipped;
        int ReasonSkippedID;
        List<ReasonSkipped> ReasonSkippedlistPojos=new ArrayList<>();
        SQLiteDatabase dbReading = getWritableDatabase();

        String queryReading = "SELECT ReasonSkippedID, ReasonSkipped FROM ReasonSkipped ORDER BY ReasonSkipped ASC";

        Cursor resultReasonSkipped = dbReading.rawQuery(queryReading,null);

        resultReasonSkipped.moveToFirst();
        if (!resultReasonSkipped.isAfterLast()) {
            do {
                ReasonSkippedID = Integer.parseInt(resultReasonSkipped.getString(resultReasonSkipped.getColumnIndex("ReasonSkippedID")));
                ReasonSkipped = resultReasonSkipped.getString(resultReasonSkipped.getColumnIndex("ReasonSkipped"));

                ReasonSkipped reasonSkipped = new ReasonSkipped(ReasonSkippedID, ReasonSkipped);
                ReasonSkippedlistPojos.add(reasonSkipped);
            }
            while(resultReasonSkipped.moveToNext());
        }
        else{
            //No records found in database
        }

        resultReasonSkipped.close();
        dbReading.close();
        return ReasonSkippedlistPojos;
    }

    private String getAreaName(int subId) {
        String AreaName="";
        SQLiteDatabase database = getWritableDatabase();
        String q= "Select AreaName from SubAreas where SubID = "+subId;
        Cursor cursor = database.rawQuery(q,null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do {
                AreaName=cursor.getString(cursor.getColumnIndex("AreaName"));
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return AreaName;
    }

    public  String getReason(int skippedId){
        SQLiteDatabase database=getWritableDatabase();
        String Reason = null;
        String q="Select ReasonSkipped from ReasonSkipped where ReasonSkippedID = "+skippedId;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            Reason=cursor.getString(cursor.getColumnIndex("ReasonSkipped"));
        }
        cursor.close();
        database.close();
        return Reason;
    }
    public List<ComponentsListPojo> getCompReadingValues(int routeId,int SubId,int InvId){
        String LOCATION,SubName,AreaName;
        String NAME,TAG;
        boolean Critical;
        int compId,subId,invId,ReasonID;
        float SIZE,BackgroundReading,Reading;
        String INSPECTED;
        List<ComponentsListPojo> readinglistPojos=new ArrayList<>();
        SQLiteDatabase dbReading = getWritableDatabase();

        String queryReading = "SELECT Inventory.InvID AS InvID, Inventory.CompID AS CompID,Inventory.SubID as SubID, Components.CompName AS CompName , Inventory.InvTag AS InvTag , " +
                "Inventory.Inspected AS Inspected,Inventory.ReasonSkippedID as ReasonID,Inventory.Reading AS Reading,Inventory.Background as Background, Inventory.InvSize as InvSize, Inventory.InvLocation AS InvLocation ,Inventory.Critical as Critical " +
                "FROM Inventory INNER JOIN Components ON Inventory.CompID = Components.CompID " +
                "WHERE Inventory.SubID = SubID AND Inventory.RouteID = "+ routeId +" AND Inventory.SubID = "+SubId+" AND Inventory.InvID= "+ InvId+" LIMIT 1";

        Cursor resultReading = dbReading.rawQuery(queryReading,null);

        resultReading.moveToFirst();
        if (!resultReading.isAfterLast())
        {
            do{
                invId=resultReading.getInt(resultReading.getColumnIndex("InvID"));
                compId = Integer.parseInt(resultReading.getString(resultReading.getColumnIndex("CompID")));
                subId= resultReading.getInt(resultReading.getColumnIndex("SubID"));
                BackgroundReading = resultReading.getFloat(resultReading.getColumnIndex("Background"));
                NAME = resultReading.getString(resultReading.getColumnIndex("CompName"));
                TAG = resultReading.getString(resultReading.getColumnIndex("InvTag"));
                ReasonID=resultReading.getInt(resultReading.getColumnIndex("ReasonID"));
                INSPECTED = resultReading.getString(resultReading.getColumnIndex("Inspected"));
                SIZE = resultReading.getFloat(resultReading.getColumnIndex("InvSize"));
                LOCATION = resultReading.getString(resultReading.getColumnIndex("InvLocation"));
                Critical = Boolean.parseBoolean(resultReading.getString(resultReading.getColumnIndex("Critical")));
                SubName = getSubName(subId);
                AreaName = getAreaName(subId);
                Reading = resultReading.getFloat(resultReading.getColumnIndex("Reading"));

                ComponentsListPojo ReadingcomponentsListPojo = new ComponentsListPojo(compId,subId, NAME, TAG, INSPECTED, SIZE, LOCATION);
                ReadingcomponentsListPojo.setCritical(Critical);
                ReadingcomponentsListPojo.setBackread(BackgroundReading);
                ReadingcomponentsListPojo.setInvID(InvId);
                ReadingcomponentsListPojo.setAreaName(AreaName);
                ReadingcomponentsListPojo.setSubName(SubName);
                ReadingcomponentsListPojo.setReading(Reading);
                ReadingcomponentsListPojo.setSkippedID(ReasonID);
                readinglistPojos.add(ReadingcomponentsListPojo);
            }
            while(resultReading.moveToNext());
        }
        else{
            //No records found in database
        }
        resultReading.close();
        dbReading.close();
        return readinglistPojos;
    }

    //Used to get next component.
    public List<ComponentsListPojo> NextCompReadingValues(int routeId,int SubId,int InvId,int InvOrder){
        String LOCATION,SubName,AreaName;
        String NAME,TAG;
        boolean Critical;
        int compId,subId,invId,ReasonID;
        float SIZE,BackgroundReading,Reading;
        String INSPECTED;
        List<ComponentsListPojo> readinglistPojos=new ArrayList<>();
        SQLiteDatabase dbReading = getWritableDatabase();

        String queryReading = "SELECT Inventory.InvID AS InvID, Inventory.CompID AS CompID,Inventory.SubID as SubID, Components.CompName AS CompName , Inventory.InvTag AS InvTag , " +
                "Inventory.Inspected AS Inspected,Inventory.ReasonSkippedID as ReasonID,Inventory.Reading AS Reading,Inventory.Background as Background, Inventory.InvSize as InvSize, Inventory.InvLocation AS InvLocation ,Inventory.Critical as Critical " +
                "FROM Inventory INNER JOIN Components ON Inventory.CompID = Components.CompID " +
                "WHERE Inventory.SubID = SubID AND Inventory.RouteID = "+ routeId +" AND Inventory.SubID = "+SubId+" AND Inventory.InvOrder > "+InvOrder+" ORDER BY InvOrder LIMIT 1";

        Cursor resultReading = dbReading.rawQuery(queryReading,null);

        resultReading.moveToFirst();
        if (!resultReading.isAfterLast())
        {
            do{
                invId=resultReading.getInt(resultReading.getColumnIndex("InvID"));
                compId = Integer.parseInt(resultReading.getString(resultReading.getColumnIndex("CompID")));
                subId= resultReading.getInt(resultReading.getColumnIndex("SubID"));
                BackgroundReading = resultReading.getFloat(resultReading.getColumnIndex("Background"));
                NAME = resultReading.getString(resultReading.getColumnIndex("CompName"));
                TAG = resultReading.getString(resultReading.getColumnIndex("InvTag"));
                ReasonID=resultReading.getInt(resultReading.getColumnIndex("ReasonID"));
                INSPECTED = resultReading.getString(resultReading.getColumnIndex("Inspected"));
                SIZE = resultReading.getFloat(resultReading.getColumnIndex("InvSize"));
                LOCATION = resultReading.getString(resultReading.getColumnIndex("InvLocation"));
                Critical = Boolean.parseBoolean(resultReading.getString(resultReading.getColumnIndex("Critical")));
                SubName = getSubName(subId);
                AreaName = getAreaName(subId);
                Reading = resultReading.getFloat(resultReading.getColumnIndex("Reading"));

                ComponentsListPojo ReadingcomponentsListPojo = new ComponentsListPojo(compId,subId, NAME, TAG, INSPECTED, SIZE, LOCATION);
                ReadingcomponentsListPojo.setCritical(Critical);
                ReadingcomponentsListPojo.setBackread(BackgroundReading);
                ReadingcomponentsListPojo.setInvID(invId);
                ReadingcomponentsListPojo.setSubName(SubName);
                ReadingcomponentsListPojo.setAreaName(AreaName);
                ReadingcomponentsListPojo.setReading(Reading);
                ReadingcomponentsListPojo.setSkippedID(ReasonID);
                readinglistPojos.add(ReadingcomponentsListPojo);
            }
            while(resultReading.moveToNext());
        }
        else{
            //No records found in database
        }
        resultReading.close();
        dbReading.close();
        return readinglistPojos;
    }

    //Used to get previous component
    public List<ComponentsListPojo> PreviousCompReadingValues(int routeId,int SubId,int InvId,int InvOrder){
        String LOCATION,SubName,AreaName;
        String NAME,TAG;
        boolean Critical;
        int compId,subId,invId,ReasonID;
        float SIZE,BackgroundReading,Reading;
        String INSPECTED;
        List<ComponentsListPojo> readinglistPojos=new ArrayList<>();
        SQLiteDatabase dbReading = getWritableDatabase();

        String queryReading = "SELECT Inventory.InvID AS InvID, Inventory.CompID AS CompID,Inventory.SubID as SubID, Components.CompName AS CompName , Inventory.InvTag AS InvTag , " +
                "Inventory.Inspected AS Inspected,Inventory.ReasonSkippedID as ReasonID,Inventory.Reading AS Reading,Inventory.Background as Background, Inventory.InvSize as InvSize, Inventory.InvLocation AS InvLocation ,Inventory.Critical as Critical " +
                "FROM Inventory INNER JOIN Components ON Inventory.CompID = Components.CompID " +
                "WHERE Inventory.SubID = SubID AND Inventory.RouteID = "+ routeId +" AND Inventory.SubID = "+SubId+" AND Inventory.InvOrder < "+InvOrder+" ORDER BY InvOrder DESC LIMIT 1";

        Cursor resultReading = dbReading.rawQuery(queryReading,null);

        resultReading.moveToFirst();
        if (!resultReading.isAfterLast())
        {
            do{
                invId=resultReading.getInt(resultReading.getColumnIndex("InvID"));
                compId = Integer.parseInt(resultReading.getString(resultReading.getColumnIndex("CompID")));
                subId= resultReading.getInt(resultReading.getColumnIndex("SubID"));
                BackgroundReading = resultReading.getFloat(resultReading.getColumnIndex("Background"));
                NAME = resultReading.getString(resultReading.getColumnIndex("CompName"));
                TAG = resultReading.getString(resultReading.getColumnIndex("InvTag"));
                ReasonID=resultReading.getInt(resultReading.getColumnIndex("ReasonID"));
                INSPECTED = resultReading.getString(resultReading.getColumnIndex("Inspected"));
                SIZE = resultReading.getFloat(resultReading.getColumnIndex("InvSize"));
                LOCATION = resultReading.getString(resultReading.getColumnIndex("InvLocation"));
                Critical = Boolean.parseBoolean(resultReading.getString(resultReading.getColumnIndex("Critical")));
                SubName = getSubName(subId);
                AreaName = getAreaName(subId);
                Reading = resultReading.getFloat(resultReading.getColumnIndex("Reading"));

                ComponentsListPojo ReadingcomponentsListPojo = new ComponentsListPojo(compId,subId, NAME, TAG, INSPECTED, SIZE, LOCATION);
                ReadingcomponentsListPojo.setCritical(Critical);
                ReadingcomponentsListPojo.setBackread(BackgroundReading);
                ReadingcomponentsListPojo.setInvID(invId);
                ReadingcomponentsListPojo.setSubName(SubName);
                ReadingcomponentsListPojo.setAreaName(AreaName);
                ReadingcomponentsListPojo.setReading(Reading);
                ReadingcomponentsListPojo.setSkippedID(ReasonID);
                readinglistPojos.add(ReadingcomponentsListPojo);
            }
            while(resultReading.moveToNext());
        }
        else{
            //No records found in database
        }
        resultReading.close();
        dbReading.close();
        return readinglistPojos;
    }

    public int getInvOrder(int RouteId,int SubId,int InvId){
        int InvOrder = 0;
        SQLiteDatabase database=getWritableDatabase();
        String q="SELECT InvOrder from Inventory WHERE RouteID = "+RouteId+" AND SubID = "+SubId+" AND InvID = "+InvId;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            InvOrder=cursor.getInt(cursor.getColumnIndex("InvOrder"));
        }
        cursor.close();
        database.close();
        return InvOrder;
    }
    public int getInvIdNext(int RouteId,int SubId,int invOrder){
        int InvID=0;
        SQLiteDatabase database=getWritableDatabase();
        String q ="Select InvID from Inventory where RouteID = "+RouteId + " AND SubID = " +SubId +" AND InvOrder ="+invOrder;
        Cursor cursor=database.rawQuery(q,null);
        while(cursor.moveToNext()){
            InvID=cursor.getInt(cursor.getColumnIndex("InvID"));
        }
        cursor.close();
        database.close();
        return InvID;
    }
    public int getLastInvOrder(int RouteId,int Subid){
        int LastOrder=0;
        SQLiteDatabase database = getWritableDatabase();
        String q = "Select MAX(InvOrder) from Inventory where RouteID = "+RouteId + " AND SubID = "+Subid;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            LastOrder=cursor.getInt(cursor.getColumnIndex("MAX(InvOrder)"));
        }
        cursor.close();
        database.close();
        return LastOrder;
    }
    public int getFirstOrder(int RouteId,int subId){
        int firstorder=1;
        SQLiteDatabase database=getWritableDatabase();
        String q= "Select min(InvOrder) from Inventory where RouteID = "+RouteId + " AND SubID = "+subId;
        Cursor cursor=database.rawQuery(q,null);
        while(cursor.moveToNext()){
            firstorder=cursor.getInt(cursor.getColumnIndex("min(InvOrder)"));
        }
        cursor.close();
        database.close();
        return firstorder;
    }


    public List<ShowLeaksPojo> viewAllLeaks(int routeID){
        String TagNo,Subarea,Service,Component,repairTypeName,RepairTypeName,RepairDate;
        String leakPathName,leakCritical,leakEssential,AreaName,LeakDate;
        float componentSize,leakRate,repairRate;
        int leakTypeID,InvID,CompID,EmpID,LeakID;
        String Path="\"\" as LeakRepairRate , \"--\" as LeakRepairTypeAbbr,";
        List<ShowLeaksPojo> showLeaksPojoList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT InvOrder as InvOrder, InvTag as InvTag, SubID as SubID, StrTypeID as StrTypeID, CompID as CompID, InvSize as InvSize, \n" +
                "                LeakRate as LeakRate, LeakRepairRate as LeakRepairRate, LeakRepairTypeAbbr as LeakRepairTypeAbbr,\n" +
                "                SubName as SubName, AreaName as AreaName, StrTypeName as StrTypeName, CompName as CompName, LeakPathTypeAbbr as LeakPathTypeAbbr, \n" +
                "                LeakTypeID as LeakTypeID, CRITICAL as CRITICAL,ESSENTIAL as ESSENTIAL,LeakImage as LeakImage,EmpID as EmpID,LeakRepairDate as LeakRepairDate,\n" +
                "\t\t\t\tLeakRepairTypeName as LeakRepairTypeName, InvID as InvID,LeakDate as LeakDate,LeakID as LeakID  \n" +
                "                FROM \n" +
                "                ( \n" +
                "                SELECT X.InvOrder, X.InvTag, X.SubID,  X.StrTypeID, X.CompID,   X.InvSize, \n" +
                "                X.LeakRate,\"--\" as LeakRepairRate , \"--\" as LeakRepairTypeAbbr, \n" +
                "                SubAreas.SubName as SubName, SubAreas.AreaName as AreaName, StreamTypes.StrTypeName as StrTypeName, Components.CompName as CompName, \n" +
                "                 X.LeakPathTypeAbbr as LeakPathTypeAbbr, X.LeakTypeID as LeakTypeID, X.CRITICAL as CRITICAL,X.ESSENTIAL as ESSENTIAL,X.LeakImage,\n" +
                "\t\t\t\t\"--\" as EmpID,\"--\" as LeakRepairDate, \"--\" as LeakRepairTypeName ,X.InvID,X.LeakDate,X.LeakID \n" +
                "                FROM \n" +
                "                ( \n" +
                "                SELECT A.InvOrder, A.InvTag, B.InvTag, A.SubID, A.CompID, A.InvSize, A.StrTypeID, B.LeakRate, B.LeakPathTypeID, B.LeakTypeID, \n" +
                "                B.LeakPathTypeAbbr, B.LeakImage,B.InvID,B.LeakDate,B.LeakID, \n" +
                "                CASE WHEN B.LeakBit1 = 1 THEN 'CRITICAL' \n" +
                "                END AS [CRITICAL], \n" +
                "                CASE WHEN B.LeakBit5 = 1 THEN 'ESSENTIAL' \n" +
                "                END AS [ESSENTIAL] \n" +
                "                FROM Inventory A INNER JOIN \n" +
                "                ( \n" +
                "                SELECT Leaks.LeakId, Leaks.InvTag, Leaks.LeakRate, Leaks.LeakPathTypeID, Leaks.LeakTypeID, \n" +
                "                Leaks.LeakBit1, Leaks.LeakBit5, LeakPathTypes.LeakPathTypeAbbr,Leaks.LeakImage,Leaks.InvID,Leaks.LeakDate,Leaks.LeakID \n" +
                "                FROM Leaks INNER JOIN LeakPathTypes ON Leaks.LeakPathTypeID = LeakPathTypes.LeakPathTypeID \n" +
                "                WHERE LeakId NOT IN \n" +
                "                (SELECT Leaks.LeakId FROM Leaks INNER JOIN LeakRepairs ON Leaks.LeakId = LeakRepairs.LeakId) \n" +
                "                AND Leaks.InvID IN (SELECT InvID FROM Inventory WHERE RouteID = "+routeID+" ) \n" +
                "                ) \n" +
                "                B ON B.InvTag = A.InvTag \n" +
                "                ) \n" +
                "                X INNER JOIN SubAreas ON X.SubID = SubAreas.SubID \n" +
                "                INNER JOIN StreamTypes ON X.StrTypeID = StreamTypes.StrTypeID \n" +
                "                INNER JOIN Components ON X.CompID = Components.CompID \n" +
                "                UNION \n" +
                "                SELECT X.InvOrder, X.InvTag as InvTag, X.SubID, X.StrTypeID, X.CompID, X.InvSize as InvSize, \n" +
                "                X.LeakRate as LeakRate, X.LeakRepairRate as LeakRepairRate, X.LeakRepairTypeAbbr as LeakRepairTypeAbbr, \n" +
                "                SubAreas.SubName as SubName, SubAreas.AreaName as AreaName, StreamTypes.StrTypeName as StrTypeName, Components.CompName as CompName, \n" +
                "                X.LeakPathTypeAbbr as LeakPathTypeAbbr,X.LeakTypeID as LeakTypeID, X.CRITICAL as CRITICAL,X.ESSENTIAL as ESSENTIAL,X.LeakImage,\n" +
                "\t\t\t\tX.EmpID,X.LeakRepairDate,X.LeakRepairTypeName,X.InvID,X.LeakDate,X.LeakID  \n" +
                "                FROM \n" +
                "                ( \n" +
                "                SELECT A.InvOrder, A.InvTag, A.SubID, A.StrTypeID, A.CompID, A.InvSize, \n" +
                "                B.LeakRate, B.LeakRepairRate, B.LeakRepairTypeAbbr,B.LeakPathTypeAbbr,B.LeakTypeID,B.CRITICAL,B.ESSENTIAL,B.LeakImage,B.EmpID,B.LeakRepairDate,\n" +
                "\t\t\t\tB.LeakRepairTypeName,B.InvID,B.LeakDate,B.LeakID  \n" +
                "                FROM Inventory A INNER JOIN \n" +
                "                ( \n" +
                "                SELECT Leaks.LeakID, Leaks.InvTag, Leaks.LeakRate,Leaks.LeakTypeID, \n" +
                "                LeakRepairs.LeakID, LeakRepairs.LeakRepairRate, LeakRepairs.LeakRepairTypeID, \n" +
                "                LeakRepairTypes.LeakRepairTypeID, LeakRepairTypes.LeakRepairTypeAbbr, \n" +
                "                LeakPathTypes.LeakPathTypeAbbr,Leaks.LeakImage, LeakRepairs.EmpID , LeakRepairs.LeakRepairDate , LeakRepairTypes.LeakRepairTypeName ,\n" +
                "\t\t\t\tLeaks.InvID,Leaks.LeakDate,Leaks.LeakID, \n" +
                "                CASE WHEN Leaks.LeakBit1 = 1 THEN 'CRITICAL' \n" +
                "                END AS [CRITICAL], \n" +
                "                CASE WHEN Leaks.LeakBit5 = 1 THEN 'ESSENTIAL' \n" +
                "                END AS [ESSENTIAL] \n" +
                "                FROM Leaks INNER JOIN LeakRepairs ON Leaks.LeakID = LeakRepairs.LeakID \n" +
                "                INNER JOIN LeakRepairTypes ON LeakRepairs.LeakRepairTypeID = LeakRepairTypes.LeakRepairTypeID \n" +
                "                INNER JOIN LeakPathTypes ON LeakPathTypes.LeakPathTypeID = Leaks.LeakPathTypeID \n" +
                "                ) \n" +
                "                B ON A.InvTag = B.InvTag \n" +
                "                WHERE A.RouteID = "+routeID +
                "                ) X INNER JOIN SubAreas ON X.SubID = SubAreas.SubID \n" +
                "                INNER JOIN StreamTypes ON X.StrTypeID = StreamTypes.StrTypeID \n" +
                "                INNER JOIN Components ON X.CompID = Components.CompID \n" +
                "                ) \n" +
                "                ORDER BY InvOrder ASC";

        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                TagNo = cursor.getString(cursor.getColumnIndex("InvTag"));
                Subarea = cursor.getString(cursor.getColumnIndex("SubName"));
                AreaName = cursor.getString(cursor.getColumnIndex("AreaName"));
                Service = cursor.getString(cursor.getColumnIndex("StrTypeName"));
                Component = cursor.getString(cursor.getColumnIndex("CompName"));
                repairTypeName = cursor.getString(cursor.getColumnIndex("LeakRepairTypeAbbr"));
                componentSize = cursor.getFloat(cursor.getColumnIndex("InvSize"));
                leakRate = cursor.getFloat(cursor.getColumnIndex("LeakRate"));
                repairRate = cursor.getFloat(cursor.getColumnIndex("LeakRepairRate"));
                leakPathName = cursor.getString(cursor.getColumnIndex("LeakPathTypeAbbr"));
                leakTypeID = cursor.getInt(cursor.getColumnIndex("LeakTypeID"));
                leakCritical = cursor.getString(cursor.getColumnIndex("CRITICAL"));
                leakEssential = cursor.getString(cursor.getColumnIndex("ESSENTIAL"));
                Path=cursor.getString(cursor.getColumnIndex("LeakImage"));
                InvID=cursor.getInt(cursor.getColumnIndex("InvID"));
                CompID=cursor.getInt(cursor.getColumnIndex("CompID"));
                EmpID=cursor.getInt(cursor.getColumnIndex("EmpID"));
                RepairTypeName=cursor.getString(cursor.getColumnIndex("LeakRepairTypeName"));
                RepairDate=cursor.getString(cursor.getColumnIndex("LeakRepairDate"));
                LeakDate=cursor.getString(cursor.getColumnIndex("LeakDate"));
                LeakID=cursor.getInt(cursor.getColumnIndex("LeakID"));
                ShowLeaksPojo showLeaksPojo = new ShowLeaksPojo(TagNo, Subarea, AreaName, Service, Component, repairTypeName, leakPathName, componentSize, leakRate, repairRate, leakTypeID, leakCritical, leakEssential);
                showLeaksPojo.setRouteID(routeID);
                showLeaksPojo.setPath(Path);
                showLeaksPojoList.add(showLeaksPojo);
                showLeaksPojo.setInvId(InvID);
                showLeaksPojo.setCompID(CompID);
                showLeaksPojo.setEmpID(EmpID);
                showLeaksPojo.setRepairDate(RepairDate);
                showLeaksPojo.setRepairTypeName(RepairTypeName);
                showLeaksPojo.setLeakDate(LeakDate);
                showLeaksPojo.setLeakID(LeakID);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return showLeaksPojoList;
    }

    public  void  UpdateReasonSkippedID(int RouteId,int SubId,int InvID, int ReasonSkippedID)  {
        SQLiteDatabase database=getWritableDatabase();
        String query="select InspectionDate from RoutesConfig where RouteID="+RouteId;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        String date = cursor.getString(cursor.getColumnIndex("InspectionDate"));
        String time=new SimpleDateFormat(" hh:mm:ss a").format(new Date());
        System.out.println("DateTImeStamp ::"+date+time);
        cursor.close();
        /*SimpleDateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy");
        Date Date= null;
        try {
            Date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        ContentValues contentValues=new ContentValues();
        contentValues.put("ReasonSkippedID",ReasonSkippedID);
        contentValues.put("Skipped",1);
        contentValues.put("Reading",0.0);
        contentValues.put("Inspected",true);
        contentValues.put("TimeStamp",date+time);
        database.update(TAB_Inventory,contentValues,"RouteID ="+RouteId+" AND SubID ="+SubId+" AND InvID="+InvID,null);
        database.close();
    }


    public  void UpdateCompReading(int routeId ,int subId,int InvId, float Reading,int SkippedId){
        SQLiteDatabase database=getWritableDatabase();
        String query="select InspectionDate from RoutesConfig where RouteID="+routeId;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        String date = cursor.getString(cursor.getColumnIndex("InspectionDate"));
        String time=new SimpleDateFormat(" hh:mm:ss a").format(new Date());
        System.out.println("DateTImeStamp ::"+date+time);
        cursor.close();
        ContentValues contentValues=new ContentValues();
        contentValues.put("ReasonSkippedID",SkippedId);
        contentValues.put("Inspected",true);
        contentValues.put("Reading",Reading);
        contentValues.put(COL_INV_TimeStamp,date+time);
        database.update(TAB_Inventory,contentValues,"RouteID = "+routeId+" AND SubID = "+subId+" AND InvID="+InvId,null);
        database.close();
    }

    public List<EmployeesPojo> employeesList = new ArrayList<>();
    public List<EmployeesPojo> getEmployeesList() {
        int empID;
        String empFirstName, empLastName;
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT EmpID, EmpFirst, EmpLast FROM Employees ORDER BY EmpFirst ASC";
        Cursor resultEmployees = database.rawQuery(query, null);
        while (resultEmployees.moveToNext()){
            empID = resultEmployees.getInt(resultEmployees.getColumnIndex("EmpID"));
            empFirstName = resultEmployees.getString(resultEmployees.getColumnIndex("EmpFirst"));
            empLastName = resultEmployees.getString(resultEmployees.getColumnIndex("EmpLast"));
            employeesList.add(new EmployeesPojo(empID,empFirstName,empLastName));
        }
        resultEmployees.close();
        database.close();
        return employeesList;
    }
    public List<LeakRepairTypesPojo> leakRepairTypes = new ArrayList<>();
    public List<LeakRepairTypesPojo> getLeakRepairTypes(){
        int repairTypeID;
        String repairTypeName;
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT LeakRepairTypeID, LeakRepairTypeName FROM LeakRepairTypes ORDER BY LeakRepairTypeName ASC";
        Cursor repairTypeResult = database.rawQuery(query,null);
        while (repairTypeResult.moveToNext()){
            repairTypeID = repairTypeResult.getInt(repairTypeResult.getColumnIndex("LeakRepairTypeID"));
            repairTypeName = repairTypeResult.getString(repairTypeResult.getColumnIndex("LeakRepairTypeName"));
            leakRepairTypes.add(new LeakRepairTypesPojo(repairTypeID,repairTypeName));
        }
        repairTypeResult.close();
        database.close();
        return leakRepairTypes;
    }

    //Check Before Upload.

    public int checkSubBackEntered(int routeID) {
        int cnt = 0;
        SQLiteDatabase database=getReadableDatabase();
        String q="Select COUNT(SubID) from SubAreas where BackgroundEntered = 1 AND RouteID = "+routeID;
        Cursor cursor=database.rawQuery(q,null);
        while(cursor.moveToNext()){
            cnt=cursor.getInt(cursor.getColumnIndex("COUNT(SubID)"));
        }
        cursor.close();
        database.close();
        return cnt;
    }

    public int checkAllComponentsInspected(int routeID) {
        int cnt=0;
        SQLiteDatabase database=getReadableDatabase();
        String q="Select COUNT(InvID) from Inventory where Inspected = 1 AND RouteID = "+routeID;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            cnt=cursor.getInt(cursor.getColumnIndex("COUNT(InvID)"));
        }
        cursor.close();
        database.close();
        return cnt;
    }

    public int getSubareasCnt(int routeID) {

        int cnt=0;
        SQLiteDatabase database=getReadableDatabase();
        String q="Select COUNT(SubID) from SubAreas where RouteID = "+routeID;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            cnt=cursor.getInt(cursor.getColumnIndex("COUNT(SubID)"));

        }
        cursor.close();
        database.close();
        return cnt;
    }

    public int getAllCompCnt(int routeID) {
        int cnt=0;
        SQLiteDatabase database=getReadableDatabase();
        String q="Select COUNT(InvID) from Inventory where RouteID = "+routeID;
        Cursor cursor=database.rawQuery(q,null);
        while (cursor.moveToNext()){
            cnt=cursor.getInt(cursor.getColumnIndex("COUNT(InvID)"));

        }
        cursor.close();
        database.close();
        return cnt;
    }

   /* public void updateInventoryAsInspected(int routeID) {
        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("Inspected",1);
        database.update(TAB_Inventory,contentValues,"RouteID ="+routeID,null);
        database.close();
    }


    public int getReasonSkippedID(int InvID){
        SQLiteDatabase database=getReadableDatabase();
        String s="select ReasonSkippedID from Inventory where InvID=4164043";
        Cursor cursor=database.rawQuery(s,null);
        int position=0;
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                position=cursor.getInt(cursor.getColumnIndex("ReasonSkippedID"));
                System.out.println(position);
            }
            cursor.close();
            database.close();
            return  position;
        }
        return  position;
    }*/
    public boolean isInspectionDateEntered(int RouteID){
        SQLiteDatabase database=getWritableDatabase();
        String query="Select InspectionDate from RoutesConfig where RouteID= "+RouteID;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        String date=cursor.getString (cursor.getColumnIndex("InspectionDate"));
        if(date.isEmpty()){
            cursor.close();
            return true;
        }else {
            cursor.close();
            return false;
        }
    }
    public void UpdateInspectionDate(String date,int RouteID){
        SQLiteDatabase database=getWritableDatabase();
        String Date="'"+date+"'";
        System.out.println(Date);
        database.execSQL("UPDATE "+TAB_RoutesConfig+" SET "+COL_RC_InspectionDate+" = "+Date+" WHERE RouteID = "+RouteID);
        database.close();
    }

    public String getRouteConfigDate(int RouteId){
        SQLiteDatabase database=getWritableDatabase();
        String query="Select InspectionDate from RoutesConfig where RouteID= "+RouteId;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        String date=cursor.getString (cursor.getColumnIndex("InspectionDate"));
        if(!date.isEmpty()){
            cursor.close();
            database.close();
            return date;
        }else {
            cursor.close();
            database.close();
            return "0";
        }
    }

    public boolean isRoutePresentCheck(int RouteID){
        SQLiteDatabase database=getWritableDatabase();
        String query="Select RouteID from RoutesConfig where RouteID= "+RouteID;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        try{
            if(cursor.getString (cursor.getColumnIndex("RouteID")).isEmpty()){
                cursor.close();
                return true;
            }else {
                return false;
            }
        }catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
            cursor.close();
            database.close();
            return true;
        }
    }

//get subid from subareaname for leak edit repair
    public int getSubId(String SubName){
        int subid;
        SQLiteDatabase database=getWritableDatabase();
        String query="Select SubID from SubAreas where SubName="+"'"+SubName+"'";
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        subid=cursor.getInt (cursor.getColumnIndex("SubID"));
        cursor.close();
        database.close();
        return subid;
    }

    //get PathName from pathAbbrivation for leak edit repair
    public String getLeakPathName(String pathAbbr){
        String pathName;
        SQLiteDatabase database=getWritableDatabase();
        String query="Select LeakPathTypeName from LeakPathTypes where LeakPathTypeAbbr="+"'"+pathAbbr+"'";
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        pathName=cursor.getString (cursor.getColumnIndex("LeakPathTypeName"));
        cursor.close();
        database.close();
        return pathName;
    }

    //check if comp is inspected
    public boolean compIsInspected(int InvId){
        int flag;
        SQLiteDatabase database=getWritableDatabase();
        String query="Select Inspected from Inventory where InvID="+InvId;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        flag=cursor.getInt (cursor.getColumnIndex("Inspected"));
        cursor.close();
        database.close();
        if (flag>0) {
            return true;
        }else {
            return false;
        }
    }

    //get employee name from empid
    public String getEmpName(int id){
        String Name;
        SQLiteDatabase database=getWritableDatabase();
        String query="Select EmpFirst from Employees where EmpID="+id;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        Name=cursor.getString (cursor.getColumnIndex("EmpFirst"));
        cursor.close();
        database.close();
        return Name;
    }

    public boolean isLeakIDIsPresent(int leakid){
        SQLiteDatabase database=getWritableDatabase();
        String query="Select LeakID from LeakRepairs where LeakID="+leakid;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        cursor.close();
        database.close();
        if(cursor.getCount()>0){
            return true;
        }else {
            return false;
        }
    }
    public boolean isLeakIDIsPresentLeakreport(int invid){
        SQLiteDatabase database=getWritableDatabase();
        String query="Select LeakID from Leaks where InvID="+invid;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        cursor.close();
        database.close();
        if(cursor.getCount()>0){
            return true;
        }else {
            return false;
        }
    }

    public void updateLeakReapir(int leakId, int empId, int leakRepairTypeId, float leakRepairRate, String dateTime){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EmpID",empId);
        contentValues.put("LeakRepairTypeID",leakRepairTypeId);
        contentValues.put("LeakRepairRate",leakRepairRate);
        contentValues.put("LeakRepairDate",dateTime);
        database.update(TAB_LeakRepairs,contentValues,"LeakId = " + leakId,null);
        database.close();
    }


}
