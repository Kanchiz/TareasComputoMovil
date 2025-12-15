package com.fic.Actividades.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fic.Actividades.R;
import com.fic.Actividades.model.History;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<History> historyList = new ArrayList<>();

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History item = historyList.get(position);
        holder.tvAction.setText(item.action);
        holder.tvDate.setText(item.createdAt);
        holder.tvDetails.setText(item.details);

        if (item.action.contains("DELETE")) holder.tvAction.setTextColor(Color.RED);
        else if (item.action.contains("UPDATE")) holder.tvAction.setTextColor(Color.parseColor("#FFA500")); // Naranja
        else holder.tvAction.setTextColor(Color.parseColor("#008000")); // Verde
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvAction, tvDate, tvDetails;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAction = itemView.findViewById(R.id.tvAction);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDetails = itemView.findViewById(R.id.tvDetails);
        }
    }
}