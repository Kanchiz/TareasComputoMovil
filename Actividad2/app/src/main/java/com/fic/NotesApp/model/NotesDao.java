package com.fic.NotesApp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface NotesDao {
    @Insert
    void insertNote(Note note);

    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE category_id = :catId ORDER BY created_at DESC")
    List<Note> getNotesByCategory(int catId);

    @Query("SELECT * FROM notes WHERE note_title LIKE '%' || :search || '%' OR note_content LIKE '%' || :search || '%'")
    List<Note> searchNotes(String search);

    @Insert
    void insertCategory(Category category);

    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();

    @Query("SELECT * FROM categories WHERE category_name = 'General' LIMIT 1")
    Category getGeneralCategory();
}
