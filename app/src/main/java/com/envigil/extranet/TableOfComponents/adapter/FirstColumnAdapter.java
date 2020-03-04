package com.envigil.extranet.TableOfComponents.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.envigil.extranet.R;
import com.envigil.extranet.models.ComponentsListPojo;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author yangsanning
 * @ClassName FirstColumnAdapter
 * @Description 一句话概括作用
 * @Date 2019/12/02
 * @History 2019/12/02 author: description:
 */
public class FirstColumnAdapter extends BaseQuickAdapter<ComponentsListPojo, BaseViewHolder> {

    List<ComponentsListPojo> componentsListPojos = new ArrayList<>();
    int subID,routeID;

    public FirstColumnAdapter(List<ComponentsListPojo> gridComponents) {
        super(R.layout.item_first_column);
        this.componentsListPojos = gridComponents;
    }



    @Override
    protected void convert(BaseViewHolder helper, ComponentsListPojo item) {
        int position = helper.getAdapterPosition();
//        if (position % 2 == 1){
//            helper.itemView.setBackgroundColor(Color.parseColor("#DAF5BC"));
//        }
        ComponentsListPojo tagColumn = componentsListPojos.get(position);
        String ifInspected = String.valueOf(tagColumn.getInspected());
        if (ifInspected.equals("true") || ifInspected.equals("1")){
            helper.setImageResource(R.id.first_column_item_name,R.drawable.ic_check_circle);
        }
    }

    @Override
    public int getItemCount() {
        return componentsListPojos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
