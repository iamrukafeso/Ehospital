package com.ehospital.ui.country;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.ehospital.R;

import java.util.List;

public class DashboardFragment extends Fragment {

    RecyclerView rvCovidCountry;
    ProgressBar progressBar;
    ContryTrackerAdapter contryTrackerAdapter;

    private static final String TAG = DashboardFragment.class.getSimpleName();
    List<CountyPopulation> covidCountries;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_country, container, false);

        return root;
    }
}
