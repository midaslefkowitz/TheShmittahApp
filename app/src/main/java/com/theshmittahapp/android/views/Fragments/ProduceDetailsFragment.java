package com.theshmittahapp.android.views.Fragments;

import com.theshmittahapp.android.HelperClasses.AppUtils;
import com.theshmittahapp.android.models.Produce;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.views.Activities.ProduceDetailsActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProduceDetailsFragment extends Fragment {

	private String url = "http://www.mymakolet.com";
	
	public ProduceDetailsFragment() {}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		// inflate view
		View v = inflater.inflate(R.layout.fragment_produce_details, container, false);
		ImageView ad = (ImageView) v.findViewById(R.id.ad);

        AppUtils.setAd(getActivity(), ad, url);
		
		Intent intent = getActivity().getIntent();
		Produce produce = (Produce) intent.getSerializableExtra(ProduceDetailsActivity.PRODUCE);
		
		TextView nameTV = (TextView) v.findViewById(R.id.details_name);
		nameTV.setText(produce.getName());
		
		TextView startTV = (TextView) v.findViewById(R.id.details_start_date);
		startTV.setText(produce.getStartDate());

        /* Set Start Date Year TV */
        TextView startYearTV = (TextView) v.findViewById(R.id.details_start_date_year);
        String startYear = produce.getStartYear();
        startYearTV.setText(startYear);
        if (startYear == null || startYear.length()==0) {
            startYearTV.setVisibility(View.GONE);
        }
		
		/* Set End Date TV */
		TextView endTV1 = (TextView) v.findViewById(R.id.end_date);
		TextView endTV2 = (TextView) v.findViewById(R.id.details_end_date);
		String endDate = produce.getEndDate();
		endTV2.setText(endDate);
		if (endDate == null || endDate.length()==0) {
			endTV1.setVisibility(View.GONE);
			endTV2.setVisibility(View.GONE);
		}

        /* Set End Date Year TV */
        TextView endYearTV = (TextView) v.findViewById(R.id.details_end_date_year);
        String endYear = produce.getEndYear();
        endYearTV.setText(endYear);
        if (endYear == null || endYear.length()==0) {
            endYearTV.setVisibility(View.GONE);
        }

        /* Sefichim Texts */

        /* Sefichim Start Date */
        TextView sefichimTitleTV = (TextView) v.findViewById(R.id.sefichim_title);
        TextView sefichimStartTV1 = (TextView) v.findViewById(R.id.sefichim_start_date);
        TextView sefichimStartTV2 = (TextView) v.findViewById(R.id.details_sefichim_start_date);
        String sefichimStart = produce.getSefichimFrom();
        sefichimStartTV2.setText(sefichimStart);
        if (sefichimStart == null || sefichimStart.length()==0) {
            sefichimTitleTV.setVisibility(View.GONE);
            sefichimStartTV1.setVisibility(View.GONE);
            sefichimStartTV2.setVisibility(View.GONE);
        }

        /* Sefichim End Date */
        TextView sefichimEndTV1 = (TextView) v.findViewById(R.id.sefichim_end_date);
        TextView sefichimEndTV2 = (TextView) v.findViewById(R.id.details_sefichim_end_date);
        String sefichimEnd = produce.getSefichimTo();
        sefichimEndTV2.setText(sefichimEnd);
        if (sefichimEnd == null || sefichimEnd.length()==0) {
            sefichimEndTV1.setVisibility(View.GONE);
            sefichimEndTV2.setVisibility(View.GONE);
        }

		/* Set prepared TV */
		TextView preparedTV1 = (TextView) v.findViewById(R.id.prepared);
		TextView preparedTV2 = (TextView) v.findViewById(R.id.details_prepared);
		String prepared = produce.getPrepared();
		preparedTV2.setText(prepared);
		if (prepared == null || prepared.length()==0) {
			preparedTV1.setVisibility(View.GONE);
			preparedTV2.setVisibility(View.GONE);
		}
		
		/* Set peeled TV */
		TextView peeledTV1 = (TextView) v.findViewById(R.id.peeled);
		TextView peeledTV2 = (TextView) v.findViewById(R.id.details_peeled);
		String peeled = produce.getPeeled();
		peeledTV2.setText(peeled);
		if (peeled == null || peeled.length()==0) {
			peeledTV1.setVisibility(View.GONE);
			peeledTV2.setVisibility(View.GONE);
		}
		
		/* Set pits TV */
		TextView pitsTV1 = (TextView) v.findViewById(R.id.pits);
		TextView pitsTV2 = (TextView) v.findViewById(R.id.details_pits);
		String pits = produce.getSeeds();
		pitsTV2.setText(pits);
		if (pits == null || pits.length()==0) {
			pitsTV1.setVisibility(View.GONE);
			pitsTV2.setVisibility(View.GONE);
		}
		
		/* Set peel TV */
		TextView peelTV1 = (TextView) v.findViewById(R.id.peel);
		TextView peelTV2 = (TextView) v.findViewById(R.id.details_peel);
		String peel = produce.getPeel();
		peelTV2.setText(peel);
		if (peel == null || peel.length()==0) {
			peelTV1.setVisibility(View.GONE);
			peelTV2.setVisibility(View.GONE);
		}
		
		/* Set mash TV */
		TextView mashedTV1 = (TextView) v.findViewById(R.id.mashed);
		TextView mashedTV2 = (TextView) v.findViewById(R.id.details_mashed);
		String mashed = produce.getSqueezed();
		mashedTV2.setText(mashed);
		if (mashed == null || mashed.length()==0) {
			mashedTV1.setVisibility(View.GONE);
			mashedTV2.setVisibility(View.GONE);
		}
		
		/* Set comments TV */
		TextView commentsTV1 = (TextView) v.findViewById(R.id.comments);
		TextView commentsTV2 = (TextView) v.findViewById(R.id.details_comments);
		String comments = produce.getComments();
		commentsTV2.setText(comments);
		if (comments == null || comments.length()==0) {
			commentsTV1.setVisibility(View.GONE);
			commentsTV2.setVisibility(View.GONE);
		}
		
		// Set title of activity to Produce name
		getActivity().getActionBar().setTitle(produce.getName());
		
		return v;
	}
}