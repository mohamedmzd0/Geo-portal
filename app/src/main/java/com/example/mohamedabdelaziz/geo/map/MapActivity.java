package com.example.mohamedabdelaziz.geo.map;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1001;
    boolean visble = true;
    final String ApiKey = "AIzaSyA1aZxdzLDWfdW49DGBikKgBfc0pLKj1vw";
    private GoogleMap mMap;
    private FloatingActionButton mFab_myLocation;
    private LocationManager locationManager;
    private boolean accessLocation = false;
    private PlaceAutocompleteFragment mAutocompleteFragment;
    private CardView searchLayout;
    private FloatingActionButton mActionButton_search_direction_fab;
    private EditText origin, destination;
    private CardView two_search_container_layout;
    private ImageButton mImageButton_back;
    private LinearLayout mLinearLayout_route_detail_layout;
    private TextView mTextView_distance, mTextView_time;
    private LinearLayout route_detail_layout;
    private boolean getRouteData = false;
    boolean alreadyDrawon = false;
    boolean activityCreated = false;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        searchLayout = findViewById(R.id.searchLayout);
        mLinearLayout_route_detail_layout = findViewById(R.id.route_detail_layout);
        mTextView_distance = findViewById(R.id.tvDistance);
        mTextView_time = findViewById(R.id.tvTime);
        route_detail_layout = findViewById(R.id.route_detail_layout);
        mAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Location l = new Location("");
                l.setLatitude(place.getLatLng().latitude);
                l.setLongitude(place.getLatLng().longitude);
                markLocation(l);
            }

            @Override
            public void onError(Status status) {

            }


        });

        mImageButton_back = findViewById(R.id.btn_back);
        mImageButton_back.setOnClickListener(this);
        ImageButton swap = findViewById(R.id.swapBtn);
        swap.setOnClickListener(this);
        two_search_container_layout = findViewById(R.id.two_search_container_layout);
        mActionButton_search_direction_fab = findViewById(R.id.search_direction_fab);
        two_search_container_layout.animate().setDuration(100).translationY(-250);
        mActionButton_search_direction_fab.setOnClickListener(this);
        origin = findViewById(R.id.origin);
        destination = findViewById(R.id.destination);

        destination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (TextUtils.isEmpty(origin.getText()))
                        origin.setError("required");
                    else if (TextUtils.isEmpty(origin.getText())) {
                        destination.setError("required");
                    } else {
                        searchLocation(origin.getText().toString(), destination.getText().toString());
                    }
                }
                return false;
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFab_myLocation = findViewById(R.id.myLocation_BTN);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFab_myLocation.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (alreadyDrawon || !activityCreated)
            return;
        showMarker();
    }

    private void showMarker() {
        if (getIntent() != null) {
            if (mMap != null) {
                final LatLng originlat = new LatLng(Double.parseDouble(getIntent().getStringExtra(Constants.LAT)), Double.parseDouble((getIntent().getStringExtra(Constants.LONG))));
                excuteLocation(originlat);
            } else {
                Toast.makeText(this, "wait please", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMarker();
                    }
                }, 1000);
            }

        } else

        {
            LatLng latLng = new LatLng(30.194221, 31.542919);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Egypt , cairo"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        alreadyDrawon = true;
    }

    private void excuteLocation(LatLng originlat) {
        activityCreated = true;
        if (accessLocation) {
            Location l = mMap.getMyLocation();
            if (l == null) {
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, false);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                l = locationManager.getLastKnownLocation(provider);
                if (l != null) {
                    mMap.clear();
                    LatLng dest = new LatLng(l.getLatitude(), l.getLongitude());
                    excuteLocation(originlat, dest);
                } else {
                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        turnGPSOn();
                        alreadyDrawon = false;
                    } else if (!isNetworkConnected()) {
                        Toast.makeText(getApplicationContext(), "connect to internet", Toast.LENGTH_LONG).show();
                    } else
                        alreadyDrawon = false;
                }
            } else {
                mMap.clear();
                LatLng dest = new LatLng(l.getLatitude(), l.getLongitude());
                excuteLocation(originlat, dest);

            }
        } else
            CheckPermission();
    }


    private void searchLocation(String origin, String destination) {
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> address1 = null;
        List<Address> address2 = null;
        try {
            address1 = geocoder.getFromLocationName(origin, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            address2 = geocoder.getFromLocationName(destination, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address1 != null && address1.size() > 0 && address2 != null && address2.size() > 0) {
            LatLng originlat = new LatLng(address1.get(0).getLatitude(), address1.get(0).getLongitude());
            LatLng destinationlat = new LatLng(address2.get(0).getLatitude(), address2.get(0).getLongitude());
            excuteLocation(originlat, destinationlat);
        }
    }

    private void excuteLocation(final LatLng origin, final LatLng destination) {
        Toast.makeText(this, "drawaing route", Toast.LENGTH_SHORT).show();
        mMap.clear();
        GoogleDirection.withServerKey(ApiKey)
                .from(origin)
                .to(destination)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            mMap.addMarker(new MarkerOptions().position(origin)).setTitle(route.getLegList().get(0).getStartAddress().toString());
                            mMap.addMarker(new MarkerOptions().position(destination)).setTitle(route.getLegList().get(0).getEndAddress().toString());
                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            mMap.addPolyline(DirectionConverter.createPolyline(getApplicationContext(), directionPositionList, 2, Color.RED));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(origin));
                            mLinearLayout_route_detail_layout.animate().setDuration(100).translationY(0);
                            mTextView_time.setText(route.getLegList().get(0).getDuration().getText().toString());
                            mTextView_distance.setText(route.getLegList().get(0).getDistance().getText().toString());
                            two_search_container_layout.animate().setDuration(100).translationY(-250);
                            getRouteData = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "connection error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(getApplicationContext(), "internet connection problem", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void showSearch() {
        if (getRouteData)
            route_detail_layout.animate().setDuration(100).translationY(0);
        searchLayout.animate().setDuration(100).translationY(0);
    }

    private void hideSearch() {
        if (route_detail_layout.getTranslationY() == 0)
            route_detail_layout.animate().setDuration(100).translationY(500);
        searchLayout.animate().setDuration(100).translationY(-(16 + searchLayout.getHeight()));
    }

    private void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showAllowDialog();
            } else {
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            accessLocation = true;
        }
    }

    private void showAllowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("Allow  Access To Location");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void myLocationPressed() {
        if (accessLocation) {
            Location l = mMap.getMyLocation();
            if (l == null) {
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, false);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                l = locationManager.getLastKnownLocation(provider);
                if (l != null) {
                    markLocation(l);
                } else {
                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                        turnGPSOn();
                    else if (!isNetworkConnected())
                        Toast.makeText(getApplicationContext(), "connect to internet", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "can't access at this time try later", Toast.LENGTH_SHORT).show();
                }
            } else {
                mMap.clear();
                markLocation(l);
            }
        } else
            CheckPermission();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void markLocation(Location l) {
        if (mMap == null)
            return;
        mMap.clear();
        LatLng sydney = new LatLng(l.getLatitude(), l.getLongitude());
        Geocoder geocoder = new Geocoder(MapActivity.this, getResources().getConfiguration().locale);
        String title = "not availble";
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null && addressList.size() > 0)
            title = addressList.get(0).getCountryName() + " " + addressList.get(0).getAdminArea() +
                    " " + addressList.get(0).getSubAdminArea() + " " + addressList.get(0).getFeatureName();
        mMap.addMarker(new MarkerOptions().position(sydney).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void turnGPSOn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle("Location services not active");
        builder.setMessage("Please enable Location Services");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        mMap.getMyLocation();
                        accessLocation = true;
                    }
                }
                return;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "getting your map ready", Toast.LENGTH_SHORT).show();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMinZoomPreference(10);
        LatLng latLng = new LatLng(30.194221, 31.542919);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Egypt , cairo"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (alreadyDrawon)
                    return;
                if (getIntent() != null && mMap != null) {
                    final LatLng originlat = new LatLng(Double.parseDouble(getIntent().getStringExtra(Constants.LAT)), Double.parseDouble((getIntent().getStringExtra(Constants.LONG))));
                    excuteLocation(originlat);
                } else {
                    LatLng latLng = new LatLng(30.194221, 31.542919);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Egypt , cairo"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
                alreadyDrawon = true;
            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (visble) {
                    hideSearch();
                } else
                    showSearch();
                visble = !visble;
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Location l = new Location("");
                l.setLatitude(latLng.latitude);
                l.setLongitude(latLng.longitude);
                markLocation(l);
            }
        });
        CheckPermission();
    }


    public void swapEditext() {
        String temp = "";
        if (!TextUtils.isEmpty(origin.getText().toString()))
            temp = origin.getText().toString();
        if (!TextUtils.isEmpty(destination.getText().toString()))
            origin.setText(destination.getText().toString());
        else
            origin.setText("");
        destination.setText(temp);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.swapBtn)
            swapEditext();
        else if (v.getId() == R.id.search_direction_fab) {
            if (two_search_container_layout.getTranslationY() != 0)
                two_search_container_layout.animate().setDuration(500).translationY(0);
            else {
                two_search_container_layout.animate().setDuration(500).translationY(-250);
            }
        } else if (v.getId() == R.id.myLocation_BTN) {
            myLocationPressed();
        } else if (v.getId() == R.id.btn_back) {
            two_search_container_layout.animate().setDuration(500).translationY(-250);
        }
    }


}
