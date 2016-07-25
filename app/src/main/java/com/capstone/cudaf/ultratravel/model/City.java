package com.capstone.cudaf.ultratravel.model;

import java.io.Serializable;

public class City implements Serializable {
    private String city;
    private String description;
    private String main_image;
    private String country;

    public City() {

    }

    public City(String city, String description, String main_image, String country) {
        this.city = city;
        this.description = description;
        this.main_image = main_image;
        this.country = country;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
