package com.garrar.user.app.ui.activity.location_pick;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.garrar.user.app.MvpApplication;
import com.garrar.user.app.ui.adapter.PlacesAutoCompleteTextViewAdapter;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.Task;
import com.garrar.user.app.R;
import com.garrar.user.app.base.BaseActivity;
import com.garrar.user.app.common.Constants;
import com.garrar.user.app.data.network.model.AddressResponse;
import com.garrar.user.app.data.network.model.UserAddress;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_ADD;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_ADD0;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_ADD1;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_ADD2;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_LAT;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_LAT0;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_LAT1;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_LAT2;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_LONG;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_LONG0;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_LONG1;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_LONG2;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.SRC_ADD;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.SRC_LAT;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.SRC_LONG;

public class LocationPickActivity extends BaseActivity
        implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationPickIView {

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0));
    private Location mLastKnownLocation;
    protected GoogleApiClient mGoogleApiClient;

    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.source)
    AutoCompleteTextView source;
    @BindView(R.id.destination)
    AutoCompleteTextView destination;
    @BindView(R.id.destination1)
    AutoCompleteTextView destination1;
    @BindView(R.id.destination2)
    AutoCompleteTextView destination2;
    @BindView(R.id.destination_layout)
    LinearLayout destinationLayout;
    @BindView(R.id.destination_layout1)
    LinearLayout destinationLayout1;
    @BindView(R.id.destination_layout2)
    LinearLayout destinationLayout2;
    @BindView(R.id.home_address_layout)
    LinearLayout homeAddressLayout;
    @BindView(R.id.work_address_layout)
    LinearLayout workAddressLayout;
    @BindView(R.id.home_address)
    TextView homeAddress;
    @BindView(R.id.work_address)
    TextView workAddress;
    @BindView(R.id.img_add)
    ImageView img_add;
    @BindView(R.id.img_add1)
    ImageView img_add1;
    @BindView(R.id.img_add2)
    ImageView img_add2;
    @BindView(R.id.img_add3)
    ImageView img_add3;
//    @BindView(R.id.locations_rv)
//    RecyclerView locationsRv;
    @BindView(R.id.location_bs_layout)
    CardView locationBsLayout;
    @BindView(R.id.dd)
    CoordinatorLayout dd;
    boolean isEnableIdle = false;
    @BindView(R.id.llSource)
    LinearLayout llSource;

    private boolean isLocationRvClick = false;
    private boolean isSettingLocationClick = false;
    private boolean mLocationPermissionGranted;
    private GoogleMap mGoogleMap;
    private String s_address;
    private Double s_latitude;
    private Double s_longitude;
    private String d_address;
    private Double d_latitude;
    private Double d_longitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;
//    private BottomSheetBehavior mBottomSheetBehavior;
    private Boolean isEditable = true;
    private UserAddress home, work = null;
    private LocationPickPresenter<LocationPickActivity> presenter = new LocationPickPresenter<>();
    private AutoCompleteTextView selectedEditText;
//    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private PlacesAutoCompleteTextViewAdapter mAutoCompleteSourceTextViewAdapter, mAutoCompleteDestinationTextViewAdapter,
        mAutoCompleteDestination1TextViewAdapter,mAutoCompleteDestination2TextViewAdapter;
    //Base on action we are show/hide view and setResults
    private String actionName = Constants.LocationActions.SELECT_SOURCE;

    PlacesClient placesClient =null;


//    private TextWatcher filterTextWatcher = new TextWatcher() {
//SRC_ADDSRC_ADD
//        public void afterTextChanged(Editable s) {
//            //autoCompleteRv.setVisibility(View.GONE);
//            if (isEditable) if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
//                locationsRv.setVisibility(View.VISIBLE);
//                mAutoCompleteAdapter.getFilter().filter(s.toString());
//                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
//                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            } else if (!mGoogleApiClient.isConnected()) Log.e("ERROR", "API_NOT_CONNECTED");
//            if (s.toString().equals("")) locationsRv.setVisibility(View.GONE);
//        }
//
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
//                autoCompleteRv.setVisibility(View.VISIBLE);
//                mAutoCompleteRVAdapter.getFilter().filter(s.toString());
//            } else if (!mGoogleApiClient.isConnected()) Log.e("ERROR", "API_NOT_CONNECTED");
////            if (s.toString().equals("")) //autoCompleteRv.setVisibility(View.GONE);
//        }
//
//    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_location_pick;
    }

    @Override
    public void initView() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        buildGoogleApiClient();
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Places.initialize(getApplicationContext(), getString(R.string.google_map_key));
        placesClient = Places.createClient(this);


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        mBottomSheetBehavior = BottomSheetBehavior.from(locationBsLayout);
//        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });

//        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.list_item_location, mGoogleApiClient, BOUNDS_INDIA);

//        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
//        locationsRv.setLayoutManager(mLinearLayoutManager);
//        locationsRv.setAdapter(mAutoCompleteAdapter);


//        source.addTextChangedListener(filterTextWatcher);
//        destination.addTextChangedListener(filterTextWatcher);

        mAutoCompleteSourceTextViewAdapter = new PlacesAutoCompleteTextViewAdapter(this, R.layout.list_item_location, BOUNDS_INDIA, mGoogleApiClient);
        source.setThreshold(3);
        source.setAdapter(mAutoCompleteSourceTextViewAdapter);
        source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final PlacesAutoCompleteTextViewAdapter.PlaceAutocomplete item = mAutoCompleteSourceTextViewAdapter.getItem(i);
                final String placeId = String.valueOf(item.placeId);
                Log.i("LocationPickActivity", "Autocomplete item selected: " + item.address);

                List<Field> placeFields = Arrays.asList(Field.LAT_LNG);

                // Construct a request object, passing the place ID and fields array.
                FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                        .build();

                placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    Place myPlace = response.getPlace();
                    Log.e("Place", "Place found: " + myPlace.getAddress());

                    LatLng latLng = myPlace.getLatLng();
                    isLocationRvClick = true;
                    isSettingLocationClick = true;
                    setLocationText(String.valueOf(item.address), latLng,
                            isLocationRvClick, isSettingLocationClick, false);

                    mGoogleMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(new LatLng(
                                    latLng.latitude,
                                    latLng.longitude
                            ), DEFAULT_ZOOM));

                }).addOnFailureListener((exception) -> {

                    Toast.makeText(getApplicationContext(), "SOMETHING WRONG", Toast.LENGTH_SHORT).show();
                });
            }
        });

        mAutoCompleteDestinationTextViewAdapter = new PlacesAutoCompleteTextViewAdapter(this, R.layout.list_item_location, BOUNDS_INDIA, mGoogleApiClient);
        destination.setThreshold(3);
        destination.setAdapter(mAutoCompleteDestinationTextViewAdapter);
        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final PlacesAutoCompleteTextViewAdapter.PlaceAutocomplete item = mAutoCompleteDestinationTextViewAdapter.getItem(i);
                final String placeId = String.valueOf(item.placeId);
                Log.i("LocationPickActivity", "Autocomplete item selected: " + item.address);

                List<Field> placeFields = Arrays.asList(Field.LAT_LNG);

                // Construct a request object, passing the place ID and fields array.
                FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                        .build();

                placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    Place myPlace = response.getPlace();
                    Log.e("Place", "Place found: " + myPlace.getAddress());

                    LatLng latLng = myPlace.getLatLng();
                    isLocationRvClick = true;
                    isSettingLocationClick = true;
                    setLocationText(String.valueOf(item.address), latLng,
                            isLocationRvClick, isSettingLocationClick, false);



                }).addOnFailureListener((exception) -> {

                    Toast.makeText(getApplicationContext(), "SOMETHING WRONG", Toast.LENGTH_SHORT).show();
                });
            }
        });

        mAutoCompleteDestination1TextViewAdapter = new PlacesAutoCompleteTextViewAdapter(this, R.layout.list_item_location, BOUNDS_INDIA, mGoogleApiClient);
        destination1.setThreshold(3);
        destination1.setAdapter(mAutoCompleteDestination1TextViewAdapter);
        destination1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final PlacesAutoCompleteTextViewAdapter.PlaceAutocomplete item = mAutoCompleteDestination1TextViewAdapter.getItem(i);
                final String placeId = String.valueOf(item.placeId);
                Log.i("LocationPickActivity", "Autocomplete item selected: " + item.address);

                List<Field> placeFields = Arrays.asList(Field.LAT_LNG);

                // Construct a request object, passing the place ID and fields array.
                FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                        .build();

                placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    Place myPlace = response.getPlace();
                    Log.e("Place", "Place found: " + myPlace.getAddress());

                    LatLng latLng = myPlace.getLatLng();
                    isLocationRvClick = true;
                    isSettingLocationClick = true;
                    setLocationText(String.valueOf(item.address), latLng,
                            isLocationRvClick, isSettingLocationClick, false);



                }).addOnFailureListener((exception) -> {

                    Toast.makeText(getApplicationContext(), "SOMETHING WRONG", Toast.LENGTH_SHORT).show();
                });
            }
        });

        mAutoCompleteDestination2TextViewAdapter = new PlacesAutoCompleteTextViewAdapter(this, R.layout.list_item_location, BOUNDS_INDIA, mGoogleApiClient);
        destination2.setThreshold(3);
        destination2.setAdapter(mAutoCompleteDestination2TextViewAdapter);
        destination2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final PlacesAutoCompleteTextViewAdapter.PlaceAutocomplete item = mAutoCompleteDestination2TextViewAdapter.getItem(i);
                final String placeId = String.valueOf(item.placeId);
                Log.i("LocationPickActivity", "Autocomplete item selected: " + item.address);

                List<Field> placeFields = Arrays.asList(Field.LAT_LNG);

                // Construct a request object, passing the place ID and fields array.
                FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                        .build();

                placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    Place myPlace = response.getPlace();
                    Log.e("Place", "Place found: " + myPlace.getAddress());

                    LatLng latLng = myPlace.getLatLng();
                    isLocationRvClick = true;
                    isSettingLocationClick = true;
                    setLocationText(String.valueOf(item.address), latLng,
                            isLocationRvClick, isSettingLocationClick, false);



                }).addOnFailureListener((exception) -> {

                    Toast.makeText(getApplicationContext(), "SOMETHING WRONG", Toast.LENGTH_SHORT).show();
                });
            }
        });

        source.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) selectedEditText = source;
        });

        destination.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) selectedEditText = destination;
        });
        destination1.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) selectedEditText = destination1;
        });

        destination2.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) selectedEditText = destination2;
        });

        destination.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                setResult(Activity.RESULT_OK, new Intent());
//                finish();
                //setResult();
                return true;
            }
            return false;
        });


        source.setText(MvpApplication.RIDE_REQUEST.containsKey(SRC_ADD)
                ? TextUtils.isEmpty(Objects.requireNonNull(MvpApplication.RIDE_REQUEST.get(SRC_ADD)).toString())
                ? ""
                : String.valueOf(MvpApplication.RIDE_REQUEST.get(SRC_ADD))
                : "");

        destination.setText(MvpApplication.RIDE_REQUEST.containsKey(DEST_ADD)
                ? TextUtils.isEmpty(Objects.requireNonNull(MvpApplication.RIDE_REQUEST.get(DEST_ADD)).toString())
                ? ""
                : String.valueOf(MvpApplication.RIDE_REQUEST.get(DEST_ADD))
                : "");


//        locationsRv.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, position) -> {
//                    if (mAutoCompleteAdapter.getItemCount() == 0) return;
//                    final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
//                    final String placeId = String.valueOf(item.placeId);
//                    Log.i("LocationPickActivity", "Autocomplete item selected: " + item.address);
//
//                    List<Field> placeFields = Arrays.asList(Field.LAT_LNG);
//
//                    // Construct a request object, passing the place ID and fields array.
//                    FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
//                            .build();
//
//                    placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
//                        Place myPlace = response.getPlace();
//                        Log.e("Place", "Place found: " + myPlace.getAddress());
//
//                        LatLng latLng = myPlace.getLatLng();
//                        isLocationRvClick = true;
//                        isSettingLocationClick = true;
//                        setLocationText(String.valueOf(item.address), latLng,
//                                isLocationRvClick, isSettingLocationClick);
//
//
//
//                    }).addOnFailureListener((exception) -> {
//
//                        Toast.makeText(getApplicationContext(), "SOMETHING WRONG", Toast.LENGTH_SHORT).show();
//                    });
//
//                    Log.i("LocationPickActivity", "Clicked: " + item.address);
//                    Log.i("LocationPickActivity", "Called getPlaceById to get Place details for " + item.placeId);
//
//
//
////                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
////                    placeResult.setResultCallback(places -> {
////                        if (places.getCount() == 1) {
////                            LatLng latLng = places.get(0).getLatLng();
////                            isLocationRvClick = true;
////                            isSettingLocationClick = true;
////                            setLocationText(String.valueOf(item.address), latLng,
////                                    isLocationRvClick, isSettingLocationClick);
////                            //Toast.makeText(getApplicationContext(), String.valueOf(places.get(0).getLatLng()), Toast.LENGTH_SHORT).show();
////                        } else
////                            Toast.makeText(getApplicationContext(), "SOMETHING WRONG", Toast.LENGTH_SHORT).show();
////                    });
////
////                    Log.i("LocationPickActivity", "Clicked: " + item.address);
////                    Log.i("LocationPickActivity", "Called getPlaceById to get Place details for " + item.placeId);
//                })
//        );

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            actionName = bundle.getString("actionName",Constants.LocationActions.SELECT_SOURCE);

            if (!TextUtils.isEmpty(actionName) && actionName.equalsIgnoreCase(Constants.LocationActions.SELECT_SOURCE)) {
                destination.setCursorVisible(true);
                source.setCursorVisible(true);
                source.requestFocus();
                selectedEditText = source;
            }else if (!TextUtils.isEmpty(actionName) && actionName.equalsIgnoreCase(Constants.LocationActions.SELECT_DESTINATION)){
                source.setCursorVisible(true);
                destination.setCursorVisible(true);
                destination.setText("");
                destination.requestFocus();
                selectedEditText = destination;
            }else if (!TextUtils.isEmpty(actionName) && actionName.equals(Constants.LocationActions.CHANGE_DESTINATION)){
                llSource.setVisibility(View.GONE);
                source.setHint(getString(R.string.select_location));
                selectedEditText = destination;
            }else if (!TextUtils.isEmpty(actionName) && (actionName.equals(Constants.LocationActions.SELECT_HOME)|| actionName.equals(Constants.LocationActions.SELECT_WORK))){
                destinationLayout.setVisibility(View.GONE);
                selectedEditText = destination;
                source.setText("");
                source.setHint(getString(R.string.select_location));
            } else{
                destinationLayout.setVisibility(View.VISIBLE);
                llSource.setVisibility(View.VISIBLE);
                source.setHint(getString(R.string.pickup_location));
                selectedEditText = source;
            }
        }
        presenter.address();
    }

    private void setLocationText(String address, LatLng latLng, boolean isLocationRvClick,
                                 boolean isSettingLocationClick, boolean isUpdateNeeded) {
        if (address != null && latLng != null) {
            isEditable = false;
            if(isUpdateNeeded)
                selectedEditText.setText(address);
            isEditable = true;

            if (selectedEditText.getTag().equals("source")) {
                s_address = address;
                s_latitude = latLng.latitude;
                s_longitude = latLng.longitude;
                MvpApplication.RIDE_REQUEST.put(SRC_ADD, address);
                MvpApplication.RIDE_REQUEST.put(SRC_LAT, latLng.latitude);
                MvpApplication.RIDE_REQUEST.put(SRC_LONG, latLng.longitude);
                MvpApplication.RIDE_REQUEST.put(DEST_ADD0, address);
                MvpApplication.RIDE_REQUEST.put(DEST_LAT0, latLng.latitude);
                MvpApplication.RIDE_REQUEST.put(DEST_LONG0, latLng.longitude);
            }

            if (selectedEditText.getTag().equals("destination")) {
                MvpApplication.RIDE_REQUEST.put(DEST_ADD, address);
                MvpApplication.RIDE_REQUEST.put(DEST_LAT, latLng.latitude);
                MvpApplication.RIDE_REQUEST.put(DEST_LONG, latLng.longitude);

                if (isLocationRvClick) {
                    //  Done functionality called...
//                    setResult(Activity.RESULT_OK, new Intent());
//                    finish();
                    //region for change by Atul Mavani
                    //setResult();
                    //endregion

                }
            }
            if (selectedEditText.getTag().equals("destination1")) {
                MvpApplication.RIDE_REQUEST.put(DEST_ADD1, address);
                MvpApplication.RIDE_REQUEST.put(DEST_LAT1, latLng.latitude);
                MvpApplication.RIDE_REQUEST.put(DEST_LONG1, latLng.longitude);

                if (isLocationRvClick) {
                    //  Done functionality called...
//                    setResult(Activity.RESULT_OK, new Intent());
//                    finish();
                    //region for change by Atul Mavani
                    //setResult();
                    //endregion

                }
            }
            if (selectedEditText.getTag().equals("destination2")) {
                MvpApplication.RIDE_REQUEST.put(DEST_ADD2, address);
                MvpApplication.RIDE_REQUEST.put(DEST_LAT2, latLng.latitude);
                MvpApplication.RIDE_REQUEST.put(DEST_LONG2, latLng.longitude);

                if (isLocationRvClick) {
                    //  Done functionality called...
//                    setResult(Activity.RESULT_OK, new Intent());
//                    finish();
                    //region for change by Atul Mavani
                    //setResult();
                    //endregion

                }
            }
        } else {
            isEditable = false;
            selectedEditText.setText("");
//            locationsRv.setVisibility(View.GONE);
            isEditable = true;

            if (selectedEditText.getTag().equals("source")) {
                MvpApplication.RIDE_REQUEST.remove(SRC_ADD);
                MvpApplication.RIDE_REQUEST.remove(SRC_LAT);
                MvpApplication.RIDE_REQUEST.remove(SRC_LONG);
                MvpApplication.RIDE_REQUEST.remove(DEST_ADD0);
                MvpApplication.RIDE_REQUEST.remove(DEST_LAT0);
                MvpApplication.RIDE_REQUEST.remove(DEST_LONG0);

            }
            if (selectedEditText.getTag().equals("destination")) {
                MvpApplication.RIDE_REQUEST.remove(DEST_ADD);
                MvpApplication.RIDE_REQUEST.remove(DEST_LAT);
                MvpApplication.RIDE_REQUEST.remove(DEST_LONG);
            }
            if (selectedEditText.getTag().equals("destination1")) {
                MvpApplication.RIDE_REQUEST.remove(DEST_ADD1);
                MvpApplication.RIDE_REQUEST.remove(DEST_LAT1);
                MvpApplication.RIDE_REQUEST.remove(DEST_LONG1);
            }
            if (selectedEditText.getTag().equals("destination2")) {
                MvpApplication.RIDE_REQUEST.remove(DEST_ADD2);
                MvpApplication.RIDE_REQUEST.remove(DEST_LAT2);
                MvpApplication.RIDE_REQUEST.remove(DEST_LONG2);
            }
        }

        if (isSettingLocationClick) {
            hideKeyboard();
//            locationsRv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.v("Google API", "Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @OnClick({R.id.source, R.id.destination, R.id.img_add,R.id.img_add1,R.id.reset_source, R.id.reset_destination, R.id.home_address_layout, R.id.work_address_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.source:
                selectedEditText = source;
                source.requestFocus();
                break;
            case R.id.destination:
                selectedEditText = destination;
                destination.requestFocus();
                break;
            case R.id.reset_source:
                selectedEditText = source;
                source.requestFocus();
                setLocationText(null, null, isLocationRvClick, isSettingLocationClick, true);
                break;
            case R.id.reset_destination:
                destination.requestFocus();
                selectedEditText = destination;
                setLocationText(null, null, isLocationRvClick, isSettingLocationClick, true);
                break;
            case R.id.home_address_layout:
                if (home != null)
                    setLocationText(home.getAddress(),
                            new LatLng(home.getLatitude(), home.getLongitude()),
                            isLocationRvClick, isSettingLocationClick, true);
                break;
            case R.id.work_address_layout:
                if (work != null)
                    setLocationText(work.getAddress(),
                            new LatLng(work.getLatitude(), work.getLongitude()),
                            isLocationRvClick, isSettingLocationClick, true);
                break;
            case R.id.img_add:
                //img_add.setImageResource(R.drawable.ic_close);
                img_add.setImageDrawable(null);
                destinationLayout1.setVisibility(View.VISIBLE);
                break;
            case R.id.img_add1:
                //img_add1.setImageResource(R.drawable.ic_close);
                img_add1.setImageDrawable(null);
                img_add.setVisibility(View.GONE);
                img_add1.setVisibility(View.GONE);
                img_add2.setVisibility(View.GONE);
                img_add3.setVisibility(View.GONE);
                destinationLayout2.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onCameraIdle() {
        try {
            CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
            if (isEnableIdle) {
                String address = getAddress(cameraPosition.target);
                System.out.println("onCameraIdle " + address);
                hideKeyboard();
                setLocationText(address, cameraPosition.target, isLocationRvClick, isSettingLocationClick, true);
            }
            isEnableIdle = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraMove() {
        System.out.println("LocationPickActivity.onCameraMove");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            //      Google map custom style...
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        } catch (Resources.NotFoundException e) {
            Log.d("Map:Style", "Can't find style. Error: ");
        }
        this.mGoogleMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        mLastKnownLocation = task.getResult();
                        mGoogleMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(new LatLng(
                                        mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()
                                ), DEFAULT_ZOOM));
                    } else {
                        Log.d("Map", "Current location is null. Using defaults.");
                        Log.e("Map", "Exception: %s", task.getException());
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mLocationPermissionGranted = true;
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
    }

    private void updateLocationUI() {
        if (mGoogleMap == null) return;
        try {
            if (mLocationPermissionGranted) {
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.setOnCameraMoveListener(this);
                mGoogleMap.setOnCameraIdleListener(this);
            } else {
                mGoogleMap.setMyLocationEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                updateLocationUI();
                getDeviceLocation();
            }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, "API_NOT_CONNECTED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
//        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_pick_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (!TextUtils.isEmpty(actionName) && actionName.equals(Constants.LocationActions.SELECT_HOME) || actionName.equals(Constants.LocationActions.SELECT_WORK)){
                    Intent intent = new Intent();
                    intent.putExtra(SRC_ADD, s_address);
                    intent.putExtra(SRC_LAT, s_latitude);
                    intent.putExtra(SRC_LONG, s_longitude);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                }
                setResult();
                return true;

//            case android.R.id.home:
//                Toast.makeText(getApplicationContext(), "Back button clicked", Toast.LENGTH_SHORT).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(AddressResponse address) {
        if (address.getHome().isEmpty()) homeAddressLayout.setVisibility(View.GONE);
        else {
            home = address.getHome().get(address.getHome().size() - 1);
            homeAddress.setText(home.getAddress());
            homeAddressLayout.setVisibility(View.VISIBLE);
        }

        if (address.getWork().isEmpty()) workAddressLayout.setVisibility(View.GONE);
        else {
            work = address.getWork().get(address.getWork().size() - 1);
            workAddress.setText(work.getAddress());
            workAddressLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    protected void setResult() {
        if (!TextUtils.isEmpty(actionName) && actionName.equals(Constants.LocationActions.SELECT_HOME) || actionName.equals(Constants.LocationActions.SELECT_WORK) || (s_address!=null && (!s_address.isEmpty() || !s_address.equalsIgnoreCase("")))){
            Intent intent = new Intent();
            intent.putExtra(SRC_ADD, s_address);
            intent.putExtra(SRC_LAT, s_latitude);
            intent.putExtra(SRC_LONG, s_longitude);

            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    }
}
