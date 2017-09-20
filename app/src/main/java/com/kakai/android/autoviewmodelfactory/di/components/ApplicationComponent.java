package com.kakai.android.autoviewmodelfactory.di.components;

import com.kakai.android.autoviewmodelfactory.App;
import com.kakai.android.autoviewmodelfactory.di.modules.ApplicationModule;
import com.kakai.android.autoviewmodelfactory.di.modules.activities.binding.ActivityBindingModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        ApplicationModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
@Singleton
interface ApplicationComponent extends AndroidInjector<App> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {
    }
}