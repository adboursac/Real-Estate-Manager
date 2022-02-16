package com.openclassrooms.realestatemanager.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.ViewModelFactory;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyListViewModel;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailsBinding;

import java.util.ArrayList;
import java.util.List;

public class PropertyDetailsFragment extends Fragment {

    private FragmentPropertyDetailsBinding mBinding;
    private RecyclerView mRecyclerView;
    private PropertyListViewModel mPropertyListViewModel;
    private List<PropertyPicture> mPictures = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPropertyDetailsBinding.inflate(inflater, container, false);
        mPropertyListViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyListViewModel.class);

        //currentPropertyId = PropertyDetailsFragmentArgs.fromBundle(requireArguments()).getPropertyId();

        //populateDetails(currentProperty);
        initPicturesRecyclerView();
        initObservers();

        return mBinding.getRoot();
    }

    private void initPicturesRecyclerView() {
        mRecyclerView = mBinding.pictures;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        PropertyPictureAdapter mAdapter = new PropertyPictureAdapter(mPictures);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initObservers() {
        mPropertyListViewModel.getCurrentProperty().observe(requireActivity(), this::populateDetails);

        mPropertyListViewModel.getCurrentPropertyPictures().observe(requireActivity(), pictures -> {
            mPictures.clear();
            mPictures.addAll(pictures);
            if (mRecyclerView.getAdapter() != null) mRecyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    private void populateDetails(Property property) {
        mBinding.surface.setText(Utils.surfaceString(property.getSurface()));
        mBinding.numberOfRooms.setText(Utils.integerString(property.getNumberOfRooms()));
        mBinding.numberOfBathrooms.setText(Utils.integerString(property.getNumberOfBathrooms()));
        mBinding.numberOfBedrooms.setText(Utils.integerString(property.getNumberOfBedrooms()));
        mBinding.location.setText(property.getAddress());
        mBinding.description.setText(property.getDescription());
        mBinding.listedDate.setText(property.getListedDate());
        mBinding.realEstateAgent.setText(property.getRealEstateAgent());
    }
}
