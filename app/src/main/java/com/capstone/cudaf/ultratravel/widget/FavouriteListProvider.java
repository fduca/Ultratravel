package com.capstone.cudaf.ultratravel.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.contentprovider.FavouriteDataSource;
import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.view.BusinessActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cudaf on 08/08/2016.
 */

public class FavouriteListProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context ctxt = null;
    private int appWidgetId;
    private FavouriteDataSource mFavouriteDataSource;
    List<Business> mBusiness;

    public FavouriteListProvider(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mFavouriteDataSource = new FavouriteDataSource(ctxt);
        mFavouriteDataSource.open();
        mBusiness = new ArrayList<>(mFavouriteDataSource.getAllFavourites());
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        mFavouriteDataSource.close();
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
        // no-op
    }
}