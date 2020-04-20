package com.onboardify.DataCommunicator;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommunicationPackages implements ReactPackage {

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {

        List<NativeModule> modules = new ArrayList<>();
        modules.add(new ToastModule(reactContext));
        modules.add(new FacebookCaller(reactContext));
        modules.add(new OnBoardingSuccess(reactContext));
        modules.add(new ImagePicker(reactContext));
        modules.add(new ScreenStatus(reactContext));
        modules.add(new GradientPicker(reactContext));
        modules.add(new GoogleCaller(reactContext));
        modules.add(new EventEmitter(reactContext));
        modules.add(new LinkedInCaller(reactContext));
        return modules;
    }

}
