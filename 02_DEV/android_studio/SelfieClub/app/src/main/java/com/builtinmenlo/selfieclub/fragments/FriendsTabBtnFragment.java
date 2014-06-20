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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.ActivityItem;
import com.builtinmenlo.selfieclub.dataSources.Friend;
import com.builtinmenlo.selfieclub.dataSources.FriendsViewData;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.builtinmenlo.selfieclub.models.UserFriendsProtocol;
import com.builtinmenlo.selfieclub.models.UserManager;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class FriendsTabBtnFragment extends Fragment implements UserFriendsProtocol{
    public ListView lv;
    public List<Friend> friends;
    public User owner;
    private MyCustomAdapter adapter;
    private ProgressBar loadingIcon;

    public FriendsTabBtnFragment() {/*..\(^_^)/..*/}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        UserManager userManager = new UserManager();
        userManager.requestFriends(this,"2394","+17143309754|+15617164724|+15617164831|+17409726939|+16505754720|+14086210100|+15303041380|+141 52646152|+16192042875|+16179397216|+15612369460|+18475305634|+14089215625|+19783175663|+14159354255|+14255032279|+16502244999|+14159028877|+12393709811|+12 135098296|+14083109905|+19196560716|+14087181396|+14085069784|+14152549391|+16509969693|+15612719211|+17869735014|+15624539140|+16506192664|+13107078969|+1 4152609007|+16263750676|+14079219866|+18187266581|+18312220104|+19312496956|+13105978645|+13106262124|+19493502980|+14084995523|+14153788924|+17816161660|+ 14156867366|+17816161660|+16507434738|+14153368700|+15105797189|+14153086768|+16506192665|+12068566868|+13105005463|+13035266649|+12133009127|+14156095557| +14084293828|+16507968877|+12135905745");
        View view = inflater.inflate(R.layout.friends_tab, container, false);

        loadingIcon = (ProgressBar)view.findViewById(R.id.loadingIcon);

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        lv = (ListView) view.findViewById(android.R.id.list);
        friends = new ArrayList<Friend>();
        populate();

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
        ImageView imageView;
        String imageURL;
        Bitmap bitmap;
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
                result.imageView.setImageBitmap(result.bitmap);
            }
        }
    }


    public void populate() {
        if (lv != null) {
            adapter = new MyCustomAdapter(getActivity(), R.layout.friends_item, friends);
            lv.post(new Runnable() {
                public void run() {
                    lv.setAdapter(adapter);
                }
            });

            lv.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    //arg1.setBackgroundColor(Color.TRANSPARENT);
                }
            });
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<Friend> {

        public MyCustomAdapter(Context context, int textViewResourceId, List<Friend> list) {
            super(context, textViewResourceId, list);
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.friends_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.imgAvatar);
                convertView.setTag(viewHolder);
            }

            Friend friend = friends.get(position);

            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.imageURL = friend.getAvatarUrl();
            new DownloadAsyncTask().execute(viewHolder);


           // TextView lblFollowers = (TextView) convertView.findViewById(R.id.lblFollowers);
            TextView lblName = (TextView) convertView.findViewById(R.id.lblName);
            //ImageView imgFollowers = (ImageView) convertView.findViewById(R.id.imgFollowers);
            ImageView imgAddOrCheck = (ImageView) convertView.findViewById(R.id.imgAddOrCheck);
            ImageView imgAvatarCheck = (ImageView) convertView.findViewById(R.id.imgAvatarCheck);

            //lblFollowers.setText(String.valueOf(friend.getFollowers()));
            lblName.setText(friend.getUsername());

           if (friend.getState() == 1) {
                imgAddOrCheck.setBackgroundResource(R.drawable.green_dot);
                imgAvatarCheck.setVisibility(View.VISIBLE);
            } else {
                imgAddOrCheck.setBackgroundResource(R.drawable.gray_stroke_gray);
                imgAvatarCheck.setVisibility(View.INVISIBLE);
            }

            /*if (friend.getFollowers() > 0){
                imgFollowers.setBackgroundResource(R.drawable.verify_arrow_green);
                lblFollowers.setTextColor(Color.GREEN);
            }else{
                imgFollowers.setBackgroundResource(R.drawable.verify_arrow_grey);
                lblFollowers.setTextColor(Color.LTGRAY);
            }*/

            return convertView;
        }
    }

    public void didReceiveFriendsList(FriendsViewData friendsViewData){
        friends =  friendsViewData.getFriends();
        owner = friendsViewData.getOwner();
        adapter.notifyDataSetChanged();
    }
    public void didReceiveFriendsListError(String errorMessage){
        System.err.println(errorMessage);
    }

}
