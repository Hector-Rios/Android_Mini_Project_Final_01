package com.hrios_practice.android_mini_project_final_01;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class CameraProfileDialogFragment extends DialogFragment {
    public String TAG = "CameraUsageForGoogleProfile";
    AlertDialog.Builder builder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);
        // Use the Builder class for convenient dialog construction
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_prompt_01)
                .setTitle(R.string.dialog_prompt_title)
                .setPositiveButton(R.string.dialog_prompt_02, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User accepts to use camera.
                        System.out.println("* * * User enables Camera usage fpr profile images");
                    }
                })
                .setNegativeButton(R.string.dialog_prompt_03, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        System.out.println("* * * User Does Not allow Camera usage fpr profile images");

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        System.out.println("Attaching ???");
    }

    public FragmentManager getChildFM()
    {
        return getChildFragmentManager();
    }
}
