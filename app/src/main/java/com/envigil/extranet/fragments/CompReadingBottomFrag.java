package com.envigil.extranet.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.envigil.extranet.Bluetooth.Bluetooth;
import com.envigil.extranet.ComponentReading;
import com.envigil.extranet.ComponentsInspectedFragment;
import com.envigil.extranet.ComponentsUninspectedFragment;
import com.envigil.extranet.R;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.UninspectedCompoAdapter;
import com.envigil.extranet.ViewComponentAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;


public class CompReadingBottomFrag extends BottomSheetDialogFragment {


    TextView CRB_btn_cancel,CRB_btn_save;
    static TextInputEditText CompReadBackReading;
    static TextInputEditText liveReading;
    String Compreading="";
    float compBackReading,prevRead;
    int SubId,CompId,RouteId,InvId;
    SQLiteHelper sqLiteHelper;
    static Double max = 0.0;
    static int i=0;

    public CompReadingBottomFrag() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comp_reading_bottom, container, false);
//        Bundle unInspectedbundle = getArguments();
        Bundle bundle =getArguments();
        SubId = bundle.getInt("SubId");
        CompId  = bundle.getInt("CompId");
        RouteId = bundle.getInt("RouteID");
        InvId =bundle.getInt("InvID");

//        Toast.makeText(getContext(), "Inv Id ::"+InvId, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "Comp Id :: "+CompId, Toast.LENGTH_SHORT).show();
        sqLiteHelper=new SQLiteHelper(getContext());
        prevRead =sqLiteHelper.getInvBackground(InvId);
        liveReading=view.findViewById(R.id.liveReading);
        liveReading.setEnabled(false);
        CRB_btn_save = view.findViewById(R.id.CRB_btn_save);
        CRB_btn_cancel = view.findViewById(R.id.CRB_btn_cancel);
        CompReadBackReading = view.findViewById(R.id.CompReadBackReading);
        CompReadBackReading.setText(String.valueOf(prevRead));
        CompReadBackReading.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CompReadBackReading.getText().clear();
                return false;
            }
        });
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        CRB_btn_save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                if (CompReadBackReading.getText().toString().equals("")){
                    CompReadBackReading.setError("Background Reading Cannot Be Empty !");
                }
                else{
                    compBackReading = Float.parseFloat(CompReadBackReading.getText().toString());
                    sqLiteHelper.UpdateBackgroundReadingInventory(RouteId,SubId,InvId,compBackReading);
                    ViewComponentAdapter viewComponentAdapter =new ViewComponentAdapter(new SQLiteHelper(getContext()).getInspectedID(SubId,RouteId));
                    ComponentsInspectedFragment.rvComponentInspection.setAdapter(viewComponentAdapter);
                    viewComponentAdapter.notifyDataSetChanged();

                     UninspectedCompoAdapter uninspectedCompoAdapter= new UninspectedCompoAdapter(new SQLiteHelper(getContext()).getUnInspectedID(SubId,RouteId));
                     ComponentsUninspectedFragment.rvCompoUninspected.setAdapter(uninspectedCompoAdapter);
                     uninspectedCompoAdapter.notifyDataSetChanged();
                     getActivity().getSupportFragmentManager().beginTransaction().remove(CompReadingBottomFrag.this).commit();

                }

            }
        });
        CRB_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                getActivity().getSupportFragmentManager().beginTransaction().remove(CompReadingBottomFrag.this).commit();
            }
        });
        Bluetooth.CompReadingBottomFrag=true;
        return view;
    }

    public static Handler CompReadingBottomFragHandler=new Handler(new Handler.Callback() {
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
                                        CompReadBackReading.setText(String.valueOf(max));
                                    }else {
                                        max=Double.valueOf(fString);
                                        CompReadBackReading.setText(String.valueOf(max));
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
    });
    public static boolean isNumber(String num){
        try{
            Double.parseDouble(num);
            return true;
        }catch (NumberFormatException e){
            return false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        max=0.0;
        i=0;
        Bluetooth.CompReadingBottomFrag=false;
        CompReadingBottomFragHandler.removeCallbacksAndMessages(null);
    }
}