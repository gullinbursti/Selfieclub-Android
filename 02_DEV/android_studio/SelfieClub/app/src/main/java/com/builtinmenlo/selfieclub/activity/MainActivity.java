/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class MainActivity
 * @project SelfieClub
 * @package com.builtinmenlo.selfieclub.activity
 *
 * @author Matt.H <matt@builtinmenlo.com>
 * @created 16-Apr-2014 @ 11:50
 * @copyright (c) 2014 Built in Menlo, LLC. All rights reserved.
 *
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~·'
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~**/


package com.builtinmenlo.selfieclub.activity;


//] includes [!]>
//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.fragments.ActivityTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.ClubsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.FriendsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.NewsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.WebviewFragment;
import com.builtinmenlo.selfieclub.models.ApplicationManager;
import com.builtinmenlo.selfieclub.models.DeepLinksManager;
import com.builtinmenlo.selfieclub.models.KeenManager;
import com.builtinmenlo.selfieclub.models.UserClubsProtocol;
import com.builtinmenlo.selfieclub.models.UserManager;

import com.tapstream.sdk.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class MainActivity extends Activity implements UserClubsProtocol{
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

    //] class properties ]>
    //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
    public final static String INTENT_PAYLOAD_AS_STRING = "com.builtinmenlo.selfieclub.activity.INTENT_PAYLOAD_AS_STRING";


    private Tab friendsTab, clubsTab, newsTab, notificationsTab;

    public Fragment tabSelected;

    private Fragment friendsFragment = new FriendsTabBtnFragment();
    private Fragment clubsFragment = new ClubsTabBtnFragment();
    private Fragment newsFragment = new NewsTabBtnFragment();
    private Fragment notificationsFragment = new ActivityTabBtnFragment();
    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


    protected void onCreate(Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

	    Config config = new Config();
	    Tapstream.create(getApplication(), Constants.TAPSTREAM_PROJECT_NAME, Constants.TAPSTREAM_SECRET_KEY, config);

        DeepLinksManager deepLinksManager = new DeepLinksManager();
        deepLinksManager.context = this.getApplicationContext();
        deepLinksManager.activity = this;
        deepLinksManager.deepLinkRegistry(data);

        setContentView(R.layout.activity_main);
        getOverflowMenu();

        ActionBar topNavActionBar = getActionBar();
        topNavActionBar.setDisplayShowTitleEnabled(false);
        topNavActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        topNavActionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.white)));

        LayoutInflater mInflater = LayoutInflater.from(this);

        friendsTab = topNavActionBar.newTab();
        View friendsTabCustomView = mInflater.inflate(R.layout.item_tab_bar, null);
        TextView friendsTitleTextView = (TextView) friendsTabCustomView.findViewById(R.id.lblTitleText);
        friendsTitleTextView.setText(R.string.tab_friends_title);
        friendsTab.setCustomView(friendsTabCustomView);
        //friendsTab.setText(R.string.tab_friends_title);
        friendsTab.setTabListener(new TabButtonListener(friendsFragment));
        topNavActionBar.addTab(friendsTab);

        newsTab = topNavActionBar.newTab();
        View newsTabCustomView = mInflater.inflate(R.layout.item_tab_bar, null);
        TextView newsTitleTextView = (TextView) newsTabCustomView.findViewById(R.id.lblTitleText);
        newsTitleTextView.setText(R.string.tab_news_title);
        newsTab.setCustomView(newsTabCustomView);
        //newsTab.setText(R.string.tab_news_title);
        newsTab.setTabListener(new TabButtonListener(newsFragment));
        topNavActionBar.addTab(newsTab);

        clubsTab = topNavActionBar.newTab();
        View clubsTabCustomView = mInflater.inflate(R.layout.item_tab_bar, null);
        TextView clubsTitleTextView = (TextView) clubsTabCustomView.findViewById(R.id.lblTitleText);
        clubsTitleTextView.setText(R.string.tab_clubs_title);
        clubsTab.setCustomView(clubsTabCustomView);
        //clubsTab.setText(R.string.tab_clubs_title);
        clubsTab.setTabListener(new TabButtonListener(clubsFragment));
        topNavActionBar.addTab(clubsTab);

        //TODO Use a real userId
        ApplicationManager applicationManager = new ApplicationManager(this);
        //applicationManager.setUserId("151159");
        //applicationManager.setUserName("matt");

        //Request the user's personal club
        UserManager userManager = new UserManager();
        String userId =applicationManager.getUserId();
        userManager.requestUserClubs(this,userId,this);

    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onMainCameraClick(View view) {

    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);

            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // inflate for action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.items, menu);
        return true;
    }

    // handle click events for action bar items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String url = null;
        switch (item.getItemId()) {
            case R.id.menuTerms:
                url = "Http://www.getselfieclub.com/terms.html";

                break;
            case R.id.menuNetworkStatus:
                url = "Http://mobile.twitter.com/selfiec_status";
                break;
            case R.id.menuRate:
                url = "Http://www.getselfieclub.com/rateapp.html";
                break;
            case  R.id.menuPrivacyPolicy:
                url = "Http://www.getselfieclub.com/privacy.html";
                break;
            case R.id.menuSupport:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("mailto:support@getselfieclub.com"));
                startActivity(intent);
                break;
            default:
                break;
        }

        if (url!=null) {
            WebviewFragment newFragment = new WebviewFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            Bundle bundle = new Bundle();
            bundle.putString(WebviewFragment.EXTRA_URL_ITEM, url);
            newFragment.setArguments(bundle);
            transaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();
        KeenManager keenManager = KeenManager.sharedInstance(this.getApplicationContext());
        keenManager.trackEvent(Constants.KEEN_EVENT_RESUMEBACKGROUND);
    }

    public void didReceiveUserClubs(ArrayList<Club> userClubs){
        //Looks for the user's personal club
        ApplicationManager applicationManager = new ApplicationManager(this);
        String username = applicationManager.getUserName();
        for (int i=0;i<userClubs.size();i++){
            Club club = userClubs.get(i);
            if(username.equalsIgnoreCase(club.getClubName())){
                applicationManager.setUserPersonalClubId(club.getClubId());
                break;
            }
        }
    }
    public void didReceiveUserClubsError(String errorMessage){
        Log.e("MainActivity","Error getting the clubs");
    }

    class TabButtonListener implements ActionBar.TabListener {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

        //] class properties ]>
        //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
        Fragment fragment;
        //]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

        // <*] class constructor [*>
        public TabButtonListener(Fragment fragment) {
            //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
            this.fragment = fragment;
        }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

        //]~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=[>
        //]~=~=~=~=~=~=~=~=~=[>


        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            //get the view for the tab
            RelativeLayout tabLayout = (RelativeLayout) tab.getCustomView();
            ImageView redDot = (ImageView) tabLayout.findViewById(R.id.imgRedDot);
            if (tabLayout != null && redDot == null) {
                ((TextView) tabLayout.findViewById(R.id.lblTitleText)).setTextColor(Color.parseColor("#005de9"));
                ((TextView) tabLayout.findViewById(R.id.lblTitleText)).setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                if (redDot != null) {
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_in);
                }
            }

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            tabSelected = fragment;
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            //get the view for the tab
            RelativeLayout tabLayout = (RelativeLayout) tab.getCustomView(); //get the view for the tab
            ImageView redDot = (ImageView) tabLayout.findViewById(R.id.imgRedDot);
            if (tabLayout != null && redDot == null) {
                ((TextView) tabLayout.findViewById(R.id.lblTitleText)).setTextColor(Color.parseColor("#bababa"));
                ((TextView) tabLayout.findViewById(R.id.lblTitleText)).setTypeface(Typeface.DEFAULT);
            } else {
                if (redDot != null) {
                    fragmentTransaction.setCustomAnimations(R.anim.fade_out,R.anim.slide_out);
                }
            }
            if (fragment != null) {
                fragmentTransaction.remove(fragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

    }
}
