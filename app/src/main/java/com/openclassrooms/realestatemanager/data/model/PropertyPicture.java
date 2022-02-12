package com.openclassrooms.realestatemanager.data.model;

public class PropertyPicture {

    private long id;
    private long propertyId;
    private String description;
    private String uri;

    public PropertyPicture(long id, long propertyId, String description, String uri) {
        this.id = id;
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
