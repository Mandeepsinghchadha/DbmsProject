package com.teamcool.touristum.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teamcool.touristum.DatabaseHelper;
import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.Agency;

import java.io.IOException;

public class Account_Fragment extends Fragment {
    private EditText edit_name,edit_address,edit_contact,edit_number;
    private ImageView img_name,img_address,img_contact,img_number;
    private TextView text_ID;
    private Button logout;
    private Agency agency;
    private SQLiteDatabase db;
    private DatabaseHelper helper;

    @Override
    public void onPause() {
        super.onPause();
        db=helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("AgencyName",edit_name.getText().toString());
        contentValues.put("AgencyAddress",edit_address.getText().toString());
        contentValues.put("AgencyContact",edit_number.getText().toString());
        contentValues.put("NumberOfPackages",edit_number.getText().toString());

        db.update("agencies",contentValues,"AgencyID = ?",new String[]{agency.getAgencyID()});
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragmentaccount,container,false);
        edit_name=v.findViewById(R.id.edit_name);
        edit_address=v.findViewById(R.id.edit_address);
        edit_contact=v.findViewById(R.id.edit_contact);
        edit_number=v.findViewById(R.id.edit_number);
        logout=v.findViewById(R.id.logout);
        text_ID=v.findViewById(R.id.TextID);
        img_name=v.findViewById(R.id.img_name);
        img_address=v.findViewById(R.id.img_address);
        img_contact=v.findViewById(R.id.img_contact);
        img_number=v.findViewById(R.id.img_number);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        helper=new DatabaseHelper(getContext());

        img_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_name.setInputType(InputType.TYPE_CLASS_TEXT);
                edit_name.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        edit_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    edit_name.setInputType(InputType.TYPE_NULL);
                }
                return false;
            }
        });
        edit_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    edit_address.setInputType(InputType.TYPE_NULL);
                }
                return false;
            }
        });
        edit_contact.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    edit_contact.setInputType(InputType.TYPE_NULL);
                }
                return false;
            }
        });
        edit_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    edit_number.setInputType(InputType.TYPE_NULL);
                }
                return false;
            }
        });
        img_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_address.setInputType(InputType.TYPE_CLASS_TEXT);
                edit_address.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        img_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_contact.setInputType(InputType.TYPE_CLASS_TEXT);
                edit_contact.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        img_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_number.setInputType(InputType.TYPE_CLASS_TEXT);
                edit_number.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        try {
            helper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        agency=LoginActivity.getLoggedInAgency();

        if(agency!=null){
            edit_name.setText(agency.getAgencyName());
            text_ID.setText("ID : "+agency.getAgencyID());
            edit_contact.setText(agency.getAgencyContact());
            edit_address.setText(agency.getAgencyAddress());
            edit_number.setText(agency.getNoOfPackages());
        }
        return v;
    }
}
