package com.envigil.extranet;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


public class RouteReadingBottomSheetFrag extends BottomSheetDialogFragment {
    Button RBR_btn_cancel,RBR_btn_save;
    static TextInputEditText RouteBackReading,MaxRouteBackReading;
    Switch Inspected;
    String RBreading="";
    float BackReading;
    int routeId;
    static View view;
    int a=1;
    static int i=0;
    int delay = 1000;
    static Double max = 0.0;




    public RouteReadingBottomSheetFrag() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         max = 0.0;
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_bot_sht_route_back_reading, container, false);
        Bundle bundle = getArguments();
        BackReading = bundle.getFloat("BackReading");
        routeId=bundle.getInt("RouteId");
//        routeConfigRepository = new RouteConfigRepository(getContext());
        RBR_btn_cancel = view.findViewById(R.id.RBR_btn_cancel);
        RBR_btn_save = view.findViewById(R.id.RBR_btn_save);
        RouteBackReading = view.findViewById(R.id.RouteBackReading);
        RouteBackReading.setEnabled(false);
        MaxRouteBackReading=view.findViewById(R.id.MaxRouteBackReading);
        MaxRouteBackReading.setText(String.valueOf(BackReading));
        MaxRouteBackReading.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MaxRouteBackReading.getText().clear();
                return false;
            }
        });
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        RBR_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                DownloadedBottomSheetFrag.canceldialog();
                Bluetooth.RouteBackReading=false;
            }
        });


        RBR_btn_save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                RBreading = MaxRouteBackReading.getText().toString();
                if(RBreading.equals("")){
//                    Toast.makeText(getActivity(), "Background Reading Cannot Be Empty", Toast.LENGTH_SHORT).show();
                    RouteBackReading.setError("Please Enter A Reading");
                }
                else{
                    SQLiteHelper sqLiteHelper=new SQLiteHelper(getContext());
                    sqLiteHelper.UpdateBackgroundReadingRoutesCofig(routeId,Float.valueOf(RBreading));
                    sqLiteHelper.UpdateBackgroundEnteredFlagRoutes(routeId);
                    TestRecycler testRecycler =new TestRecycler(sqLiteHelper.getAll());
                    DownloadedWorkOrderFrag.downloadedrecycler.setAdapter(testRecycler);
                    testRecycler.notifyDataSetChanged();

                    getActivity().getSupportFragmentManager().beginTransaction().remove(RouteReadingBottomSheetFrag.this).commit();
                }

            }
        });


        Bluetooth.RouteBackReading=true;
        return view;
    }
    public static Handler RouteBackhandler=new Handler(new Handler.Callback() {
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
                                        MaxRouteBackReading.setText(String.valueOf(max));
                                    }else {
                                        max=Double.valueOf(fString);
                                        MaxRouteBackReading.setText(String.valueOf(max));
                                    }
                                    RouteBackReading.setText(fString);
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
    });

    @Override
    public void onPause() {
        super.onPause();
        max = 0.0;
        Bluetooth.RouteBackReading=false;
    }

    @Override
    public void onResume() {
        super.onResume();
         max = 0.0;
        Bluetooth.RouteBackReading=true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        max=0.0;
        Bluetooth.RouteBackReading=false;
        RouteBackhandler.removeCallbacksAndMessages(null);
    }

    public static boolean isNumber(String num){
        try{
            Double.parseDouble(num);
            return true;
        }catch (NumberFormatException e){
            return false;
        }

    }



}