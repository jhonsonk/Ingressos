package com.digitalingressos.ingressos.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CPFormatter implements TextWatcher {

    private static final String CPF_MASK = "###.###.###-##";
    private EditText editText;

    public CPFormatter(EditText editText) {
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

        String cpf = s.toString().replaceAll("[^\\d]", "");

        try {
            if (!cpf.isEmpty()) {
                String maskedCpf = applyCPFMask(cpf);
                editText.setText(maskedCpf);
                editText.setSelection(maskedCpf.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        editText.addTextChangedListener(this);
    }

    private String applyCPFMask(String cpf) {
        StringBuilder maskedCpf = new StringBuilder();

        int i = 0;
        for (char c : CPF_MASK.toCharArray()) {
            if (c == '#') {
                maskedCpf.append(cpf.charAt(i));
                i++;
            } else {
                maskedCpf.append(c);
            }
        }

        return maskedCpf.toString();
    }
}