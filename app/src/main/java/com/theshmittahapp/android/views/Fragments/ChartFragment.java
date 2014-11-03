package com.theshmittahapp.android.views.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.theshmittahapp.android.HelperClasses.AppUtils;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.HelperClasses.TouchImageView;
import com.theshmittahapp.android.views.MyApp;

public class ChartFragment extends Fragment {


    private String mSubject = "Link to the Produce Chart in The Shmittah App";

    private String mBody = "Here\'s a link to the chart used in The Shmittah App. Just click to open or right-click and \"Save link as...\" to download.</br>" +
            "<a href=\"https://s3.eu-central-1.amazonaws.com/shmittahappfiles/shmittah_chart.jpg\">Shmittah Chart (JPG)</a>" +
            "</br></br>" +
            "Thanks for using <a href=\"http://www.TheShmittahApp.com\">The Shmittah App</a>";

	private String url = "http://www.mymakolet.com";
    private Tracker mTracker;

	public ChartFragment() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker = ((MyApp) getActivity().getApplication()).getTracker(MyApp.TrackerName.APP_TRACKER);
        mTracker.enableAdvertisingIdCollection(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chart_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If this callback does not handle the item click, onPerformDefaultAction
        // of the ActionProvider is invoked. Hence, the provider encapsulates the
        // complete functionality of the menu item.
        int id = item.getItemId();
        switch (id) {
            case R.id.save_button:
                sendFile();
                break;
                    }
        return false;
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

        AppUtils.setAd(getActivity(), ad, url);
		TouchImageView img = (TouchImageView) rootView.findViewById(R.id.chart);
		img.setImageResource(R.drawable.chart);
		return rootView;
	}

    private void sendFile() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/html");
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mBody));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(Intent.createChooser(intent, ""));
        }

    }
}
