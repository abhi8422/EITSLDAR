package com.envigil.extranet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.models.GetAllRooutesPojo;

import java.util.ArrayList;
import java.util.List;

public class DownloadedWorkOrderFrag extends Fragment {
    private static final String TAG = "DownloadedWorkOrderFrag";
   public static RecyclerView downloadedrecycler;
   public static SQLiteHelper sqLiteHelper;
    List<GetAllRooutesPojo> SelectAll = new ArrayList<>();
    public static TestRecycler testRecycler;

    public DownloadedWorkOrderFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_downloaded, container, false);
         downloadedrecycler=view.findViewById(R.id.downlodedRecycler);
         downloadedrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
         sqLiteHelper=new SQLiteHelper(getContext());
        testRecycler=new TestRecycler(sqLiteHelper.getAll());
        downloadedrecycler.setAdapter(testRecycler);
         return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sqLiteHelper=new SQLiteHelper(getContext());
        testRecycler=new TestRecycler(sqLiteHelper.getAll());
        downloadedrecycler.setAdapter(testRecycler);
        testRecycler.notifyDataSetChanged();
    }
}
