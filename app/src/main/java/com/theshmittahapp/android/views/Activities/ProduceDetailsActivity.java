package com.theshmittahapp.android.views.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.views.Fragments.ProduceDetailsFragment;
import com.theshmittahapp.android.views.MyApp;

public class ProduceDetailsActivity extends Activity {

	public static final String PRODUCE = "produce";
	
	private ShareActionProvider mShareActionProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //Get a Tracker (should auto-report)
        Tracker t = ((MyApp) getApplication()).getTracker(MyApp.TrackerName.APP_TRACKER);
        t.enableAdvertisingIdCollection(true);
        setContentView(R.layout.activity_produce_details);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new ProduceDetailsFragment()).commit();
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
}