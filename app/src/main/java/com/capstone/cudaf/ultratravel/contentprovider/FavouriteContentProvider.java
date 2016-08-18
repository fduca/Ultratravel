package com.capstone.cudaf.ultratravel.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.Nullable;


public class FavouriteContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.capstone.cudaf.ultratravel.contentprovider";
    private static final String BASE_PATH = "favourites";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    private static final int GET_ALL_FAVOURITES = 0;
    private static final int GET_ONE_FAVOURITES = 1;

    public static final String FAVOURITES_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.capstone.cudaf.ultratravel.Favourites";
    public static final String FAVOURITE_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.capstone.cudaf.ultratravel.Favourites";

    private FavouriteDataSource mFavouriteDataSource;

    private static final UriMatcher uriMatcher = getUriMatcher();

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, BASE_PATH, GET_ALL_FAVOURITES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/item", GET_ONE_FAVOURITES);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mFavouriteDataSource = new FavouriteDataSource(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case GET_ALL_FAVOURITES:
                return mFavouriteDataSource.getAllFavourites();
            case GET_ONE_FAVOURITES:
                return mFavouriteDataSource.getFavouriteByNameAndCity(selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case GET_ALL_FAVOURITES:
                return FAVOURITES_MIME_TYPE; // list
            case GET_ONE_FAVOURITES:
                return FAVOURITE_MIME_TYPE; // single item
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = mFavouriteDataSource.createFavourite(values);
        return getUriForId(id, uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int id = mFavouriteDataSource.deleteFavourite(selectionArgs[0]);
        Uri itemUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(itemUri, null);
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            getContext().
                    getContentResolver().
                    notifyChange(itemUri, null);
            return itemUri;
        }
        throw new SQLException(
                "Problem while changing into uri: " + uri);
    }
}