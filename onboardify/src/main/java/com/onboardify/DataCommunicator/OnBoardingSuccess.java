package com.onboardify.DataCommunicator;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.onboardify.OnBoardingHelper.OnBoard;
import com.onboardify.OnBoardingHelper.OnBoardifySuccessListener;
import com.onboardify.OnBoardingHelper.OnBoardingListener;


import org.json.JSONException;
import org.json.JSONObject;



public class OnBoardingSuccess extends ReactContextBaseJavaModule {

    private ReactApplicationContext reactContext;

    OnBoardingSuccess(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "OnBoardingSuccess";
    }

    @ReactMethod
    public void finish(String value) {
        JSONObject object = new JSONObject();
        try{
            object.put("secretKey","5e4290c739254f318bf3a130-fus549sbk6ht2uzs");
            object.put("screenName","Screen10");
            object.put("noOfScreensCompleted",1);
        }catch (JSONException e){
            Log.e("JSONError",""+e);
        }
        OnBoardingListener listener1 =new OnBoardingListener(reactContext);
        listener1.sendData(object);
        OnBoardifySuccessListener listener = OnBoard.getListener();
        listener.onSuccess(value);
        Activity activity = reactContext.getCurrentActivity();
        if (activity != null){
            activity.finish();
        }
    }

}
