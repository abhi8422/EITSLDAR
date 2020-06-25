package com.envigil.extranet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class InternetCheck extends AppCompatActivity {

    AlertDialog.Builder builder;
    TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_check);

        Title =findViewById(R.id.title);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null)
        {
            if ("text/plain".equals(type))
            {
                handleSendText(intent);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(InternetCheck.this, HomeActivity.class));
                        finish();
                    }
                },1500);
//                if (isInternetAvailable() == true){
//
//
//                }
//                else {
//                    builder = new AlertDialog.Builder(InternetCheck.this);
//                    //Setting message manually and performing action on button click
//                    builder.setMessage("Check Your Internet connection and retry!")
//                            .setCancelable(false)
//                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    if (isInternetAvailable()==true){
//                                        startActivity(new Intent(InternetCheck.this, HomeActivity.class));
//                                        finish();
//                                    }
//                                    else{
//                                        startActivity(new Intent(InternetCheck.this,InternetCheck.class));
//                                        finish();
//                                    }
//                                    dialog.cancel();
//                                }
//                            });
//
//                    //Creating dialog box
//                    AlertDialog alert = builder.create();
//                    //Setting the title manually
//                    alert.setTitle("No connection available !!");
//                    alert.show();
//                }
            }
        }
        else {
            intent = getPackageManager().getLaunchIntentForPackage("com.example.mainapplication");
            startActivity(intent);
            finish();

        }


    }

    private void handleSendText(Intent intent) {
        final String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        Title.setText("Connecting to "+sharedText+"....");
    }

    @SuppressLint("MissingPermission")
    private boolean isInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}