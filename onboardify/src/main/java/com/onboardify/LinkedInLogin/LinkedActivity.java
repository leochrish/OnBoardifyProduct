package com.onboardify.LinkedInLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
/*
import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.listeners.DeepLinkListener;
import com.linkedin.platform.utils.Scope;*/
import com.onboardify.onboardify.R;

public class LinkedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked);
        /*LISessionManager.getInstance(getApplicationContext()).init(this,
                buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(getApplicationContext(),"Error : "+error,Toast.LENGTH_LONG).show();
                    }
                },true);
        DeepLinkHelper helper = DeepLinkHelper.getInstance();
        helper.openCurrentProfile(this, new DeepLinkListener() {
            @Override
            public void onDeepLinkSuccess() {
                Toast.makeText(getApplicationContext(),"LINK--test",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDeepLinkError(LIDeepLinkError error) {
                Toast.makeText(getApplicationContext(),"LINK : "+error,Toast.LENGTH_LONG).show();
            }
        });*/
    }
    /*private Scope buildScope() {
        return Scope.build(Scope.R_EMAILADDRESS);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
