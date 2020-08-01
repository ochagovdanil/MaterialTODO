package com.example.materialtodo.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.materialtodo.R;
import com.example.materialtodo.adapters.TasksListAdapter;
import com.example.materialtodo.models.Task;
import com.example.materialtodo.viewmodels.MainActivityViewModel;
import com.example.materialtodo.views.fragments.AskDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_TASK_REQUEST_CODE = 1;
    private static final int EDIT_TASK_REQUEST_CODE = 2;
    private static final String ASK_DIALOG_TAG = "AskDialogFragment";

    private MainActivityViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private TasksListAdapter mAdapter;
    private TextView mTextEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.main_menu_item_delete_all_tasks) {
            if (mAdapter.getItemCount() == 0) {
                showSnackbarMessage("Empty list");
                return false;
            }

            showAskDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == RESULT_OK) {
            if (requestCode == NEW_TASK_REQUEST_CODE) {
                String title = data.getStringExtra(NewTaskActivity.EXTRA_TITLE);
                String description = data.getStringExtra(NewTaskActivity.EXTRA_DESCRIPTION);

                Task task = new Task(title, description);
                mViewModel.insert(task);
            } else if (requestCode == EDIT_TASK_REQUEST_CODE) {
                int id = data.getIntExtra(EditTaskActivity.EXTRA_ID, -1);
                String title = data.getStringExtra(EditTaskActivity.EXTRA_TITLE);
                String description = data.getStringExtra(EditTaskActivity.EXTRA_DESCRIPTION);

                Task task = new Task(title, description);
                task.setId(id);

                mViewModel.update(task);
            } else {
                showToastMessage("Unknown request code: " + requestCode);
            }
        }
    }

    private void initApp() {
        setTitle(R.string.main_activity_title);

        mTextEmptyList = findViewById(R.id.text_view_empty_todo);

        initRecyclerView();
        initViewModel();
        retrieveData();
        swipeItemToDeleteTask();
        addNewTask();
        editTask();
    }

    private void initRecyclerView() {
        mAdapter = new TasksListAdapter();

        mRecyclerView = findViewById(R.id.recycler_view_tasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MainActivityViewModel.class);
    }

    private void retrieveData() {
        mViewModel.retrieveAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                showRecyclerView(tasks.size() != 0);
                mAdapter.submitList(tasks); // get all tasks from SQLite
            }
        });
    }

    private void swipeItemToDeleteTask() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // delete a swiped task
                Task task = mAdapter.getTaskAt(viewHolder.getAdapterPosition());
                mViewModel.delete(task);
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void addNewTask() {
        findViewById(R.id.floating_button_add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                startActivityForResult(intent, NEW_TASK_REQUEST_CODE);
            }
        });
    }

    private void editTask() {
        mAdapter.setOnItemClickListener(new TasksListAdapter.OnItemClickListener() {
            @Override
            public void onClick(Task task) {
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                intent.putExtra(EditTaskActivity.EXTRA_ID, task.getId());
                intent.putExtra(EditTaskActivity.EXTRA_TITLE, task.getTitle());
                intent.putExtra(EditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
                startActivityForResult(intent, EDIT_TASK_REQUEST_CODE);
            }
        });
    }

    private void deleteAllTasks() {
        mViewModel.deleteAllTasks();

        showSnackbarMessage("All tasks deleted");
    }

    private void showRecyclerView(boolean is) {
        if (is) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mTextEmptyList.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mTextEmptyList.setVisibility(View.VISIBLE);
        }
    }

    private void showSnackbarMessage(String msg) {
        Snackbar.make(findViewById(R.id.main_activity_layout), msg, Snackbar.LENGTH_SHORT)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                }).show(); // for user messages
    }

    private void showToastMessage(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show(); // for errors
    }

    private void showAskDialog() {
        AskDialogFragment dialog = new AskDialogFragment();
        dialog.show(getSupportFragmentManager(), ASK_DIALOG_TAG);

        dialog.setOnPositiveButtonClickListener(
                new AskDialogFragment.OnPositiveButtonClickListener() {
            @Override
            public void actionConfirmed() {
                deleteAllTasks();
            }
        });
    }
}
