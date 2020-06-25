package com.envigil.extranet;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class RouteAllFragment extends Fragment {
    RouteDashboardAdapter routeDashboardAdapter;


    public  int Routeid;
    public static RecyclerView RVSubRoute;
    SQLiteHelper sqLiteHelper;

    Context context;
    public RouteAllFragment(int id) {

        Routeid=id;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_route_all, container, false);
        RVSubRoute = view.findViewById(R.id.RVSubRoute);
        RVSubRoute.setLayoutManager(new LinearLayoutManager(getActivity()));
        sqLiteHelper=new SQLiteHelper(getActivity());
        Timber.e("All Route Frag RouteID ="+Routeid);
        routeDashboardAdapter=new RouteDashboardAdapter(sqLiteHelper.getSubareas(Routeid));
        RVSubRoute.setAdapter(routeDashboardAdapter);
        return view;
    }


}
