package com.envigil.extranet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.WebService.WebService;
import com.envigil.extranet.models.Routes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AvailableWorkOrderFrag extends Fragment  {
    public static  RecyclerView availableRecyclerView;
    AvailableArrayAdapter availableArrayAdapter;
    EditText searchView;
    static int workOrderID;
     List<Routes> routes=new ArrayList<>();
     List<Routes> routesfilterlist;
    View view;
    public static  ProgressBar progressBar;
    public static TextView loading_txt_view;
    public static TextView downaload_txt_view;
    public static TextView downaloading_txt_view;
    Context context;
    public AvailableWorkOrderFrag(Context context) {
        // Required empty public constructor
        this.context=context;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_available, container, false);
        availableRecyclerView= view.findViewById(R.id.availableRecycler);
        availableRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView=view.findViewById(R.id.available_search);
        progressBar=view.findViewById(R.id.progress_bar);
        loading_txt_view=view.findViewById(R.id.progress_txt);
        downaload_txt_view =view.findViewById(R.id.downlaod_progress_txt);
        downaloading_txt_view=view.findViewById(R.id.downlaoding_progress_txt);

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    if(searchView.getText().toString().equals("")){
                        searchView.setError("Please Enter WorkOrder Number");
                        ClearAdapter();
                    }else {
                        ClearAdapter();
                        workOrderID= Integer.parseInt(searchView.getText().toString());
                        new ShowAvailableData(workOrderID,getActivity()).execute();
                    }
                    handled = true;
                }
                return handled;
            }
        });
        searchView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                 if((event.getRawX()+searchView.getPaddingRight()) >= (searchView.getRight() - searchView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        if(searchView.getText().toString().equals("")){
                            searchView.setError("Please Enter WorkOrder Number");
                            ClearAdapter();

                        }else {
                            searchView.setText("");
                            ClearAdapter();
                            progressBar     .setVisibility(View.GONE);
                            loading_txt_view.setVisibility(View.GONE);
                        }
                    }
                    }
                return false;
            }
        });

        return view;
    }
    //searchfilter edittext
   /* public static void changeTextButton(){
        btn.setVisibility(View.VISIBLE);
        btn.setText("Download The Routes");
    }

    public static void GoanTextButton(){
        btn.setVisibility(View.GONE);
    }
    private void filter(String toString) {
        routesfilterlist=new ArrayList<>();
        for(Routes item:routes){
            if(String.valueOf(item.getRouteID()).contains(toString)){
                    routesfilterlist.add(item);
            }
        }
        availableArrayAdapter=new AvailableArrayAdapter(routesfilterlist,routes,getActivity());
        availableRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        availableRecyclerView.setAdapter(availableArrayAdapter);
        //MultiSelectRecycler
        */
   /*availableRecyclerView.setItemAnimator(new DefaultItemAnimator());
        availableRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), availableRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else
                    Toast.makeText(getActivity(), "Details Page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_routes_list = new ArrayList<>();
                    isMultiSelect = true;
                }
                if (mActionMode == null) {
                    mActionMode = getActivity().startActionMode(mActionModeCallback);
                }
                multi_select(position);
            }
        }));*/
   /*
    }*/
    //MultiSelectRecycler
    /*private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
           return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_routes_list = new ArrayList<>();
            refreshAdapter();
        }
    };
    private void multi_select(int position) {
        if (multiselect_routes_list.contains(routesfilterlist.get(position)))
            multiselect_routes_list.remove(routesfilterlist.get(position));
        else
            multiselect_routes_list.add(routesfilterlist.get(position));
        if (multiselect_routes_list.size() > 0)
            mActionMode.setTitle("" + multiselect_routes_list.size());
        else
            mActionMode.setTitle("");
        refreshAdapter();
    }

    private void refreshAdapter() {
        availableArrayAdapter.routesFilterList =routesfilterlist;
        availableArrayAdapter.notifyDataSetChanged();
    }*/

    /*@SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.animator.animate));
                searchView.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if(searchView.getText().toString().equals("")){
                    searchView.setError("Please Enter WorkOrder Number");
                    ClearAdapter();
                }else {
                    workOrderID= Integer.parseInt(searchView.getText().toString());
                    new ShowAvailableData(workOrderID,getActivity()).execute();
                    progressDialog=new ProgressDialog(getContext());
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.search_route_progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                    progressDialog.setCancelable(false);
                }
    }*/


    public  class ShowAvailableData extends AsyncTask{
        int workOrderID;
        Context context;
        WebService service;
        String routeString;

        public ShowAvailableData(int workOrderID, Context context) {
            this.workOrderID = workOrderID;
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            availableRecyclerView.setVisibility(View.GONE);
            progressBar      .setVisibility(View.VISIBLE);
            loading_txt_view.setVisibility(View.VISIBLE);

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            service = new WebService();
            routeString = service.getAvailableRoutesData(workOrderID);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(routeString);
                JSONArray jsonArray = jsonObject.getJSONArray("Routes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object=jsonArray.getJSONObject(i);
                    int RouteID = object.getInt("RouteID");
                    String Facility = object.getString("Facility");
                    String Rule = object.getString("Rule");
                    String Route = object.getString("Route");
                    String Status = object.getString("Status");
                    boolean Inspected =Boolean.parseBoolean(String.valueOf(object.getInt("Inspected")));
                    Routes route=new Routes();
                    route.setRouteID(RouteID);
                    route.setFacility(Facility);
                    route.setRule(Rule);
                    route.setRoute(Route);
                    route.setStatus(Status);
                    route.setInspected(Inspected);
                routes.add(route);
              //  progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(routes.isEmpty()){
                progressBar.setVisibility(View.GONE);
                loading_txt_view.setVisibility(View.GONE);
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setMessage("No Routes Found For WO.Please Check Your WO Number Or Contact Your Project Manager");
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }else {
                progressBar .setVisibility(View.GONE);
                loading_txt_view.setVisibility(View.GONE);
                availableRecyclerView.setVisibility(View.VISIBLE);
                availableArrayAdapter=new AvailableArrayAdapter(routesfilterlist,routes,context);
                availableRecyclerView.setAdapter(availableArrayAdapter);
            /*availableArrayAdapter.setOnItemClickListener(new AvailableArrayAdapter.OnItemClickListener() {
                @Override
                public void onDeleteClick(final int position, final Context context) {
                         final Routes routes_obj=routes.get(position);
                    AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(context);
                    alertDialogBuilder1.setMessage(" Do You Want To Download "+routes_obj.getRoute()+" Route");
                    alertDialogBuilder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder1.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    WebService webService=new WebService();
                                    Responce=webService.setRoutesStatus(1,1,"D");
                                }
                            }).start();
                            //new DwonloadRouteAsync(routes_obj.getFacility(),context,availableArrayAdapter,position).execute();
                            */
            /*SharedPreferences sharedPreferences=context.getSharedPreferences("Responce Preferences", Context.MODE_PRIVATE);
                            int responce=sharedPreferences.getInt("Responce",0);*/
            /*
                            if(Responce==1){
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setMessage(routes_obj.getFacility() +" Route has been downloaded successfully");
                                alertDialogBuilder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        routes.remove(position);
                                        availableArrayAdapter.notifyItemRemoved(position);
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog1 = alertDialogBuilder.create();
                                alertDialog1.show();
                            }
                        }
                    });
                    AlertDialog alertDialog1 = alertDialogBuilder1.create();
                    alertDialog1.show();

                }
            });*/
                availableArrayAdapter.notifyDataSetChanged();

            }


    }

}


   public  void ClearAdapter() {
       if(!routes.isEmpty()){
           availableArrayAdapter.clear();
       }
    }

}
