package com.envigil.extranet.LogFileHandling;

import android.app.Application;
import android.os.Environment;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;

public class LogFileHandling extends Application {
    File readFile;
    String mainFolder,checkDate,dayFolder;
    File[] files;
    Date dCheckdate,dFiledate;
    @Override
    public void onCreate() {
        super.onCreate();
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,-7);
        Calendar calendar1=Calendar.getInstance();
        //SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat format=new SimpleDateFormat("EEE:d-MMM-yy");
        checkDate= format.format(calendar.getTime());
        mainFolder =Environment.getExternalStorageDirectory()+File.separator + "ExtranetDAEP_Log";
        dayFolder=mainFolder+File.separator +format.format(calendar1.getTime());
        File appDirectory = new File(dayFolder);
        if ( !appDirectory.exists() ) {
            appDirectory.mkdir();
        }
        readFile=new File(mainFolder);
        files=readFile.listFiles();
        File logFile = new File( appDirectory, "TLog"+System.currentTimeMillis()+".txt" );
        //timber initlization
        Timber.plant(new FileLogTimberTree(calendar1.getTime()));
        // clear the previous logcat and then write the new one to the file
        try {
            dCheckdate=format.parse(checkDate);
            Process process = Runtime.getRuntime().exec("logcat -c");
            process = Runtime.getRuntime().exec("logcat -b crash -v thread -f"+logFile);
        } catch ( IOException e ) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (files != null) {
            for (File file : files) {

                Date lastModDate = new Date(file.lastModified());
                String date=format.format(lastModDate);
                try {
                    dFiledate=format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("dCheckdate ::"+dCheckdate);
                System.out.println("dFiledate ::"+dFiledate);
                if(dCheckdate.after(dFiledate)){
                    System.out.println("Result=true File Name="+file.getName());
                    try {
                        FileUtils.deleteDirectory(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Result=Deleted File Name="+file.getName());
                    }

                }

            }
        }

    }
}
