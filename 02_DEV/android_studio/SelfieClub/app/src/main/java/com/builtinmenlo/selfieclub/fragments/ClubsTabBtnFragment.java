/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class ClubsTabBtnFragment
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
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.models.ApplicationManager;
import com.builtinmenlo.selfieclub.models.ClubInviteProtocol;
import com.builtinmenlo.selfieclub.models.ClubManager;
import com.builtinmenlo.selfieclub.models.PhoneManager;
import com.builtinmenlo.selfieclub.models.SCDialogProtocol;
import com.builtinmenlo.selfieclub.models.UserClubsProtocol;
import com.builtinmenlo.selfieclub.models.UserManager;
import com.builtinmenlo.selfieclub.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class declaration [¡]>
public class ClubsTabBtnFragment extends Fragment implements UserClubsProtocol, SCDialogProtocol, ClubInviteProtocol {

    private GridView gridClubs;
    private ProgressBar loadingIcon;
    private ArrayList<Club> clubs;
    private MyCustomAdapter adapter;
    private Club selectedClub;
    private static String INVITE_RANDOM_FRIENDS_TAG = "invite_random_friends";
    private static String RECEIVE_CLUBS_ERROR_TAG = "error_retrieving_clubs";
    private static String NO_CLUBS_ERROR_TAG = "no_clubs";
    private static String WOULD_YOU_JOIN_TAG = "would_you_join";


    public ClubsTabBtnFragment() {/*..\(^_^)/..*/}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clubs_tab, container, false);

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        loadingIcon = (ProgressBar) view.findViewById(R.id.loadingIcon);
        gridClubs = (GridView) view.findViewById(R.id.gridMenu);
        clubs = new ArrayList<Club>();
        populate();

        UserManager userManager = new UserManager();
        ApplicationManager applicationManager = new ApplicationManager(getActivity());
        userManager.requestUserClubs(this, applicationManager.getUserId());
        //userManager.requestUserClubs(this, "3509");

        return view;
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

    private static class ViewHolder {
        ImageView imageView;
        String imageURL;
        Bitmap bitmap;
        ProgressBar loadingImage;
    }

    private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            //load image directly
            ViewHolder viewHolder = params[0];
            try {
                URL imageURL = new URL(viewHolder.imageURL);
                viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
            } catch (Exception e) {
                viewHolder.bitmap = null;
            }
            return viewHolder;
        }

        @Override
        protected void onPostExecute(ViewHolder result) {
            if (result.bitmap == null) {
                result.imageView.setImageResource(R.drawable.friends_item_image_mask);
            } else {
                //result.imageView.setImageBitmap(getRoundedCornerBitmap(result.bitmap));
                result.imageView.setImageBitmap(result.bitmap);
            }
        }
    }

    public void populate() {
        if (gridClubs != null) {
            adapter = new MyCustomAdapter(getActivity(), R.layout.clubs_item, clubs);
            gridClubs.post(new Runnable() {
                public void run() {
                    gridClubs.setAdapter(adapter);
                }
            });

            gridClubs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    if (position < 1) {
                        SCDialog dialog = new SCDialog();
                        dialog.setNeutralButtonTitle(getString(R.string.ok_button_title));
                        dialog.setMessage(getString(R.string.label_coming_soon));
                        dialog.showTwoButtons = false;
                        dialog.show(getFragmentManager(), "");
                    } else {
                        ApplicationManager applicationManager = new ApplicationManager(getActivity());
                        Club clubSelected = clubs.get(position - 1);
                        boolean isMember = (clubSelected.getClubOwner().getUserId() == applicationManager.getUserId());
                        if (!isMember) {
                            for (int i = 0; i < clubSelected.getClubMembers().length(); i++) {
                                try {
                                    JSONObject member = clubSelected.getClubMembers().getJSONObject(i);
                                    if (applicationManager.getUserId().equals(member.getString("id"))) {
                                        isMember = true;
                                    }
                                } catch (JSONException e) {
                                }
                            }
                        }
                        if (!isMember) {
                            showInviteDialog(clubSelected);
                        } else {
                            SCDialog scdialog = new SCDialog();
                            scdialog.setScDialogProtocol(ClubsTabBtnFragment.this);
                            scdialog.setMessage(String.format(getString(R.string.would_you_like_joinclub_dialog), clubSelected.getClubName()));
                            scdialog.setPositiveButtonTitle(getString(R.string.yes_button_title));
                            scdialog.setNegativeButtonTitle(getString(R.string.no_button_title));
                            scdialog.showTwoButtons = true;
                            scdialog.show(getFragmentManager(), WOULD_YOU_JOIN_TAG);
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

            if (position == 0) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.clubs_item, parent, false);
                viewHolder = new ViewHolder();
                TextView lblText = (TextView) convertView.findViewById(R.id.lblClubName);
                lblText.setText(R.string.label_create_a_club);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgClub);
                viewHolder.imageView.setImageResource(R.drawable.selector_add_club);
                viewHolder.loadingImage = (ProgressBar) convertView.findViewById(R.id.loadingImage);
                viewHolder.loadingImage.setVisibility(View.INVISIBLE);
                convertView.setTag(viewHolder);
            } else {
                //if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.clubs_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgClub);
                    /*Bitmap image = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);
                    image.eraseColor(Color.TRANSPARENT);
                    viewHolder.imageView.setImageBitmap(image);*/
                viewHolder.loadingImage = (ProgressBar) convertView.findViewById(R.id.loadingImage);
                viewHolder.loadingImage.setVisibility(View.VISIBLE);
                convertView.setTag(viewHolder);


                Club club = clubs.get(position - 1);
                try {
                    String clubImagePath = new URL(club.getClubImage()).getPath();
                    String newClubImageUrl = "http://hotornot-challenges.s3.amazonaws.com" + clubImagePath;
                    viewHolder.imageURL = newClubImageUrl;
                } catch (Exception e) {
                    viewHolder.imageURL = "";
                    Log.w("ClubsTabBtnFragment", "Error parsing the club image id");
                }
                ;


                //viewHolder = (ViewHolder) convertView.getTag();
                viewHolder.imageURL = club.getClubImage();

                Picasso.with(getActivity()).load(viewHolder.imageURL).into(viewHolder.imageView, new Callback() {

                    @Override
                    public void onSuccess() {
                        viewHolder.loadingImage.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        viewHolder.imageView.setImageResource(R.drawable.default_club_cover);
                        viewHolder.loadingImage.setVisibility(View.INVISIBLE);
                    }
                });

                TextView lblText = (TextView) convertView.findViewById(R.id.lblClubName);

                lblText.setText(club.getClubName());
                //}
            }
            return convertView;
        }
    }


    public void didReceiveUserClubs(ArrayList<Club> userClubs) {
        loadingIcon.setVisibility(View.INVISIBLE);
        if (userClubs.size() < 1) {
            getActivity().findViewById(R.id.txtError).setVisibility(View.VISIBLE);
        } else {
            Log.i(this.getActivity().getClass().getName(), userClubs.toString());
            clubs = userClubs;
            adapter.notifyDataSetChanged();
        }
    }

    public void didReceiveUserClubsError(String errorMessage) {
        loadingIcon.setVisibility(View.INVISIBLE);
        SCDialog scdialog = new SCDialog();
        scdialog.setScDialogProtocol(this);
        scdialog.setMessage(errorMessage);
        scdialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
        scdialog.showTwoButtons = true;
        scdialog.show(getFragmentManager(), RECEIVE_CLUBS_ERROR_TAG);
    }

    //Dialog protocol
    public void didClickedButton(String dialogTag, int buttonIndex) {
        if (dialogTag.equalsIgnoreCase(INVITE_RANDOM_FRIENDS_TAG)) {
            if (buttonIndex == 1) {
                inviteRandomFriends();
            }
        }
    }
    //Invite 10 friends club

    public void showInviteDialog(Club club) {
        selectedClub = club;
        String message = getResources().getString(R.string.invite_random_friends_dialog);
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
        String clubId = selectedClub.getClubId();
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
        clubManager.sendClubInvite(this, userId, clubId, registeredFriends, friendsToInvite);


    }

    public void didSendCubInvite(Boolean response) {
        Util.playDefaultNotificationSound(getActivity().getApplicationContext());
    }

    public void didFailSendingClubInvite(String errorMessage) {
        Log.w(this.getClass().getName(), "Failed inviting friends");
    }

}
