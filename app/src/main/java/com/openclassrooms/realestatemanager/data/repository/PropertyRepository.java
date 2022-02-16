package com.openclassrooms.realestatemanager.data.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.dao.PropertyDao;

import java.util.List;

public class PropertyRepository {

    private final PropertyDao mPropertyDao;

    public PropertyRepository(PropertyDao propertyDao) {
        mPropertyDao = propertyDao;
    }

    public LiveData<List<Property>> fetchAllProperties() {
        return mPropertyDao.fetchAllProperties();
    }

    public LiveData<Property> fetchProperty(long propertyId) {
        return mPropertyDao.fetchProperty(propertyId);
    }

    public void insert(Property property) {
        mPropertyDao.insert(property);
    }

    public void delete(Property property) {
        mPropertyDao.delete(property.getId());
    }
}
