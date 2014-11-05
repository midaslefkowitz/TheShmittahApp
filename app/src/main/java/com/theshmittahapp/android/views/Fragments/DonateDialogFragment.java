package com.theshmittahapp.android.views.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

        View v = getCustomView();

        Dialog alertDialog = createDialog(v);

        alertDialog = customizeButtons(alertDialog);

        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return alertDialog;
    }

    private void firstEntranceInit() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean("firstTimeDonate", true);
        // User gets TOTAL_FREE_DAYS of time without seeing the donate dialog
        editor.putString(FREE_DATE, new DateTime().plusDays(TOTAL_FREE_DAYS).toString());
        editor.commit();
    }

    public void logEntries(SharedPreferences prefs) {

        SharedPreferences.Editor editor = prefs.edit();

        // Log free entries
        mFreeRemaining = (prefs.getInt(FREE_ENTRIES_REMAINING, TOTAL_FREE_ENTRIES)) - 1;
        editor.putInt(FREE_ENTRIES_REMAINING, mFreeRemaining);

        // Log never entries
        mNeverRemaining = (prefs.getInt(NEVER_ENTRIES_REMAINING, TOTAL_NEVER_ENTRIES)) - 1;
        editor.putInt(NEVER_ENTRIES_REMAINING, mNeverRemaining);

        // Log not now entries
        mNotNowRemaining = (prefs.getInt(NOT_NOW_ENTRIES_REMAINING, TOTAL_NOT_NOW_ENTRIES)) - 1;
        editor.putInt(NOT_NOW_ENTRIES_REMAINING, mNotNowRemaining);

        editor.commit();
    }

    public boolean doDisplayDialog(SharedPreferences prefs, boolean fromDrawer) {

        if (fromDrawer) {
            return true;
        }

        if (userDonated(prefs)) {
            return false;
        }

        if (freeUsesRemaining(prefs)) {
            return false;
        }

        if (neverClickedRecently(prefs)) {
            return false;
        }

        if (notNowClickedRecently(prefs)) {
            return false;
        }

        return true;
    }

    /**
     * @return true if the user has donated
     */
    private boolean userDonated(SharedPreferences prefs) {
        boolean donated = prefs.getBoolean(USER_DONATED, false);
        return donated;
    }

    private boolean freeUsesRemaining(SharedPreferences prefs) {
        DateTime today = new DateTime();
        DateTime freeDate = DateTime.parse(prefs.getString(FREE_DATE,
                today.plusDays(TOTAL_FREE_DAYS).toString()));
        // there are still uses remaining if not yet "freeDate"
        if ((Seconds.secondsBetween(freeDate, today)).getSeconds() < 0) {
            return true;
        }
        return (prefs.getInt(NEVER_ENTRIES_REMAINING, TOTAL_NEVER_DAYS) > 0);
    }

    /**
     * @return true if "never" has been clicked recently
     */
    private boolean neverClickedRecently(SharedPreferences prefs) {
        // only need to check if recent if never was clicked
        if (prefs.getBoolean(CLICKED_NEVER, false)) {
            DateTime today = new DateTime();
            // if have a saved date to show popup use that
            // by default set date to show popup for total days from now
            DateTime neverClickedDate = DateTime.parse(prefs.getString(NEVER_CLICKED_DATE,
                    today.plusDays(TOTAL_NEVER_DAYS).toString()));

            // get entries remaining
            int entriesRemaining = prefs.getInt(NEVER_ENTRIES_REMAINING, TOTAL_NEVER_DAYS);

            // if there are entries remaining return true
            if (entriesRemaining > 0) {
                return true;
            }
            // find the seconds between future - today
            double timeRemaining = Seconds.secondsBetween(today, neverClickedDate).getSeconds();

            // return if click was recent
            return (timeRemaining > 0);
        }
        return false;
    }

    /**
     * @return true if "not now" has been clicked recently
     */
    private boolean notNowClickedRecently(SharedPreferences prefs) {
        // check if "not now" has been clicked at all
        if (prefs.getBoolean(CLICKED_NOT_NOW, false)) {
            DateTime today = new DateTime();
            // if have a saved date to show popup use that
            // by default set date to show popup for total days from now
            DateTime notNowClickedDate = DateTime.parse(prefs.getString(NOT_NOW_CLICKED_DATE,
                    today.plusDays(TOTAL_NOT_NOW_DAYS).toString()));
            // get entries remaining
            int entriesRemaining = prefs.getInt(NOT_NOW_ENTRIES_REMAINING, TOTAL_NOT_NOW_DAYS);

            // if there are entries remaining return true
            if (entriesRemaining > 0) {
                return true;
            }

            // find the seconds between future - today
            double timeRemaining = Seconds.secondsBetween(today, notNowClickedDate).getSeconds();

            // return if click was recent
            return (timeRemaining > 0);
        }
        return false;
    }

    private View getCustomView() {
        mInflater = getActivity().getLayoutInflater();
        View v = mInflater.inflate(R.layout.fragment_donate_dialog, null);
        final EditText donationAmountET = (EditText) v.findViewById(R.id.donation_amount);

        donationAmountET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    donate(donationAmountET);
                    handled = true;
                }
                return handled;
            }
        });

        // add click listener to the paypal donate button
        ImageView paypal = (ImageView) v.findViewById(R.id.btn_paypal);
        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donate(donationAmountET);
            }
        });
        return v;
    }

    private void donate(EditText donationAmountET) {
        String donationAmountStr = donationAmountET.getText().toString();
        if (donationAmountStr == null || donationAmountStr.length() == 0) {
            donationAmountStr = DEFAULT_DONATION;
        }
        donatePayPalOnClick(donationAmountStr);
    }

    private void donatePayPalOnClick(String donationAmountStr) {
        mPrefs.edit().putBoolean(DonateActivity.BEFORE_DONATE, true).commit();
        Intent intent = new Intent(getActivity(), DonateActivity.class);
        intent.putExtra(DonateActivity.DONATE_AMOUNT, donationAmountStr);
        startActivity(intent);
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
        } else {
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });
        }
        return (builder.create());
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

    private Dialog customizeButtons(Dialog alertDialog) {

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setBackgroundResource(R.drawable.button_custom);
                positiveButton.setTextColor(getResources().getColor(R.color.custom_theme_color));

                Button negativeButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setBackgroundResource(R.drawable.button_custom);
                negativeButton.setTextColor(getResources().getColor(R.color.custom_theme_color));
            }
        });
        return alertDialog;
    }
}