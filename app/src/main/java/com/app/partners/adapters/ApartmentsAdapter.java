package com.app.partners.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.partners.R;
import com.app.partners.models.Apartment;
import com.app.partners.models.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ApartmentsAdapter extends RecyclerView.Adapter<ApartmentsAdapter.ViewHolder> {

    private ArrayList<Apartment> apartments = new ArrayList<>();
//    private Context context;

    public ApartmentsAdapter(ArrayList<Apartment> list) {
        apartments = list;
//        context = ctx;
    }

    public ApartmentsAdapter() { }

    public ArrayList<Apartment> getList() {
        return this.apartments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_apartment_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.addres.setText(apartments.get(position).address);

    }

    @Override
    public int getItemCount() {
        return apartments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView addres;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addres = itemView.findViewById(R.id.address);
        }
    }
}
