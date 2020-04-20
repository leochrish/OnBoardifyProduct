package com.onboardify.OnBoardingHelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.RequiresApi;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.CONNECTIVITY_SERVICE;

class BundleDownloader {

    private URL url;
    private HttpsURLConnection connection;
    private Context context;
    private OutputStream outputStream;
    private File bundleFile;
    private BundleDownloadedListener listener;
    private JSONObject jsonObject;

    static BundleDownloader getInstance(Context context) {
        return new BundleDownloader(context);
    }

    private BundleDownloader(Context context) {
        try {
            this.context = context;
            url = new URL(OnBoard.BUNDLE_URL);

            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("Content-Type","application/json");

        } catch (Exception e) {
            Log.e("Exception", "" + e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void init(JSONObject object,final BundleDownloadedListener listener) {
        this.listener = listener;
        jsonObject = object;
        if (isNetworkAvailable()) {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    synchronized (this) {
                        setupDirectory();
                        downloadBundle();
                    }
                    Looper.loop();
                }
            }.start();
        } else {
            listener.onDownloaded("201");
        }
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                    CONNECTIVITY_SERVICE);
            return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            Log.e("Error----", "" + e);
        }
        return false;
    }

    private void setupDirectory() {
        try {
            File directory = new File(OnBoard.BUNDLE_FILE_DIR);
            if (!directory.exists()) {
                Log.e("sample---",""+directory.mkdir());
            }
        } catch (Exception e) {
            Log.e("Test", "" + e);
        }
    }

    private void downloadBundle() {
        try {
            if (connection != null) {
                outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();
                outputStream.close();

                bundleFile = new File(OnBoard.BUNDLE_FILE_DIR + OnBoard.BUNDLE_NAME);
                outputStream = new FileOutputStream(bundleFile);

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    byte[] buffer = new byte[1024];
                    int bufferLength;
                    InputStream inputStream = connection.getInputStream();
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, bufferLength);
                    }
                    outputStream.close();
                    Log.e("BUNDLE", "Finished");
                    listener.onDownloaded("success");
                } else {
                    listener.onDownloaded(""+connection.getResponseCode()+" : "+connection.getResponseMessage());
                }
                outputStream.close();
            }else {
                listener.onDownloaded("Error");
            }
        } catch (IOException e) {
            Log.e("Exception", "" + e);
            listener.onDownloaded(""+e);
        }
    }
}
