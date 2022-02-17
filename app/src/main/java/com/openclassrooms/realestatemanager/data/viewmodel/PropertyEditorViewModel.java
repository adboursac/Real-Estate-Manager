package com.openclassrooms.realestatemanager.data.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.repository.PropertyPictureRepository;
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository;

import java.util.ArrayList;
import java.util.List;

public class PropertyEditorViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;
    private PropertyPictureRepository mPropertyPictureRepository;

    private MutableLiveData<Property> mCurrentProperty = new MutableLiveData<>();
    private MutableLiveData<List<PropertyPicture>> mCurrentPropertyPictures = new MutableLiveData<>(new ArrayList<>());

    public PropertyEditorViewModel(PropertyRepository propertyRepository, PropertyPictureRepository propertyPictureRepository) {
        mPropertyRepository = propertyRepository;
        mPropertyPictureRepository = propertyPictureRepository;
    }

    public LiveData<Property> getCurrentProperty() {
        return mCurrentProperty;
    }

    public LiveData<List<PropertyPicture>> getCurrentPropertyPictures() {
        return mCurrentPropertyPictures;
    }

    public void saveProperty(Property property) {
        mPropertyRepository.getExecutor().execute(() -> {
            long propertyId = mPropertyRepository.insert(property);

            List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
            if (pictures == null) return;

            for (PropertyPicture p : pictures) {
                p.setPropertyId(propertyId);
                mPropertyPictureRepository.insert(p);
            }
        });
    }

    public void addPicture(PropertyPicture picture) {
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
        if (pictures == null) return;
        pictures.add(picture);
        mCurrentPropertyPictures.setValue(pictures);
    }

    public void updateCurrentProperty(Property property) {
        mCurrentProperty.setValue(property);
    }
}
