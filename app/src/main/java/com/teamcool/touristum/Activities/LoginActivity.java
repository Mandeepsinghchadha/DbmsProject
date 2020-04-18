package com.teamcool.touristum.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.teamcool.touristum.DatabaseHelper;
import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.Agency;
import com.teamcool.touristum.data.model.Employee;
import com.teamcool.touristum.data.model.LoggedInUser;
import com.teamcool.touristum.data.model.Package;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity  {

    private static final String TAG = "LoginActivity" ;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    private EditText et_username,et_password;
    private Button login,register;
    private static Employee emp;
    private static Agency agency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        login = findViewById(R.id.bv_login);
        register = findViewById(R.id.bv_register);


        mDbHelper = new DatabaseHelper(this);
        try {
            mDbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDbHelper.openDataBase();
        mDbHelper.close();
        mDb = mDbHelper.getReadableDatabase();

        File f = new File(
                "/data/data/com.teamcool.touristum/shared_prefs/LoginCreds.xml");
        if (!f.exists()) {
            AddData(mDb);
            Log.d(TAG, "onCreate: " + "data added");
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("LoginCreds", Context.MODE_PRIVATE);
                if(sharedPreferences.contains(username)){
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString(username,"");
                    LoggedInUser obj = gson.fromJson(json, LoggedInUser.class);
                    Log.d(TAG, "onClick: " + obj.getPassword());

                    if(obj.getPassword().equals(password)){
                        emp = getEmployee(mDb,username);
                        if(obj.getType().equals("manager")){

                            Intent intent = new Intent(LoginActivity.this, EmployeeManagerActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(obj.getType().equals("customer_executive")){

                            Intent intent = new Intent(LoginActivity.this, EmployeeCustomerExecutiveActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(obj.getType().equals("agency")){
                            Intent intent=new Intent(LoginActivity.this,AgencyActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    public static Employee getLoggedInEmployee(){

        return emp;

    }
    public static Agency getLoggedInAgency(){ return agency;}

    private Employee getEmployee(SQLiteDatabase mDb, String username) {

        String empId = username.substring(8,username.length());
        Log.d(TAG, "getEmployee: " + empId);
        String sql ="SELECT employeeID, employeeName, employeeAddress, employeeEmail, employeeContact, employeeType, employeeSalary, branchName, e.branchID \n" +
                " FROM employee e,branch b " +
                "where e.BranchID=b.BranchID and e.employeeID='" + empId + "';";
        Cursor cur = mDb.rawQuery(sql, null);

        Employee emp = null;
        while (cur != null && cur.moveToNext()) {

            emp = new Employee(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(5),
                    cur.getString(7),
                    String.format("%.0f",cur.getFloat(4)),
                    cur.getString(6),
                    cur.getString(8));
        }
        return emp;
    }

    private void AddData(SQLiteDatabase mDb) {

        SharedPreferences sharedPreferences = getSharedPreferences("LoginCreds", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Gson gson = new Gson();
        String json;

        String sql ="SELECT employeeID, employeeName, employeeAddress, employeeEmail, employeeContact, employeeType, employeeSalary, e.branchID \n" +
                " FROM employee e;";
        Cursor cur = mDb.rawQuery(sql, null);

        Employee emp = null;
        while (cur != null && cur.moveToNext()) {

            LoggedInUser user = new LoggedInUser(cur.getString(0),cur.getString(5),cur.getString(5));
            json = gson.toJson(user);
            edit.putString("employee"+cur.getString(0), json);
            edit.apply();
        }

        sql ="SELECT ClientID,ClientName,ClientAddress,ClientContact,ClientEmail from Client";
        cur = mDb.rawQuery(sql, null);
        while (cur != null && cur.moveToNext()) {

            LoggedInUser user = new LoggedInUser(cur.getString(0),"client","client");
            json = gson.toJson(user);
            edit.putString("client"+cur.getString(0), json);
            edit.apply();
        }

        sql ="SELECT AgencyID,AgencyName from agencies";
        cur = mDb.rawQuery(sql, null);
        while (cur != null && cur.moveToNext()) {

            LoggedInUser user = new LoggedInUser(cur.getString(0),"agency","agency");
            json = gson.toJson(user);
            edit.putString("agency"+cur.getString(0), json);
            edit.apply();
        }

        sql ="SELECT HotelID,HotelName from hotelInformation";
        cur = mDb.rawQuery(sql, null);
        while (cur != null && cur.moveToNext()) {

            LoggedInUser user = new LoggedInUser(cur.getString(0),"hotel","hotel");
            json = gson.toJson(user);
            edit.putString("hotel"+cur.getString(0), json);
            edit.apply();
        }
        cur.close();

    }



}
