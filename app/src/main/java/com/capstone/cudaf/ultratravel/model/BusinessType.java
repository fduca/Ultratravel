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

    public static BusinessType getBusinessTypeFromString(String type){
        for (BusinessType business: BusinessType.values()){
            if (business.getBusinessType().equals(type)){
                return business;
            }
        }
        return null;
    }

}
