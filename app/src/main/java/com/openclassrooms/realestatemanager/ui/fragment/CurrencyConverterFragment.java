package com.openclassrooms.realestatemanager.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.databinding.FragmentCurrencyConverterBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

public class CurrencyConverterFragment extends Fragment {

    FragmentCurrencyConverterBinding mBinding;
    TextWatcher mDollarWatcher;
    TextWatcher mEuroWatcher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCurrencyConverterBinding.inflate(inflater, container, false);

        initTextInputs();

        return mBinding.getRoot();
    }

    private void initTextInputs() {
        mBinding.dollar.setText(Utils.integerString(0));
        mBinding.euro.setText(Utils.integerString(0));
        mDollarWatcher = dollarWatcher();
        mEuroWatcher = euroWatcher();
        mBinding.dollar.addTextChangedListener(mDollarWatcher);
        mBinding.euro.addTextChangedListener(mEuroWatcher);
    }

    private void setTextSilently(String text, TextInputEditText textInput, TextWatcher textWatcher) {
        textInput.removeTextChangedListener(textWatcher);
        textInput.setText(text);
        textInput.addTextChangedListener(textWatcher);
    }

    private TextWatcher dollarWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    setTextSilently(Utils.integerString(0), mBinding.euro, mEuroWatcher);
                } else {
                    int dollars = Integer.parseInt(mBinding.dollar.getText().toString());
                    setTextSilently(Utils.integerString(Utils.convertDollarToEuro(dollars)), mBinding.euro, mEuroWatcher);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private TextWatcher euroWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    setTextSilently(Utils.integerString(0), mBinding.dollar, mDollarWatcher);
                } else {
                    int euros = Integer.parseInt(mBinding.euro.getText().toString());
                    setTextSilently(Utils.integerString(Utils.convertEuroToDollar(euros)), mBinding.dollar, mDollarWatcher);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }
}
