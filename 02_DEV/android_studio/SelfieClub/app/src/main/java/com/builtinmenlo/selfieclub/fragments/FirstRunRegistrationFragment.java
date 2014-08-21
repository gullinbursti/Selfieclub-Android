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
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.activity.MainActivity;
import com.builtinmenlo.selfieclub.models.ClubManager;
import com.builtinmenlo.selfieclub.models.CreateClubProtocol;
import com.builtinmenlo.selfieclub.models.FirstRunManager;
import com.builtinmenlo.selfieclub.models.FirstRunProtocol;
import com.builtinmenlo.selfieclub.models.PINVerificationProtocol;
import com.builtinmenlo.selfieclub.models.SCDialogProtocol;
import com.couchbase.lite.Context;

import java.util.Random;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class FirstRunRegistrationFragment extends Fragment implements FirstRunProtocol, SCDialogProtocol, PINVerificationProtocol, CreateClubProtocol{
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_ID = "user_id";
    public static final String EXTRA_EMAIL = "user_email";


    //] class properties ]>
    private Bundle bundle;
    private ProgressDialog dialog;
    private static String freeUserId;
    private FirstRunManager manager;
    private EditText txtUsername;
    private EditText txtPhone;

    private static String FAILED_VALIDATING_TAG = "validation_failed";
    private static String FAILED_VALIDATING_PIN_TAG = "validation_pin_failed";
    private static String FIELDS_NOT_FILLED_TAG = "fields_not fill";

    //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

    // <*] class constructor [*>
    public FirstRunRegistrationFragment() {/*..\(^_^)/..*/}

    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=[>
    //]~=~=~=~=~=~=~=~=~=[>

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        View view = inflater.inflate(R.layout.first_run_registration, container, false);

        bundle = getArguments();

        dialog = ProgressDialog.show(getActivity(), "", getString(R.string.label_please_wait));


        manager = new FirstRunManager();
        manager.requestFreeUserId(this);

        String countryCode = "";
        String username = "";
        //byte[] avatarImage = null;
        if (bundle != null) {
            countryCode = bundle.getString(FirstRunCountrySelectorFragment.EXTRA_CODE);
            username = bundle.getString(EXTRA_USERNAME);
            //avatarImage = getArguments().getByteArray(CameraPreview.EXTRA_IMAGE);
        }




        /*ImageView avatar = (ImageView) view.findViewById(R.id.imgAvatar);
        if (avatarImage != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(avatarImage, 0, avatarImage.length);
            avatar.setImageBitmap(bmp);
            //mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        }
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity(), CameraPreview.class);
                //startActivity(intent);
                Fragment newFragment = new CameraFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                if (bundle == null)
                    bundle = new Bundle();
                newFragment.setArguments(bundle);
                transaction.commit();
            }
        });*/

        txtUsername = (EditText) view.findViewById(R.id.txtUserName);
        if (username != null && username.length() > 0)
            txtUsername.setText(username);

        final Button btnCountrySelector = (Button) view.findViewById(R.id.btnCountrySelect);
        if (countryCode != null && countryCode.length() > 0)
            btnCountrySelector.setText(countryCode);
        btnCountrySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new FirstRunCountrySelectorFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putString(EXTRA_USERNAME, txtUsername.getText().toString());
                newFragment.setArguments(bundle);
                transaction.commit();
            }
        });

        txtPhone = (EditText) view.findViewById(R.id.txtEnterPhone);
        txtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    //manager.usernameAndPhoneCheck(FirstRunRegistrationFragment.this,freeUserId,txtUsername.getText().toString(),btnCountrySelector.getText().toString()+txtPhone.getText().toString());
                    return true;
                }
                return false;
            }
        });

        view.findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtPhone.getText().length() > 0 && txtUsername.getText().length() > 0) {
                    getActivity().findViewById(R.id.imgCheckUserName).setVisibility(View.INVISIBLE);
                    dialog = ProgressDialog.show(getActivity(), "", getString(R.string.label_checking_username));
                    manager.usernameAndPhoneCheck(FirstRunRegistrationFragment.this, freeUserId, txtUsername.getText().toString(), btnCountrySelector.getText().toString() + txtPhone.getText().toString() + "@selfieclub.com");
                } else {
                    SCDialog scdialog = new SCDialog();
                    scdialog.setScDialogProtocol(FirstRunRegistrationFragment.this);
                    scdialog.setMessage(getString(R.string.label_fill_phone_username));
                    scdialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
                    scdialog.show(getFragmentManager(), FIELDS_NOT_FILLED_TAG);
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
        freeUserId = userId;
        dialog.dismiss();
    }

    @Override
    public void didFailReceivingFreeUserId(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {

    }

    @Override
    public void didValidateUsernamePhone(Boolean isValid, String message) {
        dialog.dismiss();
        System.err.println(message);
        if (isValid) {
            getActivity().findViewById(R.id.imgCheckUserName).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.imgCheckPhone).setVisibility(View.VISIBLE);
            SharedPreferences preferences = getActivity().getSharedPreferences("prefs",
                    Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(FirstRunRegistrationFragment.EXTRA_ID, freeUserId);
            editor.apply();


            ClubManager clubManager = new ClubManager();
            Random rn = new Random();
            int rnd = rn.nextInt(10)+1;
            String suffix="10";
            if(rnd!=10){
                suffix="0"+rnd;
            }
            String clubAvatar = String.format("http://hotornot-challenges.s3.amazonaws.com/pc-0%sMedium_320x320.jpg", suffix);
            clubManager.createClub(this,freeUserId,txtUsername.getText().toString(),"Personal club",clubAvatar);
        }
    }
    @Override
    public void didCreateClub(){
        TelephonyManager tMgr =(TelephonyManager)this.getActivity().getSystemService(this.getActivity().TELEPHONY_SERVICE);
        String mPhoneNumber = txtPhone.getText().toString();
        FirstRunManager manager = new FirstRunManager();
        manager.registerUser(this, freeUserId, txtUsername.getText().toString(), mPhoneNumber, mPhoneNumber,"https://s3.amazonaws.com/hotornot-avatars/defaultAvatar");



    }
    @Override
    public void didFailCreatingClub(String errorMessage){
        Log.w("FirstRunRegistrationFragment",errorMessage);
    }

    @Override
    public void didFailValidatingUsernamePhone(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {
        dialog.dismiss();
        SCDialog scdialog = new SCDialog();
        scdialog.setScDialogProtocol(this);
        scdialog.setMessage(message);
        scdialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
        scdialog.show(getFragmentManager(), FAILED_VALIDATING_TAG);
    }

    @Override
    public void didRegisteredUser(String userId) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void didFailRegisteringUser(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {
        Log.w("FirstRunRegistrationFragment",message);
    }

    @Override
    public void didClickedButton(String dialogTag, int buttonIndex) {
        if (dialogTag.equalsIgnoreCase(FAILED_VALIDATING_TAG)) {
            if (buttonIndex == 1) {

            }
        }
    }

    @Override
    public void didSendPIN(Boolean result) {
        dialog.dismiss();
        Fragment newFragment = new FirstRunEnterPinFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        if (bundle == null)
            bundle = new Bundle();
        bundle.putString(EXTRA_ID, freeUserId);
        bundle.putString(EXTRA_USERNAME, txtUsername.getText().toString());
        bundle.putString(EXTRA_EMAIL, txtPhone.getText().toString() + "@selfieclub.com");
        newFragment.setArguments(bundle);
        transaction.commit();
    }

    @Override
    public void didFailSendingPIN(String message) {
        dialog.dismiss();
        SCDialog scdialog = new SCDialog();
        scdialog.setScDialogProtocol(this);
        scdialog.setMessage(message);
        scdialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
        scdialog.show(getFragmentManager(), FAILED_VALIDATING_PIN_TAG);
    }

    @Override
    public void didValidatePIN(Boolean result) {

    }

    @Override
    public void didFailValidatingPIN(String message) {

    }
}
