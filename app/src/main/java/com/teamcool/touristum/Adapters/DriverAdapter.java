package com.teamcool.touristum.Adapters;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.DriverTrips;

import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> {

    private List<DriverTrips> TripList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView clientName, clientNumber,  startDate,endDate, startLocation, endLocation, distance;

        public MyViewHolder(View view) {
            super(view);
            clientName= (TextView) view.findViewById(R.id.textView);
            clientNumber = (TextView) view.findViewById(R.id.textView2);
            startDate=(TextView) view.findViewById(R.id.textView3);
            endDate=(TextView) view.findViewById(R.id.textView4);
            startLocation=(TextView) view.findViewById(R.id.textView5);
            endLocation=(TextView) view.findViewById(R.id.textView6);
            distance = (TextView) view.findViewById(R.id.textView7);
        }
    }


    public DriverAdapter(List<DriverTrips> TripList) {
        this.TripList = TripList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_trip_row_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DriverTrips driverTrips = TripList.get(position);
        holder.clientName.setText(driverTrips.getClientName());
        holder.clientNumber.setText(driverTrips.getClientNumber());
        holder.startDate.setText(driverTrips.getStartDate());
        holder.endDate.setText(driverTrips.getEndDate());
        holder.startLocation.setText(driverTrips.getStartLocation());
        holder.endLocation.setText(driverTrips.getEndLocation());
        holder.distance.setText(driverTrips.getDistance());

    }

    @Override
    public int getItemCount() {
        return TripList.size();
    }
}