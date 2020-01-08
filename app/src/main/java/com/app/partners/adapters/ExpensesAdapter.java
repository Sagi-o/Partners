package com.app.partners.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.partners.R;
import com.app.partners.models.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private ArrayList<Expense> expenses = new ArrayList<>();
//    private Context context;

    public ExpensesAdapter(ArrayList<Expense> list) {
        expenses = list;
//        context = ctx;
    }

    public ExpensesAdapter() { }

    public ArrayList<Expense> getList() {
        return this.expenses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_expense_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.price.setText(String.valueOf(expenses.get(position).value));
        holder.description.setText(expenses.get(position).description);
        holder.payerName.setText(expenses.get(position).payerName);

        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(expenses.get(position).timestamp);

        holder.date.setText(sf.format(date));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView price, description, payerName, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
            payerName = itemView.findViewById(R.id.payerName);
            date = itemView.findViewById(R.id.date);
        }
    }
}
