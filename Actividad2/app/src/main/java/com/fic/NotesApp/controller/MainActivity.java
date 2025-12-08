package com.fic.NotesApp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fic.NotesApp.R;
import com.fic.NotesApp.model.AppDatabase;
import com.fic.NotesApp.model.Category;
import com.fic.NotesApp.model.Note;
import com.fic.NotesApp.view.NotesAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;
    private NotesAdapter adapter;
    private Spinner spinnerFilter;
    private List<Category> categoriesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = AppDatabase.getDatabase(this);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter();
        recyclerView.setAdapter(adapter);

        spinnerFilter = findViewById(R.id.spinnerFilter);
        EditText etSearch = findViewById(R.id.etSearch);

        findViewById(R.id.fabAdd).setOnClickListener(v ->
                startActivity(new Intent(this, AddNoteActivity.class))
        );

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNotes(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category cat = categoriesList.get(position);
                if (cat.id == -1) {
                    loadNotes();
                } else {
                    filterNotesByCategory(cat.id);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategoriesAndNotes();
    }


    private void loadCategoriesAndNotes() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Category> cats = database.notesDao().getAllCategories();

            runOnUiThread(() -> adapter.setCategories(cats));

            Category all = new Category("Todas");
            all.id = -1;
            categoriesList.clear();
            categoriesList.add(all);
            categoriesList.addAll(cats);

            runOnUiThread(() -> {
                ArrayAdapter<Category> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriesList);
                spinnerFilter.setAdapter(spinnerAdapter);
                loadNotes();
            });
        });
    }

    private void loadNotes() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Note> notes = database.notesDao().getAllNotes();
            runOnUiThread(() -> adapter.setNotes(notes));
        });
    }

    private void filterNotesByCategory(int catId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Note> notes = database.notesDao().getNotesByCategory(catId);
            runOnUiThread(() -> adapter.setNotes(notes));
        });
    }

    private void searchNotes(String query) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Note> notes = database.notesDao().searchNotes(query);
            runOnUiThread(() -> adapter.setNotes(notes));
        });
    }
}