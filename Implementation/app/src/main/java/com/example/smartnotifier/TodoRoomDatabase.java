package com.example.smartnotifier;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.smartnotifier.Database.Todo;
import com.example.smartnotifier.Database.TodoDao;

@Database(entities = {Todo.class}, version = 3, exportSchema = false)
public abstract class TodoRoomDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();

    private  static TodoRoomDatabase INSTANCE;

    public static TodoRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (TodoRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoRoomDatabase.class, "todos")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TodoDao mDao;

        // Initial data set
        private static String[] words = {"First Default Seeded Todo"};

        PopulateDbAsync(TodoRoomDatabase db) {
            mDao = db.todoDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // If we have no words, then create the initial list of words.
            if (mDao.getAnyTodo().length < 1) {
                for (int i = 0; i <= words.length - 1; i++) {
                    Todo word = new Todo(words[i],"test", "Test", "High", "Task");
                    mDao.insert(word);
                }
            }
            return null;
        }
    }
}
