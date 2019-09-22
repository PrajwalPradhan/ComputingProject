package com.example.smartnotifier.Database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos")
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name="date")
    private String date;


    @NonNull
    @ColumnInfo(name = "time")
    private String time;

    @NonNull
    @ColumnInfo(name = "priority")
    private String priority;

    @NonNull
    @ColumnInfo(name = "type")
    private String type;



    public Todo(@NonNull String title, @NonNull String date, @NonNull String time, @NonNull String priority, @NonNull String type) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.priority = priority;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getPriority() {
        return priority;
    }

    public void setPriority(@NonNull String priority) {
        this.priority = priority;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    @Ignore
    public Todo(int id, @NonNull String title, @NonNull String date, @NonNull String time, @NonNull String priority, @NonNull String type) {
        this.title = title;
        this.id = id;
        this.date = date;
        this.time = time;
        this.priority = priority;
        this.type = type;
    }

}
