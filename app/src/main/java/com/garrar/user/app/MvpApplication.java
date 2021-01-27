package com.garrar.user.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.stetho.Stetho;
import com.garrar.user.app.common.ConnectivityReceiver;
import com.garrar.user.app.common.LocaleHelper;
import com.garrar.user.app.data.network.model.Datum;


import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import okhttp3.RequestBody;


public class MvpApplication extends Application {

    private static MvpApplication mInstance;

    public static boolean canGoToChatScreen;
    public static boolean isChatScreenOpen;

    public static boolean isCash = true;
    public static boolean isCard = true;
    public static boolean isPayumoney;
    public static boolean isPaypal;
    public static boolean isPaytm;
    public static boolean isPaypalAdaptive;
    public static boolean isBraintree;
    public static boolean openChatFromNotification = true;

    public static HashMap<String, Object> RIDE_REQUEST = new HashMap<>();
    public static HashMap<String, RequestBody> PACKAGE_DETAIL = new HashMap<>();
    public static HashMap<String, String> PACKAGE_DETAIL1 = new HashMap<>();
    public static String total_stop;
    public static File imgFile = null;
    public static Datum DATUM = null;
    public static boolean showOTP = true;

    public static synchronized MvpApplication getInstance() {
        return mInstance;
    }

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        MvpApplication.context = getApplicationContext();

        mInstance = this;

        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Crashlytics crashlytics = new Crashlytics.Builder().core(core).build();
        Fabric.with(this, crashlytics, new Crashlytics());

        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this);

        MultiDex.install(this);

        getHashkey();
    }


    public static Context getContext(){
        return MvpApplication.context;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
        MultiDex.install(newBase);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void getHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.i("Base64", Base64.encodeToString(md.digest(),Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

}
