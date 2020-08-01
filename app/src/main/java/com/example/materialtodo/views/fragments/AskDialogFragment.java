package com.example.materialtodo.views.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.actionConfirmed();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
    }

    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnPositiveButtonClickListener {
        void actionConfirmed();
    }
}
