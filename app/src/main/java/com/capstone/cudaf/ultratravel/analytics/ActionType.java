package com.capstone.cudaf.ultratravel.analytics;

public enum ActionType {
    CLICK_CONTACT("CLICK_CONTACT"),
    CLICK_RESERVATION("CLICK_RESERVATION"),
    CLICK_CITY("CLICK_CITY"),
    CLICK_CITY_MORE("CLICK_CITY_MORE"),
    CLICK_BUSINESS_LIST("CLICK_BUSINESS_LIST"),
    CLICK_MAP_VIEW("CLICK_MAP_VIEW"),
    CLICK_BUSINESS_ITEM("CLICK_BUSINESS_ITEM");

    private final String actionType;

    ActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return this.actionType;
    }

}