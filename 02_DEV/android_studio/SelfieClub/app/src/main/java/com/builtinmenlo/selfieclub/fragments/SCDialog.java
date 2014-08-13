package com.builtinmenlo.selfieclub.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.content.DialogInterface;

import com.builtinmenlo.selfieclub.models.SCDialogProtocol;

/**
 * Created by Leonardo on 8/12/14.
 */
public class SCDialog extends DialogFragment {
    private String message;
    private String positiveButtonTitle;
    private String negativeButtonTitle;
    private SCDialogProtocol scDialogProtocol;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(this.getMessage())
                .setPositiveButton(this.positiveButtonTitle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(getScDialogProtocol() != null)
                            getScDialogProtocol().didClickedButton(getTag(), 1);
                    }
                })
                .setNegativeButton(this.negativeButtonTitle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(getScDialogProtocol() != null)
                            getScDialogProtocol().didClickedButton(getTag(), 0);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    public String getPositiveButtonTitle() {
        return positiveButtonTitle;
    }

    public void setPositiveButtonTitle(String positiveButtonTitle) {
        this.positiveButtonTitle = positiveButtonTitle;
    }

    public String getNegativeButtonTitle() {
        return negativeButtonTitle;
    }

    public void setNegativeButtonTitle(String negativeButtonTitle) {
        this.negativeButtonTitle = negativeButtonTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SCDialogProtocol getScDialogProtocol() {
        return scDialogProtocol;
    }

    public void setScDialogProtocol(SCDialogProtocol scDialogProtocol) {
        this.scDialogProtocol = scDialogProtocol;
    }
}
