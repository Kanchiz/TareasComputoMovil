package com.fic.Actividades.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.Actividades.R;
import com.fic.Actividades.model.AppDatabase;
import com.fic.Actividades.model.Task;
import com.fic.Actividades.view.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = AppDatabase.getDatabase(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Aquí inicializamos el adaptador con los 3 métodos requeridos
        adapter = new TaskAdapter(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                // Ir a Editar
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
                // Marcar checkbox
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

    private void loadTasks() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Task> tasks = database.taskDao().getAllTasks();
            runOnUiThread(() -> adapter.setTasks(tasks));
        });
    }

    private void deleteTask(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            database.taskDao().deleteTask(task);
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                loadTasks();
            });
        });
    }

    // Método auxiliar para actualizar solo el checkbox
    private void updateTaskStatus(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            database.taskDao().updateTask(task);
        });
    }
}