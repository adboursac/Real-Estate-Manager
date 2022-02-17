package com.openclassrooms.realestatemanager.ui.fragment;

import android.content.Context;
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
    private SelectPropertyCommand mSelectPropertyCommand;

    public PropertyListAdapter(List<Property> properties, SelectPropertyCommand selectPropertyCommand) {
        mProperties = properties;
        mSelectPropertyCommand = selectPropertyCommand;
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
        Context context = MainApplication.getApplication();
        //if (property.getMainPictureId() != 0)

        holder.mBinding.type.setText(property.getType().toString());
        holder.mBinding.district.setText(property.getDistrict());
        holder.mBinding.price.setText(Utils.dollarString(property.getPrice()));

        renderItem(holder.itemView, holder.mBinding.price, false);
        holder.itemView.setOnClickListener(view -> selectItem(view, holder.mBinding.price, property));
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }

    private void selectItem(View view, TextView priceTextView, Property property) {
        renderItem(view, priceTextView, true);
        mSelectPropertyCommand.selectProperty(property);
        Navigation.findNavController(view).navigate(R.id.navigateFromPropertyListToDetails);
    }

    private void renderItem(View view, TextView priceTextView, boolean selected) {
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
