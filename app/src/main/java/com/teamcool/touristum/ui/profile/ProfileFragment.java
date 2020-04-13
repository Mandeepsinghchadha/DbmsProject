package com.teamcool.touristum.ui.profile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.teamcool.touristum.DatabaseHelper;
import com.teamcool.touristum.Activities.EmployeeManagerActivity;
import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.Employee;

import java.io.IOException;

public class ProfileFragment extends Fragment {


    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    private Employee employee;

    private TextView tv_id;
    private EditText et_name, et_address, et_contact, et_email,et_salary,et_branch,et_type_;
    private ImageView iv_edit_name, iv_edit_address, iv_edit_contact, iv_edit_email, iv_profile;

    public static final String TAG = "PROFILEPAGE";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        et_name = root.findViewById(R.id.et_name);
        et_address = root.findViewById(R.id.et_address);
        et_contact = root.findViewById(R.id.et_contact);
        et_email = root.findViewById(R.id.et_email);
        et_salary = root.findViewById(R.id.et_salary);
        et_branch = root.findViewById(R.id.et_branch);
        et_type_ = root.findViewById(R.id.et_type);

        tv_id = root.findViewById(R.id.tv_id);

        iv_edit_address = root.findViewById(R.id.iv_edit_address);
        iv_edit_contact = root.findViewById(R.id.iv_edit_contact);
        iv_edit_email = root.findViewById(R.id.iv_edit_email);
        iv_profile = root.findViewById(R.id.iv_profile);

        mDbHelper = new DatabaseHelper(getContext());

        iv_edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_address.setInputType(InputType.TYPE_CLASS_TEXT);
                et_address.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        iv_edit_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_contact.setInputType(InputType.TYPE_CLASS_TEXT);
                et_contact.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        iv_edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_email.setInputType(InputType.TYPE_CLASS_TEXT);
                et_email.requestFocus();
                getActivity();
                InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }
        });

        et_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    et_name.setInputType(InputType.TYPE_NULL);

                }
                return false;
            }
        });

        et_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    et_address.setInputType(InputType.TYPE_NULL);

                }
                return false;
            }
        });

        et_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    et_email.setInputType(InputType.TYPE_NULL);

                }
                return false;
            }
        });

        et_contact.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    et_contact.setInputType(InputType.TYPE_NULL);

                }
                return false;
            }
        });






        try {
            mDbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        employee = EmployeeManagerActivity.getLoggedInEmployee();

        if(employee != null){
            et_name.setText(employee.getEmp_name());
            et_contact.setText(String.valueOf(employee.getEmp_contact()));
            et_address.setText(employee.getEmp_address());
            et_email.setText(employee.getEmp_email());
            et_salary.setText(String.valueOf(employee.getSalary()));
            et_type_.setText(employee.getEmp_type());
            et_branch.setText(employee.getBranch());
            tv_id.setText("ID : " + employee.getEmp_id());
        }




        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause: ");

        mDb = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("EmployeeEmail",et_email.getText().toString());
        contentValues.put("EmployeeAddress",et_address.getText().toString());
        contentValues.put("EmployeeContact",et_contact.getText().toString());

        mDb.update("Employee",contentValues,"employeeID = ?",new String[]{employee.getEmp_id()});

        String sql = "select employeeID, employeeEmail from employee where employeeID = 702";
        Cursor cur = mDb.rawQuery(sql,null);
        while (cur != null && cur.moveToNext()) {
            Log.d(TAG, "onPause: " + cur.getString(0) + " " + cur.getString(1));
        }



    }
}