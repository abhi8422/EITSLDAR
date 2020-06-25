package com.envigil.extranet;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.models.ComponentsListPojo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static androidx.core.content.ContextCompat.getSystemService;


public class ComponentsUninspectedFragment extends Fragment {
    public static RecyclerView rvCompoUninspected;
    EditText SearchTag;
    String tag;
    Button SearchBtn;
    FloatingActionButton uninspectedFAB;
    String uninspectedResult;
    SQLiteHelper sqLiteHelper ;

    UninspectedCompoAdapter uninspectedCompoAdapter;


    List<ComponentsListPojo> listPojos = new ArrayList<>();

    int SubId,RouteId;



    public ComponentsUninspectedFragment(int id,int Rid) {
        // Required empty public constructor
        SubId = id;
        RouteId=Rid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_view_component_uninspected, container, false);
        Timber.e("Showing CompInspected frag");
        SearchTag=view.findViewById(R.id.SearchUnInspected);
        SearchBtn=view.findViewById(R.id.search_btn);
        uninspectedFAB = view.findViewById(R.id.barcode_fab_uninspected);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sqLiteHelper= new SQLiteHelper(getActivity());
        rvCompoUninspected=view.findViewById(R.id.uninspected_recycler);
        rvCompoUninspected.setLayoutManager(new LinearLayoutManager(getContext()));
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                String Tagno = SearchTag.getText().toString();
                Timber.e("(Uninspected)Searching Comp Tag="+Tagno);

                while(Tagno.indexOf("0") == 0){
                    Tagno = Tagno.substring(1);
                }
                if (Tagno.equals("")){
                    SearchTag.setError("Please Enter a Tag no.");
                }
                else {
//                    Toast.makeText(getContext(), "TAG :: " + TAG, Toast.LENGTH_SHORT).show();
                    UninspectedCompoAdapter uninspectedCompoAdapter = new UninspectedCompoAdapter(sqLiteHelper.getUnInspectedIDSort(SubId, RouteId, Tagno));
                    rvCompoUninspected.setAdapter(uninspectedCompoAdapter);
                }

            }
        });
        SearchTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.hideSoftInputFromWindow(TagSearch.getWindowToken(), 0);
                    String Tagno= SearchTag.getText().toString();
                    while(Tagno.indexOf("0") == 0){
                        Tagno = Tagno.substring(1);
                    }
                    if(Tagno.equals("")){
                        SearchTag.setError("Please Enter a Tag no.");
                    }
                    else{
                        UninspectedCompoAdapter uninspectedCompoAdapter = new UninspectedCompoAdapter(sqLiteHelper.getUnInspectedIDSort(SubId, RouteId, Tagno));
                        rvCompoUninspected.setAdapter(uninspectedCompoAdapter);
                    }
                    handled = true;
                }
                return handled;
            }
        });
        SearchTag.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if((event.getRawX()+SearchTag.getPaddingRight()) >= (SearchTag.getRight() - SearchTag.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        SearchTag.setText("");
                        UninspectedCompoAdapter uninspectedCompoAdapter = new UninspectedCompoAdapter(sqLiteHelper.getUnInspectedID(SubId,RouteId));
                        rvCompoUninspected.setAdapter(uninspectedCompoAdapter);
                    }
                }
                return false;
            }
        });





        UninspectedCompoAdapter uninspectedCompoAdapter = new UninspectedCompoAdapter(sqLiteHelper.getUnInspectedID(SubId,RouteId));
//        Toast.makeText(getContext(), "UnInspected Fragment :: "+RouteId, Toast.LENGTH_SHORT).show();
        rvCompoUninspected.setAdapter(uninspectedCompoAdapter);

        //Barcode scanning.
        uninspectedFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.e("(Unispected)Clik on Barcode scan");
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(ComponentsUninspectedFragment.this);
                integrator.setCaptureActivity(ScannerActivity.class);
                integrator.initiateScan();
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){
            if (result.getContents() == null){
//                Toast.makeText(getContext(),"Scan was cancelled",Toast.LENGTH_SHORT).show();
            }
            else {
                uninspectedResult = result.getContents();
                while(uninspectedResult.indexOf("0") == 0){
                    uninspectedResult = uninspectedResult.substring(1);
                }
//                Toast.makeText(getContext(),uninspectedResult,Toast.LENGTH_SHORT).show();
                SearchTag.setText(uninspectedResult);
            }
        }
    }
}