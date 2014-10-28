package com.theshmittahapp.android.views.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.Button;

import com.theshmittahapp.android.R;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class DonateDialogFragment extends DialogFragment {

    private final int TOTAL_NEVER_DAYS = 10;
    private final int TOTAL_NOT_NOW_DAYS = 1;
    private final int TOTAL_NEVER_ENTRIES = 10;
    private final int TOTAL_NOT_NOW_ENTRIES = 5;
    private final String USER_DONATED = "user donated";
    private final String CLICKED_NEVER = "clicked never";
    private final String CLICKED_NOT_NOW = "clicked not now";
    private final String NEVER_CLICKED_DATE = "never clicked date";
    private final String NEVER_ENTRIES_REMAINING = "never entries remaining";
    private final String NOT_NOW_CLICKED_DATE = "not now clicked date";
    private final String NOT_NOW_ENTRIES_REMAINING = "not now entries remaining";

    private SharedPreferences mPrefs;
    private int mNeverRemaining;
    private int mNotNowRemaining;

    public DonateDialogFragment() {  }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        logEntries();

        if (!displayDialog()) {
            //return null;
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(inflater.inflate(R.layout.fragment_donate_dialog, null))
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.never_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Never
                    }
                })
                .setNegativeButton(R.string.not_now_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Not Now
                    }
                });
        builder.setCustomTitle(inflater.inflate(R.layout.dialog_title_layout, null));
        Dialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setBackgroundColor(getResources().getColor(R.color.dark_green));
                positiveButton.setTextColor(getResources().getColor(R.color.custom_theme_color));
                positiveButton.setTextSize(getResources().getDimension(R.dimen.textsize));

                Button negativeButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setBackgroundColor(getResources().getColor(R.color.dark_green));
                negativeButton.setTextColor(getResources().getColor(R.color.custom_theme_color));
                negativeButton.setTextSize(getResources().getDimension(R.dimen.textsize));
            }
        });


        return alertDialog;
    }

    private void logEntries() {
        SharedPreferences.Editor editor = mPrefs.edit();

        // Log never entries
        mNeverRemaining = (mPrefs.getInt(NEVER_ENTRIES_REMAINING, TOTAL_NEVER_ENTRIES)) - 1;
        editor.putInt(NEVER_ENTRIES_REMAINING, mNeverRemaining);

        // Log not now entries
        mNotNowRemaining = (mPrefs.getInt(NOT_NOW_ENTRIES_REMAINING, TOTAL_NOT_NOW_ENTRIES)) - 1;
        editor.putInt(NOT_NOW_ENTRIES_REMAINING, mNotNowRemaining);
    }

    private boolean displayDialog() {
        if (!userDonated()) {
            return false;
        }

        if (neverClickedRecently()) {
            return false;
        }

        if (notNowClickedRecently()) {
            return false;
        }
        return true;
    }

    /**
     * @return true if the user has donated
     */
    private boolean userDonated() {
        return mPrefs.getBoolean(USER_DONATED, false);
    }

    /**
     * @return true if "never" has been clicked recently
     */
    private boolean neverClickedRecently() {
        // check if "never" has been clicked at all
        if (!mPrefs.getBoolean(CLICKED_NEVER, true)) {
            return false;
        }
        DateTime today = new DateTime();
        // if have a saved date to show popup use that
        // by default set date to show popup for total days from now
        DateTime neverClickedDate = DateTime.parse(mPrefs.getString(NEVER_CLICKED_DATE,
                today.plusDays(TOTAL_NEVER_DAYS).toString()));

        if ( ( (Seconds.secondsBetween(neverClickedDate, today)).getSeconds() < 0) &&
                (mPrefs.getInt(NEVER_ENTRIES_REMAINING, TOTAL_NEVER_DAYS) > 0)
              ) {
            return false;
        }
        return true;
    }

    /**
     * @return true if "not now" has been clicked recently
     */
    private boolean notNowClickedRecently() {
        // check if "not now" has been clicked at all
        if (!mPrefs.getBoolean(CLICKED_NOT_NOW, true)) {
            return false;
        }
        DateTime today = new DateTime();
        // if have a saved date to show popup use that
        // by default set date to show popup for total days from now
        DateTime notNowClickedDate = DateTime.parse(mPrefs.getString(NOT_NOW_CLICKED_DATE,
                today.plusDays(TOTAL_NOT_NOW_DAYS).toString()));

        if ( ( (Seconds.secondsBetween(notNowClickedDate, today)).getSeconds() < 0) &&
                (mPrefs.getInt(NOT_NOW_ENTRIES_REMAINING, TOTAL_NOT_NOW_DAYS) > 0)
                ) {
            return false;
        }
        return true;
    }
}
