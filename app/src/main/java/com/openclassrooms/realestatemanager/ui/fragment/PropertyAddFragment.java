package com.openclassrooms.realestatemanager.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ViewModelFactory;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyEditorViewModel;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyEditorBinding;
import com.openclassrooms.realestatemanager.utils.DatePicker;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PropertyAddFragment extends Fragment {

    private PropertyEditorViewModel mPropertyEditorViewModel;
    private FragmentPropertyEditorBinding mBinding;
    private RecyclerView mRecyclerView;
    private ArrayAdapter<String> mPropertyTypeDropMenuAdapter;
    private List<PropertyPicture> mPictures = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPropertyEditorBinding.inflate(inflater, container, false);
        mPropertyEditorViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyEditorViewModel.class);

        initPicturesRecyclerView();
        initObservers();
        initTypeDropDownMenu();
        initAvailableCheckBox();
        initCreateButton();
        initDateButtons();

        return mBinding.getRoot();
    }

    private void initObservers() {
        mPropertyEditorViewModel.getCurrentProperty().observe(requireActivity(), this::updateInputs);

        mPropertyEditorViewModel.getCurrentPropertyPictures().observe(requireActivity(), pictures -> {
            mPictures.clear();
            mPictures.addAll(pictures);
            if (mRecyclerView.getAdapter() != null) mRecyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    private void initPicturesRecyclerView() {
        mRecyclerView = mBinding.pictures;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        PropertyPictureAdapter mAdapter = new PropertyPictureAdapter(mPictures);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initDateButtons() {
        DatePicker.setDatePickerOnTextInput(mBinding.listedDate, getChildFragmentManager());
        DatePicker.setDatePickerOnTextInput(mBinding.soldDate, getChildFragmentManager());
    }

    private void updateAvailableRelatedViews(boolean available) {
        if (available) {
            mBinding.soldDateLayout.setVisibility(View.GONE);
        }
        else {
            mBinding.soldDate.setText("");
            mBinding.soldDateLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initAvailableCheckBox() {
        mBinding.available.setOnClickListener(view -> updateAvailableRelatedViews(mBinding.available.isChecked()));
    }

    private void initTypeDropDownMenu() {
        String[] types = getResources().getStringArray(R.array.property_types);
        mPropertyTypeDropMenuAdapter = new ArrayAdapter<>(requireContext(), R.layout.support_simple_spinner_dropdown_item, types);
        mBinding.type.setAdapter(mPropertyTypeDropMenuAdapter);
    }

    private void initCreateButton() {
        mBinding.button.setOnClickListener(this::createProperty);
    }

    private void createProperty(View view) {
        if (! verifyInputs()) {
            Toast.makeText(requireActivity(), getString(R.string.property_not_correct), Toast.LENGTH_LONG).show();
            return;
        }
        Property property = generatePropertyFromInputs();
        mPropertyEditorViewModel.saveProperty(property);
        Toast.makeText(requireActivity(), getString(R.string.property_successfully_created), Toast.LENGTH_LONG).show();
        Navigation.findNavController(view).navigate(R.id.propertyListFragment);
    }

    @SuppressWarnings("all")
    private Property generatePropertyFromInputs() {
        Property currentProperty = mPropertyEditorViewModel.getCurrentProperty().getValue();
        long mainPictureId = currentProperty != null ? currentProperty.getMainPictureId() : getMainPictureAutomatically();

        return new Property(
                mBinding.type.getText().toString(),
                mBinding.district.getText().toString(),
                Integer.parseInt(mBinding.price.getText().toString()),
                Integer.parseInt(mBinding.surface.getText().toString()),
                Integer.parseInt(mBinding.numberOfRooms.getText().toString()),
                Integer.parseInt(mBinding.numberOfBedrooms.getText().toString()),
                Integer.parseInt(mBinding.numberOfBathrooms.getText().toString()),
                mBinding.description.getText().toString(),
                mainPictureId,
                mBinding.addressNumber.getText().toString(),
                mBinding.street.getText().toString(),
                mBinding.postalCode.getText().toString(),
                mBinding.city.getText().toString(),
                mBinding.available.isChecked(),
                mBinding.listedDate.getText().toString(),
                mBinding.soldDate.getText().toString(),
                mBinding.realEstateAgent.getText().toString()
        );
    }

    private long getMainPictureAutomatically() {
        return mPictures.size() > 0 ? mPictures.get(0).getId() : 0L;
    }

    @SuppressWarnings("all")
    private boolean verifyInputs() {
        if (mBinding.type.getText().toString().isEmpty()) return false;
        if (mBinding.district.getText().toString().isEmpty()) return false;
        if (mBinding.price.getText().toString().isEmpty()) return false;
        if (mBinding.numberOfRooms.getText().toString().isEmpty()) return false;
        if (mBinding.numberOfBedrooms.getText().toString().isEmpty()) return false;
        if (mBinding.numberOfBathrooms.getText().toString().isEmpty()) return false;
        if (mBinding.description.getText().toString().isEmpty()) return false;

        if (mBinding.addressNumber.getText().toString().isEmpty()) return false;
        if (mBinding.street.getText().toString().isEmpty()) return false;
        if (mBinding.postalCode.getText().toString().isEmpty()) return false;
        if (mBinding.city.getText().toString().isEmpty()) return false;
        if (mBinding.listedDate.getText().toString().isEmpty()) return false;
        if (!mBinding.available.isChecked() && mBinding.soldDate.getText().toString().isEmpty()) return false;
        if (mBinding.realEstateAgent.getText().toString().isEmpty()) return false;
        if (mBinding.description.getText().toString().isEmpty()) return false;
        return true;
    }
    
    private void updateInputs(Property property) {
        if (property == null) return;
        mBinding.surface.setText(Utils.surfaceString(property.getSurface()));
        mBinding.numberOfRooms.setText(Utils.integerString(property.getNumberOfRooms()));
        mBinding.numberOfBathrooms.setText(Utils.integerString(property.getNumberOfBathrooms()));
        mBinding.numberOfBedrooms.setText(Utils.integerString(property.getNumberOfBedrooms()));
        mBinding.addressNumber.setText(property.getAddressNumber());
        mBinding.street.setText(property.getStreet());
        mBinding.postalCode.setText(property.getPostalCode());
        mBinding.city.setText(property.getCity());
        mBinding.description.setText(property.getDescription());
        if (property.isAvailable()) {
            mBinding.available.setChecked(true);
            mBinding.soldDateLayout.setVisibility(View.GONE);
        }
        else {
            mBinding.available.setChecked(false);
            mBinding.soldDate.setText(property.getSoldDate());
            mBinding.soldDateLayout.setVisibility(View.VISIBLE);
        }
        mBinding.listedDate.setText(property.getListedDate());
        mBinding.realEstateAgent.setText(property.getRealEstateAgent());
    }
}
