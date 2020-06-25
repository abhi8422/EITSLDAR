package com.envigil.extranet.DeleteDownlaodedRouteBottomSheet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.envigil.extranet.R;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class DeleteDownloadedRouteBottomSheetFrag extends BottomSheetDialogFragment{

    private float BackReading;
    int routeID,workId;
    String inspection;
    String DAEP;
    TextView Yes,No;
    AlertDialog.Builder builder;
    SQLiteHelper sqLiteHelper;
    public DeleteDownloadedRouteBottomSheetFrag() {
        // Required empty public constructor


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view =inflater.inflate(R.layout.fragment_bottom_sheet_delete, container, false);
        Bundle bundle =getArguments();
        /*routeID=bundle.getInt("RouteId");
        workId=bundle.getInt("workId");
        DAEP=bundle.getString("DAEP");*/
        sqLiteHelper=new SQLiteHelper(getContext());
        view.findViewById(R.id.delete_route_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Deleteing the route",Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().remove(DeleteDownloadedRouteBottomSheetFrag.this).commit();
            }
        });
        view.findViewById(R.id.delete_route_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(DeleteDownloadedRouteBottomSheetFrag.this).commit();
            }
        });
        return view;
    }


}
