package com.capstone.cudaf.ultratravel.contentprovider;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.capstone.cudaf.ultratravel.view.listener.OnSQLDataChangedListener;

public class UltratravelContentObserver extends ContentObserver {

    private OnSQLDataChangedListener mListener;

    public UltratravelContentObserver(Handler handler, OnSQLDataChangedListener listener) {
        super(handler);
        mListener = listener;
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        mListener.updateUI(uri);
    }
}
