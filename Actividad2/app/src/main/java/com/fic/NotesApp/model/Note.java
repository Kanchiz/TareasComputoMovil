package com.fic.NotesApp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "category_id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    public int id;

    @ColumnInfo(name = "note_title")
    public String title;

    @ColumnInfo(name = "note_content")
    public String content;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @ColumnInfo(name = "category_id")
    public int categoryId;

    public Note() {}

    public Note(String title, String content, String createdAt, int categoryId) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
    }
}