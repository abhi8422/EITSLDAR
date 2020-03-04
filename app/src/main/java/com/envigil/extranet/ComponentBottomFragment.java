package com.envigil.extranet;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.envigil.extranet.fragments.CompReadingBottomFrag;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class ComponentBottomFragment extends BottomSheetDialogFragment {
TextView comp_reading,com_back_reading;
int ID,SubID,RouteId,InvId;

    public ComponentBottomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_component_reading, container, false);
        Bundle Inspectedbundle = getArguments();
        ID= Inspectedbundle.getInt("CompId");
        SubID=Inspectedbundle.getInt("SubId");
        RouteId=Inspectedbundle.getInt("RouteID");
        InvId =Inspectedbundle.getInt("InvID");

//        Toast.makeText(getContext(), "Inv ID : : "+InvId, Toast.LENGTH_SHORT).show();

//        Toast.makeText(getContext(), "Bottom ::"+SubID, Toast.LENGTH_SHORT).show();
//        final float size = Inspectedbundle.getFloat("Size");
//        System.out.println(size);
//
//        final String location = Inspectedbundle.getString("Location");
//        System.out.println(location);
//
//        final Boolean inspected = Inspectedbundle.getBoolean("IF-inspected");


        comp_reading = view.findViewById(R.id.comp_reading);
        /*com_back_reading = view.findViewById(R.id.comp_backgorund_reading);*/

        comp_reading.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                startActivity(new Intent(getActivity(), ComponentReading.class).putExtra("CompId",ID).putExtra("SubId",SubID).putExtra("RouteID",RouteId).putExtra("InvID",InvId));
                getActivity().getSupportFragmentManager().beginTransaction().remove(ComponentBottomFragment.this).commit();
                getActivity().finish();

            }
        });
        /*com_back_reading.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                CompReadingBottomFrag compReadingBottomFrag =new CompReadingBottomFrag();
                compReadingBottomFrag.show(getFragmentManager(),"Component Background Reading");


                Bundle bundle = new Bundle();
                bundle.putInt("SubId",SubID);
                bundle.putInt("CompId",ID);
                bundle.putInt("RouteID",RouteId);
                bundle.putInt("InvID",InvId);
                compReadingBottomFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().remove(ComponentBottomFragment.this).commit();

            }
        });*/
        return view;
    }

}
