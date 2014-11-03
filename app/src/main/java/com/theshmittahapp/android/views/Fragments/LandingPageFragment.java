package com.theshmittahapp.android.views.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.theshmittahapp.android.HelperClasses.AppUtils;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.views.MyApp;

public class LandingPageFragment extends Fragment {

	private String url = "http://www.mymakolet.com";

    private Tracker mTracker;

	public LandingPageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker = ((MyApp) getActivity().getApplication()).getTracker(MyApp.TrackerName.APP_TRACKER);
        mTracker.enableAdvertisingIdCollection(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set screen name.
        mTracker.setScreenName("The Shmittah App Landing Page Fragment");

        // Send a screen view.
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	// inflate view
		View rootView = inflater.inflate(R.layout.fragment_landing_page, container, false);

		ImageView ad = (ImageView) rootView.findViewById(R.id.ad);
        AppUtils.setAd(getActivity(), ad, url);
		return rootView;
    }
}
