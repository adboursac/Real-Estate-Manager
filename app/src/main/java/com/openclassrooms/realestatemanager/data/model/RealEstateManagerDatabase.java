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
    }
}
