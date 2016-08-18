package com.capstone.cudaf.ultratravel.view;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.capstone.cudaf.ultratravel.R;
import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.model.BusinessType;
import com.capstone.cudaf.ultratravel.model.Hotel;
import com.capstone.cudaf.ultratravel.model.Location;
import com.capstone.cudaf.ultratravel.model.Museum;
import com.capstone.cudaf.ultratravel.model.Restaurant;
import com.capstone.cudaf.ultratravel.utils.SQLToObjectHelper;
import com.capstone.cudaf.ultratravel.view.adapter.BusinessAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class FavouriteListActivity extends BusinessListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 0x01;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void initViews() {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_dimension));
        mRecyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(
                        this,
                        Uri.parse("content://com.capstone.cudaf.ultratravel.contentprovider/favourites"),
                        null, null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mBusiness = new ArrayList<>();
        data.moveToFirst();
        while (!data.isAfterLast()) {
            mBusiness.add(SQLToObjectHelper.cursorToBusiness(data));
            data.moveToNext();
        }
        mBusinessAdapter = new BusinessAdapter(FavouriteListActivity.this,
                mBusiness, FavouriteListActivity.this);
        mRecyclerView.setAdapter(mBusinessAdapter);
        mBusinessAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
