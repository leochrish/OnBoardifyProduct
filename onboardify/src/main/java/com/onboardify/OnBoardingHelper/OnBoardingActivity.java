package com.onboardify.OnBoardingHelper;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.onboardify.DataCommunicator.CommunicationPackages;
import com.onboardify.DataCommunicator.EventEmitter;
import com.onboardify.DataCommunicator.ImagePicker;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;


public class OnBoardingActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    public final int STORAGE_PERMISSION_REQ_CODE = 2;
    private SharedPreferences preferences;
    MutableLiveData<String> liveData;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        liveData = new MutableLiveData<>();
        preferences = getSharedPreferences("onBoard",MODE_PRIVATE);
        mReactRootView = new ReactRootView(this);
        setContentView(mReactRootView);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startApplication();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQ_CODE);
        }
    }

    private void startApplication() {
        SoLoader.init(this, false);
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setCurrentActivity(this)
                //.setBundleAssetName("index.android.bundle")
                .setJSBundleFile(OnBoard.BUNDLE_FILE_DIR + OnBoard.BUNDLE_NAME)
                .addPackage(new MainReactPackage())
                .addPackage(new CommunicationPackages())
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        mReactRootView.startReactApplication(mReactInstanceManager, "test"/*sampleOb*/, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startApplication();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.IMAGE_PICKER) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String encodedImage = encodeImage(selectedImage);
                        WritableMap params = Arguments.createMap();
                        params.putString("image", encodedImage);
                        sendEvent(mReactInstanceManager.getCurrentReactContext(), "ImageCallBack", params);
                        selectedImage.recycle();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        if(requestCode == 1){
            new Thread(){
                @Override
                public void run() {
                    String token = "";
                    while (true){
                        token = preferences.getString("googleToken","");
                        if(!token.equals("")){
                            token = preferences.getString("faceToken","");
                            WritableMap params = Arguments.createMap();
                            params.putString("token", token);
                            EventEmitter eventEmitter = new EventEmitter((ReactApplicationContext) mReactInstanceManager.getCurrentReactContext());
                            eventEmitter.sendEvent(mReactInstanceManager.getCurrentReactContext(),"FacebookCallback",params);
                            break;
                        }
                    }
                    super.run();
                }
            }.start();
        }

        if(requestCode == 2){
            new Thread(){
                @Override
                public void run() {
                    String token = "";
                    while (true){
                        token = preferences.getString("googleToken","");
                        if(!token.equals("")){
                            WritableMap params = Arguments.createMap();
                            params.putString("token", token);
                            EventEmitter eventEmitter = new EventEmitter((ReactApplicationContext) mReactInstanceManager.getCurrentReactContext());
                            eventEmitter.sendEvent(mReactInstanceManager.getCurrentReactContext(),"GoogleCallback",params);
                            break;
                        }
                    }
                    super.run();
                }
            }.start();
        }
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this, this);
        }
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(this);
        }
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }
    }

    @Override
    public void onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

}

