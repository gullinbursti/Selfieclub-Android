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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.NewsItem;
import com.builtinmenlo.selfieclub.models.PicoCandyManager;
import com.picocandy.android.data.PCContent;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class TimelineFragment extends Fragment {
    public static final String EXTRA_NEWS_ITEM = "news_item";

    public TimelineFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        View view = inflater.inflate(R.layout.timeline, container, false);


        Bundle bundle = getArguments();
        NewsItem newsItem = null;
        if (bundle!= null) {
            newsItem = (NewsItem) bundle.getSerializable(EXTRA_NEWS_ITEM);
        }

        Picasso.with(getActivity()).load(newsItem.getImageUrl() + "Tab_640x960.jpg").into((ImageView)view.findViewById(R.id.background));

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
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onAttach(activity);
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onDetach() {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onDetach();
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

}
