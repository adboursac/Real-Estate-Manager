package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.BuildConfig;

import java.io.IOException;
import java.util.List;

public class MapHelper {

    public static final int DEFAULT_ZOOM = 15;
    public static final int DEFAULT_SIZE = 200;

    /**
     * Generate google static map url from given address
     *
     * @param address address
     * @param context context
     * @return url link for static map
     */
    public static String addressToStaticMapUrl(String address, Context context) {
        LatLng latLng = addressToFirstLatLng(address, context);
        if (latLng == null) return null;
        return "https://maps.googleapis.com/maps/api/staticmap?"+
                "center=" + latLng.latitude + "," + latLng.longitude +
                "&zoom="+ DEFAULT_ZOOM +
                "&size=" + DEFAULT_SIZE + "x" + DEFAULT_SIZE +
                "&markers=color:red%7C" + latLng.latitude + "," + latLng.longitude +
                "&sensor=false" +
                "&key=" + BuildConfig.google_maps_api_key;
    }

    /**
     * Return first location matching given address using a Geocoder query
     * @param address String address
     * @param context context
     * @return LatLng object matching the given address
     */
    public static LatLng addressToFirstLatLng(String address, Context context) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        Address location;
        try {
            addresses = geocoder.getFromLocationName(address, 10);
            location = addresses.get(0);
            return new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
