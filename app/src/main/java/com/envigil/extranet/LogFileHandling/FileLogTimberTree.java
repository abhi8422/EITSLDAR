package com.envigil.extranet.LogFileHandling;

import android.os.Environment;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public class FileLogTimberTree extends Timber.Tree {
    Date date;
    private String mainFolder,dayFolder;

    public FileLogTimberTree(Date date) {
        this.date=date;
    }
    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
                SimpleDateFormat format1=new SimpleDateFormat("EEE:d-MMM-yy");
                mainFolder =Environment.getExternalStorageDirectory()+File.separator + "ExtranetDAEP_Log";
                dayFolder=mainFolder+File.separator +format1.format(date.getTime());
                File appDirectory = new File(dayFolder);
                if ( !appDirectory.exists() ) {
                    appDirectory.mkdir();
                }
                File logFile = new File( appDirectory, "DataLog"+date.getTime()+".txt" );
                SimpleDateFormat format=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                String current_date=format.format(System.currentTimeMillis());
                String msg="\r\n"+current_date+"-->"+message+"\r\n";
                    try {
                        logFile.createNewFile();
                        if (logFile.exists()) {
                        FileOutputStream fos=new FileOutputStream(logFile,true);
                        fos.write(msg.getBytes());
                        fos.close();}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
    }


}
