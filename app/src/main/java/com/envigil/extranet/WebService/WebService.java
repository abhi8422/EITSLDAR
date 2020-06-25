package com.envigil.extranet.WebService;

import android.content.Context;



import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

import java.sql.ResultSet;

public class WebService {
Context context;
    public WebService() {
    }
    public WebService(Context context){
        this.context=context;
    }
 /* String WSDL_URL="http://montrosews.envigil.net/Montrose.asmx";
    String SOAP_ACTION="http://tempuri.org/";
    String NAME_SPACE="http://tempuri.org/";
    String METHOD_NAME="getRoutesByWorkOrderJson";
    String METHOD_getRoutesData="getRoutesData";*/
    //new
    public static String WSDL_URL="http://98.173.13.62:8080/web/getroutes.asmx?WSDL";
   // public static String WSDL_URL="https://www.avantienv.com/web/getroutes.asmx?WSDL";
    String SOAP_ACTION="Avanti";
    String NAME_SPACE="Avanti";
    String METHOD_NAME_getRoutesDataJSON="getRoutesDataJSON";
    String METHOD_NAME_getRoutesByWorkOrderJSON="getRoutesByWorkOrderJSON";
    String METHOD_NAME_setRoutesStatus ="setRoutesStatusAndroid";
    String METHOD_NAME_RouteInspected="RouteInspected";
    String METHOD_NAME_getInspectionsByWorkOrder="getInspectionsByWorkOrderJSON";
    String METHOD_NAME_uploadComponentsJson="UploadComponentsJSON";
    String METHOD_NAME_uploadLeakImage="UploadLeakImage";
    SoapObject result;

    /*public String getRouteSOAPHTTPUrl(int RouteID,int WorkID){

        StringBuffer response = new StringBuffer();

        try {
            URL obj = new URL(WSDL_URL);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setSSLSocketFactory( TrustKit.getInstance().getSSLSocketFactory(obj.getHost()));
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","text/xml; charset=utf-8");
            String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <getRoutesDataJSON xmlns=\"Avanti\">\n" +
                    "      <RouteID>"+RouteID+"</RouteID>\n" +
                    "      <WorkID>"+WorkID+"</WorkID>\n" +
                    "    </getRoutesDataJSON>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(xml);
            wr.flush();
            wr.close();
            String responseStatus = con.getResponseMessage();
            System.out.println(responseStatus);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("response:" + response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
        return response.toString();
    }*/
    /*public int getDownloadTest(){
         int  Result=0;
        SoapObject soapObject=new SoapObject(NAME_SPACE,METHOD_NAME_testDownlaod);
        //soapObject.addProperty("WorkID","123");
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL);
        //HttpsTransportSE httpTransportSE=new HttpsTransportSE("www.avantienv.com/", 443, "web/getroutes.asmx?WSDL", 60000);

        try {
            httpTransportSE.call(SOAP_ACTION+"/"+METHOD_NAME_testDownlaod,envelope);
            httpTransportSE.setTimeout(30);
            result = (SoapObject) envelope.bodyIn;
            Result=Integer.parseInt(result.getProperty(0).toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return Result;
    }*/
    public String getRoutesData(int routeID,int workorder){
        String resultString="";
        SSLConection.allowAllSSL();
        SoapObject soapObject=new SoapObject(NAME_SPACE,METHOD_NAME_getRoutesDataJSON);
        soapObject.addProperty("RouteID",routeID);
        soapObject.addProperty("WorkID",workorder);
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
        SSLConection.allowAllSSL();
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL,1000000000);
        /*httpTransportSE.setTimeout(15);*/
        /*SSLConection.allowAllSSL();  */

        try {
            httpTransportSE.call(SOAP_ACTION+"/"+METHOD_NAME_getRoutesDataJSON,envelope);
            result = (SoapObject) envelope.bodyIn;
            resultString=result.getProperty(0).toString();
            //System.out.println(resultString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public String getInspection(int WorkId){
        String AllInspection="";

        SoapObject soapObject=new SoapObject(NAME_SPACE,METHOD_NAME_getInspectionsByWorkOrder);
        soapObject.addProperty("WorkID", WorkId);
        //soapObject.addProperty("WorkID","123");
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
      SSLConection.allowAllSSL();
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL);
        //HttpsTransportSE httpTransportSE=new HttpsTransportSE("www.avantienv.com/", 443, "web/getroutes.asmx?WSDL", 60000);

        try {
            httpTransportSE.call(SOAP_ACTION+"/"+METHOD_NAME_getInspectionsByWorkOrder,envelope);
            result = (SoapObject) envelope.bodyIn;
            System.out.println(result);
            AllInspection=result.getProperty(0).toString();
            //System.out.println(resultString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return AllInspection;

    }

    public String validateInspecion(int EmpID,String InspStart,String InspEnd){
            String AllInspection="";
            String METHOD_INSPECTION = "validateInspectionAndroid";
            SoapObject soapObject=new SoapObject(NAME_SPACE,METHOD_INSPECTION);
            soapObject.addProperty("EmpID",EmpID);
            soapObject.addProperty("InspStart",InspStart);
            soapObject.addProperty("InspEnd",InspEnd);
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL);

        try {
            httpTransportSE.call(SOAP_ACTION+"/"+METHOD_INSPECTION,envelope);
            SoapObject ressoap = (SoapObject) envelope.bodyIn;
            AllInspection=ressoap.getProperty(0).toString();
            System.out.println("All inspection response : : "+AllInspection);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return AllInspection;
    }
    public String uploadComponents(String Result){
        String AllInspection="";
        SoapObject soapObject=new SoapObject(NAME_SPACE,METHOD_NAME_uploadComponentsJson);
        soapObject.addProperty("RouteDataJSON",Result);
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL,1000000000);

        try {
            httpTransportSE.call(SOAP_ACTION+"/"+METHOD_NAME_uploadComponentsJson,envelope);
            result = (SoapObject) envelope.bodyIn;
            AllInspection=result.getProperty(0).toString();
            System.out.println(AllInspection);

        } catch (IOException e) {
             e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return AllInspection;

    }

    public String uploadInspection(int WorkID,int EmpID,int TVAID,String InspStart,String InspEnd,double InspLunch,double InspTravel,double InspAdmin,double InspRepair,double InspReInsp){
        String AllInspection="";
        String METHOD_INSPECTION="InsertInspectionAndroid";
        SoapObject soapObject=new SoapObject(NAME_SPACE,METHOD_INSPECTION);
        soapObject.addProperty("WorkID",WorkID);
        soapObject.addProperty("EmpID",EmpID);
        soapObject.addProperty("TVAID",TVAID);
        soapObject.addProperty("InspStart",InspStart);
        soapObject.addProperty("InspEnd",InspEnd);
        soapObject.addProperty("InspLunch",InspLunch);
        soapObject.addProperty("InspTravel",InspTravel);
        soapObject.addProperty("InspAdmin",InspAdmin);
        soapObject.addProperty("InspRepair",InspRepair);
        soapObject.addProperty("InspReinsp",InspReInsp);
        //soapObject.addProperty("WorkID","123");
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
        MarshalDouble md = new MarshalDouble();
        md.register(envelope);
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL);

        try {
            httpTransportSE.call(SOAP_ACTION+"/"+METHOD_INSPECTION,envelope);
            result = (SoapObject) envelope.bodyIn;
            AllInspection=result.getProperty(0).toString();
            System.out.println(AllInspection);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return AllInspection;

    }
    public String getAvailableRoutesData(int id){
        String resultString="";
        SoapObject soapObject=new SoapObject(NAME_SPACE,METHOD_NAME_getRoutesByWorkOrderJSON);
        soapObject.addProperty("WorkID",id);
        //soapObject.addProperty("WorkID","123");
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
//
        SSLConection.allowAllSSL();
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL,6000);

        try {
            httpTransportSE.call(SOAP_ACTION+"/"+METHOD_NAME_getRoutesByWorkOrderJSON,envelope);
            result = (SoapObject) envelope.bodyIn;
            resultString=result.getProperty(0).toString();
            //System.out.println(resultString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public String setRoutesStatusAndroid(int RouteID, int WorkID, String Option){
        SoapObject responce=null;
        String result = null;
        SoapObject soapObject=new SoapObject(NAME_SPACE, METHOD_NAME_setRoutesStatus);
        soapObject.addProperty("iWorkID",WorkID);
        soapObject.addProperty("iRouteID",RouteID);
        soapObject.addProperty("sOption",Option);
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL,60000);
        try {
            httpTransportSE.call(SOAP_ACTION+"/"+ METHOD_NAME_setRoutesStatus,envelope);
            responce = (SoapObject) envelope.bodyIn;
            result=responce.toString();
            System.out.println("setRoutesStatus responce::"+responce);
            System.out.println("setRoutesStatus result ::"+result);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return result;
    }
    public String uploadLeakImage(byte[] imageByte,String FileName,int InspId){
        SoapObject response=null;
        String result=null;
        SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_NAME_uploadLeakImage);
        soapObject.addProperty("image", Base64.encode(imageByte));
        soapObject.addProperty("fileName",FileName);
        soapObject.addProperty("iInspID",InspId);
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
       MarshalBase64 marshalBase64=new MarshalBase64();
       marshalBase64.register(envelope);
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL,999999999);
        try {
            httpTransportSE.call(SOAP_ACTION+"/"+ METHOD_NAME_uploadLeakImage,envelope);
            response = (SoapObject) envelope.bodyIn;
            result=response.toString();
            System.out.println("setRoutesStatus response::"+response);
            System.out.println("setRoutesStatus result ::"+result);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String CheckIfRouteIsExists(int routeId,int workId){
        String masterTableString="";
        SoapObject soapObject=new SoapObject(NAME_SPACE,METHOD_NAME_RouteInspected);
        soapObject.addProperty("RouteID",routeId);
        soapObject.addProperty("WorkID",workId);
        //soapObject.addProperty("myname","Abhishek");
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
        try {
            SSLConection.allowAllSSL();
        HttpTransportSE httpTransportSE=new HttpTransportSE(WSDL_URL);
            httpTransportSE.call(SOAP_ACTION+"/"+METHOD_NAME_RouteInspected,envelope);
            result = (SoapObject) envelope.bodyIn;
            masterTableString=result.getProperty(0).toString();
            System.out.println(masterTableString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return masterTableString;
    }

}
