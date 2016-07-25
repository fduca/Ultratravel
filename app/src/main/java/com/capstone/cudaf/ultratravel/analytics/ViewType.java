package com.capstone.cudaf.ultratravel.analytics;

public enum ViewType {
    HOME_SCREEN("HOME_SCREEN"),
    CITY_DETAIL("CITY_DETAIL"),
    BUSINESS_LIST_VIEW("BUSINESS_LIST_VIEW"),
    BUSINESS_MAP_VIEW("BUSINESS_MAP_VIEW"),
    BUSINESS_DETAIL_VIEW("BUSINESS_DETAIL_VIEW");

    private final String viewType;

    ViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getViewType() {
        return this.viewType;
    }

}