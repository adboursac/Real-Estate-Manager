package com.openclassrooms.realestatemanager.data.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.model.dao.PropertyPictureDao;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class PropertyPictureRepository {

    private final PropertyPictureDao mPropertyPictureDao;
    private final ExecutorService mExecutor;

    public PropertyPictureRepository(PropertyPictureDao propertyPictureDao, ExecutorService executor) {
        mPropertyPictureDao = propertyPictureDao;
        mExecutor = executor;
    }

    public LiveData<List<PropertyPicture>> fetchPictures(long projectId) {
        return mPropertyPictureDao.fetchPictures(projectId);
    }

    public void insert(PropertyPicture propertyPicture) {
        mPropertyPictureDao.insert(propertyPicture);
    }

    public void delete(PropertyPicture propertyPicture) {
        mPropertyPictureDao.delete(propertyPicture.getId());
    }
}
