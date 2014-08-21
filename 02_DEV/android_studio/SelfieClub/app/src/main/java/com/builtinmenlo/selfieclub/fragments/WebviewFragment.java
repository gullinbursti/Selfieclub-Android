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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.activity.MainActivity;

public class WebviewFragment extends Fragment {
    public static final String EXTRA_URL_ITEM = "urlAdress";

    private ProgressBar loadingIcon;

    public WebviewFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        final View view = inflater.inflate(R.layout.webview, container, false);

        loadingIcon = (ProgressBar)view.findViewById(R.id.loadingIcon);

        view.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(WebviewFragment.this);
                transaction.commit();
            }
        });



        Bundle bundle = getArguments();
        String url = null;
        if (bundle!= null) {
            url = bundle.getString(EXTRA_URL_ITEM);
        }

        WebView webview = (WebView)view.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingIcon.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingIcon.setVisibility(View.INVISIBLE);
            }
        });
        webview.loadUrl(url);


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
        if (getActivity().getClass().getSimpleName().toString().equals(MainActivity.class.getSimpleName())) {
            final ActionBar actionBar = getActivity().getActionBar();
            actionBar.show();
        }
    }

}
