package com.theshmittahapp.android.views.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.views.MyApp;

public class AboutFragment extends Fragment {
	
	private String url = "http://www.mymakolet.com";
    private Tracker mTracker;

    public AboutFragment() {}

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
		View rootView = inflater.inflate(R.layout.fragment_about, container, false);	
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

        String versionName = "";
        try {
            versionName = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView versionTV = (TextView) rootView.findViewById(R.id.about_version);
        versionTV.setText(versionTV.getText() + " " + versionName);
        return rootView;
	}
}
