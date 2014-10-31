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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.theshmittahapp.android.HelperClasses.AppUtils;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.views.MyApp;

import static java.lang.String.format;

public class PDFFragment extends Fragment implements OnPageChangeListener {

    public static final String PDF = "pdf";
    private String url = "http://www.mymakolet.com";
    private String overviewPDFurl = "https://s3.eu-central-1.amazonaws.com/shmittahappfiles/Brief+Overview+of+Halachos+of+Shmittah.pdf";
    private String detailedPDFurl = "https://s3.eu-central-1.amazonaws.com/shmittahappfiles/Detailed+Halachos+of+Shmittah.pdf";

    private String mSubject;
    private String mBody;
    private String mLink;
    private String mPdfName;
    private Integer mPageNumber = 1;
    private String mTitle;
    private Tracker mTracker;

    public PDFFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker = ((MyApp) getActivity().getApplication()).getTracker(MyApp.TrackerName.APP_TRACKER);
        setHasOptionsMenu(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Set screen name.
        mTracker.setScreenName("The Shmittah App PDF " + mTitle + " Fragment");

        // Send a screen view.
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pdf, container, false);

        ImageView ad = (ImageView) v.findViewById(R.id.ad);
        AppUtils.setAd(getActivity(), ad, url);

        mPdfName = getArguments().getString(PDF);
        if (mPdfName.equals("detailed.pdf")) {
            mTitle = getResources().getString(R.string.detailed);
            mLink = detailedPDFurl;
        } else {
            mTitle = getResources().getString(R.string.overview);
            mLink = overviewPDFurl;
        }

        setupEmail();

        PDFView pdfView = (PDFView) v.findViewById(R.id.pdfview);

        pdfView.fromAsset(mPdfName)
                .defaultPage(1)
                .showMinimap(false)
                .enableSwipe(true)
                .onPageChange(this)
                .load();

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         if (mPdfName.equals("detailed.pdf")) {
             inflater.inflate(R.menu.detailed_pdf, menu);
         } else {
             inflater.inflate(R.menu.overview_pdf, menu);
         }
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
    public void onPageChanged(int page, int pageCount) {
        mPageNumber = page;
        getActivity().getActionBar().setTitle(format("%s %s / %s", mTitle, page, pageCount));
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

    private void setupEmail() {
        mSubject = mTitle + " (PDF) " + "from The Shmittah App";
        mBody = "Here's a link to the " + mTitle + " (PDF) " + "from The Shmittah App." +
            " Just click to open or right-click and \"Save link as...\" to download.</br>" +
            "<a href=\"" + mLink + "\">" + mTitle + " (PDF)</a>" +
            "</br></br>" +
            "Thanks for using <a href=\"http://www.TheShmittahApp.com\">The Shmittah App</a>";
    }
}
