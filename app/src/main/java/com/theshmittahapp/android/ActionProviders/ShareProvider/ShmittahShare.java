package com.theshmittahapp.android.ActionProviders.ShareProvider;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ShareActionProvider;

import com.theshmittahapp.android.R;

import java.lang.reflect.Method;

public class ShmittahShare extends ShareActionProvider {

    private final Context mContext;

    public ShmittahShare(Context context)
    {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateActionView()
    {
        View view = super.onCreateActionView();
        if (view != null)
        {
            try
            {
                Drawable icon = mContext.getResources().getDrawable(R.drawable.action_share);
                Method method = view.getClass().getMethod("setExpandActivityOverflowButtonDrawable", Drawable.class);
                method.invoke(view, icon);
            }
            catch (Exception e)
            {
                Log.e("ShmittahShare", "onCreateActionView", e);
            }
        }

        return view;
    }
}
