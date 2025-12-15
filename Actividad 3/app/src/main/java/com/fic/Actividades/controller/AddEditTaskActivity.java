package com.fic.Actividades.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.fic.Actividades.R;
import com.fic.Actividades.model.AppDatabase;
import com.fic.Actividades.model.History;
import com.fic.Actividades.model.Task;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private AppDatabase database;
    private int taskId = -1;
    private boolean isEditMode = false;
    private String existingDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        database = AppDatabase.getDatabase(this);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        Button btnSave = findViewById(R.id.btnSave);

        if (getIntent().hasExtra("task_id")) {
            taskId = getIntent().getIntExtra("task_id", -1);
            loadTaskData(taskId);
            isEditMode = true;
        }

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void loadTaskData(int id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Task task = database.taskDao().getTaskById(id);
            runOnUiThread(() -> {
                if (task != null) {
                    etTitle.setText(task.title);
                    etDescription.setText(task.description);
                    existingDate = task.createdAt;
                }
            });
        });
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("El título es obligatorio");
            return;
        }

        String finalDate;
        if (isEditMode && existingDate != null) {
            finalDate = existingDate;
        } else {
            finalDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        }

        final Task task = new Task(title, description, finalDate, false);

        if (isEditMode) {
            task.id = taskId;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            String historyDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

            if (isEditMode) {
                database.taskDao().updateTask(task);

                History log = new History("UPDATE_TASK", historyDate, "Se editó el contenido de: " + title);
                database.historyDao().insertHistory(log);

            } else {
                database.taskDao().insertTask(task);

                History log = new History("INSERT_TASK", historyDate, "Se creó la tarea: " + title);
                database.historyDao().insertHistory(log);
            }

            runOnUiThread(this::finish);
        });
    }
}