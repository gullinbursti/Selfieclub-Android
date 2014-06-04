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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.builtinmenlo.selfieclub.models.UserActivity;
import com.builtinmenlo.selfieclub.models.UserActivityProtocol;
import com.loopj.android.http.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class FriendsTabBtnFragment extends Fragment implements UserActivityProtocol{
    public ListView lv;
    public List<Friend> friends;
    private MyCustomAdapter myAdapter;

    private String[] imageURLArray = new String[]{
            "http://farm8.staticflickr.com/7315/9046944633_881f24c4fa_s.jpg",
            "http://farm4.staticflickr.com/3777/9049174610_bf51be8a07_s.jpg",
            "http://farm8.staticflickr.com/7324/9046946887_d96a28376c_s.jpg",
            "http://farm3.staticflickr.com/2828/9046946983_923887b17d_s.jpg",
            "http://farm4.staticflickr.com/3810/9046947167_3a51fffa0b_s.jpg",
            "http://farm4.staticflickr.com/3773/9049175264_b0ea30fa75_s.jpg",
            "http://farm4.staticflickr.com/3781/9046945893_f27db35c7e_s.jpg",
            "http://farm6.staticflickr.com/5344/9049177018_4621cb63db_s.jpg",
            "http://farm8.staticflickr.com/7307/9046947621_67e0394f7b_s.jpg",
            "http://farm6.staticflickr.com/5457/9046948185_3be564ac10_s.jpg",
            "http://farm4.staticflickr.com/3752/9046946459_a41fbfe614_s.jpg",
            "http://farm8.staticflickr.com/7403/9046946715_85f13b91e5_s.jpg",
            "http://farm8.staticflickr.com/7315/9046944633_881f24c4fa_s.jpg",
            "http://farm4.staticflickr.com/3777/9049174610_bf51be8a07_s.jpg",
            "http://farm8.staticflickr.com/7324/9046946887_d96a28376c_s.jpg",
            "http://farm3.staticflickr.com/2828/9046946983_923887b17d_s.jpg",
            "http://farm4.staticflickr.com/3810/9046947167_3a51fffa0b_s.jpg",
            "http://farm4.staticflickr.com/3773/9049175264_b0ea30fa75_s.jpg",
            "http://farm4.staticflickr.com/3781/9046945893_f27db35c7e_s.jpg",
            "http://farm6.staticflickr.com/5344/9049177018_4621cb63db_s.jpg",
            "http://farm8.staticflickr.com/7307/9046947621_67e0394f7b_s.jpg",
            "http://farm6.staticflickr.com/5457/9046948185_3be564ac10_s.jpg",
            "http://farm4.staticflickr.com/3752/9046946459_a41fbfe614_s.jpg",
            "http://farm8.staticflickr.com/7403/9046946715_85f13b91e5_s.jpg"};


    public FriendsTabBtnFragment() {/*..\(^_^)/..*/}

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(40,40,20,paint);

        return circleBitmap;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        View view = inflater.inflate(R.layout.friends_tab, container, false);

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        populate(view);

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


    public void populate(View view) {

        friends = new ArrayList<Friend>();

        Friend friend1 = new Friend();
        friend1.setName("Jenny Jones");
        friend1.setFriend(true);

        Friend friend2 = new Friend();
        friend2.setName("Jenny Jones");
        friend2.setFriend(false);
        friend2.setFollowers(40);

        friends.add(friend1);
        friends.add(friend2);

        UserActivity userActivity = new UserActivity(this);
        userActivity.requestUserActivity("131849","2014-05-31 16:30:55");


        lv = (ListView) view.findViewById(android.R.id.list);
        if (lv != null) {
            myAdapter = new MyCustomAdapter(getActivity(), R.layout.friends_item, friends);
            lv.setAdapter(myAdapter);

            lv.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    //arg1.setBackgroundColor(Color.TRANSPARENT);
                }
            });
            lv.setVisibility(View.VISIBLE);
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<Friend> {

        public MyCustomAdapter(Context context, int textViewResourceId, List<Friend> list) {
            super(context, textViewResourceId, list);

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.friends_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.imgAvatar);
                convertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.imageURL = imageURLArray[position];
            new DownloadAsyncTask().execute(viewHolder);

            Friend friend = friends.get(position);

            TextView lblFollowers = (TextView) convertView.findViewById(R.id.lblFollowers);
            TextView lblName = (TextView) convertView.findViewById(R.id.lblName);
            //ImageView imgAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);
            ImageView imgFollowers = (ImageView) convertView.findViewById(R.id.imgFollowers);
            ImageView imgAddOrCheck = (ImageView) convertView.findViewById(R.id.imgAddOrCheck);
            ImageView imgAvatarCheck = (ImageView) convertView.findViewById(R.id.imgAvatarCheck);

            lblFollowers.setText(String.valueOf(friend.getFollowers()));
            lblName.setText(friend.getName());

            //imgAvatarCheck.setImageBitmap(getRoundedCornerBitmap(friend.getAvatar(), 20));

            if (friend.isFriend()) {
                imgAddOrCheck.setBackgroundResource(R.drawable.green_check_mark);
                imgAvatarCheck.setVisibility(View.VISIBLE);
            } else {
                imgAddOrCheck.setBackgroundResource(R.drawable.blue_plus_button);
                imgAvatarCheck.setVisibility(View.INVISIBLE);
            }

            if (friend.getFollowers() > 0){
                imgFollowers.setBackgroundResource(R.drawable.verify_arrow_green);
                lblFollowers.setTextColor(Color.GREEN);
            }else{
                imgFollowers.setBackgroundResource(R.drawable.verify_arrow_grey);
                lblFollowers.setTextColor(Color.LTGRAY);
            }

            return convertView;
        }

    }

    public void didReceiveUserActivity(ArrayList<ActivityItem> activityList){
        Log.w("Info",activityList.toString());
    }
    public void didReceiveUserActivityError(String error){
        Log.e("Activity_error",error);
    }
}
