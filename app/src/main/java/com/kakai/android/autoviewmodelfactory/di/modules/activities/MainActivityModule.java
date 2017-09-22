package com.kakai.android.autoviewmodelfactory.di.modules.activities;

import android.support.v7.app.AppCompatActivity;

import com.kakai.android.autoviewmodelfactory.di.scopes.ActivityScope;
import com.kakai.android.autoviewmodelfactory.presentation.MainActivity;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MainActivityModule {

    public static final String TEST_STRING = "testString";
    public static final String ANOTHER_TEST_STRING = "anotherTestString";

    @Binds
    abstract AppCompatActivity activity(MainActivity activity);

    @Provides
    @Named(TEST_STRING)
    @ActivityScope
    static String provideTestString(MainActivity activity) {
        return activity.getTestString();
    }

    @Provides
    @Named(ANOTHER_TEST_STRING)
    @ActivityScope
    static String provideAnotherTestString(MainActivity activity) {
        return activity.getAnotherTestString();
    }
}