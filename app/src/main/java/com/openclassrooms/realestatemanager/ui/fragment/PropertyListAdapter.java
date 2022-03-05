package com.openclassrooms.realestatemanager.ui.fragment;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyListItemBinding;

import java.util.List;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.ViewHolder> {

    private final List<Property> mProperties;
    private CommandSelectProperty mCommandSelectProperty;

    public PropertyListAdapter(List<Property> properties, CommandSelectProperty commandSelectProperty) {
        mProperties = properties;
        mCommandSelectProperty = commandSelectProperty;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FragmentPropertyListItemBinding mBinding;

        public ViewHolder(FragmentPropertyListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentPropertyListItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyListAdapter.ViewHolder holder, int position) {
        Property property = mProperties.get(position);

        holder.mBinding.type.setText(property.getType());
        holder.mBinding.district.setText(property.getDistrict());
        holder.mBinding.price.setText(Utils.dollarString(property.getPrice()));

        if (property.getMainPictureId() != -1) Utils.setPicture(property.getMainPictureUri(), holder.mBinding.picture);

        renderItemColors(holder.itemView, holder.mBinding.price, false);
        holder.itemView.setOnClickListener(view -> selectItem(view, holder.mBinding.price, property));
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }

    private void selectItem(View view, TextView priceTextView, Property property) {
        renderItemColors(view, priceTextView, true);
        mCommandSelectProperty.selectProperty(property);
        Navigation.findNavController(view).navigate(R.id.propertyDetailsFragment);
    }

    private void renderItemColors(View view, TextView priceTextView, boolean selected) {
        Resources resources = MainApplication.getContext().getResources();
        if (selected) {
            view.setBackgroundColor(resources.getColor(R.color.colorAccent));
            priceTextView.setTextColor(resources.getColor(R.color.white));
        } else {
            view.setBackgroundColor(resources.getColor(R.color.white));
            priceTextView.setTextColor(resources.getColor(R.color.colorAccent));
        }
    }
}
