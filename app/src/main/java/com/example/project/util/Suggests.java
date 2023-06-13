package com.example.project.util;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.search.SuggestItem;

public class Suggests {
    private String GeoName;
    private String Address;
    private String GeoUri;
    private SuggestItem.Type ItemType;
    private Point CenterPoint;

    public Suggests(String geoName, String address, String geoUri, SuggestItem.Type itemType, Point centerPoint) {
        GeoName = geoName;
        Address = address;
        GeoUri = geoUri;
        ItemType = itemType;
        CenterPoint = centerPoint;
    }

    public String getGeoName() {
        return GeoName;
    }

    public void setGeoName(String geoName) {
        GeoName = geoName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGeoUri() {
        return GeoUri;
    }

    public void setGeoUri(String geoUri) {
        GeoUri = geoUri;
    }

    public SuggestItem.Type getItemType() {
        return ItemType;
    }

    public void setItemType(SuggestItem.Type itemType) {
        ItemType = itemType;
    }

    public Point getCenterPoint() {
        return CenterPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        CenterPoint = centerPoint;
    }
}
