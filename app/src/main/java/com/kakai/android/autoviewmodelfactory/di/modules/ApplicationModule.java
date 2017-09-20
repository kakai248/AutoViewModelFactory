package com.kakai.android.autoviewmodelfactory.di.modules;

import android.app.Application;
import android.content.Context;

import com.kakai.android.autoviewmodelfactory.App;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ApplicationModule {

    @Binds
    abstract Application application(App app);

    @Provides
    @Singleton
    static Context provideContext(Application application) {
        return application;
    }
}