package com.envigil.extranet;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RouteInspectedFragment extends Fragment {

    RouteInspectedFragment routeInspectedFragment;
    RouteInspectedAdapter routeInspectedAdapter;
    RouteDashboardAdapter routeDashboardAdapter;
    public int routeId;

    public static RecyclerView InspectedRV;

    public RouteInspectedFragment(int id) {
        // Required empty public constructor
        routeId = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route_inspected, container, false);

         InspectedRV = view.findViewById(R.id.InspectedRV);
        InspectedRV.setLayoutManager(new LinearLayoutManager(getActivity()));


        SQLiteHelper sqLiteHelper=new SQLiteHelper(getActivity());
        routeInspectedAdapter=new RouteInspectedAdapter(sqLiteHelper.getSubareasInspected(routeId));
        InspectedRV.setAdapter(routeInspectedAdapter);

        return view;
    }

}
