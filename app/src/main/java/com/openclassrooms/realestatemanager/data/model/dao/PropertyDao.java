package com.openclassrooms.realestatemanager.data.model.dao;

import android.database.sqlite.SQLiteException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.data.model.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> fetchAllProperties();

    @Query("SELECT * FROM Property WHERE property_id = :propertyId LIMIT 1")
    LiveData<Property> fetchProperty(long propertyId);

    @Query("UPDATE Property SET mainPictureId = :pictureId, mainPictureUri = :uri WHERE property_id =:propertyId")
    int updateMainPicture(long propertyId, long pictureId, String uri);

    @Query("UPDATE Property SET type = :type," +
            "district = :district," +
            "price = :price," +
            "surface = :surface," +
            "numberOfRooms = :numberOfRooms," +
            "numberOfBathrooms= :numberOfBathrooms," +
            "numberOfBedrooms = :numberOfBedrooms," +
            "description = :description," +
            "mainPictureId = :mainPictureId," +
            "mainPictureUri = :mainPictureUri," +
            "addressNumber = :addressNumber," +
            "street = :street," +
            "postalCode = :postalCode," +
            "city = :city," +
            "poiSwimmingPool = :poiSwimmingPool," +
            "poiSchool = :poiSchool," +
            "poiShopping = :poiShopping," +
            "poiParking = :poiParking," +
            "available = :available," +
            "listedDate = :listedDate," +
            "soldDate = :soldDate," +
            "realEstateAgent = :realEstateAgent WHERE property_id =:propertyId")
    int updateProperty(long propertyId,
                    String type,
                    String district,
                    int price,
                    int surface,
                    int numberOfRooms,
                    int numberOfBathrooms,
                    int numberOfBedrooms,
                    String description,
                    long mainPictureId,
                    String mainPictureUri,
                    String addressNumber,
                    String street,
                    String postalCode,
                    String city,
                    boolean poiSwimmingPool,
                    boolean poiSchool,
                    boolean poiShopping,
                    boolean poiParking,
                    boolean available,
                    String listedDate,
                    String soldDate,
                    String realEstateAgent
    );

    @Insert
    long insert(Property property);

    @Query("DELETE FROM Property WHERE property_id = :propertyId")
    int delete(long propertyId);

    @Query("DELETE FROM Property")
    void deleteAll();
}
