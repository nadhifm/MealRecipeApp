package com.example.mealrecipeapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.example.mealrecipeapp.databinding.LoadingDialogBinding;

public class LoadinDialog extends Dialog {

    private Context context;
    private LoadingDialogBinding binding;
    public LoadinDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoadingDialogBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());

        setCancelable(false);
    }
}
