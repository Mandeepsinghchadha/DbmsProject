package com.teamcool.touristum.Activities;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamcool.touristum.DatabaseHelper;
import com.teamcool.touristum.data.model.Agency;
import com.teamcool.touristum.data.model.Filter;
import com.teamcool.touristum.data.model.Package;
import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.TouristCity;
import com.teamcool.touristum.data.model.touristlocations;

import java.util.ArrayList;
import java.util.List;

public class Home_Fragment extends Fragment implements RecyclerViewAdapter.onPackageClickListner{
    View v;

    private List<Package> p1;
    private List<TouristCity>  p2;
    private List<touristlocations> p3;

    private static final String TAG = "LoginActivity";
    private RecyclerView recyclerView;

    private SQLiteDatabase mDb;
    private DatabaseHelper DbHelper;

    private RecyclerViewAdapter adapter;

    private AlertDialog.Builder builder;
    private EditText search,edit;
    //private FloatingActionButton fab_addPackage1;
    private View view;
    private ImageButton imageButton;
    private ChipGroup chipGroup;
    private Agency agency;
    private int vi;
    private static final int view_packages=0;
    private static final int view_city=1;
    private static final int view_locations=2;
    private Spinner spin,spin1;
    private String filter[];
    private ArrayList<Filter> package_filters;
    private ArrayList<Filter> City_filters;
    private ArrayList<Filter> Locations_filters;

    //popup
    private TextView packageID;
    private TextView agencyID;
    private TextView cityID;
    private EditText type,days,nights,price;
    private Button delete,update;
    public Home_Fragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragmenthome,container,false);
        /* getting logged in user Agency */
        agency=LoginActivity.getLoggedInAgency();

        /* database initialized for reading */
        DbHelper = new DatabaseHelper(getContext());
        mDb = DbHelper.getReadableDatabase();

        /* initializing database list and calling defined functions*/
        package_filters=new ArrayList<>();
        City_filters=new ArrayList<>();
        Locations_filters=new ArrayList<>();
        p1=new ArrayList<>();
        p2=new ArrayList<>();
        p3=new ArrayList<>();
        p1=getPackages();
        p2=getCity();
        p3=getLocations();

        /* initializing spinner */
        spin=v.findViewById(R.id.sp);

        /* RecyclerView initialized and adapter*/
        recyclerView= v.findViewById(R.id.recyclerview);
        search=v.findViewById(R.id.search);
        //fab_addPackage1=v.findViewById(R.id.fab_addBooking1);
        /*fab_addPackage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/
        builder=new AlertDialog.Builder(getContext());
        adapter=new RecyclerViewAdapter(getContext(),p1,p2,p3,this);
        /*fab_addPackage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final View view = getLayoutInflater().inflate(R.layout.add_popup, null);
                //To be done
            }
        });*/

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"here_listener");
                view=getLayoutInflater().inflate(R.layout.search_filter,null);
                spin1=view.findViewById(R.id.sp_filter1);
                edit=view.findViewById(R.id.et_filter1);
                imageButton=view.findViewById(R.id.bv_filter1);
                chipGroup=view.findViewById(R.id.cg_filters1);
                addChips();
                if(vi==view_packages){
                    filter=new String[]{"PackageID","AgencyName","PackageType","Days","Nights","City","PackagePrice","AgencyID","cityID"};
                    builder.setTitle("Filter Packages");
                }
                else if(vi==view_city){
                    filter=new String[]{"CityID","CityName","Rating"};
                    builder.setTitle("Filter City");
                }
                else if(vi==view_locations){
                    filter=new String[]{"LocationID","CityID","LocationName","Rating"};
                    builder.setTitle("Filter Location");
                }
                launchDialog();
            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        final String[] s ={"Packages","TouristCity","TouristLocations"};

        /* click listener on spinner */
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(s[position].equals("Packages")){
                    Log.d(TAG,"hereselected1");
                    adapter.setView(view_packages);
                    recyclerView.setAdapter(adapter);
                    vi=view_packages;
                }
                else if(s[position].equals("TouristCity")){
                    Log.d(TAG,"hereselected2");
                    adapter.setView(view_city);recyclerView.setAdapter(adapter);
                    vi=view_city;
                }
                else if(s[position].equals("TouristLocations")){
                    Log.d(TAG,"hereselected3");
                    adapter.setView(view_locations);recyclerView.setAdapter(adapter);
                    vi=view_locations;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> adap=ArrayAdapter.createFromResource(getContext(),R.array.spin,android.R.layout.simple_spinner_item);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adap);

        recyclerView.setAdapter(adapter);
        return v;
    }

    private void addChips() {
        int size = getFilterSize();
        for(int i=0;i<size;i++){
            final Chip chip = new Chip(chipGroup.getContext());
            chip.setCloseIconVisible(true);
            chip.setText(getFilterText(i));
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = chip.getText().toString().split(":")[1];
                    String type = chip.getText().toString().split(":")[0];
                    removeFromFilter(new Filter(type,text));
                    chipGroup.removeView(chip);

                }
            });
            chipGroup.addView(chip);
        }
    }

    private void removeFromFilter(Filter filter) {
        if(vi==view_packages){
            package_filters.remove(filter);
        }
        else if(vi==view_city){
            City_filters.remove(filter);
        }
        else{
            Locations_filters.remove(filter);
        }
    }

    private String getFilterText(int i) {
        if(vi==view_packages){
            return package_filters.get(i).getType() + ":" + package_filters.get(i).getFilter();
        }
        else if(vi==view_city){
            return City_filters.get(i).getType()+":"+City_filters.get(i).getFilter();
        }
        else{
            return Locations_filters.get(i).getType()+":"+Locations_filters.get(i).getFilter();
        }
    }

    private int getFilterSize() {
        if(vi==view_packages){
            return package_filters.size();
        }
        else if(vi==view_city){
            return City_filters.size();
        }
        else{
            return Locations_filters.size();
        }
    }

    private void launchDialog(){
        Log.d(TAG,"here_launch");
        ArrayAdapter<String> search_adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,filter);
        spin1.setAdapter(search_adapter);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String type = filter[position];
                edit.setHint(type);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edit.getText().length()!=0){
                            Log.d(TAG,edit.getText().toString());
                            Filter sfilter = new Filter(type,edit.getText().toString());
                            addtoFilter(sfilter);
                            final Chip chip = new Chip(getContext());
                            chip.setCloseIconVisible(true);
                            chip.setText(type+":"+sfilter.getFilter());
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String text=chip.getText().toString().split(":")[1];
                                    String type=chip.getText().toString().split(":")[0];
                                    removeFromFilter(new Filter(text,type));
                                    chipGroup.removeView(chip);
                                }
                            });
                            chipGroup.addView(chip);
                        }
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        builder.setView(view);
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateData();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    private void addtoFilter(Filter filter) {
        if(vi==view_packages){
                package_filters.add(filter);
        }
        else if(vi==view_city){
            City_filters.add(filter);
        }
        else if(vi==view_locations){
            Locations_filters.add(filter);
        }
    }

    private void updateData() {
        if(vi==view_packages){
            ArrayList<Package> Packages=getFilteredPackages(package_filters);
            p1=Packages;
            Log.d(TAG,"update");
            adapter.setL1(Packages);
            adapter.notifyDataSetChanged();
        }
        else if(vi==view_city){
            ArrayList<TouristCity> City=getFilteredCity(City_filters);
            p2=City;
            adapter.setL2(City);
            adapter.notifyDataSetChanged();
        }
        else if (vi==view_locations){
            ArrayList<touristlocations> locations=getFilteredLocation(Locations_filters);
            p3=locations;
            adapter.setL3(locations);
            adapter.notifyDataSetChanged();
        }
    }

    private ArrayList<touristlocations> getFilteredLocation(ArrayList<Filter> locations_filters) {
        String sql=generateSqlLocation(locations_filters);
        Cursor cur = mDb.rawQuery(sql, null);
        ArrayList<touristlocations> touristlocations=new ArrayList<>();
        while(cur!=null && cur.moveToNext()){
            touristlocations locations = new touristlocations(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3));
            touristlocations.add(locations);
        }
        return touristlocations;
    }

    private ArrayList<TouristCity> getFilteredCity(ArrayList<Filter> city_filters) {
        String sql=generateSqlCity(city_filters);
        Cursor cur = mDb.rawQuery(sql, null);
        ArrayList<TouristCity> touristCities=new ArrayList<>();
        while(cur!=null && cur.moveToNext()){
            TouristCity cities = new TouristCity(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2));
            touristCities.add(cities);
        }
        return touristCities;
    }


    private ArrayList<Package> getFilteredPackages(ArrayList<Filter> package_filters) {
        String sql=generateSqlPackages(package_filters);
        Cursor cur = mDb.rawQuery(sql, null);
        ArrayList<Package> packages=new ArrayList<>();
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
        return packages;
    }
    private String generateSqlCity(ArrayList<Filter> city_filters) {
        String sql="SELECT c.CityID, CityName, Rating "+
                "FROM  agencies a, package p, TouristCity c "+
                "where a.AgencyID=p.AgencyID and p.CityID=c.CityID and a.AgencyID = '"+agency.getAgencyID()+"' ";
        for(int i=0;i<city_filters.size();i++){
            if(i>=1){
                sql=sql+"or ";
            }
            if(i==0){
                sql=sql+"and (";
            }
            if(city_filters.get(i).getType().equalsIgnoreCase("CityID")){
                sql=sql+"c.CityID = "+city_filters.get(i).getFilter()+" ";
            }
            else if(city_filters.get(i).getType().equalsIgnoreCase("CityName")){
                sql=sql+"CityName = "+city_filters.get(i).getFilter()+" ";
            }
            if(i==city_filters.size()-1){
                sql=sql+");";
            }
        }
        return sql;

    }
    private String generateSqlLocation(ArrayList<Filter> city_filters) {
        String sql="SELECT LocationID, l.CityID, LocationName, l.Rating "+
                "FROM agencies a, package p, touristCity c, touristlocations l "+
                "Where a.AgencyID=p.AgencyID and p.CityID=c.CityID and c.CityID=l.CityID and a.AgencyID= '"+agency.getAgencyID()+"' ";
        for(int i=0;i<city_filters.size();i++){
            if(i>=1){
                sql=sql+"or ";
            }
            if(i==0){
                sql=sql+"and (";
            }
            if(city_filters.get(i).getType().equalsIgnoreCase("CityID")){
                sql=sql+"c.CityID = "+city_filters.get(i).getFilter()+" ";
            }
            else if(city_filters.get(i).getType().equalsIgnoreCase("LocationName")){
                sql=sql+"LocationName = "+city_filters.get(i).getFilter()+" ";
            }
            else if(city_filters.get(i).getType().equalsIgnoreCase("LocationID")){
                sql=sql+"LocationID = "+city_filters.get(i).getFilter()+" ";
            }
            if(i==city_filters.size()-1){
                sql=sql+");";
            }
        }
        return sql;

    }
    private String generateSqlPackages(ArrayList<Filter> package_filters) {
        Agency tmp=LoginActivity.getLoggedInAgency();
        StringBuilder sql = new StringBuilder("SELECT packageID, agencyName, packageType, days, nights, cityName, packagePrice,p.agencyID,p.cityID" +
                " FROM Package p,agencies a, TouristCity c " +
                "WHERE p.agencyID = a.agencyID and p.cityID = c.cityID and p.AgencyID = '"+tmp.getAgencyID()+" ' ");

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

    private ArrayList<Package> getPackages(){

        ArrayList<Package> Packages = new ArrayList<>();

        String sql ="SELECT PackageID, AgencyName, PackageType, Days, Nights, CityName, PackagePrice, e.CityID, e.AgencyID \n" +
                " FROM Package e,agencies b,TouristCity t " +
                "where e.CityID=t.CityID and e.AgencyID=b.AgencyID and e.AgencyID = '" +agency.getAgencyID() + "' ;";
        Log.d(TAG,"here1");
        Cursor cur = mDb.rawQuery(sql, null);
        Log.d(TAG,"here1");
        Package pack = null;
        while (cur != null && cur.moveToNext()) {
            pack = new Package(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5),
                    cur.getString(6),
                    cur.getString(7),
                    cur.getString(8));

            Packages.add(pack);
        }
        return Packages;
    }
    private ArrayList<TouristCity> getCity(){
        ArrayList<TouristCity> city=new ArrayList<>();
        String sql="SELECT c.CityID, CityName, Rating \n" +
                "FROM  agencies a, package p, TouristCity c " +
                "where a.AgencyID=p.AgencyID and p.CityID=c.CityID and a.AgencyID = '"+agency.getAgencyID()+"' ;";
        Log.d(TAG,"here2");
        Cursor cur = mDb.rawQuery(sql, null);
        Log.d(TAG,"here2");
        TouristCity cit=null;
        while (cur != null && cur.moveToNext()) {
            cit = new TouristCity(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2));
            city.add(cit);
        }
        return city;
    }
    private ArrayList<touristlocations> getLocations(){
        ArrayList<touristlocations> locations=new ArrayList<>();
        String sql="SELECT LocationID, l.CityID, LocationName, l.Rating \n" +
                "FROM agencies a, package p, touristCity c, touristlocations l " +
                "Where a.AgencyID=p.AgencyID and p.CityID=c.CityID and c.CityID=l.CityID and a.AgencyID= '" + agency.getAgencyID()+"' ;";
        Log.d(TAG,"here3");
        Cursor cur = mDb.rawQuery(sql, null);
        Log.d(TAG,"here3");
        touristlocations  loc=null;
        while (cur != null && cur.moveToNext()) {
            loc = new touristlocations(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3));
            locations.add(loc);
        }
        return locations;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void selectedPackage(int position) {
        updateData();
        final Package p=p1.get(position);
        Log.d(TAG,p.getPackageID());
        final View view = getLayoutInflater().inflate(R.layout.package_popup, null);
        update=view.findViewById(R.id.button_update);
        delete=view.findViewById(R.id.button_delete);
        packageID=view.findViewById(R.id.package_id);
        agencyID=view.findViewById(R.id.agency_id);
        cityID=view.findViewById(R.id.city_id);
        days=view.findViewById(R.id.package_days);
        nights=view.findViewById(R.id.package_nights);
        price=view.findViewById(R.id.package_price);
        type=view.findViewById(R.id.package_type);
        builder.setView(view);
        final AlertDialog dialog;
        packageID.setText("PackageID : "+p.getPackageID());
        agencyID.setText("AgencyID : "+p.getAgencyID());
        cityID.setText("CityID : "+p.getCity());
        days.setText(p.getDays());
        nights.setText(p.getNights());
        price.setText(p.getPrice());
        type.setText(p.getPackageType());
        dialog=builder.create();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("PackageID",p.getPackageID());
                contentValues.put("AgencyID",p.getAgencyID());
                contentValues.put("CityID",p.getCityID());
                contentValues.put("PackageType",type.getText().toString());
                contentValues.put("Days",days.getText().toString());
                contentValues.put("Nights",nights.getText().toString());
                contentValues.put("PackagePrice",price.getText().toString());
                mDb.update("Package",contentValues,"PackageID = ?",new String[]{p.getPackageID()});
                updateData();
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb.delete("package","PackageID = ?",new String[]{p.getPackageID()});
                updateData();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

