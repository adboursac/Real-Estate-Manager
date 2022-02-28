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

public class PropertyEditViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;
    private PropertyPictureRepository mPropertyPictureRepository;

    private MutableLiveData<Property> mCurrentPropertyState = new MutableLiveData<>();
    private MutableLiveData<Integer> mMainPictureRowIndex = new MutableLiveData<>(0);
    private MutableLiveData<List<PropertyPicture>> mCurrentPropertyPictures = new MutableLiveData<>(new ArrayList<>());

    private List<PropertyPicture> mDeletedPictures = new ArrayList<>();
    private List<PropertyPicture> mUpdatedPictures = new ArrayList<>();

    public PropertyEditViewModel(PropertyRepository propertyRepository, PropertyPictureRepository propertyPictureRepository) {
        mPropertyRepository = propertyRepository;
        mPropertyPictureRepository = propertyPictureRepository;
    }

    public LiveData<Property> getCurrentPropertyState() {
        return mCurrentPropertyState;
    }

    public void setCurrentPropertyState(Property property) {
        mCurrentPropertyState.setValue(property);
        updateMainPictureRowIndex();
    }

    public LiveData<List<PropertyPicture>> getCurrentPropertyPictures() {
        return mCurrentPropertyPictures;
    }

    public void setCurrentPropertyPictures(List<PropertyPicture> currentPropertyPictures) {
        mCurrentPropertyPictures.setValue(currentPropertyPictures);
    }

    public LiveData<Integer> getMainPictureRowIndex() {
        return mMainPictureRowIndex;
    }

    public void setMainPictureRowIndex(int mainPictureRowIndex) {
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
        if (pictures == null || pictures.size() == 0) return;
        mMainPictureRowIndex.setValue(mainPictureRowIndex);
    }

    public void saveProperty(Property inputProperty) {
        Property currentPropertyState = mCurrentPropertyState.getValue();
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();

        if (currentPropertyState == null) return;

        mPropertyRepository.getExecutor().execute(() -> {

            // Insert new pictures, Update property pictures and main picture id if needed
            updatePropertyPictures(pictures, inputProperty, currentPropertyState);

            //as input property come from UI inputs it has no id yet
            inputProperty.setId(currentPropertyState.getId());

            // update current property if needed
            if (!currentPropertyState.hasSameContent(inputProperty)) {
                mPropertyRepository.updateProperty(inputProperty);
            }
        });
    }

    /**
     * Insert new pictures, Update pictures attributes and main picture id if needed
     * @param pictures pictures list of given property
     * @param inputProperty property generated from UI input.
     * @param currentPropertyState current property state
     */
    private void updatePropertyPictures(List<PropertyPicture> pictures, Property inputProperty, Property currentPropertyState) {
        Integer mainPictureRowIndex = mMainPictureRowIndex.getValue();
        if (pictures == null || pictures.size() == 0 || mainPictureRowIndex == null) return;

        PropertyPicture mainPicture = pictures.get(mainPictureRowIndex);
        // If main picture is a new created picture (id == 0)
        if (mainPicture.getId() == 0) {
            //we insert it now
            long mainPictureId = mPropertyPictureRepository.insert(mainPicture);
            // Update property values
            inputProperty.setMainPictureId(mainPictureId);
            inputProperty.setMainPictureUri(mainPicture.getUri());
            // Update picture id to avoid redundant insert,
            // as every pictures with id == 0 will be inserted after this statement.
            mainPicture.setId(mainPictureId);
        }
        // Else we update property mainPicture as selected picture have been inserted already.
        else {
            inputProperty.setMainPictureId(mainPicture.getId());
            inputProperty.setMainPictureUri(mainPicture.getUri());
        }

        // Insert new pictures in database (with id == 0)
        for (PropertyPicture p : pictures) {
            p.setPropertyId(currentPropertyState.getId());
            if (p.getId() == 0) mPropertyPictureRepository.insert(p);
        }

        // remove deleted pictures from database
        for (PropertyPicture p : mDeletedPictures) mPropertyPictureRepository.delete(p);

        // update pictures changes in database (like: description update)
        for (PropertyPicture p : mUpdatedPictures) mPropertyPictureRepository.update(p);
    }

    public void addPicture(PropertyPicture picture) {
        Property currentPropertyState = mCurrentPropertyState.getValue();
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
        if (currentPropertyState == null || pictures == null) return;
        picture.setPropertyId(currentPropertyState.getId());
        pictures.add(picture);
        mCurrentPropertyPictures.setValue(pictures);
    }

    /**
     * Delete picture from current picture list
     *
     * @param pictureRowIndex row index of the picture
     */
    public void deletePicture(int pictureRowIndex) {
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
        Integer mainPictureRowIndex = mMainPictureRowIndex.getValue();
        if (pictures == null || pictures.size() == 0 || mainPictureRowIndex == null) return;

        // We check if the picture we are going to delete is main. We reset main picture if needed
        if (pictureRowIndex == mainPictureRowIndex) setMainPictureRowIndex(0);

        PropertyPicture picture = pictures.get(pictureRowIndex);

        // Register picture for deleting it from database, when we will save changes
        // New created pictures are not affected
        if (picture.getId() != 0) {
            mDeletedPictures.add(picture);
            mUpdatedPictures.remove(picture);
        }

        pictures.remove(pictureRowIndex);
        mCurrentPropertyPictures.setValue(pictures);
    }

    public void setPictureDescription(int pictureRowIndex, String description) {
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
        if (pictures == null || pictures.size() == 0) return;
        PropertyPicture picture = pictures.get(pictureRowIndex);
        picture.setDescription(description);

        // We won't update newly created pictures as it will be fully inserted
        if (picture.getId() != 0) mUpdatedPictures.add(picture);

        mCurrentPropertyPictures.setValue(pictures);
    }

    private void updateMainPictureRowIndex() {
        Property currentProperty = mCurrentPropertyState.getValue();
        List<PropertyPicture> pictures = mCurrentPropertyPictures.getValue();
        if (pictures == null || currentProperty == null) return;

        for (int i = 0; i < pictures.size(); i++) {
            if (pictures.get(i).getId() == currentProperty.getMainPictureId())
                mMainPictureRowIndex.setValue(i);
        }
    }
}