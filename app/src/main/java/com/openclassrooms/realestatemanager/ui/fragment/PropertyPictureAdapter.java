package com.openclassrooms.realestatemanager.ui.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.databinding.PropertyPictureItemBinding;

import java.util.List;

public class PropertyPictureAdapter extends RecyclerView.Adapter<PropertyPictureAdapter.ViewHolder> {

    private final List<PropertyPicture> mPictures;

    public PropertyPictureAdapter(List<PropertyPicture> pictures) {
        mPictures = pictures;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PropertyPictureItemBinding mBinding;

        public ViewHolder(PropertyPictureItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    @NonNull
    @Override
    public PropertyPictureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PropertyPictureAdapter.ViewHolder(PropertyPictureItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyPictureAdapter.ViewHolder holder, int position) {
        PropertyPicture picture = mPictures.get(position);

        //if (picture.getUri() != null)
        holder.mBinding.description.setText(picture.getDescription());
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }
}
