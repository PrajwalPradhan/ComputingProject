package com.example.smartnotifier.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Todo todo);

    @Delete
    void deleteTodo(Todo todo);

    @Query("SELECT * FROM todos ORDER BY id DESC")
    LiveData<List<Todo>> getAllTodos();


    @Query("SELECT * from todos LIMIT 1")
    Todo[] getAnyTodo();

    @Update
    void update(Todo... todo);

}
