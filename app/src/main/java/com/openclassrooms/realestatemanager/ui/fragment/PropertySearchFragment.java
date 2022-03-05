package com.openclassrooms.realestatemanager.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ViewModelFactory;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyListViewModel;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertySearchBinding;

public class PropertySearchFragment extends Fragment {

    private PropertyListViewModel mPropertyListViewModel;
    private FragmentPropertySearchBinding mBinding;
    private ArrayAdapter<String> mPropertyTypeDropMenuAdapter;

    private String type;
    private String district;
    private Integer priceMin;
    private Integer priceMax;
    private Integer surfaceMin;
    private Integer surfaceMax;
    private Integer roomsMin;
    private Integer roomsMax;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPropertySearchBinding.inflate(inflater, container, false);
        mPropertyListViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyListViewModel.class);

        initTypeDropDownMenu();
        initSearchButton();

        return mBinding.getRoot();
    }

    private void initSearchButton () {
        mBinding.button.setOnClickListener(view -> searchProperty());
    }

    private void initTypeDropDownMenu() {
        String[] types = getResources().getStringArray(R.array.property_types);
        mPropertyTypeDropMenuAdapter = new ArrayAdapter<>(requireContext(), R.layout.support_simple_spinner_dropdown_item, types);
        mBinding.type.setAdapter(mPropertyTypeDropMenuAdapter);
    }

    public void searchProperty() {
        type = mBinding.type.getText().toString().isEmpty() ? null : mBinding.type.getText().toString();
        district = mBinding.district.getText().toString().isEmpty() ? null : mBinding.district.getText().toString();
        priceMin = mBinding.priceMin.getText().toString().isEmpty() ? null : Integer.parseInt(mBinding.priceMin.getText().toString());
        priceMax = mBinding.priceMax.getText().toString().isEmpty() ? null : Integer.parseInt(mBinding.priceMax.getText().toString());
        surfaceMin = mBinding.surfaceMin.getText().toString().isEmpty() ? null : Integer.parseInt(mBinding.surfaceMin.getText().toString());
        surfaceMax = mBinding.surfaceMax.getText().toString().isEmpty() ? null : Integer.parseInt(mBinding.surfaceMax.getText().toString());
        roomsMin = mBinding.numberOfRoomsMin.getText().toString().isEmpty() ? null : Integer.parseInt(mBinding.numberOfRoomsMin.getText().toString());
        roomsMax = mBinding.numberOfRoomsMax.getText().toString().isEmpty() ? null : Integer.parseInt(mBinding.numberOfRoomsMax.getText().toString());

        // We set MainActivity as lifecycle owner, because PropertySearchFragment will be deleted before search result
        mPropertyListViewModel.searchProperties(requireActivity(),
                type,
                district,
                priceMin,
                priceMax,
                surfaceMin,
                surfaceMax,
                roomsMin,
                roomsMax,
                mBinding.poiSwimmingPool.isChecked(),
                mBinding.poiSchool.isChecked(),
                mBinding.poiShopping.isChecked(),
                mBinding.poiParking.isChecked()
        );

        Navigation.findNavController(requireView()).navigate(R.id.propertyListFragment);
    }
}
