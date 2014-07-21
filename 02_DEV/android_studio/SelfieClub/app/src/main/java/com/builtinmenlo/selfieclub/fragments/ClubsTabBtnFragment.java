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
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.models.UserClubsProtocol;
import com.builtinmenlo.selfieclub.models.UserManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class declaration [¡]>
public class ClubsTabBtnFragment extends Fragment implements UserClubsProtocol {

    private GridView gridClubs;
    private ArrayList<Club> clubs;
    private MyCustomAdapter adapter;

    public ClubsTabBtnFragment() {/*..\(^_^)/..*/}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clubs_tab, container, false);

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        gridClubs = (GridView) view.findViewById(R.id.gridMenu);
        clubs = new ArrayList<Club>();
        populate();

        UserManager userManager = new UserManager();
        userManager.requestUserClubs(this, "131820");


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
                    //arg1.setBackgroundColor(Color.TRANSPARENT);
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


            ViewHolder viewHolder;

            if (position == 0) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.clubs_item, parent, false);
                viewHolder = new ViewHolder();
                TextView lblText = (TextView) convertView.findViewById(R.id.lblClubName);
                lblText.setText("Create a club");
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgClub);
                viewHolder.imageView.setImageResource(R.drawable.selector_add_club);
                convertView.setTag(viewHolder);
            } else {
                if (convertView == null) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    convertView = inflater.inflate(R.layout.clubs_item, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgClub);
                    convertView.setTag(viewHolder);
                } else {

                    Club club = clubs.get(position - 1);

                    viewHolder = (ViewHolder) convertView.getTag();
                    viewHolder.imageURL = club.getClubImage() + "Large_640x1136.jpg";
                    new DownloadAsyncTask().execute(viewHolder);

                    TextView lblText = (TextView) convertView.findViewById(R.id.lblClubName);

                    lblText.setText(club.getClubName());
                }
            }
            return convertView;
        }
    }


    public void didReceiveUserClubs(ArrayList<Club> userClubs) {
        // TODO Add here the rendering on the clubs
        Log.i(this.getActivity().getClass().getName(), userClubs.toString());
        clubs = userClubs;
        adapter.notifyDataSetChanged();

    }

    public void didReceiveUserClubsError(String errorMessage) {
        //TODO Add here the error management for the get clubs request
    }
}
