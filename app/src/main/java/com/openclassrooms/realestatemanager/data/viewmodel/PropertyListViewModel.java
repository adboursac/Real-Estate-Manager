package com.openclassrooms.realestatemanager.data.viewmodel;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.repository.LocationRepository;
import com.openclassrooms.realestatemanager.data.repository.PropertyPictureRepository;
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class PropertyListViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;
    private PropertyPictureRepository mPropertyPictureRepository;
    private LocationRepository mLocationRepository;
    private ExecutorService mExecutorService;

    private MutableLiveData<Property> mCurrentProperty = new MutableLiveData<>();
    private MutableLiveData<List<PropertyPicture>> mCurrentPropertyPictures = new MutableLiveData<>();

    private MutableLiveData<List<Property>> mCurrentPropertiesList = new MutableLiveData<>(new ArrayList<>());

    private boolean displayingSearch = false;

    public PropertyListViewModel(PropertyRepository propertyRepository, PropertyPictureRepository propertyPictureRepository, LocationRepository locationRepository, ExecutorService executorService) {
        mPropertyRepository = propertyRepository;
        mPropertyPictureRepository = propertyPictureRepository;
        mLocationRepository = locationRepository;
        mExecutorService = executorService;
    }

    public LiveData<List<Property>> getCurrentPropertiesList() {
        return mCurrentPropertiesList;
    }

    public ExecutorService getExecutorService() {
        return mExecutorService;
    }

    public void fetchAllProperties(LifecycleOwner lifecycleOwner) {
        displayingSearch = false;
        mPropertyRepository.fetchAllProperties().observe(lifecycleOwner, mCurrentPropertiesList::setValue);
    }

    public void searchProperties(LifecycleOwner lifecycleOwner,
                                 String type,
                                 String district,
                                 Integer minPrice,
                                 Integer maxPrice,
                                 Integer minSurface,
                                 Integer maxSurface,
                                 Integer minRooms,
                                 Integer maxRooms,
                                 boolean hasSwimmingPool,
                                 boolean hasSchool,
                                 boolean hasShopping,
                                 boolean hasParking
    ) {
        displayingSearch = true;
        mPropertyRepository.searchProperty(
                type,
                district,
                minPrice,
                maxPrice,
                minSurface,
                maxSurface,
                minRooms,
                maxRooms,
                hasSwimmingPool,
                hasSchool,
                hasShopping,
                hasParking
        ).observe(lifecycleOwner, mCurrentPropertiesList::setValue);
    }

    public boolean isDisplayingSearch() {
        return displayingSearch;
    }

    public LiveData<Property> getCurrentProperty() {
        return mCurrentProperty;
    }

    public void setCurrentProperty(LifecycleOwner lifecycleOwner, Property currentProperty) {
        mCurrentProperty.setValue(currentProperty);
        mPropertyPictureRepository.fetchPictures(currentProperty.getId()).observe(lifecycleOwner, mCurrentPropertyPictures::setValue);
    }

    public void selectProperty(LifecycleOwner lifecycleOwner, long id) {
        List<Property> properties = mCurrentPropertiesList.getValue();
        if (properties == null) return;
        for (Property p : properties) {
            if (p.getId() == id ) setCurrentProperty(lifecycleOwner, p);
        }
    }

    public LiveData<List<PropertyPicture>> getCurrentPropertyPictures() {
        return mCurrentPropertyPictures;
    }

    public void refreshLocation() {
        mLocationRepository.refreshLocation();
    }


    public void deleteCurrentProperty() {
        Property currentProperty = mCurrentProperty.getValue();
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
        if (currentProperty == null || pictures == null) return;
        mPropertyRepository.getExecutor().execute(() -> {
            for (PropertyPicture p : pictures) mPropertyPictureRepository.delete(p);
            mPropertyRepository.delete(currentProperty);
        });
    }

    public String generatePoiString(Property property) {
        Context context = MainApplication.getContext();
        String poiString = property.hasPoiSwimmingPool() ? context.getString(R.string.property_poi_swimming_pool) + ", " : "";
        poiString += property.hasPoiParking() ? context.getString(R.string.property_poi_parking) + ", " : "";
        poiString += property.hasPoiSchool() ? context.getString(R.string.property_poi_school) + ", " : "";
        poiString += property.hasPoiShopping() ? context.getString(R.string.property_poi_shopping) : "";
        return poiString;
    }

    // --- Localisation feature ---

    public LiveData<LatLng> getLocationLiveData() { return  mLocationRepository.getLocationLiveData(); }
    public boolean hasLocationPermission() { return mLocationRepository.hasLocationPermission(); }
    public void requestLocationPermission(Activity activity) { mLocationRepository.requestLocationPermission(activity); }
    //public void refreshLocation() { mLocationRepository.refreshLocation(); }

}
