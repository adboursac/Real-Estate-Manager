package com.openclassrooms.realestatemanager.data.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.repository.PropertyPictureRepository;
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository;

import java.util.List;

public class PropertyListViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;
    private PropertyPictureRepository mPropertyPictureRepository;

    private MutableLiveData<Property> mCurrentProperty = new MutableLiveData<>();
    private LiveData<List<PropertyPicture>> mCurrentPropertyPictures;
    private int mMainPictureIndex = -1;

    public PropertyListViewModel(PropertyRepository propertyRepository, PropertyPictureRepository propertyPictureRepository) {
        mPropertyRepository = propertyRepository;
        mPropertyPictureRepository = propertyPictureRepository;
    }

    public LiveData<List<Property>> fetchAllProperties() {
        return mPropertyRepository.fetchAllProperties();
    }

    public LiveData<Property> getCurrentProperty() {
        return mCurrentProperty;
    }

    public void setCurrentProperty(Property currentProperty) {
        mCurrentProperty.setValue(currentProperty);
        mCurrentPropertyPictures = mPropertyPictureRepository.fetchPictures(currentProperty.getId());
    }

    public LiveData<List<PropertyPicture>> getCurrentPropertyPictures() {
        return mCurrentPropertyPictures;
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
}
