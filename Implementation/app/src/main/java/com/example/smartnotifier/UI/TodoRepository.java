package com.example.smartnotifier.UI;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.smartnotifier.AppExecutors;
import com.example.smartnotifier.Database.Todo;
import com.example.smartnotifier.Database.TodoDao;
import com.example.smartnotifier.TodoRoomDatabase;

import java.util.List;

public class TodoRepository {
    private TodoDao todoDao;
    private LiveData<List<Todo>> allTodos;

    TodoRepository(Application application) {
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(application);
        todoDao = db.todoDao();
        allTodos = todoDao.getAllTodos();
    }

    LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }


    public void deleteTodo(final Todo todo) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                todoDao.deleteTodo(todo);
            }
        });
    }


    public void update(final Todo todo) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                todoDao.update(todo);
            }
        });
    }


    public void insert(final Todo todo) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                todoDao.insert(todo);
            }
        });
    }

}
