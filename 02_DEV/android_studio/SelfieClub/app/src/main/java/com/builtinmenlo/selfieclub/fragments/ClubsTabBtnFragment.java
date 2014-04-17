package com.builtinmenlo.selfieclub.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;

public class ClubsTabBtnFragment extends Fragment {
    public ClubsTabBtnFragment() {/*..\(^_^)/..*/}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		container.setBackgroundColor(getResources().getColor(R.color.activity_clubs_bg_color));

		TextView textView = (TextView) container.findViewById(R.id.fragment_textview);
		textView.setText(R.string.clubs_message);

		return (inflater.inflate(R.layout.fragment_tab_btn, container, false));
	}


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public void onDetach() {
		super.onDetach();
	}
}
