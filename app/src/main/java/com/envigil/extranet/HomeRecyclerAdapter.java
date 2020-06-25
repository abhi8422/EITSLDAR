package com.envigil.extranet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.DownloadedRecyclerViewHolder> {
List<Downlodedpojo> list;

    public HomeRecyclerAdapter(List<Downlodedpojo> list) {
        this.list = list;
    }

    public HomeRecyclerAdapter() {

    }

    @NonNull
    @Override
    public DownloadedRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloded_item,parent,false);
        return new DownloadedRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadedRecyclerViewHolder holder, int position) {
        Downlodedpojo downlodedpojo =list.get(position);
        holder.title.setText(downlodedpojo.getTitle());
        holder.workorderdes.setText(downlodedpojo.getWorkorderdes());
        holder.facilitydes.setText(downlodedpojo.getFacilitydes());
        holder.ruledes.setText(downlodedpojo.getRuledes());
        holder.downloaddes.setText(downlodedpojo.getDownloaddes());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DownloadedRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title,workorderdes,facilitydes,ruledes,downloaddes;
        public DownloadedRecyclerViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            workorderdes = itemView.findViewById(R.id.routeid);
            facilitydes = itemView.findViewById(R.id.facides);
            ruledes = itemView.findViewById(R.id.ruledes);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
            DownloadedBottomSheetFrag downloadedBottomSheetFrag =new DownloadedBottomSheetFrag();
            downloadedBottomSheetFrag.show(appCompatActivity.getSupportFragmentManager(),"DownloadedBottomSheet");
        }
    }
}
