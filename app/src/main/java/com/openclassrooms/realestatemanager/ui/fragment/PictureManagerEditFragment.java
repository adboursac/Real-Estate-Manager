package com.openclassrooms.realestatemanager.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ViewModelFactory;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyEditViewModel;
import com.openclassrooms.realestatemanager.databinding.FragmentPictureManagerBinding;
import com.openclassrooms.realestatemanager.utils.CameraHelper;
import com.openclassrooms.realestatemanager.utils.MediaStoreHelper;

import java.util.ArrayList;
import java.util.List;

public class PictureManagerEditFragment extends Fragment implements CommandPictureManager {

    private PropertyEditViewModel mPropertyEditViewModel;
    private List<PropertyPicture> mPictures = new ArrayList<>();
    private FragmentPictureManagerBinding mBinding;
    private CameraHelper mCameraHelper;
    private MediaStoreHelper mMediaStoreHelper;
    private PictureManagerAddAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPictureManagerBinding.inflate(inflater, container, false);
        mPropertyEditViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyEditViewModel.class);

        initViewPager();
        initObservers();
        initDeleteButton();
        initSetMainPictureButton();
        initLibraryButton();
        initCameraButton();
        goToSelectedPictureRowIndex();

        return mBinding.getRoot();
    }

    private void initObservers() {
        mPropertyEditViewModel.getCurrentPropertyPictures().observe(requireActivity(), pictures -> {
            mPictures.clear();
            mPictures.addAll(pictures);
            mAdapter.notifyDataSetChanged();

            if (mPictures.size() > 0) mBinding.noPictureLabel.setVisibility(View.GONE);
            else mBinding.noPictureLabel.setVisibility(View.VISIBLE);

            mBinding.viewPager.setCurrentItem(mPictures.size() - 1);
        });

        mPropertyEditViewModel.getMainPictureRowIndex().observe(requireActivity(), index -> mAdapter.notifyDataSetChanged());
    }

    private void initViewPager() {
        mAdapter = new PictureManagerAddAdapter(mPictures, this);
        mBinding.viewPager.setAdapter(mAdapter);
    }

    private void goToSelectedPictureRowIndex() {
        int pictureRowIndex = PictureManagerEditFragmentArgs.fromBundle(requireArguments()).getPictureRowIndex();
        if (pictureRowIndex != -1) mBinding.viewPager.setCurrentItem(pictureRowIndex);
    }

    private void initSetMainPictureButton() {
        mBinding.setMainButton.setOnClickListener(view -> {
            mPropertyEditViewModel.setMainPictureRowIndex(mBinding.viewPager.getCurrentItem());
            mAdapter.notifyDataSetChanged();
        });
    }

    private void initDeleteButton() {
        mBinding.deleteButton.setOnClickListener(view -> {
            mPropertyEditViewModel.deletePicture(mBinding.viewPager.getCurrentItem());
            mAdapter.notifyDataSetChanged();
        });
    }

    private void initCameraButton() {
        mCameraHelper = new CameraHelper(this, this);
        mBinding.cameraButton.setOnClickListener(view -> mCameraHelper.startCamera());
    }

    private void initLibraryButton() {
        mMediaStoreHelper = new MediaStoreHelper(this, this);
        mBinding.libraryButton.setOnClickListener(view -> mMediaStoreHelper.openGallery());
    }

    @Override
    public int getMainPictureIndex() {
        Integer pictureIndex = mPropertyEditViewModel.getMainPictureRowIndex().getValue();
        if (pictureIndex == null) return -1;
        return pictureIndex;
    }

    @Override
    public void setMainPictureIndex(int pictureRowIndex) {
        mPropertyEditViewModel.setMainPictureRowIndex(pictureRowIndex);
    }

    @Override
    public void receivePictureUri(Uri uri) {
        mPropertyEditViewModel.addPicture(new PropertyPicture(0, getString(R.string.picture_description), uri.toString()));
    }

    @Override
    public void setPictureDescription(int pictureRowIndex, String description) {
        mPropertyEditViewModel.setPictureDescription(pictureRowIndex, description);
    }

    @Override
    public void deletePicture(int pictureRowIndex) {
        mPropertyEditViewModel.deletePicture(pictureRowIndex);
    }
}
