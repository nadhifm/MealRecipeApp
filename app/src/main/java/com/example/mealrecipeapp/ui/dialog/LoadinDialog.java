package com.example.mealrecipeapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.example.mealrecipeapp.databinding.LoadingDialogBinding;

public class LoadinDialog extends Dialog {

    private final Context context;
    public LoadinDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadingDialogBinding binding = LoadingDialogBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());

        setCancelable(false);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
