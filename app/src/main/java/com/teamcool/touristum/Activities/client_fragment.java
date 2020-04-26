package com.teamcool.touristum.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.teamcool.touristum.DatabaseHelper;
import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.Agency;
import com.teamcool.touristum.data.model.Client;
import com.teamcool.touristum.data.model.Filter;
import com.teamcool.touristum.data.model.TouristCity;

import java.util.ArrayList;
import java.util.List;

public class client_fragment extends Fragment implements ClientAdapter.onClientClickListner{
    private RecyclerView recyclerView;
    private ClientAdapter clientAdapter;
    private EditText edit_search,edit_filter;
    private Agency agency;
    private List<Client> clients;
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private final String TAG="Clients";
    //extra
    private String[] filter_options;
    private AlertDialog.Builder builder;

    private View dialog_view;

    private Button update,delete;
    private Spinner spin;
    private ChipGroup chipGroup;
    private ImageButton imageButton;
    private TextView ID;
    private EditText e1_name,e2_contact,e3_address,e4_email;
    private ImageView im1_name,im2_contact,im3_address,im4_email;

    private ArrayList<Filter> client_filter;
    public client_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragmentclients,container,false);
        recyclerView=v.findViewById(R.id.client_recycler);
        edit_search=v.findViewById(R.id.search_client);edit_search.setFocusable(false);
        agency=LoginActivity.getLoggedInAgency();
        helper = new DatabaseHelper(getContext());
        db = helper.getWritableDatabase();
        clients=getClients();
        client_filter=new ArrayList<>();
        builder = new AlertDialog.Builder(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        clientAdapter=new ClientAdapter(clients, getContext(), this);
        edit_search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                dialog_view=getLayoutInflater().inflate(R.layout.search_filter,null);
                spin=dialog_view.findViewById(R.id.sp_filter1);
                edit_filter=dialog_view.findViewById(R.id.et_filter1);
                imageButton=dialog_view.findViewById(R.id.bv_filter1);
                chipGroup=dialog_view.findViewById(R.id.cg_filters1);
                addChips();
                filter_options=new String[]{"ClientID","ClientName","ClientAddress","ClientContact","ClientEmail"};
                builder.setTitle("Filter Clients");
                launchDialog();
            }
        });
        recyclerView.setAdapter(clientAdapter);
        return v;
    }

    private void launchDialog() {
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, filter_options);
        if(spin==null){
            Log.d(TAG,"empty");
        }
        spin.setAdapter(filterAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String type = filter_options[position];
                edit_filter.setHint(type);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edit_filter.getText().length() != 0){
                            Filter filter = new Filter(type,edit_filter.getText().toString());
                            client_filter.add(filter);
                            final Chip chip = new Chip(getContext());
                            chip.setCloseIconVisible(true);
                            chip.setText(type + ":" + filter.getFilter());
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String text = chip.getText().toString().split(":")[1];
                                    String type = chip.getText().toString().split(":")[0];
                                    client_filter.remove(new Filter(type,text));
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

    private void addChips() {
        int size=client_filter.size();
        for(int i=0;i<size;i++){
            final Chip chip = new Chip(chipGroup.getContext());
            chip.setCloseIconVisible(true);
            chip.setText(client_filter.get(i).getType() + ":" + client_filter.get(i).getFilter());
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = chip.getText().toString().split(":")[1];
                    String type = chip.getText().toString().split(":")[0];
                    client_filter.remove(new Filter(type,text));
                    chipGroup.removeView(chip);
                }
            });
            chipGroup.addView(chip);
        }
    }

    private void updateData() {
        ArrayList<Client> clie=getFilteredClients(client_filter);
        clientAdapter.setClients(clie);
        clientAdapter.notifyDataSetChanged();
    }

    private ArrayList<Client> getFilteredClients(ArrayList<Filter> client_filter) {
        String sql = generateSqlEmployees(client_filter);
        Cursor cur=db.rawQuery(sql,null);
        ArrayList<Client> Client=new ArrayList<>();
        while(cur!=null && cur.moveToNext()){
            Client cc=new Client(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5));
            Client.add(cc);
        }
        return Client;
    }

    private String generateSqlEmployees(ArrayList<Filter> client_filter) {
        String sql="SELECT cl.ClientID, ClientName, ClientAddress, ClientContact, ClientEmail ,a.NumberOfPackages "+
                "FROM  agencies a, package p, TouristCity c, Booking b, Client cl "+
                "where a.AgencyID=p.AgencyID and p.CityID=c.CityID and c.CityID=b.BookingID and b.ClientID=cl.ClientID and a.AgencyID = '"+agency.getAgencyID()+"' ";
        for(int i=0;i<client_filter.size();i++){
            if(i>=1){
                sql=sql+"or ";
            }
            if(i==0){
                sql=sql+"and (";
            }
            if(client_filter.get(i).getType().equalsIgnoreCase("ClientID")){
                sql=sql+" cl.ClientID = " + client_filter.get(i).getFilter() ;
            }
            else if(client_filter.get(i).getType().equalsIgnoreCase("ClientName")){
                sql=sql+" cl.ClientName = " + client_filter.get(i).getFilter() ;
            }
            else if(client_filter.get(i).getType().equalsIgnoreCase("ClientContact")){
                sql=sql+" cl.ClientContact = " + client_filter.get(i).getFilter() ;
            }
            else if(client_filter.get(i).getType().equalsIgnoreCase("ClientAddress")){
                sql=sql+" cl.ClientAddress = " + client_filter.get(i).getFilter() ;
            }
            else if(client_filter.get(i).getType().equalsIgnoreCase("ClientEmail")){
                sql=sql+" cl.ClientEmail = " + client_filter.get(i).getFilter() ;
            }
            if (i == client_filter.size() - 1){
                sql=sql+");";
            }
        }
        return sql;
    }

    private List<Client> getClients() {
        ArrayList<Client> c=new ArrayList<>();
        String sql="SELECT cl.ClientID, ClientName, ClientAddress, ClientContact, ClientEmail ,a.NumberOfPackages "+
                "FROM  agencies a, package p, TouristCity c, Booking b, Client cl "+
                "where a.AgencyID=p.AgencyID and p.CityID=c.CityID and c.CityID=b.BookingID and b.ClientID=cl.ClientID and a.AgencyID = '"+agency.getAgencyID()+"' ;";
        Cursor cur = db.rawQuery(sql, null);
        Client cli=null;
        while (cur != null && cur.moveToNext()) {
            cli = new Client(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(4),
                    cur.getString(5));
            c.add(cli);
        }
        return c;
    }

    @Override
    public void selectedClient(int position) {
        final Client curr=clients.get(position);
        Log.d(TAG,curr.getClientID());
        final View view = getLayoutInflater().inflate(R.layout.client_popup, null);
        update=view.findViewById(R.id.but_update);
        delete=view.findViewById(R.id.but_delete);
        e1_name=view.findViewById(R.id.client_name);
        e2_contact=view.findViewById(R.id.client_contact);
        e3_address=view.findViewById(R.id.client_address);
        e4_email=view.findViewById(R.id.client_email);
        im1_name=view.findViewById(R.id.image_name);
        im2_contact=view.findViewById(R.id.image_contact);
        im3_address=view.findViewById(R.id.image_address);
        im4_email=view.findViewById(R.id.image_email);
        builder.setView(view);
        final AlertDialog dialog;
        ID=view.findViewById(R.id.client_id);
        ID.setText("ClientID : "+curr.getClientID());
        im1_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1_name.setInputType(InputType.TYPE_CLASS_TEXT);
                e1_name.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        im2_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2_contact.setInputType(InputType.TYPE_CLASS_TEXT);
                e2_contact.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        im3_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e3_address.setInputType(InputType.TYPE_CLASS_TEXT);
                e3_address.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        im4_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e4_email.setInputType(InputType.TYPE_CLASS_TEXT);
                e4_email.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        e1_name.setText(curr.getClientName());
        e2_contact.setText(curr.getClientContact());
        e3_address.setText(curr.getClientAddress());
        e4_email.setText(curr.getClientEmail());
        dialog=builder.create();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("ClientName",e1_name.getText().toString());
                contentValues.put("ClientAddress",e3_address.getText().toString());
                contentValues.put("ClientContact",e2_contact.getText().toString());
                contentValues.put("ClientEmail",e4_email.getText().toString());
                Log.d(TAG,curr.getClientName());
                db.update("Client",contentValues,"ClientID = ?",new String[]{curr.getClientID()});
                updateData();
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete("Client","ClientID = ?",new String[]{curr.getClientID()});
                updateData();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
