package com.envigil.extranet;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import static com.envigil.extranet.RouteUnInspectedFragment.oncreate_flag;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubAreaBackgroundReadingFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private Button save,cancel;
    private static EditText subbackreading;
    private static EditText maxsubbackreading;
    private String reading;
    SQLiteHelper sqLiteHelper;
    Float background;
    int SubID,RouteId;
    static Double max = 0.0;
    static int i=0;
    Context context;

    public SubAreaBackgroundReadingFragment(Context context) {
        this.context=context;
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_area_background_reading, container, false);
        subbackreading=view.findViewById(R.id.SubBackReading);
        subbackreading.setEnabled(false);
        maxsubbackreading=view.findViewById(R.id.MaxSubBackReading);
        Bundle bundle=getArguments();
        background=bundle.getFloat("background");
        SubID = bundle.getInt("SubID");
        RouteId =bundle.getInt("RouteID");
        maxsubbackreading.setText(String.valueOf(background));
        maxsubbackreading.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                maxsubbackreading.setText("");
                return false;
            }
        });
        save=view.findViewById(R.id.save);
        cancel=view.findViewById(R.id.cancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        Bluetooth.SubAreaBackReading =true;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Inflate the layout for this fragment
        return view;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                reading=maxsubbackreading.getText().toString().trim();
                if(!reading.equals("")){
                    sqLiteHelper=new SQLiteHelper(getContext());
                    sqLiteHelper.UpdateBackgroundReadingSubAreas(RouteId,SubID,Float.valueOf(reading));
                    sqLiteHelper.UpdateBackgroundEnteredFlagSubAreas(RouteId,SubID);
                    //Route Dashboard
        RouteDashboardAdapter routeDashboardAdapter =new RouteDashboardAdapter(new SQLiteHelper(getContext()).getSubareas(RouteId));
        RouteAllFragment.RVSubRoute.setAdapter(routeDashboardAdapter);
        routeDashboardAdapter.notifyDataSetChanged();

        //Route Inspected
        RouteInspectedAdapter routeInspectedAdapter=new RouteInspectedAdapter(new SQLiteHelper(getContext()).getSubareasInspected(RouteId));
        RouteInspectedFragment.InspectedRV.setAdapter(routeInspectedAdapter);
        routeInspectedAdapter.notifyDataSetChanged();

//        Route UnInspected
                    if(oncreate_flag){
                        RouteUninspectedAdapter routeUninspectedAdapter=new RouteUninspectedAdapter(new SQLiteHelper(getContext()).getSubareasUnInspected(RouteId));
                        RouteUnInspectedFragment.UnInspectedRV.setAdapter(routeUninspectedAdapter);
                        routeUninspectedAdapter.notifyDataSetChanged();
                    }
        getActivity().getSupportFragmentManager().beginTransaction().remove(SubAreaBackgroundReadingFragment.this).commit();


                }else {
                    subbackreading.setError("Please Enter Value");
                }
                break;
            case R.id.cancel:
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                AllBottomSheetFragment.cancelbottom();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //SendReceive.SubAreaBackReading=true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        max=0.0;
        i=0;
        Bluetooth.SubAreaBackReading =false;
        SubAreaBackhandler.removeCallbacksAndMessages(null);

    }
    public static Handler SubAreaBackhandler=new Handler(new Handler.Callback() {
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
                                        maxsubbackreading.setText(String.valueOf(max));
                                    }else {
                                        max=Double.valueOf(fString);
                                        maxsubbackreading.setText(String.valueOf(max));
                                    }
                                    subbackreading.setText(fString);
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
    public static boolean isNumber(String num){
        try{
            Double.parseDouble(num);
            return true;
        }catch (NumberFormatException e){
            return false;
        }

    }
}