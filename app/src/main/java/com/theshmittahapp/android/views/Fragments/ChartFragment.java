package com.theshmittahapp.android.views.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.theshmittahapp.android.HelperClasses.Advertisement;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.HelperClasses.TouchImageView;
import com.theshmittahapp.android.views.MyApp;

public class ChartFragment extends Fragment {
	
	private String url = "http://www.mymakolet.com";
    private Tracker mTracker;

	public ChartFragment() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        mTracker = ((MyApp) getActivity().getApplication()).getTracker(MyApp.TrackerName.APP_TRACKER);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set screen name.
        mTracker.setScreenName("The Shmittah App About Fragment");

        // Send a screen view.
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		// inflate view
		View rootView = inflater.inflate(R.layout.fragment_chart, container, false);
		ImageView ad = (ImageView) rootView.findViewById(R.id.ad);

        Advertisement.setAd(getActivity(), ad, url);
		TouchImageView img = (TouchImageView) rootView.findViewById(R.id.chart);
		img.setImageResource(R.drawable.chart);
		return rootView;
	}
}
