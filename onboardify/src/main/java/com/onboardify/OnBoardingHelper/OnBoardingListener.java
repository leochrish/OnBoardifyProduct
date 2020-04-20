package com.onboardify.OnBoardingHelper;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class OnBoardingListener {

    private URL url;
    private HttpURLConnection connection;
    private Context context;
    private OutputStream outputStream;

    public OnBoardingListener(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        try {
            url = new URL(OnBoard.STATUS_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
        } catch (Exception e) {
            Log.e("Exception", "" + e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendData(final JSONObject jsonObject) {
        new Thread(){
            @Override
            public void run() {
                synchronized (this){
                    try {
                        if (connection != null) {
                            String response = "";
                            outputStream = connection.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                            writer.write(jsonObject.toString());
                            writer.flush();
                            writer.close();
                            outputStream.close();
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                String line;
                                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                while ((line = br.readLine()) != null) {
                                    response += line;
                                }
                                JSONObject jsonResponse = new JSONObject(response);
                                Log.e("Response",""+jsonResponse.getString("message"));
                            }else {
                                Log.e("ResponseError",""+connection.getResponseMessage());
                            }
                        }
                    } catch (IOException | JSONException e) {
                        Log.e("Exception", "" + e);
                    }
                }
            }
        }.start();
    }
}