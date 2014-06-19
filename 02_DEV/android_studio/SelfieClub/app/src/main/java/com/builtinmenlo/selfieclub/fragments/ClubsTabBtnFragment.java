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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.models.UserClubsProtocol;
import com.builtinmenlo.selfieclub.models.UserManager;

;import org.json.JSONObject;

import java.util.ArrayList;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class ClubsTabBtnFragment extends Fragment implements UserClubsProtocol{
    public ClubsTabBtnFragment() {/*..\(^_^)/..*/}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		container.setBackgroundColor(getResources().getColor(R.color.activity_clubs_bg_color));

		TextView textView = (TextView) container.findViewById(R.id.fragment_textview);
		//textView.setText(R.string.clubs_message);

        UserManager userManager = new UserManager();
        userManager.requestUserClubs(this,"131820");


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

    public void didReceiveUserClubs(ArrayList<Club> userClubs){
    // TODO Add here the rendering on the clubs
        Log.i(this.getActivity().getClass().getName(),userClubs.toString());

    }
    public void didReceiveUserClubsError(String errorMessage){
    //TODO Add here the error management for the get clubs request
    }
}
