package com.envigil.extranet.MasterDataSyncAsync;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.envigil.extranet.R;

public class MasterDataAsync extends AsyncTask {
Context context;
ProgressDialog progressDialog;
    public MasterDataAsync(Context context) {
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Master Data Is Syncing Please Wait..");
        progressDialog.setContentView(R.layout.progress_dialog_master_sync);
        progressDialog.show();
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
