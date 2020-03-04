package com.envigil.extranet;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.models.ShowLeaksPojo;

import java.util.List;

public class ShowLeaksAdapter extends RecyclerView.Adapter<ShowLeaksAdapter.ShowLeaksViewHolder> {

    List<ShowLeaksPojo> showLeaksPojos;

    public ShowLeaksAdapter(List<ShowLeaksPojo> leaksPojos) {
        this.showLeaksPojos = leaksPojos;
    }


    @NonNull
    @Override
    public ShowLeaksAdapter.ShowLeaksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_leaks_adapter, parent, false);
        return new ShowLeaksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShowLeaksViewHolder holder, int position) {
        ShowLeaksPojo leaksPojo = showLeaksPojos.get(position);
        holder.tvTagShowNo.setText(leaksPojo.getTagNO());
        holder.tvSubareaShow.setText(leaksPojo.getSubArea());
        holder.tvAreaName.setText(leaksPojo.getAreaName());
        holder.tvComponentShow.setText(leaksPojo.getComponentName());
        holder.tvSizeShow.setText(String.valueOf(leaksPojo.getComponentSize()));
        holder.tvService.setText(leaksPojo.getServiceType());
        holder.tvLeakRateShow.setText(String.valueOf(leaksPojo.getLeakRate()));
        if(leaksPojo.getRepairRate()==0.0){
            holder.tvRepairRateShow.setText("--");
        }else {
            holder.tvRepairRateShow.setText(String.valueOf(leaksPojo.getRepairRate()));
            int leakTypeID = leaksPojo.getLeakTypeID();
            if (leakTypeID == 1){
                holder.tvRepairRateType.setText(" PPM");
                holder.tvLeakRateType.setText(" PPM");
                holder.tvLeakTypeShow.setText("PPM");
            }
            else if (leakTypeID == 2){
                holder.tvRepairRateType.setText(" DPM");
                holder.tvLeakRateType.setText(" DPM");
                holder.tvLeakTypeShow.setText("DPM");
            }
            else {
                holder.tvRepairRateType.setText(" LEL");
                holder.tvLeakRateType.setText(" LEL");
                holder.tvLeakTypeShow.setText("LEL");
            }
        }
        holder.tvRepairTypeShow.setText(leaksPojo.getRepairType());
        holder.tvLeakPathShow.setText(leaksPojo.getLeakPathName());

        String ifCritical = leaksPojo.isLeakCritical();
        if (ifCritical == null){
            holder.tvLeakCritical.setVisibility(View.GONE);
        }
        else {
            holder.tvLeakCritical.setText("Critical");
            holder.tvLeakCritical.setTextColor(Color.RED);
        }

        String ifEssential = leaksPojo.isLeakEssential();
        if ( ifEssential == null){
            holder.tvLeakEssential.setVisibility(View.GONE);
        }
        else {
            holder.tvLeakEssential.setText("Essential");
        }
    }

    @Override
    public int getItemCount() {
        return showLeaksPojos.size();
    }

    public class ShowLeaksViewHolder extends RecyclerView.ViewHolder {
        TextView tvTagShowNo,tvSubareaShow,tvComponentShow,tvSizeShow,tvService,tvLeakTypeShow,tvAreaName,tvLeakRateType,tvRepairRateType;
        TextView tvLeakRateShow,tvRepairRateShow,tvRepairTypeShow,tvLeakPathShow,tvLeakCritical,tvLeakEssential;

        public ShowLeaksViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTagShowNo = itemView.findViewById(R.id.tv_tagNo_show);
            tvSubareaShow = itemView.findViewById(R.id.subarea_show);
            tvComponentShow = itemView.findViewById(R.id.component_show);
            tvSizeShow = itemView.findViewById(R.id.component_size_show);
            tvService = itemView.findViewById(R.id.service_show);
            tvLeakRateShow = itemView.findViewById(R.id.leak_rate_show);
            tvRepairRateShow = itemView.findViewById(R.id.repair_rate_show);
            tvRepairTypeShow = itemView.findViewById(R.id.repair_type_show);
            tvLeakPathShow = itemView.findViewById(R.id.leak_path_show);
            tvLeakTypeShow = itemView.findViewById(R.id.leak_type_show);
            tvLeakCritical = itemView.findViewById(R.id.leak_critical);
            tvLeakEssential = itemView.findViewById(R.id.leak_essential);
            tvAreaName = itemView.findViewById(R.id.tv_areaname_show);
            tvLeakRateType=itemView.findViewById(R.id.LeakRateType);
            tvRepairRateType=itemView.findViewById(R.id.RepairRateType);
        }
    }
}