package com.capstone.cudaf.ultratravel.model;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class YelpResponse {

    List<Business> businesses;
    Region region;

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
