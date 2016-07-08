package com.tl.pro.musicapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 * Created by Administrator on 2016/7/8.
 */
public class MyFragment extends Fragment {

	private String mTitle;
	private static final String BUNDLE_TITLE = "title";

	public static MyFragment newInstance(String title){
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, title);
		MyFragment frg = new MyFragment();
		frg.setArguments(bundle);
		return frg;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		Bundle bundle = getArguments();
		if(null != bundle){
			mTitle = bundle.getString(BUNDLE_TITLE);
		}
		TextView tv = new TextView(getActivity());
		tv.setText(mTitle);
		tv.setGravity(Gravity.CENTER);

		return tv;
	}
}
