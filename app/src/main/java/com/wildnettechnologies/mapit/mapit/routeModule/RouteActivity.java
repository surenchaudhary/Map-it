package com.wildnettechnologies.mapit.mapit.routeModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.map.SKAnimationSettings;
import com.skobbler.ngx.map.SKAnnotation;
import com.skobbler.ngx.map.SKCoordinateRegion;
import com.skobbler.ngx.map.SKMapCustomPOI;
import com.skobbler.ngx.map.SKMapPOI;
import com.skobbler.ngx.map.SKMapSettings;
import com.skobbler.ngx.map.SKMapSurfaceListener;
import com.skobbler.ngx.map.SKMapSurfaceView;
import com.skobbler.ngx.map.SKMapViewHolder;
import com.skobbler.ngx.map.SKPOICluster;
import com.skobbler.ngx.map.SKScreenPoint;
import com.skobbler.ngx.positioner.SKCurrentPositionListener;
import com.skobbler.ngx.positioner.SKCurrentPositionProvider;
import com.skobbler.ngx.positioner.SKPosition;
import com.skobbler.ngx.routing.SKRouteManager;
import com.skobbler.ngx.routing.SKRouteSettings;
import com.wildnettechnologies.mapit.mapit.R;
import com.wildnettechnologies.mapit.mapit.routeModule.adapter.InitSearchRowAdapter;
import com.wildnettechnologies.mapit.mapit.util.DemoUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouteActivity extends Activity implements SKMapSurfaceListener, SKCurrentPositionListener {

    private static final String TAG = RouteActivity.class.getName();
    private SKMapSurfaceView mapView;
    private SKPosition currentPosition;
    private SKCurrentPositionProvider currentPositionProvider;
    public static SKCoordinate currentCoordinates;
    private boolean navigationInProgress;
    private static boolean checkNavBar = false;


    @Bind(R.id.navigation_bar_icon)
    ImageView navBar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.chess_board_background)
    RelativeLayout chessBoardBackground;

    @Bind(R.id.view_group_map)
    SKMapViewHolder mapHolder;

    @Bind(R.id.edit_text_header)
    EditText beginSearchTextHeader;

    @Bind(R.id.current_location_icon)
    ImageView currentLocationIdentifier;

    @Bind(R.id.background_search_list_row_view)
    LinearLayout layoutRowView;

    @Bind(R.id.create_new_route_layout)
    LinearLayout layoutNewRoute;

    @Bind(R.id.marker)
    ImageView selectMarker;

    @Bind(R.id.text_view_present_loc)
    TextView textPresentLoc;

    @Bind(R.id.text_view_distance)
    TextView textPresentLocDistance;

    @Bind(R.id.text_view_desc)
    TextView textPresentLocDesc;

    @Bind(R.id.favourites_icon)
    ImageView favouriteStatusIcon;

    @Bind(R.id.listings_icon)
    ImageView listedStatusIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        ButterKnife.bind(this);

        mapHolder.setMapSurfaceListener(this);

        currentPositionProvider = new SKCurrentPositionProvider(this);
        currentPositionProvider.setCurrentPositionListener(this);
        currentPositionProvider.requestLocationUpdates(DemoUtils.hasGpsModule(this), DemoUtils.hasNetworkModule(this), false);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        if (InitSearchRowAdapter.selectedLocations == null || InitSearchRowAdapter.selectedLocations.size() == 0) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("MapItRealtorApp would like to use your current location.");
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (currentCoordinates != null && mapView != null) {
                        SKAnnotation annotation1 = new SKAnnotation(1);
                        annotation1.setLocation(currentCoordinates);
                        annotation1.setMininumZoomLevel(5);
                        annotation1.setAnnotationType(SKAnnotation.SK_ANNOTATION_TYPE_MARKER);
                        mapView.addAnnotation(annotation1, SKAnimationSettings.ANIMATION_NONE);
                    }
                }
            });

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert1 = alertDialog.create();
            alert1.show();

        } else {
            beginSearchTextHeader.setText("");
            beginSearchTextHeader.setText(InitSearchRowAdapter.selectedLocations.get(0));
        }

        beginSearchTextHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCoordinates != null) {
                    startActivity(new Intent(RouteActivity.this, InitialSearchActivity.class));
                    finish();
                }
            }
        });


        navBar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @OnClick(R.id.current_location_icon)
    void onClickingSearchEditView(final View view) {
        if (mapView != null && currentPosition != null) {
            currentCoordinates = new SKCoordinate(currentPosition.getCoordinate().getLongitude(), currentPosition.getCoordinate().getLatitude());
            mapView.setPositionAsCurrent(currentCoordinates, 0, true);
            mapView.setZoom(5);
        } else {
            Toast.makeText(RouteActivity.this, "sorry no postion available", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    @OnClick(R.id.create_new_route_layout)
    void onClickingNewRouteViewLayout(final View view) {
        startActivity(new Intent(RouteActivity.this, SearchActivity.class));
    }


    @Override
    public void onCurrentPositionUpdate(SKPosition skPosition) {
        if (skPosition != null) {
            this.currentPosition = skPosition;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapHolder.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapHolder.onPause();
    }

    @Override
    public void onActionPan() {

    }

    @Override
    public void onActionZoom() {

    }

    @Override
    public void onSurfaceCreated(SKMapViewHolder skMapViewHolder) {
        chessBoardBackground.setVisibility(View.GONE);
        mapView = mapHolder.getMapSurfaceView();

        if (!navigationInProgress) {
            mapView.getMapSettings().setFollowerMode(SKMapSettings.SKMapFollowerMode.NONE);
        }


        if (InitSearchRowAdapter.selectedLocations == null || InitSearchRowAdapter.selectedLocations.size() == 0) {
            if (this.currentPosition != null) {
                currentCoordinates = new SKCoordinate(this.currentPosition.getCoordinate().getLongitude(), this.currentPosition.getCoordinate().getLatitude());
                mapView.setPositionAsCurrent(currentCoordinates, 0, true);
                mapView.setZoom(5);

            } else {
            }
        } else if (currentCoordinates != null) {
            String address = InitSearchRowAdapter.selectedLocations.get(0);
            String latlong = getLocationFromAddress(address);
            if (latlong != null && !latlong.equalsIgnoreCase(null)) {
                String[] latLongArray = latlong.split(",");

                String CityName = InitSearchRowAdapter.selectedLocations.get(0).split(",")[0];
                if (latLongArray.length == 2) {
                    SKCoordinate selectedCoordinates = new SKCoordinate(Double.parseDouble(latLongArray[1]),
                            Double.parseDouble(latLongArray[0]));
                    mapView.setPositionAsCurrent(selectedCoordinates, 0, true);
                    mapView.setZoom(12);
                    textPresentLoc.setText(CityName);
                    mapHolder.setScaleViewEnabled(true);
                    mapHolder.setScaleViewPosition(0, 80, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM);

                    SKRouteSettings route = DemoUtils.launchRouteCalculation(selectedCoordinates.getLatitude(), selectedCoordinates.getLongitude(), this.currentCoordinates.getLatitude(), this.currentCoordinates.getLongitude());
                    SKRouteManager.getInstance().calculateRoute(route);

                    textPresentLocDesc.setText(getDescriptionFromAddress(address));

                    layoutRowView.setVisibility(View.VISIBLE);
                    layoutNewRoute.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(RouteActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        }

        mapView.getMapSettings().setCompassPosition(new SKScreenPoint(6, 70));  // right top corner
        mapView.getMapSettings().setCompassShown(true);
    }

    @NonNull
    private String getDescriptionFromAddress(String address) {
        final String searchRowMain = address.split(",")[0];

        String searchText = address.replace(searchRowMain, "");

        String[] strArraySearchResponse = searchText.split(",");

        int len = strArraySearchResponse.length;
        String searchResponseListDescription = "";
        for (int i = 1; i < len; i++)
            searchResponseListDescription += (strArraySearchResponse[i] + ", ");
        return searchResponseListDescription;
    }


    @Override
    public void onMapRegionChanged(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onMapRegionChangeStarted(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onMapRegionChangeEnded(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onDoubleTap(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onSingleTap(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onRotateMap() {

    }

    @Override
    public void onLongPress(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onInternetConnectionNeeded() {

    }

    @Override
    public void onMapActionDown(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onMapActionUp(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onPOIClusterSelected(SKPOICluster skpoiCluster) {

    }

    @Override
    public void onMapPOISelected(SKMapPOI skMapPOI) {

    }

    @Override
    public void onAnnotationSelected(SKAnnotation skAnnotation) {

    }

    @Override
    public void onCustomPOISelected(SKMapCustomPOI skMapCustomPOI) {

    }

    @Override
    public void onCompassSelected() {

    }

    @Override
    public void onCurrentPositionSelected() {

    }

    @Override
    public void onObjectSelected(int i) {

    }

    @Override
    public void onInternationalisationCalled(int i) {

    }

    @Override
    public void onBoundingBoxImageRendered(int i) {

    }

    @Override
    public void onGLInitializationError(String s) {

    }

    @Override
    public void onScreenshotReady(Bitmap bitmap) {

    }

    public String getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        String result = "";
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            result = location.getLatitude() + "," + location.getLongitude();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
