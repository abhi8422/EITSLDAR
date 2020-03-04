package com.envigil.extranet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.envigil.extranet.SQLiteOpenHelper.SQLiteHelper;
import com.envigil.extranet.SoapAsyncTask.DownloadRouteAsync;
import com.envigil.extranet.WebService.WebService;
import com.envigil.extranet.models.Routes;

import java.util.ArrayList;
import java.util.List;

public class AvailableArrayAdapter extends RecyclerView.Adapter<AvailableArrayAdapter.AvailableViewHolder> {

    List<Routes> routesFilterList =new ArrayList<>();
    static List<Routes> routesList =new ArrayList<>();
   public static ProgressDialog progressDialog;
   public boolean onClicked=true;
    private long mLastClickTime = 0;
    AlertDialog.Builder builder;


  /*  TextView title,WorkOrder,Facility,rule;
    CardView cardView;
    ImageView imageView;
    CheckBox checkBox;*/

    static Context context;



    public AvailableArrayAdapter(List<Routes> routesFilterList, List<Routes> routesList, Context context) {
        this.routesFilterList = routesFilterList;
        this.routesList = routesList;
        this.context = context;
    }

    @NonNull
    @Override
    public AvailableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.available_item, parent, false);
        return new AvailableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AvailableViewHolder holder, final int position) {

        final Routes routes= routesList.get(position);
        holder.workorder.setText(String.valueOf(routes.getRouteID()));
        holder.title.setText(routes.getRoute());
        holder.facility.setText(routes.getFacility());
        if(routes.getStatus().equals("")){
            holder.inspected_status.setText("No");
        }else {
            holder.inspected_status.setText("Yes");
        }

        holder.rule.setText(routes.getRule());
        holder.view.setTag(position);


       holder.view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                   return;
               }
               mLastClickTime = SystemClock.elapsedRealtime();
               if (isInternetAvailable() == true){
                   final String name=routesList.get(position).getRoute();
                   final int routeID=routesList.get(position).getRouteID();
                   new CheckRouteStatus(routeID, AvailableWorkOrderFrag.workOrderID,name).execute();
               }
               else {
                   builder = new AlertDialog.Builder(context);
                   builder.setTitle("Not connected to internet!!");
                   builder.setMessage("Check your internet connection and try again.");
                   builder.setCancelable(false);
                   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) { dialogInterface.cancel();
                       }
                   });
                   builder.create();
                   builder.show();
               }

           }//onclick end
     });

    }
    @SuppressLint("MissingPermission")
    private boolean isInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    @Override
    public int getItemCount() {
        return routesList.size();
    }

    public class AvailableViewHolder extends RecyclerView.ViewHolder{
        TextView title,workorder,facility,rule,inspected_status;
        CardView cardView1;
        View view;
        LinearLayout linearLayout;
        public AvailableViewHolder(@NonNull final View itemView) {

            super(itemView);
            view=itemView;
            cardView1 =itemView.findViewById(R.id.dcardAvailable);
            //checkBox=itemView.findViewById(R.id.select_check);
            title=itemView.findViewById(R.id.titleAvailable);
            workorder=itemView.findViewById(R.id.routeid2);
            facility=itemView.findViewById(R.id.facides_available);
            rule =itemView.findViewById(R.id.ruledes_available);
           inspected_status=itemView.findViewById(R.id.inspected_status_available);

            /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =getAdapterPosition();
                String name=routesList.get(position).getRoute();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Do You Want To Download "+name+" Route");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(context,"Route Is Downlaoding",Toast.LENGTH_LONG).show();
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            });*/
        }
    }
    public void clear() {
        int size = routesList.size();
        routesList.clear();
        notifyItemRangeRemoved(0, size);
    }
    public class CheckRouteStatus extends AsyncTask{
        int RouteID,WorkID;
        String result,name;
        boolean isRoutepreset;
        SQLiteHelper sqLiteHelper=new SQLiteHelper(context);
        public CheckRouteStatus(int routeID, int workID,String name) {
            RouteID = routeID;
            WorkID = workID;
            this.name=name;
        }

       /* @Override
        protected void onPreExecute() {
            super.onPreExecute();
            availableRecyclerView.setVisibility(View.GONE);
            downaloading_txt_view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }*/

        @Override
        protected Object doInBackground(Object[] objects) {
            WebService webService=new WebService();
            result =webService.CheckIfRouteIsExists(RouteID,WorkID);
            System.out.println("CheckIfRouteIsExists result:: "+result);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            /*availableRecyclerView.setVisibility(View.VISIBLE);
            downaloading_txt_view.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);*/
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                if(result.equals("D")){
                    alertDialogBuilder.setMessage(""+name+" Route Is Already Downloaded");
                    alertDialogBuilder.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.show();
                }
                else if (result.equals("I")){
                    alertDialogBuilder.setMessage(""+name+" Route Is Already Inspected");
                    alertDialogBuilder.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.show();
                }else if(sqLiteHelper.isRoutePresentCheck(RouteID)){
                    alertDialogBuilder.setMessage("Do You Want To Download "+name+" Route?");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    new DownloadRouteAsync(name,context,RouteID, AvailableWorkOrderFrag.workOrderID).execute();
                                }
                            });
                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.show();
                }
                else {
                    alertDialogBuilder.setMessage(""+name+" Is Already Present In Your Device");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();

                                }
                            });
                    alertDialogBuilder.show();
                }
            }
        }
    }


