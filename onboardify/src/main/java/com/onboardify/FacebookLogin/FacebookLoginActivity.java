package com.onboardify.FacebookLogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.onboardify.onboardify.R;


public class FacebookLoginActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager manager;
    AccessToken token;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        manager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        LoginManager.getInstance().registerCallback(manager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                //Toast.makeText(getApplicationContext(), "" + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                sendResult(loginResult.getAccessToken().getToken());
                Log.e("sample", loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                Log.e("sample", "Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_LONG).show();
            }

        });
        token = AccessToken.getCurrentAccessToken();
        if(token!=null) {
            sendResult(token.getToken());
            //Toast.makeText(getApplicationContext(), "" + token.getToken(), Toast.LENGTH_LONG).show();
        }else {
            loginButton.callOnClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        manager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void sendResult(String result){
        preferences = getSharedPreferences("onBoard",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("faceToken",""+result);
        editor.apply();
        editor.commit();
        finish();
    }
}