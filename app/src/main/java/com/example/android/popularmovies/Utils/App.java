package com.example.android.popularmovies.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.android.popularmovies.di.AppComponent;
import com.example.android.popularmovies.di.AppModule;
import com.example.android.popularmovies.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class App extends Application implements HasActivityInjector{

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    public static Context context;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initDagger();
        context = getApplicationContext();

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    private void initDagger() {
        DaggerAppComponent.builder().application(this).appModule(new AppModule(this)).build().inject(this);
    }
}
