package com.fic.Actividades; // CAMBIA ESTO POR TU PAQUETE

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "task_title")
    public String title;

    @ColumnInfo(name = "task_description")
    public String description;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @ColumnInfo(name = "is_completed")
    public boolean isCompleted;

    public Task() {}

    public Task(String title, String description, String createdAt, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.isCompleted = isCompleted;
    }
}