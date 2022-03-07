package com.openclassrooms.realestatemanager.utils;


import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.openclassrooms.realestatemanager.MainApplication;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Provide application permissions status and requests
 */
public class PermissionHelper {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 767967;

    /**
     * Tells if fine location is permitted.
     * @return true is fine location is permitted, false instead.
     */
    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(MainApplication.getContext(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }

    /**
     * Request fine location.
     * @param activity from which permission is requested
     */
    public void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }
}
