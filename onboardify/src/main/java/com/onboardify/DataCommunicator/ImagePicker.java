package com.onboardify.DataCommunicator;


import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;


public class ImagePicker extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    public static final int IMAGE_PICKER = 10;

    ImagePicker(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "ImagePicker";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void getImage() {
        Activity activity = reactContext.getCurrentActivity();
        if (activity != null){
            Intent imageIntent=new Intent(Intent.ACTION_GET_CONTENT);
            imageIntent.setType("image/*");
            activity.startActivityForResult(Intent.createChooser(imageIntent,"Select Image"),IMAGE_PICKER);
        }
    }
}
