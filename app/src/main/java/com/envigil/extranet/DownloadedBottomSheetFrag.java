package com.envigil.extranet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.envigil.extranet.AddInspectionDate.AddInspectionDate;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.models.UploadRouteData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class DownloadedBottomSheetFrag extends BottomSheetDialogFragment{

    private float BackReading;
    int routeID,workId;
    String inspection,routeName,inspDate;
    String DAEP;
    TextView RouteBackgroundReading,ViewSubArea,UploadRoute,viewLeaks,UploadRoutePartially;
    static RouteReadingBottomSheetFrag routeReadingBottomSheetFrag;
    AlertDialog.Builder builder;
    SQLiteHelper sqLiteHelper;
    LinearLayout linearLayout;

    public DownloadedBottomSheetFrag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        linearLayout=view.findViewById(R.id.downaload_bottom);
        Bundle bundle =getArguments();
        BackReading = bundle.getFloat("BackgroundReading");
        routeID=bundle.getInt("RouteId");
        workId=bundle.getInt("workId");
        DAEP=bundle.getString("DAEP");
        routeName=bundle.getString("RouteName");
        inspection=bundle.getString("Inspected");
        inspDate=bundle.getString("InspDate");

        //set route name and date for previnspection screen;
       UploadRouteData.routename=routeName;
       UploadRouteData.routeinspdate=inspDate;

        RouteBackgroundReading = view.findViewById(R.id.RouteBackgroundReading);
        ViewSubArea = view.findViewById(R.id.ViewSubArea);
        UploadRoute = view.findViewById(R.id.UploadRoute);
        viewLeaks = view.findViewById(R.id.ViewLeaks);
        UploadRoutePartially=view.findViewById(R.id.UploadRoutePartially);
        sqLiteHelper=new SQLiteHelper(getContext());

        if(!new SQLiteHelper(view.getContext()).getRouteConfigDate(routeID).equals("0")){
            view.findViewById(R.id.AddInspDate).setVisibility(View.GONE);
            view.findViewById(R.id.dividerinsp).setVisibility(View.GONE);
        }else {
            view.findViewById(R.id.AddInspDate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), AddInspectionDate.class).putExtra("RouteID",routeID).putExtra("RouteName",routeName));
                }
            });
        }

        if (inspection.equals("true")||inspection.equals("1")){
            UploadRoutePartially.setVisibility(View.GONE);
        }

        RouteBackgroundReading.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                 routeReadingBottomSheetFrag = new RouteReadingBottomSheetFrag();
                routeReadingBottomSheetFrag.show(getFragmentManager(),"RouteBackgroundReading");
                Bundle bundle=new Bundle();
                bundle.putFloat("BackReading",BackReading);
                bundle.putInt("RouteId",routeID);
                routeReadingBottomSheetFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().remove(DownloadedBottomSheetFrag.this).commit();
            }
        });

        ViewSubArea.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                boolean result=sqLiteHelper.isInspectionDateEntered(routeID);
                if(result){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please set the inspection date");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().getSupportFragmentManager().beginTransaction().remove(DownloadedBottomSheetFrag.this).commit();
                            startActivity(new Intent(getContext(), AddInspectionDate.class).putExtra("RouteID",routeID).putExtra("RouteName",routeName));
                        }
                    });
                    builder.show();
                }else {
                    startActivity(new Intent(getActivity(), RouteDashboard.class).putExtra("RouteID",routeID).putExtra("DAEP",DAEP));
                    getActivity().getSupportFragmentManager().beginTransaction().remove(DownloadedBottomSheetFrag.this).commit();
                }
            }
        });

        viewLeaks.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                startActivity(new Intent(getActivity(),ShowLeaksActivity.class).putExtra("RouteID",routeID));
                getActivity().getSupportFragmentManager().beginTransaction().remove(DownloadedBottomSheetFrag.this).commit();

            }
        });

        UploadRoute.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
//                Toast.makeText(getContext(), "Inspected ::"+inspection, Toast.LENGTH_SHORT).show();

                    if (inspection.equals("true")|| inspection.equals("1")){
                        startActivity(new Intent(getActivity(),PrevInspection.class).putExtra("WorkId",workId).putExtra("RouteID",routeID));
                        getActivity().getSupportFragmentManager().beginTransaction().remove(DownloadedBottomSheetFrag.this).commit();

                    }
                    else{
                        if (DAEP.equals("0")||DAEP.equals("false")){
                            builder = new AlertDialog.Builder(getContext());
                            //Setting message manually and performing action on button click
                            builder.setMessage("Do you want to mark all components as inspected and proceed !")
                                    .setCancelable(false)
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            sqLiteHelper.markAllCompInspected(routeID);
                                            startActivity(new Intent(getActivity(),PrevInspection.class).putExtra("WorkId",workId).putExtra("RouteID",routeID));
                                            getActivity().getSupportFragmentManager().beginTransaction().remove(DownloadedBottomSheetFrag.this).commit();
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("Route not Inspected !!");
                            alert.show();
                        }
                        else {
                            builder = new AlertDialog.Builder(getContext());
                            //Setting message manually and performing action on button click
                            builder.setMessage("Route is not Inspected you cannot upload the route !")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("Route not Inspected !!");
                            alert.show();
                        }
                        //Toast.makeText(getContext(), "Route Inspection Is Not Done You Cannot Upload The Route", Toast.LENGTH_SHORT).show();
                    }
                }
        });

        UploadRoutePartially.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int compCnt,compCntTotal;
                final boolean partially=true;
                compCnt=sqLiteHelper.checkAllComponentsInspected(routeID);
                compCntTotal=sqLiteHelper.getAllCompCnt(routeID);
                if (compCnt==0){
                    builder = new AlertDialog.Builder(getContext());
                    //Setting message manually and performing action on button click
                    builder.setMessage("Cannot Upload the Route")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("No Component Inspected ");
                    alert.show();
                }
                else {
                    builder = new AlertDialog.Builder(getContext());
                    //Setting message manually and performing action on button click
                    builder.setMessage("Only "+compCnt+" Of "+compCntTotal+" Components are Inspected ! Do you want to upload it partially ?")
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(getActivity(),PrevInspection.class).putExtra("WorkId",workId).putExtra("RouteID",routeID).putExtra("Partially",partially));
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(DownloadedBottomSheetFrag.this).commit();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Upload Route Partially ?");
                    alert.show();
                }

            }
        });

        return view;
    }

    public static void canceldialog(){
        routeReadingBottomSheetFrag.dismiss();
    }
}
