package com.theshmittahapp.android.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

public class Advertisement {

    public static void setAd (final Context context, ImageView ad, final String url) {
        // no ad in landscape so check for null first
        if (ad!=null) {
            ad.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    Uri webpage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    context.startActivity(Intent.createChooser(intent, ""));
                }
            });
        }
    }
}
