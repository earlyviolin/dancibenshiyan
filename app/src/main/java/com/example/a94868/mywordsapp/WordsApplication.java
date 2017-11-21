package com.example.a94868.mywordsapp;

import android.app.Application;
import android.content.Context;


public class WordsApplication extends Application{
    private static Context context;
    public static Context getContext(){
        return WordsApplication.context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        WordsApplication.context=getApplicationContext();
    }
}
