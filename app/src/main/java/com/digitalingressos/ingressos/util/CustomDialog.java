package com.digitalingressos.ingressos.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.digitalingressos.ingressos.databinding.CustomDialogBinding;

import io.reactivex.annotations.NonNull;

public class CustomDialog extends Dialog {

    public CustomDialog(@NonNull Context context) {
        super(context);
    }
    private CustomDialogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CustomDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        clickButtons();
    }

    public void setMessage(String message) {
        binding.textviewMessage.setText(message);
    }

    @Override
    public void setTitle(CharSequence title) {
        binding.textviewTitle.setText(title);
    }

    public void setLabelButtonCancel (String message){
        binding.btnCancel.setText(message);
    }

    private void clickButtons() {
        binding.btnCancel.setOnClickListener(click ->
                cancel()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
