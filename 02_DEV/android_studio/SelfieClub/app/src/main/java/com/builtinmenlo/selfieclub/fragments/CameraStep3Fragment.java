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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.activity.CameraPreview;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.models.UserClubsProtocol;
import com.builtinmenlo.selfieclub.models.UserManager;
import com.builtinmenlo.selfieclub.util.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class CameraStep3Fragment extends Fragment implements UserClubsProtocol {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

    //] class properties ]>
    //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
    private ImageDownloader downloader;
    private ListView listClubs;
    private ArrayList<Club> clubs;
    private MyCustomAdapter adapter;

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
        if (bundle != null) {
            avatarImage = bundle.getByteArray(CameraPreview.EXTRA_IMAGE);
        }

        downloader = new ImageDownloader(getActivity(), "camera_clubs");
        listClubs = (ListView) view.findViewById(android.R.id.list);
        clubs = new ArrayList<Club>();
        populate();

        UserManager userManager = new UserManager();
        userManager.requestUserClubs(this, "131820");

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
                    //if (arg1.findViewById(R.id.imgAddOrCheck).getBackground().equals(getResources().getDrawable(R.drawable.green_selection_dot)))
                    arg1.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.green_selection_dot);
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

            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.camera_step3_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imgClub = (ImageView) convertView.findViewById(R.id.imgClub);
                viewHolder.loadingImage = (ProgressBar) convertView.findViewById(R.id.loadingImage);
                viewHolder.lblClubName = (TextView) convertView.findViewById(R.id.lblClubName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (position >= clubs.size()){
                viewHolder.lblClubName.setText("Select All");
                Bitmap image = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
                image.eraseColor(Color.TRANSPARENT);
                viewHolder.imgClub.setImageBitmap(image);
            } else {
                Club club = clubs.get(position);
                //new DownloadAsyncTask().execute(viewHolder);
                downloader.DisplayImage(club.getClubImage() + "Large_640x1136.jpg", String.valueOf(position), getActivity(), viewHolder.imgClub, viewHolder.loadingImage);
                viewHolder.lblClubName.setText(club.getClubName());
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
