package com.theshmittahapp.android.views.Activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.views.MyApp;

public class NoResults extends Activity {

    public static String QUERY = "query";

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get a Tracker (should auto-report)
        ((MyApp) getApplication()).getTracker(MyApp.TrackerName.APP_TRACKER);
        setContentView(R.layout.activity_produce_details);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_no_results);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        // Create and set the share Intent
        mShareActionProvider.setShareIntent(createShareIntent());

        // Return true to display menu
        return true;
    }

    private Intent createShareIntent() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide what to do with it.
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_body));
        return intent;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_no_results, container, false);
            Intent i = getActivity().getIntent();
            String query = i.getStringExtra(QUERY);
            String result = getResources().getString(R.string.no_result) + " \"" +
                    query + "\".";
            TextView resultTV = (TextView) rootView.findViewById(R.id.no_result);
            resultTV.setText(result);
            return rootView;
        }
    }
}
