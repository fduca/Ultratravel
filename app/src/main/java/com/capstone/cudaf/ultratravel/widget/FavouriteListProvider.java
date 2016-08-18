package com.capstone.cudaf.ultratravel.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.utils.SQLToObjectHelper;
import com.capstone.cudaf.ultratravel.view.BusinessActivity;

import java.util.ArrayList;
import java.util.List;


public class FavouriteListProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context ctxt = null;
    private int appWidgetId;
    List<Business> mBusiness = new ArrayList<>();

    private static final int LOADER_ID = 0x03;

    public FavouriteListProvider(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return (mBusiness.size());
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(ctxt.getPackageName(),
                R.layout.list_item);

        row.setTextViewText(android.R.id.text1, mBusiness.get(position).getName());

        Intent i = new Intent();
        i.putExtra(BusinessActivity.BUSINESS_PARAM, mBusiness.get(position));
        row.setOnClickFillInIntent(android.R.id.text1, i);
        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return (null);
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public boolean hasStableIds() {
        return (true);
    }

    @Override
    public void onDataSetChanged() {
        // Revert back to our process' identity so we can work with our content provider
        long identityToken = Binder.clearCallingIdentity();
        try {
            // Query the message list
            Cursor listCursor = ctxt.getContentResolver().query(Uri.parse("content://com.capstone.cudaf.ultratravel.contentprovider/favourites"),
                    null, null, null, null);
            if (listCursor != null) {
                try {
                    listCursor.moveToFirst();
                    while (!listCursor.isAfterLast()) {
                        mBusiness.add(SQLToObjectHelper.cursorToBusiness(listCursor));
                        listCursor.moveToNext();
                    }
                } finally {
                    listCursor.close();
                }
            }
        } finally {
            // Restore the identity - not sure if it's needed since we're going to return right here, but
            // it just *seems* cleaner
            Binder.restoreCallingIdentity(identityToken);
        }

    }

}