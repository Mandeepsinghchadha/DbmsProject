package com.teamcool.touristum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;

public class EditBookingActivity extends AppCompatActivity {

    public static final String TAG = "EditBookingActivity";

    private TextView tv_client, tv_duration, tv_agency, tv_bookingID;
    private Spinner sp_city, sp_packageType, sp_vehicle, sp_hotel;
    private EditText et_from, et_price;
    private Button bv_update,bv_cancel, bv_cancelBooking;

    private ArrayList<String> city_options,package_options, vehicle_options, hotel_options;
    private ArrayAdapter<String> cityAdapter, packageAdapter, vehicleAdapter, hotel_adapter;

    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    private Booking booking;

    private int cityPosition, packagePosition, vehiclePosition, hotelPosition;
    private float changedPrice;

    private HashMap<String,String> cityMap,vehicleMap, packageMap, hotelMap, agencyMap;

    public String changedCity, changedPackageType, changedVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);

        tv_client = findViewById(R.id.tv_client_name);
        tv_duration = findViewById(R.id.tv_duration);
        tv_agency = findViewById(R.id.tv_agency);
        tv_bookingID = findViewById(R.id.tv_addBooking);
        sp_city = findViewById(R.id.sp_city);
        sp_packageType = findViewById(R.id.sp_packageType);
        sp_vehicle = findViewById(R.id.sp_vehicle);
        sp_hotel = findViewById(R.id.sp_hotel);
        et_from = findViewById(R.id.et_from);
        et_price = findViewById(R.id.et_price);
        bv_update = findViewById(R.id.bv_update);
        bv_cancel = findViewById(R.id.bv_cancel);
        bv_cancelBooking = findViewById(R.id.bv_cancelBooking);

        booking = (Booking) getIntent().getSerializableExtra("EditBooking");

        tv_bookingID.setText("Booking ID : " + booking.getBookingID());
        tv_client.setText("Client : " + booking.getClientName());
        tv_duration.setText("Duration : " + booking.getDays() + " Days/ " + booking.getNights() + "Nights");
        tv_agency.setText("Agency : " + booking.getAgencyName());
        et_from.setText(booking.getFromDate());
        et_price.setText(booking.getPrice());

        mDbHelper = new DatabaseHelper(this);
        mDb = mDbHelper.getWritableDatabase();

        populateOptions();

        bv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String changedFrom = et_from.getText().toString();
                String changedPrice = et_price.getText().toString();

                String changedPackageID = packageMap.get(sp_packageType.getSelectedItem().toString());
                String changedVehicleID = vehicleMap.get(sp_vehicle.getSelectedItem().toString());
                String changedCityID = cityMap.get(sp_city.getSelectedItem().toString());
                String changedHotelID = hotelMap.get(sp_hotel.getSelectedItem().toString());

                Log.d(TAG, "onClick: " + changedCityID + changedPackageID + changedVehicleID);

                ContentValues contentValues = new ContentValues();
                contentValues.put("FromDate",changedFrom);
                contentValues.put("Price",changedPrice);
                contentValues.put("PackageID",changedPackageID);
                contentValues.put("VehicleID",changedVehicleID);
                contentValues.put("CityID",changedCityID);
                contentValues.put("HotelID",changedHotelID);
                contentValues.put("PackageType",sp_packageType.getSelectedItem().toString().split(",")[0].split(":")[1]);

                mDb.update("booking",contentValues,"bookingID = ?",new String[]{booking.getBookingID()});

                String sql = "select bookingID, price from booking where bookingID = 2";
                Cursor cur = mDb.rawQuery(sql,null);
                while (cur != null && cur.moveToNext()) {
                    Log.d(TAG, "onPause: " + cur.getString(0) + " " + cur.getString(1));
                }

                booking.setFromDate(changedFrom);
                booking.setPrice(changedPrice);
                booking.setPackageID(changedPackageID);
                booking.setVehicleID(changedVehicleID);
                booking.setCityID(changedCityID);
                booking.setHotelID(changedHotelID);


                Intent intent = new Intent();
                setResult(HomeFragment.BOOKING_UPDATE,intent);
                finish();


            }
        });

        bv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(HomeFragment.BOOKING_UPDATE + 2,intent);
                finish();
            }
        });

        bv_cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb.delete("booking","bookingID = ?",new String[]{booking.getBookingID()});
                Intent intent = new Intent();
                setResult(HomeFragment.BOOKING_UPDATE,intent);
                finish();
            }
        });


    }

    private void populateOptions() {

        getCityOptions(mDb,booking.getCity());
        getPackageOptions(mDb,booking.getCity());
        getVehicleOptions(mDb,booking.getCity());
        getHotelOptions(mDb,booking.getCity());

        populateCitySpinner();
        populatePackageSpinner();
        populateVehicleSpinner();
        populateHotelSpinner();

    }

    private void getHotelOptions(SQLiteDatabase mDb, String cityName) {
        String sql = "Select hotelID, HotelName, availableRooms, h.rating From HotelInformation h, TouristCity c " +
                "Where c.cityID = h.cityID and c.cityName = '" + cityName + "';" ;
        Cursor cur = mDb.rawQuery(sql,null);

        int position = 0;
        hotel_options = new ArrayList<>();
        hotelMap = new HashMap<>();
        while(cur!=null && cur.moveToNext()){

            hotel_options.add("Name:" + cur.getString(1) + ",Rating:" + cur.getString(3) + ",Rooms:" + cur.getString(2));
            hotelMap.put("Name:" + cur.getString(1) + ",Rating:" + cur.getString(3) + ",Rooms:" + cur.getString(2),cur.getString(0));
            if(cur.getString(0).equalsIgnoreCase(booking.getHotelID()))
                hotelPosition = position;
            position ++;
        }

        sp_hotel.setSelection(hotelPosition);

        cur.close();
    }

    private void getVehicleOptions(SQLiteDatabase mDb, String cityName) {

        String sql = "Select vehicleID, vehicleType, capacity From Vehicle v, TouristCity c " +
                "Where v.cityID = c.cityID and c.cityName = '" + cityName + "';" ;
        Cursor cur = mDb.rawQuery(sql,null);

        int position = 0;
        vehicle_options = new ArrayList<>();
        vehicleMap = new HashMap<>();
        while(cur!=null && cur.moveToNext()){

            vehicle_options.add(cur.getString(1) + "(capacity:" + cur.getString(2) + ")");
            vehicleMap.put(cur.getString(1) + "(capacity:" + cur.getString(2) + ")",cur.getString(0));
            if(cur.getString(0).equalsIgnoreCase(booking.getVehicleID()))
                vehiclePosition = position;
            position ++;

        }

        sp_vehicle.setSelection(vehiclePosition);

        cur.close();

    }

    private void getPackageOptions(SQLiteDatabase mDb, String cityName) {

        String sql = "Select packageID, packageType, days, nights, packagePrice,agencyName From Package p, TouristCity c, agencies a " +
                "Where p.cityID = c.cityID and p.agencyID = a.agencyID and c.cityName = '" + cityName + "';" ;
        Cursor cur = mDb.rawQuery(sql,null);

        int position = 0;
        package_options = new ArrayList<>();
        packageMap = new HashMap<>();
        agencyMap = new HashMap<>();
        while(cur!=null && cur.moveToNext()){

            package_options.add("Type:" + cur.getString(1) + ",Days/Nights:" + cur.getString(2) + "/" + cur.getString(3) + ",Price:" + cur.getString(4));
            packageMap.put("Type:" + cur.getString(1) + ",Days/Nights:" + cur.getString(2) + "/" + cur.getString(3) + ",Price:" + cur.getString(4),cur.getString(0));
            agencyMap.put("Type:" + cur.getString(1) + ",Days/Nights:" + cur.getString(2) + "/" + cur.getString(3) + ",Price:" + cur.getString(4),cur.getString(5));
            if(cur.getString(1).equalsIgnoreCase(booking.getPackageType())
                    && cur.getString(2).equalsIgnoreCase(booking.getDays())
                    && cur.getString(3).equalsIgnoreCase(booking.getNights())
                    && cur.getString(4).equalsIgnoreCase(booking.getPrice()))
                packagePosition = position;
            position ++;
        }

        sp_packageType.setSelection(packagePosition);

        cur.close();



    }

    private void getCityOptions(SQLiteDatabase mDb,String city) {

        String sql = "Select cityID, cityName From touristCity ;";
        Cursor cur = mDb.rawQuery(sql,null);

        int position = 0;
        city_options = new ArrayList<>();
        cityMap = new HashMap<>();
        while(cur!=null && cur.moveToNext()){

            city_options.add(cur.getString(1));
            cityMap.put(cur.getString(1),cur.getString(0));
            if(cur.getString(1).equalsIgnoreCase(city))
                cityPosition = position;
            position ++;

        }

        cur.close();

    }

    private void populateCitySpinner() {


        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, city_options);
        cityAdapter.setNotifyOnChange(true);

        sp_city.setAdapter(cityAdapter);
        sp_city.setSelection(cityPosition);

        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                changedCity = city_options.get(position);

                packageAdapter.clear();
                vehicleAdapter.clear();

                getPackageOptions(mDb,changedCity);
                getVehicleOptions(mDb,changedCity);


//                packageAdapter.addAll(package_options);
//                vehicleAdapter.addAll(vehicle_options);
                populatePackageSpinner();
                populateVehicleSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populatePackageSpinner() {


        packageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, package_options);
        packageAdapter.setNotifyOnChange(true);

        sp_packageType.setAdapter(packageAdapter);

        sp_packageType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String days = package_options.get(position).split(",")[1].split(":")[1].split("/")[0];
                String nights = package_options.get(position).split(",")[1].split(":")[1].split("/")[1];
                String price = package_options.get(position).split(",")[2].split(":")[1];

                String agency = agencyMap.get(package_options.get(position));

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


        vehicleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicle_options);
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

        hotel_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hotel_options);
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
}
