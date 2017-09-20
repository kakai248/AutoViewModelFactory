package com.kakai.android.autoviewmodelfactory.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.kakai.android.autoviewmodelfactory.annotations.AutoViewModelFactory;
import com.kakai.android.autoviewmodelfactory.data.TestSingleton;

@AutoViewModelFactory
public class MainViewModel extends ViewModel {

    private TestSingleton testSingleton;
    private String testString;

    private MutableLiveData<String> message = new MutableLiveData<>();

    public MainViewModel(TestSingleton testSingleton, String testString) {
        this.testSingleton = testSingleton;
        this.testString = testString;

        Log.d("cenas", "-------------------- viewmodel constructor!");

        message.setValue("Hello World!");
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
