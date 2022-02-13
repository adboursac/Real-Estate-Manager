package com.openclassrooms.realestatemanager.data.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.data.model.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> fetchAllProperties();

    @Insert
    long insert(Property property);

    @Query("DELETE FROM Property WHERE property_id = :propertyId")
    int delete(long propertyId);

    @Query("DELETE FROM Property")
    void deleteAll();
}
