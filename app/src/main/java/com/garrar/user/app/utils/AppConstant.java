package com.garrar.user.app.utils;

import android.content.Context;

import com.aapbd.appbajarlib.storage.PersistData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AppConstant {

    private static final String CURRENTLANGUAGE = "CURRENTLANGUAGE";

    public static String phone = "";

    public static String SELECTED_LANGUAGE = null;


    public static String getCurrentLanguage(Context con)
    {


        return PersistData.getStringData(con, AppConstant.CURRENTLANGUAGE);

    }

    public static void setCurrentLanguage(Context con, String langaugecode)
    {


        PersistData.setStringData(con, AppConstant.CURRENTLANGUAGE, langaugecode);

    }

}
