package com.openclassrooms.realestatemanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.data.model.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.data.repository.PropertyPictureRepository;
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyAddViewModel;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyListViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static volatile ViewModelFactory sFactory;

    @NonNull
    private final PropertyRepository mPropertyRepository;
    @NonNull
    private final PropertyPictureRepository mPropertyPictureRepository;

    public static ViewModelFactory getInstance(Context context) {
        if (sFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (sFactory == null) {
                    sFactory = new ViewModelFactory(context);
                    System.out.println("Just Created "+context.getClass());
                }
            }
        }
        return sFactory;
    }

    private ViewModelFactory(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getDatabase(context);

        mPropertyRepository = new PropertyRepository(database.propertyDao(), database.getDatabaseWriteExecutor());
        mPropertyPictureRepository = new PropertyPictureRepository(database.propertyPictureDao(), database.getDatabaseWriteExecutor());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyListViewModel.class)) {
            return (T) new PropertyListViewModel(mPropertyRepository, mPropertyPictureRepository);
        }
        else if (modelClass.isAssignableFrom(PropertyAddViewModel.class)) {
            return (T) new PropertyAddViewModel(mPropertyRepository, mPropertyPictureRepository);
        }
    throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
