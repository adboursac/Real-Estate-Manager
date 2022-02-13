package com.openclassrooms.realestatemanager.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyListItemBinding;

import java.util.List;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.ViewHolder>{

    private Context mContext;
    private final List<Property> mProperties;

    public PropertyListAdapter(List<Property> properties) {
        mProperties = properties;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(FragmentPropertyListItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyListAdapter.ViewHolder holder, int position) {
        Property property = mProperties.get(position);

        if (property.getMainPictureId() == 0) holder.mBinding.picture.setImageDrawable(
                mContext.getDrawable(R.drawable.ic_baseline_no_photography_24)
        );

        holder.mBinding.type.setText(property.getType().toString());
        holder.mBinding.district.setText(property.getDistrict());
        holder.mBinding.price.setText(Utils.dollarString(property.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }
}
