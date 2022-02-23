package com.openclassrooms.realestatemanager.utils;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.ui.fragment.CommandPictureManager;

import static android.app.Activity.RESULT_OK;

public class CameraHelper {

    private ActivityResultLauncher<Intent> mLauncher;
    private Fragment mFragment;
    private Uri mResultUri;

    public CameraHelper(Fragment fragment, CommandPictureManager commandReceivePictureUri) {
        mFragment = fragment;
        mLauncher = mFragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        commandReceivePictureUri.receivePictureUri(mResultUri);
                    }
                });
    }

    public void startCamera() {
        //TODO check :  if (permissionGranted) {
        startCameraIntent();
    }

    private void startCameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        mResultUri = mFragment.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mResultUri);

        mLauncher.launch(cameraIntent);
    }
}
