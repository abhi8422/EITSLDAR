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


import java.util.ArrayList;
import java.util.List;

public class RouteInspectedAdapter extends RecyclerView.Adapter<RouteInspectedAdapter.ViewHolder> {

    List<SubAreasPojo> list=new ArrayList<>();
    public RouteInspectedAdapter(List<SubAreasPojo> list) {
        this.list = list;

    }


    @NonNull
    @Override
    public RouteInspectedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_inspected_adapter,parent,false);

        return new ViewHolder(view);
    }

    private void showToast(Context context) {
//        Toast.makeText(context,"List is Empty",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBindViewHolder(@NonNull RouteInspectedAdapter.ViewHolder holder, int position) {
        SubAreasPojo subAreasPojo=list.get(position);


        holder.titleSubRouteIn.setText(subAreasPojo.getSubArea());

        int inspected = subAreasPojo.getInspected();
        if (inspected==1){
            holder.InsptrueSubIn.setVisibility(View.VISIBLE);

        }
        else{
            holder.InsptrueSubIn.setVisibility(View.GONE);
        }
        holder.BackgroundSubRouteIn.setText(String.valueOf(subAreasPojo.getBackground()));
        holder.InspectionDateIn.setText(subAreasPojo.getDate());
        holder.InspCount.setText(String.valueOf(subAreasPojo.getCnt()));
        holder.InspPer.setText(String.format("%.2f",subAreasPojo.getPer()) + " %");
        holder.InspArea.setText(subAreasPojo.getAreaName());
        String SubDesc=subAreasPojo.getSubDesc();
        if (SubDesc==null){
            holder.InspSubDesc.setText("--");
        }
        else {
            holder.InspSubDesc.setText(SubDesc);
        }
    }

    @Override
    public int getItemCount() {
       // int size=subAreaIn.size()+RouteConfigsIn.size();
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleSubRouteIn, InspectedSubRouteIn, BackgroundSubRouteIn,InspectionDateIn,InspCount,InspPer,InspArea,InspSubDesc;
        ImageView InsptrueSubIn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleSubRouteIn = itemView.findViewById(R.id.TitleSubRouteIn);
            InsptrueSubIn = itemView.findViewById(R.id.InspTrueSubIn);
            BackgroundSubRouteIn = itemView.findViewById(R.id.BackgroundSubRouteIn);
            InspectionDateIn =itemView.findViewById(R.id.InspectionDateIn);
            InspCount = itemView.findViewById(R.id.TotalCompInsp);
            InspPer = itemView.findViewById(R.id.InspPerInsp);
            InspArea = itemView.findViewById(R.id.InspArea);
            InspSubDesc = itemView.findViewById(R.id.InspSubDesc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i=getLayoutPosition();
            int Subid = list.get(i).getSubId();
            int RouteId = list.get(i).getRouteId();
            Float background=list.get(i).getBackground();
            Bundle bundle=new Bundle();
            bundle.putFloat("Background",background);
            bundle.putInt("SubID",Subid);
            bundle.putInt("RouteID",RouteId);
            AppCompatActivity appCompatActivity = (AppCompatActivity)v.getContext();
            AllBottomSheetFragment allBottomSheetFragment=new AllBottomSheetFragment(appCompatActivity);
            allBottomSheetFragment.show(appCompatActivity.getSupportFragmentManager(),"RouteReadingBottomSheet");
            allBottomSheetFragment.setArguments(bundle);
        }
    }
}
