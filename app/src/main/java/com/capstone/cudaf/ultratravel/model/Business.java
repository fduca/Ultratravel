package com.capstone.cudaf.ultratravel.model;

import java.io.Serializable;
import java.util.List;

public class Business implements Serializable {

    private transient int id;
    private String name;
    private String rating;
    private Location location;
    private List<List<String>> categories;
    private String display_phone;
    private String image_url;
    private Boolean is_closed;
    private String mobile_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<List<String>> getCategories() {
        return categories;
    }

    public void setCategories(List<List<String>> categories) {
        this.categories = categories;
    }

    public String getDisplay_phone() {
        return display_phone;
    }

    public void setDisplay_phone(String display_phone) {
        this.display_phone = display_phone;
    }

    public String getImage_url() {
        return image_url.replace("/ms.jpg", "/ls.jpg");
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean is_closed() {
        return is_closed;
    }

    public void setIs_closed(boolean is_closed) {
        this.is_closed = is_closed;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public BusinessType getBusinessType() {
        return null;
    }

}
