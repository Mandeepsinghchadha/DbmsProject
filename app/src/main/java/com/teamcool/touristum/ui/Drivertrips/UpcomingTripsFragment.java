package com.teamcool.touristum.ui.Drivertrips;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamcool.touristum.Adapters.DriverAdapter;
import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.DriverTrips;

import java.util.ArrayList;
import java.util.List;

public class UpcomingTripsFragment extends Fragment {
    private List<DriverTrips> driverList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DriverAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_upcoming_trips, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        mAdapter = new DriverAdapter(driverList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareTripData();
        return  root;
    }

    private void prepareTripData() {
        DriverTrips trips = new DriverTrips("abc", "def", "ggh","hh","uui","kko","iio");
        driverList.add(trips);

        DriverTrips trips1 = new DriverTrips("abc", "def", "ggh","hh","uui","kko","iio");
        driverList.add(trips1);

        DriverTrips trips2 = new DriverTrips("abc", "def", "ggh","hh","uui","kko","iio");
        driverList.add(trips2);

        DriverTrips trips3 = new DriverTrips("abc", "def", "ggh","hh","uui","kko","iio");
        driverList.add(trips3);

        DriverTrips trips4 = new DriverTrips("abc", "def", "ggh","hh","uui","kko","iio");
        driverList.add(trips4);


    }
}