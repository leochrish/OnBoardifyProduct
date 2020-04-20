package com.onboardify.DataCommunicator;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.onboardify.OnBoardingHelper.OnBoard;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


import static android.content.Context.CONNECTIVITY_SERVICE;


public class ScreenStatus extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    private URL url;
    private HttpsURLConnection connection;
    private OutputStream outputStream;

    ScreenStatus(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "ScreenStatus";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void passed(String screenName,String testId) {
        JSONObject object = new JSONObject();
        WifiManager manager = (WifiManager)reactContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        try {
            object.put("secretKey", this.reactContext.getString(reactContext.getResources().getIdentifier("secret_key", "string", this.reactContext.getApplicationInfo().packageName)));
            object.put("projectName",this.reactContext.getString(reactContext.getResources().getIdentifier("app_name","string",this.reactContext.getApplicationInfo().packageName)));
            object.put("packageName",this.reactContext.getApplicationInfo().packageName);
            object.put("screenName",screenName);
            object.put("userId",address);
            object.put("testId",testId);
        }catch (Exception ignored){ }
        try {
            url = new URL(OnBoard.STATUS_URL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("Content-Type", "application/json");
            if(isNetworkAvailable()){
                if (connection != null) {
                    outputStream = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                    writer.write(object.toString());
                    writer.flush();
                    writer.close();
                    outputStream.close();
                    outputStream.close();
                    Log.e("Testid",""+connection.getResponseCode()+" : "+connection.getResponseMessage());
                }
            }
        }catch (Exception ignored){}
    }
    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager manager = (ConnectivityManager) reactContext.getSystemService(
                    CONNECTIVITY_SERVICE);
            return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            Log.e("Error----", "" + e);
        }
        return false;
    }
}
