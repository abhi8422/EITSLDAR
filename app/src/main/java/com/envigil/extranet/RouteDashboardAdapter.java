package com.envigil.extranet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class RouteDashboardAdapter extends RecyclerView.Adapter<RouteDashboardAdapter.ViewHolder> {
    SQLiteHelper sqLiteHelper;
    Context context;

    public RouteDashboardAdapter(List<SubAreasPojo> list) {
        this.list = list;
    }

    List<SubAreasPojo> list=new ArrayList<>();

    @NonNull
    @Override
    public RouteDashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_route_dashboard_adapter,parent,false);

        return new ViewHolder(view);
}

    @Override
    public void onBindViewHolder(@NonNull RouteDashboardAdapter.ViewHolder holder, int position) {
        SubAreasPojo subAreasPojo=list.get(position);
        holder.titleSubRoute.setText(subAreasPojo.getSubArea());
        int inspect=subAreasPojo.getInspected();
        if (inspect==1){
            holder.InsptrueSub.setVisibility(View.VISIBLE);

        }
        else{
            holder.InsptrueSub.setVisibility(View.GONE);
        }
        if(subAreasPojo.getBackground()==0.0f){
            holder.BackgroundSubRoute.setText("--");
        }else {
            holder.BackgroundSubRoute.setText(String.valueOf(subAreasPojo.getBackground()));
        }
        holder.InspectionDate.setText(subAreasPojo.getDate());
        holder.TotalComp.setText(String.valueOf(subAreasPojo.getCnt()));
        holder.InspPer.setText(String.format("%.2f",subAreasPojo.getPer()) + " %");
        holder.AllAreaName.setText(subAreasPojo.getAreaName());

    }

    @Override
    public int getItemCount() {

            return list.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleSubRoute, InspectedSubRoute, BackgroundSubRoute,InspectionDate,TotalComp,InspPer,AllAreaName;
        ImageView InsptrueSub;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleSubRoute = itemView.findViewById(R.id.TitleSubRoute);

            BackgroundSubRoute = itemView.findViewById(R.id.BackgroundSubRoute);
            InspectionDate =itemView.findViewById(R.id.InspectionDate);
            TotalComp=itemView.findViewById(R.id.TotalComp);
            InspPer=itemView.findViewById(R.id.InspPer);
            AllAreaName =itemView.findViewById(R.id.AllAreaName);
            InsptrueSub  =itemView.findViewById(R.id.InspTrueSub);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i=getLayoutPosition();
            int Subid = list.get(i).getSubId();
            int RouteID=list.get(i).getRouteId();
            Float background=list.get(i).getBackground();
            AppCompatActivity appCompatActivity = (AppCompatActivity)v.getContext();
            AllBottomSheetFragment allBottomSheetFragment=new AllBottomSheetFragment(appCompatActivity);
            Bundle bundle=new Bundle();
            bundle.putFloat("Background",background);
            bundle.putInt("SubID",Subid);
            bundle.putInt("RouteID",RouteID);
            allBottomSheetFragment.show(appCompatActivity.getSupportFragmentManager(),"RouteReadingBottomSheet");
            allBottomSheetFragment.setArguments(bundle);
        }
    }
    private void showToast(Context context) {
//        Toast.makeText(context,"List is Empty",Toast.LENGTH_SHORT).show();

    }
}
