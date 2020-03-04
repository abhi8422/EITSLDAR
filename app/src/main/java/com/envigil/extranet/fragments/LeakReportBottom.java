package com.envigil.extranet.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.envigil.extranet.ComponentDashboard;
import com.envigil.extranet.ComponentReading;
import com.envigil.extranet.R;
import com.envigil.extranet.RepairRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class LeakReportBottom extends BottomSheetDialogFragment {

    Button btnCanReport, btnReport;
    int CompId ,SubId,LeakId;
    String Unit;

    public LeakReportBottom() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_leak_reporting_bottom, container, false);
        Intent intent=new Intent();
        LeakId=intent.getIntExtra("LeakID",0);
        Toast.makeText(getContext(), "Leak ID : : "+LeakId, Toast.LENGTH_SHORT).show();
        SubId=intent.getIntExtra("SubId",0);
//        Toast.makeText(getContext(), "Bottom::"+SubId, Toast.LENGTH_SHORT).show();
        CompId=intent.getIntExtra("CompId",0);
//        Toast.makeText(getContext(), "CompID::"+CompId, Toast.LENGTH_SHORT).show();
        Unit=intent.getStringExtra("Unit");
        btnCanReport = view.findViewById(R.id.btn_can_report);
        btnReport = view.findViewById(R.id.btn_repair_report);

        final LeakReportBottom leakReportBottom = new LeakReportBottom();

        btnReport.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animate));

                startActivity(new Intent(getActivity(), RepairRequest.class).putExtra("CompId",CompId).putExtra("SubId",SubId).putExtra("Unit",Unit).putExtra("LeakID",LeakId));
                getActivity().getSupportFragmentManager().beginTransaction().remove(LeakReportBottom.this).commit();

            }
        });
        btnCanReport.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animate));
                startActivity(new Intent(getActivity(), RepairRequest.class).putExtra("CompId",CompId).putExtra("SubId",SubId));
//                startActivity(new Intent(getActivity(), ComponentDashboard.class).putExtra("SubId",SubId));
            }
        });
        return view;
    }

 /*   public void repairReading(View view) {
        startActivity(new Intent(getActivity(), RepairRequest.class));
    }*/



}
