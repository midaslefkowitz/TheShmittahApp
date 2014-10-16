package com.theshmittahapp.android.views.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.theshmittahapp.android.R;

public class LandingPageFragment extends Fragment {

	private String url = "http://www.mymakolet.com";

	public LandingPageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	// inflate view
		View rootView = inflater.inflate(R.layout.fragment_landing_page, container, false);	

		ImageView ad = (ImageView) rootView.findViewById(R.id.ad);
		ad.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Uri webpage = Uri.parse(url);
			    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
			    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			    startActivity(Intent.createChooser(intent, ""));				
			}
		});
		return rootView;
    }
}
