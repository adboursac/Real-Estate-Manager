package com.openclassrooms.realestatemanager.ui.fragment;

import android.net.Uri;

public interface CommandPictureManager {
    int getMainPictureIndex();
    void setMainPictureIndex(int pictureRowIndex);
    void receivePictureUri(Uri uri);
    void setPictureDescription(int pictureRowIndex, String description);
    void deletePicture(int pictureRowIndex);
}