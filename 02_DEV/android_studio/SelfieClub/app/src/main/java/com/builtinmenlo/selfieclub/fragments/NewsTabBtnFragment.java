/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class NewsTabBtnFragment
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
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.NewsItem;
import com.builtinmenlo.selfieclub.models.ClubManager;
import com.builtinmenlo.selfieclub.models.NewsFeedProtocol;

;import java.util.ArrayList;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class NewsTabBtnFragment extends Fragment implements NewsFeedProtocol{
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

	//] class properties ]>
	//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
	//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

	// <*] class constructor [*>
	public NewsTabBtnFragment() {/*..\(^_^)/..*/}
	//]~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=[>
	//]~=~=~=~=~=~=~=~=~=[>

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		container.setBackgroundColor(getResources().getColor(R.color.activity_verify_bg_color));

		TextView textView = (TextView) container.findViewById(R.id.fragment_textview);
		//textView.setText(R.string.verify_message);
        ClubManager clubManager = new ClubManager();
        clubManager.requestNews(this,"131820");

		return (inflater.inflate(R.layout.fragment_tab_btn, container, false));
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

    public void didReceiveNewsFeed(ArrayList<NewsItem> newsItemArrayList){

    }
    public void didReceiveNewsFeedError(String errorMessage){

    }
}
