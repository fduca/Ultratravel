package com.capstone.cudaf.ultratravel.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.model.BusinessType;
import com.capstone.cudaf.ultratravel.model.Hotel;
import com.capstone.cudaf.ultratravel.model.Location;
import com.capstone.cudaf.ultratravel.model.Museum;
import com.capstone.cudaf.ultratravel.model.Restaurant;
import com.capstone.cudaf.ultratravel.utils.ViewFieldHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavouriteDataSource {
    // Database fields
    private SQLiteDatabase database;
    private FavouriteSQLiteHelper dbHelper;
    private String[] allColumns = {FavouriteSQLiteHelper.COLUMN_ID,
            FavouriteSQLiteHelper.COLUMN_FAVOURITE_NAME,
            FavouriteSQLiteHelper.COLUMN_FAVOURITE_RATINGS,
            FavouriteSQLiteHelper.COLUMN_FAVOURITE_LOCATION_ADDRESS,
            FavouriteSQLiteHelper.COLUMN_FAVOURITE_LOCATION_SHORT_ADDRESS,
            FavouriteSQLiteHelper.COLUMN_FAVOURITE_LOCATION_CITY,
            FavouriteSQLiteHelper.COLUMN_FAVOURITE_CATEGORY,
            FavouriteSQLiteHelper.COLUMN_FAVOURITE_PHONE,
            FavouriteSQLiteHelper.COLUMN_FAVOURITE_IMAGE,
            FavouriteSQLiteHelper.COLUMN_FAVOURITE_MOBILE_URL,
            FavouriteSQLiteHelper.COLUMN_FAVOURITE_TYPE
    };

    public FavouriteDataSource(Context context) {
        dbHelper = new FavouriteSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Business createFavourite(Business business) {
        ContentValues values = new ContentValues();
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_NAME, business.getName());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_RATINGS, business.getRating());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_LOCATION_ADDRESS, business.getLocation().getDisplay_address().toString().replace("[", "").replace("]", ""));
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_LOCATION_SHORT_ADDRESS, business.getLocation().getDisplay_address().get(0));
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_LOCATION_CITY, business.getLocation().getCity());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_CATEGORY, ViewFieldHelper.generateCategory(business.getCategories()));
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_PHONE, business.getDisplay_phone());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_IMAGE, business.getImage_url());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_MOBILE_URL, business.getMobile_url());
        values.put(FavouriteSQLiteHelper.COLUMN_FAVOURITE_TYPE, business.getBusinessType().getBusinessType());

        long insertId = database.insert(FavouriteSQLiteHelper.TABLE_FAVOURITE, null,
                values);
        Cursor cursor = database.query(FavouriteSQLiteHelper.TABLE_FAVOURITE,
                allColumns, FavouriteSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Business newFavourite = cursorToBusiness(cursor);
        cursor.close();
        return newFavourite;
    }

    public boolean deleteFavourite(Business business) {
        long id = business.getId();
        int result = database.delete(FavouriteSQLiteHelper.TABLE_FAVOURITE, FavouriteSQLiteHelper.COLUMN_ID
                + " = " + id, null);
        if (result>0){
            return true;
        } else {
            return false;
        }
    }

    public Business getFavouriteByNameAndCity(String name, String city) {
        Business business = null;

        Cursor cursor = database.query(FavouriteSQLiteHelper.TABLE_FAVOURITE,
                allColumns,
                FavouriteSQLiteHelper.COLUMN_FAVOURITE_NAME+" like ? AND "+FavouriteSQLiteHelper.COLUMN_FAVOURITE_LOCATION_CITY+"= ?",
                new String[]{name+"%", city},
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            business = cursorToBusiness(cursor);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return business;
    }

    public List<Business> getAllFavourites() {
        List<Business> businessList = new ArrayList<Business>();

        Cursor cursor = database.query(FavouriteSQLiteHelper.TABLE_FAVOURITE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Business business = cursorToBusiness(cursor);
            businessList.add(business);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return businessList;
    }

    private Business cursorToBusiness(Cursor cursor) {
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String rating = cursor.getString(2);
        String locationAddress = cursor.getString(3);
        String locationShortAddress = cursor.getString(4);
        String remainingAddress = locationAddress.replace(locationShortAddress, "");
        String city = cursor.getString(5);
        String category = cursor.getString(6);
        String phone = cursor.getString(7);
        String imageUrl = cursor.getString(8);
        String mobileUrl = cursor.getString(9);
        String type = cursor.getString(10);
        Business business = createBusiness(BusinessType.getBusinessTypeFromString(type));
        business.setId(id);
        business.setName(name);
        business.setRating(rating);
        Location location = new Location();
        location.setDisplay_address(Arrays.asList(locationShortAddress, remainingAddress));
        location.setCity(city);
        business.setLocation(location);
        business.setCategories(Arrays.asList(Arrays.asList(category)));
        business.setDisplay_phone(phone);
        business.setImage_url(imageUrl);
        business.setMobile_url(mobileUrl);
        return business;
    }

    private Business createBusiness(BusinessType businessType) {
        switch (businessType) {
            case RESTAURANT:
                return new Restaurant();
            case HOTELS:
                return new Hotel();
            case MUSEUMS:
                return new Museum();
            default:
                return new Business();
        }
    }
}

