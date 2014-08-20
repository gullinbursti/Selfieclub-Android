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

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.NewsItem;
import com.builtinmenlo.selfieclub.models.PicoCandyManager;
import com.picocandy.android.data.PCContent;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimelineFragment extends Fragment {
    public static final String EXTRA_NEWS_ITEM = "news_item";

    public TimelineFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        final View view = inflater.inflate(R.layout.timeline, container, false);

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new NewsTabBtnFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.commit();
            }
        });



        Bundle bundle = getArguments();
        NewsItem newsItem = null;
        if (bundle!= null) {
            newsItem = (NewsItem) bundle.getSerializable(EXTRA_NEWS_ITEM);
        }

        ((TextView)view.findViewById(R.id.lblTitleText)).setText(newsItem.getClubName());
        ((TextView)view.findViewById(R.id.lblUserName)).setText(newsItem.getUserName());
        SimpleDateFormat from_format = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
        Date rowDate = null;
        try {
            rowDate = from_format.parse(newsItem.getTimestamp());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rowDate);

        long time = (((Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis()) / 1000) % 3600) / 60;

        ((TextView)view.findViewById(R.id.lblTimeStamp)).setText(String.valueOf(time) + getString(R.string.label_minutes_ago));

        Picasso.with(getActivity()).load(newsItem.getImageUrl() + "Tab_640x960.jpg").into((ImageView)view.findViewById(R.id.background), new Callback() {

            @Override
            public void onSuccess() {
                view.findViewById(R.id.loadingIcon).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {
                view.findViewById(R.id.loadingIcon).setVisibility(View.INVISIBLE);
            }
        });

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        LinearLayout listStickers = (LinearLayout) view.findViewById(R.id.listStickers);

        for (int i = 0; i < newsItem.getStatus().length(); i++) {
            try {
                ImageView imgSticker = new ImageView(getActivity());
                listStickers.addView(imgSticker);
                if (newsItem.getStatus() != null && newsItem.getStatus().length() > 0) {
                    PCContent sticker = PicoCandyManager.sharedInstance().getStickerByName(newsItem.getStatus().getString(i));
                    if (sticker != null)
                        Picasso.with(getActivity()).load(sticker.getSmall_image()).into(imgSticker);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return view;
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onCreate(Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onCreate(savedInstanceState);
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.hide();
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onDetach() {
        super.onDetach();
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.show();
    }

}
