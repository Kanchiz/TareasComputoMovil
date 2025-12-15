package com.fic.Actividades.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fic.Actividades.R;
import com.fic.Actividades.model.AppDatabase;
import com.fic.Actividades.model.History;
import com.fic.Actividades.model.Task;
import com.fic.Actividades.view.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = AppDatabase.getDatabase(this);

        Button btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TaskAdapter(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra("task_id", task.id);
                startActivity(intent);
            }

            @Override
            public void onItemDelete(Task task) {
                deleteTask(task);
            }

            @Override
            public void onTaskStatusChanged(Task task) {
                updateTaskStatus(task);
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddTask);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Ver Historial Auditoría");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadTasks() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Task> tasks = database.taskDao().getAllTasks();
            runOnUiThread(() -> adapter.setTasks(tasks));
        });
    }

    private void deleteTask(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            database.taskDao().deleteTask(task);

            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            History log = new History("DELETE_TASK", date, "Se eliminó la tarea: " + task.title);
            database.historyDao().insertHistory(log);

            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                loadTasks();
            });
        });
    }

    private void updateTaskStatus(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            database.taskDao().updateTask(task);

            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            String status = task.isCompleted ? "COMPLETADA" : "PENDIENTE";
            History log = new History("UPDATE_TASK", date, "Estado cambiado a " + status + ": " + task.title);
            database.historyDao().insertHistory(log);

        });
    }
}