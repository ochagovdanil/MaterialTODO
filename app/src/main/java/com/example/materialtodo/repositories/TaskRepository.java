package com.example.materialtodo.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.materialtodo.databases.TaskDatabase;
import com.example.materialtodo.models.Task;

import java.util.List;

// TODO: implement separate background threading later
public class TaskRepository {

    private TaskDatabase db;

    public TaskRepository(Application application) {
        db = TaskDatabase.getInstance(application);
    }

    public LiveData<List<Task>> retrieveAllTasks() {
        return db.taskDao().retrieveAllTasks();
    }

    public void deleteAllTasks() {
        db.taskDao().deleteAllTasks();
    }

    public void insert(Task task) {
        db.taskDao().insert(task);
    }

    public void update(Task task) {
        db.taskDao().update(task);
    }

    public void delete(Task task) {
        db.taskDao().delete(task);
    }
}
