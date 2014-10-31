//TODO: make the thank you look nicer

package com.theshmittahapp.android.views.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.theshmittahapp.android.R;


public class ThankYouFragment extends Fragment {

    public ThankYouFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_thank_you, container, false);
        return rootView;
    }
}