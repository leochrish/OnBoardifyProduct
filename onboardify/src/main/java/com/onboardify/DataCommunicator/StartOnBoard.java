package com.onboardify.DataCommunicator;

import android.util.Log;

import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.onboardify.OnBoardingHelper.OnBoard;
import com.onboardify.OnBoardingHelper.OnBoardifySuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

class StartOnBoard extends ReactContextBaseJavaModule {

    private ReactApplicationContext reactContext;
    public StartOnBoard(@NonNull ReactApplicationContext reactContext1) {
        super(reactContext1);
        reactContext = reactContext1;
    }

    @NonNull
    @Override
    public String getName() {
        return "StartOnBoard";
    }

    @ReactMethod
    public void start(int platform, String projectName, String packageName, String secretKey){
        JSONObject object = new JSONObject();
        try {
            object.put("platform",platform);
            object.put("projectName",projectName);
            object.put("packageName",packageName);
            object.put("secretKey",secretKey);
        } catch (JSONException e) {
            Log.e("JSONError",""+e);
        }
        OnBoard.getInstance().startOnBoarding(reactContext, reactContext.getCurrentActivity(), new OnBoardifySuccessListener() {
            @Override
            public void onSuccess(String value) {}
        },object);
    }
}
