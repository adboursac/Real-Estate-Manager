package com.openclassrooms.realestatemanager.ui.fragment;

import android.view.View;

import androidx.navigation.Navigation;

import com.openclassrooms.realestatemanager.data.model.PropertyPicture;

import java.util.List;

public class PropertyEditAdapter extends PropertyDetailsAdapter {

    private final CommandPictureManager mCommandPictureManager;

    public PropertyEditAdapter(List<PropertyPicture> pictures, CommandPictureManager commandPictureManager) {
        super(pictures);
        mCommandPictureManager = commandPictureManager;
    }

    @Override
    public void onItemClick(View view, int position) {
        Navigation.findNavController(view).navigate(
                PropertyEditFragmentDirections.fromEditFragmentToPictureManagerEdit().setPictureRowIndex(position)
        );
    }

    @Override
    public void displayMainPictureIcon(View view, int position) {
        if (mCommandPictureManager.getMainPictureIndex() == position) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }
}
