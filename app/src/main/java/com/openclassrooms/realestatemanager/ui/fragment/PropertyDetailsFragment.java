package com.openclassrooms.realestatemanager.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyEditViewModel;
import com.openclassrooms.realestatemanager.utils.MapHelper;
import com.openclassrooms.realestatemanager.utils.Utils;
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
    private Property mProperty;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPropertyDetailsBinding.inflate(inflater, container, false);
        mPropertyListViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyListViewModel.class);

        initPicturesRecyclerView();
        initObservers();

        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    private void initPicturesRecyclerView() {
        mRecyclerView = mBinding.pictures;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        PropertyDetailsAdapter mAdapter = new PropertyDetailsAdapter(mPictures);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initObservers() {
        mPropertyListViewModel.getCurrentProperty().observe(getViewLifecycleOwner(), this::populateDetails);

        mPropertyListViewModel.getCurrentPropertyPictures().observe(getViewLifecycleOwner(), pictures -> {
            mPictures.clear();
            mPictures.addAll(pictures);
            if (mRecyclerView.getAdapter() != null) mRecyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    private void populateDetails(Property property) {
        mProperty = property;

        mBinding.surface.setText(Utils.surfaceString(property.getSurface()));
        mBinding.numberOfRooms.setText(Utils.integerString(property.getNumberOfRooms()));
        mBinding.numberOfBathrooms.setText(Utils.integerString(property.getNumberOfBathrooms()));
        mBinding.numberOfBedrooms.setText(Utils.integerString(property.getNumberOfBedrooms()));
        mBinding.poi.setText(mPropertyListViewModel.generatePoiString(property));
        mBinding.location.setText(property.getFullAddress());
        mBinding.description.setText(property.getDescription());
        mBinding.listedDate.setText(property.getListedDate());
        mBinding.realEstateAgent.setText(property.getRealEstateAgent());

        initMapButton();
        setMapPictureAsynchronously(property);
    }

    private void initMapButton() {
        mBinding.map.setOnClickListener(view ->
        Navigation.findNavController(view).navigate(R.id.mapFragment));
    }

    public void setMapPictureAsynchronously(Property property) {
        Handler mainHandler = new Handler(requireContext().getMainLooper());
        mPropertyListViewModel.getExecutorService().execute(() -> {
            String mapUrl = MapHelper.addressToStaticMapUrl(property.getFullAddress(), getContext());
            mainHandler.post(() -> {
                mBinding.noWifi.setVisibility(View.GONE);
                Utils.setPicture(mapUrl, mBinding.map);
            });
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.property_details_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editPropertyButton:
                navigateToEditFragment();
                break;
            case R.id.deletePropertyButton:
                mPropertyListViewModel.deleteCurrentProperty();
                Navigation.findNavController(requireView()).navigate(R.id.propertyListFragment);
                break;
            default:
                Log.w("MeetingListFragment", "onOptionsItemSelected: didn't match any menu item");
        }
        return true;
    }

    private void navigateToEditFragment() {
        // To avoid requesting already loaded data we prepare edit view model now
        PropertyEditViewModel editViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyEditViewModel.class);
        editViewModel.setCurrentPropertyState(mProperty);
        editViewModel.setCurrentPropertyPictures(mPictures);
        Navigation.findNavController(requireView()).navigate(R.id.propertyEditFragment);
    }
}
