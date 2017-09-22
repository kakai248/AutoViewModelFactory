package com.kakai.android.autoviewmodelfactory.presentation;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.kakai.android.autoviewmodelfactory.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    MainViewModelFactory viewModelFactory;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        viewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                ((TextView) findViewById(R.id.message)).setText(message);
            }
        });
    }

    public String getTestString() {
        return "test";
    }

    public String getAnotherTestString() {
        return "another test";
    }
}
