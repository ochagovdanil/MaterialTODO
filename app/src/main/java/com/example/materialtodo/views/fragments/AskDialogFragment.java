package com.example.materialtodo.views.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.materialtodo.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AskDialogFragment extends DialogFragment {

    private OnPositiveButtonClickListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(getContext())
                .setCancelable(true)
                .setTitle(R.string.ask_dialog_title)
                .setMessage(R.string.ask_dialog_description)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    if (listener != null) {
                        listener.actionConfirmed();
                    }
                }).setNegativeButton("No", (dialogInterface, i) ->
                        dialogInterface.dismiss()).create();
    }

    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnPositiveButtonClickListener {
        void actionConfirmed();
    }
}
