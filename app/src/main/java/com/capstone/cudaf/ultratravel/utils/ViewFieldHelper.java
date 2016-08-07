package com.capstone.cudaf.ultratravel.utils;


import com.capstone.cudaf.ultratravel.model.Location;

import java.util.List;

public class ViewFieldHelper {

    public static String generateCategory(List<List<String>> categories) {
        StringBuilder category = new StringBuilder();
        if (categories != null && !categories.isEmpty()) {
            for (List<String> catList : categories) {
                if (category.length() > 0) {
                    category.append(" > ");
                }
                category.append(catList.get(0));
            }
        }
        return category.toString();
    }

    public static String createAddress(Location location) {
        StringBuilder builder = new StringBuilder();
        if (location.getDisplay_address() != null && location.getDisplay_address().size() > 0) {
            builder.append(location.getDisplay_address().get(0)).append(", ");
        }
        builder.append(location.getCity());
        return builder.toString();
    }
}
