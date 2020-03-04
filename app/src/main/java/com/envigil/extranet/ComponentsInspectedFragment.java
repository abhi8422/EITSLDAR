package com.envigil.extranet;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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



public class ComponentsInspectedFragment extends Fragment {
    public static RecyclerView rvComponentInspection;
    EditText TagSearch;
    ImageView ClearSearch;
    SQLiteHelper sqLiteHelper;
    Button Search;
    String scannedResult;
    FloatingActionButton inspectedFAB;
    AlertDialog.Builder builder;
    Context context;
    public ComponentsInspectedFragment(Context context) {
        this.context=context;
    }

    int SubID,RouteId;
    List<ComponentsListPojo> listPojos = new ArrayList<>();


    public ComponentsInspectedFragment(int id,int Rid) {
        // Required empty public constructor
        SubID = id;
        RouteId = Rid;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_view_component, container, false);

//        ComponentDashboard componentDashboard =new ComponentDashboard();
//        int subId=componentDashboard.subId;
        rvComponentInspection = view.findViewById(R.id.inspected_recycler);
        rvComponentInspection.setLayoutManager(new LinearLayoutManager(getContext()));
        TagSearch=view.findViewById(R.id.TagSearch);
        Search=view.findViewById(R.id.search_btn2);


        TagSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.hideSoftInputFromWindow(TagSearch.getWindowToken(), 0);
                    String Tagno= TagSearch.getText().toString();
                    if(Tagno.equals("")){
                        TagSearch.setError("Please Enter a Tag no.");
                    }
                    else{
                        ViewComponentAdapter viewComponentAdapter=new ViewComponentAdapter(sqLiteHelper.getInspectedIDSort(SubID,RouteId,Tagno));
                        rvComponentInspection.setAdapter(viewComponentAdapter);
                        viewComponentAdapter.notifyDataSetChanged();
                    }
                    handled = true;
                }
                return handled;
            }
        });
        TagSearch.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if((event.getRawX()+TagSearch.getPaddingRight()) >= (TagSearch.getRight() - TagSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        TagSearch.setText("");
                        ViewComponentAdapter viewComponentAdapter = new ViewComponentAdapter(sqLiteHelper.getInspectedID(SubID,RouteId));
                        rvComponentInspection.setAdapter(viewComponentAdapter);
                    }
                }
                return false;
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inspectedFAB = view.findViewById(R.id.barcode_fab_inspected);
        inspectedFAB.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(ComponentsInspectedFragment.this);
                integrator.setCaptureActivity(ScannerActivity.class);
                integrator.initiateScan();
            }
        });

       Search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                String Tagno= TagSearch.getText().toString();
                if(Tagno.equals("")){
                    TagSearch.setError("Please Enter a Tag no.");
                }
                else{
                    ViewComponentAdapter viewComponentAdapter=new ViewComponentAdapter(sqLiteHelper.getInspectedIDSort(SubID,RouteId,Tagno));
                    rvComponentInspection.setAdapter(viewComponentAdapter);
                    viewComponentAdapter.notifyDataSetChanged();
                }

            }
        });



       sqLiteHelper = new SQLiteHelper(getActivity());
        ViewComponentAdapter viewComponentAdapter = new ViewComponentAdapter(sqLiteHelper.getInspectedID(SubID,RouteId));
        rvComponentInspection.setAdapter(viewComponentAdapter);
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){
            if (result.getContents() == null){
            }
            else {
                scannedResult = result.getContents();
                TagSearch.setText(scannedResult);
            }
        }
    }

}