package com.theshmittahapp.android.views.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.theshmittahapp.android.R;

import static java.lang.String.format;

public class PDFFragment extends Fragment implements OnPageChangeListener {

    public static final String PDF = "pdf";
    private String url = "http://www.mymakolet.com";

    private String mPdfName;
    private Integer mPageNumber = 1;
    private String mTitle;

    public PDFFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pdf, container, false);
        ImageView ad = (ImageView) v.findViewById(R.id.ad);

        // no ad in landscape so check for null first
        if (ad!=null) {
            ad.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    Uri webpage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(Intent.createChooser(intent, ""));
                }
            });
        }

        mPdfName = getArguments().getString(PDF);
        mTitle = (mPdfName.equals("detailed.pdf")) ?
                    getResources().getString(R.string.detailed):
                    getResources().getString(R.string.overview);

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
    public void onPageChanged(int page, int pageCount) {
        mPageNumber = page;
        getActivity().getActionBar().setTitle(format("%s %s / %s", mTitle, page, pageCount));
    }
}
