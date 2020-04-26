package com.teamcool.touristum.Activities;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcool.touristum.data.model.Package;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcool.touristum.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context context;
    List<Package> l;
    Dialog dialog;

    public RecyclerViewAdapter(Context context, List<Package> l) {
        this.context = context;
        this.l = l;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(context).inflate(R.layout.item_package,parent,false);
        MyViewHolder holder=new MyViewHolder(v);

        dialog=new Dialog(context);
        dialog.setContentView(R.layout.click_fragment);


        holder.item_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Test Click",Toast.LENGTH_SHORT).show();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.t_city.setText(l.get(position).getCity());
        holder.t_price.setText(l.get(position).getPrice());
        holder.t_packageid.setText(l.get(position).getPackageID());
        holder.t_agencyname.setText(l.get(position).getAgencyName());
        holder.t_packagetype.setText(l.get(position).getPackageType());
        holder.t_days.setText(l.get(position).getDays());
        holder.t_nights.setText(l.get(position).getNights());
        holder.t_cityid.setText(l.get(position).getCityID());
        holder.t_agencyid.setText(l.get(position).getAgencyID());
    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout item_package;
        private TextView t_city;
        private TextView t_price;
        private TextView t_packageid;
        private TextView t_agencyname;
        private TextView t_packagetype;
        private TextView t_days;
        private TextView t_nights;
        private TextView t_cityid;
        private TextView t_agencyid;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_package=(LinearLayout) itemView.findViewById(R.id.item_package);
            t_city=(TextView)itemView.findViewById(R.id.city);
            t_price=(TextView)itemView.findViewById(R.id.price);
            t_packageid=(TextView)itemView.findViewById(R.id.packageid);
            t_agencyname=(TextView)itemView.findViewById(R.id.agencyname);
            t_packagetype=(TextView)itemView.findViewById(R.id.packagetype);
            t_days=(TextView)itemView.findViewById(R.id.days);
            t_nights=(TextView)itemView.findViewById(R.id.nights);
            t_cityid=(TextView)itemView.findViewById(R.id.cityid);
            t_agencyid=(TextView)itemView.findViewById(R.id.agencyid);
        }
    }
}
