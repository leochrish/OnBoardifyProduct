package com.onboardify.OnBoardingHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class OnBoard implements BundleDownloadedListener {

    private Context context;
    private Activity activity;
    private static OnBoardifySuccessListener listener;
    static String BUNDLE_URL = "https://devapi.onboardfy.com/api/tests/bundle";
    public static String STATUS_URL = "https://devapi.onboardfy.com/api/stats";
    static String BUNDLE_NAME="/index.android.bundle";
    static String BUNDLE_FILE_DIR;
    private ProgressDialog dialog;
    public static OnBoard getInstance() {
        return new OnBoard();
    }

    public void startOnBoarding(Context context, Activity activity, OnBoardifySuccessListener listener, JSONObject object) {
        this.context = context;
        this.activity =activity;
        OnBoard.listener = listener;
        BUNDLE_FILE_DIR = context.getApplicationInfo().dataDir+"/.bundle";
        dialog = new ProgressDialog(context);
        dialog.setMessage("Updating Bundle");
        dialog.setCancelable(false);
        dialog.show();
        BundleDownloader.getInstance(this.context).init(object,this);
        Intent onBoardIntent = new Intent(context, OnBoardingActivity.class);
        activity.startActivity(onBoardIntent);
    }

    public static OnBoardifySuccessListener getListener() {
        return listener;
    }

    @Override
    public void onDownloaded(String status) {
        if(status.equals("success")) {
            Log.e("OnBoarding",status);
            Intent onBoardIntent = new Intent(context, OnBoardingActivity.class);
            activity.startActivity(onBoardIntent);
        } else {
            Log.e("OnBoarding",status);
        }
        dialog.cancel();
    }
}
