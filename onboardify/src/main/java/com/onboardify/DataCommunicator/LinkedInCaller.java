package com.onboardify.DataCommunicator;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.onboardify.FacebookLogin.FacebookLoginActivity;
import com.onboardify.LinkedInLogin.LinkedActivity;

import java.util.HashMap;
import java.util.Map;

public class LinkedInCaller extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    private ReactApplicationContext reactContext;
    LinkedInCaller(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "LinkedInCaller";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void callLinkedIn(){
        Activity activity = reactContext.getCurrentActivity();
        if(activity!=null) {
            Intent facebookIntent = new Intent(reactContext, LinkedActivity.class);
            facebookIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(facebookIntent, 3);
        }
    }
}

