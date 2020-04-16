package com.teamcool.touristum.ui.AddBooking;

import android.content.ContentValues;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.teamcool.touristum.DatabaseHelper;
import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.Booking;
import com.teamcool.touristum.ui.home.HomeFragment;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddBookingFragment extends Fragment {

    public static final String TAG = "AddFragment";

    private TextView tv_client, tv_duration, tv_agency, tv_bookingID;
    private Spinner sp_city, sp_packageType, sp_vehicle, sp_hotel,sp_client;
    private EditText et_from_day,et_from_month,et_from_year, et_price;
    private Button bv_addBooking,bv_cancel;

    private ArrayList<String> city_options,package_options, vehicle_options, hotel_options,client_options;
    private ArrayAdapter<String> cityAdapter, packageAdapter, vehicleAdapter, hotel_adapter,client_adapter;

    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    private int cityPosition, packagePosition, vehiclePosition, hotelPosition;
    private float changedPrice;

    private HashMap<String,String> cityMap,vehicleMap, packageMap, hotelMap, clientMap,agencyMap;

    public String changedCity, changedPackageType, changedVehicle;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_add_booking, container, false);

        tv_client = root.findViewById(R.id.tv_client_name);
        tv_duration = root.findViewById(R.id.tv_duration);
        tv_agency = root.findViewById(R.id.tv_agency);
        tv_bookingID = root.findViewById(R.id.tv_addBooking);
        sp_city = root.findViewById(R.id.sp_city);
        sp_client = root.findViewById(R.id.sp_clientName);
        sp_packageType = root.findViewById(R.id.sp_packageType);
        sp_vehicle = root.findViewById(R.id.sp_vehicle);
        sp_hotel = root.findViewById(R.id.sp_hotel);
        et_from_day = root.findViewById(R.id.et_from_day);
        et_from_month = root.findViewById(R.id.et_from_month);
        et_from_year = root.findViewById(R.id.et_from_year);
        et_price = root.findViewById(R.id.et_price);
        bv_addBooking = root.findViewById(R.id.bv_addBooking);
        bv_cancel = root.findViewById(R.id.bv_cancel);

        mDbHelper = new DatabaseHelper(getContext());
        mDb = mDbHelper.getWritableDatabase();

        populateOptions();

        bv_addBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String changedFromDay = et_from_day.getText().toString();
                String changedFromMonth = et_from_month.getText().toString();
                String changedFromYear = et_from_year.getText().toString();
                String changedPrice = et_price.getText().toString();

                String changedPackageID = packageMap.get(sp_packageType.getSelectedItem().toString());
                String changedVehicleID = vehicleMap.get(sp_vehicle.getSelectedItem().toString());
                String changedCityID = cityMap.get(sp_city.getSelectedItem().toString());
                String changedHotelID = hotelMap.get(sp_hotel.getSelectedItem().toString());
                String changedClientID = clientMap.get(sp_client.getSelectedItem().toString());
                String agency = agencyMap.get(sp_packageType.getSelectedItem().toString()).split(",")[0];
                String agencyID = agencyMap.get(sp_packageType.getSelectedItem().toString()).split(",")[1];

                String from = changedFromDay + " " + changedFromMonth + " " + changedFromYear;

                String bookingID = "0";

//                Log.d(TAG, "onClick: " + changedCityID + changedPackageID + changedVehicleID);



                String package_type = sp_packageType.getSelectedItem().toString().split(",")[0].split(":")[1];
                String vehicleName = sp_vehicle.getSelectedItem().toString().split(",")[0].split(":")[1];
                String vehicleType = sp_vehicle.getSelectedItem().toString().split(",")[1].split(":")[1];
                String hotelName = sp_hotel.getSelectedItem().toString().split(",")[0].split(":")[1];

//                mDb.update("booking",contentValues,"bookingID = ?",new String[]{booking.getBookingID()});

                String sql = "select bookingID from booking order by bookingID DESC LIMIT 1";
                Cursor cur = mDb.rawQuery(sql,null);
                while (cur != null && cur.moveToNext()) {
                    bookingID = cur.getString(0);
                }
                bookingID = String.valueOf(Integer.parseInt(bookingID) + 1);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yyyy");
                LocalDateTime now = LocalDateTime.now();
                String days = sp_packageType.getSelectedItem().toString().split(",")[1].split(":")[1].split("/")[0];
                String nights = sp_packageType.getSelectedItem().toString().split(",")[1].split(":")[1].split("/")[1];
                String price = sp_packageType.getSelectedItem().toString().split(",")[2].split(":")[1];

                ContentValues contentValues = new ContentValues();
                contentValues.put("bookingID",bookingID);
                contentValues.put("clientID",changedClientID);
                contentValues.put("dateOfBooking",dtf.format(now));
                contentValues.put("FromDate",from);
                contentValues.put("Price",changedPrice);
                contentValues.put("PackageID",changedPackageID);
                contentValues.put("VehicleID",changedVehicleID);
                contentValues.put("CityID",changedCityID);
                contentValues.put("HotelID",changedHotelID);
                contentValues.put("PackageType",package_type);

                mDb.insert("Booking",null,contentValues);

            }
        });

        bv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();

            }
        });

        return root;

    }

    private void populateOptions() {

        getCityOptions(mDb);
        getClientOptions(mDb);

        populateClientSpinner();
        populateCitySpinner();


    }

    private void getHotelOptions(SQLiteDatabase mDb, String cityName) {
        String sql = "Select hotelID, HotelName, availableRooms, h.rating From HotelInformation h, TouristCity c " +
                "Where c.cityID = h.cityID and c.cityName = '" + cityName + "';" ;
        Cursor cur = mDb.rawQuery(sql,null);

        hotel_options = new ArrayList<>();
        hotelMap = new HashMap<>();
        while(cur!=null && cur.moveToNext()){

            hotel_options.add("Name:" + cur.getString(1) + ",Rating:" + cur.getString(3) + ",Rooms:" + cur.getString(2));
            hotelMap.put("Name:" + cur.getString(1) + ",Rating:" + cur.getString(3) + ",Rooms:" + cur.getString(2),cur.getString(0));

        }

        cur.close();
    }

    private void getVehicleOptions(SQLiteDatabase mDb, String cityName) {

        String sql = "Select vehicleID, vehicleType, capacity, vehicleName From Vehicle v, TouristCity c " +
                "Where v.cityID = c.cityID and c.cityName = '" + cityName + "';" ;
        Cursor cur = mDb.rawQuery(sql,null);

        vehicle_options = new ArrayList<>();
        vehicleMap = new HashMap<>();
        while(cur!=null && cur.moveToNext()){

            vehicle_options.add("Name:"+cur.getString(3)+",Type:"+cur.getString(1) + "capacity:" + cur.getString(2) + ")");
            vehicleMap.put("Name:"+cur.getString(3)+",Type:"+cur.getString(1) + "capacity:" + cur.getString(2) + ")",cur.getString(0));

        }


        cur.close();

    }

    private void getPackageOptions(SQLiteDatabase mDb, String cityName) {

        String sql = "Select packageID, packageType, days, nights, packagePrice,agencyName,p.agencyID From Package p, TouristCity c, agencies a " +
                "Where p.cityID = c.cityID and p.agencyID = a.agencyID and c.cityName = '" + cityName + "';" ;
        Cursor cur = mDb.rawQuery(sql,null);

        package_options = new ArrayList<>();
        packageMap = new HashMap<>();
        agencyMap = new HashMap<>();
        while(cur!=null && cur.moveToNext()){

            package_options.add("Type:" + cur.getString(1) + ",Days/Nights:" + cur.getString(2) + "/" + cur.getString(3) + ",Price:" + cur.getString(4));
            packageMap.put("Type:" + cur.getString(1) + ",Days/Nights:" + cur.getString(2) + "/" + cur.getString(3) + ",Price:" + cur.getString(4),cur.getString(0));
            agencyMap.put("Type:" + cur.getString(1) + ",Days/Nights:" + cur.getString(2) + "/" + cur.getString(3) + ",Price:" + cur.getString(4),cur.getString(5)+","+cur.getString(6));
        }

        cur.close();



    }

    private void getClientOptions(SQLiteDatabase mDb) {

        String sql = "Select clientID, clientName From Client";
        Cursor cur = mDb.rawQuery(sql,null);

        client_options = new ArrayList<>();
        clientMap = new HashMap<>();
        while(cur!=null && cur.moveToNext()){

            client_options.add(cur.getString(1));
            clientMap.put(cur.getString(1),cur.getString(0));
        }

        cur.close();



    }

    private void getCityOptions(SQLiteDatabase mDb) {

        String sql = "Select cityID, cityName From touristCity ;";
        Cursor cur = mDb.rawQuery(sql,null);

        city_options = new ArrayList<>();
        cityMap = new HashMap<>();
        while(cur!=null && cur.moveToNext()){

            city_options.add(cur.getString(1));
            cityMap.put(cur.getString(1),cur.getString(0));
        }

        cur.close();

    }

    private void populateCitySpinner() {


        cityAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, city_options);
        cityAdapter.setNotifyOnChange(true);

        sp_city.setAdapter(cityAdapter);
        sp_city.setSelection(cityPosition);

        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                changedCity = city_options.get(position);

                if(packageAdapter!=null)
                    packageAdapter.clear();
                if(vehicleAdapter!=null)
                    vehicleAdapter.clear();
                if(hotel_adapter!=null)
                    hotel_adapter.clear();

                getPackageOptions(mDb,changedCity);
                getVehicleOptions(mDb,changedCity);
                getHotelOptions(mDb,changedCity);



//                packageAdapter.addAll(package_options);
//                vehicleAdapter.addAll(vehicle_options);
                populatePackageSpinner();
                populateVehicleSpinner();
                populateHotelSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populatePackageSpinner() {


        packageAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, package_options);
        packageAdapter.setNotifyOnChange(true);

        sp_packageType.setAdapter(packageAdapter);

        sp_packageType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String days = package_options.get(position).split(",")[1].split(":")[1].split("/")[0];
                String nights = package_options.get(position).split(",")[1].split(":")[1].split("/")[1];
                String price = package_options.get(position).split(",")[2].split(":")[1];

                String agency = agencyMap.get(package_options.get(position)).split(",")[0];
                tv_duration.setText("Duration : " + days + " Days/ " + nights + "Nights");
                et_price.setText(price);
                tv_agency.setText("Agency : " + agency);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateVehicleSpinner() {


        vehicleAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, vehicle_options);
        vehicleAdapter.setNotifyOnChange(true);

        sp_vehicle.setAdapter(vehicleAdapter);

        sp_vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateHotelSpinner() {

        Log.d(TAG, "populateHotelSpinner: " + hotel_options.size());

        hotel_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, hotel_options);
        hotel_adapter.setNotifyOnChange(true);

        sp_hotel.setAdapter(hotel_adapter);

        sp_hotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateClientSpinner() {

        Log.d(TAG, "populateHotelSpinner: " + client_options.size());

        client_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, client_options);
        client_adapter.setNotifyOnChange(true);

        sp_client.setAdapter(client_adapter);

        sp_client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
