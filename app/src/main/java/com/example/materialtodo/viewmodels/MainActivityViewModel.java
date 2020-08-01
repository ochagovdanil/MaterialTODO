package com.example.materialtodo.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.materialtodo.models.Task;
import com.example.materialtodo.repositories.TaskRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private TaskRepository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public LiveData<List<Task>> retrieveAllTasks() {
        return repository.retrieveAllTasks();
    }

    public void deleteAllTasks() {
        repository.deleteAllTasks();
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }
}
