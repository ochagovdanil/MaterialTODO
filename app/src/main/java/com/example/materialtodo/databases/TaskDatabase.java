package com.example.materialtodo.databases;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.materialtodo.dao.TaskDao;
import com.example.materialtodo.models.Task;

@Database(entities = Task.class, version = 1)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;

    public static synchronized TaskDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    application.getApplicationContext(),
                    TaskDatabase.class,
                    "tasks_database")
                        .allowMainThreadQueries() // TODO: implement separate background threading later

                        .build();
        }

        return instance;
    }

    public abstract TaskDao taskDao();
}
