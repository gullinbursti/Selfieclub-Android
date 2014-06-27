/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class TabButtonListener
 * @project SelfieClub
 * @package com.builtinmenlo.selfieclub.listeners
 *
 * @author Matt.H <matt@builtinmenlo.com>
 * @created 16-Apr-2014 @ 13:36
 * @copyright (c) 2014 Built in Menlo, LLC. All rights reserved.
 *
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~·'
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~**/


package com.builtinmenlo.selfieclub.listeners;


//] includes [!]>
//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.text.Html;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class TabButtonListener implements ActionBar.TabListener {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

	//] class properties ]>
	//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
	Fragment fragment;
	//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

	// <*] class constructor [*>
	public TabButtonListener (Fragment fragment) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		this.fragment = fragment;
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	//]~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=[>
	//]~=~=~=~=~=~=~=~=~=[>


	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        RelativeLayout tabLayout = (RelativeLayout) tab.getCustomView(); //get the view for the tab
        ImageView redDot = (ImageView) tabLayout.findViewById(R.id.imgRedDot);
        if (tabLayout != null && redDot == null) {
            ((TextView)tabLayout.findViewById(R.id.lblTitleText)).setTextColor(Color.parseColor("#005de9"));
        }

		fragmentTransaction.replace(R.id.tab_fragment_layout, fragment);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        RelativeLayout tabLayout = (RelativeLayout) tab.getCustomView(); //get the view for the tab
        ImageView redDot = (ImageView) tabLayout.findViewById(R.id.imgRedDot);
        if (tabLayout != null && redDot == null) {
            ((TextView)tabLayout.findViewById(R.id.lblTitleText)).setTextColor(Color.parseColor("#bababa"));
        }
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }

        //fragmentTransaction.remove(fragment);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯
}
