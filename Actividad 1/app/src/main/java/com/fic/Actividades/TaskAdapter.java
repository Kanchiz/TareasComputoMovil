package com.fic.Actividades; // <--- ESTO ES LO QUE ARREGLA EL ERROR

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Task task);
        void onItemDelete(Task task);
        void onTaskStatusChanged(Task task);
    }

    public TaskAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        // Solo ponemos el título (YA NO LA FECHA)
        holder.tvTitle.setText(task.title);

        holder.cbCompleted.setOnCheckedChangeListener(null);
        holder.cbCompleted.setChecked(task.isCompleted);

        // Listeners
        holder.itemView.setOnClickListener(v -> listener.onItemClick(task));

        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemDelete(task);
            return true;
        });

        holder.cbCompleted.setOnClickListener(v -> {
            task.isCompleted = holder.cbCompleted.isChecked();
            listener.onTaskStatusChanged(task);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        CheckBox cbCompleted;
        // La fecha (tvDate) ya no existe aquí

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            cbCompleted = itemView.findViewById(R.id.cbCompleted);
        }
    }
}