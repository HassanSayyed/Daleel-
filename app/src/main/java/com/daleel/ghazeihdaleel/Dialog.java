package com.daleel.ghazeihdaleel;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class Dialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        }
        builder.setTitle("Important Notice")
                .setMessage(getString(R.string.dialog1) +". \n"+
                        getString(R.string.dialog2) +". \n"+
                         getString(R.string.dialog3)+"\n"+
                        "daleel.ghazeih@gmail.com")
                 .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                     }
                 });

        return builder.create();


    }
}
