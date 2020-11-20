package com.emproto.onboardify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.onboardify.OnBoardingHelper.OnBoard;
import com.onboardify.OnBoardingHelper.OnBoardifySuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONObject object = new JSONObject();
        try {
            object.put("platform",0);
            object.put("projectName",getApplicationContext().getString(getApplicationContext().getResources().getIdentifier("app_name","string",getApplicationContext().getApplicationInfo().packageName)));
            object.put("packageName",getApplicationContext().getApplicationInfo().packageName);
            object.put("secretKey",getApplicationContext().getString(getApplicationContext().getResources().getIdentifier("secret_key","string",getApplicationContext().getApplicationInfo().packageName)));
        } catch (JSONException e) {
            Log.e("JSONError",""+e);
        }
        OnBoard.getInstance().startOnBoarding(this, this, new OnBoardifySuccessListener() {
            @Override
            public void onSuccess(String value) {}
        },object);
    }
}
