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


import com.envigil.extranet.UninspectedCmpBottomSheet.UninspectedCmpBottomSheetFragment;

import java.util.List;

public class RouteUninspectedAdapter extends RecyclerView.Adapter<RouteUninspectedAdapter.ViewHolder> {


    private List<SubAreasPojo> list;
    Context context;

    public RouteUninspectedAdapter(List<SubAreasPojo> list) {
        this.list = list;

    }
    @NonNull
    @Override
    public RouteUninspectedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_uninspected_adapter,parent,false);

        return new ViewHolder(view);
    }

    private void showToast(Context context) {
//        Toast.makeText(context,"List is Empty",Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBindViewHolder(@NonNull RouteUninspectedAdapter.ViewHolder holder, int position) {
        SubAreasPojo subAreasPojo=list.get(position);

        holder.titleSubRouteUnIn.setText(subAreasPojo.getSubArea());

        int inspected = subAreasPojo.getInspected();
        if (inspected==1){
            holder.InsptrueSubUn.setVisibility(View.VISIBLE);

        }
        else {
            holder.InsptrueSubUn.setVisibility(View.GONE);
        }
        holder.BackgroundSubRouteUnIn.setText(String.valueOf(subAreasPojo.getBackground()));
        holder.InspectionDateUnIn.setText(subAreasPojo.getDate());
        holder.UnInspCount.setText(String.valueOf(subAreasPojo.getCnt()));
        holder.UnInspPer.setText(String.format("%.2f",subAreasPojo.getPer()) + " %");
        holder.UnInspArea.setText(subAreasPojo.getAreaName());
        String SubDesc = subAreasPojo.getSubDesc();
        if (SubDesc==null){
            holder.UnInspSubDesc.setText("--");
        }
        else {
            holder.UnInspSubDesc.setText(SubDesc);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleSubRouteUnIn, InspectedSubRouteUnIn, BackgroundSubRouteUnIn,InspectionDateUnIn,UnInspCount,UnInspPer,UnInspArea,UnInspSubDesc;
        ImageView InsptrueSubUn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


                titleSubRouteUnIn = itemView.findViewById(R.id.TitleSubRouteUnIn);
                InsptrueSubUn = itemView.findViewById(R.id.InspTrueSubUn);
                BackgroundSubRouteUnIn = itemView.findViewById(R.id.BackgroundSubRouteUnIn);
                InspectionDateUnIn =itemView.findViewById(R.id.InspectionDateUnIn);
                UnInspCount = itemView.findViewById(R.id.UnInspCount);
                UnInspPer = itemView.findViewById(R.id.UnInspPer);
                UnInspArea = itemView.findViewById(R.id.UnInspArea);
                UnInspSubDesc = itemView.findViewById(R.id.UnInspSubDesc);
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i=getLayoutPosition();
            int Subid = list.get(i).getSubId();
            int RouteId= list.get(i).getRouteId();
            Float background=list.get(i).getBackground();
            Bundle bundle=new Bundle();
            bundle.putFloat("Background",background);
            bundle.putInt("SubID",Subid);
            bundle.putInt("RouteID",RouteId);
            
            AppCompatActivity appCompatActivity = (AppCompatActivity)v.getContext();
            AllBottomSheetFragment allBottomSheetFragment=new AllBottomSheetFragment(appCompatActivity);
            allBottomSheetFragment.show(appCompatActivity.getSupportFragmentManager(),"AllBottomSheetFragment");
            allBottomSheetFragment.setArguments(bundle);

            /*UninspectedCmpBottomSheetFragment uninspectedCmpBottomSheetFragment=new UninspectedCmpBottomSheetFragment(appCompatActivity);
            uninspectedCmpBottomSheetFragment.show(appCompatActivity.getSupportFragmentManager(),"UninspectedCmpBottomSheetFragment");
            uninspectedCmpBottomSheetFragment.setArguments(bundle);*/
        }
    }

}
