package com.fic.NotesApp.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fic.NotesApp.R;
import com.fic.NotesApp.model.AppDatabase;
import com.fic.NotesApp.model.Category;
import com.fic.NotesApp.model.Note;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private Spinner spinnerCategory;
    private AppDatabase database;
    private ArrayAdapter<Category> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        database = AppDatabase.getDatabase(this);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        Button btnSave = findViewById(R.id.btnSave);

        loadCategories();

        btnAddCategory.setOnClickListener(v -> showAddCategoryDialog());

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String content = etContent.getText().toString();
            Category selectedCategory = (Category) spinnerCategory.getSelectedItem();

            if (title.isEmpty() || selectedCategory == null) {
                Toast.makeText(this, "Falta título o categoría", Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            Note note = new Note(title, content, date, selectedCategory.id);

            AppDatabase.databaseWriteExecutor.execute(() -> {
                database.notesDao().insertNote(note);
                runOnUiThread(this::finish);
            });
        });
    }

    private void loadCategories() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Category> categories = database.notesDao().getAllCategories();
            runOnUiThread(() -> {
                categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
                spinnerCategory.setAdapter(categoryAdapter);
            });
        });
    }

    private void showAddCategoryDialog() {
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Nueva Categoría")
                .setView(input)
                .setPositiveButton("Crear", (dialog, which) -> {
                    String name = input.getText().toString();
                    if (!name.isEmpty()) {
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            database.notesDao().insertCategory(new Category(name));
                            loadCategories();
                        });
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
