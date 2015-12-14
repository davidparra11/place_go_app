package com.placego.util;

/**
 * Created by juanes on 23/11/15.
 */

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Lugares")
public class PersonLocation extends ParseObject implements ClusterItem {

    LatLng mPosition;

    public PersonLocation() {
        super();
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public void setPosition(LatLng mPosition) {
        this.mPosition = mPosition;
    }


    public void setDescripcion(String value) {
        put("descripcion", value);
    }

    public String getDescripcion() {
        return getString("descripcion");
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("ubicacion");
    }

    public void setLocation(ParseGeoPoint value) {
        put("ubicacion", value);
    }

    public void setName(String value) {
        put("name", value);
    }

    public String getName() {
        return getString("name");
    }

    public ParseUser getUser() {
        return getParseUser("fk_user");
    }

    public void setUser(ParseUser value) {
        put("fk_user", value);
    }

    public static ParseQuery<PersonLocation> getQuery() {
        return ParseQuery.getQuery(PersonLocation.class);
    }

}
