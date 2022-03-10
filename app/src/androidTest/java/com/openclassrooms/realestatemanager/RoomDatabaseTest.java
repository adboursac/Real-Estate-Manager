package com.openclassrooms.realestatemanager;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.model.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.data.model.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.model.dao.PropertyPictureDao;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RoomDatabaseTest {

    private RealEstateManagerDatabase db;
    private PropertyDao mPropertyDao;
    private PropertyPictureDao mPropertyPictureDao;
    private final long DUMMY_PROPERTY_ID = 1;
    private final Property DUMMY_PROPERTY = new Property(
            "House",
            "Bastille",
            340000,
            240,
            5,
            3,
            1,
            "description of house 12 rue de",
            0,
            "",
            "12",
            "rue de la roquette",
            "75012",
            "Paris",
            false,
            true,
            true,
            true,
            true,
            "20/01/2021",
            null,
            "Roger");
    private final PropertyPicture DUMMY_PICTURE_1 = new PropertyPicture(DUMMY_PROPERTY_ID, "picture_1", " ");
    private final PropertyPicture DUMMY_PICTURE_2 = new PropertyPicture(DUMMY_PROPERTY_ID, "picture_2", " ");

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context,
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();

        mPropertyDao = db.propertyDao();
        mPropertyPictureDao = db.propertyPictureDao();
    }

    @After
    public void closeDb() {
        db.clearAllTables();
    }

    @Test
    public void insertAndGetProperty() throws InterruptedException {
        // insert a DUMMY_PROPERTY
        mPropertyDao.insert(DUMMY_PROPERTY);
        // Assert it's in database
        Property property = LiveDataTestUtil.getValue(mPropertyDao.fetchProperty(DUMMY_PROPERTY_ID));
        assertTrue(property.getDescription().equals(DUMMY_PROPERTY.getDescription()) && property.getId() == DUMMY_PROPERTY_ID);
    }

    @Test
    public void getPropertyWhenNoItemInserted() throws InterruptedException {
        List<Property> properties = LiveDataTestUtil.getValue(mPropertyDao.fetchAllProperties());
        assertTrue(properties.isEmpty());
    }

    @Test
    public void insertAndUpdateProperty() throws InterruptedException {
        // Insert property
        mPropertyDao.insert(DUMMY_PROPERTY);
        // Update price
        int expectedPrice = 34;
        Property p = LiveDataTestUtil.getValue(mPropertyDao.fetchProperty(DUMMY_PROPERTY_ID));
        p.setPrice(expectedPrice);
        mPropertyDao.updateProperty(
                p.getId(),
                p.getType(),
                p.getDistrict(),
                p.getPrice(),
                p.getSurface(),
                p.getNumberOfRooms(),
                p.getNumberOfBathrooms(),
                p.getNumberOfBedrooms(),
                p.getDescription(),
                p.getMainPictureId(),
                p.getMainPictureUri(),
                p.getAddressNumber(),
                p.getStreet(),
                p.getPostalCode(),
                p.getCity(),
                p.hasPoiSwimmingPool(),
                p.hasPoiSchool(),
                p.hasPoiShopping(),
                p.hasPoiParking(),
                p.isAvailable(),
                p.getListedDate(),
                p.getSoldDate(),
                p.getRealEstateAgent());

                //verify it's correctly updated
                Property property = LiveDataTestUtil.getValue(mPropertyDao.fetchProperty(DUMMY_PROPERTY_ID));
        assertEquals(expectedPrice, property.getPrice());
    }

    //Illustration Dao
    @Test
    public void insertAndGetPictures() throws InterruptedException {
        // insert property and picture 1
        String expectedDescription = DUMMY_PICTURE_1.getDescription();
        mPropertyDao.insert(DUMMY_PROPERTY);
        mPropertyPictureDao.insert(DUMMY_PICTURE_1);
        mPropertyPictureDao.insert(DUMMY_PICTURE_2);

        // verify pictures are correctly inserted
        List<PropertyPicture> pictures = LiveDataTestUtil.getValue(mPropertyPictureDao.fetchPictures(DUMMY_PROPERTY_ID));
        assertEquals(2, pictures.size());
        assertEquals(expectedDescription, pictures.get(0).getDescription());
    }

    @Test
    public void getPicturesWhenNoItemInserted() throws InterruptedException {
        List<PropertyPicture> pictures = LiveDataTestUtil.getValue(mPropertyPictureDao.fetchPictures(DUMMY_PROPERTY_ID));
        TestCase.assertTrue(pictures.isEmpty());
    }
}
