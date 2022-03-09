package com.openclassrooms.realestatemanager.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.RealEstateManagerDatabase;

public class RealEstateManagerContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.data.provider";
    public static final String TABLE_NAME = Property.class.getSimpleName();
    public static final Uri URI_PROPERTY = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (getContext() != null) {
            long propertyId = ContentUris.parseId(uri);
            final Cursor cursor = RealEstateManagerDatabase.getDatabase(getContext()).propertyDao().getPropertyWithCursor(propertyId);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.property/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        if (getContext() != null) {
            final long id = RealEstateManagerDatabase.getDatabase(getContext()).propertyDao().insert(Property.buildWithContentValues(contentValues));
            if (id != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }

        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null) {
            final int count = RealEstateManagerDatabase.getDatabase(getContext()).propertyDao().delete(ContentUris.parseId(uri));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null) {
            final int count = RealEstateManagerDatabase.getDatabase(getContext()).propertyDao().updateProperty(contentValues.getAsLong("property_id"),
                    contentValues.getAsString("type"),
                    contentValues.getAsString("district"),
                    contentValues.getAsInteger("price"),
                    contentValues.getAsInteger("surface"),
                    contentValues.getAsInteger("numberOfRooms"),
                    contentValues.getAsInteger("numberOfBedrooms"),
                    contentValues.getAsInteger("numberOfBathrooms"),
                    contentValues.getAsString("description"),
                    contentValues.getAsLong("mainPictureId"),
                    contentValues.getAsString("mainPictureUri"),
                    contentValues.getAsString("addressNumber"),
                    contentValues.getAsString("street"),
                    contentValues.getAsString("postalCode"),
                    contentValues.getAsString("city"),
                    contentValues.getAsBoolean("poiSwimmingPool"),
                    contentValues.getAsBoolean("poiSchool"),
                    contentValues.getAsBoolean("poiShopping"),
                    contentValues.getAsBoolean("poiParking"),
                    contentValues.getAsBoolean("available"),
                    contentValues.getAsString("listedDate"),
                    contentValues.getAsString("soldDate"),
                    contentValues.getAsString("realEstateAgent"));

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }
}