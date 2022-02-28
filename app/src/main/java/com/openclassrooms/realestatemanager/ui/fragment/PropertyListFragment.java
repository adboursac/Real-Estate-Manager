package com.openclassrooms.realestatemanager.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ViewModelFactory;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.viewmodel.PropertyListViewModel;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyListBinding;

import java.util.ArrayList;
import java.util.List;

public class PropertyListFragment extends Fragment implements CommandSelectProperty {

    private FragmentPropertyListBinding mBinding;
    private PropertyListViewModel mPropertyListViewModel;
    private RecyclerView mRecyclerView;
    private List<Property> mProperties = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPropertyListBinding.inflate(inflater, container, false);
        mPropertyListViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PropertyListViewModel.class);

        initRecyclerView();
        initObservers();

        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        mRecyclerView = mBinding.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        PropertyListAdapter mAdapter = new PropertyListAdapter(mProperties, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initObservers() {
        mPropertyListViewModel.fetchAllProperties().observe(requireActivity(), properties -> {
            mProperties.clear();
            mProperties.addAll(properties);
            if (mRecyclerView.getAdapter() != null) mRecyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    @Override
    public void selectProperty(Property property) {
        mPropertyListViewModel.setCurrentProperty(property);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.property_list_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_property_button:
                Navigation.findNavController(getView()).navigate(R.id.propertyAddFragment);
                break;
            case R.id.search_property_button:
                // Navigate to search property fragment
                break;
            default:
                Log.w("MeetingListFragment", "onOptionsItemSelected: didn't match any menu item");
        }
        return true;
    }
}
