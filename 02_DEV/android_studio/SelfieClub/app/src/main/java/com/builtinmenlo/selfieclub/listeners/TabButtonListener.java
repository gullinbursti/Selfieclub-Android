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
import android.widget.RelativeLayout;

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
        if (tabLayout != null) {
            tabLayout.setBackgroundColor(Color.RED); // change the background
            tab.setCustomView(tabLayout); // assign back to the tab
        }

		fragmentTransaction.replace(R.id.tab_fragment_layout, fragment);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        if (fragment != null) {
            String formattedName = tab.getText().toString();
            tab.setText(Html.fromHtml(formattedName));
            // Detach the fragment, because another one is being attached
            fragmentTransaction.remove(fragment);
        }

        //fragmentTransaction.remove(fragment);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯
}
