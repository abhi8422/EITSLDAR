package com.envigil.extranet.UninspectedCmpBottomSheet;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.envigil.extranet.R;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.SubAreaBackgroundReadingFragment;
import com.envigil.extranet.TableOfComponents.ComponentsTable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class UninspectedCmpBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private  TextView backGroundTxtView,markallcmpinspected,viewComponentGrid;
    Float background;
    int SubId,RouteId;

static SubAreaBackgroundReadingFragment subAreaBackgroundReadingFragment;
WeakReference<Context> weakReference;
Context context;
    public UninspectedCmpBottomSheetFragment(Context context) {
        // Required empty public constructor
        weakReference=new WeakReference<>(context);
        this.context=context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        background=bundle.getFloat("Background");
        SubId = bundle.getInt("SubID");
        RouteId=bundle.getInt("RouteID");
        View view=inflater.inflate(R.layout.fragment_uninspectedcmp_bottom_sheet, container, false);
        backGroundTxtView=view.findViewById(R.id.enter_backgorund_reading);
        backGroundTxtView.setOnClickListener(this);
       // viewComponent=view.findViewById(R.id.view_compoents);
        viewComponentGrid = view.findViewById(R.id.view_compoents_grid);
       // viewComponent.setOnClickListener(this);
        viewComponentGrid.setOnClickListener(this);
        markallcmpinspected=view.findViewById(R.id.txt_mark_all_cmpminspcted);
        markallcmpinspected.setOnClickListener(this);

        return view;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.enter_backgorund_reading:
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animate));
                subAreaBackgroundReadingFragment=new SubAreaBackgroundReadingFragment(weakReference.get());
                Bundle bundle=new Bundle();
                bundle.putFloat("background",background);
                bundle.putInt("SubID",SubId);
                bundle.putInt("RouteID",RouteId);
                subAreaBackgroundReadingFragment.show(getFragmentManager(),"sub reading fragment");
                subAreaBackgroundReadingFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().remove(UninspectedCmpBottomSheetFragment.this).commit();
                break;
            /*case R.id.view_compoents:
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext());

                builder.setMessage("The Component is already inspected. Do you want to proceed ?") .setTitle("Alert");

                //Setting message manually and performing action on button click
                builder.setMessage("Enter A Background Reading before going ahead.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });

                if (background==0.0f || background==null){
                    //Toast.makeText(getContext(), "Enter BackgroundReading Before going ahead...", Toast.LENGTH_SHORT).show();
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Background Reading not entered !");
                    alert.show();
                }
                else{
                    Intent intent=new Intent(getActivity(),ComponentDashboard.class).putExtra("SubId",SubId).putExtra("RouteID",RouteId);
                    startActivity(intent);
                    getActivity().getSupportFragmentManager().beginTransaction().remove(AllBottomSheetFragment.this).commit();
                    getActivity().finish();
//                    Toast.makeText(getContext(), "Background Entered"+background, Toast.LENGTH_SHORT).show();
                }

               break;*/
            case R.id.view_compoents_grid:
                AlertDialog.Builder builder1 = null;
                builder1 = new AlertDialog.Builder(getContext());

                builder1.setMessage("The Component is already inspected. Do you want to proceed ?") .setTitle("Alert");

                //Setting message manually and performing action on button click
                builder1.setMessage("Enter A Background Reading before going ahead.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });

                if (background==0.0f || background==null){
                    //Toast.makeText(getContext(), "Enter BackgroundReading Before going ahead...", Toast.LENGTH_SHORT).show();
                    //Creating dialog box
                    AlertDialog alert = builder1.create();
                    //Setting the title manually
                    alert.setTitle("Background Reading not entered !");
                    alert.show();
                }
                else{
                    Intent intent=new Intent(getActivity(), ComponentsTable.class).putExtra("SubId",SubId).putExtra("RouteID",RouteId);
                    startActivity(intent);
                    getActivity().finish();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(UninspectedCmpBottomSheetFragment.this).commit();

               }
                break;
            case R.id.txt_mark_all_cmpminspcted:
                AlertDialog.Builder builder2 = null;
                builder2 = new AlertDialog.Builder(getContext());
                builder2.setMessage("Do You Want To Mark All Components As Inspected .")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteHelper sqLiteHelper=new SQLiteHelper(getContext());
                                sqLiteHelper.setAllCmpAsInspected(RouteId,SubId);
                                sqLiteHelper.markAllCompInspected(RouteId);
                                dialog.cancel();
                            }
                        });
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder2.create();
                alert.show();
                break;

        }
    }


}
