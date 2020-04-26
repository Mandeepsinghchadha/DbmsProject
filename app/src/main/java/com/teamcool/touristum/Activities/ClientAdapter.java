package com.teamcool.touristum.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcool.touristum.R;
import com.teamcool.touristum.data.model.Client;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {
    private List<Client> clients;
    private Context context;
    private onClientClickListner listner;


    public ClientAdapter(List<Client> clients, Context context, onClientClickListner listner) {
        this.clients = clients;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_client,parent,false);
        return new ClientViewHolder(itemView,listner);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client=clients.get(position);
        holder.t_name.setText("ClientName : " + client.getClientName()+" ");
        holder.t_ID.setText("ClientID : " + client.getClientID()+" ");
        holder.t_contact.setText("ClientContact : " + client.getClientContact()+" ");
        holder.t_email.setText("ClientEmail : " + client.getClientEmail()+" ");
        holder.t_address.setText("ClientAddress : " + client.getClientAddress()+" ");

    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t_name,t_ID,t_contact,t_email,t_address;
        onClientClickListner listner;
        public ClientViewHolder(@NonNull View itemView,onClientClickListner clickListner) {
            super(itemView);
            t_ID=itemView.findViewById(R.id.ClientID);
            t_name=itemView.findViewById(R.id.ClientName);
            t_contact=itemView.findViewById(R.id.ClientContact);
            t_address=itemView.findViewById(R.id.ClientAddress);
            t_email=itemView.findViewById(R.id.ClientEmail);
            this.listner=clickListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listner.selectedClient(getAdapterPosition());
        }
    }
    public interface onClientClickListner{
        void selectedClient(int position);
    }
}
