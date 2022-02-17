package com.openclassrooms.realestatemanager.utils;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class DatePicker {

    public static void setDatePickerOnTextInput(TextInputEditText textInput, FragmentManager fragmentManager) {
        textInput.setOnClickListener(v -> openDatePicker(textInput, fragmentManager));
    }

    public static void openDatePicker(TextInputEditText textInput, FragmentManager fragmentManager) {
        LocalDate date = Utils.stringToDate(textInput.getText().toString(), LocalDate.now());
        long epochDate = LocalDateTime.of(date, LocalTime.of(2,0))
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        MaterialDatePicker picker = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(R.string.title_pick_a_date)
                .setSelection(epochDate)
                .build();

        picker.show(fragmentManager, null);

        picker.addOnPositiveButtonClickListener(v -> {
            LocalDate localDate = Instant.ofEpochMilli((long) picker.getSelection())
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            textInput.setText(Utils.dateToString(localDate));
        });
    }
}
