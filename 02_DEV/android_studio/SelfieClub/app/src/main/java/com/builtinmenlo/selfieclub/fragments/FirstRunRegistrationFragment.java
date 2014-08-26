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

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.activity.MainActivity;
import com.builtinmenlo.selfieclub.models.ApplicationManager;
import com.builtinmenlo.selfieclub.models.ClubInviteProtocol;
import com.builtinmenlo.selfieclub.models.ClubManager;
import com.builtinmenlo.selfieclub.models.CreateClubProtocol;
import com.builtinmenlo.selfieclub.models.FirstRunManager;
import com.builtinmenlo.selfieclub.models.FirstRunProtocol;
import com.builtinmenlo.selfieclub.models.PINVerificationProtocol;
import com.builtinmenlo.selfieclub.models.PhoneManager;
import com.builtinmenlo.selfieclub.models.SCDialogProtocol;
import com.builtinmenlo.selfieclub.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class FirstRunRegistrationFragment
        extends Fragment
        implements FirstRunProtocol,
        SCDialogProtocol,
        PINVerificationProtocol,
        CreateClubProtocol,
        ClubInviteProtocol{
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
    private final static String INVITE_RANDOM_FRIENDS_TAG = "invite_random";

    //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

    // <*] class constructor [*>
    public FirstRunRegistrationFragment() {/*..\(^_^)/..*/}

    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=[>
    //]~=~=~=~=~=~=~=~=~=[>


    @Override
    public void onResume() {
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        View view = inflater.inflate(R.layout.first_run_registration, container, false);

        bundle = getArguments();

        dialog = ProgressDialog.show(getActivity(), "", getString(R.string.label_please_wait));


        manager = new FirstRunManager();
        manager.requestFreeUserId(this,this.getActivity());

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
                    manager.usernameAndPhoneCheck(FirstRunRegistrationFragment.this, freeUserId, txtUsername.getText().toString(), btnCountrySelector.getText().toString() + txtPhone.getText().toString() + "@selfieclub.com",getActivity());
                } else {
                    SCDialog scdialog = new SCDialog();
                    scdialog.setScDialogProtocol(FirstRunRegistrationFragment.this);
                    scdialog.setMessage(getString(R.string.label_fill_phone_username));
                    scdialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
                    scdialog.showTwoButtons = true;
                    scdialog.show(getFragmentManager(), FIELDS_NOT_FILLED_TAG);
                }
            }
        });

        view.findViewById(R.id.lblTermsText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebviewFragment newFragment = new WebviewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, newFragment);
                Bundle bundle = new Bundle();
                bundle.putString(WebviewFragment.EXTRA_URL_ITEM, "http://www.getselfieclub.com/terms.html");
                newFragment.setArguments(bundle);
                transaction.commit();
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
        ActionBar topNavActionBar = getActivity().getActionBar();
        topNavActionBar.hide();
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onDetach() {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onDetach();
        ActionBar topNavActionBar = getActivity().getActionBar();
        topNavActionBar.show();
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    @Override
    public void didReceiveFreeUserId(String userId) {
        freeUserId = userId;
        dialog.dismiss();
    }

    @Override
    public void didFailReceivingFreeUserId(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {
        manager.requestFreeUserId(this,this.getActivity());
    }

    @Override
    public void didValidateUsernamePhone(Boolean isValid, String message) {
        dialog.dismiss();
        if (isValid) {
            getActivity().findViewById(R.id.imgCheckUserName).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.imgCheckPhone).setVisibility(View.VISIBLE);
            ApplicationManager applicationManager = new ApplicationManager(getActivity());
            applicationManager.setUserId(freeUserId);
            applicationManager.setUserName(txtUsername.getText().toString());

            ClubManager clubManager = new ClubManager();
            Random rn = new Random();
            int rnd = rn.nextInt(10)+1;
            String suffix="10";
            if(rnd!=10){
                suffix="0"+rnd;
            }
            String clubAvatar = String.format("http://hotornot-challenges.s3.amazonaws.com/pc-0%sMedium_320x320.jpg", suffix);
            clubManager.createClub(this,freeUserId,txtUsername.getText().toString(),"Personal club",clubAvatar,this.getActivity());
        }
    }
    @Override
    public void didCreateClub(String clubId,String clubName){
        //Store the user's personal club info
        ApplicationManager applicationManager = new ApplicationManager(this.getActivity());
        applicationManager.setUserPersonalClubId(clubId);
        String mPhoneNumber = txtPhone.getText().toString();
        FirstRunManager manager = new FirstRunManager();
        manager.registerUser(this, freeUserId, txtUsername.getText().toString(), mPhoneNumber, mPhoneNumber,"https://s3.amazonaws.com/hotornot-avatars/defaultAvatar",this.getActivity());



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
        showInviteDialog();
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
        if (dialogTag.equalsIgnoreCase(INVITE_RANDOM_FRIENDS_TAG)) {
            if (buttonIndex == 1) {
                inviteRandomFriends();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
            else{
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
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

    public void showInviteDialog() {
        String message = getResources().getString(R.string.would_you_like_invite_friends_dialog);
        SCDialog dialog = new SCDialog();
        dialog.setScDialogProtocol(this);
        dialog.showTwoButtons = true;
        dialog.setMessage(message);
        dialog.setPositiveButtonTitle(getResources().getString(R.string.yes_button_title));
        dialog.setNegativeButtonTitle(getResources().getString(R.string.no_button_title));
        dialog.show(getFragmentManager(), INVITE_RANDOM_FRIENDS_TAG);
    }

    public void inviteRandomFriends() {
        ApplicationManager applicationManager = new ApplicationManager(this.getActivity());
        String userId = applicationManager.getUserId();
        String clubId = applicationManager.getUserPersonalClubId();
        PhoneManager phoneManager = new PhoneManager();
        ArrayList<HashMap<String, String>> contactsList = phoneManager.getContacts(this.getActivity().getContentResolver());
        HashMap<String, String> contact;
        ArrayList<HashMap<String, String>> friendsToInvite = new ArrayList<HashMap<String, String>>();
        ArrayList<String> registeredFriends = new ArrayList<String>();
        if (contactsList.size() > Constants.NUMBER_OF_RANDOM_FRIENDS) {
            int[] randomNumbers = Util.randomNumbers(Constants.NUMBER_OF_RANDOM_FRIENDS, 0, contactsList.size());
            for (int i = 0; i < randomNumbers.length; i++) {
                contact = contactsList.get(randomNumbers[i]);
                friendsToInvite.add(contact);
            }
        } else {
            for (int i = 0; i < contactsList.size(); i++) {
                contact = contactsList.get(i);
                friendsToInvite.add(contact);
            }
        }

        ClubManager clubManager = new ClubManager();
        clubManager.sendClubInvite(this,userId,clubId,registeredFriends,friendsToInvite,this.getActivity());

    }


    public void didSendCubInvite(Boolean response){
        Log.w("MainActivity","Invite send");

    }
    public void didFailSendingClubInvite(String errorMessage){
        Log.w("MainActivity","Failed sending invites");
    }
}
