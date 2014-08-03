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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.activity.CameraPreview;
import com.builtinmenlo.selfieclub.models.FirstRunManager;
import com.builtinmenlo.selfieclub.models.FirstRunProtocol;
import com.builtinmenlo.selfieclub.models.PicoCandyManager;

;import java.util.ArrayList;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class FirstRunRegistrationFragment extends Fragment implements FirstRunProtocol {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

	//] class properties ]>
    private Bundle bundle;
    private ProgressDialog dialog;
    private static String freeUserId;
    private FirstRunManager manager;
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

        String countryCode = null;
        byte[] avatarImage = null;
        if (getArguments()!= null) {
            countryCode = getArguments().getString(FirstRunCountrySelectorFragment.EXTRA_CODE);
            avatarImage = getArguments().getByteArray(CameraPreview.EXTRA_IMAGE);
        }

        ImageView avatar = (ImageView) view.findViewById(R.id.imgAvatar);
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
        });


        final Button btnCountrySelector = (Button) view.findViewById(R.id.btnCountrySelect);
        if (countryCode != null)
            btnCountrySelector.setText(countryCode);
        btnCountrySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new FirstRunCountrySelectorFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                if (bundle == null)
                    bundle = new Bundle();
                newFragment.setArguments(bundle);
                transaction.commit();
            }
        });

        final EditText txtUsername = (EditText) view.findViewById(R.id.txtUserName);
        final EditText txtPhone = (EditText) view.findViewById(R.id.txtEnterPhone);
        txtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ( actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    manager.usernameAndPhoneCheck(FirstRunRegistrationFragment.this,freeUserId,txtUsername.getText().toString(),btnCountrySelector.getText().toString()+txtPhone.getText().toString());
                    return true;
                }
                return false;
            }
        });


        PicoCandyManager picoCandyManager = new PicoCandyManager();
        picoCandyManager.registerApp(this.getActivity().getApplicationContext());
        //picoCandyManager.getCurrentUser();
        ArrayList<String> ids = new ArrayList<String>();
        ids.add("824");
        ids.add("827");
        ids.add("823");
        ids.add("813");
        picoCandyManager.getContentGroups(ids);

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
    public void didValideUsernamePhone(Boolean isValid, String message) {
        System.err.println(message);
    }

    @Override
    public void didFailValidatingUsernamePhone(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);

    }

    @Override
    public void didRegisteredUser(String userId) {

    }

    @Override
    public void didFailRegisteringUser(FirstRunManager.FIRSTRUN_ERROR errorType, String message) {

    }
}
