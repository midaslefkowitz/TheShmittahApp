package com.theshmittahapp.android.views.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.theshmittahapp.android.R;

public class DefinitionActivity extends Activity {

	public static String TERM = "term";
	public static String DEF = "definition";
	
	private ShareActionProvider mShareActionProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_definition);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
				
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new DefinitionFragment()).commit();
		}
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
	
	/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	*/

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class DefinitionFragment extends Fragment {
		
		private String url = "http://www.mymakolet.com";
		
		public DefinitionFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_definition,
					container, false);
			ImageView ad = (ImageView) v.findViewById(R.id.ad);
			
			// no ad in landscape so check for null first
			ad.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Uri webpage = Uri.parse(url);
				    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
				    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				    startActivity(Intent.createChooser(intent, ""));				
				}
			});			
			Intent intent = getActivity().getIntent();
			String term = intent.getStringExtra(TERM);
			String def = intent.getStringExtra(DEF);
			
			((TextView) v.findViewById(R.id.term)).setText(term);
			((TextView) v.findViewById(R.id.def)).setText(def);
			
			// Set title of activity to term
			getActivity().getActionBar().setTitle(term);
			
			return v;
		}
	}
}
