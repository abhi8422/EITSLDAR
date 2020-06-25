package com.envigil.extranet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.models.ComponentAttributes;

import java.util.List;

public class RouteRecyclerAdapter extends RecyclerView.Adapter<RouteRecyclerAdapter.RouteRecyclerViewHolder> {
    List<ComponentAttributes> strings;

    public RouteRecyclerAdapter(List<ComponentAttributes> strings) {
        this.strings = strings;
    }

    @NonNull
    @Override
    public RouteRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_comp_item, parent, false);
        return new RouteRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteRecyclerViewHolder holder, int position) {
        ComponentAttributes componentAttributes=strings.get(position);
        holder.id.setText(componentAttributes.getId());
        holder.type.setText(componentAttributes.getType());
        holder.size.setText(componentAttributes.getSize());
        holder.status.setText(componentAttributes.getStatus());
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class RouteRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id,type,size,status;
        public RouteRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.comp_tag_id);
            type=itemView.findViewById(R.id.comp_type);
            size=itemView.findViewById(R.id.comp_size);
            status=itemView.findViewById(R.id.inspection_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            ComponentBottomFragment componentReadingFragment=new ComponentBottomFragment();
            componentReadingFragment.show(activity.getSupportFragmentManager(),"ComponentReadingFragment");
        }
    }
}
