package com.openclassrooms.realestatemanager.data.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.data.model.PropertyPicture;

import java.util.List;

@Dao
public interface PropertyPictureDao {

    @Query("SELECT * FROM PropertyPicture")
    LiveData<List<PropertyPicture>> fetchAllPictures();

    @Insert
    long insert(PropertyPicture propertyPicture);

    @Query("DELETE FROM PropertyPicture WHERE property_picture_id = :propertyPictureId")
    int delete(long propertyPictureId);
}
