package com.theshmittahapp.android.views.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.theshmittahapp.android.R;
import com.theshmittahapp.android.views.Fragments.DonateDialogFragment;

import java.math.BigDecimal;

public class DonateActivity extends Activity {

    public static final String DONATE_AMOUNT = "donate amount";
    private static final String TAG = "Donate Activity";
    public static final String BEFORE_DONATE = "before donate";

    /* Paypal constants */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String LIVE_ID = "AUZQMRAYcAGeLPQDw0WBp-SQFP1tO0e7jyJFXGb4oBA196hMeF5U-To5dLvz";
    private static final String SANDBOX_ID = "AQjvuBDUyBRHOUYUIzLOYxtXc_Mc6iHTa8vit70lzUp2F1PPZRsAXncV3IYG";
    private static final String CONFIG_CLIENT_ID = SANDBOX_ID;
    public static final int REQUEST_CODE_PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);

    private static final String mPaypalCurrencyCode = "USD";

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        startPaypalService();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean beforeDonate = mPrefs.getBoolean(BEFORE_DONATE, true);
        Log.d(TAG, "onResume beforeDonate = " + beforeDonate);
        if (beforeDonate) {
            mPrefs.edit().putBoolean(BEFORE_DONATE, false).commit();
            final String donateAmountStr = getIntent().getStringExtra(DONATE_AMOUNT);
            donate(donateAmountStr);
        } else {
            mPrefs.edit().putBoolean(BEFORE_DONATE, false).commit();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void startPaypalService() {
        try {
            Log.d(TAG, "starting paypal service");
            Intent intent = new Intent(this, PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            startService(intent);
        } catch (Exception e) {
            Log.i("PayPal Exception", e.getMessage());
        }
    }

    public void donate(String donationAmount) {
        Log.d(TAG, "starting paypal activity");
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
        Log.d(TAG, "onActivityResult");
        String result = (resultCode==-1) ? "RESULT_OK" : "RESULT_CANCELED";
        Log.d(TAG, "onActivityResult: result = " + result);
        Intent intent = new Intent(this, MainActivity.class);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: going to Thank You Activity");
                SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                mPrefs.edit().putBoolean(DonateDialogFragment.USER_DONATED, true).commit();
                intent = new Intent(this, ThankYouActivity.class);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        Log.d(TAG, "Stopping paypal service");
        try {
            stopService(new Intent(this, PayPalService.class));
        } catch (Exception e) {
            Log.i("PayPal Exception", e.getMessage());
        }
        super.onDestroy();
    }
}
