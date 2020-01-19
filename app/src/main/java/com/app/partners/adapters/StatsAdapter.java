package com.app.partners.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.partners.R;
import com.app.partners.activities.utils.NameValue;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {

    private ArrayList<NameValue> nameValues = new ArrayList<>();

    public StatsAdapter(ArrayList<NameValue> list) {
        nameValues = list;
    }

    public StatsAdapter() { }

    public ArrayList<NameValue> getList() {
        return this.nameValues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_stat_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(nameValues.get(position).partnerName + " Spent " + nameValues.get(position).value + " ₪");
    }

    @Override
    public int getItemCount() {
        return nameValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
