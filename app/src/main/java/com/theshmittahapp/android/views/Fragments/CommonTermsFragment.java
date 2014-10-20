package com.theshmittahapp.android.views.Fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.views.Activities.DefinitionActivity;
import com.theshmittahapp.android.views.MyApp;

public class CommonTermsFragment extends Fragment{
	
	private String url = "http://www.mymakolet.com";
	
	private String[] mGlossary_terms;
	private String[] mGlossary_defs;
	private View mRootView;
	private ListView mListView;
    private Tracker mTracker;
	
	public CommonTermsFragment() { }

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
        mTracker.setScreenName("The Shmittah App Common Terms Fragment");

        // Send a screen view.
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		// inflate view
		mRootView = inflater.inflate(R.layout.fragment_common_terms, container, false);
		ImageView ad = (ImageView) mRootView.findViewById(R.id.ad);
		
		// no ad in landscape so check for null first
		if (ad!=null) {
			ad.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Uri webpage = Uri.parse(url);
				    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
				    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				    startActivity(Intent.createChooser(intent, ""));				
				}
			});
		}

		// create arrays of terms and defs
		mGlossary_terms = getResources().getStringArray(R.array.glossary_terms);
		mGlossary_defs = getResources().getStringArray(R.array.glossary_defs);
		
		// add list to the listview adapter
		addListToAdapter(mGlossary_terms, mGlossary_defs);

		return mRootView;
	}
	
	
	
	private void addListToAdapter(String[] terms, String[] defs) {
		
		List<String> allTermsList = new ArrayList<String>(Arrays.asList(terms));
		
		ArrayAdapter<String> glossaryAdapter =
                new ArrayAdapter<String>(
                    getActivity(), // The current context (this activity)
                    R.layout.glossary_list_item, // The name of the layout ID.
                    R.id.list_item_term_textview, // The ID of the textview to populate.
                    allTermsList); // The List to bind
		
		mListView = (ListView) mRootView.findViewById(R.id.terms_list);
		mListView.setAdapter(glossaryAdapter);

		/* Add Click Listener */

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), DefinitionActivity.class);
				String term = mGlossary_terms[position];
				String def = mGlossary_defs[position];
				intent.putExtra(DefinitionActivity.TERM, term);
				intent.putExtra(DefinitionActivity.DEF, def);
				startActivity(intent);
			}
		});
	}
}