package com.envigil.extranet.Bluetooth;

public class BluetoothGlobal {
    public static String devicename;
    public static void setDevice_name(String device_name){
        devicename=device_name;
    }
    public static String getDevice_name(){
        return devicename;
    }
}
