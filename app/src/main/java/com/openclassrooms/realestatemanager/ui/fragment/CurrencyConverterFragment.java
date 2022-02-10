package com.openclassrooms.realestatemanager.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.databinding.FragmentCurrencyConverterBinding;

public class CurrencyConverterFragment extends Fragment {

    FragmentCurrencyConverterBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCurrencyConverterBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }
}
