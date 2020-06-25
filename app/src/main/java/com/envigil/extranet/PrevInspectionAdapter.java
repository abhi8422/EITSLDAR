package com.envigil.extranet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.AddInspectionDate.AddInspectionDate;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.models.UploadRouteData;

import java.util.List;

import timber.log.Timber;

public class PrevInspectionAdapter extends RecyclerView.Adapter<PrevInspectionAdapter.InspectionViewHolder> {

    public List<PrevInspectionModel> prevInspectionModels;
    Context context;

    public PrevInspectionAdapter(List<PrevInspectionModel> prevInspectionModelArrayList) {this.prevInspectionModels = prevInspectionModelArrayList; }

    @NonNull
    @Override
    public PrevInspectionAdapter.InspectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.prev_inspection_adapter, parent, false);
        InspectionViewHolder viewHolder = new InspectionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InspectionViewHolder viewHolder, int position) {
        PrevInspectionModel inspectionModel = prevInspectionModels.get(position);
        viewHolder.tvinspid.setText(String.valueOf(inspectionModel.getInspectionID()));
        viewHolder.tvEmployeeFirstName.setText(inspectionModel.getEmployeeFirstName());
        viewHolder.tvStartTimePrev.setText(inspectionModel.getStartTime());
        viewHolder.tvEndTimePrev.setText(inspectionModel.getEndTime());
        viewHolder.tvInspDate.setText(inspectionModel.getInspectionDate());
    }

    @Override
    public int getItemCount() {
        return prevInspectionModels.size();
    }

    public class InspectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvInspDate, tvEmployeeFirstName, tvStartTimePrev, tvEndTimePrev,tvinspid;
        CardView Card;

        public InspectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInspDate = itemView.findViewById(R.id.tv_insp_date);
            tvEmployeeFirstName = itemView.findViewById(R.id.tv_employee_first_name);
            tvStartTimePrev = itemView.findViewById(R.id.tv_start_time_previn);
            tvEndTimePrev = itemView.findViewById(R.id.tv_end_time_previn);
            tvinspid=itemView.findViewById(R.id.tv_inspection_id);
            Card = itemView.findViewById(R.id.Card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i =getLayoutPosition();
            int InspID=prevInspectionModels.get(i).getInspectionID();
            Timber.e("Uploading Route To Inspection="+InspID);
            boolean partial=PrevInspection.partial;
            Bundle bundle=new Bundle();
            bundle.putInt("InspID",InspID);
            bundle.putBoolean("Partially",partial);
            if( UploadRouteData.routeinspdate.equals(prevInspectionModels.get(i).getInspectionDate())){
                AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                InspectionUploadBottomSheetFragment  inspectionUploadBottomSheetFragment =new InspectionUploadBottomSheetFragment(v.getContext());
                inspectionUploadBottomSheetFragment.show(appCompatActivity.getSupportFragmentManager(),"InspectionUploadBottomSheet");
                inspectionUploadBottomSheetFragment.setArguments(bundle);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Date does not match");
                builder.setMessage("Date of Route Inspection and Inspection Date is not matching please select correct Inspection");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

        }
    }
}
