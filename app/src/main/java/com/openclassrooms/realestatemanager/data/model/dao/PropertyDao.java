package com.openclassrooms.realestatemanager.data.model.dao;

import android.database.Cursor;

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

    @Query("SELECT * FROM Property WHERE property_id = :propertyId LIMIT 1")
    LiveData<Property> fetchProperty(long propertyId);

    @Query("SELECT * FROM Property WHERE property_id = :propertyId")
    Cursor getPropertyWithCursor(long propertyId);

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

    @Query("SELECT * FROM Property " +
            "WHERE (:type IS NULL OR type = :type)" +
            " AND (:district IS NULL OR district LIKE :district)" +
            " AND (" +
            "   (:minPrice IS NULL AND :maxPrice IS NULL )" +
            "   OR (:minPrice IS NULL AND price <= :maxPrice )" +
            "   OR (:maxPrice IS NULL AND price >= :minPrice )" +
            "   OR price BETWEEN :minPrice AND :maxPrice)" +
            " AND (" +
            "   (:minSurface IS NULL AND :maxSurface IS NULL )" +
            "   OR (:minSurface IS NULL AND surface <= :maxSurface )" +
            "   OR (:maxSurface IS NULL AND surface >= :minSurface )" +
            "   OR surface BETWEEN :minSurface AND :maxSurface)" +
            " AND (" +
            "   (:minRooms IS NULL AND :maxRooms IS NULL )" +
            "   OR (:minRooms IS NULL AND numberOfRooms <= :maxRooms )" +
            "   OR (:maxRooms IS NULL AND numberOfRooms >= :minRooms )" +
            "   OR numberOfRooms BETWEEN :minRooms AND :maxRooms)" +
            " AND (NOT :hasSwimmingPool OR poiSwimmingPool LIKE :hasSwimmingPool)" +
            " AND (NOT :hasSchool OR poiSchool LIKE :hasSchool)" +
            " AND (NOT :hasShopping OR poiShopping LIKE :hasShopping)" +
            " AND (NOT :hasParking OR poiParking LIKE :hasParking)"
    )
    LiveData<List<Property>> searchProperty(String type,
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
                                            boolean hasParking);

    @Insert
    long insert(Property property);

    @Query("DELETE FROM Property WHERE property_id = :propertyId")
    int delete(long propertyId);

    @Query("DELETE FROM Property")
    void deleteAll();
}
