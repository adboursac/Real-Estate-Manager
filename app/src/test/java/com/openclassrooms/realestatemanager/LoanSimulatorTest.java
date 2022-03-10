package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoanSimulatorTest {

    private final double loanAmount = 1200;
    private final double interestRate = 10;
    private final double duration = 1;
    private final double monthlyPayment = 105.5;
    private final double loanCost = 67;

    @Test
    public void calculateMonthlyPayment_test() {

        assertEquals(monthlyPayment, Utils.calculateMonthlyPayment(interestRate, duration*12, loanAmount), 2);
    }

    @Test
    public void calculateLoanCost_test() {

        assertEquals(loanCost, Utils.calculateLoanCost(monthlyPayment, duration*12, loanAmount), 2);
    }
}