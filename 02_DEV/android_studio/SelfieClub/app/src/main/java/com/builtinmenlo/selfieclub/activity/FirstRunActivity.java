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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewConfiguration;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.fragments.FirstRunCountrySelectorFragment;
import com.builtinmenlo.selfieclub.fragments.FirstRunEnterPinFragment;
import com.builtinmenlo.selfieclub.fragments.FirstRunRegistrationFragment;

import java.lang.reflect.Field;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class FirstRunActivity extends Activity {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

    //] class properties ]>
    //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
    public final static String INTENT_PAYLOAD_AS_STRING = "com.builtinmenlo.selfieclub.activity.INTENT_PAYLOAD_AS_STRING";

    Tab friendsTab, clubsTab, newsTab, notificationsTab;

    Fragment registration = new FirstRunRegistrationFragment();
    Fragment countrySelector = new FirstRunCountrySelectorFragment();
    Fragment enterPin = new FirstRunEnterPinFragment();
    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


    protected void onCreate(Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        ActionBar topNavActionBar = getActionBar();
        topNavActionBar.setDisplayShowTitleEnabled(false);

        Log.i("__LOCAL_CLASS_NAME", this.getLocalClassName() + "][" + this.getClass().toString());

        LayoutInflater mInflater = LayoutInflater.from(this);

        Fragment newFragment = new FirstRunRegistrationFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();

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
}
