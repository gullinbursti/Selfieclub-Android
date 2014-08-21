/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class FriendsTabBtnFragment
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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.Friend;
import com.builtinmenlo.selfieclub.dataSources.FriendsViewData;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.builtinmenlo.selfieclub.models.ApplicationManager;
import com.builtinmenlo.selfieclub.models.ClubInviteProtocol;
import com.builtinmenlo.selfieclub.models.ClubManager;
import com.builtinmenlo.selfieclub.models.PhoneManager;
import com.builtinmenlo.selfieclub.models.SCDialogProtocol;
import com.builtinmenlo.selfieclub.models.UserFriendsProtocol;
import com.builtinmenlo.selfieclub.models.UserManager;
import com.builtinmenlo.selfieclub.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsTabBtnFragment extends Fragment implements UserFriendsProtocol,SCDialogProtocol,ClubInviteProtocol {
    private ListView lv;
    private ArrayList<Friend> friends;
    private User owner;
    private MyCustomAdapter adapter;
    private ProgressBar loadingIcon;
    private Friend selectedFriend;
    private static String INVITE_FRIEND_TAG = "invite_friend";
    private static String RECEIVE_FRIENDS_ERROR_TAG = "error_receiving_friends_list";
    private static String NO_FRIENDS_ERROR_TAG = "no_friends";

    public FriendsTabBtnFragment() {/*..\(^_^)/..*/}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

        View view = inflater.inflate(R.layout.friends_tab, container, false);

        loadingIcon = (ProgressBar) view.findViewById(R.id.loadingIcon);

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        lv = (ListView) view.findViewById(android.R.id.list);
        friends = new ArrayList<Friend>();
        populate();

        PhoneManager phoneManager = new PhoneManager();
        ArrayList<HashMap<String, String>> countryCodes = phoneManager.getCountryCodes(getActivity().getApplicationContext());

        UserManager userManager = new UserManager();
        SharedPreferences preferences = getActivity().getSharedPreferences("prefs",
                Activity.MODE_PRIVATE);


        String userId = preferences.getString(FirstRunRegistrationFragment.EXTRA_ID, "");
        userManager.requestFriends(this, userId, "+17143309754|+15617164724",getActivity().getContentResolver());

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

    private static class ViewHolder {
        TextView lblName;
        // TextView lblFollowers;
        // ImageView imgFollowers;
        ImageView imgAddOrCheck;
        ImageView imgAvatarCheck;
        ImageView imgAvatar;
        ProgressBar imgLoading;
    }

    public void populate() {
        if (lv != null) {
            adapter = new MyCustomAdapter(getActivity().getBaseContext(), friends);
            lv.post(new Runnable() {
                public void run() {
                    lv.setAdapter(adapter);
                }
            });

            lv.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    selectedFriend = friends.get(position);
                    showinviteFriendDialog(selectedFriend);

/*                    if (friends.get(position).isSelected()) {
                        arg1.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.gray_selection_dot);
                        friends.get(position).setSelected(false);
                    } else {
                        arg1.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.green_selection_dot);
                        friends.get(position).setSelected(true);
                    }
*/                }
            });
        }
    }

    private class MyCustomAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<Friend> listData;

        public MyCustomAdapter(Context context, ArrayList<Friend> listData) {
            this.listData = listData;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return friends.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            //if (convertView == null) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            //convertView = inflater.inflate(R.layout.friends_item, parent, false);
            convertView = inflater.inflate(R.layout.new_friends_item, parent, false);

            viewHolder = new ViewHolder();
            //viewHolder.imgAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);
            // viewHolder.lblFollowers = (TextView) convertView.findViewById(R.id.lblFollowers);
            viewHolder.lblName = (TextView) convertView.findViewById(R.id.lblName);
            //viewHolder.imgFollowers = (ImageView) convertView.findViewById(R.id.imgFollowers);
            viewHolder.imgAddOrCheck = (ImageView) convertView.findViewById(R.id.imgAddOrCheck);
            //viewHolder.imgAvatarCheck = (ImageView) convertView.findViewById(R.id.imgAvatarCheck);
            //viewHolder.imgLoading = (ProgressBar) convertView.findViewById(R.id.loadingImage);
            convertView.setTag(viewHolder);
            //} else {
            //viewHolder = (ViewHolder) convertView.getTag();
            //}

            Friend friend = friends.get(position);

            if (friend.isSelected())
                convertView.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.green_selection_dot);
            else
                convertView.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.gray_selection_dot);


            // viewHolder.lblFollowers.setText(String.valueOf(friend.getFollowers()));
            viewHolder.lblName.setText(friend.getUsername());

            /*if (friend.getState() == 1) {
                viewHolder.imgAddOrCheck.setBackgroundResource(R.drawable.green_selection_dot);
                viewHolder.imgAvatarCheck.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imgAddOrCheck.setBackgroundResource(R.drawable.gray_selection_dot);
                viewHolder.imgAvatarCheck.setVisibility(View.INVISIBLE);
            }*/

            /*if (friend.getFollowers() > 0){
                viewHolder.imgFollowers.setBackgroundResource(R.drawable.verify_arrow_green);
                viewHolder.lblFollowers.setTextColor(Color.GREEN);
            }else{
                viewHolder.imgFollowers.setBackgroundResource(R.drawable.verify_arrow_grey);
                viewHolder.lblFollowers.setTextColor(Color.LTGRAY);
            }*/


            /*if (viewHolder.imgAvatar != null) {
                Picasso.with(getActivity()).load(friend.getAvatarUrl()).into(viewHolder.imgAvatar, new Callback() {

                    @Override
                    public void onSuccess() {
                        viewHolder.imgAvatar.setVisibility(View.VISIBLE);
                        viewHolder.imgLoading.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        viewHolder.imgAvatar.setVisibility(View.VISIBLE);
                        viewHolder.imgLoading.setVisibility(View.INVISIBLE);
                    }
                });
            }*/

            return convertView;
        }
    }

    public void didReceiveFriendsList(FriendsViewData friendsViewData) {
        loadingIcon.setVisibility(View.INVISIBLE);
        if (friendsViewData.getFriends().size() < 1 ) {
            getActivity().findViewById(R.id.txtError).setVisibility(View.VISIBLE);
        } else {
            friends = friendsViewData.getFriends();
            owner = friendsViewData.getOwner();
            adapter.notifyDataSetChanged();
        }
    }

    public void didReceiveFriendsListError(String errorMessage) {
        loadingIcon.setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.txtError).setVisibility(View.VISIBLE);
    }

    public void didClickedButton(String dialogTag, int buttonIndex){
        if(dialogTag.equalsIgnoreCase(INVITE_FRIEND_TAG)){
            if(buttonIndex==1){
                inviteFriend();
            }
        }
    }

    //Invite methods

    private void showinviteFriendDialog(Friend friend){
        String message = String.format(getResources().getString(R.string.invite_friend_dialog),friend.getUsername());
        SCDialog dialog = new SCDialog();
        dialog.setScDialogProtocol(this);
        dialog.setMessage(message);
        dialog.setPositiveButtonTitle(getResources().getString(R.string.yes_button_title));
        dialog.setNegativeButtonTitle(getResources().getString(R.string.no_button_title));
        dialog.showTwoButtons=true;
        dialog.show(getFragmentManager(),INVITE_FRIEND_TAG);
    }

    public void inviteFriend(){
        ApplicationManager applicationManager = new ApplicationManager(this.getActivity());
        String userId = applicationManager.getUserId();
        String clubId = applicationManager.getUserPersonalClubId();

        String selectedFriendId = selectedFriend.getUserId();
        if(selectedFriendId.equalsIgnoreCase("no_member")){
            ArrayList<String> friends = new ArrayList<String>();
            ArrayList<HashMap<String,String>> nonUsers = new ArrayList<HashMap<String, String>>();
            HashMap<String,String>contact = new HashMap<String, String>();
            contact.put("name",selectedFriend.getUsername());
            contact.put("number",selectedFriend.getPhoneNumber());
            nonUsers.add(contact);
            ClubManager clubManager = new ClubManager();
            clubManager.sendClubInvite(this,userId,clubId,friends,nonUsers);
        }
        else{
            ArrayList<String> friends = new ArrayList<String>();
            friends.add(selectedFriendId);
            ArrayList<HashMap<String,String>> nonUsers = new ArrayList<HashMap<String, String>>();
            ClubManager clubManager = new ClubManager();
            clubManager.sendClubInvite(this,userId,clubId,friends,nonUsers);
        }

    }

    public void didSendCubInvite(Boolean response){
        Util.playDefaultNotificationSound(getActivity().getApplicationContext());
        selectedFriend.setSelected(true);
        adapter.notifyDataSetChanged();;
    }
    public void didFailSendingClubInvite(String errorMessage){
        Log.w(getClass().getName(),"Failed inviting user");

    }

}
