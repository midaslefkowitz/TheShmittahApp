package com.theshmittahapp.android.views.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ShareActionProvider;

import com.theshmittahapp.android.HelperClasses.NavDrawerListAdapter;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.models.NavDrawerItem;
import com.theshmittahapp.android.views.Fragments.AboutFragment;
import com.theshmittahapp.android.views.Fragments.ChartFragment;
import com.theshmittahapp.android.views.Fragments.CommonTermsFragment;
import com.theshmittahapp.android.views.Fragments.FAQFragment;
import com.theshmittahapp.android.views.Fragments.LandingPageFragment;
import com.theshmittahapp.android.views.Fragments.PDFFragment;
import com.theshmittahapp.android.views.Fragments.ProduceFragment;

import java.util.ArrayList;

import com.newrelic.agent.android.NewRelic;

public class MainActivity extends Activity {
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
    private String mDetailedName = "detailed.pdf";
    private String mOverviewName = "overview.pdf";

	private String mShiurimUrl = "http://mp3shiur.com/viewProd.asp?catID=279&max=all&startAt=1";
	private String mRabbisEmailAddress = "theshmittahapp@gmail.com";
	private String mSubject = "Question from The Shmittah App";

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	//private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	private ShareActionProvider mShareActionProvider;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Required to track app stats with New Relic */
        NewRelic.withApplicationToken(
                "AA5c2072a8d0e78b2f2c2171782195865df39a8e78"
        ).start(this.getApplication());

        setContentView(R.layout.drawer_with_fragment_activity);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.drawer_items);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();		
		
		// adding nav drawer items to array
		// produce_list
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]) );
		// common terms
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]) );
		// faqs
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]) );
		// Halacha overview
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]) );
		// detailed halachos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]) );
		// chart
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5]) );
		// shiurim
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6]) );
		// ask the rabbi
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7]) );
		// about
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8]) );
		
		// Recycle the typed array
		// navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(this, navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(
				this, 					/* host Activity */
				mDrawerLayout,			/* DrawerLayout object */
				R.drawable.ic_drawer, 	/* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,	/* "open drawer" description for accessibility */
                R.string.drawer_close	/* "close drawer" description for accessibility */
		) {
			/* Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}
			
			/* Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		// Only load landing page fragment the first time the app is run on a device
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
        	 getFragmentManager().beginTransaction()
             .add(R.id.frame_container, new LandingPageFragment())
             .commit();
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putBoolean("firstTime", true);
	        editor.commit();
        } else if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}
    
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * Called when invalidateOptionsMenu() is triggered
	 */
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/*
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		Bundle args;
		boolean ask = false;
		boolean terms = false;
		boolean shiurim = false;
		switch (position) {
			case 0:
				// produce_list
				fragment = new ProduceFragment();
				break;
			case 1:
				// common terms
				fragment = new ProduceFragment();
				terms = true;
				break;
			case 2:
				// faqs
				fragment = new FAQFragment();
				break;
			case 3:
				// Halacha overview
				fragment = new PDFFragment();
				args = new Bundle();
    			args.putString(PDFFragment.PDF, mOverviewName);
    			fragment.setArguments(args);
				break;
			case 4:
				// detailed halachos
				fragment = new PDFFragment();
				args = new Bundle();
    			args.putString(PDFFragment.PDF, mDetailedName);
    			fragment.setArguments(args);
				break;
			case 5:
				// chart
				fragment = new ChartFragment();
				break;
			case 6:
				// shiurim
				fragment = new ProduceFragment();
				shiurim = true;
				break;
			case 7:
				// ask the rabbi
				fragment = new ProduceFragment();
				ask = true;
				break;
			case 8:
				// about
				fragment = new AboutFragment();
				break;
			default:
				break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
            if (position == 6 || position == 7) {
                setTitle(navMenuTitles[0]);
            } else {
                setTitle(navMenuTitles[position]);
            }

			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
		
		// placed here to force close drawer first
        if (ask) {
        	//setTitle(R.string.app_name);
        	ask = false;
        	askTheRabbi();
        } else if (terms) {
        	//setTitle(R.string.app_name);
        	terms = false;
        	commonTerms();
        } else if (shiurim) {
        	shiurim = false;
        	onlineShiruim();
        }
	}

	private void askTheRabbi() {	
	    Intent intent = new Intent(Intent.ACTION_SENDTO);
	    intent.setType("plain/text");
	    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
	    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { mRabbisEmailAddress });
	    intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
	    if (intent.resolveActivity(getPackageManager()) != null) {
	        startActivity(Intent.createChooser(intent, ""));
	    }
	}
	
	private void commonTerms() {
		Intent intent = new Intent(this, CommonTermsActivity.class);
		startActivity(intent);
	}
	
	private void onlineShiruim() {
		Uri webpage = Uri.parse(mShiurimUrl);
	    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
	    if (intent.resolveActivity(getPackageManager()) != null) {
	        startActivity(Intent.createChooser(intent, ""));
	    }
	}
	
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}