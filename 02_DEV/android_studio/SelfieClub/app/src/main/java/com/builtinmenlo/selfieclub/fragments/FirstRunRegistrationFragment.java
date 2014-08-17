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

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.models.FirstRunManager;
import com.builtinmenlo.selfieclub.models.FirstRunProtocol;
import com.builtinmenlo.selfieclub.models.PicoCandyManager;
import com.builtinmenlo.selfieclub.models.SCDialogProtocol;
import com.picocandy.android.data.PCContent;
import com.picocandy.android.data.PCContentGroup;

import java.util.ArrayList;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class FirstRunRegistrationFragment extends Fragment implements FirstRunProtocol, SCDialogProtocol {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_ID = "user_id";
    public static final String EXTRA_EMAIL= "user_email";


    //] class properties ]>
    private Bundle bundle;
    private ProgressDialog dialog;
    private static String freeUserId;
    private FirstRunManager manager;

    private static String FAILED_VALIDATING_TAG = "validation_failed";

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

        dialog = ProgressDialog.show(getActivity(), "", "Please Wait");


        manager = new FirstRunManager();
        manager.requestFreeUserId(this);

        String countryCode = "";
        String username = "";
        //byte[] avatarImage = null;
        if (bundle != null) {
            countryCode =bundle.getString(FirstRunCountrySelectorFragment.EXTRA_CODE);
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

        final EditText txtUsername = (EditText) view.findViewById(R.id.txtUserName);
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

        final EditText txtPhone = (EditText) view.findViewById(R.id.txtEnterPhone);
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
                getActivity().findViewById(R.id.imgCheckUserName).setVisibility(View.INVISIBLE);
                manager.usernameAndPhoneCheck(FirstRunRegistrationFragment.this, freeUserId, txtUsername.getText().toString(), btnCountrySelector.getText().toString() + txtPhone.getText().toString() + "@selfieclub.com");
            }
        });

        return view;
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void didReceiveStickers(ArrayList<PCContentGroup> contentGroupsList, ArrayList<PCContent> stickerList) {
        Log.w("", "");
        PicoCandyManager picoCandyManager = PicoCandyManager.sharedInstance();
        picoCandyManager.getStickerByName("happy");
    }

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
        System.err.println(message);
        if (isValid){
            getActivity().findViewById(R.id.imgCheckUserName).setVisibility(View.VISIBLE);
            Fragment newFragment = new FirstRunEnterPinFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            if (bundle == null)
                bundle = new Bundle();
            newFragment.setArguments(bundle);
            transaction.commit();
        }
    }

    @Override
    public void didFailValidatingUsernamePhone(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {
        SCDialog dialog = new SCDialog();
        dialog.setScDialogProtocol(this);
        dialog.setMessage(message);
        dialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
        dialog.show(getFragmentManager(), FAILED_VALIDATING_TAG);
    }

    @Override
    public void didRegisteredUser(String userId) {

    }

    @Override
    public void didFailRegisteringUser(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {

    }

    @Override
    public void didClickedButton(String dialogTag, int buttonIndex) {
        if (dialogTag.equalsIgnoreCase(FAILED_VALIDATING_TAG)) {
            if (buttonIndex == 1) {

            }
        }
    }
}
