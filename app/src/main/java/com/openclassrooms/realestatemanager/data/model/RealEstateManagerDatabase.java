package com.openclassrooms.realestatemanager.data.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.data.model.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.model.dao.PropertyPictureDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Property.class, PropertyPicture.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    private static volatile RealEstateManagerDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // --- DAO ---
    public abstract PropertyDao propertyDao();

    public abstract PropertyPictureDao propertyPictureDao();

    public ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }

    // --- INSTANCE ---
    public static RealEstateManagerDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            RealEstateManagerDatabase.class, "RealEstateManagerDatabase.db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(RealEstateManagerDatabase::prepopulateDatabase);
        }
    };

    private static void prepopulateDatabase() {
        PropertyDao propertyDao = INSTANCE.propertyDao();
        PropertyPictureDao propertyPictureDao = INSTANCE.propertyPictureDao();
        propertyDao.deleteAll();
        propertyPictureDao.deleteAll();

        Property property1 = new Property(
                "Penthouse",
                "Manhattan",
                9000000,
                280,
                8,
                2,
                4,
                "Beautiful penthouse",
                0,
                "4",
                "Wall St",
                "10005",
                "New York",
                true,
                Utils.getTodayDate(),
                "",
                "Bill");

        Property property2 = new Property(
                "Apartment",
                "Brooklyn",
                700000,
                90,
                4,
                1,
                2,
                "Superb penthouse",
                0,
                "4",
                "Wall St",
                "10005",
                "New York",
                true,
                Utils.getTodayDate(),
                "",
                "Buck");

        Property property3 = new Property(
                "Loft",
                "Manhattan",
                1200000,
                100,
                2,
                1,
                1,
                "Insane loft",
                0,
                "4",
                "Wall St",
                "10005",
                "New York",
                true,
                Utils.getTodayDate(),
                "",
                "Samantha");

        long propertyId1 = propertyDao.insert(property1);
        long propertyId2 = propertyDao.insert(property2);
        long propertyId3 = propertyDao.insert(property3);

        long pictureId1 = propertyPictureDao.insert(new PropertyPicture(propertyId1, "Living room", ""));
        propertyPictureDao.insert(new PropertyPicture(propertyId1, "Bathroom", ""));
        propertyPictureDao.insert(new PropertyPicture(propertyId1, "Bedroom", ""));
        propertyPictureDao.insert(new PropertyPicture(propertyId1, "Bedroom", ""));
        propertyPictureDao.insert(new PropertyPicture(propertyId1, "Bedroom", ""));

        long pictureId2 = propertyPictureDao.insert(new PropertyPicture(propertyId2, "Living room", ""));

        long pictureId3 = propertyPictureDao.insert(new PropertyPicture(propertyId3, "Living room", ""));

        propertyDao.updateMainPicture(propertyId1, pictureId1);
        propertyDao.updateMainPicture(propertyId2, pictureId2);
        propertyDao.updateMainPicture(propertyId3, pictureId3);
    }
}
