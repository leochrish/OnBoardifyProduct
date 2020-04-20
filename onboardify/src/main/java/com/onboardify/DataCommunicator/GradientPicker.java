package com.onboardify.DataCommunicator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.onboardify.Utilities.GradientCreator;

import java.io.ByteArrayOutputStream;


public class GradientPicker extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;

    private int[] colors;
    public GradientPicker(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "GradientPicker";
    }

    @ReactMethod
    public void getGradient(ReadableArray colorArray, int alpha, int corner, int orientation) {
        GradientCreator creator = new GradientCreator(reactContext);
        colors = new int[colorArray.size()];
        for(int i=0; i<colorArray.size();i++){
            try {
                colors[i] = Color.parseColor("#"+colorArray.getString(i));
            }catch (Exception e){
                colors[i] = Color.WHITE;
            }
        }
        GradientDrawable drawable = creator.getGradient(colors, alpha, corner, orientation);
        Bitmap gradientBitmap = convertToBitmap(drawable);
        String encodedImage = encodeImage(gradientBitmap);
        WritableMap params = Arguments.createMap();
        params.putString("gradient", encodedImage);
        sendEvent(reactContext, "GradientCallback", params);
        gradientBitmap.recycle();
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    private Bitmap convertToBitmap(Drawable drawable) {
        Bitmap mutableBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, 512, 512);
        drawable.draw(canvas);
        return mutableBitmap;
    }
}
