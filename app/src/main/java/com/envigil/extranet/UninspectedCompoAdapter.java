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
import java.util.List;

import timber.log.Timber;

public class UninspectedCompoAdapter extends RecyclerView.Adapter<UninspectedCompoAdapter.UnViewHolder> {

    List<ComponentsListPojo> listPojos;
    SQLiteHelper sqLiteHelper;

    public UninspectedCompoAdapter(List<ComponentsListPojo> listPojos) {
        this.listPojos = listPojos;
    }



    @NonNull
    @Override
    public UnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uninspected_compo_adapter, parent, false);
        if (listPojos.isEmpty()) {
            showToast(parent.getContext());
        }
        return new UnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnViewHolder holder, int position) {
        ComponentsListPojo componentsListPojo = listPojos.get(position);
        holder.UninspectedCompoTagInspected.setText(String.valueOf(componentsListPojo.getInvTag()));
        holder.UnInspectedCompoName.setText(String.valueOf(componentsListPojo.getCompName()));
//        holder.UnInspectedCompoName.setText("default");
        holder.UninspectedCompoSize.setText(Float.toString(componentsListPojo.getInvSize()));
        holder.UnInspLocation.setText(componentsListPojo.getInvLocation());
        holder.UnInspBackground.setText(String.valueOf(componentsListPojo.getBackread()));

        String uninspected=componentsListPojo.getInspected();
        if (uninspected.equals("true")|| uninspected.equals("1")){
            holder.InsptrueCompUn.setVisibility(View.VISIBLE);

        }
        else{
            holder.InsptrueCompUn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listPojos.size();
    }

    public class UnViewHolder extends RecyclerView.ViewHolder {

        TextView UninspectedCompoTagInspected, UnInspectedCompoName, UninspectedCompoSize,UnInspLocation,UnInspBackground;
        ImageView InsptrueCompUn;

        public UnViewHolder(@NonNull View itemView) {
            super(itemView);
            UninspectedCompoTagInspected = itemView.findViewById(R.id.compo_tag_uninspected);
            UnInspectedCompoName = itemView.findViewById(R.id.uninspected_compo_name);
            UninspectedCompoSize = itemView.findViewById(R.id.uninspected_comp_size);
            InsptrueCompUn = itemView.findViewById(R.id.InspTrueCompUn);
            UnInspLocation = itemView.findViewById(R.id.UnInspLocation);
            UnInspBackground = itemView.findViewById(R.id.UnInspBackground);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    sqLiteHelper = new SQLiteHelper(view.getContext());
                    int CompId = listPojos.get(pos).getCompID();
                    Timber.e("(Uninsp)Opening Component ComID="+CompId);
                    String uncompName = listPojos.get(pos).getCompName();
                    float unsize = listPojos.get(pos).getInvSize();
                    String unlocation = listPojos.get(pos).getInvLocation();
                    String uninspected = listPojos.get(pos).getInspected();
                    int SubId = listPojos.get(pos).getSubID();
                    int RouteId = listPojos.get(pos).getRouteId();
                    int InvId = listPojos.get(pos).getInvID();

                    Bundle unInspectedbundle = new Bundle();
                    unInspectedbundle.putInt("CompId",CompId);
                    unInspectedbundle.putString("UnCompName",uncompName);
                    unInspectedbundle.putFloat("UnSize",unsize);
                    unInspectedbundle.putString("UnLocation",unlocation);
                    unInspectedbundle.putString("IF-uninspected",uninspected);
                    unInspectedbundle.putInt("SubId",SubId);
                    unInspectedbundle.putInt("RouteID",RouteId);
                    unInspectedbundle.putInt("InvID",InvId);
                    AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                    Intent intent=new Intent(appCompatActivity, ComponentReading.class).putExtra("CompId",CompId).
                            putExtra("SubId",SubId).putExtra("RouteID",RouteId).putExtra("InvID",InvId);
                    appCompatActivity.startActivity(intent);
                    appCompatActivity.finish();
                   /* ComponentBottomFragment componentBottomFragment = new ComponentBottomFragment();
                    componentBottomFragment.show(appCompatActivity.getSupportFragmentManager(), "UnInspectedComponent");
                    componentBottomFragment.setArguments(unInspectedbundle);*/

                }
            });
        }
    }

    public void showToast(Context context) {
        Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show();
    }
}
