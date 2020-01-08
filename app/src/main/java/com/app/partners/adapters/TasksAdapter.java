package com.app.partners.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.partners.R;
import com.app.partners.models.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private ArrayList<Task> tasks = new ArrayList<>();
//    private Context context;

    public TasksAdapter(ArrayList<Task> list) {
        tasks = list;
//        context = ctx;
    }

    public TasksAdapter() { }

    public ArrayList<Task> getList() {
        return this.tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.description.setText(tasks.get(position).description);
        holder.publisherName.setText(tasks.get(position).partnerName);
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(tasks.get(position).timestampPublish);

        holder.date.setText(sf.format(date));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView publisherName, description, date;
        CheckBox isDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            publisherName = itemView.findViewById(R.id.publisherName);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            isDone = itemView.findViewById(R.id.commitedCheck);
        }
    }


}
