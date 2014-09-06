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
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.activity.MainActivity;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.models.AnalyticsManager;
import com.builtinmenlo.selfieclub.models.ApplicationManager;
import com.builtinmenlo.selfieclub.models.ClubManager;
import com.builtinmenlo.selfieclub.models.ClubPhotoSubmissionProtocol;
import com.builtinmenlo.selfieclub.models.SCDialogProtocol;
import com.builtinmenlo.selfieclub.models.UserClubsProtocol;
import com.builtinmenlo.selfieclub.models.UserManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class CameraStep3Fragment extends Fragment implements UserClubsProtocol, ClubPhotoSubmissionProtocol, SCDialogProtocol {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

    //] class properties ]>

    public static final String EXTRA_STICKERS = "stickers";
    private static final String UPLOAD_SUCCESS_TAG = "upload_succeed";
    private static final String UPLOAD_FAILED_TAG = "upload_failed";
    private static final String NO_CLUBS_SELECTED_TAG = "no_clubs";

    //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
    private boolean uploadedSuccess;
    private int uploads;
    private ListView listClubs;
    private Button btnSubmit;
    private ArrayList<String> selectedClubs;
    private ArrayList<Club> clubs;
    private MyCustomAdapter adapter;

    private Bundle bundle;
    private ProgressBar loadingIcon;
    private ProgressDialog waiting;
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

        uploads = 0;

        bundle = getArguments();

        listClubs = (ListView) view.findViewById(android.R.id.list);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        clubs = new ArrayList<Club>();
        populate();

        UserManager userManager = new UserManager();
        ApplicationManager applicationManager = new ApplicationManager(getActivity());
        userManager.requestUserClubs(this, applicationManager.getUserId(),this.getActivity());

        loadingIcon = (ProgressBar) view.findViewById(R.id.loadingIcon);

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewCameraStep2Fragment newFragment = new NewCameraStep2Fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(CameraStep3Fragment.this);
                transaction.replace(R.id.fragment_container, newFragment);
                newFragment.setArguments(bundle);
                transaction.commit();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedClubs = new ArrayList<String>();
                for (Club club : clubs) {
                    if (club.isSelected()) {
                        selectedClubs.add(club.getClubId());
                    }
                }
                if (selectedClubs.size() < 1) {
                    SCDialog dialog = new SCDialog();
                    dialog.setScDialogProtocol(CameraStep3Fragment.this);
                    dialog.setMessage(getString(R.string.message_no_selected_club));
                    dialog.setNegativeButtonTitle(getResources().getString(R.string.ok_button_title));
                    dialog.showTwoButtons = true;
                    dialog.show(getFragmentManager(), UPLOAD_FAILED_TAG);
                } else {
                    ClubManager manager = new ClubManager();
                    byte[] avatarImage = null;
                    ArrayList<String> selected = null;
                    if (bundle != null) {
                        avatarImage = bundle.getByteArray(CameraFragment.EXTRA_IMAGE);
                        selected = bundle.getStringArrayList(EXTRA_STICKERS);
                    }
                    waiting = ProgressDialog.show(getActivity(), "", getString(R.string.label_uploading_image));
                    ApplicationManager applicationManager = new ApplicationManager(getActivity());
                    for (String clubId : selectedClubs) {
                        manager.submitPhoto(CameraStep3Fragment.this, getActivity(), applicationManager.getUserId(), clubId, avatarImage, selected);
                    }
                }
            }
        });

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
        AnalyticsManager analyticsManager = AnalyticsManager.sharedInstance(this.getActivity().getApplication());
        analyticsManager.trackEvent(Constants.KEEN_EVENT_CAMERA_SETEP3);
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.hide();
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    @Override
    public void onDetach() {
        super.onDetach();
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.show();
    }

    private static class ViewHolder {
        ImageView imgClub;
        ProgressBar loadingImage;
        TextView lblClubName;
    }

    public void populate() {
        if (listClubs != null) {
            adapter = new MyCustomAdapter(getActivity(), R.layout.camera_step3_item, clubs);
            listClubs.post(new Runnable() {
                public void run() {
                    listClubs.setAdapter(adapter);
                }
            });

            listClubs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    if (position >= clubs.size()) {
                        if (arg1.findViewById(R.id.imgAddOrCheck).getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.gray_selection_dot).getConstantState()))
                            for (Club club : clubs) {
                                club.setSelected(true);
                            }
                        else
                            for (Club club : clubs) {
                                club.setSelected(false);
                            }
                        adapter.notifyDataSetChanged();
                    } else {
                        if (clubs.get(position).isSelected()) {
                            arg1.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.gray_selection_dot);
                            clubs.get(position).setSelected(false);
                        } else {
                            arg1.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.green_selection_dot);
                            clubs.get(position).setSelected(true);
                        }
                    }
                }
            });
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<Club> {

        public MyCustomAdapter(Context context, int textViewResourceId, List<Club> list) {
            super(context, textViewResourceId, list);

        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return clubs.size() + 1;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            LayoutInflater inflater = getActivity().getLayoutInflater();
            convertView = inflater.inflate(R.layout.camera_step3_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgClub = (ImageView) convertView.findViewById(R.id.imgClub);
            viewHolder.loadingImage = (ProgressBar) convertView.findViewById(R.id.loadingImage);
            viewHolder.lblClubName = (TextView) convertView.findViewById(R.id.lblClubName);
            convertView.setTag(viewHolder);

            if (position >= clubs.size()) {
                viewHolder.lblClubName.setText(R.string.label_select_all_clubs);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                //layoutParams.setMargins(100, 0, 0, 0);
                viewHolder.lblClubName.setLayoutParams(layoutParams);
                Bitmap image = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
                image.eraseColor(Color.TRANSPARENT);
                boolean allSelected = true;
                for (Club club : clubs) {
                    if (!club.isSelected()) {
                        allSelected = false;
                        break;
                    }
                }
                viewHolder.loadingImage.setVisibility(View.INVISIBLE);
                if (allSelected)
                    convertView.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.green_selection_dot);
                else
                    convertView.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.gray_selection_dot);
                viewHolder.imgClub.setImageBitmap(image);
            } else {
                Club club = clubs.get(position);
                viewHolder.lblClubName.setText(club.getClubName());
                if (club.isSelected())
                    convertView.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.green_selection_dot);
                else
                    convertView.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.gray_selection_dot);

                Picasso.with(getActivity()).load(club.getClubImage()).into(viewHolder.imgClub, new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolder.loadingImage.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        viewHolder.imgClub.setImageResource(R.drawable.default_club_cover);
                        viewHolder.loadingImage.setVisibility(View.INVISIBLE);
                    }
                });
            }

            return convertView;
        }
    }

    public void didReceiveUserClubs(ArrayList<Club> userClubs) {
        Log.i(this.getActivity().getClass().getName(), userClubs.toString());
        clubs = userClubs;
        btnSubmit.setVisibility(View.VISIBLE);
        listClubs.setVisibility(View.VISIBLE);
        loadingIcon.setVisibility(View.INVISIBLE);
        adapter.notifyDataSetChanged();

    }

    public void didReceiveUserClubsError(String errorMessage) {
        btnSubmit.setVisibility(View.VISIBLE);
        loadingIcon.setVisibility(View.INVISIBLE);
    }

    @Override
    public void didSubmittedPhotoInClub(Boolean result) {
        uploads++;
        if (result) {
            uploadedSuccess = true;
        }
        if (uploads >= clubs.size()) {
            waiting.dismiss();
            if (uploadedSuccess) {
                SCDialog dialog = new SCDialog();
                dialog.setScDialogProtocol(this);
                dialog.setMessage(getString(R.string.label_successfully_uploaded));
                dialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
                dialog.showTwoButtons = true;
                dialog.show(getFragmentManager(), UPLOAD_SUCCESS_TAG);
            } else {
                SCDialog dialog = new SCDialog();
                dialog.setScDialogProtocol(this);
                dialog.setMessage(getString(R.string.label_upload_failed));
                dialog.setPositiveButtonTitle(getResources().getString(R.string.retry_button_title));
                dialog.setNegativeButtonTitle(getResources().getString(R.string.ok_button_title));
                dialog.showTwoButtons = true;
                dialog.show(getFragmentManager(), UPLOAD_FAILED_TAG);
            }
        }
    }

    @Override
    public void didFailSubmittingPhotoInClub(String message) {
        uploads++;
        if (uploads >= clubs.size()) {
            waiting.dismiss();
            if (uploadedSuccess) {
                SCDialog dialog = new SCDialog();
                dialog.setScDialogProtocol(this);
                dialog.setMessage(getString(R.string.label_successfully_uploaded));
                dialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
                dialog.showTwoButtons = true;
                dialog.show(getFragmentManager(), UPLOAD_SUCCESS_TAG);
            } else {
                SCDialog dialog = new SCDialog();
                dialog.setScDialogProtocol(this);
                dialog.setMessage(getString(R.string.label_upload_failed));
                dialog.setPositiveButtonTitle(getResources().getString(R.string.retry_button_title));
                dialog.setNegativeButtonTitle(getResources().getString(R.string.ok_button_title));
                dialog.showTwoButtons = true;
                dialog.show(getFragmentManager(), UPLOAD_FAILED_TAG);
            }
        }
    }

    @Override
    public void didClickedButton(String dialogTag, int buttonIndex) {
        if (dialogTag.equalsIgnoreCase(UPLOAD_FAILED_TAG)) {
            if (buttonIndex == 1) {
                ClubManager manager = new ClubManager();
                byte[] avatarImage = null;
                ArrayList<String> selected = null;
                if (bundle != null) {
                    avatarImage = bundle.getByteArray(CameraFragment.EXTRA_IMAGE);
                    selected = bundle.getStringArrayList(EXTRA_STICKERS);
                }
                waiting = ProgressDialog.show(getActivity(), "", getString(R.string.label_uploading_image));
                ApplicationManager applicationManager = new ApplicationManager(this.getActivity());
                String userId = applicationManager.getUserId();
                String clubId = applicationManager.getUserPersonalClubId();
                manager.submitPhoto(CameraStep3Fragment.this, getActivity(), userId, clubId, avatarImage, selected);
            }
        } else if (dialogTag.equalsIgnoreCase(UPLOAD_SUCCESS_TAG)) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(this);
            if (((MainActivity)getActivity()).tabSelected != null) {
                transaction.replace(R.id.fragment_container, ((MainActivity)getActivity()).tabSelected);
                ((MainActivity)getActivity()).tabSelected = null;
            } else {
                transaction.replace(R.id.fragment_container, new NewsTabBtnFragment());
            }
            transaction.commit();
        }

    }

}
