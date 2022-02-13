package com.openclassrooms.realestatemanager.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Property.class,
        parentColumns = "property_id",
        childColumns = "property_id"))
public class PropertyPicture {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_picture_id")
    private long id;

    @ColumnInfo(name = "property_id", index = true)
    private long propertyId;

    private String description;
    private String uri;

    public PropertyPicture(long propertyId, String description, String uri) {
        this.propertyId = propertyId;
        this.description = description;
        this.uri = uri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
