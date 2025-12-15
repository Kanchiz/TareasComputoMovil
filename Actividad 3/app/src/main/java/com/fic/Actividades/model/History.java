package com.fic.Actividades.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class History {
    @PrimaryKey(autoGenerate = true)
    public int history_id;

    @ColumnInfo(name = "action")
    public String action;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @ColumnInfo(name = "details")
    public String details;

    public History(String action, String createdAt, String details) {
        this.action = action;
        this.createdAt = createdAt;
        this.details = details;
    }
}