package com.openclassrooms.realestatemanager.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ViewModelFactory;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyAddViewModel;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyEditorBinding;
import com.openclassrooms.realestatemanager.utils.DatePicker;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static androidx.navigation.Navigation.*;

public class PropertyAddFragment extends Fragment implements CommandPictureManager {

    private PropertyAddViewModel mPropertyAddViewModel;
    private FragmentPropertyEditorBinding mBinding;
    private RecyclerView mRecyclerView;
    private ArrayAdapter<String> mPropertyTypeDropMenuAdapter;
    private PropertyAddAdapter mPropertyAddAdapter;
    private List<PropertyPicture> mPictures = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPropertyEditorBinding.inflate(inflater, container, false);
        mPropertyAddViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyAddViewModel.class);

        initPicturesRecyclerView();
        initObservers();
        initTypeDropDownMenu();
        initAvailableCheckBox();
        initSaveButton();
        initDateButtons();

        return mBinding.getRoot();
    }

    private void initObservers() {
        mPropertyAddViewModel.getCurrentProperty().observe(requireActivity(), property -> {
            updateInputs(property);
            mPropertyAddAdapter.notifyDataSetChanged();
        });

        mPropertyAddViewModel.getMainPictureRowIndex().observe(requireActivity(), index -> mPropertyAddAdapter.notifyDataSetChanged());

        mPropertyAddViewModel.getCurrentPropertyPictures().observe(requireActivity(), pictures -> {
            mPictures.clear();
            mPictures.addAll(pictures);
            mPropertyAddAdapter.notifyDataSetChanged();
            updateRecyclerViewLabel();
        });
    }

    private void initPicturesRecyclerView() {
        mRecyclerView = mBinding.pictures;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mPropertyAddAdapter = new PropertyAddAdapter(mPictures, this);
        mRecyclerView.setAdapter(mPropertyAddAdapter);

        mBinding.addPictureButton.setOnClickListener(view -> findNavController(view).navigate(R.id.pictureManagerFragment));
    }

    private void updateRecyclerViewLabel() {
        if (mPictures.size() > 0) mBinding.addPictureButton.setVisibility(View.GONE);
        else mBinding.addPictureButton.setVisibility(View.VISIBLE);
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

    private void initSaveButton() {
        mBinding.button.setOnClickListener(this::saveProperty);
    }

    private void saveProperty(View view) {
        if (! verifyInputs()) {
            Toast.makeText(requireActivity(), getString(R.string.property_not_correct), Toast.LENGTH_LONG).show();
            return;
        }

        if (requireAtLeastOnePicture()) return;

        Property property = generatePropertyFromInputsWithoutMainPicture();
        mPropertyAddViewModel.saveProperty(property);
        Toast.makeText(requireActivity(), getString(R.string.property_successfully_created), Toast.LENGTH_LONG).show();
        findNavController(view).navigate(R.id.propertyListFragment);
    }

    private boolean requireAtLeastOnePicture() {
        if (mPictures.size() == 0) {
            Toast.makeText(requireActivity(), getString(R.string.property_at_least_one_picture), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    @SuppressWarnings("all")
    // Fields nullity is checked before we call this method.
    private Property generatePropertyFromInputsWithoutMainPicture() {
        return new Property(
                mBinding.type.getText().toString(),
                mBinding.district.getText().toString(),
                Integer.parseInt(mBinding.price.getText().toString()),
                Integer.parseInt(mBinding.surface.getText().toString()),
                Integer.parseInt(mBinding.numberOfRooms.getText().toString()),
                Integer.parseInt(mBinding.numberOfBedrooms.getText().toString()),
                Integer.parseInt(mBinding.numberOfBathrooms.getText().toString()),
                mBinding.description.getText().toString(),
                getMainPictureIndex(),
                "",
                mBinding.addressNumber.getText().toString(),
                mBinding.street.getText().toString(),
                mBinding.postalCode.getText().toString(),
                mBinding.city.getText().toString(),
                mBinding.poiSwimmingPool.isChecked(),
                mBinding.poiSchool.isChecked(),
                mBinding.poiShopping.isChecked(),
                mBinding.poiParking.isChecked(),
                mBinding.available.isChecked(),
                mBinding.listedDate.getText().toString(),
                mBinding.soldDate.getText().toString(),
                mBinding.realEstateAgent.getText().toString()
        );
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
        mBinding.type.setText(property.getType());
        mBinding.district.setText(property.getDistrict());
        mBinding.surface.setText(Utils.integerString(property.getSurface()));
        mBinding.price.setText(Utils.integerString(property.getPrice()));
        mBinding.numberOfRooms.setText(Utils.integerString(property.getNumberOfRooms()));
        mBinding.numberOfBathrooms.setText(Utils.integerString(property.getNumberOfBathrooms()));
        mBinding.numberOfBedrooms.setText(Utils.integerString(property.getNumberOfBedrooms()));
        mBinding.addressNumber.setText(property.getAddressNumber());
        mBinding.street.setText(property.getStreet());
        mBinding.postalCode.setText(property.getPostalCode());
        mBinding.city.setText(property.getCity());

        mBinding.poiSwimmingPool.setChecked(property.hasPoiSwimmingPool());
        mBinding.poiParking.setChecked(property.hasPoiParking());
        mBinding.poiSchool.setChecked(property.hasPoiSchool());
        mBinding.poiShopping.setChecked(property.hasPoiShopping());

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


    @Override
    public int getMainPictureIndex() {
        Integer pictureIndex = mPropertyAddViewModel.getMainPictureRowIndex().getValue();
        if (pictureIndex == null) return -1;
        return pictureIndex;
    }

    @Override
    public void setMainPictureIndex(int pictureRowIndex) {
        // not used
    }

    @Override
    public void receivePictureUri(Uri uri) {
        // not used
    }

    @Override
    public void setPictureDescription(int pictureRowIndex, String description) {
        // not used
    }

    @Override
    public void deletePicture(int pictureRowIndex) {
        // not used
    }
}
