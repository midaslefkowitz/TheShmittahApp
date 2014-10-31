package com.theshmittahapp.android.models;

import android.content.Context;

public class ChartSave extends SaveItem {

    public ChartSave (Context context) {
        super(context);
    }

    @Override
    public String getEmailBody() {
        StringBuilder body  = new StringBuilder();
        body.append("Here is a link to the Chart found in The Shmittah App. Just click to open it or right-click and \"Save link as...\" to download them.");
        body.append('\n');
        body.append("<a href=\"https://s3.eu-central-1.amazonaws.com/shmittahappfiles/shmittah_chart.jpg\">Shmittah Chart (JPG)</a>");
        body.append("</ul></br>");
        body.append('\n');
        body.append("</br>");
        body.append('\n');
        body.append( "Thanks for using <a href=\"http://www.TheShmittahApp.com\">The Shmittah App</a>");

        return body.toString();
    }

    @Override
    public String getEmailSubject() {
        return "Chart from The Shmittah App (JPG)";
    }

}
