package com.openclassrooms.realestatemanager.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.ViewModelFactory;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyEditorViewModel;
import com.openclassrooms.realestatemanager.databinding.FragmentPictureManagerBinding;

public class PictureManagerFragment extends Fragment {

    private PropertyEditorViewModel mPropertyEditorViewModel;
    FragmentPictureManagerBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPictureManagerBinding.inflate(inflater, container, false);
        mPropertyEditorViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyEditorViewModel.class);

        return mBinding.getRoot();
    }
}
