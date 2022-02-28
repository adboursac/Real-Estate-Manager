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

public class PropertyAddViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;
    private PropertyPictureRepository mPropertyPictureRepository;

    private MutableLiveData<Property> mCurrentProperty = new MutableLiveData<>();
    private MutableLiveData<Integer> mMainPictureRowIndex = new MutableLiveData<>(0);
    private MutableLiveData<List<PropertyPicture>> mCurrentPropertyPictures = new MutableLiveData<>(new ArrayList<>());

    public PropertyAddViewModel(PropertyRepository propertyRepository, PropertyPictureRepository propertyPictureRepository) {
        mPropertyRepository = propertyRepository;
        mPropertyPictureRepository = propertyPictureRepository;
    }

    public LiveData<Property> getCurrentProperty() {
        return mCurrentProperty;
    }

    public LiveData<List<PropertyPicture>> getCurrentPropertyPictures() {
        return mCurrentPropertyPictures;
    }

    public LiveData<Integer> getMainPictureRowIndex() { return mMainPictureRowIndex; }

    public void setMainPictureRowIndex(int mainPictureRowIndex) {
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
        if (pictures == null || pictures.size() == 0) return;
        mMainPictureRowIndex.setValue(mainPictureRowIndex);
    }

    public void saveProperty(Property property) {
        mPropertyRepository.getExecutor().execute(() -> {
            int mainPictureRowIndex;

            // If main picture has not been set yet,
            // We set mainPictureId as -1L
            if (mMainPictureRowIndex.getValue() == null) {
                property.setMainPictureId(-1L);
                mainPictureRowIndex = -1;
            }
            else mainPictureRowIndex = mMainPictureRowIndex.getValue();

            long propertyId = mPropertyRepository.insert(property);

            List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
            if (pictures == null) return;

            for (int i=0; i < pictures.size(); i++) {
                PropertyPicture p = pictures.get(i);
                p.setPropertyId(propertyId);
                long pictureId = mPropertyPictureRepository.insert(p);

                if (i == mainPictureRowIndex) {
                    mPropertyRepository.updateMainPicture(propertyId, pictureId, p.getUri());
                }
            }
        });
    }

    public void addPicture(PropertyPicture picture) {
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
        if (pictures == null) return;
        pictures.add(picture);
        mCurrentPropertyPictures.setValue(pictures);
    }

    /**
     * Delete picture from current picture list
     * @param pictureRowIndex row index of the picture
     */
    public void deletePicture(int pictureRowIndex) {
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
        if (pictures == null || pictures.size() == 0) return;

        // We check if the picture we are going to delete is main
        // If it is so, we set first picture as main picture
        int mainPictureRowIndex = mMainPictureRowIndex.getValue();
        if (pictureRowIndex == mainPictureRowIndex) setMainPictureRowIndex(0);

        pictures.remove(pictureRowIndex);
        mCurrentPropertyPictures.setValue(pictures);
    }
}
