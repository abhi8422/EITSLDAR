package com.envigil.extranet.Bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.envigil.extranet.HomeActivity;
import com.envigil.extranet.R;
import com.envigil.extranet.RouteDashboard;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.envigil.extranet.ComponentReading.CompBackReadingHandler;
import static com.envigil.extranet.HomeActivity.fraglayout;
import static com.envigil.extranet.RouteReadingBottomSheetFrag.RouteBackhandler;
import static com.envigil.extranet.SubAreaBackgroundReadingFragment.SubAreaBackhandler;
import static com.envigil.extranet.fragments.CompReadingBottomFrag.CompReadingBottomFragHandler;

public class Bluetooth extends AppCompatActivity {
    Button btn_logstart,btn_list_devices,btn_send,btn_save,btn_detector_fid,btn_listen,btn_home;
    TextView status,message;
    EditText edt_message;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] btArray;
    BluetoothDevice device;
    DrawerLayout drawerLayout;
    ArrayList<String> buffer_received_data = new ArrayList<>();
    private static final String TAG = "Bluetooth";
    public static final int STATE_LISTING=1;
    public static final int STATE_CONNECTING=2;
    public static final int STATE_CONNECTED=3;
    public static final int STATE_CONNECTION_FAILED=4;
    public static final int STATE_CONNECTION_RECEIVED=5;
    public static final String APP_NAME="Bluetooth_App";
    public static final UUID my_uuid=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static boolean RouteBackReading=false;
    public static boolean SubAreaBackReading=false;
    public static boolean CompReadingBottomFrag=false;
    public static boolean CompBackReading=false;
    public static boolean RepairRequest=false;
    SendReceive sendReceive;
    Logger logger;
    int REQUEST_ENABLED_BLUETOOTH=1;
    Intent bluetoothEnableIntent;
    ListView listView;
    byte[] buffer_send_receive;
    String device_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);
        /*btn_logstart =findViewById(R.id.ble_listen);
        btn_detector_fid=findViewById(R.id.ble_listen2);
        btn_list_devices=findViewById(R.id.btn_list_devices);*/
        /*btn_send=findViewById(R.id.btn_send);*/
        btn_save=findViewById(R.id.btn_save);
        /*btn_home=findViewById(R.id.btn_goto_home);*/
        status=findViewById(R.id.txt_status);
        /*edt_message=findViewById(R.id.edt_message);*/
        listView=findViewById(R.id.list_item);
        message=findViewById(R.id.message);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        bluetoothEnableIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        configureNavDrawer();
        configureToolbar();
        String next = "<font color='#1777AA'>CLICK</font>";
        message.setText(Html.fromHtml("* If you don't see your TVA in above list, "+next+" here to go to mobile bluetooth settings and pair your TVA with this device"));
        if(BluetoothGlobal.getDevice_name()!=null){
            status.setText("Connected TVA :"+BluetoothGlobal.getDevice_name());
        }
        //get UUID
     /*   ParcelUuid[] uuids = new ParcelUuid[0];
        try {
            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
            uuids = (ParcelUuid[]) getUuidsMethod.invoke(bluetoothAdapter, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        for (ParcelUuid uuid: uuids) {
            Log.d(TAG, "UUID: " + uuid.getUuid().toString());
        }*/
        //logging file
        logger = Logger.getLogger("MyLog");

        File logfile;
        File logcatfile;
        FileHandler fh;
        Process process;
        try {
            // This block configure the logger with handler and formatter
            logfile = new File(Environment.getExternalStorageDirectory(), "Extranet_TVALog.txt");
            logcatfile = new File(Environment.getExternalStorageDirectory(), "Extranet_Logcat.txt");
            fh = new FileHandler(logfile.getPath());
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
             process= Runtime.getRuntime().exec("logcat -c");
             process = Runtime.getRuntime().exec("logcat -f " + logcatfile);
            // the following statement is used to log any messages
            logger.info("Logging Started");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //check bluetooth is on or off
        if(!bluetoothAdapter.isEnabled()){
            logger.info("Enabling Bluetooth");
            startActivityForResult(bluetoothEnableIntent,REQUEST_ENABLED_BLUETOOTH);
            logger.info("Bluetooth Is Enabled");
        }
        if(( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED )){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
        }
        logger.info("Getting Paired Devices");
        Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
        String[] strings=new String[bt.size()];
        btArray=new BluetoothDevice[bt.size()];
        int index=0;
        if(bt.size()>0){
            for(BluetoothDevice device:bt){
                btArray[index]=device;
                strings[index]=device.getName();
//                       logger.info("Paired Devices Name is :: "+device.getName()+", Device UUID :: "+device.getUuids().toString());
                index++;
            }
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
        listView.setAdapter(arrayAdapter);
        implementListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this,"Permission Accepted",Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this,"User Refused To Accept The Permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void implementListener() {
       /* btn_list_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logger.info("Getting Paired Devices");
                Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
                String[] strings=new String[bt.size()];
                btArray=new BluetoothDevice[bt.size()];
                int index=0;
                if(bt.size()>0){
                    for(BluetoothDevice device:bt){
                        btArray[index]=device;
                        strings[index]=device.getName();
//                       logger.info("Paired Devices Name is :: "+device.getName()+", Device UUID :: "+device.getUuids().toString());
                        index++;
                    }
                }
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                listView.setAdapter(arrayAdapter);
            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    device=btArray[position];
                    BluetoothGlobal.setDevice_name(device.getName());
                    ClientClass clientClass=new ClientClass(device);
                    clientClass.start();
                    logger.info("Connecting Devices :: "+btArray[position].getName());
                    status.setText("Connecting");
                } catch (IOException e) {
                    e.printStackTrace();
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    logger.info("listView OnClickListener"+errors.toString());
                }
            }
        });

       /* btn_logstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*ServerClass serverClass=new ServerClass();
                serverClass.start();
                Toast.makeText(getApplicationContext()," (1) Listening Device",Toast.LENGTH_LONG).show();*//*
                String str_logstart="log start\r\n";
                try {
                    sendReceive.write(str_logstart.getBytes());
                    logger.info("Sending log start command");

                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info("Exception At btn_logstart::"+e.getMessage());
                }
            }
        });

        btn_detector_fid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_detector_fid="detector fid\r\n";
                try {
                    sendReceive.write(str_detector_fid.getBytes());
                    logger.info("Sending detector fid command");

                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info("Exception At btn_detector_fid::"+e.getMessage());
                }
            }
        });*/

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                 ComponentName cn = new ComponentName("com.android.settings",
                         "com.android.settings.bluetooth.BluetoothSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity( intent);
            }
        });
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case STATE_LISTING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("CONNECTING");
                    break;
                case  STATE_CONNECTED:
                    status.setText("Connected Device :"+device.getName());
                    AlertDialog.Builder builder = new AlertDialog.Builder(Bluetooth.this);
                    builder.setTitle("Device "+device.getName()+" Is Connected");
                    builder.setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                    dialog.dismiss();
                                }
                            });
                    final AlertDialog alertdialog = builder.create();
                    alertdialog.show();

                    alertdialog.getWindow().setGravity(Gravity.BOTTOM);
                    logger.info("Device "+device.getName()+" Is Connected");

                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("CONNECTION_FAILED");
                    logger.info("Device Connection Failed");
                    break;
                case 7:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempmsg=new String(readBuff,0,msg.arg1);
                    //message.setText(tempmsg);
                    logger.info("Data Received :: "+tempmsg);
                    //status.setText("MSG :"+tempmsg);
                    break;
                case 8:
                    break;

            }
            return true;
        }
    });

    public void generateNoteOnSD(Context context, String sFileName, ArrayList<String> bufferdata) {
        logger.info("Received Data :: "+bufferdata);
        Log.i(TAG,"Run Received Message::: "+bufferdata);
        File file;
        FileOutputStream outputStream;
        try {
            logger.info("Writing Data Into File");
            file = new File(Environment.getExternalStorageDirectory(), "TVA_Buffer_Data.txt");
            outputStream = new FileOutputStream(file);
            outputStream.write(buffer_send_receive);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.info("generateNoteOnSD :: "+errors.toString());
        }

    }

    /*private class ServerClass extends Thread{
        private BluetoothServerSocket bluetoothServerSocket;
        public ServerClass(){
            try {
                bluetoothServerSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME,my_uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            BluetoothSocket socket=null;
            while(socket==null){
                try {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket=bluetoothServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    logger.info("ServerClass run(),whiel(socket==null)"+errors.toString());
                }
                if(socket!=null){
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);
                    try {
                        sendReceive=new SendReceive(socket);
                        sendReceive.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        StringWriter errors = new StringWriter();
                        e.printStackTrace(new PrintWriter(errors));
                        logger.info("SendReceive run(),if(socket!=null)"+errors.toString());
                    }
                    break;
                }
            }
        }

    }*/

    private class ClientClass extends Thread{
        private BluetoothDevice device;
        private BluetoothSocket socket;
        public ClientClass(BluetoothDevice device1) throws IOException {
            logger.info("ClientClass Thread Started");
            device=device1;
            final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            logger.info("ClientClass Creating Client Socket");

                socket=device.createRfcommSocketToServiceRecord(SERIAL_UUID);

        }
        public void run(){
            try {
                logger.info("ClientClass Socket Connecting ");
                /*if (!socket.isConnected()){
                    socket.connect();
                }*/
                socket.connect();
                logger.info("ClientClass Socket Connected ");
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);
                logger.info("Starting SendReceive Thread");
                sendReceive=new SendReceive(socket);
                String str_logstart="log start\r\n";
                try {
                    sendReceive.write(str_logstart.getBytes());
                    logger.info("Sending log start command");
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info("Exception At btn_logstart::"+e.getMessage());
                }
                String str_detector_fid="detector fid\r\n";
                try {
                    sendReceive.write(str_detector_fid.getBytes());
                    logger.info("Sending detector fid command");
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info("Exception At btn_detector_fid::"+e.getMessage());
                }
                sendReceive.start();
                logger.info("SendReceive Start() ");

            } catch (IOException e) {
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                logger.info("(Client Class) Connection Failed ");
                handler.sendMessage(message);
                e.printStackTrace();
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                logger.info("Client Class run()"+errors.toString());
            }
        }
    }

    public class SendReceive extends Thread{

        private final BluetoothSocket socket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        private SendReceive(BluetoothSocket socket) throws IOException {
            logger.info("SendReceive Started");
            this.socket = socket;
            InputStream temIn=null;
            OutputStream temOut=null;
            temIn=socket.getInputStream();
            temOut=socket.getOutputStream();
            inputStream=temIn;
            outputStream=temOut;
        }

        public void run(){
            buffer_send_receive=new byte[1024];
            int bytes;
            while (true){
                try {

                    logger.info("(SendReceive)Reading Data From InputStream");
                    bytes=inputStream.read(buffer_send_receive);
                    if(bytes<=0){
                        logger.info("Nothing To Read From InputStream");
                        handler.obtainMessage(8);
                    }
                    //String readMessage = new String(buffer_send_receive, 0, bytes);
                    //buffer_received_data.add(readMessage);
                    handler.obtainMessage(7,bytes,-1,buffer_send_receive).sendToTarget();
                    if(RouteBackReading){
                        RouteBackhandler.obtainMessage(7,bytes,-1,buffer_send_receive).sendToTarget();
                    }
                    if(SubAreaBackReading){
                        SubAreaBackhandler.obtainMessage(7,bytes,-1,buffer_send_receive).sendToTarget();
                    }
                    if(CompReadingBottomFrag){
                        CompReadingBottomFragHandler.obtainMessage(7,bytes,-1,buffer_send_receive).sendToTarget();
                    }
                    if(CompBackReading){
                        CompBackReadingHandler.obtainMessage(7,bytes,-1,buffer_send_receive).sendToTarget();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    logger.info("SendReceive run() ::"+errors.toString());
                }
            }
        }

        public void write(byte[] bytes) throws IOException {
            logger.info("Writing Data Into OutPutStream");
            outputStream.write(bytes);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    //Drawer Layout
    private void configureToolbar() {
        Toolbar nav_toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(nav_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavDrawer() {
        drawerLayout = findViewById(R.id.layout);
        NavigationView navigationView = findViewById(R.id.nav_view_bluetooth);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction, fragmentTransaction1, fragmentTransaction2;
                int menuId = item.getItemId();

                if (menuId == R.id.home_app){
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.mainapplication");
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    finish();
                }
                else if (menuId == R.id.inspect_routes){

                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.down_routes){

                    fraglayout.setCurrentItem(1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.upload_routes){

                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuId == R.id.bt_config){
//                    Toast.makeText(getApplicationContext(),"Redirecting to bluetooth configuration",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    moveTaskToBack(true);
                    finish();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (java.util.logging.Handler handler:logger.getHandlers()){
            handler.close();
        }
    }
}
