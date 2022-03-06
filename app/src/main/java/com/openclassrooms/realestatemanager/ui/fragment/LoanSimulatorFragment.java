package com.openclassrooms.realestatemanager.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.databinding.FragmentCurrencyConverterBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentLoanSimulatorBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.DecimalFormat;

public class LoanSimulatorFragment extends Fragment {

    private FragmentLoanSimulatorBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentLoanSimulatorBinding.inflate(inflater, container, false);

        initTextFields();
        initCalculateButton();

        return mBinding.getRoot();
    }

    public void initTextFields() {
        mBinding.monthlyPayment.setText("_");
        mBinding.creditCost.setText("_");
    }

    public void initCalculateButton() {
        mBinding.button.setOnClickListener(view -> calculate());
    }

    public void calculate() {

        double loanAmount = Integer.parseInt(mBinding.loanAmount.getText().toString());
        double interestRate = Double.parseDouble(String.valueOf(mBinding.interestRate.getText()));
        double duration = Integer.parseInt(mBinding.duration.getText().toString()) * 12;

        double monthlyPayment = Utils.calculateMonthlyPayment(interestRate, duration, loanAmount);
        double loanCost = Utils.calculateLoanCost(monthlyPayment, duration, loanAmount);

        mBinding.monthlyPayment.setText(new DecimalFormat("##.##").format(monthlyPayment));
        mBinding.creditCost.setText(new DecimalFormat("##.##").format(loanCost));
    }

}
