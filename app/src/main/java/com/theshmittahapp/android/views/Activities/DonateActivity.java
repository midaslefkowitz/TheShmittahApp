//TODO: Thank you page after donations in onactivityresult

package com.theshmittahapp.android.views.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.views.Fragments.DonateDialogFragment;

import java.math.BigDecimal;

public class DonateActivity extends Activity {

    public static final String DONATE_AMOUNT = "donate amount";
    private static final String TAG = "Donate Activity";
    private boolean beforeDonate = true;

    /* Paypal constants */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static final String LIVE_ID = "AUZQMRAYcAGeLPQDw0WBp-SQFP1tO0e7jyJFXGb4oBA196hMeF5U-To5dLvz";
    private static final String SANDBOX_ID = "AQjvuBDUyBRHOUYUIzLOYxtXc_Mc6iHTa8vit70lzUp2F1PPZRsAXncV3IYG";
    private static final String CONFIG_CLIENT_ID = LIVE_ID;
    public static final int REQUEST_CODE_PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);

    private String mPaypalCurrencyCode = "USD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        startPaypalService();
        if (beforeDonate) {
            String donateAmountStr = getIntent().getStringExtra(DONATE_AMOUNT);
            beforeDonate = false;
            donate(donateAmountStr);
        }
    }

    private void startPaypalService() {
        try {
            Intent intent = new Intent(this, PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            startService(intent);
        } catch (Exception e) {
            Log.i("PayPal Exception", e.getMessage());
        }
    }

    public void donate(String donationAmount) {

        PayPalPayment donation = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE, donationAmount);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, donation);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent, String donationAmount) {
        return new PayPalPayment(
                new BigDecimal(donationAmount),
                mPaypalCurrencyCode,
                getResources().getString(R.string.donation_description),
                paymentIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        beforeDonate = true;
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putBoolean(DonateDialogFragment.USER_DONATED, true).commit();
                // TODO: Send to new activity with thank you page
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        try {
            stopService(new Intent(this, PayPalService.class));
        } catch (Exception e) {
            Log.i("PayPal Exception", e.getMessage());
        }
        super.onDestroy();
    }
}
