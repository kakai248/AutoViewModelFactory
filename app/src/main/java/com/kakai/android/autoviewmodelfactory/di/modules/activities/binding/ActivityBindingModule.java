package com.kakai.android.autoviewmodelfactory.di.modules.activities.binding;

import com.kakai.android.autoviewmodelfactory.di.modules.activities.MainActivityModule;
import com.kakai.android.autoviewmodelfactory.di.scopes.ActivityScope;
import com.kakai.android.autoviewmodelfactory.presentation.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();
}