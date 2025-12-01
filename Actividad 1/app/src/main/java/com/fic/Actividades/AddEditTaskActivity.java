package com.fic.Actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private AppDatabase database;
    private int taskId = -1;
    private boolean isEditMode = false;
    private String existingDate = null; // Para guardar la fecha original si editamos

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
                    existingDate = task.createdAt; // Guardamos la fecha original
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

        // Lógica de fecha automática
        String finalDate;
        if (isEditMode && existingDate != null) {
            finalDate = existingDate; // Si editamos, mantenemos la fecha de creación original
        } else {
            // Si es nueva, generamos fecha de hoy
            finalDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        }

        final Task task = new Task(title, description, finalDate, false);

        if (isEditMode) {
            task.id = taskId;
            // Nota: Aquí podrías querer leer el estado "isCompleted" anterior si no quieres que se resetee a false
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (isEditMode) {
                database.taskDao().updateTask(task);
            } else {
                database.taskDao().insertTask(task);
            }
            runOnUiThread(this::finish);
        });
    }
}