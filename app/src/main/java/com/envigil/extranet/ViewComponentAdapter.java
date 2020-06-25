package com.envigil.extranet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.models.ComponentsListPojo;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ViewComponentAdapter extends RecyclerView.Adapter<ViewComponentAdapter.ViewHolder> {
    private static final String TAG = "ViewComponentAdapter";

    List<ComponentsListPojo> listPojos = new ArrayList<>();
    SQLiteHelper sqLiteHelper;

    public ViewComponentAdapter(List<ComponentsListPojo> listPojos) {
        this.listPojos = listPojos;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_component_adapter, parent, false);
        if (listPojos.isEmpty()) {
            showToast(parent.getContext());
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        ComponentsListPojo componentsListPojo = listPojos.get(position);
        holder.CompoTagInspected.setText(componentsListPojo.getInvTag());
        holder.InspectedCompoName.setText(componentsListPojo.getCompName());
        holder.CompoSize.setText(Float.toString(componentsListPojo.getInvSize()));
        holder.Location.setText(componentsListPojo.getInvLocation());
        if(componentsListPojo.getBackread()==0.0)
        {
            holder.InspBackground.setText("--");
        }else {
            holder.InspBackground.setText(String.valueOf(componentsListPojo.getBackread()));
        }
        if(componentsListPojo.getReading()==0.0)
        {
            holder.ReadingInspComp.setText("--");
        }else {
            holder.ReadingInspComp.setText(String.valueOf(componentsListPojo.getReading()));
        }

        String inspected=componentsListPojo.getInspected();
        if (inspected.equals("true")|| inspected.equals("1")){
            holder.InsptrueCompIn.setVisibility(View.VISIBLE);
        }
        else{
            holder.InsptrueCompIn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listPojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView CompoTagInspected, InspectedCompoName, CompoSize, ReadingInspComp,Location,InspBackground;
        ImageView InsptrueCompIn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CompoTagInspected = itemView.findViewById(R.id.compo_tag_inspected);
            InspectedCompoName = itemView.findViewById(R.id.inspected_compo_name);
            CompoSize = itemView.findViewById(R.id.comp_size);
            Location=itemView.findViewById(R.id.Location);
            InspBackground = itemView.findViewById(R.id.InspBackground);
            InsptrueCompIn = itemView.findViewById(R.id.InspTrueCompIn);
            ReadingInspComp = itemView.findViewById(R.id.ReadingInspComp);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            sqLiteHelper = new SQLiteHelper(view.getContext());
            int compId = listPojos.get(pos).getCompID();
            Timber.e("Opneing Comp for CompReading CompID="+compId);
            String compNAME = listPojos.get(pos).getCompName();
            float size = listPojos.get(pos).getInvSize();
            float reading=listPojos.get(pos).getReading();
            String location = listPojos.get(pos).getInvLocation();
            String inspected = listPojos.get(pos).getInspected();
            int SubID=listPojos.get(pos).getSubID();
            int RouteId = listPojos.get(pos).getRouteId();
            int InvId = listPojos.get(pos).getInvID();

            Bundle InspectedBundle = new Bundle();
            InspectedBundle.putInt("CompId",compId);
            InspectedBundle.putInt("SubId",SubID);
            InspectedBundle.putInt("RouteID",RouteId);
            InspectedBundle.putInt("InvID",InvId);
            InspectedBundle.putFloat("Reading",reading);
            InspectedBundle.putString("CompName",compNAME);
            InspectedBundle.putFloat("Size",size);
            InspectedBundle.putString("Location",location);
            InspectedBundle.putString("IF-inspected",inspected);

            AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
            Intent intent=new Intent(appCompatActivity, ComponentReading.class).putExtra("CompId",compId).
                    putExtra("SubId",SubID).putExtra("RouteID",RouteId).putExtra("InvID",InvId);
            appCompatActivity.startActivity(intent);
            appCompatActivity.finish();

            /*AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
            ComponentBottomFragment componentBottomFragment = new ComponentBottomFragment();
            componentBottomFragment.show(appCompatActivity.getSupportFragmentManager(), "InspectedComponent");
            componentBottomFragment.setArguments(InspectedBundle);*/
        }
    }

    public void showToast(Context context) {
        Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show();
    }
}
