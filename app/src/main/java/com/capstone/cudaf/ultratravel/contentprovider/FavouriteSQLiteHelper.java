package com.capstone.cudaf.ultratravel.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to create the db for favourites
 */
public class FavouriteSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_FAVOURITE = "favourites";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FAVOURITE_NAME = "name";
    public static final String COLUMN_FAVOURITE_RATINGS = "ratings";
    public static final String COLUMN_FAVOURITE_LOCATION_ADDRESS = "address";
    public static final String COLUMN_FAVOURITE_LOCATION_SHORT_ADDRESS = "shortAddress";
    public static final String COLUMN_FAVOURITE_LOCATION_CITY = "city";
    public static final String COLUMN_FAVOURITE_CATEGORY = "category";
    public static final String COLUMN_FAVOURITE_PHONE = "phone";
    public static final String COLUMN_FAVOURITE_IMAGE = "image";
    public static final String COLUMN_FAVOURITE_MOBILE_URL = "mobileUrl";
    public static final String COLUMN_FAVOURITE_TYPE = "type";


    private static final String DATABASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_FAVOURITE + "( " + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_FAVOURITE_NAME + " text not null, "
            + COLUMN_FAVOURITE_RATINGS + " text not null, "
            + COLUMN_FAVOURITE_LOCATION_ADDRESS + " text not null, "
            + COLUMN_FAVOURITE_LOCATION_SHORT_ADDRESS + " text not null, "
            + COLUMN_FAVOURITE_LOCATION_CITY + " text not null, "
            + COLUMN_FAVOURITE_CATEGORY + " text not null, "
            + COLUMN_FAVOURITE_PHONE + " text not null, "
            + COLUMN_FAVOURITE_IMAGE + " text not null, "
            + COLUMN_FAVOURITE_MOBILE_URL + " text not null, "
            + COLUMN_FAVOURITE_TYPE + " text not null "
            + ");";

    public FavouriteSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE);
        onCreate(db);
    }

}

