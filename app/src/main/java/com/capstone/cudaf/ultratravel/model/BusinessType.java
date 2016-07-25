package com.capstone.cudaf.ultratravel.model;

public enum BusinessType {
    RESTAURANT("Restaurants"),
    HOTELS("Hotels"),
    MUSEUMS("Museums");
    private final String businessType;

    BusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessType() {
        return this.businessType;
    }

}
