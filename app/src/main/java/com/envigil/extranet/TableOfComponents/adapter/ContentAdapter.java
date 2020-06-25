package com.envigil.extranet.TableOfComponents.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.envigil.extranet.ComponentReading;
import com.envigil.extranet.R;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.models.ComponentsListPojo;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author yangsanning
 * @ClassName ContentAdapter
 * @Description 一句话概括作用
 * @Date 2019/11/29
 * @History 2019/11/29 author: description:
 */
public class ContentAdapter extends BaseQuickAdapter<ComponentsListPojo, BaseViewHolder> {

    List<ComponentsListPojo> componentsListPojos;
    SQLiteHelper sqLiteHelper;


    public ContentAdapter(List<ComponentsListPojo> gridComponents) {
        super(R.layout.item_content);
        this.componentsListPojos = gridComponents;

    }

    @Override
    protected void convert(final BaseViewHolder helper, ComponentsListPojo item) {
        final int position = helper.getAdapterPosition();
//        if (position % 2 == 1){
//            helper.itemView.setBackgroundColor(Color.parseColor("#DAF5BC"));
//        }

        ComponentsListPojo contentColumns = componentsListPojos.get(position);
        String ifInspected = String.valueOf(contentColumns.getInspected());
        if (ifInspected.equals("true") || ifInspected.equals("1")){
            helper.setImageResource(R.id.first_column_item_name,R.drawable.ic_check_circle);
        }
        helper.setText(R.id.content_item_code,
                "Tag: " + contentColumns.getInvTag() + "\n" +
                        "Component: " + contentColumns.getCompName() + "\n" +
                "Type: " + contentColumns.getCompTypeName());
        helper.setText(R.id.content_item_zdf,String.valueOf(contentColumns.getInvSize()));
        helper.setText(R.id.content_item_lb,
                "Area: " + contentColumns.getAreaName() + "\n" +
                "Subarea: " + contentColumns.getSubName() + "\n" +
                "Location: " + contentColumns.getComponentLocation());
        helper.setText(R.id.content_item_cje,String.valueOf(contentColumns.getBackread()));
        helper.setText(R.id.content_item_reading,String.valueOf(contentColumns.getInvReading()));

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = helper.getAdapterPosition();
                int compId = componentsListPojos.get(pos).getCompID();
                int subId = componentsListPojos.get(pos).getSubID();
                int routeId = componentsListPojos.get(pos).getRouteId();
                int invId = componentsListPojos.get(pos).getInvID();
                boolean grid = true;

                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(appCompatActivity, ComponentReading.class).putExtra("CompId",compId).putExtra("SubId",subId).
                        putExtra("RouteID",routeId).putExtra("InvID",invId).putExtra("Grid",grid);
                appCompatActivity.startActivity(intent);
                appCompatActivity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return componentsListPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
