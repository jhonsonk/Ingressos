package com.digitalingressos.ingressos.util;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;

public class PhoneFormatterWatcher implements TextWatcher {

    private TextInputEditText editText;

    public PhoneFormatterWatcher(TextInputEditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Nada a fazer aqui
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Nada a fazer aqui
    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);

        String phoneNumber = s.toString().replaceAll("[^\\d]", "");

        try {
            if (!phoneNumber.isEmpty()) {
                String formattedPhoneNumber = PhoneFormatter.formatPhoneNumber(phoneNumber);
                editText.setText(formattedPhoneNumber);
                editText.setSelection(formattedPhoneNumber.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        editText.addTextChangedListener(this);
    }
}
