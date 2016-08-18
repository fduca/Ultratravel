package com.capstone.cudaf.ultratravel.utils;

import android.database.Cursor;

import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.model.BusinessType;
import com.capstone.cudaf.ultratravel.model.Hotel;
import com.capstone.cudaf.ultratravel.model.Location;
import com.capstone.cudaf.ultratravel.model.Museum;
import com.capstone.cudaf.ultratravel.model.Restaurant;

import java.util.Arrays;

public class SQLToObjectHelper {

    public static  Business cursorToBusiness(Cursor cursor) {
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

    private static Business createBusiness(BusinessType businessType) {
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
