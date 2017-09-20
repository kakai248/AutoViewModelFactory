package com.kakai.android.autoviewmodelfactory.di.modules.activities;

import android.support.v7.app.AppCompatActivity;

import com.kakai.android.autoviewmodelfactory.di.scopes.ActivityScope;
import com.kakai.android.autoviewmodelfactory.presentation.MainActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MainActivityModule {

    @Binds
    abstract AppCompatActivity activity(MainActivity activity);

    @Provides
    @ActivityScope
    static String provideTestString(MainActivity activity) {
        return activity.getTestString();
    }
}