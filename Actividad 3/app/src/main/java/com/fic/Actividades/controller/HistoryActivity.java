package com.fic.Actividades.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fic.Actividades.R;
import com.fic.Actividades.model.AppDatabase;
import com.fic.Actividades.model.History;
import com.fic.Actividades.view.HistoryAdapter;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private AppDatabase database;
    private HistoryAdapter adapter;
    private Spinner spinnerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        database = AppDatabase.getDatabase(this);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        RecyclerView rv = findViewById(R.id.rvHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter();
        rv.setAdapter(adapter);

        String[] options = {"TODOS", "INSERT_TASK", "UPDATE_TASK", "DELETE_TASK"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spinnerFilter.setAdapter(spinnerAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = options[position];
                loadHistory(selected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadHistory(String filter) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<History> list;
            if (filter.equals("TODOS")) {
                list = database.historyDao().getAllHistory();
            } else {
                list = database.historyDao().getHistoryByAction(filter);
            }
            runOnUiThread(() -> adapter.setHistoryList(list));
        });
    }
}