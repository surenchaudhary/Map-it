package com.wildnettechnologies.mapit.mapit.util;


import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.routing.SKRouteSettings;


public class DemoUtils {


    public static boolean hasGpsModule(final Context context) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        for (final String provider : locationManager.getAllProviders()) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasNetworkModule(final Context context) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        for (final String provider : locationManager.getAllProviders()) {
            if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                return true;
            }
        }
        return false;
    }

    public static SKRouteSettings launchRouteCalculation(double destinationLatitude, double destinationLongitude, double currentLatitude, double currentlongitude) {
        // get a route settings object and populate it with the desired properties
        SKRouteSettings route = new SKRouteSettings();
        // set start and destination points
        route.setStartCoordinate(new SKCoordinate(currentlongitude, currentLatitude));


        route.setDestinationCoordinate(new SKCoordinate(destinationLongitude, destinationLatitude));
        // set the number of routes to be calculated
        route.setNoOfRoutes(3);
        // set the route mode
        route.setRouteMode(SKRouteSettings.SKRouteMode.CAR_SHORTEST);
        // set whether the route should be shown on the map after it's computed
        route.setRouteExposed(true);
        // set the route listener to be notified of route calculation
        // events
        return route;
    }
}