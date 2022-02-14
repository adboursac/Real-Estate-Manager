package com.openclassrooms.realestatemanager.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

/**
 * Base Activity class that allow to manage all the common code for the activities
 * @param <T> Should be the type of the viewBinding of the activitybinding
 */
abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    abstract T getViewBinding();
    protected T mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
    }

    /**
     * Initialise the binding object and the layout of the activity
     */
    private void initBinding(){
        mBinding = getViewBinding();
        View view = mBinding.getRoot();
        setContentView(view);
    }
}

