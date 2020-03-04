package com.envigil.extranet.LogFileHandling;

import android.app.Application;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogFileHandling extends Application {
    File readFile;
    String filePath,checkDate;
    File[] files;
    Date dCheckdate,dFiledate;
    @Override
    public void onCreate() {
        super.onCreate();
        filePath=Environment.getExternalStorageDirectory()+File.separator + "ExtranetDAEP_Log";
        readFile=new File(filePath);
        files=readFile.listFiles();
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,-7);
        SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy");
        checkDate= dateFormat.format(calendar.getTime());
        try {
            dCheckdate=dateFormat.parse(checkDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (files != null) {
            for (File file : files) {
                Date lastModDate = new Date(file.lastModified());
                String date=dateFormat.format(lastModDate);
                try {
                    dFiledate=dateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("dCheckdate ::"+dCheckdate);
                System.out.println("dFiledate ::"+dFiledate);
                if(dCheckdate.after(dFiledate)){
                    file.delete();
                    System.out.println("Result=true");
                }else {
                    System.out.println("Result=false");
                }

            }


        }

    }
}
