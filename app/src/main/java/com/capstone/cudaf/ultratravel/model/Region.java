package com.capstone.cudaf.ultratravel.model;

import java.io.Serializable;

public class Region implements Serializable {

    private Coordinate center;

    public Coordinate getCenter() {
        return center;
    }

    public void setCenter(Coordinate center) {
        this.center = center;
    }
}
