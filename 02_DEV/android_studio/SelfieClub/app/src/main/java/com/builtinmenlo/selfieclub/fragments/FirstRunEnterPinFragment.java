/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class SettingsTabBtnFragment
 * @project SelfieClub
 * @package com.builtinmenlo.selfieclub.fragments
 *
 * @author Matt.H <matt@builtinmenlo.com>
 * @created 16-Apr-2014 @ 16:11
 * @copyright (c) 2014 Built in Menlo, LLC. All rights reserved.
 *
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~·'
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~**/



package com.builtinmenlo.selfieclub.fragments;


//] includes [!]>
//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.activity.MainActivity;
import com.builtinmenlo.selfieclub.models.ApplicationManager;
import com.builtinmenlo.selfieclub.models.FirstRunManager;
import com.builtinmenlo.selfieclub.models.FirstRunProtocol;
import com.builtinmenlo.selfieclub.models.PINVerificationProtocol;
import com.builtinmenlo.selfieclub.models.SCDialogProtocol;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class FirstRunEnterPinFragment extends Fragment implements FirstRunProtocol, SCDialogProtocol, PINVerificationProtocol {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

	//] class properties ]>
    private ProgressDialog dialog;

    private String userId;
    private String username;
    private String email;
    private EditText txtPin;

    private static String NO_PIN_TAG = "no_pin";
    private static String FAILED_PIN_TAG = "failed_pin";

    // <*] class constructor [*>
    public FirstRunEnterPinFragment() {/*..\(^_^)/..*/}

	//]~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=[>
	//]~=~=~=~=~=~=~=~=~=[>

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        View view = inflater.inflate(R.layout.first_run_enter_pin, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId =  bundle.getString(FirstRunRegistrationFragment.EXTRA_ID);
            username = bundle.getString(FirstRunRegistrationFragment.EXTRA_USERNAME);
            email = bundle.getString(FirstRunRegistrationFragment.EXTRA_EMAIL);
        }

        txtPin = (EditText)view.findViewById(R.id.txtEnterPin);

        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtPin.getText().length() > 0) {
                    FirstRunManager manager = new FirstRunManager();
                    String[] tokens = email.split("@");
                    String phone = tokens[0];
                    dialog = ProgressDialog.show(getActivity(), "", "Validating PIN...");
                    manager.validatePIN(FirstRunEnterPinFragment.this,userId,phone,txtPin.getText().toString(),getActivity());
                } else {
                    String message = "You need to enter the PIN";
                    SCDialog dialog = new SCDialog();
                    dialog.setScDialogProtocol(FirstRunEnterPinFragment.this);
                    dialog.setMessage(message);
                    dialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
                    dialog.show(getFragmentManager(),NO_PIN_TAG);
                }
            }
        });
		return view;
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯


	public void onCreate(Bundle savedInstanceState) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		super.onCreate(savedInstanceState);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	public void onAttach(Activity activity) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		super.onAttach(activity);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	public void onDetach() {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		super.onDetach();
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    @Override
    public void didReceiveFreeUserId(String userId) {

    }

    @Override
    public void didFailReceivingFreeUserId(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {

    }

    @Override
    public void didValidateUsernamePhone(Boolean isValid, String message) {

    }

    @Override
    public void didFailValidatingUsernamePhone(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {

    }

    @Override
    public void didRegisteredUser(String userId) {
        dialog.dismiss();
        ApplicationManager applicationManager = new ApplicationManager(getActivity());
        applicationManager.setUserId(userId);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void didFailRegisteringUser(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {
        dialog.dismiss();
        SCDialog scdialog = new SCDialog();
        scdialog.setScDialogProtocol(FirstRunEnterPinFragment.this);
        scdialog.setMessage(message);
        scdialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
        scdialog.show(getFragmentManager(),NO_PIN_TAG);
    }

    @Override
    public void didClickedButton(String dialogTag, int buttonIndex) {

    }

    @Override
    public void didSendPIN(Boolean result) {

    }

    @Override
    public void didFailSendingPIN(String message) {

    }

    @Override
    public void didValidatePIN(Boolean result) {
        dialog.dismiss();
        if (result) {
            FirstRunManager manager = new FirstRunManager();
            dialog = ProgressDialog.show(getActivity(), "", getString(R.string.label_register_user));
            manager.registerUser(FirstRunEnterPinFragment.this, userId, username, email, txtPin.getText().toString(), "",this.getActivity());
        }
    }

    @Override
    public void didFailValidatingPIN(String message) {
        dialog.dismiss();
        SCDialog scdialog = new SCDialog();
        scdialog.setScDialogProtocol(FirstRunEnterPinFragment.this);
        scdialog.setMessage(message);
        scdialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
        scdialog.show(getFragmentManager(),FAILED_PIN_TAG);
    }
}
