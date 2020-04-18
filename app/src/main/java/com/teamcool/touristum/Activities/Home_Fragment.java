package com.teamcool.touristum.Activities;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcool.touristum.DatabaseHelper;
import com.teamcool.touristum.data.model.Agency;
import com.teamcool.touristum.data.model.Package;
import com.teamcool.touristum.R;

import java.util.ArrayList;
import java.util.List;

public class Home_Fragment extends Fragment {
    View v;
    private RecyclerView recyclerView;
    private SQLiteDatabase mDb;
    private RecyclerView.Adapter adapter;
    private List<Package> p;
    private DatabaseHelper DbHelper;
    private AlertDialog.Builder builder;
    private Agency agency;
    public Home_Fragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_home,container,false);
        agency=LoginActivity.getLoggedInAgency();
        recyclerView=(RecyclerView) v.findViewById(R.id.recyclerview);

        DbHelper = new DatabaseHelper(getContext());
        mDb = DbHelper.getReadableDatabase();
        p=getPackages();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter=new RecyclerViewAdapter(getContext(),p);
        recyclerView.setAdapter(adapter);
        return v;
    }
    private ArrayList<Package> getPackages(){

        ArrayList<Package> Packages = new ArrayList<>();
        String sql ="SELECT PackageID, AgencyName,PackageType, Days, Nights, City,packagePrice, e.CityID,e.AgencyID \n" +
                " FROM Package e,Agency b,TouristCity t" +
                "where e.CityID=t.CityID and e.AgencyID=b.AgencyID and e.AgencyID = " +agency.getAgencyID() + " ;";
        Cursor cur = mDb.rawQuery(sql, null);
        Package pack = null;
        while (cur != null && cur.moveToNext()) {
            pack = new Package(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(5),
                    cur.getString(7),
                    String.format("%.0f",cur.getFloat(4)),
                    cur.getString(6),
                    cur.getString(8));

            Packages.add(pack);
        }
        return Packages;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

