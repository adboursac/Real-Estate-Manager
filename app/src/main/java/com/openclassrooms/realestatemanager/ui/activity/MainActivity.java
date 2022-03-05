package com.openclassrooms.realestatemanager.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private NavController mNavController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNavigationComponents();
        ConfigureNavigationComponentsDisplayRules();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Set toolbar_real_estate_list label instead of app name as toolbar title when MainActivity start
        mBinding.toolbar.setTitle(R.string.toolbar_properties_list);
    }

    @Override
    ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    private void initNavigationComponents() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment == null) return;
        mNavController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(mNavController.getGraph())
                .setOpenableLayout(mBinding.drawerLayout)
                .build();

        setSupportActionBar(mBinding.toolbar);

        NavigationUI.setupWithNavController(mBinding.drawerContent, mNavController);
        NavigationUI.setupWithNavController(mBinding.toolbar, mNavController, appBarConfiguration);
    }


    /**
     * Hide toolbar for specifics fragments
     * ( pictureManagerFragment, pictureViewerFragment)
     */
    @SuppressLint("NonConstantResourceId")
    private void ConfigureNavigationComponentsDisplayRules() {
        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.pictureManagerFragment:
                case R.id.pictureManagerEditFragment:
                case R.id.pictureViewerFragment:
                    showToolbar(false);
                    break;
                default:
                    showToolbar(true);
            }
        });
    }

    /**
     * Set visibility of toolbar
     *
     * @param visible true show both toolbar and bottom navigation
     */
    private void showToolbar(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        mBinding.toolbar.setVisibility(visibility);
    }

    @SuppressWarnings("all")
    //SuppressWarnings: as graph has a start destination mNavController.getCurrentDestination() won't be null
    @Override
    public void onBackPressed() {
        //Close Drawer if it's open
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START))
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);

        else if (mNavController.getCurrentDestination().getId() != R.id.propertyListFragment) super.onBackPressed();
    }
}