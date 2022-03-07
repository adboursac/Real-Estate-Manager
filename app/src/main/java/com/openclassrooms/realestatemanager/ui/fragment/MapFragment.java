package com.openclassrooms.realestatemanager.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ViewModelFactory;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyListViewModel;
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding;
import com.openclassrooms.realestatemanager.utils.MapHelper;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private int mMapZoom = 18;
    private GoogleMap mMap;
    private FragmentMapBinding mBinding;
    private PropertyListViewModel mPropertyListViewModel;
    private LatLng mCurrentPropertyLocation;
    private LatLng mLastLocation;
    private boolean requireLocationActivation = true;

    public MapFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMapBinding.inflate(inflater, container, false);

        mPropertyListViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyListViewModel.class);
        //setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        configureMapObjects();
        initObservers();
        initLocateButton();
    }

    /**
     * Configure all google map related objects and listeners
     */
    public void configureMapObjects() {
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.google_map_style));
        verifyLocationConfiguration();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    /**
     * Init viewModel observers reactions
     */
    private void initObservers() {
        mPropertyListViewModel.getCurrentPropertiesList().observe(getViewLifecycleOwner(), this::updateEveryProperties);

        mPropertyListViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
            verifyLocationConfiguration();
            mLastLocation = location;
        });
    }

    private void verifyLocationConfiguration() {
        if (requireLocationActivation) {
            if (mPropertyListViewModel.hasLocationPermission()) {
                requireLocationActivation = false;
                configureLocationRelatedObjects();
            }
            else {
                mPropertyListViewModel.requestLocationPermission(requireActivity());
            }
        }
    }

    /**
     * Locations related configurations that require location permission to occur
     */
    @SuppressLint("MissingPermission")
    public void configureLocationRelatedObjects() {
        if (mMap != null && mPropertyListViewModel.hasLocationPermission()) {
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     * Create all the markers of the properties contained in the given list
     *
     * @param properties list of restaurants to display as markers
     */
    public void updateEveryProperties(List<Property> properties) {
        if (mMap == null) return;

        long currentPropertyId;
        Property currentProperty = mPropertyListViewModel.getCurrentProperty().getValue();
        if (currentProperty == null) currentPropertyId = -1L;
        else currentPropertyId = currentProperty.getId();

        Handler mainHandler = new Handler(requireContext().getMainLooper());
        mMap.clear();
        for (Property p : properties) {
            displayMarkerAsynchronously(p, p.getId() == currentPropertyId, mainHandler);
        }
    }

    /**
     * Create asynchronously a markers of the property
     * @param property property to display
     * @param displayInfo if true the marker will display info window with property type
     * @param mainHandler UI thread handler to post marker creation action
     */
    public void displayMarkerAsynchronously(Property property, boolean displayInfo, Handler mainHandler) {

        mPropertyListViewModel.getExecutorService().execute(() -> {

            LatLng latLng = MapHelper.addressToFirstLatLng(property.getFullAddress(), getContext());
            if (displayInfo) mCurrentPropertyLocation = latLng;

            mainHandler.post(() -> {
                createPropertyMarker(
                        latLng,
                        property.getType(),
                        property.getId(),
                        displayInfo
                );
                if (displayInfo) moveCameraOnCurrentProperty();
            });
        });
    }

    /**
     * Create a Property marker.
     *
     * @param latLng property location
     * @param markerLabel marker info label
     * @param id  id of the property
     * @param displayInfo if true marker will display property Type in info window
     */
    private void createPropertyMarker(LatLng latLng, String markerLabel, long id, boolean displayInfo) {
        if (latLng == null || mMap == null) return;
        float hue = BitmapDescriptorFactory.HUE_ROSE;
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(markerLabel)
                .icon(BitmapDescriptorFactory.defaultMarker(hue)));
        if (marker != null) {
            marker.setTag(id);
            if (displayInfo) marker.showInfoWindow();
        }
    }

    private void initLocateButton() {
        mBinding.floatingActionButton.setOnClickListener(view -> moveCamera(mLastLocation));
    }

    /**
     * Moves map camera on given location
     *
     * @param latLng location to move the camera
     */
    public void moveCamera(LatLng latLng) {
        if (latLng == null ) return;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMapZoom));
    }

    public void moveCameraOnCurrentProperty() {
        if (mCurrentPropertyLocation == null ) return;
        moveCamera(mCurrentPropertyLocation);
    }
}
