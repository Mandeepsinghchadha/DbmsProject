package com.teamcool.touristum.ui.employees;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.teamcool.touristum.Activities.EmployeeManagerActivity;
import com.teamcool.touristum.Adapters.ManagerEmployeeAdapter;
import com.teamcool.touristum.DatabaseHelper;
import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.Booking;
import com.teamcool.touristum.data.model.Employee;
import com.teamcool.touristum.data.model.Filter;

import java.util.ArrayList;
import java.util.List;

public class EmployeesFragment extends Fragment {

    private RecyclerView rv_data;
    private EditText et_search, et_filter;
    private ImageButton ib_seeach;

    private Employee currEmployee;
    private ArrayList<Employee> employees;

    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    private ManagerEmployeeAdapter managerEmployeeAdapter;

    private String[] filter_options;
    private AlertDialog.Builder builder;
    private View dialog_view;
    private Spinner sp_dialog_filter;
    private ChipGroup cg_filters;
    private ImageButton bv_filter;

    private ArrayList<Filter> employee_filters;

    public static final int VIEW_MODE_BOOKING = 1;
    public static final int VIEW_MODE_AGENCY = 2;
    public static final int VIEW_MODE_PACKAGE = 3;
    public static final int VIEW_MODE_HOTEL = 4;
    public static final int VIEW_MODE_CLIENT = 5;
    private int VIEW_MODE;

    public static final String TAG = "EmployeesFragnent";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_employees, container, false);
//        final TextView textView = root.findViewById(R.id.text_employees);

        rv_data = root.findViewById(R.id.rv_employee_data);
        et_search = root.findViewById(R.id.et_search_employees);
        ib_seeach = root.findViewById(R.id.ib_search_employees);

        currEmployee = EmployeeManagerActivity.getLoggedInEmployee();

        mDbHelper = new DatabaseHelper(getContext());
        mDb = mDbHelper.getReadableDatabase();

        employees = getEmployees();
        employee_filters = new ArrayList<>();

        builder = new AlertDialog.Builder(getContext());

        rv_data.setLayoutManager(new LinearLayoutManager(getContext()));
        managerEmployeeAdapter = new ManagerEmployeeAdapter(employees,getContext());
        rv_data.setAdapter(managerEmployeeAdapter);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_view = getLayoutInflater().inflate(R.layout.dialog_filter,null);
                sp_dialog_filter = dialog_view.findViewById(R.id.sp_filter);
                et_filter = dialog_view.findViewById(R.id.et_filter);
                bv_filter = dialog_view.findViewById(R.id.bv_filter);
                cg_filters = dialog_view.findViewById(R.id.cg_filters);

                addChips();

                filter_options = new String[]{"employeeID", "employeeName", "employeeAddress","employeeEmail", "employeeContact", "employeeType", "employeeSalary", "branchName", "branchID"};
                builder.setTitle("Filter Employees");


                launchDialog();

            }
        });


        return root;
    }

    private ArrayList<Employee> getEmployees(){

        ArrayList<Employee> employees = new ArrayList<>();

        Log.d(TAG, "getEmployee: " + currEmployee.getEmp_id());
        String sql ="SELECT employeeID, employeeName, employeeAddress, employeeEmail, employeeContact, employeeType, employeeSalary, branchName, e.branchID \n" +
                " FROM employee e,branch b " +
                "where e.BranchID=b.BranchID and e.employeeID != " + currEmployee.getEmp_id() + " and employeeType != 'manager' and employeeType != 'CEO';";
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

            employees.add(emp);
        }

        return employees;

    }

    private void addChips() {
        int size = employee_filters.size();
        for(int i=0;i<size;i++){
            final Chip chip = new Chip(cg_filters.getContext());
            chip.setCloseIconVisible(true);
            chip.setText(employee_filters.get(i).getType() + ":" + employee_filters.get(i).getFilter());
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = chip.getText().toString().split(":")[1];
                    String type = chip.getText().toString().split(":")[0];
                    employee_filters.remove(new Filter(type,text));
                    cg_filters.removeView(chip);

                }
            });

            cg_filters.addView(chip);

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
                            employee_filters.add(filter);
                            final Chip chip = new Chip(getContext());
                            chip.setCloseIconVisible(true);
                            chip.setText(type + ":" + filter.getFilter());
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String text = chip.getText().toString().split(":")[1];
                                    String type = chip.getText().toString().split(":")[0];
                                    employee_filters.remove(new Filter(type,text));
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

        ArrayList<Employee> employees = getFilteredEmployees(employee_filters);
        managerEmployeeAdapter.setEmployees(employees);
        managerEmployeeAdapter.notifyDataSetChanged();
    }

    private ArrayList<Employee> getFilteredEmployees(ArrayList<Filter> employee_filters) {

        String sql = generateSqlEmployees(employee_filters);
        Cursor cur = mDb.rawQuery(sql, null);

        ArrayList<Employee> employees = new ArrayList<>();

        while(cur!=null && cur.moveToNext()){

            Employee emp = new Employee(cur.getString(0),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3),
                    cur.getString(5),
                    cur.getString(7),
                    String.format("%.0f",cur.getFloat(4)),
                    cur.getString(6),
                    cur.getString(8));

            employees.add(emp);


        }
        Log.d(TAG, "generated: employees" + employees.size());

        return employees;

    }

    private String generateSqlEmployees(ArrayList<Filter> employee_filters) {
        StringBuilder sql = new StringBuilder("SELECT employeeID, employeeName, employeeAddress, employeeEmail, employeeContact, employeeType, employeeSalary, branchName, e.branchID \n" +
                " FROM employee e,branch b " +
                "where e.BranchID=b.BranchID and e.employeeID != " + currEmployee.getEmp_id() + " and employeeType != 'manager' and employeeType != 'CEO' ");

        for(int i=0;i<employee_filters.size();i++){

            if(i >=1){
                sql.append("or ");

            }

            if(i == 0){
                sql.append("and (");
            }


            if(employee_filters.get(i).getType().equalsIgnoreCase("employeeID")){
                sql.append("e.employeeID = " + employee_filters.get(i).getFilter() + " ");

            }
            else if(employee_filters.get(i).getType().equalsIgnoreCase("employeeName")){
                sql.append("e.employeeName = " + employee_filters.get(i).getFilter() + " ");

            }
            else if(employee_filters.get(i).getType().equalsIgnoreCase("employeeAddress")){
                sql.append("e.employeeAddress = " + employee_filters.get(i).getFilter() + " ");

            }
            else if(employee_filters.get(i).getType().equalsIgnoreCase("employeeEmail")){
                sql.append("e.employeeEmail = " + employee_filters.get(i).getFilter() + " ");

            }
            else if(employee_filters.get(i).getType().equalsIgnoreCase("employeeContact")){
                sql.append("e.employeeContact = " + employee_filters.get(i).getFilter() + " ");

            }
            else if(employee_filters.get(i).getType().equalsIgnoreCase("employeeType")){
                sql.append("e.employeeType = " + employee_filters.get(i).getFilter() + " ");

            }
            else if(employee_filters.get(i).getType().equalsIgnoreCase("employeeSalary")){
                sql.append("e.employeeSalary = " + employee_filters.get(i).getFilter() + " ");

            }
            else if(employee_filters.get(i).getType().equalsIgnoreCase("branchName")){
                sql.append("b.branchName = " + employee_filters.get(i).getFilter() + " ");

            }
            else if(employee_filters.get(i).getType().equalsIgnoreCase("branchID")){
                sql.append("e.branchID = " + employee_filters.get(i).getFilter() + " ");

            }

            if (i == employee_filters.size() - 1){
                sql.append(");");
            }

        }

        Log.d(TAG, "generatedSql: " + sql.toString());

        return sql.toString();
    }

}