package com.teamcool.touristum.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.Employee;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ManagerEmployeeAdapter extends RecyclerView.Adapter<ManagerEmployeeAdapter.EmployeeViewHolder> {

    private ArrayList<Employee> employees;
    private Context context;
    private onEmployeeClickListener employeeClickListener;

    public interface onEmployeeClickListener{
        void selectedEmployee(Employee employee);
    }

    public ManagerEmployeeAdapter(ArrayList<Employee> employees, Context context, onEmployeeClickListener employeeClickListener) {
        this.employees = employees;
        this.context = context;
        this.employeeClickListener = employeeClickListener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_employee,parent,false);
        return new EmployeeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {

        Employee employee = employees.get(position);
        holder.tv_name.setText("Name : " + employee.getEmp_name() + "(ID : " + employee.getEmp_id() + ")");
        holder.tv_branch.setText("Branch : " + employee.getBranch() + "(ID : " + employee.getBranchID() + ")q");
        holder.tv_email.setText("Email : " + employee.getEmp_email());
        holder.tv_contact.setText("Contact : " + employee.getEmp_contact());
        holder.tv_address.setText("Address : " + employee.getEmp_address());
        holder.tv_salary.setText("Salary : " + employee.getSalary());
        holder.tv_type.setText("Type : " + employee.getEmp_type());


    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public void setEmployees(ArrayList<Employee> list){
        employees = list;
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_contact, tv_address, tv_branch, tv_email, tv_type, tv_salary;


        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_contact = itemView.findViewById(R.id.tv_contact);
            tv_salary = itemView.findViewById(R.id.tv_salary);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_branch = itemView.findViewById(R.id.tv_branch);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    employeeClickListener.selectedEmployee(employees.get(getAdapterPosition()));
                }
            });
        }
    }
}
