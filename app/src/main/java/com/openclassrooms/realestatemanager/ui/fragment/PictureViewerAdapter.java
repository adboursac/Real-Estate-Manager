package com.openclassrooms.realestatemanager.ui.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.databinding.PictureViewerItemBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

public class PictureViewerAdapter extends RecyclerView.Adapter<PictureViewerAdapter.ViewHolder> {

    private final List<PropertyPicture> mPictures;

    public PictureViewerAdapter(List<PropertyPicture> pictures) {
        mPictures = pictures;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PictureViewerItemBinding mBinding;

        public ViewHolder(PictureViewerItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    @NonNull
    @Override
    public PictureViewerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PictureViewerAdapter.ViewHolder(PictureViewerItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewerAdapter.ViewHolder holder, int position) {
        PropertyPicture picture = mPictures.get(position);

        holder.mBinding.description.setText(picture.getDescription());
        Utils.setPicture(picture.getUri(), holder.mBinding.picture);
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }
}
