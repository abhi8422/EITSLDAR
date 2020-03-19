package com.envigil.extranet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.envigil.extranet.AddInspectionDate.AddInspectionDate;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.models.GetAllRooutesPojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteRouteAdapter extends RecyclerView.Adapter<DeleteRouteAdapter.DeleteViewHolder> {
    List<GetAllRooutesPojo> list;
    private SQLiteHelper sqLiteHelper;
    List<String> LeakImagePath = new ArrayList<>();
    Context context;

    public DeleteRouteAdapter(List<GetAllRooutesPojo> getAllRooutesPojoList, Context context) {
        this.list = getAllRooutesPojoList;
        this.context = context;
    }

    @NonNull
    @Override
    public DeleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_delete_route_adapter, parent, false);
        return new DeleteViewHolder(view);
    }

    public void onBindViewHolder(@NonNull DeleteViewHolder holder, int position) {

        GetAllRooutesPojo getAllRooutesPojo = list.get(position);
        holder.workOrder.setText("Work Order:" + String.valueOf(getAllRooutesPojo.getWorkID()));
        holder.title.setText(getAllRooutesPojo.getTitle());
        holder.routeid.setText(String.valueOf(getAllRooutesPojo.getRouteID()));
        holder.facilitydes.setText(getAllRooutesPojo.getFacility());
        holder.ruledes.setText(getAllRooutesPojo.getRuleNumber());
        if (getAllRooutesPojo.getInspected().equals("true") || getAllRooutesPojo.getInspected().equals("1")) {
            holder.Insptrue.setVisibility(View.VISIBLE);
        } else {
            holder.Insptrue.setVisibility(View.GONE);
        }
        String DAEP = getAllRooutesPojo.isDAEP();
        if (DAEP.equals("1") || DAEP.equals("true")) {
            holder.RuleTypeName.setText("DAEP");
        } else {
            holder.RuleTypeName.setText("Manual");
        }
        holder.DownDate.setText(getAllRooutesPojo.getDownDate());
        if (getAllRooutesPojo.getBackground() == 0.0f) {
            holder.BackgroundRoute.setText("--");
        } else {
            holder.BackgroundRoute.setText(String.valueOf(getAllRooutesPojo.getBackground()));
        }
        holder.RouteTotalCnt.setText(String.valueOf(getAllRooutesPojo.getCount()));
        String per = String.format("%.2f", getAllRooutesPojo.getPercent());
        if (per.equals("NaN")) {
            holder.InspPer.setText("00.0%");
        } else {
            holder.InspPer.setText(String.format("%.2f", getAllRooutesPojo.getPercent()) + " %");
        }
        if (!getAllRooutesPojo.getInspdate().isEmpty()) {
            holder.insp_date_layout.setVisibility(View.VISIBLE);
            holder.inspectionDate.setText(getAllRooutesPojo.getInspdate());
        }
        //h
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DeleteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, routeid, facilitydes, ruledes, inspected, workOrder, RuleTypeName, BackgroundRoute, DownDate, RouteTotalCnt, InspPer, inspectionDate;
        ImageView Insptrue;
        LinearLayout insp_date_layout;

        public DeleteViewHolder(@NonNull final View itemView) {
            super(itemView);
            workOrder = itemView.findViewById(R.id.delworkorder);
            insp_date_layout = itemView.findViewById(R.id.delinsp_date_layout);
            inspectionDate = itemView.findViewById(R.id.delinspection_date);
            title = itemView.findViewById(R.id.deltitle);
            routeid = itemView.findViewById(R.id.delrouteid);
            facilitydes = itemView.findViewById(R.id.delfacides);
            ruledes = itemView.findViewById(R.id.delruledes);
//            inspected=itemView.findViewById(R.id.inspected_status);
            RuleTypeName = itemView.findViewById(R.id.delRuleTypeName);
            BackgroundRoute = itemView.findViewById(R.id.delBackgroundRoute);
            DownDate = itemView.findViewById(R.id.deldownload_date);
            RouteTotalCnt = itemView.findViewById(R.id.delRouteTotalComp);
            InspPer = itemView.findViewById(R.id.delinspection_percent);
            Insptrue = itemView.findViewById(R.id.delInspTrue);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            AlertDialog alertDialog = null;
            int pos = getAdapterPosition();
            sqLiteHelper = new SQLiteHelper(v.getContext());
            final int Id = list.get(pos).getRouteID();
            int workId = list.get(pos).getWorkID();
            String DAEP = list.get(pos).isDAEP();
            final String RouteName = list.get(pos).getTitle();
            float BackReading = sqLiteHelper.getBackground(Id);
            String inspdate = list.get(pos).getInspdate();
            LeakImagePath = sqLiteHelper.getAllLeakImagePath(Id);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
            alertDialogBuilder.setMessage("Are you sure you want to delete "+RouteName+" Route?");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (!LeakImagePath.isEmpty()) {
                                for (int i = 0; i < LeakImagePath.size(); i++) {
                                    if (LeakImagePath.get(i) != null) {
                                        File file = new File(LeakImagePath.get(i));
                                        if (file.exists()) {
                                            file.delete();
                                            System.out.println("ImageDeleted");
                                        }
                                    }
                                }
                            }
                            sqLiteHelper.deleteLeakRepair(Id);
                            sqLiteHelper.deleteLeaks(Id);
                            sqLiteHelper.deleteInventory(Id);
                            sqLiteHelper.deleteSubAreas(Id);
                            sqLiteHelper.deleteRoutesConfig(Id);

                            AlertDialog.Builder alertdialogBuilder= new AlertDialog.Builder(v.getRootView().getContext());
                            alertdialogBuilder.setMessage("Route "+RouteName+" has been Deleted !");
                            alertdialogBuilder.setCancelable(false);
                            alertdialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DeleteRouteAdapter deleteRouteAdapter=new DeleteRouteAdapter(sqLiteHelper.getAll(),context);
                                    DeleteRoute.rvDeleteRoute.setAdapter(deleteRouteAdapter);
                                    deleteRouteAdapter.notifyDataSetChanged();
                                }
                            });
                            AlertDialog alertDialog1= alertdialogBuilder.create();
                            alertDialog1.show();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }
}
