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
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.ActivityItem;
import com.builtinmenlo.selfieclub.dataSources.Friend;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.builtinmenlo.selfieclub.models.UserActivity;
import com.builtinmenlo.selfieclub.models.UserActivityProtocol;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class ActivityTabBtnFragment extends Fragment implements UserActivityProtocol {
    public ListView lv;
    public List<ActivityItem> notifications;
    private MyCustomAdapter myAdapter;

    public ActivityTabBtnFragment() {/*..\(^_^)/..*/}

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(48,48,24,paint);

        return circleBitmap;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        View view = inflater.inflate(R.layout.notifications_tab, container, false);

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        UserActivity api = new UserActivity(this);

        api.requestUserActivity("131820");


        //return (inflater.inflate(R.layout.friends_tab, container, false));
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
                result.imageView.setImageResource(R.drawable.ic_launcher);
            } else {
                result.imageView.setImageBitmap(getRoundedCornerBitmap(result.bitmap));
            }
        }
    }


    public void populate() {

        lv = (ListView) getActivity().findViewById(android.R.id.list);
        if (lv != null) {
            myAdapter = new MyCustomAdapter(getActivity(), R.layout.friends_item, notifications);
            lv.setAdapter(myAdapter);

            lv.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    //arg1.setBackgroundColor(Color.TRANSPARENT);
                }
            });
            lv.setVisibility(View.VISIBLE);
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<ActivityItem> {

        public MyCustomAdapter(Context context, int textViewResourceId, List<Friend> list) {
            super(context, textViewResourceId, list);

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

            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.imageURL = notifications.get(position).;
            new DownloadAsyncTask().execute(viewHolder);

            User friend = notifications.get(position).getUser();

           // TextView lblFollowers = (TextView) convertView.findViewById(R.id.lblFollowers);
            TextView lblName = (TextView) convertView.findViewById(R.id.lblName);
            //ImageView imgFollowers = (ImageView) convertView.findViewById(R.id.imgFollowers);
            ImageView imgAddOrCheck = (ImageView) convertView.findViewById(R.id.imgAddOrCheck);
            ImageView imgAvatarCheck = (ImageView) convertView.findViewById(R.id.imgAvatarCheck);

            //lblFollowers.setText(String.valueOf(friend.getFollowers()));
            lblName.setText(friend.get());

            if (friend.isFriend()) {
                imgAddOrCheck.setBackgroundResource(R.drawable.green_check_mark);
                imgAvatarCheck.setVisibility(View.VISIBLE);
            } else {
                imgAddOrCheck.setBackgroundResource(R.drawable.blue_plus_button);
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

    public void didReceiveUserActivity(ArrayList<ActivityItem> activityList){
        notifications = activityList;

        populate(findViewbyId);
    }

    public void didReceiveUserActivityError(String error){

    }
}
