package com.capstone.cudaf.ultratravel.deserializer;

import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.model.BusinessType;
import com.capstone.cudaf.ultratravel.model.Hotel;
import com.capstone.cudaf.ultratravel.model.Museum;
import com.capstone.cudaf.ultratravel.model.Restaurant;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BusinessDeserializer implements JsonDeserializer<Business> {
    BusinessType businessType;

    public BusinessDeserializer(BusinessType type) {
        this.businessType = type;
    }

    @Override
    public Business deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        switch (businessType) {
            case RESTAURANT:
                return context.deserialize(json, Restaurant.class);
            case HOTELS:
                return context.deserialize(json, Hotel.class);
            case MUSEUMS:
                return context.deserialize(json, Museum.class);
            default:
                throw new IllegalArgumentException("Can't deserialize ");
        }
    }
}
