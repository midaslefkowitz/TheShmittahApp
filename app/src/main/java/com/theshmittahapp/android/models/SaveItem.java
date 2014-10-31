package com.theshmittahapp.android.models;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.theshmittahapp.android.R;

public class SaveItem extends ActionProvider {

    private String mSubject;

    private String mBody;
    private Context mContext;

    public SaveItem(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateActionView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.save_action_menu_layout,null);
        mSubject = getEmailSubject();
        mBody = getEmailBody();
        ImageButton button = (ImageButton) view.findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFile();
            }
        });
        return view;
    }

    @Override
    public boolean onPerformDefaultAction() {
        sendFile();
        return true;
    }

    private void sendFile() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/html");
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mBody));
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(Intent.createChooser(intent, ""));
        }

    }

    protected String getEmailSubject() {
        return "Resources from the Shmittah App";
    }

    protected String getEmailBody() {
        StringBuilder body  = new StringBuilder();
        body.append("Here are links to the resources used in The Shmittah App. Just click to open them or right-click and \"Save link as...\" to download them.");
        body.append("<ul style=\"list-style-type:none;\"></br>");
        body.append('\n');
        body.append("<li><a href=\"https://s3.eu-central-1.amazonaws.com/shmittahappfiles/Brief+Overview+of+Halachos+of+Shmittah.pdf\">Brief Overview of the Halachos of Shmittah (PDF)</a></li>");
        body.append("<li><a href=\"https://s3.eu-central-1.amazonaws.com/shmittahappfiles/Detailed+Halachos+of+Shmittah.pdf\">Detailed Halachos of Shmittah (PDF)</a></li>");
        body.append("<li><a href=\"https://s3.eu-central-1.amazonaws.com/shmittahappfiles/shmittah_chart.jpg\">Shmittah Chart (JPG)</a></li>");
        body.append("</ul></br>");
        body.append('\n');
        body.append("</br>");
        body.append('\n');
        body.append( "Thanks for using <a href=\"http://www.TheShmittahApp.com\">The Shmittah App</a>");

        return body.toString();
    }
}
