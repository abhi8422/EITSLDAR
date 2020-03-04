package com.envigil.extranet;



import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteUnInspectedFragment extends Fragment {
RouteUninspectedAdapter routeUninspectedAdapter;
    public static RecyclerView UnInspectedRV;
    public int routeId;
public static boolean oncreate_flag;

    public RouteUnInspectedFragment(int id) {
        // Required empty public constructor
        routeId = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_route_un_inspected, container, false);

         UnInspectedRV = view.findViewById(R.id.UnInspectedRV);

        UnInspectedRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        SQLiteHelper sqLiteHelper=new SQLiteHelper(getActivity());
        routeUninspectedAdapter=new RouteUninspectedAdapter(sqLiteHelper.getSubareasUnInspected(routeId));
        UnInspectedRV.setAdapter(routeUninspectedAdapter);

oncreate_flag=true;

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        oncreate_flag=false;
    }
}
