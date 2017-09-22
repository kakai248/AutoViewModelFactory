package com.kakai.android.autoviewmodelfactory.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.kakai.android.autoviewmodelfactory.annotations.AutoViewModelFactory;
import com.kakai.android.autoviewmodelfactory.data.TestSingleton;

import javax.inject.Named;

import static com.kakai.android.autoviewmodelfactory.di.modules.activities.MainActivityModule.ANOTHER_TEST_STRING;
import static com.kakai.android.autoviewmodelfactory.di.modules.activities.MainActivityModule.TEST_STRING;

@AutoViewModelFactory
public class MainViewModel extends ViewModel {

    private TestSingleton testSingleton;
    private String testString;
    private String anotherTestString;

    private MutableLiveData<String> message = new MutableLiveData<>();

    public MainViewModel(TestSingleton testSingleton,
                         @Named(TEST_STRING) String testString,
                         @Named(ANOTHER_TEST_STRING) String anotherTestString) {
        this.testSingleton = testSingleton;
        this.testString = testString;
        this.anotherTestString = anotherTestString;

        message.setValue("Hello World!");
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
