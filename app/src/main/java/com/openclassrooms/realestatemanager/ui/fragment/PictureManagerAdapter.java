package com.openclassrooms.realestatemanager.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.data.model.PropertyPicture;
import com.openclassrooms.realestatemanager.databinding.PictureManagerItemBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

public class PictureManagerAdapter extends RecyclerView.Adapter<PictureManagerAdapter.ViewHolder> {

    private final List<PropertyPicture> mPictures;
    private CommandPictureManager mCommandPictureManager;

    public PictureManagerAdapter(List<PropertyPicture> pictures, CommandPictureManager commandPictureManager) {
        mPictures = pictures;
        mCommandPictureManager = commandPictureManager;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PictureManagerItemBinding mBinding;

        public ViewHolder(PictureManagerItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    @NonNull
    @Override
    public PictureManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PictureManagerAdapter.ViewHolder(PictureManagerItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PictureManagerAdapter.ViewHolder holder, int position) {
        PropertyPicture picture = mPictures.get(position);

        holder.mBinding.description.setText(picture.getDescription());
        Utils.setPicture(picture.getUri(), holder.mBinding.picture);
        displayMainPictureIcon(holder.mBinding.isMainPicture, position);
        initEditButton(holder.mBinding.description, position);
    }

    private void displayMainPictureIcon(View view, int position) {
        if (mCommandPictureManager.getMainPictureIndex() == position) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }

    private void initEditButton(TextInputEditText textInput, int position) {
        textInput.setOnEditorActionListener((view, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                updateDescription(position, textInput.getText().toString());
                // clear text input focus
                textInput.setEnabled(false);
                textInput.setEnabled(true);
            }
            return false;
        });
    }

    private void updateDescription(int position, String description) {
        mCommandPictureManager.setPictureDescription(position, description);
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }
}
