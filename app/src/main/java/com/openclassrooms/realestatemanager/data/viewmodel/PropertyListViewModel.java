package com.openclassrooms.realestatemanager.data.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.repository.PropertyPictureRepository;
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.ui.fragment.SelectPropertyCommand;

import java.util.List;

public class PropertyListViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;
    private PropertyPictureRepository mPropertyPictureRepository;


    private MutableLiveData<Property> mCurrentProperty = new MutableLiveData<>();


    private LiveData<List<PropertyPicture>> mCurrentPropertyPictures;

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
}
