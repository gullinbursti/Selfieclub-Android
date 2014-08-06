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
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.activity.CameraPreview;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class CameraStep3Fragment extends Fragment {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

    //] class properties ]>
    //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
    public static final String EXTRA_CODE = "country_code";

    private Bundle bundle;
    private ProgressBar loadingIcon;
    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

    private Fragment backView;
    private Fragment nextView;

    public void setBackView(Fragment backView) {
        this.backView = backView;
    }

    public void setNextView(Fragment nextView) {
        this.nextView = nextView;
    }

    // <*] class constructor [*>
    public CameraStep3Fragment() {/*..\(^_^)/..*/}

    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=[>
    //]~=~=~=~=~=~=~=~=~=[>

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        View view = inflater.inflate(R.layout.camera_step_3, container, false);

        bundle = getArguments();
        byte[] avatarImage = null;
        if (bundle!= null) {
            avatarImage = bundle.getByteArray(CameraPreview.EXTRA_IMAGE);
        }

        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.hide();

        loadingIcon = (ProgressBar) view.findViewById(R.id.loadingIcon);

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        hideKeyboard(view);
        return view;
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯


    private void hideKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void onCreate(Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onCreate(savedInstanceState);
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onAttach(Activity activity) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onAttach(activity);
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    @Override
    public void onDetach() {
        super.onDetach();
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_camera, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // handle click events for action bar items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_menu_next:
                Fragment newFragment = new CameraFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                if (bundle == null)
                    bundle = new Bundle();
                newFragment.setArguments(bundle);
                transaction.commit();
                return true;

            /*case R.id.copyLink:
                showToast("Copy link was clicked.");
                return true;

            case R.id.share:
                showToast("Share was clicked.");
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
