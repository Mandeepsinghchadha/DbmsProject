package com.teamcool.touristum.ui.home;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.teamcool.touristum.Activities.EditBookingActivity;
import com.teamcool.touristum.Adapters.ManagerHomeAdapter;
import com.teamcool.touristum.DatabaseHelper;
import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.Agency;
import com.teamcool.touristum.data.model.Booking;
import com.teamcool.touristum.data.model.Client;
import com.teamcool.touristum.data.model.Filter;
import com.teamcool.touristum.data.model.Hotel;
import com.teamcool.touristum.data.model.Package;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private Spinner sp_manager_options, sp_dialog_filter;
    private ChipGroup cg_filters;
    private ImageButton bv_filter;
    private RecyclerView rv_data;
    private EditText et_search, et_filter;
    private ManagerHomeAdapter managerHomeAdapter;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    private ArrayList<Booking> bookings;
    private ArrayList<Agency> agencies;
    private ArrayList<Client> clients;
    private ArrayList<Package> packages;
    private ArrayList<Hotel> hotels;
    private ArrayList<Filter> booking_filters;
    private ArrayList<Filter> client_filters;
    private ArrayList<Filter> hotel_filters;
    private ArrayList<Filter> package_filters;
    private ArrayList<Filter> agency_filters;

    private String[] filter_options;
    private AlertDialog.Builder builder,bookingBuilder;
    private View dialog_view;

    public static final int VIEW_MODE_BOOKING = 1;
    public static final int VIEW_MODE_AGENCY = 2;
    public static final int VIEW_MODE_PACKAGE = 3;
    public static final int VIEW_MODE_HOTEL = 4;
    public static final int VIEW_MODE_CLIENT = 5;
    private int VIEW_MODE;

    public static final int BOOKING_UPDATE = 2;

    public static final String TAG = "Home Fragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        booking_filters = new ArrayList<>();
        client_filters = new ArrayList<>();
        package_filters = new ArrayList<>();
        hotel_filters = new ArrayList<>();
        agency_filters = new ArrayList<>();

        sp_manager_options = root.findViewById(R.id.sp_manager_options);
        rv_data = root.findViewById(R.id.rv_data);
        et_search = root.findViewById(R.id.et_search);

        builder = new AlertDialog.Builder(getContext());

        managerHomeAdapter = new ManagerHomeAdapter(getContext(), new ManagerHomeAdapter.onBookingClickListener() {
            @Override
            public void selectedBooking(Booking booking) {

                Intent intent = new Intent(getActivity(), EditBookingActivity.class);
                intent.putExtra("EditBooking", booking);
                startActivityForResult(intent, BOOKING_UPDATE);

            }
        }, new ManagerHomeAdapter.onBookingLongClickListener() {
            @Override
            public void selectedBooking(Booking booking) {
                Log.d(TAG, "selectedBooking: ");
                final Booking b = booking;

                View view = getLayoutInflater().inflate(R.layout.booking_popup, null);
                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.show();

                view.findViewById(R.id.bv_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), EditBookingActivity.class);
                        intent.putExtra("EditBooking", b);
                        startActivityForResult(intent, BOOKING_UPDATE);

                    }
                });

                view.findViewById(R.id.bv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelBooking(b);
                        dialog.dismiss();


                    }
                });

            }
        }, new ManagerHomeAdapter.onPackageClickListener() {
            @Override
            public void selectedPackage(final Package pack) {
                final View view = getLayoutInflater().inflate(R.layout.package_price_popup, null);
                builder.setView(view);

                final AlertDialog dialog;
                builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which == DialogInterface.BUTTON_POSITIVE){
                            String newPrice = ((EditText)view.findViewById(R.id.et_newPrice)).getText().toString();
                            updatePrice(newPrice,pack.getPackageID());
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_NEGATIVE){
                            dialog.dismiss();
                        }
                    }
                });



                dialog = builder.create();
                dialog.show();






            }
        });


        rv_data.setLayoutManager(new LinearLayoutManager(getContext()));

        mDbHelper = new DatabaseHelper(getContext());
        mDb = mDbHelper.getReadableDatabase();

        et_search.setFocusable(false);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_view = getLayoutInflater().inflate(R.layout.dialog_filter,null);
                sp_dialog_filter = dialog_view.findViewById(R.id.sp_filter);
                et_filter = dialog_view.findViewById(R.id.et_filter);
                bv_filter = dialog_view.findViewById(R.id.bv_filter);
                cg_filters = dialog_view.findViewById(R.id.cg_filters);

                addChips();

                if(VIEW_MODE == VIEW_MODE_BOOKING){

                    filter_options = new String[]{"BookingID","ClientID","PackageID","cityID","VehicleID","agencyID","ClientName","PackageType","CityName","VehicleName","VehicleType","DateOfBooking","FromDate","Days","Nights","Price", "agencyName"};
                    builder.setTitle("Filter Bookings");
                }
                else if(VIEW_MODE == VIEW_MODE_AGENCY){
                    filter_options = new String[]{"AgencyID","AgencyName","AgencyAddress","AgencyContact","NoOfPackages"};
                    builder.setTitle("Filter Agencies");
                }
                else if(VIEW_MODE == VIEW_MODE_HOTEL){
                    filter_options = new String[]{"HotelID","HotelName","HotelCity","HotelLocation","HotelRating","AvailableRooms","cityID", "locationID"};
                    builder.setTitle("Filter Hotels");
                }
                else if(VIEW_MODE == VIEW_MODE_CLIENT){
                    filter_options = new String[]{"ClientID","ClientName","ClientContact","ClientAddress","ClientEmail","NoOfBookings"};
                    builder.setTitle("Filter Clients");
                }
                else if(VIEW_MODE == VIEW_MODE_PACKAGE){
                    filter_options = new String[]{"PackageID","AgencyName","PackageType","Days","Nights","City","PackagePrice","AgencyID","cityID"};
                    builder.setTitle("Filter Packages");
                }
                launchDialog();

            }
        });


        final String[] manager_options = {"Bookings", "Hotels", "Packages", "Agencies","Clients"};

        bookings = getBookings();
        clients = getClients();
        packages = getPackages();
        hotels = getHotels();
        agencies = getAgencies();

        sp_manager_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_search.setHint("Filter " + manager_options[position]);
                if(manager_options[position].equals("Bookings")){


                    managerHomeAdapter.setBookings(bookings);
                    managerHomeAdapter.setViewMode(ManagerHomeAdapter.VIEW_MODE_BOOKING);
                    rv_data.setAdapter(managerHomeAdapter);
                    VIEW_MODE = VIEW_MODE_BOOKING;

                }
                else if(manager_options[position].equals("Hotels")){


                    managerHomeAdapter.setHotels(hotels);
                    managerHomeAdapter.setViewMode(ManagerHomeAdapter.VIEW_MODE_HOTEL);
                    rv_data.setAdapter(managerHomeAdapter);
                    VIEW_MODE = VIEW_MODE_HOTEL;

                }
                else if(manager_options[position].equals("Packages")){


                    managerHomeAdapter.setPackages(packages);
                    managerHomeAdapter.setViewMode(ManagerHomeAdapter.VIEW_MODE_PACKAGE);
                    rv_data.setAdapter(managerHomeAdapter);
                    VIEW_MODE = VIEW_MODE_PACKAGE;

                }
                else if(manager_options[position].equals("Agencies")){


                    managerHomeAdapter.setAgencies(agencies);
                    managerHomeAdapter.setViewMode(ManagerHomeAdapter.VIEW_MODE_AGENCY);
                    rv_data.setAdapter(managerHomeAdapter);
                    VIEW_MODE = VIEW_MODE_AGENCY;

                }
                else if(manager_options[position].equals("Clients")){


                    managerHomeAdapter.setClients(clients);
                    managerHomeAdapter.setViewMode(ManagerHomeAdapter.VIEW_MODE_CLIENT);
                    rv_data.setAdapter(managerHomeAdapter);
                    VIEW_MODE = VIEW_MODE_CLIENT;

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, manager_options){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // this part is needed for hiding the original view
                View view = super.getView(position, convertView, parent);
                view.setVisibility(View.GONE);

                return view;
            }
        };

        sp_manager_options.setAdapter(dataAdapter);

        return root;
    }

    private void updatePrice(String newPrice, String packageID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PackagePrice",newPrice);
        mDb.update("package",contentValues,"packageID = ?",new String[]{packageID});
        updateData();
    }

    private void cancelBooking(Booking b) {
        mDb.delete("booking","bookingID = ?",new String[]{b.getBookingID()});
        updateData();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BOOKING_UPDATE){
            if(resultCode == BOOKING_UPDATE){
                updateData();
            }
        }
    }

    private ArrayList<Booking> getFilteredBookings(ArrayList<Filter> booking_filters) {

        String sql = generateSqlBookings(booking_filters);
        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Booking> bookings = new ArrayList<>();

        while(cur!=null && cur.moveToNext()){
            Booking booking = new Booking(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5),
                    cur.getString(6),
                    cur.getString(7),
                    cur.getString(8),
                    cur.getString(9),
                    cur.getString(10),
                    cur.getString(11),
                    cur.getString(12),
                    cur.getString(13),
                    cur.getString(14),
                    cur.getString(15),
                    cur.getString(16));
            bookings.add(booking);

        }
        Log.d(TAG, "generated: bookings" + bookings.size());

        return bookings;

    }

    private ArrayList<Package> getFilteredPackages(ArrayList<Filter> package_filters) {
        String sql = generateSqlPackages(package_filters);

        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Package> packages = new ArrayList<>();

        while(cur!=null && cur.moveToNext()){
            Package aPackage = new Package(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5),
                    cur.getString(6),
                    cur.getString(7),
                    cur.getString(8));
            packages.add(aPackage);

        }
        Log.d(TAG, "onItemSelected: " + packages.size());

        return packages;

    }

    private ArrayList<Client> getFilteredClients(ArrayList<Filter> client_filters) {

        String sql = generateSqlClients(client_filters);
        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Client> clients = new ArrayList<>();

        while(cur!=null && cur.moveToNext()){

            Client client = new Client(cur.getString(0),
                    cur.getString(1),
                    String.format("%.0f",cur.getFloat(2)),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5));
            clients.add(client);

        }
        Log.d(TAG, "onItemSelected: " + clients.size());

        return clients;

    }

    private ArrayList<Hotel> getFilteredHotels(ArrayList<Filter> hotel_filters) {

        String sql = generateSqlHotels(hotel_filters);
        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Hotel> hotels = new ArrayList<>();

        while(cur!=null && cur.moveToNext()){
            Hotel hotel = new Hotel(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5),
                    cur.getString(6),
                    cur.getString(7));
            hotels.add(hotel);

        }
        Log.d(TAG, "onItemSelected: " + hotels.size());
        return hotels;
    }

    private ArrayList<Agency> getFilteredAgencies(ArrayList<Filter> agency_filters) {

        String sql = generateSqlAgencies(agency_filters);

        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Agency> agencies = new ArrayList<>();


        while(cur!=null && cur.moveToNext()){
            Agency agency = new Agency(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    String.format("%.0f",cur.getFloat(3)),
                    cur.getString(4));
            agencies.add(agency);

        }
        Log.d(TAG, "onItemSelected: " + agencies.size());

        return agencies;

    }


    private String generateSqlBookings(ArrayList<Filter> booking_filters) {

        StringBuilder sql = new StringBuilder("SELECT bookingID, clientName, b.packageType, cityName, vehicleName, vehicleType, dateOfBooking, fromDate, days, nights, b.price, agencyName,b.vehicleID,p.agencyID, b.packageID, b.clientID,b.cityID " +
                "FROM booking b,client c,TouristCity t,Vehicle v,Package p,agencies a " +
                "WHERE b.clientID = c.clientID and b.packageID = p.packageID and b.cityID = t.cityID and b.vehicleID = v.vehicleID and p.agencyID = a.agencyID ");
        
        for(int i=0;i<booking_filters.size();i++){
            
            if(i >=1){
                sql.append("or ");

            }

            if(i == 0){
                sql.append("and (");
            }

            
            if(booking_filters.get(i).getType().equalsIgnoreCase("BookingID")){
                sql.append("b.bookingID = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("ClientName")){
                sql.append("c.ClientName = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("PackageType")){
                sql.append("b.PackageType = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("CityName")){
                sql.append("t.cityName = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("VehicleName")){
                sql.append("v.vehicleName = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("VehicleType")){
                sql.append("v.vehicleType = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("DateOfBooking")){
                sql.append("b.DateOfBooking = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("FromDate")){
                sql.append("b.FromDate = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("Days")){
                sql.append("p.Days = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("Nights")){
                sql.append("p.Nights = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("Price")){
                sql.append("b.price = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("agencyName")){
                sql.append("a.agencyName = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("PackageID")){
                sql.append("b.PackageID = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("cityID")){
                sql.append("b.cityID = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("VehicleID")){
                sql.append("b.VehicleID = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("ClientID")){
                sql.append("b.ClientID = " + booking_filters.get(i).getFilter() + " ");

            }
            else if(booking_filters.get(i).getType().equalsIgnoreCase("AgencyID")){
                sql.append("p.agencyID = " + booking_filters.get(i).getFilter() + " ");

            }

            if (i == booking_filters.size() - 1){
                sql.append(");");
            }

        }

        Log.d(TAG, "generatedSql: " + sql.toString());

        return sql.toString();
    }

    private String generateSqlAgencies(ArrayList<Filter> agency_filters) {
        StringBuilder sql = new StringBuilder("SELECT agencyID, agencyName, agencyAddress, agencyContact, numberOfPackages " +
                "FROM agencies a ");

        for(int i=0;i<agency_filters.size();i++){

            if(i >=1){
                sql.append("or ");

            }

            if(i == 0){
                sql.append("WHERE ");
            }


            if(agency_filters.get(i).getType().equalsIgnoreCase("agencyID")){
                sql.append("a.agencyID = " + agency_filters.get(i).getFilter() + " ");

            }
            else if(agency_filters.get(i).getType().equalsIgnoreCase("agencyName")){
                sql.append("a.agencyName = " + agency_filters.get(i).getFilter() + " ");

            }
            else if(agency_filters.get(i).getType().equalsIgnoreCase("agencyAddress")){
                sql.append("a.agencyAddress = " + agency_filters.get(i).getFilter() + " ");

            }
            else if(agency_filters.get(i).getType().equalsIgnoreCase("agencyContact")){
                sql.append("a.agencyContact = " + agency_filters.get(i).getFilter() + " ");

            }
            else if(agency_filters.get(i).getType().equalsIgnoreCase("numberOfPackages")){
                sql.append("a.numberOfPackages = " + agency_filters.get(i).getFilter() + " ");

            }


            if (i == agency_filters.size() - 1){
                sql.append(";");
            }

        }

        Log.d(TAG, "generatedSql: " + sql.toString());

        return sql.toString();
    }

    private String generateSqlPackages(ArrayList<Filter> package_filters) {
        StringBuilder sql = new StringBuilder("SELECT packageID, agencyName, packageType, days, nights, cityName, packagePrice,p.agencyID,p.cityID" +
                " FROM Package p,agencies a, TouristCity c " +
                "WHERE p.agencyID = a.agencyID and p.cityID = c.cityID ");

        for(int i=0;i<package_filters.size();i++){
            Log.d(TAG, "generateSqlPackages: " + "inside");

            if(i >=1){
                sql.append("or ");

            }

            if(i == 0){
                sql.append("and (");
            }


            if(package_filters.get(i).getType().equalsIgnoreCase("packageID")){
                Log.d(TAG, "generateSqlPackages: "+ "herer");
                sql.append("p.packageID = " + package_filters.get(i).getFilter() + " ");

            }
            else if(package_filters.get(i).getType().equalsIgnoreCase("agencyName")){
                sql.append("a.agencyName = " + package_filters.get(i).getFilter() + " ");

            }
            else if(package_filters.get(i).getType().equalsIgnoreCase("packageType")){
                sql.append("p.PackageType = " + package_filters.get(i).getFilter() + " ");

            }
            else if(package_filters.get(i).getType().equalsIgnoreCase("days")){
                sql.append("p.days = " + package_filters.get(i).getFilter() + " ");

            }
            else if(package_filters.get(i).getType().equalsIgnoreCase("nights")){
                sql.append("p.nights = " + package_filters.get(i).getFilter() + " ");

            }
            else if(package_filters.get(i).getType().equalsIgnoreCase("cityName")){
                sql.append("c.cityName = " + package_filters.get(i).getFilter() + " ");

            }
            else if(package_filters.get(i).getType().equalsIgnoreCase("packagePrice")){
                sql.append("p.packagePrice = " + package_filters.get(i).getFilter() + " ");

            }
            else if(package_filters.get(i).getType().equalsIgnoreCase("agencyID")){
                sql.append("p.agencyID = " + package_filters.get(i).getFilter() + " ");

            }
            else if(package_filters.get(i).getType().equalsIgnoreCase("cityID")){
                sql.append("p.cityID = " + package_filters.get(i).getFilter() + " ");

            }


            if (i == package_filters.size() - 1){
                sql.append(");");
            }

        }

        Log.d(TAG, "generatedSql: " + sql.toString());

        return sql.toString();
    }

    private String generateSqlClients(ArrayList<Filter> client_filters) {
        StringBuilder sql = new StringBuilder("SELECT c.clientID, clientName, clientContact, clientAddress, clientEmail, count(b.bookingID) " +
                "FROM Client c, Booking b " +
                "WHERE c.clientID = b.clientID ");

        for(int i=0;i<client_filters.size();i++){

            if(i >=1){
                sql.append("or ");

            }

            if(i == 0){
                sql.append("and (");
            }


            if(client_filters.get(i).getType().equalsIgnoreCase("clientID")){
                sql.append("c.clientID = " + client_filters.get(i).getFilter() + " ");

            }
            else if(client_filters.get(i).getType().equalsIgnoreCase("clientName")){
                sql.append("c.clientName = " + client_filters.get(i).getFilter() + " ");

            }
            else if(client_filters.get(i).getType().equalsIgnoreCase("clientContact")){
                sql.append("c.clientContact = " + client_filters.get(i).getFilter() + " ");

            }
            else if(client_filters.get(i).getType().equalsIgnoreCase("clientAddress")){
                sql.append("c.clientAddress = " + client_filters.get(i).getFilter() + " ");

            }
            else if(client_filters.get(i).getType().equalsIgnoreCase("clientEmail")){
                sql.append("c.clientEmail = " + client_filters.get(i).getFilter() + " ");

            }
            else if(client_filters.get(i).getType().equalsIgnoreCase("NoOfBookings")){
                sql.append("having count(b.bookingID) = " + client_filters.get(i).getFilter() + " ");

            }


            if (i == client_filters.size() - 1){
                sql.append(") GROUP BY c.clientID ;");
            }

        }

        Log.d(TAG, "generatedSql: " + sql.toString());

        return sql.toString();
    }

    private String generateSqlHotels(ArrayList<Filter> hotel_filters) {
        StringBuilder sql = new StringBuilder("SELECT hotelID, hotelName, cityName, locationName, h.Rating, availableRooms,h.cityID, h.locationID" +
                " FROM hotelinformation h, touristCity c, touristlocations l " +
                "WHERE h.cityID = c.cityID and h.locationID = l.locationID ");

        Collections.sort(hotel_filters);
        for(int i=0;i<hotel_filters.size();i++){

            if(i >=1){
                sql.append("or ");

            }

            if(i == 0){
                sql.append("and (");
            }


            if(hotel_filters.get(i).getType().equalsIgnoreCase("hotelID")){
                sql.append("h.hotelID = " + hotel_filters.get(i).getFilter() + " ");

            }
            else if(hotel_filters.get(i).getType().equalsIgnoreCase("hotelName")){
                sql.append("h.hotelName = " + hotel_filters.get(i).getFilter() + " ");

            }
            else if(hotel_filters.get(i).getType().equalsIgnoreCase("cityName")){
                sql.append("c.cityName = " + hotel_filters.get(i).getFilter() + " ");

            }
            else if(hotel_filters.get(i).getType().equalsIgnoreCase("locationName")){
                sql.append("l.locationName = " + hotel_filters.get(i).getFilter() + " ");

            }
            else if(hotel_filters.get(i).getType().equalsIgnoreCase("Rating")){
                sql.append("h.Rating = " + hotel_filters.get(i).getFilter() + " ");

            }
            else if(hotel_filters.get(i).getType().equalsIgnoreCase("availableRooms")){
                sql.append("h.availableRooms = " + hotel_filters.get(i).getFilter() + " ");

            }
            else if(hotel_filters.get(i).getType().equalsIgnoreCase("cityID")){
                sql.append("h.cityID = " + hotel_filters.get(i).getFilter() + " ");

            }


            if (i == hotel_filters.size() - 1){
                sql.append(");");
            }

        }

        Log.d(TAG, "generatedSql: " + sql.toString());

        return sql.toString();
    }



    private void addChips() {
        int size = getFilterSize();
        for(int i=0;i<size;i++){
            final Chip chip = new Chip(cg_filters.getContext());
            chip.setCloseIconVisible(true);
            chip.setText(getFilterText(i));
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = chip.getText().toString().split(":")[1];
                    String type = chip.getText().toString().split(":")[0];
                    removeFromFilter(new Filter(type,text));
                    cg_filters.removeView(chip);

                }
            });

            cg_filters.addView(chip);

        }
    }

    private String getFilterText(int i){
        if(VIEW_MODE == VIEW_MODE_BOOKING){
            return booking_filters.get(i).getType() + ":" + booking_filters.get(i).getFilter();
        }
        else if(VIEW_MODE == VIEW_MODE_AGENCY){
            return agency_filters.get(i).getType() + ":" + agency_filters.get(i).getFilter();
        }
        else if(VIEW_MODE == VIEW_MODE_HOTEL){
            return hotel_filters.get(i).getType() + ":" + hotel_filters.get(i).getFilter();
        }
        else if(VIEW_MODE == VIEW_MODE_CLIENT){
            return client_filters.get(i).getType() + ":" + client_filters.get(i).getFilter();
        }
        else {
            return package_filters.get(i).getType() + ":" + package_filters.get(i).getFilter();
        }
    }

    private int getFilterSize() {
        if(VIEW_MODE == VIEW_MODE_BOOKING){
            return booking_filters.size();
        }
        else if(VIEW_MODE == VIEW_MODE_AGENCY){
            return agency_filters.size();
        }
        else if(VIEW_MODE == VIEW_MODE_HOTEL){
            return hotel_filters.size();
        }
        else if(VIEW_MODE == VIEW_MODE_CLIENT){
            return client_filters.size();
        }
        else {
            return package_filters.size();
        }
    }

    private void removeFromFilter(Filter filter){
        if(VIEW_MODE == VIEW_MODE_BOOKING){
            booking_filters.remove(filter);
        }
        else if(VIEW_MODE == VIEW_MODE_AGENCY){
            agency_filters.remove(filter);
        }
        else if(VIEW_MODE == VIEW_MODE_HOTEL){
            hotel_filters.remove(filter);
        }
        else if(VIEW_MODE == VIEW_MODE_CLIENT){
            client_filters.remove(filter);
        }
        else if(VIEW_MODE == VIEW_MODE_PACKAGE){
            package_filters.remove(filter);
        }
    }

    private void addToFilter(Filter filter){
        if(VIEW_MODE == VIEW_MODE_BOOKING){
            booking_filters.add(filter);
        }
        else if(VIEW_MODE == VIEW_MODE_AGENCY){
            agency_filters.add(filter);
        }
        else if(VIEW_MODE == VIEW_MODE_HOTEL){
            hotel_filters.add(filter);
        }
        else if(VIEW_MODE == VIEW_MODE_CLIENT){
            client_filters.add(filter);
        }
        else if(VIEW_MODE == VIEW_MODE_PACKAGE){
            package_filters.add(filter);
        }
    }

    private void launchDialog(){
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, filter_options);
        sp_dialog_filter.setAdapter(filterAdapter);

        sp_dialog_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String type = filter_options[position];
                et_filter.setHint(type);

                bv_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(et_filter.getText().length() != 0){
                            Filter filter = new Filter(type,et_filter.getText().toString());
                            addToFilter(filter);
//                            booking_filters.add(filter);
                            final Chip chip = new Chip(getContext());
                            chip.setCloseIconVisible(true);
                            chip.setText(type + ":" + filter.getFilter());
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String text = chip.getText().toString().split(":")[1];
                                    String type = chip.getText().toString().split(":")[0];
                                    removeFromFilter(new Filter(type,text));
//                                    booking_filters.remove(new Filter(type,text));
                                    cg_filters.removeView(chip);

                                }
                            });

                            cg_filters.addView(chip);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(dialog_view);
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateData();



            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateData() {

        if(VIEW_MODE == VIEW_MODE_BOOKING){
            ArrayList<Booking> bookings = getFilteredBookings(booking_filters);
            managerHomeAdapter.setBookings(bookings);
            managerHomeAdapter.notifyDataSetChanged();
        }
        else if(VIEW_MODE == VIEW_MODE_AGENCY){
            ArrayList<Agency> agencies = getFilteredAgencies(agency_filters);
            managerHomeAdapter.setAgencies(agencies);
            managerHomeAdapter.notifyDataSetChanged();
        }
        else if(VIEW_MODE == VIEW_MODE_HOTEL){
            ArrayList<Hotel> hotels = getFilteredHotels(hotel_filters);
            managerHomeAdapter.setHotels(hotels);
            managerHomeAdapter.notifyDataSetChanged();
        }
        else if(VIEW_MODE == VIEW_MODE_CLIENT){
            ArrayList<Client> clients = getFilteredClients(client_filters);
            managerHomeAdapter.setClients(clients);
            managerHomeAdapter.notifyDataSetChanged();
        }
        else if(VIEW_MODE == VIEW_MODE_PACKAGE){
            ArrayList<Package> packages = getFilteredPackages(package_filters);
            managerHomeAdapter.setPackages(packages);
            managerHomeAdapter.notifyDataSetChanged();
        }
    }


    private ArrayList<Agency> getAgencies() {

        String sql = "SELECT agencyID, agencyName, agencyAddress, agencyContact, numberOfPackages " +
                "FROM agencies a;";

        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Agency> agencies = new ArrayList<>();


        while(cur!=null && cur.moveToNext()){
            Agency agency = new Agency(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    String.format("%.0f",cur.getFloat(3)),
                    cur.getString(4));
            agencies.add(agency);

        }
        Log.d(TAG, "onItemSelected: " + agencies.size());

        return agencies;
    }

    private ArrayList<Hotel> getHotels() {

        String sql = "SELECT hotelID, hotelName, cityName, locationName, h.Rating, availableRooms,h.cityID, h.locationID" +
                " FROM hotelinformation h, touristCity c, touristlocations l " +
                "WHERE h.cityID = c.cityID and h.locationID = l.locationID;";
        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Hotel> hotels = new ArrayList<>();

        while(cur!=null && cur.moveToNext()){
            Hotel hotel = new Hotel(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5),
                    cur.getString(6),
                    cur.getString(7));
            hotels.add(hotel);

        }
        Log.d(TAG, "onItemSelected: " + hotels.size());
        return hotels;
    }

    private ArrayList<Package> getPackages() {
        String sql = "SELECT packageID, agencyName, packageType, days, nights, cityName, packagePrice,p.agencyID,p.cityID" +
                " FROM Package p,agencies a, TouristCity c " +
                "WHERE p.agencyID = a.agencyID and p.cityID = c.cityID;";

        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Package> packages = new ArrayList<>();

        while(cur!=null && cur.moveToNext()){
            Package aPackage = new Package(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5),
                    cur.getString(6),
                    cur.getString(7),
                    cur.getString(8));
            packages.add(aPackage);

        }
        Log.d(TAG, "onItemSelected: " + packages.size());

        return packages;
    }

    private ArrayList<Client> getClients() {

        String sql = "SELECT c.clientID, clientName, clientContact, clientAddress, clientEmail, count(b.bookingID) " +
                "FROM Client c, Booking b " +
                "WHERE c.clientID = b.clientID " +
                "GROUP BY c.clientID;";
        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Client> clients = new ArrayList<>();

        while(cur!=null && cur.moveToNext()){

//            String sql2 = "SELECT bookingID, clientName, b.packageType, cityName, vehicleName, vehicleType, dateOfBooking, fromDate, days, nights, packagePrice, agencyName,b.vehicleID, p.agencyID, b.packageID, b.clientID,b.cityID " +
//                    "FROM booking b,client c,TouristCity t,Vehicle v,Package p,agencies a " +
//                    "WHERE b.clientID = c.clientID and b.packageID = p.packageID and b.cityID = t.cityID and b.vehicleID = v.vehicleID and p.agencyID = a.agencyID and c.clientID = "+cur.getString(0) + ";";
//
//            Cursor cur2 = mDb.rawQuery(sql2, null);
//            ArrayList<Booking> bookings = new ArrayList<>();
//            while(cur2!=null && cur2.moveToNext()){
//                Booking booking = new Booking(cur2.getString(0),
//                        cur2.getString(1),
//                        String.format("%.0f",cur.getFloat(2)),
//                        cur2.getString(3),
//                        cur2.getString(4),
//                        cur2.getString(5),
//                        cur2.getString(6),
//                        cur2.getString(7),
//                        cur2.getString(8),
//                        cur2.getString(9),
//                        cur2.getString(10),
//                        cur2.getString(11),
//                        cur2.getString(12),
//                        cur2.getString(13),
//                        cur2.getString(14),
//                        cur2.getString(15),
//                        cur2.getString(16));
//                bookings.add(booking);
//
//            }

            Client client = new Client(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5));
            clients.add(client);

        }
        Log.d(TAG, "onItemSelected: " + clients.size());

        return clients;
    }

    private ArrayList<Booking> getBookings() {

        String sql = "SELECT bookingID, clientName, b.packageType, cityName, vehicleName, vehicleType, dateOfBooking, fromDate, days, nights, packagePrice, agencyName, b.vehicleID,p.agencyID, b.packageID, b.clientID,b.cityID " +
                "FROM booking b,client c,TouristCity t,Vehicle v,Package p,agencies a " +
                "WHERE b.clientID = c.clientID and b.packageID = p.packageID and b.cityID = t.cityID and b.vehicleID = v.vehicleID and p.agencyID = a.agencyID;";
        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Booking> bookings = new ArrayList<>();

        while(cur!=null && cur.moveToNext()){
            Booking booking = new Booking(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5),
                    cur.getString(6),
                    cur.getString(7),
                    cur.getString(8),
                    cur.getString(9),
                    cur.getString(10),
                    cur.getString(11),
                    cur.getString(12),
                    cur.getString(13),
                    cur.getString(14),
                    cur.getString(15),
                    cur.getString(16));
            bookings.add(booking);

        }
        Log.d(TAG, "onItemSelected: bookings" + bookings.size());

        return bookings;


    }
}