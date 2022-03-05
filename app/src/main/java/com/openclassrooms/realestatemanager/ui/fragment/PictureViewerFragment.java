package com.openclassrooms.realestatemanager.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.ViewModelFactory;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyListViewModel;
import com.openclassrooms.realestatemanager.databinding.FragmentPictureViewerBinding;

import java.util.ArrayList;
import java.util.List;

public class PictureViewerFragment extends Fragment {

    private PropertyListViewModel mPropertyListViewModel;
    private List<PropertyPicture> mPictures = new ArrayList<>();
    private FragmentPictureViewerBinding mBinding;
    private PictureViewerAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPictureViewerBinding.inflate(inflater, container, false);
        mPropertyListViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyListViewModel.class);

        initViewPager();
        initObservers();
        goToSelectedPictureRowIndex();

        return mBinding.getRoot();
    }

    private void initViewPager() {
        mAdapter = new PictureViewerAdapter(mPictures);
        mBinding.viewPager.setAdapter(mAdapter);
    }

    private void initObservers() {
        mPropertyListViewModel.getCurrentPropertyPictures().observe(getViewLifecycleOwner(), pictures -> {
            mPictures.clear();
            mPictures.addAll(pictures);
            mAdapter.notifyDataSetChanged();
        });
    }

    private void goToSelectedPictureRowIndex() {
        int pictureRowIndex = PictureViewerFragmentArgs.fromBundle(requireArguments()).getPictureRowIndex();
        if (pictureRowIndex != -1) mBinding.viewPager.setCurrentItem(pictureRowIndex);
    }
}
