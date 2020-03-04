package com.envigil.extranet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.models.GetAllRooutesPojo;

import java.util.List;

public class TestRecycler extends RecyclerView.Adapter<TestRecycler.DownloadedRecyclerViewHolder> {

List<GetAllRooutesPojo> list;
    private SQLiteHelper sqLiteHelper;


    public TestRecycler(List<GetAllRooutesPojo> list) {
        this.list = list;
    }

    public TestRecycler() {
    }

    @NonNull
    @Override
    public DownloadedRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloded_item,parent,false);

        return new DownloadedRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadedRecyclerViewHolder holder, int position) {

        GetAllRooutesPojo getAllRooutesPojo= list.get(position);
        holder.workOrder.setText("Work Order:"+String.valueOf(getAllRooutesPojo.getWorkID()));
        holder.title.setText(getAllRooutesPojo.getTitle());
        holder.routeid.setText(String.valueOf(getAllRooutesPojo.getRouteID()));
        holder.facilitydes.setText(getAllRooutesPojo.getFacility());
        holder.ruledes.setText(getAllRooutesPojo.getRuleNumber());
        if(getAllRooutesPojo.getInspected().equals("true")||getAllRooutesPojo.getInspected().equals("1")){
            holder.Insptrue.setVisibility(View.VISIBLE);
        }else {
            holder.Insptrue.setVisibility(View.GONE);
        }
        String DAEP =getAllRooutesPojo.isDAEP();
        if (DAEP.equals("1")||DAEP.equals("true")){
            holder.RuleTypeName.setText("DAEP");
        }
        else{
            holder.RuleTypeName.setText("Manual");
        }
        holder.DownDate.setText(getAllRooutesPojo.getDownDate());
        if(getAllRooutesPojo.getBackground()==0.0f){
            holder.BackgroundRoute.setText("--");
        }else {
            holder.BackgroundRoute.setText(String.valueOf(getAllRooutesPojo.getBackground()));
        }
        holder.RouteTotalCnt.setText(String.valueOf(getAllRooutesPojo.getCount()));
        String per=String.format("%.2f",getAllRooutesPojo.getPercent());
        if(per.equals("NaN")){
            holder.InspPer.setText("00.0%");
        }else
        {
            holder.InspPer.setText(String.format("%.2f",getAllRooutesPojo.getPercent()) + " %");
        }
        if(!getAllRooutesPojo.getInspdate().isEmpty()){
            holder.insp_date_layout.setVisibility(View.VISIBLE);
            holder.inspectionDate.setText(getAllRooutesPojo.getInspdate());
        }
        //holder.inspected.setText(getAllRooutesPojo.getInspected());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DownloadedRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title,routeid,facilitydes,ruledes,inspected,workOrder,RuleTypeName,BackgroundRoute,DownDate,RouteTotalCnt,InspPer,inspectionDate;
        ImageView Insptrue;
        LinearLayout insp_date_layout;
        public DownloadedRecyclerViewHolder(@NonNull final View itemView) {
            super(itemView);
            workOrder=itemView.findViewById(R.id.workorder);
            insp_date_layout=itemView.findViewById(R.id.insp_date_layout);
            inspectionDate=itemView.findViewById(R.id.inspection_date);
            title = itemView.findViewById(R.id.title);
            routeid = itemView.findViewById(R.id.routeid);
            facilitydes = itemView.findViewById(R.id.facides);
            ruledes = itemView.findViewById(R.id.ruledes);
//            inspected=itemView.findViewById(R.id.inspected_status);
            RuleTypeName =itemView.findViewById(R.id.RuleTypeName);
            BackgroundRoute= itemView.findViewById(R.id.BackgroundRoute);
            DownDate = itemView.findViewById(R.id.download_date);
            RouteTotalCnt = itemView.findViewById(R.id.RouteTotalComp);
            InspPer = itemView.findViewById(R.id.inspection_percent);
            Insptrue = itemView.findViewById(R.id.InspTrue);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            sqLiteHelper=new SQLiteHelper(v.getContext());
            int Id = list.get(pos).getRouteID();
            int workId=list.get(pos).getWorkID();
            String DAEP = list.get(pos).isDAEP();
            String RouteName=list.get(pos).getTitle();
            float BackReading =sqLiteHelper.getBackground(Id);
            String inspdate=list.get(pos).getInspdate();
            Bundle bundle =new Bundle();
            bundle.putString("Inspected",list.get(pos).getInspected());
            bundle.putFloat("BackgroundReading",BackReading);
            bundle.putInt("RouteId",Id);
            bundle.putInt("workId",workId);
            bundle.putString("DAEP",DAEP);
            bundle.putString("RouteName",RouteName);
            bundle.putString("InspDate",inspdate);
            AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
            DownloadedBottomSheetFrag downloadedBottomSheetFrag =new DownloadedBottomSheetFrag();
            downloadedBottomSheetFrag.show(appCompatActivity.getSupportFragmentManager(),"DownloadedBottomSheet");
            downloadedBottomSheetFrag.setArguments(bundle);


        }
    }
    public void showToast(Context context)
    {
        Toast.makeText(context,"List is Empty",Toast.LENGTH_SHORT).show();
    }

}
