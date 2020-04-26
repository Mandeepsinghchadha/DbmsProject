package com.teamcool.touristum.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.Agency;
import com.teamcool.touristum.data.model.Booking;
import com.teamcool.touristum.data.model.Client;
import com.teamcool.touristum.data.model.Hotel;
import com.teamcool.touristum.data.model.Package;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ManagerHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Booking> bookings;
    private ArrayList<Client> clients;
    private ArrayList<Package> packages;
    private ArrayList<Hotel> hotels;
    private ArrayList<Agency> agencies;

    public static final int VIEW_MODE_BOOKING = 1;
    public static final int VIEW_MODE_AGENCY = 2;
    public static final int VIEW_MODE_PACKAGE = 3;
    public static final int VIEW_MODE_HOTEL = 4;
    public static final int VIEW_MODE_CLIENT = 5;
    public static final String TAG = "ManagerHomeAdapter";
    private int VIEW_MODE;
    private Context context;

    private onBookingClickListener bookingClickListener;
    private onPackageClickListener packageClickListener;
    private onBookingLongClickListener bookingLongClickListener;

    public interface onBookingClickListener{
        void selectedBooking(Booking booking);
    }

    public interface onBookingLongClickListener{
        void selectedBooking(Booking booking);
    }

    public interface onPackageClickListener{
        void selectedPackage(Package pack);
    }

    public ManagerHomeAdapter(Context context, onBookingClickListener bookingClickListener,onBookingLongClickListener bookingLongClickListener,onPackageClickListener packageClickListener) {
        this.context = context;
        this.packageClickListener = packageClickListener;
        this.bookingClickListener = bookingClickListener;
        this.bookingLongClickListener = bookingLongClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: " + VIEW_MODE);
        if(VIEW_MODE == VIEW_MODE_BOOKING){
            View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_booking,parent,false);
            return new BookingsViewHolder(itemView);
        }
        else if(VIEW_MODE == VIEW_MODE_AGENCY){
            View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_agencies,parent,false);
            return new AgencyViewHolder(itemView);
        }
        else if(VIEW_MODE == VIEW_MODE_HOTEL){
            View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_hotels,parent,false);
            return new HotelViewHolder(itemView);
        }
        else if(VIEW_MODE == VIEW_MODE_CLIENT){
            View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_clients,parent,false);
            return new ClientViewHolder(itemView);
        }
        else{
            View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_packages,parent,false);
            return new PackageViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");

        if(VIEW_MODE == VIEW_MODE_BOOKING){
            Booking booking = bookings.get(position);
            BookingsViewHolder booking_holder = (BookingsViewHolder)holder;
            booking_holder.tv_name.setText("Client : " + booking.getClientName().toString() +"(Id : " + booking.getClientID() + ")");
            booking_holder.tv_city.setText("City : " + booking.getCity().toString() +"(Id : " + booking.getCityID() + ")");
            booking_holder.tv_price.setText("Price : "  + booking.getPrice().toString());
            booking_holder.tv_package.setText("Package : " + booking.getPackageType().toString() +"(Id : " + booking.getPackageID() + ")");
            booking_holder.tv_from.setText("From : " + booking.getFromDate().toString());
            booking_holder.tv_booking.setText("BookingID : " + booking.getBookingID().toString());
            booking_holder.tv_vehicle.setText("Vehicle : " + booking.getVehicleName().toString() + " (" + booking.getVehicleType() + ")(Id : " + booking.getVehicleID() + ")");
            booking_holder.tv_agency.setText("Agency : " + booking.getAgencyName().toString() +"(Id : " + booking.getAgencyID() + ")");
            booking_holder.tv_hotel.setText("Hotel : " + booking.getHotel().toString() +"(Id : " + booking.getHotelID() + ")");

        }
        else if(VIEW_MODE == VIEW_MODE_AGENCY){

            Agency agency = agencies.get(position);
            AgencyViewHolder agencyViewHolder = (AgencyViewHolder) holder;
            agencyViewHolder.tv_name.setText("Name : " + agency.getAgencyName() +"(Id : " + agency.getAgencyID() + ")");
            agencyViewHolder.tv_address.setText("Address : " + agency.getAgencyAddress());
            agencyViewHolder.tv_contact.setText("Contact : " + agency.getAgencyContact());
            agencyViewHolder.tv_package_count.setText("Packages : " + agency.getNoOfPackages());

        }
        else if(VIEW_MODE == VIEW_MODE_HOTEL){
            Hotel hotel = hotels.get(position);
            HotelViewHolder hotelViewHolder = (HotelViewHolder) holder;
            hotelViewHolder.tv_name.setText("Name : " + hotel.getHotelName() +"(Id : " + hotel.getHotelID() + ")");
            hotelViewHolder.tv_location.setText("Location : " + hotel.getHotelLocation() +"(Id : " + hotel.getLocationID() + ")");
            hotelViewHolder.tv_availability.setText("Availability : " + hotel.getAvailableRooms());
            hotelViewHolder.ratingBar.setRating(Float.parseFloat(hotel.getHotelRating()));
            hotelViewHolder.tv_city.setText("City : " + hotel.getHotelCity().toString() +"(Id : " + hotel.getCityID() + ")");
        }
        else if(VIEW_MODE == VIEW_MODE_CLIENT){
            Client client = clients.get(position);
            ClientViewHolder clientViewHolder = (ClientViewHolder) holder;
            clientViewHolder.tv_name.setText("Name : " + client.getClientName() +"(Id : " + client.getClientID() + ")");
            clientViewHolder.tv_bookings.setText("Bookings : " + client.getNoOfBookings());
            clientViewHolder.tv_contact.setText("Contact : " + client.getClientContact());
            clientViewHolder.tv_address.setText("Address : " + client.getClientAddress());
            clientViewHolder.tv_email.setText("Email : " + client.getClientEmail());

        }
        else{
            Package aPackage = packages.get(position);
            PackageViewHolder packageViewHolder = (PackageViewHolder) holder;
            packageViewHolder.tv_days.setText("Days : " + aPackage.getDays().toString());
            packageViewHolder.tv_city.setText("City : " + aPackage.getCity().toString() +"(Id : " + aPackage.getCityID() + ")");
            packageViewHolder.tv_price.setText("Price : "  + aPackage.getPrice().toString());
            packageViewHolder.tv_packege.setText("Package : " + aPackage.getPackageType().toString() +"(Id : " + aPackage.getPackageID() + ")");
            packageViewHolder.tv_nights.setText("Nights : " + aPackage.getNights().toString());
            packageViewHolder.tv_agency.setText("Agency : " + aPackage.getAgencyName().toString()+"(Id : " + aPackage.getAgencyID() + ")");
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        if(VIEW_MODE == VIEW_MODE_BOOKING)
            return bookings.size();
        else if(VIEW_MODE == VIEW_MODE_AGENCY)
            return agencies.size();
        else if(VIEW_MODE == VIEW_MODE_HOTEL)
            return hotels.size();
        else if(VIEW_MODE == VIEW_MODE_PACKAGE)
            return packages.size();
        else
            return clients.size();
    }


    public void setBookings(ArrayList<Booking> list){
        bookings = list;
    }

    public void setHotels(ArrayList<Hotel> list) {
        hotels = list;
    }

    public void setPackages(ArrayList<Package> list) {
        packages = list;
    }

    public void setViewMode(int mode){
        this.VIEW_MODE = mode;
    }

    public void setAgencies(ArrayList<Agency> list) {
        agencies = list;
    }

    public void setClients(ArrayList<Client> list) {
        clients = list;
    }


    public class BookingsViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_city,tv_package,tv_from,tv_price, tv_booking, tv_vehicle, tv_agency, tv_hotel;

        public BookingsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_from = itemView.findViewById(R.id.tv_from);
            tv_package = itemView.findViewById(R.id.tv_package);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_booking = itemView.findViewById(R.id.tv_booking);
            tv_vehicle = itemView.findViewById(R.id.tv_vehicle);
            tv_agency = itemView.findViewById(R.id.tv_agency);
            tv_hotel = itemView.findViewById(R.id.tv_hotel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookingClickListener.selectedBooking(bookings.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG, "onLongClick: ");
                    bookingLongClickListener.selectedBooking(bookings.get(getAdapterPosition()));
                    return true;

                }
            });
        }
    }

    public class AgencyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name, tv_contact, tv_package_count, tv_address;

        public AgencyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_contact = itemView.findViewById(R.id.tv_contact);
            tv_package_count = itemView.findViewById(R.id.tv_package_count);
            tv_address = itemView.findViewById(R.id.tv_address);

        }
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name, tv_location, tv_availability, tv_city;
        RatingBar ratingBar;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_availability = itemView.findViewById(R.id.tv_availability);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tv_city = itemView.findViewById(R.id.tv_city);
        }
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder{

        TextView tv_city, tv_packege, tv_agency, tv_days, tv_nights, tv_price;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_packege = itemView.findViewById(R.id.tv_package_type);
            tv_agency = itemView.findViewById(R.id.tv_agency);
            tv_days = itemView.findViewById(R.id.tv_days);
            tv_nights = itemView.findViewById(R.id.tv_nights);
            tv_price = itemView.findViewById(R.id.tv_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    packageClickListener.selectedPackage(packages.get(getAdapterPosition()));
                }
            });

        }
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name, tv_address, tv_contact, tv_email, tv_bookings;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_contact = itemView.findViewById(R.id.tv_contact);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_bookings = itemView.findViewById(R.id.tv_bookings);

        }
    }

}
