package com.onboardify.DataCommunicator;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.onboardify.GoogleAuth.GoogleAuthenticationActivity;

import java.util.HashMap;
import java.util.Map;


public class GoogleCaller extends ReactContextBaseJavaModule {

    private ReactApplicationContext reactContext;
    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    GoogleCaller(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "GoogleCaller";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void callGoogle(){
        Activity activity = reactContext.getCurrentActivity();
        if(activity!=null) {
            Intent facebookIntent = new Intent(reactContext, GoogleAuthenticationActivity.class);
            facebookIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(facebookIntent, 2);
        }
    }
}
