package com.theshmittahapp.android.views.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.views.Activities.DonateActivity;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class DonateDialogFragment extends DialogFragment {

    private static final int TOTAL_NEVER_DAYS = 30;
    private static final int TOTAL_NEVER_ENTRIES = 15;
    private static final int TOTAL_NOT_NOW_DAYS = 5;
    private static final int TOTAL_NOT_NOW_ENTRIES = 5;
    private static final int TOTAL_FREE_DAYS = 5;
    private static final int TOTAL_FREE_ENTRIES = 5;
    private static final String DEFAULT_DONATION = "5";

    public static final String USER_DONATED = "user donated";
    private static final String CLICKED_NEVER = "clicked never";
    private static final String CLICKED_NOT_NOW = "clicked not now";
    private static final String NEVER_CLICKED_DATE = "never clicked date";
    private static final String NEVER_ENTRIES_REMAINING = "never entries remaining";
    private static final String NOT_NOW_CLICKED_DATE = "not now clicked date";
    private static final String NOT_NOW_ENTRIES_REMAINING = "not now entries remaining";
    private static final String FREE_DATE = "free date";
    private static final String FREE_ENTRIES_REMAINING = "free entries remaining";
    private static final String TAG = "Donate Dialog";
    private static final String FROM_DRAWER = "from drawer";

    private SharedPreferences mPrefs;
    private int mNeverRemaining;
    private int mNotNowRemaining;
    private int mFreeRemaining;
    private LayoutInflater mInflater;
    private boolean mFromDrawer;

    public DonateDialogFragment() { }

    public static DonateDialogFragment newInstance (boolean fromDrawer) {
        DonateDialogFragment f = new DonateDialogFragment();

        Bundle args = new Bundle();
        args.putBoolean(FROM_DRAWER, fromDrawer);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mFromDrawer = getArguments().getBoolean(FROM_DRAWER);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!mPrefs.getBoolean("firstTimeDonate", false)) {
            firstEntranceInit();
        }

        logEntries();

        if (!mFromDrawer) {
            if (!displayDialog()) {
                return null;
            }
        }

        View v = getCustomView();

        Dialog alertDialog = createDialog(v);

        alertDialog = customizeButtons(alertDialog);

        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return alertDialog;
    }


    private void firstEntranceInit() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean("firstTimeDonate", true);
        editor.putString(FREE_DATE, new DateTime().plusDays(TOTAL_FREE_DAYS).toString());
        editor.commit();
    }

    private View getCustomView() {
        mInflater = getActivity().getLayoutInflater();
        View v = mInflater.inflate(R.layout.fragment_donate_dialog, null);
        final EditText donationAmountET = (EditText) v.findViewById(R.id.donation_amount);
        ImageView paypal = (ImageView) v.findViewById(R.id.btn_paypal);
        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String donationAmountStr = donationAmountET.getText().toString();
                if (donationAmountStr == null || donationAmountStr.length() == 0) {
                    donationAmountStr = DEFAULT_DONATION;
                }
                donatePayPalOnClick(donationAmountStr);
            }
        });
        return v;
    }

    private Dialog createDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v);
        if (!mFromDrawer) {
            builder.setPositiveButton(R.string.never_btn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                        // Never
                        resetNeverValues();
                }
            })
                    .setNegativeButton(R.string.not_now_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        // Not Now
                        resetNotNowValues();
                        }
                    });
        }
        return (builder.create());
    }

    private Dialog customizeButtons(Dialog alertDialog) {

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setBackgroundResource(R.drawable.button_custom);
                positiveButton.setTextColor(getResources().getColor(R.color.custom_theme_color));
                positiveButton.setTextSize(getResources().getDimension(R.dimen.textsize));


                Button negativeButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setBackgroundResource(R.drawable.button_custom);
                negativeButton.setTextColor(getResources().getColor(R.color.custom_theme_color));
                negativeButton.setTextSize(getResources().getDimension(R.dimen.textsize));
            }
        });
        return alertDialog;
    }

    private void resetNeverValues() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(NEVER_ENTRIES_REMAINING, TOTAL_NEVER_ENTRIES);
        editor.putString(NEVER_CLICKED_DATE, new DateTime().plusDays(TOTAL_NEVER_DAYS).toString());
        editor.commit();
    }

    private void resetNotNowValues() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(NOT_NOW_ENTRIES_REMAINING, TOTAL_NOT_NOW_ENTRIES);
        editor.putString(NOT_NOW_CLICKED_DATE, new DateTime().plusDays(TOTAL_NOT_NOW_DAYS).toString());
        editor.commit();
    }

    private void logEntries() {
        SharedPreferences.Editor editor = mPrefs.edit();

        // Log free entries
        mFreeRemaining = (mPrefs.getInt(FREE_ENTRIES_REMAINING, TOTAL_FREE_ENTRIES)) - 1;
        editor.putInt(FREE_ENTRIES_REMAINING, mFreeRemaining);

        // Log never entries
        mNeverRemaining = (mPrefs.getInt(NEVER_ENTRIES_REMAINING, TOTAL_NEVER_ENTRIES)) - 1;
        editor.putInt(NEVER_ENTRIES_REMAINING, mNeverRemaining);

        // Log not now entries
        mNotNowRemaining = (mPrefs.getInt(NOT_NOW_ENTRIES_REMAINING, TOTAL_NOT_NOW_ENTRIES)) - 1;
        editor.putInt(NOT_NOW_ENTRIES_REMAINING, mNotNowRemaining);

        editor.commit();
    }

    private boolean displayDialog() {
        if (userDonated()) {
            return false;
        }

        if (freeUsesRemaining()) {
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

    private boolean freeUsesRemaining() {
        DateTime today = new DateTime();
        DateTime freeDate = DateTime.parse(mPrefs.getString(FREE_DATE,
                today.plusDays(TOTAL_FREE_DAYS).toString()));

        return (((Seconds.secondsBetween(freeDate, today)).getSeconds() < 0) &&
                (mPrefs.getInt(NEVER_ENTRIES_REMAINING, TOTAL_NEVER_DAYS) > 0));
    }

    /**
     * @return true if the user has donated
     */
    private boolean userDonated() {
        boolean donated = mPrefs.getBoolean(USER_DONATED, false);
        return donated;
    }

    /**
     * @return true if "never" has been clicked recently
     */
    private boolean neverClickedRecently() {
        // only need to check if recent if never was clicked
        if (mPrefs.getBoolean(CLICKED_NEVER, false)) {
            DateTime today = new DateTime();
            // if have a saved date to show popup use that
            // by default set date to show popup for total days from now
            DateTime neverClickedDate = DateTime.parse(mPrefs.getString(NEVER_CLICKED_DATE,
                    today.plusDays(TOTAL_NEVER_DAYS).toString()));

            // get entries remaining
            int entriesRemaining = mPrefs.getInt(NEVER_ENTRIES_REMAINING, TOTAL_NEVER_DAYS);
            // find the seconds between future - today
            double timeRemaining = Seconds.secondsBetween(today, neverClickedDate).getSeconds();

            // return if click was recent
            return !(( timeRemaining > 0) || ( entriesRemaining > 0));
        }
        return false;
    }

    /**
     * @return true if "not now" has been clicked recently
     */
    private boolean notNowClickedRecently() {
        // check if "not now" has been clicked at all
        if (mPrefs.getBoolean(CLICKED_NOT_NOW, false)) {
            DateTime today = new DateTime();
            // if have a saved date to show popup use that
            // by default set date to show popup for total days from now
            DateTime notNowClickedDate = DateTime.parse(mPrefs.getString(NOT_NOW_CLICKED_DATE,
                    today.plusDays(TOTAL_NOT_NOW_DAYS).toString()));
            // get entries remaining
            int entriesRemaining = mPrefs.getInt(NOT_NOW_ENTRIES_REMAINING, TOTAL_NOT_NOW_DAYS);
            // find the seconds between future - today
            double timeRemaining = Seconds.secondsBetween(today, notNowClickedDate).getSeconds();

            // return if click was recent
            return !(( timeRemaining > 0) || ( entriesRemaining > 0));
        }
        return false;
    }

    private void donatePayPalOnClick(String donationAmountStr) {
        Intent intent = new Intent(getActivity(), DonateActivity.class);
        intent.putExtra(DonateActivity.DONATE_AMOUNT, donationAmountStr);
        startActivity(intent);
    }
}