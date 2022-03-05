package com.openclassrooms.realestatemanager.data.repository;

import android.database.sqlite.SQLiteException;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.dao.PropertyDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class PropertyRepository {

    private final PropertyDao mPropertyDao;
    private final ExecutorService mExecutor;

    public PropertyRepository(PropertyDao propertyDao, ExecutorService executor) {
        mPropertyDao = propertyDao;
        mExecutor = executor;
    }

    public LiveData<List<Property>> fetchAllProperties() {
        return mPropertyDao.fetchAllProperties();
    }

    public LiveData<Property> fetchProperty(long propertyId) {
        return mPropertyDao.fetchProperty(propertyId);
    }

    public int updateMainPicture(long propertyId, long pictureId, String uri) {
        return mPropertyDao.updateMainPicture(propertyId, pictureId, uri);
    }

    public long insert(Property property) {
        return mPropertyDao.insert(property);
    }

    public void delete(Property property) {
        mPropertyDao.delete(property.getId());
    }

    public int updateProperty(Property property) {
        return mPropertyDao.updateProperty(property.getId(),
                property.getType(),
                property.getDistrict(),
                property.getPrice(),
                property.getSurface(),
                property.getNumberOfBedrooms(),
                property.getNumberOfBathrooms(),
                property.getNumberOfBedrooms(),
                property.getDescription(),
                property.getMainPictureId(),
                property.getMainPictureUri(),
                property.getAddressNumber(),
                property.getStreet(),
                property.getPostalCode(),
                property.getCity(),
                property.hasPoiSwimmingPool(),
                property.hasPoiSchool(),
                property.hasPoiShopping(),
                property.hasPoiParking(),
                property.isAvailable(),
                property.getListedDate(),
                property.getSoldDate(),
                property.getRealEstateAgent());
    }

    public LiveData<List<Property>> searchProperty(String type,
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
                                                   boolean hasParking) {
        return mPropertyDao.searchProperty(type,
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
                hasParking);
    }

    public ExecutorService getExecutor() {
        return mExecutor;
    }
}
